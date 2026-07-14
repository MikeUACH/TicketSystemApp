package org.sistemas.ticketsystemapp.controller;


import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import org.sistemas.ticketsystemapp.dto.request.CreateTicketRequest;
import org.sistemas.ticketsystemapp.dto.response.ActiveTicketDTO;
import org.sistemas.ticketsystemapp.dto.response.TicketResponse;
import org.sistemas.ticketsystemapp.service.TicketService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor

public class TicketController {

    private final TicketService service;


    @PostMapping(consumes = "multipart/form-data")
    public TicketResponse create(

            @RequestPart("ticket")
            CreateTicketRequest req,

            @RequestPart(value = "files", required = false)
            List<MultipartFile> files

    ) throws IOException {

        return service.create(req, files);
    }


    @GetMapping("/active")
    public List<ActiveTicketDTO> getActive(@RequestParam String sessionToken) {
        return service.getActive(sessionToken);
    }

}



