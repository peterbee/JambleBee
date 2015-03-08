package contrivance.rest.model;

import java.util.HashMap;
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
	public static final String VIDEO_LIST = "parentId";
	
	public static final String START_TIME = "startime";
	public static final String END_TIME = "endtime";
	
	private Map<String, TimeKeeper> videos;
	
	/**
	 * Create a new video project
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
		videos = new HashMap<String, TimeKeeper>();
		extractVideoList();
	}
	/**
	 * @return the id
	 */
	public String getId() {
		try {
			return super.getString(ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param id the id to set
	 * @throws JSONException 
	 */
	public void setId(String id) throws JSONException {
		super.put(ID, id);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		try {
			return super.getString(NAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param name the name to set
	 * @throws JSONException 
	 */
	public void setName(String name) throws JSONException {
		super.put(NAME, name);
	}


	/**
	 * @return the owner
	 */
	public String getOwner() {
		try {
			return super.getString(OWNER);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param owner the owner to set
	 * @throws JSONException 
	 */
	public void setOwner(String owner) throws JSONException {
		super.put(OWNER, owner);
	}


	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		try {
			return super.getString(CREATED_AT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param createdAt the createdAt to set
	 * @throws JSONException 
	 */
	public void setCreatedAt(String createdAt) throws JSONException {
		super.put(CREATED_AT, createdAt);
	}


	/**
	 * @return the origId
	 */
	public String getOrigId() {
		try {
			return super.getString(ORIGINAL_VID_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param origId the origId to set
	 * @throws JSONException 
	 */
	public void setOrigId(String origId) throws JSONException {
		super.put(ORIGINAL_VID_ID, origId);
	}


	/**
	 * @return the parentId
	 */
	public String getParentId() {
		try {
			return super.getString(PARENT_VID_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param parentId the parentId to set
	 * @throws JSONException 
	 */
	public void setParentId(String parentId) throws JSONException {
		super.put(PARENT_VID_ID, parentId);
	}
	/**
	 * Gets the start time of a particular video.
	 * @param videoName the video Id
	 * @return the start time expressed as milliseconds from the project start, ie time 0.
	 */
	public long getVideoStartTime(String videoId) {
		TimeKeeper keeper = videos.get(videoId);
		if (keeper == null) {
			throw new RuntimeException("Video " + videoId + "does not exist in project");
		}
		return keeper.getStartTime();
	}
	/**
	 * Gets the end time of a particular video.
	 * @param videoName the video Id
	 * @return the end time expressed as milliseconds from the project start, ie time 0.
	 */
	public long getVideoEndTime(String videoId) {
		TimeKeeper keeper = videos.get(videoId);
		if (keeper == null) {
			throw new RuntimeException("Video " + videoId + "does not exist in project");
		}
		return keeper.getEndTime();
	}
	/**
	 * Sets the start time of a video
	 * @param videoId the video id
	 * @param startTime the new start time
	 */
	public void setVideoStartTime(String videoId, long startTime) {
		TimeKeeper keeper = videos.get(videoId);
		if (keeper == null) {
			throw new RuntimeException("Video " + videoId + "does not exist in project");
		}
		keeper.setStartTime(startTime);
	}
	/**
	 * Sets the end time of the video
	 * @param videoId the id of the video 
	 * @param endTime the new end time
	 */
	public void setVideoEndTime(String videoId, long endTime) {
		TimeKeeper keeper = videos.get(videoId);
		if (keeper == null) {
			throw new RuntimeException("Video " + videoId + "does not exist in project");
		}
		keeper.setEndTime(endTime);
	}
	/**
	 * Adds a video to the project
	 * @param videoId the id of the video to add
	 * @param startTime the start time of the video
	 * @param endTime the end time of the video
	 */
	public void addVideo(String videoId, long startTime, long endTime) {
		videos.put(videoId, new TimeKeeper(startTime, endTime));
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


	/**
	 * @return the videoList
	 */
	public void extractVideoList() {
		try {
			JSONObject obj = super.getJSONObject(VIDEO_LIST);
			Iterator<String> iter = obj.keys();
			while (iter.hasNext()) {
				String videoName = iter.next();
				JSONObject times = obj.getJSONObject(videoName);
				int startTime = times.getInt(START_TIME);
				int endTime = times.getInt(END_TIME);
				TimeKeeper keeper = new TimeKeeper(startTime, endTime);
				videos.put(videoName, keeper);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
