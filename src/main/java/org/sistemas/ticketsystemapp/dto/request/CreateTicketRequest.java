package org.sistemas.ticketsystemapp.dto.request;


import lombok.Data;

@Data
public class CreateTicketRequest {

    private String subject;
    private String category;
    private String description;
    private String priority;

    private String deviceId;
    private String sessionToken;
}

