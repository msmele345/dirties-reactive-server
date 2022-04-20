package com.mitchmele.dirtiesreactiveserver.service;


import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventDTO;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PottyEventService {

    Mono<PottyEvent> getPottyEvent(String eventId);
    Mono<PottyEvent> getPottyEventByEventId(String eventId);

    Mono<PottyEvent> saveNewPotty(PottyEvent pottyEvent);

    Flux<PottyEvent> getAllPotties();

    Mono<PottyServiceResponse> getAllRegPotties();
}
