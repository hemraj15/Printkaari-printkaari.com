/**
 * 
 */
package com.printkaari.rest.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.utils.ValidationUtils;

/**
 * @author Hemraj
 *
 */
public class ContactUSForm {
	
	
	private String name;
	@NotNull(message = ErrorCodes.ENQUIRY_USER_ID_NULL)
	@NotEmpty(message = ErrorCodes.ENQUIRY_USER_ID_EMPTY)
	@Email(message = ErrorCodes.ENQUIRY_EMAIL_INVALID, regexp = ValidationUtils.EMAIL_PATTERN)
	private String email;
	private String adress;
	private String subjectMatter;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}
	/**
	 * @param adress the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}
	/**
	 * @return the subjectMatter
	 */
	public String getSubjectMatter() {
		return subjectMatter;
	}
	/**
	 * @param subjectMatter the subjectMatter to set
	 */
	public void setSubjectMatter(String subjectMatter) {
		this.subjectMatter = subjectMatter;
	}
	
	

}
