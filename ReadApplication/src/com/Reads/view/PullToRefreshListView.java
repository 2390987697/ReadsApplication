package com.Reads.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.Reads.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * ����ˢ��ListView(������ͷ���ֵ�ͷ������) �����ж�״̬ �ӿڻص� ˢ��ҳ�������
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private static final int STATE_PULL_TO_REFRESH = 1;// ����ˢ��״̬
	private static final int STATE_RELEASE_TO_REFRESH = 2;// �ɿ�ˢ��״̬
	private static final int STATE_REFRESH = 3;// ����ˢ��״̬

	private int mCurrentState = STATE_PULL_TO_REFRESH;// ��ǰˢ��״̬

	private View mHeaderView;// ����ͷ����(ͷ��������ӵ�listView����)
	private int mHeaderViewHeight; // ͷ���ֵĸ߶�
	private int startY = -1; // ����Ĭ��y����

	private int endY; // �ƶ����y����

	private TextView tvTitle;// ˢ������
	private TextView tvTime;// ��ǰʱ��
	private ImageView ivArrow;// ��ͷ

	private RotateAnimation animUp; // ����
	private RotateAnimation animDown;
	private ProgressBar pbProgress;// ������

	private View mFooterView;// �㲼��
	private int mFooterViewHeight;// �㲼�ֵĸ߶�

	/***
	 * �������������췽��
	 *
	 */

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooter();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooter();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooter();
	}

	/**
	 * ��ʼ��ͷ����
	 */

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		this.addHeaderView(mHeaderView);

		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);

		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		// ����ͷ����

		mHeaderView.measure(0, 0);// ����������ֿ�͸�
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// ע��:���ĸ�ֵ�ֱ���:���ϡ��֡���,��ˢ�²�����������

		initAnim();// ��ʼ�����ֵ�ʱ���ʼ������

		setCurrentTime();

	}

	/**
	 * ��ʼ���Ų���
	 */
	private void initFooter() {
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

		this.setOnScrollListener(this);// ���û�������

	}

	// ����ˢ��ʱ��
	private void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = format.format(new Date());

		tvTime.setText(currentTime);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {//// ���û���סͷ�����ŵ�viewPage��������ʱ,
				// ACTION_DOWN�ᱻViewPager���ٵ�,����startYû�и�ֵ,�˴���Ҫ���»�ȡһ��
				startY = (int) ev.getY();
			}
			if (mCurrentState == STATE_REFRESH) {
				// ���������ˢ��,����ѭ��
				break;

				// return true ;
				// //���ﲻ����return,����ҵ�,��Ϊ��return�Ļ�,���뷵��true��false,������ط�,���ǲ�֪��������true����false��
			}

			endY = (int) ev.getY();
			int dy = endY - startY;

			int firstVisiblePosition = getFirstVisiblePosition();// ��ǰ��ʾ�ĵ�һ��item��λ��
			if (dy > 0 && firstVisiblePosition == 0) {// ��������,���ҵ�ǰ��ʾ���ǵ�һ��item

				int padding = dy - mHeaderViewHeight;// ����������ƫ����

				mHeaderView.setPadding(0, padding, 0, 0);// ���㵱ǰ�����ؼ���paddingֵ
				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {// ����ʱ
					// ��Ϊ�ɿ�ˢ��
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// ��Ϊ����ˢ��
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESH;
				refreshState();

				// ����չʾͷ����
				mHeaderView.setPadding(0, 0, 0, 0);

				// 4.��ˢ�¹����н��лص�
				if (mListener != null) {
					mListener.onRefresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// ����ͷ����
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ��ʼ����ͷ����
	 * 
	 */
	private void initAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}

	/**
	 * ���ݵ�ǰ״̬ˢ�½���
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("����ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animDown);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("�ɿ�ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animUp);
			break;
		case STATE_REFRESH:
			tvTitle.setText("����ˢ��...");
			// ����ˢ��ʱ,��ʾ������
			ivArrow.clearAnimation();// �����ͷ����,�����޷�����
			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * ˢ�½���,����ؼ�
	 *
	 */
	public void onRefreshComplete(boolean success) {

		if (!isLoadMore) {
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("����ˢ��");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);

			if (success) {// ֻ��ˢ�³ɹ�֮����ܸ���ʱ��
				setCurrentTime();// ����ʱ��
			}
		} else {
			// ���ظ���
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// ���ز���
			isLoadMore = false;
		}

	}

	// 3.�����Ա����,���ռ�������
	private OnRefreshListener mListener;

	/**
	 * 2.��¶�ӿ�,���ü���
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 1.����ˢ�µĻ��{�ӿ�
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		// �������ظ���
		public void onLoadMore();
	}

	private boolean isLoadMore;// ����Ƿ���ظ���

	// ����״̬�����仯
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {// ����״̬
			int lastVisiblePosition = getLastVisiblePosition();

			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				// ��ǰ��ʾ�������һ��item����û�����ڼ��ظ���
				// getCount();���item�ĸ���,�����ж�����������һ��item
				// ������
				System.out.println("���ظ���...");

				isLoadMore = true;
				mFooterView.setPadding(0, 0, 0, 0);// ��ʾ���ظ��಼��
				setSelection(getCount() - 1);// ��listView��ʾ�����һ��item��,
				// �Ӷ����ظ����ֱ��չʾ����,�����ֶ�����

				// ֪ͨ�����������һҳ����
				if (mListener != null) {
					mListener.onLoadMore();
				}

			}
		}
	}

	// �������̻ص�
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
