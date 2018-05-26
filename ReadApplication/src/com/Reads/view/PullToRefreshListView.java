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
 * 下拉刷新ListView(不包括头布局的头条新闻) 包括判断状态 接口回调 刷新页面等内容
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private static final int STATE_PULL_TO_REFRESH = 1;// 下拉刷新状态
	private static final int STATE_RELEASE_TO_REFRESH = 2;// 松开刷新状态
	private static final int STATE_REFRESH = 3;// 正在刷新状态

	private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前刷新状态

	private View mHeaderView;// 定义头布局(头条新闻添加到listView上面)
	private int mHeaderViewHeight; // 头布局的高度
	private int startY = -1; // 定义默认y坐标

	private int endY; // 移动后的y坐标

	private TextView tvTitle;// 刷新文字
	private TextView tvTime;// 当前时间
	private ImageView ivArrow;// 箭头

	private RotateAnimation animUp; // 动画
	private RotateAnimation animDown;
	private ProgressBar pbProgress;// 进度条

	private View mFooterView;// 足布局
	private int mFooterViewHeight;// 足布局的高度

	/***
	 * 下面是三个构造方法
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
	 * 初始化头布局
	 */

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		this.addHeaderView(mHeaderView);

		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);

		pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		// 隐藏头布局

		mHeaderView.measure(0, 0);// 测量这个布局宽和高
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 注意:这四个值分别是:左、上、又、下,将刷新布局隐藏起来

		initAnim();// 初始化布局的时候初始化动画

		setCurrentTime();

	}

	/**
	 * 初始化脚布局
	 */
	private void initFooter() {
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

		this.setOnScrollListener(this);// 设置滑动监听

	}

	// 设置刷新时间
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
			if (startY == -1) {//// 当用户按住头条新闻的viewPage进行下拉时,
				// ACTION_DOWN会被ViewPager销毁掉,导致startY没有赋值,此处需要重新获取一下
				startY = (int) ev.getY();
			}
			if (mCurrentState == STATE_REFRESH) {
				// 如果是正在刷新,跳出循环
				break;

				// return true ;
				// //这里不能用return,否则挂掉,因为用return的话,必须返回true或false,在这个地方,你是不知道到底是true还是false的
			}

			endY = (int) ev.getY();
			int dy = endY - startY;

			int firstVisiblePosition = getFirstVisiblePosition();// 当前显示的第一个item的位置
			if (dy > 0 && firstVisiblePosition == 0) {// 必须下拉,并且当前显示的是第一个item

				int padding = dy - mHeaderViewHeight;// 下拉滑动的偏移量

				mHeaderView.setPadding(0, padding, 0, 0);// 计算当前下拉控件的padding值
				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {// 下拉时
					// 改为松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 改为下拉刷新
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

				// 完整展示头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				// 4.在刷新过程中进行回调
				if (mListener != null) {
					mListener.onRefresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 初始化箭头动画
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
	 * 根据当前状态刷新界面
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animDown);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.setAnimation(animUp);
			break;
		case STATE_REFRESH:
			tvTitle.setText("正在刷新...");
			// 正在刷新时,显示进度条
			ivArrow.clearAnimation();// 清除箭头动画,否则无法隐藏
			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新结束,收起控件
	 *
	 */
	public void onRefreshComplete(boolean success) {

		if (!isLoadMore) {
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);

			if (success) {// 只有刷新成功之后才能更新时间
				setCurrentTime();// 设置时间
			}
		} else {
			// 加载更多
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏布局
			isLoadMore = false;
		}

	}

	// 3.定义成员变量,接收监听对象
	private OnRefreshListener mListener;

	/**
	 * 2.暴露接口,设置监听
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 1.下拉刷新的回調接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();

		// 下拉加载更多
		public void onLoadMore();
	}

	private boolean isLoadMore;// 标记是否加载更多

	// 滑动状态发生变化
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {// 空闲状态
			int lastVisiblePosition = getLastVisiblePosition();

			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				// 当前显示的是最后一个item并且没有正在加载更多
				// getCount();获得item的个数,这行判断是如果是最后一个item
				// 到底了
				System.out.println("加载更多...");

				isLoadMore = true;
				mFooterView.setPadding(0, 0, 0, 0);// 显示加载更多布局
				setSelection(getCount() - 1);// 将listView显示在最后一个item上,
				// 从而加载更多会直接展示出来,无需手动滑动

				// 通知主界面加载下一页数据
				if (mListener != null) {
					mListener.onLoadMore();
				}

			}
		}
	}

	// 滑动过程回调
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
