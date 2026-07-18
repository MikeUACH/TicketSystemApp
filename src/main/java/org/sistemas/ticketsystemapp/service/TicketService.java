package org.sistemas.ticketsystemapp.service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.sistemas.ticketsystemapp.dto.request.CreateTicketRequest;
import org.sistemas.ticketsystemapp.dto.response.ActiveTicketDTO;
import org.sistemas.ticketsystemapp.dto.response.AttachmentDTO;
import org.sistemas.ticketsystemapp.dto.response.TicketResponse;
import org.sistemas.ticketsystemapp.dto.request.UpdateTicketRequest;
import org.sistemas.ticketsystemapp.entity.Priority;
import org.sistemas.ticketsystemapp.entity.Ticket;
import org.sistemas.ticketsystemapp.entity.TicketAttachment;
import org.sistemas.ticketsystemapp.entity.TicketStatus;
import org.sistemas.ticketsystemapp.repository.TicketAttachmentRepository;
import org.sistemas.ticketsystemapp.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository repo;
    private final TicketAttachmentRepository attachmentRepo;
    public TicketResponse create(
            CreateTicketRequest req,
            List<MultipartFile> files
    ) throws IOException
    {
        int nextNumber = 1;
        Optional<Ticket> lastTicket = repo.findTopByOrderByTicketIdDesc();
        if (lastTicket.isPresent()){
            String lastTicketNumber = lastTicket.get().getTicketNumber();

            nextNumber = Integer.parseInt(
                    lastTicketNumber.replace("INC-", "")
            ) + 1;
        }
        Ticket ticket = Ticket.builder()
                .subject(req.getSubject())
                .category(req.getCategory())
                .description(req.getDescription())
                .priority(Priority.valueOf(req.getPriority().toUpperCase()))
                .status(TicketStatus.ABIERTO)
                .deviceId(req.getDeviceId())
                .sessionToken(req.getSessionToken())
                .accessToken(UUID.randomUUID().toString())
                .ticketNumber("INC-" + "00"+ nextNumber)
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(ticket);


        if (files != null) {

            Path uploadDir = Paths.get(System.getProperty("user.dir"),"uploads");

            System.out.println("UPLOAD DIR: " + uploadDir.toAbsolutePath());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            for (MultipartFile file : files) {

                String fileName = file.getOriginalFilename();

                Path destination =
                        uploadDir.resolve(fileName);

                file.transferTo(destination.toFile());

                TicketAttachment attachment =
                        new TicketAttachment();

                attachment.setTicketId(
                        ticket.getTicketId());

                attachment.setFileName(
                        fileName);

                attachment.setFileType(
                        file.getContentType());

                attachment.setFileUrl(
                        "/uploads/" + fileName);

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
                List.of(TicketStatus.CERRADO)
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

    public TicketResponse updateTicket(
            Long ticketId,
            UpdateTicketRequest req,
            List<MultipartFile> files
    ) throws IOException {

        Ticket ticket = repo.findById(ticketId)
                .orElseThrow();

        ticket.setSubject(req.getSubject());
        ticket.setCategory(req.getCategory());
        ticket.setDescription(req.getDescription());

        ticket.setPriority(
                Priority.valueOf(
                        req.getPriority().toUpperCase()
                )
        );

        if (files != null && !files.isEmpty()){

            List<TicketAttachment> oldAttachments =
                    attachmentRepo.findByTicketId(ticketId);

            attachmentRepo.deleteAll(oldAttachments);

            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        }
        // Replace attachments here if files != null

        repo.save(ticket);

        return TicketResponse.builder()
                .ticketId(ticket.getTicketId())
                .ticketNumber(ticket.getTicketNumber())
                .status(ticket.getStatus().name())
                .accessToken(ticket.getAccessToken())
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}
