package vo;

import java.util.List;

import thread.vo.LocalizedObjectAnnotation;
import thread.vo.VisionResponse;

public class ResponseVO {


	private List<VisionResponse> responses;
	

	public List<VisionResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<VisionResponse> responses) {
		this.responses = responses;
	}

	public boolean isPersonFound() {
		if (null != responses) {
			for (VisionResponse vo :responses ) {
				if (vo.getLocalizedObjectAnnotations()!= null) {
					for (LocalizedObjectAnnotation annotation: vo.getLocalizedObjectAnnotations()) {
						if ("Person".equalsIgnoreCase(annotation.getName()) || "Woman".equalsIgnoreCase(annotation.getName()) || "Man".equalsIgnoreCase(annotation.getName())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	

	
}
