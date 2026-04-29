package com.project.revhive.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserGrowthDTO {
    private String date;
    private long count;
}
