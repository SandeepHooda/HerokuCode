package thread.vo;

public class Feature {

	private String type = "OBJECT_LOCALIZATION";
	private int maxResults = 10;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
}
