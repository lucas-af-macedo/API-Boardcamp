package com.boardcamp.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalDTO {
	@Positive
    @NotNull
    private Long customerId;

    @Positive
    @NotNull
    private Long gameId;

	@Min(1)
    @NotNull
    private Long daysRented;
}
