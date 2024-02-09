package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {
	@NotBlank
    private String name;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "^\\d+$")
    @NotNull
    private String cpf;
}
