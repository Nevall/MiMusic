package com.hero.musicplayer.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hero.musicplayer.R;
import com.hero.musicplayer.app.MyMusicApplication;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.service.MusicService;
import com.hero.musicplayer.util.CommonUtils;
import com.hero.musicplayer.util.Consts;
import com.hero.musicplayer.util.ImageUtils;
import com.hero.musicplayer.view.PlayPauseButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class QuickControlsFragment extends BaseFragment implements 
OnClickListener,OnSeekBarChangeListener,Consts{

	private View rootView;
	private PlayPauseButton mPlayPause;
	private PlayPauseButton mPlayPauseExpanded;
	private View playPauseWrapper;
	private View playPauseWrapperExpanded;
	private ProgressBar mProgress;
	private SeekBar mSeekBar;
	private TextView mTitle;
	private TextView mArtist;
	private TextView mTitleExpanded;
	private TextView mArtistExpanded;
	private ImageView mAlbumArt;
	private ImageView mBlurredArt;
	private ImageView next;
	private ImageView previous;
	public static View topContainer;
	private boolean isTrackingSeekBar = false;
	private MyMusicApplication app;
	private List<Music> data;
	private boolean duetoplaypause = false;
	private int duration;
	private int currentPosition;
	private InnerBrodcastReceiver receiver;
	private String currentFragment = "";
	//	private int percent;

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);
		setViews(rootView);//初始化布局
		setLinstener();//设置监听器
		return rootView;
	}


	@Override
	protected void initData() {
		//获取app及对应的数据源
		app = (MyMusicApplication) mActivity.getApplication();
		data = app.getMusic();
		//注册广播
		//创建广播接收者
		receiver = new InnerBrodcastReceiver();
		//创建意图过滤器,过滤Activity发送的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_THE_DATA_OF_SONGS);
		//注册广播接收者
		LocalBroadcastManager.getInstance(mActivity).registerReceiver(receiver,filter);
	}

	@Override
	public void onResume() {
		super.onResume();
		topContainer = rootView.findViewById(R.id.topContainer);
	}

	@Override
	public void onDestroy() {
		if (receiver != null) {
			//注销广播
			LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	/**
	 * 初始化布局
	 * @param rootView
	 */
	private void setViews(View rootView) {
		mPlayPause = (PlayPauseButton) rootView.findViewById(R.id.play_pause);//大控制台播放或暂停按钮
		mPlayPauseExpanded = (PlayPauseButton) rootView.findViewById(R.id.playpause);//大控制台播放或暂停按钮中的背景图片
		playPauseWrapper = rootView.findViewById(R.id.play_pause_wrapper);//小控制台的播放或暂停按钮
		playPauseWrapperExpanded = rootView.findViewById(R.id.playpausewrapper);//小控制台的播放或暂停按钮中的背景图片
		mProgress = (ProgressBar) rootView.findViewById(R.id.song_progress_normal);//小控制台的进度条
		mSeekBar = (SeekBar) rootView.findViewById(R.id.song_progress);//大控制台的进度条
		mTitle = (TextView) rootView.findViewById(R.id.title);//歌曲名
		mArtist = (TextView) rootView.findViewById(R.id.artist);//歌手
		mTitleExpanded = (TextView) rootView.findViewById(R.id.song_title);//歌曲名
		mArtistExpanded = (TextView) rootView.findViewById(R.id.song_artist);//歌手
		mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art_nowplayingcard);//专辑图片
		mBlurredArt = (ImageView) rootView.findViewById(R.id.blurredAlbumart);//背景图
		next = (ImageView) rootView.findViewById(R.id.next);//大控制台的下一首按钮
		previous = (ImageView) rootView.findViewById(R.id.previous);//大控制台的上一首按钮
		topContainer = rootView.findViewById(R.id.topContainer);//整个xml布局文件
		//获取进度条的LayoutParams对象，用于设置该布局下的控件参数与inflate相似
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProgress.getLayoutParams();
		mProgress.measure(0, 0);
		//设置进度条的左边距，达到进度条的更换效果
		layoutParams.setMargins(0, -(mProgress.getMeasuredHeight() / 2), 0, 0);
		mProgress.setLayoutParams(layoutParams);
		//设置播放按钮的背景颜色，与主题色一致
		mPlayPause.setColor(Color.parseColor("#ffff5722"));
		mPlayPauseExpanded.setColor(Color.WHITE);
	}


	/**
	 * 设置监听器
	 */
	private void setLinstener() {
		playPauseWrapper.setOnClickListener(this);//为小控制台的播放或暂停按钮设置监听器
		playPauseWrapperExpanded.setOnClickListener(this);//为大控制台的播放或暂停按钮设置监听器
		//为进度条设置监听器
		mSeekBar.setOnSeekBarChangeListener(this);
		//为下一首按钮设置监听器
		next.setOnClickListener(this);
		//为上一首设置监听器
		previous.setOnClickListener(this);
	}

	public void startCurrentMusic(Intent intent){
		int currentIndex = intent.getIntExtra("currentIndex",0);
		//显示正在播放的歌曲
		mTitle.setText(data.get(currentIndex).getName());//歌曲名
		mArtist.setText(data.get(currentIndex).getArtist());//歌手名
		mTitleExpanded.setText(data.get(currentIndex).getName());//歌曲名
		mArtistExpanded.setText(data.get(currentIndex).getArtist());//歌手名
		//设置背景图片
		//		if (!duetoplaypause) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.ic_empty_music2)
				.resetViewBeforeLoading(true)
				.build();
		String url = "";
		if ("LocationFragment".equals(currentFragment)) {
			url = CommonUtils.getAlbumArtUri(data.get(currentIndex).getAlbumId()).toString();
		}else if ("OnlineFragment".equals(currentFragment)) {
			url = data.get(currentIndex).getPicture();
		}
		ImageLoader.getInstance().displayImage(url, mAlbumArt,
				options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				Bitmap failedBitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.ic_empty_music2);
				//将图片进行高斯模糊
				if (mActivity != null)
					new setBlurredAlbumArt().execute(failedBitmap);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				//将图片进行高斯模糊
				if (mActivity != null)
					new setBlurredAlbumArt().execute(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
		//		}
		duetoplaypause = false;
		duration = intent.getIntExtra("duration",0);
		mProgress.setMax(duration);
		mSeekBar.setMax(duration);
	}

	public void updateSeekbar(Intent intent) {
		if(duration!=0){
			currentPosition = intent.getIntExtra("currentPosition", 0);
			//更新进度条
			mProgress.setProgress(currentPosition);
			mSeekBar.setProgress(currentPosition);
		}
	}

	//更新播放状态
	public void updateState() {
		if (MusicService.isRunning) {
			if (!mPlayPause.isPlayed()) {
				mPlayPause.setPlayed(true);
				mPlayPause.startAnimation();
			}
			if (!mPlayPauseExpanded.isPlayed()) {
				mPlayPauseExpanded.setPlayed(true);
				mPlayPauseExpanded.startAnimation();
			}
		} else {
			if (mPlayPause.isPlayed()) {
				mPlayPause.setPlayed(false);
				mPlayPause.startAnimation();
			}
			if (mPlayPauseExpanded.isPlayed()) {
				mPlayPauseExpanded.setPlayed(false);
				mPlayPauseExpanded.startAnimation();
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		//判断点击按钮
		switch (v.getId()){
		case R.id.play_pause_wrapper://为小控制台的播放或暂停按钮设置监听器
			//发送广播：播放或暂停
			duetoplaypause = true;
			//改变播放或暂停按钮     
			if (!mPlayPause.isPlayed()) {
				mPlayPause.setPlayed(true);
				mPlayPause.startAnimation();
				mPlayPauseExpanded.setPlayed(true);
				mPlayPauseExpanded.startAnimation();
			} else {
				mPlayPause.setPlayed(false);
				mPlayPause.startAnimation();
				mPlayPauseExpanded.setPlayed(false);
				mPlayPauseExpanded.startAnimation();
			}
			intent.setAction(INTENT_ACTION_ACT_PLAY_OR_PAUSE);
			LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
			break;
		case R.id.playpausewrapper://为大控制台的播放或暂停按钮设置监听器
			//发送广播：播放或暂停
			duetoplaypause = true;
			//改变播放或暂停按钮    
			if (!mPlayPause.isPlayed()) {
				mPlayPause.setPlayed(true);
				mPlayPause.startAnimation();
				mPlayPauseExpanded.setPlayed(true);
				mPlayPauseExpanded.startAnimation();
			} else {
				mPlayPause.setPlayed(false);
				mPlayPause.startAnimation();
				mPlayPauseExpanded.setPlayed(false);
				mPlayPauseExpanded.startAnimation();
			}
			intent.setAction(INTENT_ACTION_ACT_PLAY_OR_PAUSE);
			LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
			break;
		case R.id.previous:
			//发送广播：播放上一首
			intent.setAction(INTENT_ACTION_ACT_PREVIOUS);
			LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
			break;
		case R.id.next:
			//发送广播：播放下一首
			intent.setAction(INTENT_ACTION_ACT_NEXT);
			LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// 当进度条开始拖动时
		isTrackingSeekBar = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// 当停止拖动进度条时
		isTrackingSeekBar = false;
		//获取进度条的拖动百分比
		int currentPosition = seekBar.getProgress();
		//进度条百分比
		int percent = currentPosition*100/duration;
		//向Service发送广播
		Intent intent = new Intent();
		intent.setAction(INTENT_ACTION_ACT_SEEK);
		intent.putExtra("seekTo", percent);
		LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
	}




	//设置专辑图片为模糊背景
	private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

		@Override
		protected Drawable doInBackground(Bitmap... loadedImage) {
			//将专辑图片转为drawable图层
			Drawable drawable = null;
			try {
				//将图片进行高斯模糊
				drawable = ImageUtils.createBlurredImageFromBitmap(loadedImage[0], getActivity(), 6);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			//设置背景图片的淡入淡出的渐变动画过渡，
			if (result != null) {
				if (mBlurredArt.getDrawable() != null) {
					final TransitionDrawable td =
							new TransitionDrawable(new Drawable[]{
									mBlurredArt.getDrawable(),
									result
							});
					mBlurredArt.setImageDrawable(td);
					td.startTransition(400);

				} else {
					mBlurredArt.setImageDrawable(result);
				}
			}
		}

		@Override
		protected void onPreExecute() {

		}
	}

	/**
	 * 广播接收者的子类，用于接收广播
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//接收广播，获取Activity发送的Action
			String action = intent.getAction();
			if (UPDATE_THE_DATA_OF_SONGS.equals(action)) {
				currentFragment  = intent.getStringExtra("currentFragment");
				MusicData musicData = (MusicData)intent.getSerializableExtra("musicData");
				if (musicData != null) {
					//更新数据源
					data = musicData.getSongs();
				}
			}
		}
	}
}
