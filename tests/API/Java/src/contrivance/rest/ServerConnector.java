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

	private static final Object GET_PROJECT_DATA = "/data/get_project";
	private static final Object GET_VIDEO_DATA = "/data/get_video";
	private static final Object POST_PROJECT_DATA = "/data/post_project";
	private static final Object POST_VIDEO_DATA = "/data/post_video	";

	
	
	public String downloadVideo(String host, String saveLocation,
			String videoName) throws ClientProtocolException, IOException {
		Download dl = new VideoDownload(host, videoName);
		HttpResponse response = dl.executeRequest();
		dl.saveFile(response, saveLocation);
		return saveLocation;
	}

	public void uploadVideo(String host, String fileName) throws ClientProtocolException,
			IOException {
		Path filePath = Paths.get(fileName);
		Upload ul = new VideoUpload(host, filePath);
		ul.executeRequest();
	}
	
	public VideoProject downloadProjectData(String host, String projectId) throws ClientProtocolException, IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_PROJECT_DATA, projectId));
		String responseString = executeMethodWithJSONResponse(get);
		return new VideoProject(responseString);
		
	}
	
	public VideoData downloadVideoData(String host, String videoId) throws ClientProtocolException, IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_VIDEO_DATA, videoId));
		String responseString = executeMethodWithJSONResponse(get);
		return new VideoData(responseString);

		
	}
	
	public JSONObject uploadProjectData(String host, VideoProject projectData) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s/%s", host,
				POST_PROJECT_DATA, projectData.getId()));
		StringEntity entity = new StringEntity(projectData.asJsonString(), ContentType.create("application/json"));
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return new JSONObject(responseString);
		
	}
	public JSONObject uploadVideoData(String host, VideoData videoData) throws ClientProtocolException, IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s/%s", host,
				POST_VIDEO_DATA, videoData.getId()));
		StringEntity entity = new StringEntity(videoData.asJsonString(), ContentType.create("application/json"));
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return new JSONObject(responseString);
		
	}
	
	private String executeMethodWithJSONResponse(HttpUriRequest request) throws ClientProtocolException, IOException, JSONException {
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
