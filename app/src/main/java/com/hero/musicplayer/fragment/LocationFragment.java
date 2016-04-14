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
	 * List<Music>����
	 */
	private List<Music> data;
	/**
	 * ��ǰ��Ŀ��Application
	 */
	private MyMusicApplication app;
	/**
	 * ListView�ؼ�
	 */
	private ListView lvMusicPlayer;
	/**
	 * ������
	 */
	private MusicAdapter adapter;
	private boolean isTrackingSeekBar = false;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container,
				false);
		// ��ʼ���ؼ�
		setViews(view);
		// ���ü�����
		setListeners();
		// ΪListView�ؼ�����������
		adapter = new MusicAdapter(mActivity, data);
		lvMusicPlayer.setAdapter(adapter);
		return view;
	}

	@Override
	protected void initData() {
		// ��ȡapp����Ӧ������Դ
		data = MusicDaoFactory.newInstance(mActivity).getData();
		// ���͹㲥֪ͨMusicService��������Դ
		MusicData musicdata = new MusicData();
		musicdata.setSongs(data);
		// ���㲥֪ͨ��̨�����������Դ
		Intent intent = new Intent(UPDATE_THE_DATA_OF_SONGS);
		intent.putExtra("musicData", musicdata);
		intent.putExtra("currentFragment", "LocationFragment");
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}

	/**
	 * ��ȡ�ؼ�
	 */
	private void setViews(View view) {
		lvMusicPlayer = (ListView) view.findViewById(R.id.lv_player_music_list); // �����б�
	}

	/**
	 * ���ü�����
	 */
	private void setListeners() {
		lvMusicPlayer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvMusicPlayer.setOnItemClickListener(this);// ListView������
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ������ͼ
		Intent intent = new Intent();
		// �������
		intent.setAction(INTENT_ACTION_ACT_PLAY_NEW);
		intent.putExtra("position", position);
		// ���͹㲥
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}

	/**
	 * �ı�ListView�е�ǰ���ŵĸ�����������ɫ
	 * 
	 * @param intent
	 */
	public void setItemTextColor(Intent intent) {
		int currentIndex = intent.getIntExtra("currentIndex", 0);
		adapter.setSelectedItem(currentIndex);
		adapter.notifyDataSetChanged();
	}

	/**
	 * �ı�ListView�е�ǰ���ŵĸ���ͼ��״̬
	 */
	public void setIconState(boolean isRunning) {
		adapter.isRunning = isRunning;
		adapter.notifyDataSetChanged();
	}
}
