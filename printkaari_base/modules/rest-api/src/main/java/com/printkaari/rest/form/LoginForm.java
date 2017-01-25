/**
 * 
 */
package com.printkaari.rest.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.printkaari.rest.constant.ErrorCodes;

/**
 * @author Hemraj
 *
 */
public class LoginForm {
	
	@NotNull(message = ErrorCodes.LOGIN_USER_ID_NULL)
	@NotBlank(message = ErrorCodes.LOGIN_USER_ID_EMPTY)
	private String username;
	
	@NotNull(message = ErrorCodes.LOGIN_USER_PASSWORD_NULL)
	@NotBlank(message = ErrorCodes.LOGIN_USER_PASSWORD_EMPTY)
	private String password;
	
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginForm [userId=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}
	
	

}
