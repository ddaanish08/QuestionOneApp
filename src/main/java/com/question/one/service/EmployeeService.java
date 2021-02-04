package com.question.one.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.question.one.dto.RestResponse;
import com.question.one.exception.QuestionOneBaseException;
import com.question.one.model.Employee;
import com.question.one.model.TaskInfo;

public interface EmployeeService {

	RestResponse getUploadStatus(String taskUID) throws IOException;

	void saveEmployees(final InputStream inputStream, TaskInfo taskInfo) throws IOException, QuestionOneBaseException;

	CompletableFuture<List<Employee>> getAllEmployees();

	TaskInfo createAndSaveTaskInfo(String action, String status);

	RestResponse deleteEmployee(int employeeId);

	Employee getEmployee(int employeeId);
}
