package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {
	@NotBlank
    private String name;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "^\\d+$")
    private String cpf;
}
