package com.question.one.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.question.one.constant.Constant;
import com.question.one.dto.RestResponse;
import com.question.one.exception.QuestionOneBaseException;
import com.question.one.model.Employee;
import com.question.one.model.TaskInfo;
import com.question.one.repository.EmployeeRepository;
import com.question.one.repository.TaskInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	TaskInfoRepository taskInfoRepository;

	@Async
	public CompletableFuture<List<Employee>> getAllEmployees() {
		log.info("Request to get a list of employees");
		final List<Employee> employees = employeeRepository.findAll();
		return CompletableFuture.completedFuture(employees);
	}

	@Async
	public void saveEmployees(final InputStream inputStream, TaskInfo taskInfo) throws QuestionOneBaseException {
		final long start = System.currentTimeMillis();
		List<Employee> employees = parseInputFile(inputStream);
		log.info("Saving a list of employess of size {} records", employees.size());
		employeeRepository.saveAll(employees);
		log.info("Elapsed time: {}", (System.currentTimeMillis() - start));
		taskInfo.setTimeTaken(System.currentTimeMillis() - start);
		updateTaskInfo(taskInfo, Constant.COMPLETED.toString());
	}

	@Override
	public RestResponse deleteEmployee(int employeeId) {
		RestResponse restResponse = new RestResponse();
		TaskInfo taskInfo = createAndSaveTaskInfo(Constant.DELETE.toString(), Constant.IN_PROGRESS.toString());
		employeeRepository.deleteById(employeeId);
		restResponse.setMessage("Delete has been Completed For EmployeeId: " + employeeId);
		restResponse.setTaskInfoId(taskInfo.getTaskUID());
		updateTaskInfo(taskInfo, Constant.COMPLETED.toString());
		return restResponse;
	}

	@Override
	public Employee getEmployee(int employeeId) {
		RestResponse restResponse = new RestResponse();
		TaskInfo taskInfo = createAndSaveTaskInfo(Constant.FETCH.toString(), Constant.IN_PROGRESS.toString());
		Optional<Employee> employee =employeeRepository.findById(employeeId);
		restResponse.setMessage("Fetch has been Completed : " + employeeId);
		restResponse.setTaskInfoId(taskInfo.getTaskUID());
		updateTaskInfo(taskInfo, Constant.COMPLETED.toString());
		return employee.orElse(null);
	}

	public RestResponse getUploadStatus(String taskUID) {
		TaskInfo taskInfo = taskInfoRepository.findByTaskUID(taskUID);
		RestResponse restResponse = new RestResponse();
		restResponse.setTaskInfoId(taskUID);
		restResponse.setMessage(taskInfo.getTaskState());
		return restResponse;
	}

	private List<Employee> parseInputFile(final InputStream inputStream) throws QuestionOneBaseException {
		final List<Employee> employees = new ArrayList<>();
		try {
			try(final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
				String strLine;
				while((strLine = br.readLine()) != null) {
					String[] tokens = strLine.split(" ");
					StringBuilder name = new StringBuilder();
					int age = 0;
					Employee employee = new Employee();
					for(String token : tokens) {
						if(!isNumeric(token)) {
							name.append(" ").append(token);
						} else {
							age = Integer.parseInt(token);
						}
					}
					employee.setName(name.toString().trim());
					employee.setAge(age);
					employees.add(employee);
				}
				return employees;
			}
		} catch(final IOException e) {
			log.error("Failed to parse Input file {}", e.getMessage());
			throw new QuestionOneBaseException("Invalid File has been processed");
		}
	}

	public TaskInfo createAndSaveTaskInfo(String action, String status) {
		TaskInfo taskInfo = TaskInfo.builder()
				.action(action)
				.taskUID(UUID.randomUUID().toString())
				.taskState(status)
				.build();
		log.debug(taskInfo.getTaskUID(), taskInfo.getTaskState());
		return taskInfoRepository.save(taskInfo);
	}

	public void updateTaskInfo(TaskInfo taskInfo, String status) {
		taskInfo.setTaskState(status);
		log.debug(taskInfo.getTaskUID(), taskInfo.getTaskState());
		taskInfoRepository.save(taskInfo);
	}

	private boolean isNumeric(String strNum) {
		return !Objects.isNull(strNum) && pattern.matcher(strNum).matches();
	}
}