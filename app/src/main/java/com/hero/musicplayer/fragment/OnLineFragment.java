package com.hero.musicplayer.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hero.musicplayer.R;
import com.hero.musicplayer.Adapter.OnlineMusicAdapter;
import com.hero.musicplayer.app.MyMusicApplication;
import com.hero.musicplayer.biz.OnlineFragmentBiz;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.util.Consts;

public class OnLineFragment extends BaseFragment implements Consts,OnItemClickListener{
	private OnlineFragmentBiz biz;
	private ListView listView;
	private OnlineMusicAdapter adapter;

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_ONLINE_MUSIC_DATA_SUCCESS:
				Bundle data = msg.getData();
				MusicData onlineMusic = (MusicData)data.getSerializable("onlineMusic");
				if (onlineMusic != null) {
					List<Music> songs = onlineMusic.getSongs();
					//���͹㲥֪ͨ��̨�����������Դ
					Intent intent = new Intent(UPDATE_THE_DATA_OF_SONGS);
					intent.putExtra("musicData", onlineMusic);
					intent.putExtra("currentFragment", "OnlineFragment");
					LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
					//����UI
					adapter.addList(songs);
				}
				break;

			case LOAD_ONLINE_MUSIC_DATA_FAILUE:
				Toast.makeText(mActivity, "�����޷�����", Toast.LENGTH_SHORT).show();
				break;
			}
			return false;
		}
	});


	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online, container, false);
		//��ʼ���ؼ�
		setViews(view);
		//���ü�����
		setListeners();
		return view;
	}

	/**
	 * ��ʼ���ؼ�
	 * @param inflater
	 * @param container
	 */
	private void setViews(View view) {
		//��ʼ���ؼ�
		listView = (ListView)view.findViewById(R.id.lv_online_music_list);
		//��ȡapp����Ӧ������Դ
		adapter = new OnlineMusicAdapter(mActivity, null);
		listView.setAdapter(adapter);
	}

	/**
	 * ���ü�����
	 */
	private void setListeners() {
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(this);//ListView������
	}


	@Override
	protected void initData(){
		//����Http�����ȡ����
		biz = new OnlineFragmentBiz(mActivity);
		biz.loadOnlineMusic(handler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//������ͼ
		Intent intent = new Intent();
		//�������
		intent.setAction(INTENT_ACTION_ACT_PLAY_NEW);
		intent.putExtra("position", position);
		//���͹㲥
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}
	
	/**
	 * �ı�ListView�е�ǰ���ŵĸ�����������ɫ
	 * @param intent
	 */
	public void setItemTextColor(Intent intent){
		int currentIndex = intent.getIntExtra("currentIndex",0);
		adapter.setSelectedItem(currentIndex);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * �ı�ListView�е�ǰ���ŵĸ���ͼ��״̬
	 */
	public void setIconState(boolean isRunning){
		adapter.isRunning = isRunning;
		adapter.notifyDataSetChanged();
	}
}
