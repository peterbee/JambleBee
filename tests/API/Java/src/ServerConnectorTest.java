import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import contrivance.rest.ServerConnector;
import contrivance.rest.model.VideoProject;

public class ServerConnectorTest {

	private static final String HOST = "http://guygrigsby.com:3000";
	private static final String LOCAL_VID_LOCATION = "../../vids";
	private static final String LOCAL_SAVE_LOCATION = "../../vids/downloads";
	
	private ServerConnector connector;
	
	@Before
	public void init() {
		connector = new ServerConnector(HOST);
	}

//	@Test
//	public void downloadTest() {
//		String videoName = "catVideo1";
//		
//		try {
//			connector.downloadVideo(LOCAL_SAVE_LOCATION, videoName);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}
//
//	@Test
//	public void uploadTest() {
//		String fileName = LOCAL_VID_LOCATION + "/catVideo.mp4";
//		try {
//			connector.uploadVideo(fileName);
//		} catch (IOException e) {
//			fail(e.getLocalizedMessage());
//		}
//
//	}
//
//	@Test
//	public void upDownTest() {
//		String[] vids = { 
//				LOCAL_VID_LOCATION + "/catVideo.mp4",
//				LOCAL_VID_LOCATION + "/catVideo1.mp4",
//				LOCAL_VID_LOCATION + "/catVideo2.mp4",
//				LOCAL_VID_LOCATION + "/catVideo3.mp4" 
//		};
//		for (String videoName : vids) {
//			try {
//				connector.uploadVideo(videoName);
//			} catch (IOException e) {
//				fail(e.getLocalizedMessage());
//			}
//		}
//		List<String> downloadPaths = new ArrayList<String>();
//		for (String videoName : vids) {
//			try {
//				downloadPaths
//						.add(connector.downloadVideo(LOCAL_SAVE_LOCATION, videoName));
//			} catch (IOException e) {
//				fail(e.getLocalizedMessage());
//			}
//		}
//	}
	
	@Test
	public void uploadProjectDataTest() {
		Map<String, Object> values = getTestValues();
		VideoProject projectData;
		try {
			projectData = new VideoProject(values);
			String res = connector.uploadProjectData(projectData);
			//System.out.println(res);
		} catch (JSONException e) {
			fail(e.getLocalizedMessage());
		} catch (ClientProtocolException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void downloadProjectDataTest() {

		try {
			String id = "TestID";
			String res = connector.downloadProjectData(id);
			System.out.println(res);
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
		values.put(VideoProject.ID, "TestID");
		values.put(VideoProject.NAME, "TestName");
		values.put(VideoProject.OWNER, "TestOwner");
		values.put(VideoProject.ORIGINAL_VID_ID, "TestOriginalID");
		values.put(VideoProject.PARENT_VID_ID, "TestParentID");
		return values;
	}

}
