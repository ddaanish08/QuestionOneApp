package com.question.one.controller;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.question.one.constant.Constant;
import com.question.one.dto.RestResponse;
import com.question.one.model.Employee;
import com.question.one.model.TaskInfo;
import com.question.one.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	/**
	 * @param files list of file with employee details
	 */
	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskInfo> uploadFlatFile(@RequestParam(value = "action") String action, @RequestParam(value = "files") MultipartFile[] files) {
		log.debug("Inside uploadFlatFile API");
		TaskInfo taskInfo = employeeService.createAndSaveTaskInfo(action, Constant.IN_PROGRESS.toString());
		try {
			for(final MultipartFile file : files) {
				log.debug("File Name:" + file.getOriginalFilename());
				employeeService.saveEmployees(file.getInputStream(), taskInfo);
			}
			return ResponseEntity.ok(taskInfo);
		} catch(final Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * @param taskUID taskId to get the status of upload
	 */
	@GetMapping(path = "/status")
	public ResponseEntity<RestResponse> getUploadStatus(@RequestParam(value = "taskUID") String taskUID) throws IOException {
		log.debug("In getUploadStatus API: [{}]", taskUID);
		RestResponse restResponse = employeeService.getUploadStatus(taskUID);
		log.debug("Upload Call has been Completed");
		return ResponseEntity.ok(restResponse);
	}

	/**
	 * @return Return List of all employees
	 */
	@GetMapping(path = "/getAllEmployees", produces = MediaType.APPLICATION_JSON_VALUE)
	public CompletableFuture<ResponseEntity> getAllEmployees() {
		return employeeService.getAllEmployees().<ResponseEntity>thenApply(ResponseEntity::ok)
				.exceptionally(handleGetAllEmployeeFailure);
	}

	/**
	 * Helper Method to handle the exception
	 */
	private static Function<Throwable, ResponseEntity<? extends List<Employee>>> handleGetAllEmployeeFailure = throwable -> {
		log.error("Failed to read records: {}", throwable.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	};


}
