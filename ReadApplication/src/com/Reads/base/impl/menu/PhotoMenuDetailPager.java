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
		// ��ͼ�л���ť����
		btnphoto.setOnClickListener(new OnClickListener() {

			private boolean isListView = true;// ��ǵ�ǰ�Ƿ���ListViewչʾ

			@Override
			public void onClick(View v) {
				if (isListView) {
					// �л���GirdView
					lvPhoto.setVisibility(View.GONE);
					gvPhoto.setVisibility(View.VISIBLE);

					btnphoto.setImageResource(R.drawable.icon_pic_list_type);
					isListView = false;
				} else {
					// �л���ListView
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
		// aView.setText("�˵�����ҳ-��ͼ");
		// aView.setTextColor(Color.RED);
		// aView.setGravity(Gravity.CENTER);
		// aView.setTextSize(22);

		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		// ע��:GridView�Ĳ��ֽṹ��ListView��ȫһ��,����
		// ������Թ���һ��AdPater
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
				// ����ʧ��
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
			PhotoNews item = getItem(position);// �õ���Ӧλ�õ�ĳ������
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
