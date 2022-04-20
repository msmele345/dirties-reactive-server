package com.mitchmele.dirtiesreactiveserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PottyServiceResponse {

    private List<PottyEventDTO> potties;

}
