/**
 * 
 */
package com.printkaari.rest.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.printkaari.rest.constant.ErrorCodes;

/**
 * @author Hemraj
 *
 */
public class LoginForm {
	
	@NotNull(message = ErrorCodes.LOGIN_USER_ID_NULL)
	@NotEmpty(message = ErrorCodes.LOGIN_USER_ID_EMPTY)
	private String username;
	
	@NotNull(message = ErrorCodes.LOGIN_USER_PASSWORD_NULL)
	@NotEmpty(message = ErrorCodes.LOGIN_USER_PASSWORD_EMPTY)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}