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
 * ����� ����:�����ListView ����Դ ������ �Լ�ѡ�е�item����
 * 
 */

public class LeftMenuFragment extends BaseFragment {

	public ListView ivList;
	public List<NewsMenuDataTitle> mNewMenuData;// ���������������������ҳ�洫����������
	public int mCurrentPos;// ��ǰѡ�е�item����
	private LeftAdpater leftAdpater; // ListView��������

	@Override
	public void initDate() {
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.left_list_menu, null);// ����һ�����ַ���һ����ͼ����
		ivList = (ListView) view.findViewById(R.id.iv_list);
		return view;
	}

	/**
	 * ���ò�������ݵķ���
	 */
	public void setMenuData(List<NewsMenuDataTitle> data) {// ����Ĳ��������Ҫ����������ҳ�洫����
		mCurrentPos = 0;// ��ʼ�����ݺ�,��ѡ�е��������,����������
		mNewMenuData = data;// ����������ҳ�����ݴ��ݸ�mNewMenuData
		leftAdpater = new LeftAdpater();
		ivList.setAdapter(leftAdpater);// ����������

		ivList.setOnItemClickListener(new OnItemClickListener() {// ListView�ĵ���¼�(����޸�����ҳ�е�����)

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCurrentPos = position;// ���±�ѡ�е�item,���º�Ҫˢ��ListView

				leftAdpater.notifyDataSetChanged();// ˢ��ListView

				// �����������,����ֱ�ӵ���BasePager���toggle()����
				BasePager aPager = new BasePager(mActivity);
				aPager.toggle();

				setCurrentDetailPager(position);// ���item��Ҫ����֡���ֵ�����ҳ
			}
		});
	}

	/**
	 * ���������:�Ȼ��MainActivity����------>>��MainActivity���õ���ҳ��ContentFragment(fragment��������mainActivity�е�)
	 * ---->>�ٴ���ҳ��ContentFragment���õ�NewCenterPager��ǩҳ------�����NewCenterPager��ǩҳ�н�����ز�������(ǰ�������õ�
	 * // ��������ҳ,���޸�����Ĳ���) �Ӵ���õ�С��
	 */
	protected void setCurrentDetailPager(int position) {
		MainActivity mainUI = (MainActivity) mActivity;
		ContentFragment fragment = mainUI.getContentFragment();
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPagets();// �õ������������ҳ��,ע��:������Ҫ��ContentFragment�ж���һ������������ҳ��ķ���f
		newsCenterPager.setCurrentDetailPager(position); // �õ���,���޸����������
	}

	class LeftAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			return mNewMenuData.size();// ����item��ĸ��� 4
		}

		@Override
		public NewsMenuDataTitle getItem(int position) { // ��Object���ͻ��ɲ������������
			return mNewMenuData.get(position);// �õ���Ӧ��item
		}

		@Override
		public long getItemId(int position) {
			return position; // ��������
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {// ��������ÿ��item�Ĳ���
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
			NewsMenuDataTitle item = getItem(position);
			tvMenu.setText(item.title);// ���ò�����ı�����
			if (mCurrentPos == position) {// ��ѡ�е�item
				tvMenu.setEnabled(true);// ���ֱ�Ϊ��ɫ
			} else {
				tvMenu.setEnabled(false);// ����Ϊ��ɫ
			}
			return view;
		}

	}
}
