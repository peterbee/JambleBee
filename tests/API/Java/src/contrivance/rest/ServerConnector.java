package contrivance.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import contrivance.rest.model.VideoData;
import contrivance.rest.model.VideoProject;

public class ServerConnector {

	private static final String GET_PROJECT_DATA = "/data/get_project";
	private static final String GET_VIDEO_DATA = "/data/get_video";
	private static final String POST_PROJECT_DATA = "/data/post_project";
	private static final String POST_VIDEO_DATA = "/data/post_video	";

	private String host;
	/**
	 * 
	 * @param hostName the fully qualified host name (eg http://guygrigsby.com:3000)
	 */
	public ServerConnector(String hostName) {
		host = hostName;
	}

	public String downloadVideo(String saveLocation,
			String videoName) throws ClientProtocolException, IOException {
		Download dl = new VideoDownload(host, videoName);
		HttpResponse response = dl.executeRequest();
		dl.saveFile(response, saveLocation);
		return saveLocation;
	}

	public void uploadVideo(String fileName) throws ClientProtocolException,
			IOException {
		Path filePath = Paths.get(fileName);
		Upload ul = new VideoUpload(host, filePath);
		ul.executeRequest();
	}
	
	public String downloadProjectData(String projectId) throws ClientProtocolException, IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_PROJECT_DATA, projectId));
		String responseString = executeMethodWithJSONResponse(get);
		return responseString;
		
	}
	
	public VideoData downloadVideoData(String videoId) throws ClientProtocolException, IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_VIDEO_DATA, videoId));
		String responseString = executeMethodWithJSONResponse(get);
		return new VideoData(responseString);

		
	}
	
	public String uploadProjectData(VideoProject projectData) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s/%s", host,
				POST_PROJECT_DATA, projectData.getId()));
		StringEntity entity = new StringEntity(projectData.asJsonString(), ContentType.create("application/json"));
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return responseString;
		
	}
	public JSONObject uploadVideoData(VideoData videoData) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s/%s", host,
				POST_VIDEO_DATA, videoData.getId()));
		StringEntity entity = new StringEntity(videoData.asJsonString(), ContentType.create("application/json"));
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return new JSONObject(responseString);
		
	}
	
	private String executeMethodWithJSONResponse(HttpUriRequest request) throws ClientProtocolException, IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpResponse response = httpclient.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
		    result.append(line);
		}

		return result.toString();
	}

}
