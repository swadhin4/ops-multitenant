package com.pms.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJava {

	public static void main(String[] args) {

		List<Map<Integer,Integer>> testList=new ArrayList<Map<Integer,Integer>>();
		Map<Integer,Integer> mapData=new HashMap<Integer, Integer>();
		mapData.put(1, 2);
		mapData.put(2, 4);
		
		Map<Integer,Integer> mapData2=new HashMap<Integer, Integer>();
		mapData2.put(10, 11);
		mapData2.put(11, 14);
		
		testList.add(mapData);
		testList.add(mapData2);
		System.out.println(testList);
	}

}
