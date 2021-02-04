package com.question.one.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.question.one.dto.RestResponse;
import com.question.one.model.Employee;
import com.question.one.model.TaskInfo;
import com.question.one.repository.EmployeeRepository;
import com.question.one.repository.TaskInfoRepository;

class EmployeeServiceImplTest {

	private EmployeeServiceImpl employeeServiceImplUnderTest;

	@BeforeEach
	public void setUp() {
		employeeServiceImplUnderTest = new EmployeeServiceImpl();
		employeeServiceImplUnderTest.employeeRepository = mock(EmployeeRepository.class);
		employeeServiceImplUnderTest.taskInfoRepository = mock(TaskInfoRepository.class);
	}

	@Test
	void testGetAllEmployees() throws ExecutionException, InterruptedException {
		final Employee employee = new Employee();
		employee.setEmployeeId(1);
		employee.setName("Sachin");
		employee.setAge(43);
		final List<Employee> employees = Collections.singletonList(employee);
		when(employeeServiceImplUnderTest.employeeRepository.findAll()).thenReturn(employees);

		final CompletableFuture<List<Employee>> result = employeeServiceImplUnderTest.getAllEmployees();
		Assertions.assertThat(result.get().size()).isEqualTo(1);
		Assertions.assertThat(result.get().get(0).getAge()).isEqualTo(43);
	}

	@Test
	void testGetAllEmployees_EmployeeRepositoryReturnsNoItems() throws ExecutionException, InterruptedException {
		when(employeeServiceImplUnderTest.employeeRepository.findAll()).thenReturn(Collections.emptyList());
		final CompletableFuture<List<Employee>> result = employeeServiceImplUnderTest.getAllEmployees();
		Assertions.assertThat(result.get().size()).isZero();
	}

	@Test
	void testSaveEmployees() throws Exception {
		final InputStream inputStream = new ByteArrayInputStream("content".getBytes());
		final TaskInfo taskInfo = new TaskInfo(0, "taskUID", 0L, "action", "taskState");

		final Employee employee = new Employee();
		employee.setEmployeeId(1);
		employee.setName("Dravid");
		employee.setAge(39);
		final List<Employee> employees = Collections.singletonList(employee);
		when(employeeServiceImplUnderTest.employeeRepository.saveAll(Collections.singletonList(new Employee()))).thenReturn(employees);
		when(employeeServiceImplUnderTest.employeeRepository.findAll()).thenReturn(employees);

		final TaskInfo taskInfo1 = new TaskInfo(0, "taskUID", 0L, "action", "taskState");
		when(employeeServiceImplUnderTest.taskInfoRepository.save(any(TaskInfo.class))).thenReturn(taskInfo1);
		employeeServiceImplUnderTest.saveEmployees(inputStream, taskInfo);
		Assertions.assertThat(employeeServiceImplUnderTest.employeeRepository.findAll().size()).isEqualTo(1);

	}

	@Test
	void testSaveEmployees_EmptyInputStream() throws Exception {
		final InputStream inputStream = new ByteArrayInputStream(new byte[]{});
		final TaskInfo taskInfo = new TaskInfo(0, "taskUID", 0L, "action", "taskState");

		final Employee employee = new Employee();
		employee.setEmployeeId(0);
		employee.setName("name");
		employee.setAge(0);
		final List<Employee> employees = Collections.singletonList(employee);
		when(employeeServiceImplUnderTest.employeeRepository.saveAll(Collections.singletonList(new Employee()))).thenReturn(employees);

		final TaskInfo taskInfo1 = new TaskInfo(0, "taskUID", 0L, "action", "taskState");
		when(employeeServiceImplUnderTest.taskInfoRepository.save(any(TaskInfo.class))).thenReturn(taskInfo1);

		employeeServiceImplUnderTest.saveEmployees(inputStream, taskInfo);
		Assertions.assertThat(employeeServiceImplUnderTest.employeeRepository.findAll().size()).isEqualTo(1);
	}


	@Test
	void testSaveEmployees_EmployeeRepositoryReturnsNoItems() throws Exception {
		final InputStream inputStream = new ByteArrayInputStream("content".getBytes());
		final TaskInfo taskInfo = new TaskInfo(0, UUID.randomUUID().toString(), 0L, "action", "taskState");
		when(employeeServiceImplUnderTest.employeeRepository.saveAll(Collections.singletonList(new Employee()))).thenReturn(Collections.emptyList());

		final TaskInfo taskInfo1 = new TaskInfo(0, "taskUID", 0L, "action", "taskState");
		when(employeeServiceImplUnderTest.taskInfoRepository.save(any(TaskInfo.class))).thenReturn(taskInfo1);

		employeeServiceImplUnderTest.saveEmployees(inputStream, taskInfo);
		Assertions.assertThat(employeeServiceImplUnderTest.employeeRepository.findAll().size()).isZero();

	}

	@Test
	void testGetUploadStatus() {
		final TaskInfo taskInfo = new TaskInfo(0, UUID.randomUUID().toString(), 0L, "action", "taskState");
		when(employeeServiceImplUnderTest.taskInfoRepository.findByTaskUID("taskUID")).thenReturn(taskInfo);

		final RestResponse result = employeeServiceImplUnderTest.getUploadStatus("taskUID");
		Assertions.assertThat(result.getMessage()).isEqualTo(taskInfo.getTaskState());
	}

	@Test
	void testCreateAndSaveTaskInfo() {

		final TaskInfo taskInfo = new TaskInfo(0, UUID.randomUUID().toString(), 0L, "Upload", "In Progress");
		when(employeeServiceImplUnderTest.taskInfoRepository.save(any(TaskInfo.class))).thenReturn(taskInfo);

		final TaskInfo result = employeeServiceImplUnderTest.createAndSaveTaskInfo("Upload", "In Progress");
		Assertions.assertThat(result.getTaskState()).isEqualTo(taskInfo.getTaskState());
	}

	@Test
	void testUpdateTaskInfo() {
		final TaskInfo taskInfo = new TaskInfo(1, UUID.randomUUID().toString(), 0L, "action", "In Progress");
		final TaskInfo taskInfo1 = new TaskInfo(1, UUID.randomUUID().toString(), 0L, "action", "Completed");
		when(employeeServiceImplUnderTest.taskInfoRepository.save(any(TaskInfo.class))).thenReturn(taskInfo1);
		employeeServiceImplUnderTest.updateTaskInfo(taskInfo, "status");
	}
}
