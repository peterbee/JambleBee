import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.junit.Test;


public class Tester {
 
	@Test
	public void test() {
		String host = "http://guygrigsby.com:3000";
		String saveLocation = "/tmp";
		String videoName = "catVideo";
		VideoDownload dl = new VideoDownload(host, saveLocation, videoName);
		try {
			HttpResponse response = dl.executeRequest();

			dl.saveFile(response);
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
		
		//fail("Not yet implemented");
	}

}
