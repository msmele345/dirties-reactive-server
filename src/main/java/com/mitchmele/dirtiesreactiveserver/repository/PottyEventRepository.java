package com.mitchmele.dirtiesreactiveserver.repository;

import com.mitchmele.dirtiesreactiveserver.model.PottyEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PottyEventRepository extends ReactiveMongoRepository<PottyEvent, String> {

    Mono<PottyEvent> findPottyEventById(ObjectId id);
}
