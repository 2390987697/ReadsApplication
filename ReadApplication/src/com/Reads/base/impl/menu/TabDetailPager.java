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
 * 页签页面对象,属于新闻菜单详情页中的内容,所以必须在新闻菜单详情页里面初始化
 * 注意这里有个疑惑:这个属于新闻菜单详情页里面的内容,为什么不写在那里,而要写在TabDetailPager里面呢? 首先
 * NewsMenuDetailPager
 * 的布局是TabPageIndicator和ViewPager,ViewPager才属于NewsMenuDetailPager页面.
 * ViewPager里面的内容需要单独封装也就是单独定义了一个类TabDetailPager,来展示内容,而这个TabPageIndicator是
 * 作为ViewPager的一个item页面,所以这个类是实现每一个item页面,而菜单详情页只是将ViewPager容器添加了进来.
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsMenuDataTag mTabData;// 单个页签的网络数据
	// private TextView view;

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mViewPager;// 头条新闻页面

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;// 头条新闻页面指示器(小圆点)

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;// 头条新闻的标题

	@ViewInject(R.id.lv_list)
	private PullToRefreshListView lvList;

	private String mUrl;// 网络链接

	private List<TopNews> mTopNews;// 头条新闻
	private List<NewsData> mNewsList;// 新闻对象

	private NewsAdapter mNewsAdapter;

	private String mMoreUrl;// 下一页数据链接

	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsMenuDataTag newsTabData) {
		super(activity);
		mTabData = newsTabData;

		mUrl = GlobalConstans.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		// 要给帧布局填充布局对象
		// view = new TextView(mActivity);
		//
		// // view.setText(mTabData.title);//此处空指针
		//
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		// 给ListView添加头布局
		View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeaderView);// 此处必须将头布局也注入
		lvList.addHeaderView(mHeaderView);

		// 5.前端界面设置回调
		lvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// 刷新数据
				getDataFromServer();
			}

			/**
			 * 下拉加载更多
			 */
			@Override
			public void onLoadMore() {
				// 判断是否有下一页数据
				if (!TextUtils.isEmpty(mMoreUrl)) { // mMoreUrl != null
					// 有下一页
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "到底了", Toast.LENGTH_SHORT).show();

					// 没有数据时也要收起控件
					lvList.onRefreshComplete(true);
				}
			}

		});
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int headerViewsCount = lvList.getHeaderViewsCount();// 获得头布局的数量
				position = position - headerViewsCount;// 需要减去头布局的占位

				System.out.println("第" + position + "个被点击了");

				NewsData news = mNewsList.get(position);

				// 标记已读和未读
				// read_ids:1101 ,1102,1105,1205
				String readIds = PrefUtils.getString(mActivity, "read_ids", "");

				if (!readIds.contains(news.id + "")) {// 只有不包含当前的id,才追加,避免重复添加同一个id
					readIds = readIds + news.id + ","; // 第一次进来这个值
					// readIds等于""+"1101"+","就是1101,
					PrefUtils.setString(mActivity, "read_ids", readIds);
				}

				// 要将被点击的item的文字颜色改为灰色 局部刷新,view对象就是当前被点击的对象
				TextView tvTitle1 = (TextView) view.findViewById(R.id.tv_title);
				tvTitle1.setTextColor(Color.GRAY);

				// mNewsAdapter.notifyDataSetChanged();// 全局刷新,浪费性能

				// 跳到新闻详情页面
				Intent intent = new Intent(mActivity, NewsDetaiActivity.class);

				intent.putExtra("url", news.url);// 点击ListView的子项将链接传递给下一个Activity页面

				mActivity.startActivity(intent);// 当前页面不是Activity页面,不能直接使用startActivity();方法
				// 要通过mActivityu来开启

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

				// 收起下拉刷新控件
				lvList.onRefreshComplete(false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();

				String result = NetData.DATA_CHANNEL_BEIJING;
				processData(result, false);

				// 收起下拉刷新控件
				lvList.onRefreshComplete(true);
			}
		});
	}

	/**
	 * 加载下一页数据
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
				// // 收起下拉刷新控件
				// lvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();

				String result = NetData.DATA_CHANNEL_BEIJING1;
				processData(result, true);

				// 收起下拉刷新控件
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
			// 头条新闻填充数据
			mTopNews = newsTabBean.data.topnews;
			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);// 快照方式展示

				// 事件要设置给Indicator
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// 更新头条新闻标题
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

				// 更新第一个头条新闻标题
				tvTitle.setText(mTopNews.get(0).title);
				mIndicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
			}

			// 列表新闻
			mNewsList = newsTabBean.data.news;
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
			// 实现图片自动轮播效果
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						int currentItem = mViewPager.getCurrentItem();// 拿到当前的ViewPager
						currentItem++;

						if (currentItem > mTopNews.size() - 1) {
							currentItem = 0;// 如果已经到了最后一个页面,跳到第一页
						}
						mViewPager.setCurrentItem(currentItem);

						mHandler.sendEmptyMessageDelayed(0, 2000);// 继续发送延时2秒的信息,形成一个内循环

					}
				};

				mHandler.sendEmptyMessageDelayed(0, 2000);// 发送延时2秒的信息

				// 设置ViewPager的触摸按住事件
				mViewPager.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// 停止广告自动轮播
							// 删除Handle的所有消息
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							// @Override
							// public void run() {
							// // 在主线程运行
							// }
							// });
							break;
						case MotionEvent.ACTION_CANCEL:// 取消事件,当按下ViewPager后,直接滑动ListView,导致抬起时间无法响应,但会响应此事件
							System.out.println("ACTION_CANCEL");
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 2000);// 发送延时2秒的信息
							break;
						case MotionEvent.ACTION_UP:
							// 启动广告
							mHandler.sendEmptyMessageDelayed(0, 2000);// 发送延时2秒的信息
						default:
							break;
						}
						return false;
					}
				});

			}

		} else {
			// 加载更多数据
			List<NewsData> moreNews = newsTabBean.data.news;
			mNewsList.addAll(moreNews);// 将数据追加在原来的集合中

			// 刷新ListView
			mNewsAdapter.notifyDataSetChanged();
		}

	}

	// 头条新闻数据适配器
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;
		private ImageView view;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置加载中的默认图片
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
			view.setScaleType(ScaleType.FIT_XY);// 设置图片缩放方式, 宽高填充父控件

			String imageUrl = mTopNews.get(position).topimage;// 图片下载链接

			// 下载图片-将图片设置给imageview-避免内存溢出-缓存
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

			// 根据本地记录来标记已读和未读
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
