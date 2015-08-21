package com.example.demo.news.databeans.message;

public class MessageOpenData {
	private int ret;
	private MessageOpenDataDetail data;
	private String msg;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public MessageOpenDataDetail getData() {
		return data;
	}

	public void setData(MessageOpenDataDetail data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
