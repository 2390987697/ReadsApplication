package com.Reads.Fragment;

import java.util.List;
import com.Reads.MainActivity;
import com.Reads.base.BasePager;
import com.Reads.base.impl.NewsCenterPager;
import com.Reads.domian.NewMenuData.NewsMenuDataTitle;
import com.Reads.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 侧边栏 分析:侧边栏ListView 数据源 适配器 以及选中的item子项
 * 
 */

public class LeftMenuFragment extends BaseFragment {

	public ListView ivList;
	public List<NewsMenuDataTitle> mNewMenuData;// 这个是用来接收新闻中心页面传过来的数据
	public int mCurrentPos;// 当前选中的item索引
	private LeftAdpater leftAdpater; // ListView的适配器

	@Override
	public void initDate() {
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.left_list_menu, null);// 加载一个布局返回一个视图对象
		ivList = (ListView) view.findViewById(R.id.iv_list);
		return view;
	}

	/**
	 * 设置侧边栏数据的方法
	 */
	public void setMenuData(List<NewsMenuDataTitle> data) {// 这里的侧边栏数据要从新闻中心页面传过来
		mCurrentPos = 0;// 初始化数据后,把选中的子项归零,否则发生错乱
		mNewMenuData = data;// 将新闻中心页的数据传递给mNewMenuData
		leftAdpater = new LeftAdpater();
		ivList.setAdapter(leftAdpater);// 设置适配器

		ivList.setOnItemClickListener(new OnItemClickListener() {// ListView的点击事件(点击修改详情页中的内容)

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentPos = position;// 更新被选中的item,更新后要刷新ListView

				leftAdpater.notifyDataSetChanged();// 刷新ListView

				// 点击收起侧边栏,这里直接调用BasePager里的toggle()方法
				BasePager aPager = new BasePager(mActivity);
				aPager.toggle();

				setCurrentDetailPager(position);// 点击item后要更改帧布局的详情页
			}
		});
	}

	/**
	 * 这个过程是:先获得MainActivity对象------>>从MainActivity中拿到主页面ContentFragment(fragment是依附在mainActivity中的)
	 * ---->>再从主页面ContentFragment中拿到NewCenterPager标签页------最后在NewCenterPager标签页中进行相关布局设置(前提是先拿到
	 * // 新闻中心页,再修改里面的布局) 从大的拿到小的
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment fragment = mainUI.getContentFragment();
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPagets();// 拿到新闻中心这个页面,注意:这里需要在ContentFragment中定义一个拿新闻中心页面的方法f
		newsCenterPager.setCurrentDetailPager(position); // 拿到后,就修改里面的内容
	}

	class LeftAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			return mNewMenuData.size();// 返回item项的个数 4
		}

		@Override
		public NewsMenuDataTitle getItem(int position) { // 将Object类型换成侧边栏数据类型
			return mNewMenuData.get(position);// 拿到对应的item
		}

		@Override
		public long getItemId(int position) {
			return position; // 返回索引
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {// 这是设置每个item的布局
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
			NewsMenuDataTitle item = getItem(position);
			tvMenu.setText(item.title);// 设置侧边栏文本标题
			if (mCurrentPos == position) {// 被选中的item
				tvMenu.setEnabled(true);// 文字变为红色
			} else {
				tvMenu.setEnabled(false);// 文字为白色
			}
			return view;
		}

	}
}
