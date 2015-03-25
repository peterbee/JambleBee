package contrivance.rest;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import contrivance.rest.model.ProjectData;
import contrivance.rest.model.VideoData;

public interface JambleHostConnector {

	/**
	 * Downloads a video in mp4 format from the server
	 * @param saveLocation the absolute path of the local directory to save the video
	 * @param videoId the id of the video
	 * @return the full filename of the downloaded video
	 * @throws IOException if the directory does not exist
	 */
	public abstract String downloadVideo(String saveLocation, String videoId)
			throws IOException;

	/**
	 * Uploads a video file to the server
	 * @param fileName the absolute path of the file
	 * @throws IOException if the file does not exist
	 */
	public abstract void uploadVideo(String fileName) throws IOException;

	/**
	 * Downloads project data from the server
	 * @param projectId the id of the project
	 * @return the project metadata
	 * @throws IOException if the server cannot be reached
	 * @throws JSONException if the server sends back data in an invalid format
	 */
	public abstract ProjectData downloadProjectData(String projectId)
			throws IOException, JSONException;

	/**
	 * Downloads video data from the server
	 * @param projectId the id of the video
	 * @return the video metadata
	 * @throws IOException if the server cannot be reached
	 * @throws JSONException if the server sends back data in an invalid format
	 */
	public abstract VideoData downloadVideoData(String videoId)
			throws IOException, JSONException;

	/**
	 * Uploads project data to the server
	 * @param projectData the data to upload
	 * @return status code //TODO this needs to be determined
	 * @throws IOException if the server cannot be reached
	 * @throws JSONException if the server sends back data in an invalid format
	 */
	public abstract String uploadProjectData(ProjectData projectData)
			throws IOException, JSONException;

	/**
	 * Uploads video data to the server
	 * @param projectData the data to upload
	 * @return status code //TODO this needs to be determined
	 * @throws IOException if the server cannot be reached
	 * @throws JSONException if the server sends back data in an invalid format
	 */
	public abstract JSONObject uploadVideoData(VideoData videoData)
			throws IOException, JSONException;

}