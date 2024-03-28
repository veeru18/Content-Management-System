package com.example.cms.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class BlogRequest {

	@NotNull @NotBlank
	@Pattern(regexp = "^[a-zA-Z ]*$", message="please enter only alphabets for title names")
	private String title;
	@NotNull @NotBlank @NotEmpty
	private String[] topics;
	//can be null or blank
	private String about;
}
