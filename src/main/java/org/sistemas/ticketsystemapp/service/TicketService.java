package org.sistemas.ticketsystemapp.service;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.sistemas.ticketsystemapp.dto.request.CreateTicketRequest;
import org.sistemas.ticketsystemapp.dto.response.ActiveTicketDTO;
import org.sistemas.ticketsystemapp.dto.response.TicketResponse;
import org.sistemas.ticketsystemapp.entity.Priority;
import org.sistemas.ticketsystemapp.entity.Ticket;
import org.sistemas.ticketsystemapp.entity.TicketStatus;
import org.sistemas.ticketsystemapp.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;

    public TicketResponse create(CreateTicketRequest req) {

        Ticket ticket = Ticket.builder()
                .subject(req.getSubject())
                .category(req.getCategory())
                .description(req.getDescription())
                .priority(Priority.valueOf(req.getPriority().toUpperCase()))
                .status(TicketStatus.OPEN)
                .deviceId(req.getDeviceId())
                .sessionToken(req.getSessionToken())
                .accessToken(UUID.randomUUID().toString())
                .ticketNumber("INC-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(ticket);

        return TicketResponse.builder()
                .ticketId(ticket.getTicketId())
                .ticketNumber(ticket.getTicketNumber())
                .status(ticket.getStatus().name())
                .accessToken(ticket.getAccessToken())
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    public List<ActiveTicketDTO> getActive(String sessionToken) {
        return repo.findBySessionTokenAndStatusNotIn(
                sessionToken,
                List.of(TicketStatus.RESOLVED, TicketStatus.CLOSED)
        ).stream().map(t -> ActiveTicketDTO.builder()
                .ticketId(t.getTicketId())
                .ticketNumber(t.getTicketNumber())
                .subject(t.getSubject())
                .status(t.getStatus().name())
                .priority(t.getPriority().name())
                .createdAt(t.getCreatedAt())
                .build()
        ).toList();
    }
}
