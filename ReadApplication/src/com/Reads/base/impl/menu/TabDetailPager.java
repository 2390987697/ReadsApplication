package com.Reads.base.impl.menu;

import java.util.List;
import com.Reads.NewsDetaiActivity;
import com.Reads.base.BaseMenuDetailPager;
import com.Reads.domian.NetData;
import com.Reads.domian.NewsTabBean;
import com.Reads.domian.NewMenuData.NewsMenuDataTag;
import com.Reads.domian.NewsTabBean.NewsData;
import com.Reads.domian.NewsTabBean.TopNews;
import com.Reads.global.GlobalConstans;
import com.Reads.view.PullToRefreshListView;
import com.Reads.view.TopNewsViewPager;
import com.Reads.view.PullToRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.Reads.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import utils.CacheUtils;
import utils.PrefUtils;

/**
 * ҳǩҳ�����,�������Ų˵�����ҳ�е�����,���Ա��������Ų˵�����ҳ�����ʼ��
 * ע�������и��ɻ�:����������Ų˵�����ҳ���������,Ϊʲô��д������,��Ҫд��TabDetailPager������? ����
 * NewsMenuDetailPager
 * �Ĳ�����TabPageIndicator��ViewPager,ViewPager������NewsMenuDetailPagerҳ��.
 * ViewPager�����������Ҫ������װҲ���ǵ���������һ����TabDetailPager,��չʾ����,�����TabPageIndicator��
 * ��ΪViewPager��һ��itemҳ��,�����������ʵ��ÿһ��itemҳ��,���˵�����ҳֻ�ǽ�ViewPager��������˽���.
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsMenuDataTag mTabData;// ����ҳǩ����������
	// private TextView view;

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mViewPager;// ͷ������ҳ��

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;// ͷ������ҳ��ָʾ��(СԲ��)

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;// ͷ�����ŵı���

	@ViewInject(R.id.lv_list)
	private PullToRefreshListView lvList;

	private String mUrl;// ��������

	private List<TopNews> mTopNews;// ͷ������
	private List<NewsData> mNewsList;// ���Ŷ���

	private NewsAdapter mNewsAdapter;

	private String mMoreUrl;// ��һҳ��������

	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsMenuDataTag newsTabData) {
		super(activity);
		mTabData = newsTabData;

		mUrl = GlobalConstans.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		// Ҫ��֡������䲼�ֶ���
		// view = new TextView(mActivity);
		//
		// // view.setText(mTabData.title);//�˴���ָ��
		//
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		// ��ListView���ͷ����
		View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeaderView);// �˴����뽫ͷ����Ҳע��
		lvList.addHeaderView(mHeaderView);

		// 5.ǰ�˽������ûص�
		lvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// ˢ������
				getDataFromServer();
			}

			/**
			 * �������ظ���
			 */
			@Override
			public void onLoadMore() {
				// �ж��Ƿ�����һҳ����
				if (!TextUtils.isEmpty(mMoreUrl)) { // mMoreUrl != null
					// ����һҳ
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "������", Toast.LENGTH_SHORT).show();

					// û������ʱҲҪ����ؼ�
					lvList.onRefreshComplete(true);
				}
			}

		});
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int headerViewsCount = lvList.getHeaderViewsCount();// ���ͷ���ֵ�����
				position = position - headerViewsCount;// ��Ҫ��ȥͷ���ֵ�ռλ

				System.out.println("��" + position + "���������");

				NewsData news = mNewsList.get(position);

				// ����Ѷ���δ��
				// read_ids:1101 ,1102,1105,1205
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");

				if (!readIds.contains(news.id + "")) {// ֻ�в�������ǰ��id,��׷��,�����ظ����ͬһ��id
					readIds = readIds + news.id + ","; // ��һ�ν������ֵ
					// readIds����""+"1101"+","����1101,
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}

				// Ҫ���������item��������ɫ��Ϊ��ɫ �ֲ�ˢ��,view������ǵ�ǰ������Ķ���
				TextView tvTitle1 = (TextView) view.findViewById(R.id.tv_title);
				tvTitle1.setTextColor(Color.GRAY);

				// mNewsAdapter.notifyDataSetChanged();// ȫ��ˢ��,�˷�����

				// ������������ҳ��
				Intent intent = new Intent(mActivity, NewsDetaiActivity.class);

				intent.putExtra("url", news.url);// ���ListView��������Ӵ��ݸ���һ��Activityҳ��

				mActivity.startActivity(intent);// ��ǰҳ�治��Activityҳ��,����ֱ��ʹ��startActivity();����
				// Ҫͨ��mActivityu������

			}

		});

		return view;
	}

	@Override
	public void initData() {
		// view.setText(mTabData.title);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache, false);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// String result = responseInfo.result;
				// processData(result,false);
				//
				// CacheUtils.setCache(mUrl, result, mActivity);

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// ����ʧ��
				error.printStackTrace();

				String result = NetData.DATA_CHANNEL_BEIJING;
				processData(result, false);

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(true);
			}
		});
	}

	/**
	 * ������һҳ����
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// String result = responseInfo.result;
				// processData(result, true);
				//
				// // CacheUtils.setCache(mUrl, result, mActivity);
				//
				// // ��������ˢ�¿ؼ�
				// lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// ����ʧ��
				error.printStackTrace();

				String result = NetData.DATA_CHANNEL_BEIJING1;
				processData(result, true);

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(false);
			}
		});

	}

	protected void processData(String result, boolean isMore) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);

		String moreUrl = newsTabBean.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GlobalConstans.SERVER_URL + moreUrl;
		} else {
			mMoreUrl = null;
		}

		if (!isMore) {
			// ͷ�������������
			mTopNews = newsTabBean.data.topnews;
			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);// ���շ�ʽչʾ

				// �¼�Ҫ���ø�Indicator
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// ����ͷ�����ű���
						TopNews topNews = mTopNews.get(position);
						tvTitle.setText(topNews.title);
					}

					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

				// ���µ�һ��ͷ�����ű���
				tvTitle.setText(mTopNews.get(0).title);
				mIndicator.onPageSelected(0);// Ĭ���õ�һ��ѡ��(���ҳ�����ٺ����³�ʼ��ʱ,Indicator��Ȼ�����ϴ�Բ��λ�õ�bug)
			}

			// �б�����
			mNewsList = newsTabBean.data.news;
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
			// ʵ��ͼƬ�Զ��ֲ�Ч��
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						int currentItem = mViewPager.getCurrentItem();// �õ���ǰ��ViewPager
						currentItem++;

						if (currentItem > mTopNews.size() - 1) {
							currentItem = 0;// ����Ѿ��������һ��ҳ��,������һҳ
						}
						mViewPager.setCurrentItem(currentItem);

						mHandler.sendEmptyMessageDelayed(0, 2000);// ����������ʱ2�����Ϣ,�γ�һ����ѭ��

					}
				};

				mHandler.sendEmptyMessageDelayed(0, 2000);// ������ʱ2�����Ϣ

				// ����ViewPager�Ĵ�����ס�¼�
				mViewPager.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// ֹͣ����Զ��ֲ�
							// ɾ��Handle��������Ϣ
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							// @Override
							// public void run() {
							// // �����߳�����
							// }
							// });
							break;
						case MotionEvent.ACTION_CANCEL:// ȡ���¼�,������ViewPager��,ֱ�ӻ���ListView,����̧��ʱ���޷���Ӧ,������Ӧ���¼�
							System.out.println("ACTION_CANCEL");
							// �������
							mHandler.sendEmptyMessageDelayed(0, 2000);// ������ʱ2�����Ϣ
							break;
						case MotionEvent.ACTION_UP:
							// �������
							mHandler.sendEmptyMessageDelayed(0, 2000);// ������ʱ2�����Ϣ
						default:
							break;
						}
						return false;
					}
				});

			}

		} else {
			// ���ظ�������
			List<NewsData> moreNews = newsTabBean.data.news;
			mNewsList.addAll(moreNews);// ������׷����ԭ���ļ�����

			// ˢ��ListView
			mNewsAdapter.notifyDataSetChanged();
		}

	}

	// ͷ����������������
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;
		private ImageView view;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);// ���ü����е�Ĭ��ͼƬ
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			view = new ImageView(mActivity);
			// view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);// ����ͼƬ���ŷ�ʽ, �����丸�ؼ�

			String imageUrl = mTopNews.get(position).topimage;// ͼƬ��������

			// ����ͼƬ-��ͼƬ���ø�imageview-�����ڴ����-����
			// BitmapUtils-XUtils
			mBitmapUtils.display(view, imageUrl);

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class NewsAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsData getItem(int position) {
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
				convertView = View.inflate(mActivity, R.layout.list_item_news, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// ���ݱ��ؼ�¼������Ѷ���δ��
			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);

			String readIds = PrefUtils.getString(mActivity, "read_ids", "");
			if (readIds.contains(news.id + "")) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

			mBitmapUtils.display(holder.ivIcon, news.listimage);
			return convertView;
		}

	}

	static class ViewHolder {

		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
