package com.example.cms.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PublishRequest {

	@NotNull(message = "must enter title") @NotBlank(message = "please enter title") @NotEmpty(message = "don't leave title as empty fieldstring")
	private String seoTitle;
	private String seoDescription;
	@NotBlank(message = "don't leave tags as blank field")
	private String[] seoTags;
	private ScheduleRequest schedule;
}
