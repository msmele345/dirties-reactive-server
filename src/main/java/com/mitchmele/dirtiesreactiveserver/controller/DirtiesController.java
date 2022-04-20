package com.mitchmele.dirtiesreactiveserver.controller;

import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventDTO;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventServiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DirtiesController {

    private final PottyEventServiceHandler service;
    private final PottyEventRepository repository;

    @GetMapping("/dirties/{id}")
    public Mono<ResponseEntity<PottyEvent>> getPottyEventById(@PathVariable String id) {

        return repository.findByEventId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/dirties")
    public Flux<PottyEvent> getAllPotties() {
        return repository.findAll();
    }

    @GetMapping(value = "/regs")
    public Mono<PottyServiceResponse> getAllRegPotties() {
        return service.getAllRegPotties();
    }

    @GetMapping(value = "/stream/dirties", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PottyEvent> getStreamDirties() {
        return service.getAllPotties();
    }

    @PostMapping("/dirties")
    public Mono<PottyEvent> savePotty(@RequestBody PottyEvent pottyEvent) {
        return service.saveNewPotty(pottyEvent);
    }
}
