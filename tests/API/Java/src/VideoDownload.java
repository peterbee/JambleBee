import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class VideoDownload implements Download {
	
	public static final String DOWNLOAD_API_CALL = "/videos/download";
	
	private String videoName;
	private String host;
	
	public VideoDownload(String hostIn, String videoNameIn) {
		videoName = videoNameIn;
		host = hostIn;
	}
	
	/* (non-Javadoc)
	 * @see Download#executeRequest()
	 */
	@Override
	public HttpResponse executeRequest() throws ClientProtocolException, IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(
				String.format("%s%s/%s", host, DOWNLOAD_API_CALL, videoName));
		return httpclient.execute(get);
	}
	
	/* (non-Javadoc)
	 * @see Download#saveFile(org.apache.http.HttpResponse, java.lang.String)
	 */
	@Override
	public String saveFile(HttpResponse response, String saveLocation) throws IllegalStateException, IOException {
		InputStream input = null;
		OutputStream output = null;
		String fullFilePath = String.format("%s/%s.mp4", saveLocation, videoName);
		byte[] buffer = new byte[1024];

		try {
			input = response.getEntity().getContent();
			output = new FileOutputStream(fullFilePath);
			for (int length; (length = input.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException logOrIgnore) {
				}
			if (input != null)
				try {
					input.close();
				} catch (IOException logOrIgnore) {
				}
		}
		return fullFilePath;
	}
	
}
