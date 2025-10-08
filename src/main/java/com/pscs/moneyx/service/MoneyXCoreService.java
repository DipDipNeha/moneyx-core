/**
 * @author Dipak
 */

package com.pscs.moneyx.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.pscs.moneyx.entity.CustomerDocInfo;
import com.pscs.moneyx.entity.MoneyXCoreCustomer;
import com.pscs.moneyx.entity.OtpDataTabl;
import com.pscs.moneyx.helper.ConvertRequestUtils;
import com.pscs.moneyx.helper.CoreConstant;
import com.pscs.moneyx.model.RequestData;
import com.pscs.moneyx.model.ResponseData;
import com.pscs.moneyx.repo.ContactsUsRepo;
import com.pscs.moneyx.repo.CustomerDocInfoRepo;
import com.pscs.moneyx.repo.MoneyXCoreCustomerRepo;
import com.pscs.moneyx.repo.OtpDataTablRepo;
import com.pscs.moneyx.service.post.EmailAndSMSPostingService;
import com.pscs.moneyx.utils.CommonUtils;

@Service
public class MoneyXCoreService {

	private final CustomerDocInfoRepo customerDocInfoRepo;
	private final MoneyXCoreCustomerRepo moneyXCoreCustomerRepo;
	private final ContactsUsRepo contactsUsRepo;
	private final EmailAndSMSPostingService smsPostingService;
	private final OtpDataTablRepo otpDataTablRepo;

	public MoneyXCoreService(CustomerDocInfoRepo customerDocInfoRepo, MoneyXCoreCustomerRepo moneyXCoreCustomerRepo,
			ContactsUsRepo contactsUsRepo, EmailAndSMSPostingService smsPostingService,
			OtpDataTablRepo otpDataTablRepo) {
		this.customerDocInfoRepo = customerDocInfoRepo;
		this.moneyXCoreCustomerRepo = moneyXCoreCustomerRepo;
		this.contactsUsRepo = contactsUsRepo;
		this.smsPostingService = smsPostingService;
		this.otpDataTablRepo = otpDataTablRepo;
	}

	// login API
	public ResponseData login(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);
			
			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			String email = jbody.getString("email");
			String password = jbody.getString("password");

			// validate OTP for login if required
			// String otp = jbody.getString("otp");
			ResponseData validateOtp = validateOtp(requestData);

			if (validateOtp.getResponseCode().equals(CoreConstant.FAILURE_CODE)) {
				return validateOtp;
			} else {

				MoneyXCoreCustomer byEmailAndPassword = moneyXCoreCustomerRepo.findByEmailAndPassword(email,
						CommonUtils.b64_sha256(password));

				if (byEmailAndPassword == null) {
					response.setResponseCode(CoreConstant.FAILURE_CODE);
					response.setResponseMessage(CoreConstant.FAILED + " to upload image");
					return response;
				} else {

					response.setResponseCode(CoreConstant.SUCCESS_CODE);
					response.setResponseMessage(CoreConstant.SUCCESS);
					response.setResponseData(resjson.toMap());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}

		return response;
	}

	public ResponseData uploadImage(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);

			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			JSONObject jheader = requestJson.getJSONObject("jheader");
			CustomerDocInfo mobCustomerDocInfo = new CustomerDocInfo();
			mobCustomerDocInfo.setDocumentType(jbody.getString("documentType"));
			mobCustomerDocInfo.setDescription(jbody.getString("description"));
			mobCustomerDocInfo.setBlog(jbody.getString("blog"));
			mobCustomerDocInfo.setBlogCategory(jbody.getString("blogCategory"));
			mobCustomerDocInfo.setCreatedBy(jheader.getString("userid"));
			mobCustomerDocInfo.setCreatedDttm(new java.util.Date());
			mobCustomerDocInfo.setFileData(jbody.getString("fileData"));
			CustomerDocInfo saveresponse = customerDocInfoRepo.save(mobCustomerDocInfo);

			if (saveresponse == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " to upload image");
				return response;
			} else {

				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				response.setResponseMessage(CoreConstant.SUCCESS);
				response.setResponseData(resjson.toMap());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}

		return response;
	}

	// update image details
	public ResponseData updateImage(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);

			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			JSONObject jheader = requestJson.getJSONObject("jheader");
			Long id = jbody.getLong("id");
			CustomerDocInfo mobCustomerDocInfo = customerDocInfoRepo.findById(id).orElse(null);
			mobCustomerDocInfo.setDocumentType(jbody.getString("documentType"));
			mobCustomerDocInfo.setDescription(jbody.getString("description"));
			mobCustomerDocInfo.setBlog(jbody.getString("blog"));
			mobCustomerDocInfo.setBlogCategory(jbody.getString("blogCategory"));
			mobCustomerDocInfo.setCreatedBy(jheader.getString("userId"));
			mobCustomerDocInfo.setUpdatedDttm(new java.util.Date());
			if (jbody.has("fileData") && jbody.getString("fileData") != null
					&& !jbody.getString("fileData").isEmpty()) {
				mobCustomerDocInfo.setFileData(jbody.getString("fileData"));
			}

			CustomerDocInfo saveresponse = customerDocInfoRepo.save(mobCustomerDocInfo);

			if (saveresponse == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " to update image details");
				return response;
			} else {

				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				response.setResponseMessage(CoreConstant.SUCCESS);
				response.setResponseData(resjson.toMap());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}

		return response;
	}

	public ResponseData generateOtp(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);

			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			System.out.println("Request : " + requestData);
			String strJheader = ConvertRequestUtils.getJsonString(requestData.getJheader());
			JSONObject jHeader = new JSONObject(strJheader);

			String userid = jHeader.getString("userid");

			// check email exist
			MoneyXCoreCustomer byEmail = moneyXCoreCustomerRepo.findByEmail(userid);
			if (byEmail == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " Email not registered");
				return response;
			}

			String otp = "234567";// CommonUtils.createRandomNumber(6);
			String encryptedOtp = CommonUtils.b64_sha256(otp);
			// generate otp by using random
			OtpDataTabl otpDataTabl = new OtpDataTabl();
			otpDataTabl.setOtp(encryptedOtp);
			otpDataTabl.setUserId(userid);
			otpDataTabl.setTransType(jHeader.getString("requestType"));
			String mobileNo = byEmail.getPhoneNumber();
			otpDataTabl.setMobileNo(mobileNo);
			otpDataTabl.setEmailId(userid);

			otpDataTabl.setChannel(jHeader.getString("channel"));
			otpDataTabl.setOtpStatus("A");

			OtpDataTabl save = otpDataTablRepo.save(otpDataTabl);

			if (save == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " to Generate OTP");
				return response;
			} else {
				JSONObject smsRequest = new JSONObject();
				if (mobileNo.startsWith("234")) {

					smsRequest.put("messages",
							ConvertRequestUtils.generateSMSJson(mobileNo, jHeader.getString("requestType"), otp));

				} else {
					response.setResponseCode(CoreConstant.SUCCESS_CODE);
					response.setResponseMessage(CoreConstant.SMS_SENT + otp);
					return response;
				}

				response.setResponseMessage(CoreConstant.OTP_SENT + otp);
				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				//JSONObject sendSMSRes = smsPostingService.sendPostRequest(smsRequest.toString(), "sms.url");

				JSONObject emailRequest = new JSONObject();

				emailRequest.put("messages", ConvertRequestUtils.generateEmailJson(otpDataTabl.getEmailId(),
						"MoneyX Core One Time Password", "Dear Customer, Your OTP is " + otp));

				JSONObject sendMailRes = smsPostingService.sendPostRequest(emailRequest.toString(), "email.url");

//				if (sendSMSRes.getString("respsode").equals("200")) {
					if ("200".equals("200")) {
					response.setResponseCode(CoreConstant.SUCCESS_CODE);
					response.setResponseMessage(CoreConstant.SMS_SENT + otp);
//					response.setResponseData(sendSMSRes.toMap());

				} else {
					response.setResponseCode(CoreConstant.FAILURE_CODE);
					response.setResponseMessage(CoreConstant.FAILED + " to send SMS");
//					response.setResponseData(sendSMSRes.toMap());
				}

			}

			response.setResponseCode(CoreConstant.SUCCESS_CODE);
			response.setResponseMessage(CoreConstant.SUCCESS);
			response.setResponseData(resjson.toMap());

		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}
		return response;
	}

	// Validate otp
	public ResponseData validateOtp(RequestData request) {
		ResponseData response = new ResponseData();
		try {

			System.out.println("Request : " + request);
			String jsonString = ConvertRequestUtils.getJsonString(request.getJbody());

			JSONObject requestJson = new JSONObject(jsonString);
			System.out.println("Request Body: " + requestJson.toString());

			String otp = requestJson.getString("authValue");
			String username = requestJson.getString("email");
			String mobileNumber = requestJson.getString("phoneNumber");

			OtpDataTabl otpDataTabl = otpDataTablRepo.findByUserIdAndOtp(username, CommonUtils.b64_sha256(otp));

			// Check if the OTP is older than 2 minutes

			if (otpDataTabl != null) {
				long otpCreationTime = otpDataTabl.getTransDttm().getTime();
				long currentTime = System.currentTimeMillis();
				long timeDifference = currentTime - otpCreationTime;

				// Check if the OTP is older than 2 minutes (120000 milliseconds) use constant
				// for 2 minutes
				if (timeDifference > CommonUtils.OTP_VALIDITY_DURATION) {
					otpDataTabl.setOtpStatus("E"); // Set status to Expired
					otpDataTablRepo.save(otpDataTabl);
					response.setResponseCode(CoreConstant.FAILURE_CODE);
					response.setResponseMessage(CoreConstant.EXPIRED_OTP);
					return response;
				}
			}

			if (otpDataTabl == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.INVALID_OTP);
				return response;
			} else if (otpDataTabl.getOtpStatus().equals("A")) {
				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				response.setResponseMessage(CoreConstant.OTP_VERIFIED);
				// if success the update the oto status to S
				otpDataTabl.setOtpStatus("S");
				otpDataTablRepo.save(otpDataTabl);

			} else if (otpDataTabl.getOtpStatus().equals("S")) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.OTP_USED);
			} else if (otpDataTabl.getOtpStatus().equals("E")) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.EXPIRED_OTP);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + " to Validate OTP");
		}
		return response;
	}

	public ResponseData viewblogs(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);

			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			
			List<CustomerDocInfo> all = customerDocInfoRepo.findAll();
			JSONArray jsonArray = new JSONArray(all);
			
			
				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				response.setResponseMessage(CoreConstant.SUCCESS);
				response.setResponseData(jsonArray.toList());
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}

		return response;
	}

	public ResponseData contactsUs(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);
			JSONObject requestJson = new JSONObject(jsonString);
			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			com.pscs.moneyx.entity.ContactsUs contactsUs = new com.pscs.moneyx.entity.ContactsUs();
			contactsUs.setName(jbody.getString("name"));
			contactsUs.setEmail(jbody.getString("email"));
			contactsUs.setSubject(jbody.getString("subject"));
			contactsUs.setMessage(jbody.getString("message"));
			contactsUs.setPhone(jbody.getString("phone"));
			contactsUs.setCreatedDttm(new java.util.Date());
			com.pscs.moneyx.entity.ContactsUs saveresponse = contactsUsRepo.save(contactsUs);

			JSONObject emailRequest = new JSONObject();

			emailRequest.put("messages", ConvertRequestUtils.generateEmailJson(contactsUs.getSubject(),
					contactsUs.getEmail(), jbody.getString("message")));

			JSONObject sendMailRes = smsPostingService.sendPostRequest(emailRequest.toString(), "email.url");

			if (saveresponse == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " to submit contact us form");
				return response;
			} else {
				response.setResponseCode(CoreConstant.SUCCESS_CODE);
				response.setResponseMessage(CoreConstant.SUCCESS);
				response.setResponseData(resjson.toMap());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;

		}
		return response;
	}

	public ResponseData viewContactUsMessages(RequestData requestData) {
	    ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);
			JSONObject requestJson = new JSONObject(jsonString);
			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");

			List<com.pscs.moneyx.entity.ContactsUs> allMessages = contactsUsRepo.findAll();
			JSONArray jsonArray = new JSONArray(allMessages);

			response.setResponseCode(CoreConstant.SUCCESS_CODE);
			response.setResponseMessage(CoreConstant.SUCCESS);
			response.setResponseData(jsonArray.toList());

		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}
		return response;
	}

	public ResponseData updateContactUsMessageStatus(RequestData requestData) {
	ResponseData response = new ResponseData();
	try {
		JSONObject resjson = new JSONObject();
		String jsonString = ConvertRequestUtils.getJsonString(requestData);
		JSONObject requestJson = new JSONObject(jsonString);
		System.out.println("Request Body: " + requestJson.toString());
		JSONObject jbody = requestJson.getJSONObject("jbody");
		Long id = jbody.getLong("id");
		String status = jbody.getString("status");
		String remarks = jbody.getString("remarks");

		com.pscs.moneyx.entity.ContactsUs contactUsMessage = contactsUsRepo.findById(id).orElse(null);
		if (contactUsMessage == null) {
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + " Contact Us message not found");
			return response;
		}

		contactUsMessage.setStatus(status);
		contactUsMessage.setRemarks(remarks);
		contactsUsRepo.save(contactUsMessage);

		response.setResponseCode(CoreConstant.SUCCESS_CODE);
		response.setResponseMessage(CoreConstant.SUCCESS);
		response.setResponseData(resjson.toMap());

	} catch (Exception e) {
		e.printStackTrace();
		response.setResponseCode(CoreConstant.FAILURE_CODE);
		response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
		return response;
	}
		return response;
	}
}
