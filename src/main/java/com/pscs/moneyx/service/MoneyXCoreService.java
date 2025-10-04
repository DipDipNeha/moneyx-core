/**
 * @author Dipak
 */

package com.pscs.moneyx.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.pscs.moneyx.entity.CustomerDocInfo;
import com.pscs.moneyx.entity.MoneyXCoreCustomer;
import com.pscs.moneyx.helper.ConvertRequestUtils;
import com.pscs.moneyx.helper.CoreConstant;
import com.pscs.moneyx.model.RequestData;
import com.pscs.moneyx.model.ResponseData;
import com.pscs.moneyx.repo.ContactsUsRepo;
import com.pscs.moneyx.repo.CustomerDocInfoRepo;
import com.pscs.moneyx.repo.MoneyXCoreCustomerRepo;
import com.pscs.moneyx.service.post.EmailAndSMSPostingService;

@Service
public class MoneyXCoreService {

	private final CustomerDocInfoRepo customerDocInfoRepo;
	private final MoneyXCoreCustomerRepo moneyXCoreCustomerRepo;
	private final ContactsUsRepo contactsUsRepo;
	private final EmailAndSMSPostingService smsPostingService ;

	public MoneyXCoreService(CustomerDocInfoRepo customerDocInfoRepo, MoneyXCoreCustomerRepo moneyXCoreCustomerRepo,
			ContactsUsRepo contactsUsRepo, EmailAndSMSPostingService smsPostingService) {
		this.customerDocInfoRepo = customerDocInfoRepo;
		this.moneyXCoreCustomerRepo = moneyXCoreCustomerRepo;
		this.contactsUsRepo = contactsUsRepo;
		this.smsPostingService = smsPostingService;
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

			MoneyXCoreCustomer byEmailAndPassword = moneyXCoreCustomerRepo.findByEmailAndPassword(email, password);

			if (byEmailAndPassword == null) {
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
			mobCustomerDocInfo.setCreatedBy(jheader.getString("userId"));
			mobCustomerDocInfo.setCreatedDttm(new java.util.Date());
			mobCustomerDocInfo.setFileData(ConvertRequestUtils.stringToClob(jbody.getString("fileData")));
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
				mobCustomerDocInfo.setFileData(ConvertRequestUtils.stringToClob(jbody.getString("fileData")));
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
			String email = jbody.getString("email");
			// check email exist
			MoneyXCoreCustomer byEmail = moneyXCoreCustomerRepo.findByEmail(email);
			if (byEmail == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " Email not registered");
				return response;
			}

			// generate otp
			String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));
			System.out.println("Generated OTP: " + otp);

			// Here, you would typically send the OTP to the user's email address.
			// For demonstration purposes, we'll just include it in the response.

			resjson.put("otp", otp);

			response.setResponseCode(CoreConstant.SUCCESS_CODE);
			response.setResponseMessage(CoreConstant.SUCCESS);
			response.setResponseData(resjson.toMap());

		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(CoreConstant.FAILURE_CODE);
			response.setResponseMessage(CoreConstant.FAILED + e.getMessage());
			return response;
		}
		return null;
	}

	public ResponseData viewImage(RequestData requestData) {
		ResponseData response = new ResponseData();
		try {
			JSONObject resjson = new JSONObject();
			String jsonString = ConvertRequestUtils.getJsonString(requestData);

			JSONObject requestJson = new JSONObject(jsonString);

			System.out.println("Request Body: " + requestJson.toString());
			JSONObject jbody = requestJson.getJSONObject("jbody");
			Long id = jbody.getLong("id");
			CustomerDocInfo mobCustomerDocInfo = customerDocInfoRepo.findById(id).orElse(null);
			if (mobCustomerDocInfo == null) {
				response.setResponseCode(CoreConstant.FAILURE_CODE);
				response.setResponseMessage(CoreConstant.FAILED + " to view image");
				return response;
			} else {
				resjson.put("id", mobCustomerDocInfo.getId());
				resjson.put("documentType", mobCustomerDocInfo.getDocumentType());
				resjson.put("description", mobCustomerDocInfo.getDescription());
				resjson.put("blog", mobCustomerDocInfo.getBlog());
				resjson.put("blogCategory", mobCustomerDocInfo.getBlogCategory());
				resjson.put("fileData", ConvertRequestUtils.clobToString(mobCustomerDocInfo.getFileData()));
				resjson.put("createdBy", mobCustomerDocInfo.getCreatedBy());
				resjson.put("createdDttm", mobCustomerDocInfo.getCreatedDttm());
				resjson.put("updatedDttm", mobCustomerDocInfo.getUpdatedDttm());

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

		return null;
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
			contactsUs.setCreatedDttm(new java.util.Date());
			com.pscs.moneyx.entity.ContactsUs saveresponse = contactsUsRepo.save(contactsUs);
			
			JSONObject emailRequest = new JSONObject();
			
			emailRequest.put("messages", ConvertRequestUtils.generateEmailJson(contactsUs.getSubject(),contactsUs.getEmail(),
					jbody.getString("message")));
			
			JSONObject sendMailRes = smsPostingService.sendPostRequest(emailRequest.toString(),"email.url");

			
			
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
}
