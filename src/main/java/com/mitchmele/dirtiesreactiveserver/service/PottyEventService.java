package com.mitchmele.dirtiesreactiveserver.service;


import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PottyEventService {

    Mono<PottyEvent> getPottyEventByEventId(ObjectId eventId);

    Mono<PottyEvent> saveNewPotty(PottyEvent pottyEvent);

    Flux<PottyEvent> getAllPotties();

    Mono<PottyServiceResponse> getAllRegPotties();
}
