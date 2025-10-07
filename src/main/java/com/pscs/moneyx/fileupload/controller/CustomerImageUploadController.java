/**
 * 
 */
package com.pscs.moneyx.fileupload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pscs.moneyx.model.RequestData;
import com.pscs.moneyx.model.ResponseData;
import com.pscs.moneyx.service.FileSystemStorageService;
import com.pscs.moneyx.service.MoneyXCoreService;

/**
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("/api/moneyxcore")
public class CustomerImageUploadController {

	FileSystemStorageService fileSytemStorage;
	MoneyXCoreService moneyXCoreService;


	public CustomerImageUploadController(FileSystemStorageService fileSytemStorage,
			MoneyXCoreService moneyXCoreService) {
		super();
		this.fileSytemStorage = fileSytemStorage;
		this.moneyXCoreService = moneyXCoreService;
	}


	@PostMapping("/uploadImage")
	public ResponseEntity<ResponseData> uploadImage(@RequestBody RequestData requestData){

		ResponseData apiResponse = new ResponseData();

		try {

			apiResponse.setResponseCode("99");
			apiResponse.setResponseMessage("Unable to process request");

			

			apiResponse = moneyXCoreService.uploadImage(requestData);
			

		} catch (Exception e) {
			e.printStackTrace();

			apiResponse.setResponseCode("01");
			apiResponse.setResponseMessage("Internal Error Occurred!" + e.getMessage());

		} finally {

		}

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PostMapping("/viewImage")
	public ResponseData viewImage(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response = moneyXCoreService.viewblogs(requestData);
		return response;
	}
	@PostMapping("/updateimage")
	public ResponseData updateImage(@RequestBody RequestData requestData) {
		ResponseData response = new ResponseData();
		response = moneyXCoreService.updateImage(requestData);
		return response;
	}
	

}
