package com.Reads.base.impl;

import java.util.ArrayList;
import com.Reads.MainActivity;
import com.Reads.Fragment.LeftMenuFragment;
import com.Reads.base.BaseMenuDetailPager;
import com.Reads.base.BasePager;
import com.Reads.base.impl.menu.InteractMenuDetailPager;
import com.Reads.base.impl.menu.NewsMenuDetailPager;
import com.Reads.base.impl.menu.PhotoMenuDetailPager;
import com.Reads.base.impl.menu.TopicMenuDetailPager;
import com.Reads.domian.NetData;
import com.Reads.domian.NewMenuData;
import com.Reads.global.GlobalConstans;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import utils.CacheUtils;

/**
 * 新闻中心页面 除标题栏和底下单选组合框(中间空白的部分) 中间的内容是5个详情页的集合
 * 
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mDetailPagers;// 菜单详情页集合

	private NewMenuData mNewData;// 分类信息网络数据(侧边栏中的数据) 这里就在这里解析数据传递给侧边栏了

	public NewsCenterPager(Activity Activity) {
		super(Activity);
	}

	@Override
	public void initData() {// 初始化数据的方法
		// TextView aView = new TextView(mActivity);
		// aView.setText("新闻");
		// aView.setTextColor(Color.RED);
		// aView.setGravity(Gravity.CENTER);
		// aView.setTextSize(22);
		// flcontent.addView(aView);

		open_left_fragment.setVisibility(View.VISIBLE);

		String cache = CacheUtils.getCache(GlobalConstans.CATEGORY_URL, mActivity);// 网络缓存
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);// 有缓存,就解析数据
		}
		getDataFromServer();// 请求数据
	}

	// 填充侧边栏数据,首先解析得到数据后通过方法来传递给侧边栏,因为他和新闻中心也关联,
	// 所以就在新闻中心写,从新闻中心里面获得数据后,传递给侧边栏
	private void getDataFromServer() {// 请求数据
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstans.CATEGORY_URL, new RequestCallBack<String>() {

			/**
			 * 这里注意:由于本项目用的是假数据,真数据在这里获取不到,所以解析请求数据的都写在失败的方法中了,本项目所有关于数据的请求都是在失败的
			 * 方法中请求
			 */
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// String result = responseInfo.result;
				// processData(result);
				// CacheUtils.setCache(GlobalConstans.CATEGORY_URL, result,
				// mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				String result = NetData.DATA_TILTE;
				processData(result);
				CacheUtils.setCache(GlobalConstans.CATEGORY_URL, result, mActivity);// 缓存
			}
		});

	}

	public void processData(String json) {// 解析数据
		Gson gson = new Gson();
		mNewData = gson.fromJson(json, NewMenuData.class);
		System.out.println("mNewData=" + mNewData.toString());
		// 解析数据后顺便初始化菜单详情页
		mDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewData.data.get(0).children));
		mDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mDetailPagers.add(new PhotoMenuDetailPager(mActivity, btnphoto));
		mDetailPagers.add(new InteractMenuDetailPager(mActivity));

		// 给侧边栏填充数据(思路:先通过MainActivity来拿到侧边栏在设置数据)
		MainActivity mianUI = (MainActivity) mActivity;
		LeftMenuFragment leftMenu = mianUI.getLeftMenuFragment();
		leftMenu.setMenuData(mNewData.data);
		// 将新闻菜单详情页设置为默认页面

		setCurrentDetailPager(0);

	}

	// 设置菜单详情页
	public void setCurrentDetailPager(int position) {
		// 重新给fragment添加内容
		BaseMenuDetailPager pager = mDetailPagers.get(position);

		// 初始化数据
		pager.initData();

		View view = pager.mRootView;// 当前页面的布局

		flcontent.removeAllViews();// 清除帧布局的内容(没有清除那么数据内容会一层一层的覆盖,这是帧布局的缺陷)
		flcontent.addView(view);

		// 更新页签标题
		tvtitle.setText(mNewData.data.get(position).title.toString().trim());

		// 如果是组图页面,需要显示切换按钮
		if (pager instanceof PhotoMenuDetailPager) {// 组图的页面
			btnphoto.setVisibility(View.VISIBLE);
		} else {
			// 隐藏切换按钮
			btnphoto.setVisibility(View.GONE);
		}
	}
}
