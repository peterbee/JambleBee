import junit.framework.TestCase;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.msudenver.jamblebee.model.JSONProjectData;
import edu.msudenver.jamblebee.server.JambleHostConnector;
import edu.msudenver.jamblebee.server.ServerConnector;


public class ServerConnectorTest extends TestCase {

	private static final String HOST = "http://jamble.guygrigsby.com";
	private static final String LOCAL_VID_LOCATION = "../vids";
	private static final String LOCAL_SAVE_LOCATION = "../vids/downloads";
	
	private JambleHostConnector connector;

	public void init() {
		connector = new ServerConnector(HOST);
	}
    @Test
	public void downloadTest() {
		String videoName = "catVideo1";
		
		try {
			connector.downloadVideo(LOCAL_SAVE_LOCATION, videoName);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
    @Test
	public void uploadTest() {
		String fileName = LOCAL_VID_LOCATION + "/catVideo.mp4";
		try {
			connector.uploadVideo(fileName);
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

	}
    @Test
	public void upDownTest() {
		String[] vids = { 
				LOCAL_VID_LOCATION + "/catVideo.mp4",
				LOCAL_VID_LOCATION + "/catVideo1.mp4",
				LOCAL_VID_LOCATION + "/catVideo2.mp4",
				LOCAL_VID_LOCATION + "/catVideo3.mp4" 
		};
		for (String videoName : vids) {
			try {
				connector.uploadVideo(videoName);
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
		}
		List<String> downloadPaths = new ArrayList<String>();
		for (String videoName : vids) {
			try {
				downloadPaths
						.add(connector.downloadVideo(LOCAL_SAVE_LOCATION, videoName));
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
		}
	}
    @Test
	public void uploadDownloadProjectDataTest() {

		try {
            JSONProjectData projectData = new JSONProjectData(getTestValues());
			String res = connector.uploadProjectData(projectData);
			System.out.println("UPLOAD RESPONSE = "+res);
			
			String id = projectData.getId();
            JSONProjectData downloadedProject = (JSONProjectData)connector.downloadProjectData(id);
			
			Iterator<String> iter = projectData.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(JSONProjectData.CREATED_AT)) {
					continue;
				}
				String downloaded = downloadedProject.optString(key);
				String correct = projectData.optString(key);
				assertEquals(correct, downloaded);
			}
		} catch (JSONException e) {
			fail(e.getLocalizedMessage());
		} catch (ClientProtocolException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
    private Map<String, Object> getTestValues() {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(JSONProjectData.ID, "" + System.currentTimeMillis());
		values.put(JSONProjectData.NAME, "TestName");
		values.put(JSONProjectData.OWNER, "TestOwner");
		values.put(JSONProjectData.ORIGINAL_VID_ID, "TestOriginalID");
		values.put(JSONProjectData.PARENT_VID_ID, "TestParentID");
		return values;
	}

}
