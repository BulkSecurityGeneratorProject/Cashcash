package com.gagnepain.cashcash.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gagnepain.cashcash.config.Constants;

/**
 * A DTO representing a user's credentials
 */
public class LoginDTO {
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@NotNull
	@Size(min = 1,
			max = 50)
	private String username;

	@NotNull
	@Size(min = ManagedUserDTO.PASSWORD_MIN_LENGTH,
			max = ManagedUserDTO.PASSWORD_MAX_LENGTH)
	private String password;

	private Boolean rememberMe;

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(final Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		return "LoginDTO{" + "password='" + password + '\'' + ", username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
	}
}
