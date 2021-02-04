package com.question.one.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "TaskInfo")
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo implements Serializable {
	private static final long serialVersionUID = 3883293199083425385L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private String taskUID;
	@Column
	private long timeTaken;
	@Column
	private String action;
	@Column
	private String taskState;
}
