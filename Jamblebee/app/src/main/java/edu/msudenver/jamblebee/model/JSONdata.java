package edu.msudenver.jamblebee.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kim on 4/9/15.
 */
public class JSONdata {
    JSONObject globalObject;

    public JSONdata(){
        globalObject = new JSONObject();
    }
    public void putProject(JSONObject newProject){
        try {
            globalObject.put(String.valueOf(newProject.get("name")), newProject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ///get locations of videos from one project
    public ArrayList<String> getLocations(JSONObject project){
        try {
            return (ArrayList<String>) project.get("locations");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteProject(JSONObject project){
        try {
            if(globalObject.has((String) project.get("name"))) {
                globalObject.remove((String) project.get("name"));
                return true;
            }
            else{
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public JSONObject loadProject(String name){
        try {
            return (JSONObject) globalObject.get("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getProject(String projectName){
        try {
            return (JSONObject) globalObject.get(projectName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getProjectsNames(){
        Iterator<String> list = globalObject.keys();
        ArrayList<String> listNames = new ArrayList<String>();
        while(list.hasNext()){
            listNames.add(list.next()) ;
        }
        return listNames;

    }


}
