/**
 * 
 */
package com.printkaari.rest.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.FileDownloadException;
import com.printkaari.rest.exception.FileUploadException;
import com.printkaari.rest.exception.InvalidFieldLengthException;
import com.printkaari.rest.exception.MailNotSendException;
import com.printkaari.rest.exception.ProductNotFoundException;
import com.printkaari.rest.exception.SignUpException;
import com.printkaari.rest.exception.UserNotFoundException;
import com.printkaari.rest.form.ContactUSForm;
import com.printkaari.rest.form.SignUpStep2Form;

/**
 * @author Hemraj
 *
 */
public interface PrintStoreService {

	String completeSignup(SignUpStep2Form SignUpStep2Form) throws SignUpException, InvalidFieldLengthException;

	Long uploadFile(String fileType, MultipartFile file)
	        throws FileUploadException, UserNotFoundException;

	Map<String, Object> downloadCollegeProjectFiles(Long order_id) throws DatabaseException ,FileDownloadException;

	Map<String, Object> uploadProductSampleFile(String fileType, MultipartFile file, Long productId) throws FileUploadException, ProductNotFoundException;

	Object contactUS(ContactUSForm contactUsForm) throws  MailNotSendException, DatabaseException, UserNotFoundException;

}
