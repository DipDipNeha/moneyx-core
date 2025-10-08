/**
 * @author Dipak
 */

package com.pscs.moneyx.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pscs.moneyx.model.RequestData;
import com.pscs.moneyx.model.ResponseData;
import com.pscs.moneyx.service.MoneyXCoreService;
@RestController
@RequestMapping("/api/moneyxcore")
public class MoneyXCoreController {
	
	private final MoneyXCoreService moneyXCoreService;
	
	
	public MoneyXCoreController(MoneyXCoreService moneyXCoreService) {
		this.moneyXCoreService = moneyXCoreService;
	}
	//echo
	@PostMapping("/echo")
	public ResponseData echo(RequestData requestData) {
		ResponseData response = new ResponseData();
		
		response.setResponseCode("200");
		response.setResponseMessage("Echo from MoneyX Core Service");
		return response;
	}
	//Login API
	@PostMapping("/login")
	public ResponseData login(@RequestBody  RequestData requestData) {
		ResponseData response = new ResponseData();
		response=moneyXCoreService.login(requestData);
		return response;
	}
	//Generate OTP API
	@PostMapping("/generateOtp")
	public ResponseData generateOtp(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response=moneyXCoreService.generateOtp(requestData);
		return response;
	}
	
	//contacts us API
	@PostMapping("/contactsus")
	public ResponseData contactsUs(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response = moneyXCoreService.contactsUs(requestData);
		return response;
	}
	
	// view contact us messages
	@PostMapping("/viewcontactus")
	public ResponseData viewContactUsMessages(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response = moneyXCoreService.viewContactUsMessages(requestData);
		return response;
	}

	// update contact us message status
	@PostMapping("/updatecontactusstatus")
	public ResponseData updateContactUsMessageStatus(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response = moneyXCoreService.updateContactUsMessageStatus(requestData);
		return response;
	}
	
}
