package com.mitchmele.dirtiesreactiveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "PottyEvent")
public class PottyEvent {

    @Id
    private String eventId;
    private String type;
    private String description;
    private LocalDateTime pottyTime;
}
