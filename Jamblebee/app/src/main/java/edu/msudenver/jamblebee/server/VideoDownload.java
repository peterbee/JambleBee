package edu.msudenver.jamblebee.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoDownload implements Download {

	public static final String DOWNLOAD_API_CALL = "/videos/download";

	private String videoName;
	private String host;
	/**
	 * 
	 * @param hostIn the host running the server
	 * @param videoNameIn the name of the video without extension
	 */
	public VideoDownload(String hostIn, String videoNameIn) {
		videoName = videoNameIn;
		host = hostIn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Download#executeRequest()
	 */
	@Override
	public HttpResponse executeRequest() throws ClientProtocolException,
			IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				DOWNLOAD_API_CALL, videoName));
		return httpclient.execute(get);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Download#saveFile(org.apache.http.HttpResponse, java.lang.String)
	 */
	@Override
	public String saveFile(HttpResponse response, String saveLocation)
			throws IllegalStateException, IOException {
		String fullFilePath = String.format("%s/%s.mp4", saveLocation,
				videoName);
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			InputStream input = null;
			OutputStream output = null;
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
		} else {
			throw new ClientProtocolException(
					"Cannot save: Unexpected response status: "
							+ response.getStatusLine());
		}

		return fullFilePath;
	}

}
