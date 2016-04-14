package com.hero.musicplayer.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Android on 2016/3/5.
 */
public abstract class BaseFragment extends Fragment {
	protected Activity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActivity = getActivity();
		initData();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 加载数据
	 */
	protected void initData() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mActivity = null;
	}

	/**
	 * 加载布局文件
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract View initView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);
}
