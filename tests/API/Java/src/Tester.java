import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class Tester {

	private static final String HOST = "http://guygrigsby.com:3000";
	private static final String LOCAL_SAVE_LOCATION = "/tmp/vids";

	@Test
	public void downloadTest() {

		String videoName = "catVideo";
		downloadVideo(LOCAL_SAVE_LOCATION, videoName);
		// fail("Not yet implemented");
	}

	@Test
	public void uploadTest() {
		String fileName = "/tmp/catVideo.mp4";
		uploadVideo(fileName);

	}
	
	@Test
	public void upDownTest() {
		String[] vids = {"/tmp/catVideo.mp4","/tmp/catVideo1.mp4","/tmp/catVideo2.mp4","/tmp/catVideo3.mp4"};
		for (String videoName : vids) {
			uploadVideo(videoName);
		}
		List<String> downloadPaths = new ArrayList<String>();
		for (String videoName : vids) {
			downloadPaths.add(downloadVideo(LOCAL_SAVE_LOCATION, videoName));
		}
	}
	
	private void uploadVideo(String fileName) {
		Path filePath = Paths.get(fileName);
		Upload ul = new VideoUpload(HOST, filePath);
		try {
			ul.executeRequest();
		} catch (IOException e) {
			//fail(e.getLocalizedMessage());
		}
	}

	private String downloadVideo(String saveLocation, String videoName) {

		Download dl = new VideoDownload(HOST, videoName);
		try {
			HttpResponse response = dl.executeRequest();
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
            	dl.saveFile(response, saveLocation);
            	HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + response.getStatusLine());
            }
		} catch (IOException e) {
			//fail(e.getLocalizedMessage());
		}
		return saveLocation;
	}

}
