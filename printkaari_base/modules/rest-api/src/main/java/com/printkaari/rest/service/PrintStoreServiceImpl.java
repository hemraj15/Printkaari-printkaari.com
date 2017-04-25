/**
 * 
 */
package com.printkaari.rest.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.printkaari.auth.service.SystemRoles;
import com.printkaari.data.dao.CityDao;
import com.printkaari.data.dao.CountryDao;
import com.printkaari.data.dao.CustomerDao;
import com.printkaari.data.dao.CustomerFileDao;
import com.printkaari.data.dao.EmployeeDao;
import com.printkaari.data.dao.OrderDao;
import com.printkaari.data.dao.PrintStoreDao;
import com.printkaari.data.dao.ProductDao;
import com.printkaari.data.dao.ProductSampleDao;
import com.printkaari.data.dao.RoleDao;
import com.printkaari.data.dao.SampleFileRecordDao;
import com.printkaari.data.dao.StateDao;
import com.printkaari.data.dao.UrlTypeDao;
import com.printkaari.data.dao.UserDao;
import com.printkaari.data.dao.entity.Address;
import com.printkaari.data.dao.entity.City;
import com.printkaari.data.dao.entity.Country;
import com.printkaari.data.dao.entity.Customer;
import com.printkaari.data.dao.entity.CustomerFiles;
import com.printkaari.data.dao.entity.Employee;
import com.printkaari.data.dao.entity.CustOrder;
import com.printkaari.data.dao.entity.Product;
import com.printkaari.data.dao.entity.ProductSamples;
import com.printkaari.data.dao.entity.Role;
import com.printkaari.data.dao.entity.SampleFileRecord;
import com.printkaari.data.dao.entity.State;
import com.printkaari.data.dao.entity.User;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.message.utils.ReadConfigurationFile;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.UserStatus;
import com.printkaari.rest.constant.UserTypes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.FileDownloadException;
import com.printkaari.rest.exception.FileUploadException;
import com.printkaari.rest.exception.InvalidFieldLengthException;
import com.printkaari.rest.exception.InvalidUserTypeException;
import com.printkaari.rest.exception.ProductNotFoundException;
import com.printkaari.rest.exception.SignUpException;
import com.printkaari.rest.exception.UserNotFoundException;
import com.printkaari.rest.form.SignUpStep2Form;
import com.printkaari.rest.utils.FileUtils;
import com.printkaari.rest.utils.PasswordUtils;
import com.printkaari.rest.utils.ValidationUtils;

/**
 * @author Developer
 *
 */

@Service
public class PrintStoreServiceImpl implements PrintStoreService {

	private static String		BASE_UPLOAD_PATH	= "";

	private Logger				LOGGER				= LoggerFactory
	        .getLogger(PrintStoreServiceImpl.class);

	@Autowired
	private UserDao				userDao;

	@Autowired
	private UrlTypeDao			urlTypeDao;

	@Autowired
	private RoleDao				roleDao;

	@Autowired
	private CityDao				cityDao;

	@Autowired
	private StateDao			stateDao;

	@Autowired
	private CountryDao			countryDao;

	@Autowired
	private EmployeeDao			empDao;

	@Autowired
	private CustomerDao			custDao;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private CustomerFileDao		custFileDao;

	@Autowired
	private OrderDao			ordDao;

	@Autowired
	private ProductDao			prodDao;

	@Autowired
	private SampleFileRecordDao	sapleFileRecDao;

	@Autowired
	private ProductSampleDao	sampleDao;

	static {
		Properties props = ReadConfigurationFile.getProperties("file-upload.properties");
		BASE_UPLOAD_PATH = props.getProperty("base_upload_path");
	}

	@Override
	@Transactional
	public String completeSignup(SignUpStep2Form signUpStep2Form)
	        throws SignUpException, InvalidFieldLengthException {

		// Validating Complete Sign Up Request
		User user = validateCompleteSignUpRequest(signUpStep2Form);
		String tempPassword = user.getTempPassword();
		Long userId = null;

		if (user.getUserType().equalsIgnoreCase(UserTypes.CUSTOMER.toString())) {

			Customer cust = new Customer();

			cust = validateCustomer(signUpStep2Form);
			userId = saveCustomer(signUpStep2Form, user, cust);

		} else {

			Employee emp = validateEmployee(signUpStep2Form);
			userId = saveEmployee(signUpStep2Form, user, emp);

		}

		// Long userId = saveUser(signUpStep2Form, user);
		LOGGER.debug("Created User ID : " + userId);

		return tempPassword;
	}

	private Long saveEmployee(SignUpStep2Form signUpStep2Form, User user, Employee emp)
	        throws SignUpException {

		Country contry = new Country();
		City city = new City();
		State state = new State();
		Address address = new Address();

		try {
			state = stateDao.find(signUpStep2Form.getStateId());

			city = cityDao.find(signUpStep2Form.getCityId());

			contry = countryDao.find(signUpStep2Form.getCountryId());

			address.setCityId(city.getId());
			address.setStateId(state.getId());
			// address.setCity(city);
			address.setArea(signUpStep2Form.getArea());
			address.setHouseNo(signUpStep2Form.getHouseNo());
			address.setLandMark(signUpStep2Form.getLandMark());
			address.setPinCode(signUpStep2Form.getZipCode());
			address.setCountryId(contry.getId());

			emp.setAddress(address);
			emp.setContactNo(signUpStep2Form.getContactNo());

			emp.setStatus(UserStatus.ACTIVE.toString());

			empDao.saveOrUpdate(emp);

			// Updating User
			Set<Role> roles = new HashSet<>();

			if (user.getUserType().equals(UserTypes.ADMIN.toString())) {
				roles.add((Role) roleDao
				        .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.ADMIN)));

			} else if (user.getUserType().equals(UserTypes.EMPLOYEE.toString())) {
				roles.add((Role) roleDao
				        .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.EMPLOYEE)));
			} else if (user.getUserType().equals(UserTypes.HR.toString())) {

				roles.add((Role) roleDao
				        .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.HR)));
			} else {

				throw new InvalidUserTypeException("Invalid User Type in the request ",
				        ErrorCodes.INVALID_USER_TYPE_ERROR);
			}

			/*
			 * roles.add((Role) roleDao
			 * .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.ADMIN)));
			 * 
			 * roles.add((Role) roleDao.getByCriteria(
			 * roleDao.getFindByNameCriteria(SystemRoles.ADMIN)));
			 */

			user.setRoles(roles);
			// user.setCompany(printStore);
			user.setStatus(UserStatus.ACTIVE.toString());
			userDao.update(user);

		} catch (Exception e) {

			if (e instanceof SignUpException) {
				throw (SignUpException) e;
			}
			LOGGER.error("Some error occurred while updating User or adding PrintStore. Reason : "
			        + e.getMessage(), e);
			throw new SignUpException("Error occurred while updating database!",
			        ErrorCodes.DATABASE_ERROR);
		}
		return user.getId();
	}

	private Long saveCustomer(SignUpStep2Form signUpStep2Form, User user, Customer cust)
	        throws SignUpException {

		Country contry = new Country();
		City city = new City();
		State state = new State();
		Address address = new Address();

		try {
			state = stateDao.find(signUpStep2Form.getStateId());

			city = cityDao.find(signUpStep2Form.getCityId());

			contry = countryDao.find(signUpStep2Form.getCountryId());

			address.setCityId(city.getId());
			address.setStateId(state.getId());
			address.setArea(signUpStep2Form.getArea());
			address.setHouseNo(signUpStep2Form.getHouseNo());
			address.setLandMark(signUpStep2Form.getLandMark());
			address.setPinCode(signUpStep2Form.getZipCode());
			address.setCountryId(contry.getId());

			cust.setAddress(address);
			cust.setContactNumber(signUpStep2Form.getContactNo());

			cust.setStatus(UserStatus.ACTIVE.toString());

			custDao.saveOrUpdate(cust);

			// Updating User
			Set<Role> roles = new HashSet<>();
			roles.add((Role) roleDao
			        .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.CUSTOMER)));

			user.setRoles(roles);
			user.setStatus(UserStatus.ACTIVE.toString());
			userDao.update(user);

		} catch (Exception e) {

			if (e instanceof SignUpException) {
				throw (SignUpException) e;
			}
			LOGGER.error("Some error occurred while updating User or adding PrintStore. Reason : "
			        + e.getMessage(), e);
			throw new SignUpException("Error occurred while updating database!",
			        ErrorCodes.DATABASE_ERROR);

		}
		return user.getId();
	}

	/*
	 * private Long saveUser(SignUpStep2Form signUpStep2Form, User user) throws SignUpException { //
	 * Saving PrintStore Entity PrintStore printStore = new PrintStore();
	 * 
	 * Address adress=new Address(); State state=new State(); City city=new City(); try {
	 * printStore.setStoreName(signUpStep2Form.getCompanyName());
	 * printStore.setAddress(signUpStep2Form.getAddress());
	 * printStore.setStoreContactNo(signUpStep2Form.getCompanyContactNo());
	 * 
	 * state=stateDao.find(signUpStep2Form.getStateId());
	 * 
	 * city=cityDao.find(signUpStep2Form.getCity());
	 * 
	 * adress.setPinCode(signUpStep2Form.getZipCode());
	 * printStore.setStatus(CommonStatus.ACTIVE.toString());
	 * //printStore.setCompanyExtVideoUrl(signUpStep2Form.getCompanyExtVideoUrl());
	 * printStore.setStoreWebsite(signUpStep2Form.getCompanyWebsite()); if
	 * (signUpStep2Form.getCompanyUrlForms() != null && signUpStep2Form.getCompanyUrlForms().size()
	 * > 0) { Set<PrintStoreUrl> printStoreUrls = new HashSet<>(); for (CompanyUrlForm
	 * companyUrlForm : signUpStep2Form.getCompanyUrlForms()) { PrintStoreUrl printStoreUrl = new
	 * PrintStoreUrl(); //printStoreUrl.setCompany(printStore); try { UrlType urlType =
	 * urlTypeDao.load(companyUrlForm.getUrlTypeId()); if (urlType == null || urlType.getStatus()
	 * .equalsIgnoreCase(CommonStatus.INACTIVE.toString()) || urlType.getStatus()
	 * .equalsIgnoreCase(CommonStatus.ARCHIVED.toString())) { throw new SignUpException(
	 * "Invalid URL Type Provided!", ErrorCodes.SIGNUP_URL_TYPE_INVALID); } } catch (Exception e) {
	 * throw new SignUpException("Invalid URL Type Provided!", ErrorCodes.SIGNUP_URL_TYPE_INVALID);
	 * } printStoreUrl.setUrlType(urlTypeDao.load(companyUrlForm.getUrlTypeId()));
	 * printStoreUrl.setUrl(companyUrlForm.getUrl());
	 * printStoreUrl.setStatus(CommonStatus.ACTIVE.toString()); printStoreUrls.add(printStoreUrl); }
	 * printStore.setPrintStoreUrls((printStoreUrls)); }
	 * 
	 * // Updating User Set<Role> roles = new HashSet<>(); roles.add((Role) roleDao
	 * .getByCriteria(roleDao.getFindByNameCriteria("ROLE_COMPANY_ADMIN"))); roles.add((Role)
	 * roleDao .getByCriteria(roleDao.getFindByNameCriteria(SystemRoles.ADMIN))); roles.add((Role)
	 * roleDao.getByCriteria( roleDao.getFindByNameCriteria(SystemRoles.ADMIN)));
	 * user.setRoles(roles); //user.setCompany(printStore);
	 * user.setStatus(UserStatus.ACTIVE.toString()); userDao.update(user); //printStore =
	 * user.getCompany();
	 * 
	 * } catch (Exception e) { if (e instanceof SignUpException) { throw (SignUpException) e; }
	 * LOGGER.error("Some error occurred while updating User or adding PrintStore. Reason : " +
	 * e.getMessage(), e); throw new SignUpException("Error occurred while updating database!",
	 * ErrorCodes.DATABASE_ERROR); } return printStore.getId(); }
	 */

	private Employee validateEmployee(SignUpStep2Form signUpStep2Form) throws SignUpException {
		Employee emp = null;
		String email = PasswordUtils.decode(signUpStep2Form.getEmailToken());

		if (!ValidationUtils.validateEmail(email)) {
			throw new SignUpException("Please provide valid Email.", ErrorCodes.VALIDATION_ERROR);
		}

		try {
			emp = (Employee) empDao.getByCriteria(empDao.getFindByEmailCriteria(email));
			if (emp == null) {
				throw new SignUpException("No customer found with this Email",
				        ErrorCodes.USER_NOT_FOUND_ERROR);
			}
		} catch (InstanceNotFoundException e) {
			LOGGER.error("No user found with this Email", e);
			throw new SignUpException("No Employee found with this Email",
			        ErrorCodes.EMPLOYEE_NOT_FOUND_ERROR);
		}

		return emp;

	}

	private Customer validateCustomer(SignUpStep2Form signUpStep2Form) throws SignUpException {

		Customer cust = null;
		String email = PasswordUtils.decode(signUpStep2Form.getEmailToken());

		if (!ValidationUtils.validateEmail(email)) {
			throw new SignUpException("Please provide valid Email.", ErrorCodes.VALIDATION_ERROR);
		}

		try {
			cust = (Customer) custDao.getByCriteria(custDao.getFindByEmailCriteria(email));
			if (cust == null) {
				throw new SignUpException("No customer found with this Email",
				        ErrorCodes.USER_NOT_FOUND_ERROR);
			}
		} catch (InstanceNotFoundException e) {
			LOGGER.error("No user found with this Email", e);
			throw new SignUpException("No Customer found with this Email",
			        ErrorCodes.CUSTOMER_NOT_FOUND_ERROR);
		}

		return cust;

	}

	private User validateCompleteSignUpRequest(SignUpStep2Form signUpStep2Form)
	        throws SignUpException, InvalidFieldLengthException {
		User user = null;
		Long contNum = signUpStep2Form.getContactNo();
		Integer zipcode = signUpStep2Form.getZipCode();
		String email = PasswordUtils.decode(signUpStep2Form.getEmailToken());
		if (!ValidationUtils.validateEmail(email)) {
			throw new SignUpException("Please provide valid Email.", ErrorCodes.VALIDATION_ERROR);
		}
		if (contNum != null && String.valueOf(contNum).length() != 10) {

			throw new InvalidFieldLengthException("Contact Number Should Be 10 digits ",
			        ErrorCodes.CONTACT_NUMBER_LENGTH_INVALI);

		}

		if (zipcode != null && String.valueOf(zipcode).length() != 6) {

			throw new InvalidFieldLengthException("Zipcode should be 6 digits ",
			        ErrorCodes.ZIPCODE_LENGTH_INVALID);

		}

		try {
			user = (User) userDao.getByCriteria(userDao.getFindByEmailCriteria(email));
			if (user == null) {
				throw new SignUpException("No user found with this Email",
				        ErrorCodes.USER_NOT_FOUND_ERROR);
			}
			UserStatus status = UserStatus.valueOf(user.getStatus());
			switch (status) {

			case ACTIVE:
				throw new SignUpException("User already registered with this Email!",
				        ErrorCodes.SIGNUP_ALREADY_ACTIVE);
			case FORGET_PASSWORD_INITIATED:
				throw new SignUpException("User already registered with this Email!",
				        ErrorCodes.SIGNUP_ALREADY_ACTIVE);
			case ARCHIVED:
				throw new SignUpException(
				        "Your account is deactivated, Please contact your adminisrator!",
				        ErrorCodes.SIGNUP_ACCOUNT_DEACTIVATED);
			default:
				break;
			}

		} catch (InstanceNotFoundException e) {
			LOGGER.error("No user found with this Email", e);
			throw new SignUpException("No user found with this Email",
			        ErrorCodes.USER_NOT_FOUND_ERROR);
		}
		return user;
	}

	@Override
	@Transactional
	public Long uploadFile(String fileType, MultipartFile file)
	        throws FileUploadException, UserNotFoundException {
		// Validating Request
		validateUploadFileInput(fileType);
		Long fileId = null;

		try {

			User user = null;
			Customer cust = null;

			user = (User) customerService.getLoggedinUser();

			if (user != null && user.getUserType().equals(UserTypes.CUSTOMER.toString())) {
				LOGGER.info("user found which is customer");

				cust = (Customer) custDao
				        .getByCriteria(custDao.getFindByEmailCriteria(user.getEmailId()));

				if (cust != null) {
					LOGGER.info("Logged in user is a customer");
					String custFileName = file.getOriginalFilename();
					String custFormatterName = cust.getFirstName().trim();
					String custFileRelativePath = "printkaari_files" + File.separator
					        + "customer_data" + File.separator + "customer_" + cust.getId();
					String outPutFileName = custFileName; /*.substring(0, custFileName.lastIndexOf("."))+
					        "-" + new Date().getTime()
					        + custFileName.substring(custFileName.lastIndexOf("."));
*/
					LOGGER.debug("custFileRelativePath : " + custFileRelativePath);
					LOGGER.debug("outPutFileName : " + outPutFileName);

					FileUtils.uploadFile(BASE_UPLOAD_PATH + File.separator + custFileRelativePath
					        + File.separator, outPutFileName, file);
					String filePath = custFileRelativePath + File.separator + outPutFileName;
					CustomerFiles custFile = new CustomerFiles();
					custFile.setFilaPath(filePath);
					custFile.setName(outPutFileName);
					custFile.setStatus(CommonStatus.ACTIVE.toString());

					fileId = custFileDao.save(custFile);

				} else {
					LOGGER.info("Logged in user is not a customer");
					throw new UserNotFoundException("No Customer found with this ID!",
					        ErrorCodes.CUSTOMER_NOT_FOUND_ERROR);
				}

			}

			/*
			 * PrintStore printStore = printStoreDao.find(companyId); if
			 * (printStore.getStatus().equalsIgnoreCase(CommonStatus.ARCHIVED.toString()) ||
			 * printStore.getStatus().equalsIgnoreCase(CommonStatus.INACTIVE.toString())) { throw
			 * new FileUploadException("No active company found with this ID!",
			 * ErrorCodes.SIGNUP_COMPANY_NOT_ACTIVE); } String companyFileName =
			 * file.getOriginalFilename(); String companyFormattedName =
			 * printStore.getStoreName().trim().replaceAll(" +", "_"); String companyRelativePath =
			 * "assessment_files" + File.separator + "company" // + File.separator +
			 * companyFormattedName + "_" + printStore.getId() + File.separator; String
			 * outputFileName = companyFormattedName + "_" + fileType.trim().toUpperCase() +
			 * companyFileName.substring(companyFileName.lastIndexOf(".")); LOGGER.debug(
			 * "companyRelativePath : " + companyRelativePath); LOGGER.debug("logoOutputFileName : "
			 * + outputFileName); switch (fileType.toUpperCase()) { case "LOGO": //
			 * printStore.setCompanylogoPath(companyRelativePath + outputFileName); break; case
			 * "VIDEO": // printStore.setCompanyVideoPath(companyRelativePath + outputFileName);
			 * default: throw new FileUploadException("Invalid File Path!",
			 * ErrorCodes.COMPANY_FILE_UPLOAD_FILE_TYPE_INVALID); }
			 * 
			 * printStoreDao.update(printStore);
			 */
		} catch (InstanceNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
			throw new UserNotFoundException("No PrintStore found with this ID!",
			        ErrorCodes.USER_NOT_FOUND_ERROR);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new FileUploadException("Some error occurred while uploading file!",
			        ErrorCodes.CUSTOMER_FILE_UPLOAD_ERROR);
		}
		return fileId;
	}

	private void validateUploadFileInput(String fileType) throws FileUploadException {
		if (fileType == null || fileType.trim().length() <= 0) {
			throw new FileUploadException("File type is required!",
			        ErrorCodes.CUSTOMER_FILE_UPLOAD_FILE_TYPE_EMPTY);
		} else if (!fileType.equalsIgnoreCase("pdf") && !fileType.equals("doc")) {
			throw new FileUploadException("Invalid File Path !",
			        ErrorCodes.CUSTOMER_FILE_UPLOAD_FILE_TYPE_INVALID);
		}
	}

	@Override
	@Transactional
	public Map<String, Object> downloadCollegeProjectFiles(Long order_id)
	        throws DatabaseException, FileDownloadException {

		CustOrder ord = null;
		Map<String, Object> map = new LinkedHashMap<>();
		Set<CustomerFiles> custFiles = new HashSet<>();
		String fileLocation = null;

		CustomerFiles file = null;

		String str = "1";

		try {

			ord = ordDao.find(order_id);

			custFiles = ord.getFileId();

			for (CustomerFiles custFile : custFiles) {

				map.put(str, BASE_UPLOAD_PATH + File.separator + custFile.getFilaPath());
				map.put("file path ", BASE_UPLOAD_PATH + File.separator + custFile.getFilaPath());

				LOGGER.info("file path " + custFile.getFilaPath());

				Integer pathNo = Integer.parseInt(str);
				pathNo = pathNo + 1;
				pathNo.toString();

			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new FileDownloadException("Some error occurred while downloading file !",
			        ErrorCodes.CUSTOMER_FILE_DOWNLOAD_ERROR);
		}
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> uploadProductSampleFile(String fileType, MultipartFile file, Long productId)
	        throws FileUploadException, ProductNotFoundException {
		Product prod = null;
		Map<String, Object> map = new HashMap<>();
		Long sampleFileId = null;
		try {
			DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			//
			System.out.println("date :" + format1);

			LocalDateTime date = LocalDateTime.now();

			System.out.println("Date before format  :" + date);
			String dateStr = date.format(format1);
			LOGGER.debug("Loading Product for id  : " + productId);
			prod = prodDao.find(productId);

			if (prod != null) {
        
				
				String sampleFileName = file.getOriginalFilename();
				String sampleFileFormatterName = prod.getName().trim();
				String sampleFileRelativePath = "printkaari_files" + File.separator + "product_data"
				        + File.separator + "product-" + prod.getId();
				String outPutFileName = sampleFileName ;
						/*.substring(0, sampleFileName.lastIndexOf("."))
				        + "_" + dateStr + sampleFileName.substring(sampleFileName.lastIndexOf("."));*/

				LOGGER.debug("sample relative path : " + sampleFileRelativePath);
				LOGGER.debug("sample OutputFileName : " + outPutFileName);

				FileUtils.uploadFile(
				        BASE_UPLOAD_PATH + File.separator + sampleFileRelativePath + File.separator,
				        outPutFileName, file);
				String filePath = sampleFileRelativePath + File.separator + outPutFileName;

				SampleFileRecord sampleFile = new SampleFileRecord();

				sampleFile.setName(prod.getName());
				sampleFile.setStatus("ACTIVE");
				sampleFile.setFilePath(filePath);

				sampleFileId = sapleFileRecDao.save(sampleFile);
				sampleFile = sapleFileRecDao.find(sampleFileId);
				
				prod.setSampleFileId(sampleFile);
				prodDao.saveOrUpdate(prod);
 
				List<ProductSamples> sampleList = null;
				sampleList = sampleDao.getSampleRecordByProductId(prod.getId());

				if (!CollectionUtils.isEmpty(sampleList) && sampleList.size() == 1) {
					ProductSamples sample = null;
					sample = sampleList.get(0);
					sample.setSampleFileId(sampleFile);
					sampleDao.saveOrUpdate(sample);
				} else if (!CollectionUtils.isEmpty(sampleList) && sampleList.size() > 1) {

					for (ProductSamples sample1 : sampleList) {

						sample1.setSampleFileId(sampleFile);
						sampleDao.saveOrUpdate(sample1);
					}

				} else {
					ProductSamples sample = new ProductSamples();
					sample.setName(prod.getName());
					sample.setProduct(prod);
					sample.setSampleFileId(sampleFile);
					Long sampleId = sampleDao.save(sample);
				}

			} else {

				LOGGER.info("Product Not found for " + productId);

				throw new ProductNotFoundException(
				        "Some error occured while getting product from DB for sampel file upload",
				        ErrorCodes.PRODUCT_NOT_FOUND_IN_DATABASE);

			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new FileUploadException("Some error occurred while uploading sample file !",
			        ErrorCodes.SAMPLE_FILE_UPLOAD_ERROR);
		}

		return map;
	}

}
