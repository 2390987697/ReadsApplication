package com.Reads.base.impl.menu;

import java.util.List;

import com.Reads.base.BaseMenuDetailPager;
import com.Reads.domian.NetData;
import com.Reads.domian.PhotosBean;
import com.Reads.domian.PhotosBean.PhotoNews;
import com.Reads.global.GlobalConstans;
import com.google.gson.Gson;
import com.Reads.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import utils.CacheUtils;

public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private List<PhotoNews> mNewsList;

	private ImageButton btnphoto;

	public PhotoMenuDetailPager(Activity mActivity, final ImageButton btnphoto) {
		super(mActivity);
		this.btnphoto = btnphoto;
		// 组图切换按钮设置
		btnphoto.setOnClickListener(new OnClickListener() {

			private boolean isListView = true;// 标记当前是否是ListView展示

			@Override
			public void onClick(View v) {
				if (isListView) {
					// 切换成GirdView
					lvPhoto.setVisibility(View.GONE);
					gvPhoto.setVisibility(View.VISIBLE);

					btnphoto.setImageResource(R.drawable.icon_pic_list_type);
					isListView = false;
				} else {
					// 切换成ListView
					lvPhoto.setVisibility(View.VISIBLE);
					gvPhoto.setVisibility(View.GONE);

					btnphoto.setImageResource(R.drawable.icon_pic_grid_type);
					isListView = true;
				}
			}
		});
	}

	@Override
	public View initView() {
		// TextView aView = new TextView(mActivity);
		// aView.setText("菜单详情页-组图");
		// aView.setTextColor(Color.RED);
		// aView.setGravity(Gravity.CENTER);
		// aView.setTextSize(22);

		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		// 注意:GridView的布局结构和ListView完全一致,所以
		// 这个可以共用一個AdPater
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		return view;
	}

	@Override
	public void initData() {

		String cache = CacheUtils.getCache(GlobalConstans.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			ProcessedData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstans.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				ProcessedData(result);
				CacheUtils.setCache(GlobalConstans.PHOTOS_URL, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				String result = NetData.DATA_PHOTOS;
				ProcessedData(result);
				CacheUtils.setCache(GlobalConstans.PHOTOS_URL, result, mActivity);
			}
		});
	}

	protected void ProcessedData(String result) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

		mNewsList = photosBean.data.news;

		lvPhoto.setAdapter(new PhotoAdapter());
		gvPhoto.setAdapter(new PhotoAdapter());

	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotoAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			PhotoNews item = getItem(position);// 拿到对应位置的某个对象
			holder.tvTitle.setText(item.title);
			mBitmapUtils.display(holder.ivPic, item.listimage);
			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvTitle;
	}
}
