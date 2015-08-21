package com.baoyz.swipemenulistview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * 右侧菜单的view，根据添加的菜单，得到最后的view
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenuLayout extends FrameLayout {

	private static final int CONTENT_VIEW_ID = 1;
	private static final int MENU_VIEW_ID = 2;

	private static final int STATE_CLOSE = 0;
	private static final int STATE_OPEN = 1;

	private View mContentView;
	private SwipeMenuView mMenuView;
	private int mDownX;
	private int state = STATE_CLOSE;
	private GestureDetectorCompat mGestureDetector;
	private OnGestureListener mGestureListener;
	private boolean isFling;
	private int MIN_FLING = dp2px(15);
	private int MAX_VELOCITYX = -dp2px(500);
	private ScrollerCompat mOpenScroller;
	private ScrollerCompat mCloseScroller;
	private int mBaseX;
	private int position;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	public SwipeMenuLayout(View contentView, SwipeMenuView menuView) {
		this(contentView, menuView, null, null);
	}
	/***
	 * 
	 * @param contentView 列表listview的view
	 * @param menuView	  菜单view
	 * @param closeInterpolator  为空时，自动创建默认动画
	 * @param openInterpolator   为空时，自动创建默认动画
	 */
	public SwipeMenuLayout(View contentView, SwipeMenuView menuView,
			Interpolator closeInterpolator, Interpolator openInterpolator) {
		super(contentView.getContext());
		mCloseInterpolator = closeInterpolator;
		mOpenInterpolator = openInterpolator;
		mContentView = contentView;
		mMenuView = menuView;
		mMenuView.setLayout(this);
		init();
	}

	private SwipeMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private SwipeMenuLayout(Context context) {
		super(context);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		mMenuView.setPosition(position);
	}

	private void init() {
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				isFling = false;
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) {
				//如果水平移动超过15dp，X轴上的移动速度（velocityX），像素/秒
				//（如果往左滑动的速度够快，那么就执行打开操作，（不需要滑动距离超过一半））
				if ((e1.getX() - e2.getX()) > MIN_FLING
						&& velocityX < MAX_VELOCITYX) {
					isFling = true;
				}
				 Log.i("byz", MAX_VELOCITYX + ", velocityX = " + velocityX);
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
		mGestureDetector = new GestureDetectorCompat(getContext(),
				mGestureListener);

		// mScroller = ScrollerCompat.create(getContext(), new
		// BounceInterpolator());
		if (mCloseInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(),
					mCloseInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}
		if (mOpenInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(),
					mOpenInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}

		LayoutParams contentParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContentView.setLayoutParams(contentParams);
		if (mContentView.getId() < 1) {
			mContentView.setId(CONTENT_VIEW_ID);
		}

		mMenuView.setId(MENU_VIEW_ID);
		mMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		addView(mContentView);
		addView(mMenuView);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/***
	 * 控制item 左侧 menu打开关闭。
	 * 只要touch down，isFling就会被置为false，如果touch move左移速度没有达到要求，<br>
	 * 就在手指离开屏幕是，关闭右侧菜单(因为isFling被置为了false)
	 * 
	 * @param event
	 * @return
	 */
	public boolean onSwipe(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) event.getX();
			isFling = false;
			break;
		case MotionEvent.ACTION_MOVE: //如果已经打开，那么实际显示为滑动的距离，加上菜单的宽度。如果宽度超过菜单
			// Log.i("byz", "downX = " + mDownX + ", moveX = " + event.getX());
			int dis = (int) (mDownX - event.getX());
			if (state == STATE_OPEN) {
				dis += mMenuView.getWidth();
			}
			swipe(dis);
			break;
		case MotionEvent.ACTION_UP:
			//左滑速度够快，或者移动距离超过菜单宽度的一半，那就执行左滑动画
			if (isFling || (mDownX - event.getX()) > (mMenuView.getWidth() / 2)) {
				// open
				smoothOpenMenu();
			} else {
				// close
				smoothCloseMenu();
				return false;
			}
			break;
		}
		return true;
	}

	public boolean isOpen() {
		return state == STATE_OPEN;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	/***
	 * <p>如果距离超过菜单的 宽度，那么不做任何滑动，距离依旧是菜单宽度 。<br>
	 * 如果宽度小于0，说明已经关闭并且往右滑，不做任何操作
	 * 
	 * @param dis 距离
	 */
	private void swipe(int dis) {
		if (dis > mMenuView.getWidth()) {
			dis = mMenuView.getWidth();
		}
		if (dis < 0) {
			dis = 0;
		}
		mContentView.layout(-dis, mContentView.getTop(),
				mContentView.getWidth() - dis, getMeasuredHeight());
		mMenuView.layout(mContentView.getWidth() - dis, mMenuView.getTop(),
				mContentView.getWidth() + mMenuView.getWidth() - dis,
				mMenuView.getBottom());
	}

	@Override
	public void computeScroll() {
		if (state == STATE_OPEN) {
			if (mOpenScroller.computeScrollOffset()) {
				swipe(mOpenScroller.getCurrX());
				postInvalidate();
			}
		} else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe(mBaseX - mCloseScroller.getCurrX());
				postInvalidate();
			}
		}
	}

	public void smoothCloseMenu() {
		state = STATE_CLOSE;
		mBaseX = -mContentView.getLeft();
		mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);
		postInvalidate();
	}

	public void smoothOpenMenu() {
		state = STATE_OPEN;
		int i= mContentView.getLeft();
		mOpenScroller.startScroll(-mContentView.getLeft(), 0,
				mMenuView.getWidth(), 0, 350);
		postInvalidate();
	}

	public void closeMenu() {
		if (mCloseScroller.computeScrollOffset()) {
			mCloseScroller.abortAnimation();
		}
		if (state == STATE_OPEN) {
			state = STATE_CLOSE;
			swipe(0);
		}
	}

	public void openMenu() {
		if (state == STATE_CLOSE) {
			state = STATE_OPEN;
			swipe(mMenuView.getWidth());
		}
	}

	public View getContentView() {
		return mContentView;
	}

	public SwipeMenuView getMenuView() {
		return mMenuView;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mMenuView.measure(MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
				getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mContentView.layout(0, 0, getMeasuredWidth(),
				mContentView.getMeasuredHeight());
		mMenuView.layout(getMeasuredWidth(), 0,
				getMeasuredWidth() + mMenuView.getMeasuredWidth(),
				mContentView.getMeasuredHeight());
	}

	public void setMenuHeight(int measuredHeight) {
		LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
		if (params.height != measuredHeight) {
			params.height = measuredHeight;
			mMenuView.setLayoutParams(mMenuView.getLayoutParams());
		}
	}
}
