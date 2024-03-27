package com.example.cms.requestdto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	@NotNull
	@NotBlank
	private String userName;
	@Email(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$",message = "please enter valid email with @ and .")
	@Column(unique = true)
	@NotBlank @NotNull
	private String email;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one letter, one number, one special character")
	@NotNull @NotBlank
	private String password;
}
