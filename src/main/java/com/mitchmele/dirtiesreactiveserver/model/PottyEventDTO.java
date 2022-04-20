package com.mitchmele.dirtiesreactiveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PottyEventDTO {

    private String id;
    private String type;
    private String description;
    private LocalDateTime pottyTime;
}
