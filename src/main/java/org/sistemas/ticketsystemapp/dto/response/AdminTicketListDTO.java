package org.sistemas.ticketsystemapp.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminTicketListDTO {

    private Long ticketId;
    private String ticketNumber;
    private String subject;
    private String category;
    private String priority;
    private String status;
    private String deviceId;
    private String assignedTeam;
    private LocalDateTime createdAt;
}

