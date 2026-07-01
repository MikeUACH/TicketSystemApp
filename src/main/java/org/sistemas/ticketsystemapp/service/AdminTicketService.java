package org.sistemas.ticketsystemapp.service;

import lombok.RequiredArgsConstructor;
import org.sistemas.ticketsystemapp.dto.request.TicketFilterRequest;
import org.sistemas.ticketsystemapp.dto.response.AdminTicketListDTO;
import org.sistemas.ticketsystemapp.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminTicketService {

    private final TicketRepository repo;

    public Page<AdminTicketListDTO> search(TicketFilterRequest filter) {

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        return repo.findByStatusContainingAndPriorityContainingAndCategoryContaining(
                filter.getStatus() != null ? filter.getStatus() : "",
                filter.getPriority() != null ? filter.getPriority() : "",
                filter.getCategory() != null ? filter.getCategory() : "",
                pageable
        ).map(t -> AdminTicketListDTO.builder()
                .ticketId(t.getTicketId())
                .ticketNumber(t.getTicketNumber())
                .subject(t.getSubject())
                .category(t.getCategory())
                .priority(t.getPriority().name())
                .status(t.getStatus().name())
                .deviceId(t.getDeviceId())
                .assignedTeam(t.getAssignedTeam())
                .createdAt(t.getCreatedAt())
                .build()
        );
    }
}