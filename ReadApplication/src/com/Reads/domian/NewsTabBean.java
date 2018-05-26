package com.Reads.domian;

import java.util.List;

/**
 * 
 * 页签详情数据对象
 */
public class NewsTabBean {

	public NewsTab data;

	@Override
	public String toString() {
		return "NewsTabBean [data=" + data + "]";
	}

	public class NewsTab {
		public String more;
		public List<NewsData> news;
		public List<TopNews> topnews;

		@Override
		public String toString() {
			return "NewsTab [more=" + more + ", news=" + news + ", topnews=" + topnews + "]";
		}

	}

	// 新闻列表对象
	public class NewsData {
		public int id;
		public String listimage;
		public String pubdate;
		public String title;

		public String type;
		public String url;
	}

	// 头条新闻
	public class TopNews {
		@Override
		public String toString() {
			return "TopNews [id=" + id + ", topimage=" + topimage + ", pubdate=" + pubdate + ", title=" + title
					+ ", type=" + type + ", url=" + url + "]";
		}

		public int id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}

}
