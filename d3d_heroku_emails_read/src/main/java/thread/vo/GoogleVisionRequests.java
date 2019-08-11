package thread.vo;

import java.util.ArrayList;
import java.util.List;

public class GoogleVisionRequests {
	private Image image = new Image();
	private List<Feature> features = new ArrayList<Feature>();
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<Feature> getFeatures() {
		return features;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

}
