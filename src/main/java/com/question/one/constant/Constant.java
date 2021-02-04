package com.question.one.constant;

public enum Constant {

	IN_PROGRESS("In Progress"),
	COMPLETED("Completed");

	private final String state;

	Constant(final String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return state;
	}
}
