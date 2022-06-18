package com.mitchmele.dirtiesreactiveserver.repository;

import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventDTO;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventNotFoundException;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import com.mitchmele.dirtiesreactiveserver.service.PottyEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PottyEventServiceHandler implements PottyEventService {

    private final PottyEventRepository repository;

    @Override
    public Mono<PottyEvent> getPottyEventByEventId(ObjectId eventId) {
        return repository.findByEventId(eventId)
                .switchIfEmpty(Mono.error(PottyEventNotFoundException::new));
    }

    @Override
    public Mono<PottyEvent> saveNewPotty(PottyEvent pottyEvent) {
        return repository.save(pottyEvent);
    }

    @Override
    public Flux<PottyEvent> getAllPotties() {
        return repository.findAll();
    }

    @Override
    public Mono<PottyServiceResponse> getAllRegPotties() {
        return repository.findAll()
                .collectList()
                .map(potties -> {
                    return potties.stream().map(this::mapEvent).collect(Collectors.toList());
                })
                .map(dtos -> PottyServiceResponse.builder().potties(dtos).build());
        }

    private PottyEventDTO mapEvent(PottyEvent event) {
        return PottyEventDTO.builder()
                .id(event.getId().toString())
                .type(event.getType())
                .description(event.getDescription())
                .pottyTime(event.getPottyTime())
                .build();
    }
}
