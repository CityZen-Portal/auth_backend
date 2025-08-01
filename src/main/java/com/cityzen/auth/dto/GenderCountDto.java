package com.cityzen.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenderCountDto {
    private int maleCount;
    private int femaleCount;
    private int othersCount;
}
