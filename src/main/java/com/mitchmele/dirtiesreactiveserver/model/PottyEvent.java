package com.mitchmele.dirtiesreactiveserver.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "PottyEvent")
public class PottyEvent {

    @Id
    private ObjectId id;

    private String type;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "America/Chicago")
    private LocalDateTime pottyTime;
}
