package Learning.com.learning.objectResources;

public class Company {
	private String name;
	private String enumber;
	private Case [] caselist;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnumber() {
		return enumber;
	}
	public void setEnumber(String enumber) {
		this.enumber = enumber;
	}
	public Case [] getCaselist() {
		return caselist;
	}
	public void setCaselist(Case [] caselist) {
		this.caselist = caselist;
	}
}
