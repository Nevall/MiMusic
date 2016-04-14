package com.hero.musicplayer.fragment;

import java.util.List;

import com.hero.musicplayer.R;
import com.hero.musicplayer.Adapter.MusicAdapter;
import com.hero.musicplayer.app.MyMusicApplication;
import com.hero.musicplayer.dal.MusicDaoFactory;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.util.Consts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LocationFragment extends BaseFragment implements
		OnItemClickListener, Consts {
	/**
	 * List<Music>集合
	 */
	private List<Music> data;
	/**
	 * 当前项目的Application
	 */
	private MyMusicApplication app;
	/**
	 * ListView控件
	 */
	private ListView lvMusicPlayer;
	/**
	 * 适配器
	 */
	private MusicAdapter adapter;
	private boolean isTrackingSeekBar = false;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container,
				false);
		// 初始化控件
		setViews(view);
		// 设置监听器
		setListeners();
		// 为ListView控件创建适配器
		adapter = new MusicAdapter(mActivity, data);
		lvMusicPlayer.setAdapter(adapter);
		return view;
	}

	@Override
	protected void initData() {
		// 获取app及对应的数据源
		data = MusicDaoFactory.newInstance(mActivity).getData();
		// 发送广播通知MusicService更改数据源
		MusicData musicdata = new MusicData();
		musicdata.setSongs(data);
		// 发广播通知后台服务更新数据源
		Intent intent = new Intent(UPDATE_THE_DATA_OF_SONGS);
		intent.putExtra("musicData", musicdata);
		intent.putExtra("currentFragment", "LocationFragment");
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}

	/**
	 * 获取控件
	 */
	private void setViews(View view) {
		lvMusicPlayer = (ListView) view.findViewById(R.id.lv_player_music_list); // 歌曲列表
	}

	/**
	 * 设置监听器
	 */
	private void setListeners() {
		lvMusicPlayer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvMusicPlayer.setOnItemClickListener(this);// ListView监听器
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 创建意图
		Intent intent = new Intent();
		// 添加数据
		intent.setAction(INTENT_ACTION_ACT_PLAY_NEW);
		intent.putExtra("position", position);
		// 发送广播
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}

	/**
	 * 改变ListView中当前播放的歌曲的字体颜色
	 * 
	 * @param intent
	 */
	public void setItemTextColor(Intent intent) {
		int currentIndex = intent.getIntExtra("currentIndex", 0);
		adapter.setSelectedItem(currentIndex);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 改变ListView中当前播放的歌曲图表状态
	 */
	public void setIconState(boolean isRunning) {
		adapter.isRunning = isRunning;
		adapter.notifyDataSetChanged();
	}
}
