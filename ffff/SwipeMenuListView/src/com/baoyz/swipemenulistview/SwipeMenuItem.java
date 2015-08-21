package com.baoyz.swipemenulistview;


import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 左侧菜单对象<br>
 * 一个item对象 title和icon只能设置一个，title会创建textview，icon创建imageview
 * @author xfg
 * @date 2015-3-10
 * 
 */
public class SwipeMenuItem {

	private int id;
	private Context mContext;
	private String title;
	private Drawable icon;
	private Drawable background;
	private int titleColor;
	private int titleSize;
	private int width;

	public SwipeMenuItem(Context context) {
		mContext = context;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTitleColor() {
		return titleColor;
	}

	public int getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public String getTitle() {
		return title;
	}

	/***
	 * 一个item，只能设置文本或是icon，不能同时，否则会添加两边
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/***
	 * 一个item，只能设置文本或是icon，不能同时，否则会添加两边
	 * @param title
	 */
	public void setTitle(int resId) {
		setTitle(mContext.getString(resId));
	}

	public Drawable getIcon() {
		return icon;
	}
	/***
	 * 一个item，只能设置文本或是icon，不能同时，否则会添加两边
	 * @param title
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	/***
	 * 一个item，只能设置文本或是icon，不能同时，否则会添加两边
	 * @param title
	 */
	public void setIcon(int resId) {
		this.icon = mContext.getResources().getDrawable(resId);
	}

	public Drawable getBackground() {
		return background;
	}

	public void setBackground(Drawable background) {
		this.background = background;
	}

	public void setBackground(int resId) {
		this.background = mContext.getResources().getDrawable(resId);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
