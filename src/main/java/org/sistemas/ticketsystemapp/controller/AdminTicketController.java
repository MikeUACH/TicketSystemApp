package org.sistemas.ticketsystemapp.controller;


import lombok.RequiredArgsConstructor;
import org.sistemas.ticketsystemapp.dto.request.TicketFilterRequest;
import org.sistemas.ticketsystemapp.dto.response.AdminTicketListDTO;
import org.sistemas.ticketsystemapp.service.AdminTicketService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final AdminTicketService service;

    @PostMapping("/search")
    public Page<AdminTicketListDTO> search(@RequestBody TicketFilterRequest req) {
        return service.search(req);
    }
}

