package org.sistemas.ticketsystemapp.controller;


import lombok.RequiredArgsConstructor;
import java.util.List;
import org.sistemas.ticketsystemapp.dto.request.CreateTicketRequest;
import org.sistemas.ticketsystemapp.dto.response.ActiveTicketDTO;
import org.sistemas.ticketsystemapp.dto.response.TicketResponse;
import org.sistemas.ticketsystemapp.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor

public class TicketController {

    private final TicketService service;

    @PostMapping
    public TicketResponse create(@RequestBody CreateTicketRequest req) {

        System.out.println("===== CONTROLLER HIT =====");
        System.out.println(req);

        return service.create(req);
    }

    @GetMapping("/active")
    public List<ActiveTicketDTO> getActive(@RequestParam String sessionToken) {
        return service.getActive(sessionToken);
    }

}



