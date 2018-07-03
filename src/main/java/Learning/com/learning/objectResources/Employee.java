package Learning.com.learning.objectResources;

import constants.Level;

public class Employee {
	private String username;
	private Company Company;
	private Level level;
	private Case [] caselist;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Company getCompany() {
		return Company;
	}
	public void setCompany(Company company) {
		Company = company;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public Case [] getCaselist() {
		return caselist;
	}
	public void setCaselist(Case [] caselist) {
		this.caselist = caselist;
	}
}
