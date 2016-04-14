package com.hero.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseAdapter<T> extends android.widget.BaseAdapter{
	/**
	 * �����Ķ���
	 */
	Context context;
	/**
	 * List����
	 */
	List<T> data;
	/**
	 * �������ļ�����ΪView���󹤾�
	 */
	LayoutInflater inflater;
	
	/**
	 * ���췽��
	 * @param context �����Ķ���
	 * @param data ���Դ
	 */
	
	public BaseAdapter(Context context, List<T> data) {
		setContext(context);
		setData(data);
		setInflater(inflater);
	}

	/**
	 * ��ȡ�����Ķ���
	 * @return �����Ķ���
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * ���������Ķ���
	 * @param context �����Ķ��󣬲���Ϊnullֵ�������׳��쳣
	 */
	public void setContext(Context context) {
		if(context == null){
			throw new IllegalArgumentException("参数Context不能为null值！！！");
		}
		this.context = context;
	}

	/**
	 * ��ȡList���Դ����
	 * @return ���Դ
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * ����List���Դ����
	 * @param data ���Դ
	 */
	public void setData(List<T> data) {
		if(data == null){
			data = new ArrayList<T>();
		}
		this.data = data;
	}
	


	/**
	 * ��ȡLayoutInflater����
	 * @return LayoutInflater����
	 */
	public LayoutInflater getInflater() {
		return inflater;
	}

	/**
	 * ����LayoutInflater����
	 * @param inflater LayoutInflater����
	 */
	public void setInflater(LayoutInflater inflater) {
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}
	

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
