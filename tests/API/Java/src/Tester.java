import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import contrivance.rest.Download;
import contrivance.rest.Upload;
import contrivance.rest.VideoDownload;
import contrivance.rest.VideoUpload;

public class Tester {

	private static final String HOST = "http://guygrigsby.com:3000";
	private static final String LOCAL_VID_LOCATION = "../../vids";
	private static final String LOCAL_SAVE_LOCATION = "../../vids/downloads";

	@Test
	public void downloadTest() {

		String videoName = "catVideo1";
		try {
			downloadVideo(LOCAL_SAVE_LOCATION, videoName);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void uploadTest() {
		String fileName = LOCAL_VID_LOCATION + "/catVideo.mp4";
		try {
			uploadVideo(fileName);
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
				uploadVideo(videoName);
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
		}
		List<String> downloadPaths = new ArrayList<String>();
		for (String videoName : vids) {
			try {
				downloadPaths
						.add(downloadVideo(LOCAL_SAVE_LOCATION, videoName));
			} catch (IOException e) {
				fail(e.getLocalizedMessage());
			}
		}
	}

	private void uploadVideo(String fileName) throws ClientProtocolException,
			IOException {
		Path filePath = Paths.get(fileName);
		Upload ul = new VideoUpload(HOST, filePath);
		ul.executeRequest();
	}

	private String downloadVideo(String saveLocation, String videoName)
			throws ClientProtocolException, IOException {

		Download dl = new VideoDownload(HOST, videoName);
		HttpResponse response = dl.executeRequest();
		dl.saveFile(response, saveLocation);
		return saveLocation;
	}

}
