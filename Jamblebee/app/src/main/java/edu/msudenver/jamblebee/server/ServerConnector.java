package edu.msudenver.jamblebee.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.msudenver.jamblebee.model.JSONProjectData;
import edu.msudenver.jamblebee.model.ProjectData;
import edu.msudenver.jamblebee.model.VideoData;

public class ServerConnector implements JambleHostConnector {

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
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#downloadVideo(java.lang.String, java.lang.String)
	 */
	@Override
	public String downloadVideo(String saveLocation,
			String videoId) throws IOException {
		Download dl = new VideoDownload(host, videoId);
		HttpResponse response = dl.executeRequest();
		dl.saveFile(response, saveLocation);
		return saveLocation;
	}
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#uploadVideo(java.lang.String)
	 */
	@Override
	public void uploadVideo(String fileName) throws IOException {
		Upload ul = new VideoUpload(host, fileName);
		ul.executeRequest();
	}
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#downloadProjectData(java.lang.String)
	 */
	@Override
	public ProjectData downloadProjectData(String projectId) throws IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_PROJECT_DATA, projectId));
		String responseString = executeMethodWithJSONResponse(get);
		String json = removeObjectFromArray(responseString);
		return new JSONProjectData(json);
		
	}
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#downloadVideoData(java.lang.String)
	 */
	@Override
	public VideoData downloadVideoData(String videoId) throws IOException, JSONException {
		HttpGet get = new HttpGet(String.format("%s%s/%s", host,
				GET_VIDEO_DATA, videoId));
		String responseString = executeMethodWithJSONResponse(get);
		String json = removeObjectFromArray(responseString);
		return new VideoData(json);

		
	}
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#uploadProjectData(contrivance.rest.model.ProjectData)
	 */
	@Override
	public String uploadProjectData(ProjectData projectData) throws IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s", host,
				POST_PROJECT_DATA));
		StringEntity entity = new StringEntity(projectData.asJsonString(), ContentType.create("application/json").toString());
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return responseString;
		
	}
	/* (non-Javadoc)
	 * @see contrivance.rest.JambleHostConnector#uploadVideoData(contrivance.rest.model.VideoData)
	 */
	@Override
	public JSONObject uploadVideoData(VideoData videoData) throws IOException, JSONException {
		HttpPost post = new HttpPost(String.format("%s%s/%s", host,
				POST_VIDEO_DATA, videoData.getId()));
		StringEntity entity = new StringEntity(videoData.asJsonString(), ContentType.create("application/json").toString());
		post.setEntity(entity);
		String responseString = executeMethodWithJSONResponse(post);
		return new JSONObject(responseString);
		
	}
	/**
	 * Internal method for executing a {@code HttpUriRequest}
	 * @param request the request to be executed
	 * @return the server response
	 * @throws IOException if the server cannot be reached
	 * @throws JSONException if the server sends back data in an invalid format
	 */
	private String executeMethodWithJSONResponse(HttpUriRequest request) throws IOException {
		StringBuffer result = new StringBuffer();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(request);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
			
			String line = "";
			while ((line = rd.readLine()) != null) {
			    result.append(line);
			}
		} finally {
			response.close();
			httpclient.close();
		}
		String res = result.toString();
		return res;
	}
	/**
	 * Currently the server responds with an JSON object wrapped in a JSON array. We need to strip the array.
	 * @param array
	 * @return
	 */
	private String removeObjectFromArray(String array) {
		return array.substring(1, array.length()-1);
	}

}
