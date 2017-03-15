package com.printkaari.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.printkaari.auth.service.SystemRoles;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.FileUploadException;
import com.printkaari.rest.exception.InvalidFieldLengthException;
import com.printkaari.rest.exception.SignUpException;
import com.printkaari.rest.form.SignUpStep1Form;
import com.printkaari.rest.form.SignUpStep2Form;
import com.printkaari.rest.model.ErrorResponse;
import com.printkaari.rest.model.SignUpErrorResponse;
import com.printkaari.rest.service.PrintStoreService;
import com.printkaari.rest.service.UserService;
import com.printkaari.rest.utils.ErrorUtils;
import com.printkaari.rest.utils.PasswordUtils;

@RestController
@RequestMapping("/signup")
public class SignUpController {

	private Logger				LOGGER	= LoggerFactory.getLogger(SignUpController.class);

	@Autowired
	private UserService			userService;

	@Autowired
	private PrintStoreService	printStoreService;

	@ResponseBody
	// @Consumes("application/json")
	@RequestMapping(value = "/initiate", method = RequestMethod.POST, consumes = "application/json")

	public Object initiate(@RequestBody @Valid SignUpStep1Form signUpstep1Form,
	        BindingResult result, HttpServletResponse response) {
		Object data = null;
		if (result.hasErrors()) {
			String message = ErrorUtils.getTextValidationErrorMessage(result.getAllErrors());
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(message);
			((ErrorResponse) data).setMessage("Form validation failed!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				userService.initiateSignUp(signUpstep1Form);
				Map<String, String> dataMap = new HashMap<>();
				dataMap.put("emailToken", PasswordUtils.encode(signUpstep1Form.getEmail()));
				dataMap.put("message",
				        "Sign Up Initiated successfully, please check your email to proceed further.");
				data = dataMap;
				response.setStatus(HttpServletResponse.SC_CREATED);

			} catch (SignUpException e) {
				SignUpErrorResponse errorResponse = new SignUpErrorResponse();
				errorResponse.setErrorCode(e.getErrorCode());
				errorResponse.setMessage(e.getMessage());
				switch (e.getErrorCode()) {
				case ErrorCodes.SIGNUP_ALREADY_INITIATED:
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					errorResponse.setEmailToken(PasswordUtils.encode(signUpstep1Form.getEmail()));
					break;
				case ErrorCodes.SIGNUP_ALREADY_ACTIVE:
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					break;
				default:
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					break;
				}
				data = errorResponse;
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			}
		}
		return data;

	}

	@ResponseBody
	@RequestMapping(value = "/complete", method = RequestMethod.POST, consumes = "application/json")
	public Object complete(@RequestBody @Valid SignUpStep2Form signUpStep2Form,
	        BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		Object data = null;
		if (result.hasErrors()) {
			String message = ErrorUtils.getTextValidationErrorMessage(result.getAllErrors());
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(message);
			((ErrorResponse) data).setMessage("Form validation failed!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				String tempPassword = printStoreService.completeSignup(signUpStep2Form);
				
				LOGGER.info("temp pasword " + tempPassword);
				LOGGER.info("email token" + signUpStep2Form.getEmailToken());
				data = userService.autoLoginUser(signUpStep2Form.getEmailToken(), tempPassword);
				response.setStatus(HttpServletResponse.SC_CREATED);
			} catch (SignUpException e) {
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(e.getErrorCode());
				((ErrorResponse) data).setMessage(e.getMessage());
				switch (e.getErrorCode()) {
				case ErrorCodes.SIGNUP_ALREADY_ACTIVE:
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					break;
				case ErrorCodes.SIGNUP_ACCOUNT_DEACTIVATED:
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					break;
				default:
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					break;
				}
			} catch (InvalidFieldLengthException e) {
				LOGGER.error(e.getMessage(), e);
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
		return data;
	}

	@ResponseBody
	@RequestMapping(value = "/resend-email/{token}", method = RequestMethod.GET)
	public Object resendEmail(@PathVariable String token, HttpServletResponse response) {
		Object data = new ErrorResponse();
		try {
			userService.resendEmail(token);
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("message", "Resend email successfully!");
			data = dataMap;
		} catch (SignUpException e) {
			((ErrorResponse) data).setErrorCode(e.getErrorCode());
			((ErrorResponse) data).setMessage(e.getMessage());
			switch (e.getErrorCode()) {
			case ErrorCodes.SIGNUP_ALREADY_ACTIVE:
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				break;
			case ErrorCodes.SIGNUP_ACCOUNT_DEACTIVATED:
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				break;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;

	}

	

	@ResponseBody
	@RequestMapping(value = "/email", method = RequestMethod.GET)
//	@Secured({SystemRoles.ROLE_CUSTOMER})
	@Secured({SystemRoles.CUSTOMER})
	public Object getEmailUser()
	
	{
		Object data = null;
		data = userService.getEmail();
		return data;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/emailWoAuth", method = RequestMethod.GET)
//	@Secured({SystemRoles.ROLE_CUSTOMER})
	public Object getEmailUserWOAuth()
	
	{
		Object data = null;
		data = userService.getEmail();
		return data;
	}
}
