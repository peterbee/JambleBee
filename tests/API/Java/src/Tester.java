import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.HttpResponse;
import org.junit.Test;

public class Tester {

	private static final String HOST = "http://guygrigsby.com:3000";

	@Test
	public void downloadTest() {

		String saveLocation = "/tmp";
		String videoName = "catVideo";
		downloadVideo(saveLocation, videoName);
		// fail("Not yet implemented");
	}

	@Test
	public void uploadTest() {
		String fileName = "/tmp/catVideo.mp4";
		Path filePath = Paths.get(fileName);
		VideoUpload ul = new VideoUpload(HOST, filePath);
		try {
			ul.executeRequest();
		} catch (IOException e) {
			//fail(e.getLocalizedMessage());
		}
	}

	private File downloadVideo(String saveLocation, String videoName) {

		VideoDownload dl = new VideoDownload(HOST, videoName);
		try {
			HttpResponse response = dl.executeRequest();

			dl.saveFile(response, saveLocation);
		} catch (IOException e) {
			//fail(e.getLocalizedMessage());
		}
		return new File(saveLocation);
	}

}
