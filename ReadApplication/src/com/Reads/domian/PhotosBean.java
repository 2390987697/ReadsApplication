package com.Reads.domian;

import java.util.List;

/**
 * ×éÍ¼¶ÔÏó
 * 
 */
public class PhotosBean {

	public PhotosData data;

	public static class PhotosData {
		public List<PhotoNews> news;
	}

	public static class PhotoNews {
		public int id;
		public String listimage;
		public String title;
	}
}
