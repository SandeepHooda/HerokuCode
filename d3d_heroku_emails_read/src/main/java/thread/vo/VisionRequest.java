package thread.vo;

import java.util.ArrayList;
import java.util.List;

public class VisionRequest {

	private List<GoogleVisionRequests> requests =new ArrayList<GoogleVisionRequests>() ;

	public List<GoogleVisionRequests> getRequests() {
		return requests;
	}

	public void setRequests(List<GoogleVisionRequests> requests) {
		this.requests = requests;
	}
	
	public static VisionRequest getRequestVo() {
		VisionRequest vo = new VisionRequest();
		
		GoogleVisionRequests visionRequest = new GoogleVisionRequests();
		Feature feature = new Feature();
		visionRequest.getFeatures().add(feature);
		vo.getRequests().add(visionRequest);
		return vo;
	}
}
