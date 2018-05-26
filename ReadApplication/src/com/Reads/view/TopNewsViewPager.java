package com.Reads.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ͷ�������Զ���ViewPager(���ø�ViewPager�Ļ����¼�)
 */
public class TopNewsViewPager extends ViewPager {

	// �ƶ�֮ǰ������
	private int startX;
	private int startY;

	// ϰ����д������췽��
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	/**
	 * 1.���»��� ��Ҫ���� 2.���һ��� ���һ������ҵ�ǰ�ǵ�һ��ҳ��,��Ҫ���� 3.���ҵ�ǰ�����һ��ҳ��,��Ҫ����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);// �տ�ʼ���������¼�
		// ��ô�ж������»���,�������һ���?��ˮƽ�������ֱ������ĸ�ƫ������
		switch (ev.getAction()) {// ����¼��Ķ���
		case MotionEvent.ACTION_DOWN:// ����

			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:// �ƶ�
			// �ƶ�֮�������
			int endX = (int) ev.getX();
			int endY = (int) ev.getY();

			// ����ƫ����
			int dx = endX - startX;// ˮƽ�����ƫ����
			int dy = endY - startY;// ��ֱ������ƫ����

			if (Math.abs(dy) < Math.abs(dx)) { // ������ֵ���ж�ƫ����,ˮƽ�����ƫ�������ʾ���һ���

				int currentItem = getCurrentItem();// ���ViewPager�ĵ�ǰҳ��
				// ��ô�ж������󻬶�,�������һ�����?ֻ��Ҫ�ж�dx>0����dx<0������
				// dx>0���һ��� dx<0���󻬶�
				if (dx > 0) {
					// ���һ���
					if (currentItem == 0) {
						// ��һ��ҳ��,��Ҫ����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// ����
					int count = getAdapter().getCount();// (item����)
														// ͼƬ���ǹ̶�ֵ,��Ҫ���item�ĸ���,����ȷ�����һ��ͼƬ
					if (currentItem == count - 1) {
						// ���һ��ҳ��,��Ҫ����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {// ���»���,��Ҫ����
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
