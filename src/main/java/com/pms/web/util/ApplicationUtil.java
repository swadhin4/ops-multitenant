package com.pms.web.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationUtil.class);
	public static String getServerUploadLocation(){
		LOGGER.info("Inside ApplicationUtil .. getServerUploadLocation");
		LOGGER.info("Getting server resource storage location...");
		String serverPath = System.getProperty("catalina.base");
		serverPath=serverPath.replaceAll("\\\\","/");
		Path path = FileSystems.getDefault().getPath(serverPath+"/uploads/malay-first-s3-bucket-pms-test/"); 
	 	String fileUploadLocation = path.toString();
	 	LOGGER.info("Resource Storage location : "+ fileUploadLocation);
	 	LOGGER.info("Exit ApplicationUtil .. getServerUploadLocation");
		return fileUploadLocation;
	}
	
	public static String getServerDownloadLocation(){
		LOGGER.info("Inside ApplicationUtil .. getServerDownloadLocation");
		LOGGER.info("Getting server resource storage location...");
		String serverPath = System.getProperty("catalina.base");
		serverPath=serverPath.replaceAll("\\\\","/");
		Path path = FileSystems.getDefault().getPath(serverPath+"/downloads/malay-first-s3-bucket-pms-test/"); 
	 	String fileDownloadLocation = path.toString();
	 	LOGGER.info("Resource Storage location : "+ fileDownloadLocation);
	 	LOGGER.info("Exit ApplicationUtil .. getServerDownloadLocation");
		return fileDownloadLocation;
	}
	
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json != JSONObject.NULL) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	
	public static Date getSqlDate(java.util.Date javaDate){
		java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime()); 
		return sqlDate;
		
	}

	public static Map<String, String> extractResult(Map<String, Object> out) {
		String result1 =  out.get("#result-set-1").toString();
		result1 = result1.replaceAll("\\[", "").replaceAll("\\]","");
		result1 = result1.replaceAll("\\{", "").replaceAll("\\}","");
		    System.out.println(result1);
		    String[] keyValuePairs = result1.split(",");              //split the string to creat key-value pairs
		    Map<String,String> resultMap = new HashMap<>();               

		    for(String pair : keyValuePairs)                        //iterate over the pairs
		    {
		        String[] entry = pair.split("=");                   //split the pairs to get key and value 
		        resultMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		    }
		return resultMap;
	}
	
	public static String makeSQLDateFromString(String dateString){
		DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sqlDateString="";
		
		try {
			java.util.Date oldFormat = df1.parse(dateString);
			sqlDateString =  df2.format(oldFormat);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return sqlDateString;
	}
	
	public static String makeDateStringFromSQLDate(String dateString){
		DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String javaDateString="";
		
		try {
			java.util.Date oldFormat = df2.parse(dateString);
			javaDateString =  df1.format(oldFormat);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return javaDateString;
	}

	public static String getDateStringFromSQLDate(String dateString){
		DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String javaDateString="";
		
		try {
			java.util.Date oldFormat = df2.parse(dateString);
			javaDateString =  df1.format(oldFormat);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return javaDateString;
	}
	public static String bytesConverter(long size){
		    String s = "";
	        double kb = size / 1024;
	        double mb = kb / 1024;
	        double gb = kb / 1024;
	        double tb = kb / 1024;
	        if(size < 1024) {
	            s = size + " Bytes";
	        } else if(size >= 1024 && size < (1024 * 1024)) {
	            s =  String.format("%.2f", kb) + " KB";
	        } else if(size >= (1024 * 1024) && size < (1024 * 1024 * 1024)) {
	            s = String.format("%.2f", mb) + " MB";
	        } else if(size >= (1024 * 1024 * 1024) && size < (1024 * 1024 * 1024 * 1024)) {
	            s = String.format("%.2f", gb) + " GB";
	        } else if(size >= (1024 * 1024 * 1024 * 1024)) {
	            s = String.format("%.2f", tb) + " TB";
	        }
	        return s;
	}
}
