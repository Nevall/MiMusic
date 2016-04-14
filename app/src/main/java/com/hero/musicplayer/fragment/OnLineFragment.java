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
					//发送广播通知后台服务更新数据源
					Intent intent = new Intent(UPDATE_THE_DATA_OF_SONGS);
					intent.putExtra("musicData", onlineMusic);
					intent.putExtra("currentFragment", "OnlineFragment");
					LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
					//更新UI
					adapter.addList(songs);
				}
				break;

			case LOAD_ONLINE_MUSIC_DATA_FAILUE:
				Toast.makeText(mActivity, "网络无法连接", Toast.LENGTH_SHORT).show();
				break;
			}
			return false;
		}
	});


	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online, container, false);
		//初始化控件
		setViews(view);
		//设置监听器
		setListeners();
		return view;
	}

	/**
	 * 初始化控件
	 * @param inflater
	 * @param container
	 */
	private void setViews(View view) {
		//初始化控件
		listView = (ListView)view.findViewById(R.id.lv_online_music_list);
		//获取app及对应的数据源
		adapter = new OnlineMusicAdapter(mActivity, null);
		listView.setAdapter(adapter);
	}

	/**
	 * 设置监听器
	 */
	private void setListeners() {
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(this);//ListView监听器
	}


	@Override
	protected void initData(){
		//发送Http请求获取数据
		biz = new OnlineFragmentBiz(mActivity);
		biz.loadOnlineMusic(handler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//创建意图
		Intent intent = new Intent();
		//添加数据
		intent.setAction(INTENT_ACTION_ACT_PLAY_NEW);
		intent.putExtra("position", position);
		//发送广播
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}
	
	/**
	 * 改变ListView中当前播放的歌曲的字体颜色
	 * @param intent
	 */
	public void setItemTextColor(Intent intent){
		int currentIndex = intent.getIntExtra("currentIndex",0);
		adapter.setSelectedItem(currentIndex);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 改变ListView中当前播放的歌曲图表状态
	 */
	public void setIconState(boolean isRunning){
		adapter.isRunning = isRunning;
		adapter.notifyDataSetChanged();
	}
}
