package org.sistemas.ticketsystemapp.dto.request;

import lombok.Data;

@Data
public class UpdateTicketRequest {

    private String subject;

    private String category;

    private String description;

    private String priority;
}