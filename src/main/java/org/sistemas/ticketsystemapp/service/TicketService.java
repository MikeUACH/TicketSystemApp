package org.sistemas.ticketsystemapp.service;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.sistemas.ticketsystemapp.dto.request.CreateTicketRequest;
import org.sistemas.ticketsystemapp.dto.response.ActiveTicketDTO;
import org.sistemas.ticketsystemapp.dto.response.AttachmentDTO;
import org.sistemas.ticketsystemapp.dto.response.TicketResponse;
import org.sistemas.ticketsystemapp.entity.Priority;
import org.sistemas.ticketsystemapp.entity.Ticket;
import org.sistemas.ticketsystemapp.entity.TicketAttachment;
import org.sistemas.ticketsystemapp.entity.TicketStatus;
import org.sistemas.ticketsystemapp.repository.TicketAttachmentRepository;
import org.sistemas.ticketsystemapp.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;
    private final TicketAttachmentRepository attachmentRepo;

    public TicketResponse create(
            CreateTicketRequest req,
            List<MultipartFile> files
    )
    {

        Ticket ticket = Ticket.builder()
                .subject(req.getSubject())
                .category(req.getCategory())
                .description(req.getDescription())
                .priority(Priority.valueOf(req.getPriority().toUpperCase()))
                .status(TicketStatus.ABIERTO)
                .deviceId(req.getDeviceId())
                .sessionToken(req.getSessionToken())
                .accessToken(UUID.randomUUID().toString())
                .ticketNumber("INC-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(ticket);


        if (files != null) {

            for (MultipartFile file : files) {

                TicketAttachment attachment =
                        new TicketAttachment();

                attachment.setTicketId(
                        ticket.getTicketId());

                attachment.setFileName(
                        file.getOriginalFilename());

                attachment.setFileType(
                        file.getContentType());

                attachment.setFileUrl(
                        "/uploads/" +
                                file.getOriginalFilename());

                attachmentRepo.save(attachment);
            }
        }

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
                List.of(TicketStatus.RESUELTO, TicketStatus.CERRADO)
        ).stream().map(t -> {

            List<AttachmentDTO> attachments =
                    attachmentRepo.findByTicketId(t.getTicketId())
                            .stream()
                            .map(a -> AttachmentDTO.builder()
                                    .id(a.getId())
                                    .fileName(a.getFileName())
                                    .fileType(a.getFileType())
                                    .fileUrl(a.getFileUrl())
                                    .build())
                            .toList();

            return ActiveTicketDTO.builder()
                    .ticketId(t.getTicketId())
                    .ticketNumber(t.getTicketNumber())
                    .subject(t.getSubject())
                    .description(t.getDescription())
                    .category(t.getCategory())
                    .status(t.getStatus().name())
                    .priority(t.getPriority().name())
                    .createdAt(t.getCreatedAt())
                    .attachments(attachments)
                    .build();

        }).toList();
    }
}
