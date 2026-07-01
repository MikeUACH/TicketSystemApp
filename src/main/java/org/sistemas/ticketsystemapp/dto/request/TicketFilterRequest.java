package org.sistemas.ticketsystemapp.dto.request;


import lombok.Data;

@Data
public class TicketFilterRequest {

    private String status;
    private String priority;
    private String category;

    private int page = 0;
    private int size = 10;
}

