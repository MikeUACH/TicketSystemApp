package org.sistemas.ticketsystemapp.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminTicketDetailDTO {

    private Long ticketId;
    private String ticketNumber;

    private String subject;
    private String description;

    private String category;
    private String priority;
    private String status;

    private String deviceId;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}

