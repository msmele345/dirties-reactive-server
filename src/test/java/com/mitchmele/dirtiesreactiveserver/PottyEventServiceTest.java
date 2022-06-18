package com.mitchmele.dirtiesreactiveserver;

import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventDTO;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventNotFoundException;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventServiceHandler;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PottyEventServiceTest {

    @Mock
    private PottyEventRepository repository;

    @InjectMocks
    private PottyEventServiceHandler service;

    private LocalDateTime mockTime;
    private LocalDateTime mockTime2;

    @BeforeEach
    void setUp() {
        mockTime = LocalDateTime.of(2, 1, 1, 1, 1);
        mockTime2 = LocalDateTime.of(2, 1, 1, 2, 1);
    }

    @Test
    void getPottyEventByEventId() {

        LocalDateTime mockTime = LocalDateTime.of(2, 1, 1, 1, 1);

        ObjectId inputId = new ObjectId();

        PottyEvent event = PottyEvent.builder()
                .id(inputId)
                .pottyTime(mockTime)
                .type("wet")
                .build();

        when(repository.findPottyEventById(any(ObjectId.class)))
                .thenReturn(Mono.just((event)));

        Mono<PottyEvent> actual = service.getPottyEventByEventId(inputId);

        StepVerifier
                .create(actual)
                .consumeNextWith(potty -> {
                    assertThat(potty.getType()).isEqualTo("wet");
                })
                .verifyComplete();

        verify(repository).findPottyEventById(inputId);
    }

    @Test
    void getAllPotties() {

        LocalDateTime mockTime = LocalDateTime.of(2, 1, 1, 1, 1);
        LocalDateTime mockTime2 = LocalDateTime.of(2, 1, 1, 2, 1);

        PottyEvent event = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(mockTime)
                .type("wet")
                .build();

        PottyEvent event2 = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(mockTime2)
                .type("dirty")
                .build();

        when(repository.findAll()).thenReturn(Flux.just(event, event2));

        Flux<PottyEvent> pottyFlux = service.getAllPotties();

        StepVerifier
                .create(pottyFlux)
                .expectNext(event)
                .assertNext(pottyEvent -> {
                    assertThat(pottyEvent).isEqualTo(event2);
                })
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void getByPottyId_throwsWhenNotPresent() {

        when(repository.findPottyEventById(any()))
                .thenReturn(Mono.empty());

        Mono<PottyEvent> actual = service.getPottyEventByEventId(new ObjectId());

        StepVerifier
                .create(actual)
                .expectErrorMatches(t ->  t instanceof PottyEventNotFoundException)
                .verify();

    }

    @Test
    void getAllPottiesFromFluxToServiceResponse() {

        ObjectId id = new ObjectId();
        PottyEvent event = PottyEvent.builder()
                .id(id)
                .pottyTime(mockTime)
                .type("wet")
                .build();

        PottyEvent event2 = PottyEvent.builder()
                .id(id)
                .pottyTime(mockTime2)
                .type("dirty")
                .build();

        PottyEventDTO ex1 = PottyEventDTO.builder()
                .id(id.toString())
                .pottyTime(mockTime)
                .type("wet")
                .build();

        PottyEventDTO ex2 = PottyEventDTO.builder()
                .id(id.toString())
                .pottyTime(mockTime2)
                .type("dirty")
                .build();

        when(repository.findAll())
                .thenReturn(Flux.just(event, event2));

        PottyServiceResponse ex = PottyServiceResponse.builder().potties(asList(ex1, ex2)).build();

        Mono<PottyServiceResponse> actual = service.getAllRegPotties();

        StepVerifier
                .create(actual)
                .expectNext(ex)
                .verifyComplete();
    }

    @Test
    void savePotty() {

        PottyEvent event = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(mockTime)
                .type("wet")
                .build();

        when(repository.save(any())).thenReturn(Mono.just(event));

        Mono<PottyEvent> actual = service.saveNewPotty(event);

        StepVerifier
                .create(actual)
                .expectNext(event)
                .verifyComplete();

        verify(repository).save(event);
    }
}