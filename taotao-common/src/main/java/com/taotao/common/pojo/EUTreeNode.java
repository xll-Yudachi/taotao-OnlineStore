package com.taotao.common.pojo;
/**
 * 
 * @ClassName EUTreeNode
 * @Description easyUI树形控件节点格式
 * @Author xll
 * @Date 2018年12月28日 上午9:03:39
 * @Version 1.0
 *
 */
public class EUTreeNode {

	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
