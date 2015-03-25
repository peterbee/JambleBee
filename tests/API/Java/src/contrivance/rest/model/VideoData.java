package contrivance.rest.model;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoData extends JSONObject {

	public VideoData(String responseString) throws JSONException {
		super(responseString);
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String asJsonString() {
		return super.toString();
	}

}
