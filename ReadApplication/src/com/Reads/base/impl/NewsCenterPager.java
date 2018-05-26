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
 * ��������ҳ�� ���������͵��µ�ѡ��Ͽ�(�м�հ׵Ĳ���) �м��������5������ҳ�ļ���
 * 
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mDetailPagers;// �˵�����ҳ����

	private NewMenuData mNewData;// ������Ϣ��������(������е�����) �����������������ݴ��ݸ��������

	public NewsCenterPager(Activity Activity) {
		super(Activity);
	}

	@Override
	public void initData() {// ��ʼ�����ݵķ���
		// TextView aView = new TextView(mActivity);
		// aView.setText("����");
		// aView.setTextColor(Color.RED);
		// aView.setGravity(Gravity.CENTER);
		// aView.setTextSize(22);
		// flcontent.addView(aView);

		open_left_fragment.setVisibility(View.VISIBLE);

		String cache = CacheUtils.getCache(GlobalConstans.CATEGORY_URL, mActivity);// ���绺��
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);// �л���,�ͽ�������
		}
		getDataFromServer();// ��������
	}

	// �����������,���Ƚ����õ����ݺ�ͨ�����������ݸ������,��Ϊ������������Ҳ����,
	// ���Ծ�����������д,�������������������ݺ�,���ݸ������
	private void getDataFromServer() {// ��������
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstans.CATEGORY_URL, new RequestCallBack<String>() {

			/**
			 * ����ע��:���ڱ���Ŀ�õ��Ǽ�����,�������������ȡ����,���Խ����������ݵĶ�д��ʧ�ܵķ�������,����Ŀ���й������ݵ���������ʧ�ܵ�
			 * ����������
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
				CacheUtils.setCache(GlobalConstans.CATEGORY_URL, result, mActivity);// ����
			}
		});

	}

	public void processData(String json) {// ��������
		Gson gson = new Gson();
		mNewData = gson.fromJson(json, NewMenuData.class);
		System.out.println("mNewData=" + mNewData.toString());
		// �������ݺ�˳���ʼ���˵�����ҳ
		mDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewData.data.get(0).children));
		mDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mDetailPagers.add(new PhotoMenuDetailPager(mActivity, btnphoto));
		mDetailPagers.add(new InteractMenuDetailPager(mActivity));

		// ��������������(˼·:��ͨ��MainActivity���õ����������������)
		MainActivity mianUI = (MainActivity) mActivity;
		LeftMenuFragment leftMenu = mianUI.getLeftMenuFragment();
		leftMenu.setMenuData(mNewData.data);
		// �����Ų˵�����ҳ����ΪĬ��ҳ��

		setCurrentDetailPager(0);

	}

	// ���ò˵�����ҳ
	public void setCurrentDetailPager(int position) {
		// ���¸�fragment�������
		BaseMenuDetailPager pager = mDetailPagers.get(position);

		// ��ʼ������
		pager.initData();

		View view = pager.mRootView;// ��ǰҳ��Ĳ���

		flcontent.removeAllViews();// ���֡���ֵ�����(û�������ô�������ݻ�һ��һ��ĸ���,����֡���ֵ�ȱ��)
		flcontent.addView(view);

		// ����ҳǩ����
		tvtitle.setText(mNewData.data.get(position).title.toString().trim());

		// �������ͼҳ��,��Ҫ��ʾ�л���ť
		if (pager instanceof PhotoMenuDetailPager) {// ��ͼ��ҳ��
			btnphoto.setVisibility(View.VISIBLE);
		} else {
			// �����л���ť
			btnphoto.setVisibility(View.GONE);
		}
	}
}
