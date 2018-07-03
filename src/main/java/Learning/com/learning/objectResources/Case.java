package Learning.com.learning.objectResources;

import constants.CaseState;

public class Case {
	private String name;
	private CaseState state;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CaseState getState() {
		return state;
	}
	public void setState(CaseState state) {
		this.state = state;
	}
}
