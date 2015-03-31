package edu.msudenver.jamblebee.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class VideoUpload implements Upload {

	private static final String UPLOAD_API_CALL = "/videos/upload";
	private String videoPath;
	private String host;

	/**
	 * 
	 * @param hostIn
	 *            the host for the API call
	 * @param videoPathIn
	 *            the full path of the local video
	 */
	public VideoUpload(String hostIn, String videoPathIn) {
		videoPath = videoPathIn;
		host = hostIn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Upload#executeRequest()
	 */
	@Override
	public HttpResponse executeRequest() throws ClientProtocolException,
			IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(String.format("%s%s", host,
				UPLOAD_API_CALL));
		File video = new File(videoPath);
		// Create entity which wraps content of the file to be uploaded any and
		// additional parameters
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		/* example for setting a HttpMultipartMode */
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		/* example for adding an image part */
		FileBody fileBody = new FileBody(video);
		builder.addPart("video", fileBody);

		// Add multipart entity object to the HTTP post object
		post.setEntity(builder.build());
		// send the post request to the server
		HttpResponse response = httpclient.execute(post);
		HttpEntity resEntity = response.getEntity();

		// Print the server response:
		if (resEntity != null) {

			/* Checking response */
			if (response != null) {
				InputStream in = response.getEntity().getContent();
				// TODO verify upload
			}
		}
		return response;
	}
}
