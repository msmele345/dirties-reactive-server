package com.mitchmele.dirtiesreactiveserver.util;

import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import com.mitchmele.dirtiesreactiveserver.repository.PottyEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializerService implements ApplicationListener<ApplicationReadyEvent> {

    private final PottyEventRepository repository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        PottyEvent pe = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(LocalDateTime.now().minusDays(1))
                .type("wet")
                .description("bad")
                .build();

        PottyEvent pe2 = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(LocalDateTime.now().minusDays(2))
                .type("dirty")
                .description("blow out")
                .build();

        PottyEvent pe3 = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(LocalDateTime.now().minusDays(3))
                .type("dirty")
                .description("mild")
                .build();

        PottyEvent p4 = PottyEvent.builder()
                .id(new ObjectId())
                .pottyTime(LocalDateTime.now().minusDays(5))
                .type("wet")
                .description("odd color")
                .build();

        repository.deleteAll()
                .thenMany(Flux.just(pe, pe2, pe3, p4).flatMap(repository::save))
                .thenMany(repository.findAll())
                .subscribe(potty -> log.info("saving " + potty.toString()));

    }
}
