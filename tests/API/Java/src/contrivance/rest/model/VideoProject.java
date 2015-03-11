package contrivance.rest.model;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
/*
 * /data/post_project
Body include an object named projectData with : 

 */
public class VideoProject extends JSONObject {
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String OWNER = "owner";
	public static final String CREATED_AT = "createdAt";
	public static final String ORIGINAL_VID_ID = "origId";
	public static final String PARENT_VID_ID = "parentId";
	public static final String VIDEO_LIST = "videoList";
	
	public static final String START_TIME = "startime";
	public static final String END_TIME = "endtime";
	
	/**
	 * Create a new video project from existing project data
	 * 
	 * @param jsonInput the JSON formatted {@code String} conforming to the following format
	 * 	- id
		- name
		- owner
		- createdAt
		- origId  //id of original video
		- parentId //id of parent project
		- videoList //JSOn pbject of the videoId and their start and end time 
			i.e {vid1:{startime:1234, endtime: 2345}, vid2:{starttime:2345,endtime:3345}}
	 * @throws JSONException
	 */
	public VideoProject(String jsonInput) throws JSONException {
		super(jsonInput);
	}
	
	public VideoProject(Map<String, Object> map) throws JSONException {
		super();
		long createdAt = System.currentTimeMillis();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = it.next();
		    this.put(pairs.getKey(), pairs.getValue() );
		}
		if (this.opt(CREATED_AT) == null) {
			this.put(CREATED_AT, createdAt);
		}
		if (this.opt(VIDEO_LIST) == null) {
			this.put(VIDEO_LIST, new JSONObject());
		}
		
	}
	
	/**
	 * @return the id or {@code null} if none exists
	 */
	public String getId() {
		return super.optString(ID);
	}


	/**
	 * @param id the id to set
	 * @throws JSONException 
	 */
	public void setId(String id) {
		try {
			super.put(ID, id);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}


	/**
	 * @return the name or {@code null} if none exists
	 */
	public String getName() {
		return super.optString(NAME);
	}


	/**
	 * @param name the name to set
	 * @throws JSONException 
	 */
	public void setName(String name) {
		try {
			super.put(NAME, name);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}


	/**
	 * @return the owner or {@code null} if none exists
	 */
	public String getOwner() {
		return super.optString(OWNER);
	}


	/**
	 * @param owner the owner to set
	 * @throws JSONException 
	 */
	public void setOwner(String owner) {
		try {
			super.put(OWNER, owner);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}


	/**
	 * @return the createdAt or {@code null} if none exists
	 */
	public String getCreatedAt() {
		return super.optString(CREATED_AT);
	}


	/**
	 * @param createdAt the createdAt to set
	 * @throws JSONException 
	 */
	public void setCreatedAt(String createdAt) {
		try {
			super.put(CREATED_AT, createdAt);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}


	/**
	 * @return the origId or {@code null} if none exists
	 */
	public String getOrigId() {
		return super.optString(ORIGINAL_VID_ID);
	}


	/**
	 * @param origId the origId to set
	 * @throws JSONException 
	 */
	public void setOrigId(String origId) {
		try {
			super.put(ORIGINAL_VID_ID, origId);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}


	/**
	 * @return the parentId or {@code null} if none exists
	 */
	public String getParentId() {
		return super.optString(PARENT_VID_ID);
	}


	/**
	 * @param parentId the parentId to set
	 * @throws JSONException 
	 */
	public void setParentId(String parentId) {
		try {
			super.put(PARENT_VID_ID, parentId);
		} catch (JSONException e) {
			//the key is not null so this will not be thrown
		}
	}
	/**
	 * Gets the start time of a particular video.
	 * @param videoName the video Id
	 * @return the start time expressed as milliseconds from the project start or {@code -1} if none exists
	 */
	public long getVideoStartTime(String videoId) {
		try {
			JSONObject allVids = super.getJSONObject(VIDEO_LIST);
			JSONObject vidTimes = allVids.getJSONObject(videoId);
			return vidTimes.getLong(START_TIME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	/**
	 * Gets the end time of a particular video.
	 * @param videoName the video Id
	 * @return the end time expressed as milliseconds from the project start or {@code -1} if none exists
	 */
	public long getVideoEndTime(String videoId) {
		try {
			JSONObject allVids = super.getJSONObject(VIDEO_LIST);
			JSONObject vidTimes = allVids.getJSONObject(videoId);
			return vidTimes.getLong(END_TIME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	/**
	 * Sets the start time of a video
	 * @param videoId the video id
	 * @param startTime the new start time
	 * @throws JSONException 
	 */
	public void setVideoStartTime(String videoId, long startTime) {
		JSONObject allVids;
		try {
			allVids = super.getJSONObject(VIDEO_LIST);
			JSONObject vidTimes = allVids.getJSONObject(videoId);
			vidTimes.put(START_TIME, startTime);
		} catch (JSONException e) {
			//All fields are required so this should not happen
		}

	}
	/**
	 * Sets the end time of the video
	 * @param videoId the id of the video 
	 * @param endTime the new end time
	 * @throws JSONException 
	 */
	public void setVideoEndTime(String videoId, long endTime) {
		JSONObject allVids;
		try {
			allVids = super.getJSONObject(VIDEO_LIST);
			JSONObject vidTimes = allVids.getJSONObject(videoId);
			vidTimes.put(END_TIME, endTime);
		} catch (JSONException e) {
			//All fields are required so this should not happen
		}
	}
	/**
	 * Adds a video to the project
	 * @param videoId the id of the video to add
	 * @param startTime the start time of the video
	 * @param endTime the end time of the video
	 */
	public void addVideo(String videoId, long startTime, long endTime) {

		try {
			JSONObject allVids = super.getJSONObject(VIDEO_LIST);
			JSONObject vidTimes = new JSONObject();
			vidTimes.put(START_TIME, startTime);
			vidTimes.put(END_TIME, endTime);
			allVids.put(videoId, vidTimes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a video to the project with start and end times ignored.
	 * @param videoId the id of the video to add
	 * @param startTime the start time of the video
	 * @param endTime the end time of the video
	 */
	public void addVideo(String videoId) {
		addVideo(videoId, -1, -1);
	}
	public String asJsonString() {
		return super.toString();
	}
}
