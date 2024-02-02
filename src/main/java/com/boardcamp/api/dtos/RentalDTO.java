package com.boardcamp.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalDTO {
	@Positive
    private Long customerId;

    @Positive
    private Long gameId;

	@Min(1)
    private Long daysRented;
}
