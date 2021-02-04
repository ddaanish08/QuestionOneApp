package com.question.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.question.one.model.TaskInfo;

@Repository
public interface TaskInfoRepository extends JpaRepository<TaskInfo, Integer> {
	TaskInfo findByTaskUID(String taskUID);
}
