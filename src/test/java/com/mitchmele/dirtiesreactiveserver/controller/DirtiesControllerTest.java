package com.mitchmele.dirtiesreactiveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.model.PottyEventDTO;
import com.mitchmele.dirtiesreactiveserver.model.PottyServiceResponse;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventServiceHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = DirtiesController.class)
//@SpringBootTest
//@AutoConfigureWebTestClient
//@Import(PottyEventService.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DirtiesControllerTest {

    @MockBean
    private PottyEventRepository repository;

    @MockBean
    private PottyEventServiceHandler service;

    @Autowired
    private WebTestClient webTestClient;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllPotties() {

        PottyEvent pe = PottyEvent.builder()
                .eventId("1")
                .pottyTime(LocalDateTime.now())
                .type("wet")
                .build();

        PottyEvent pe2 = PottyEvent.builder()
                .eventId("2")
                .pottyTime(LocalDateTime.now())
                .type("dirty")
                .build();

        Flux<PottyEvent> potties = Flux.just(pe, pe2);
        when(repository.findAll()).thenReturn(potties);

        webTestClient
                .get()
                .uri("/api/v1/dirties")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PottyEvent.class);

        verify(repository).findAll();
    }

    @Test
    void getPottyEventByID() {

        PottyEvent pe = PottyEvent.builder()
                .eventId("1")
                .pottyTime(LocalDateTime.now())
                .type("wet")
                .build();

        when(repository.findById(anyString())).thenReturn(Mono.just(pe));

        webTestClient
                .get()
                .uri("/api/v1/dirties/{id}", Collections.singletonMap("id", "1"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isNotNull();
                });
    }

    @Test
    void getAllRegPotties() {

        PottyEventDTO p1 = PottyEventDTO.builder()
                .id("1")
                .pottyTime(LocalDateTime.now())
                .type("wet")
                .build();

        PottyEventDTO p2 = PottyEventDTO.builder()
                .id("2")
                .pottyTime(LocalDateTime.now())
                .type("dirty")
                .build();

        PottyServiceResponse ex = PottyServiceResponse.builder().potties(asList(p1, p2)).build();

        when(service.getAllRegPotties()).thenReturn(Mono.just(ex));

        webTestClient
                .get()
                .uri("/api/v1/regs")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PottyEventDTO.class);

        verify(service).getAllRegPotties();
    }

    @Test
    void savePotty()  {

        PottyEvent pe = PottyEvent.builder()
                .eventId("1")
                .pottyTime(LocalDateTime.now())
                .type("wet")
                .build();

        when(service.saveNewPotty(any())).thenReturn(Mono.just(pe));

        webTestClient
                .post()
                .uri("/api/v1/dirties")
                .bodyValue(pe)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isNotNull();
                });
    }
}