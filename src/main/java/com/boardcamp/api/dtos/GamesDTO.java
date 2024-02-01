package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GamesDTO {
	@NotBlank
    private String name;

    @NotNull
    private String image;

    @Positive
    private Long stockTotal;

    @Positive
    private Long pricePerDay;
}