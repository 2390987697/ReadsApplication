package com.Reads.domian;

import java.util.List;

/**
 * json½âÎöÀà  
 */
public class NewMenuData {
	public int retcode;
	public List<NewsMenuDataTitle> data;
	public List<Integer> extend;

	public static class NewsMenuDataTitle {
		public int id;
		public String title;
		public int type;
		public List<NewsMenuDataTag> children;

		@Override
		public String toString() {
			return "NewsMenuDataTitle [title=" + title + ", children=" + children + "]";
		}

	}

	public static class NewsMenuDataTag {
		public int id;
		public String title;
		public int type;
		public String url;

		@Override
		public String toString() {
			return "NewsMenuDataTag [title=" + title + "]";
		}

	}

	@Override
	public String toString() {
		return "NewMenuData [data=" + data + "]";
	}

}
