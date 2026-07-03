package org.sistemas.ticketsystemapp.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ActiveTicketDTO {


    private Long ticketId;
    private String ticketNumber;

    private String subject;
    private String description;

    private String category;

    private String status;
    private String priority;

    private LocalDateTime createdAt;
    private List<AttachmentDTO> attachments;

}

