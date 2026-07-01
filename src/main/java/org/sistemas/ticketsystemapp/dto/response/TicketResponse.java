package org.sistemas.ticketsystemapp.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TicketResponse {

    private Long ticketId;
    private String ticketNumber;
    private String status;
    private String accessToken;
    private LocalDateTime createdAt;

}

