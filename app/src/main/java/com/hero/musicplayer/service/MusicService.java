package com.hero.musicplayer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hero.musicplayer.R;
import com.hero.musicplayer.app.MyMusicApplication;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.ui.MainActivity;
import com.hero.musicplayer.util.CommonUtils;
import com.hero.musicplayer.util.Consts;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.RemoteViews;

public class MusicService extends Service implements Consts,
		MediaPlayer.OnPreparedListener {
	/**
	 * 应用程序app
	 */
	private MyMusicApplication app;
	/**
	 * 音乐播放工具
	 */
	private MediaPlayer player;
	/**
	 * List<Music>数据源
	 */
	List<Music> musics;
	/**
	 * 当前播放的歌曲索引
	 */
	private int currentIndex = 0;
	/**
	 * 暂停位置
	 */
	private int pausePosition;
	/**
	 * 广播接收者
	 */
	private BroadcastReceiver receiver;
	/**
	 * 是否开始播放
	 */
	private boolean isStartPlay;
	private String currentFragment = "LocationFragment";
	private DisplayImageOptions options;

	/**
	 * MediaPlayer异步准备监听
	 */
	@Override
	public void onPrepared(MediaPlayer player) {
		// 异步准备完后，播放音乐
		// 跳到暂停位置
		player.seekTo(pausePosition);
		// 播放歌曲
		player.start();
		// 清除暂停位置
		pausePosition = 0;
		// 将播放状态设为开始播放
		isStartPlay = true;
		// 创建Intent对象
		Intent intent = new Intent();
		intent.setAction(INTENT_ACTION_SERVICE_PLAY);
		// 将currentIndex、duration封装到Intent对象中
		intent.putExtra("currentIndex", currentIndex);
		intent.putExtra("duration", player.getDuration());
		// 发送广播：播放
		sendBroadcast(intent);
		// 开启线程
		startThread();
		// 发送通知栏通知
		sendNotification();
	}

	/**
	 * 发送通知栏通知
	 */
	private void sendNotification() {
		// 加载自定义通知栏布局
		final RemoteViews views = new RemoteViews(getPackageName(),
				R.layout.notification_nowplaying_card);
		// 设置自定义通知中的控件的内容
		views.setTextViewText(R.id.notification_title, musics.get(currentIndex)
				.getName());
		views.setTextViewText(R.id.notification_artist, musics
				.get(currentIndex).getArtist());
		// 如果正在播放，则显示暂停按钮，否则播放按钮；
		Bitmap bitmap = null;
		if (player.isPlaying()) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_pause_white);
		} else {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_play_arrow_white);
		}
		views.setImageViewBitmap(R.id.notification_play_pause, bitmap);
		// 设置PendingIntent延时意图对象,点击通知时打开一个新的目标Activity
		// 打开Activity
		Intent activityIntent = new Intent(this, MainActivity.class);
		final PendingIntent activityPi = PendingIntent.getActivity(this, 0,
				activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 暂停/播放
		Intent pauseOrPlayIntent = new Intent(INTENT_ACTION_ACT_PLAY_OR_PAUSE);
		PendingIntent pauseOrPlayPi = PendingIntent.getBroadcast(this, 1,
				pauseOrPlayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_play_pause,
				pauseOrPlayPi);
		// 下一首
		Intent nextIntent = new Intent(INTENT_ACTION_ACT_NEXT);
		PendingIntent nextPi = PendingIntent.getBroadcast(this, 2, nextIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_next, nextPi);
		// 清除通知栏
		Intent clearIntent = new Intent(INTENT_ACTION_ACT_CLEAR_NOTIFICATION);
		PendingIntent clearPi = PendingIntent.getBroadcast(this, 3, clearIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_clear, clearPi);
		// 创建NotificationManager
		final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建Notification通知
		final Notification.Builder builder = new Notification.Builder(this);
		// 获取当前播放歌曲的图片,并加载完图片后发送通知
		String url = "";
		if ("LocationFragment".equals(currentFragment)) {
			url = CommonUtils.getAlbumArtUri(
					musics.get(currentIndex).getAlbumId()).toString();
		} else if ("OnlineFragment".equals(currentFragment)) {
			url = musics.get(currentIndex).getPicture();
		}
		ImageLoader.getInstance().loadImage(url, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// 设置Notification
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.ic_empty_music2);
						views.setImageViewBitmap(R.id.notification_album_art,
								bitmap);
						builder.setContentIntent(activityPi)
								// 为通知设置延时意图
								.setContent(views)
								.setSmallIcon(R.drawable.ic_launcher);
						Notification notification = builder.build();
						notification.flags = Notification.FLAG_NO_CLEAR;
						// 发送通知
						manager.notify(0, notification);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap bitmap) {
						views.setImageViewBitmap(R.id.notification_album_art,
								bitmap);
						// 设置Notification
						builder.setContentIntent(activityPi)
								// 为通知设置延时意图
								.setContent(views)
								.setSmallIcon(R.drawable.ic_launcher);
						Notification notification = builder.build();
						notification.flags = Notification.FLAG_NO_CLEAR;
						// 发送通知
						manager.notify(0, notification);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
	}

	@Override
	public void onCreate() {
		// 设置ImageLoader参数属性
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageOnFail(R.drawable.ic_empty_music2)
				.showImageForEmptyUri(R.drawable.ic_empty_music2).build();

		musics = new ArrayList<Music>();
		musics = ((MyMusicApplication) getApplication()).getMusic();
		// 初始化媒体播放工具
		player = new MediaPlayer();
		// 创建广播接收者
		receiver = new InnerBrodcastReceiver();
		// 创建意图过滤器,过滤Activity发送的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION_ACT_PLAY_OR_PAUSE);
		filter.addAction(INTENT_ACTION_ACT_PREVIOUS);
		filter.addAction(INTENT_ACTION_ACT_NEXT);
		filter.addAction(INTENT_ACTION_ACT_PLAY_NEW);
		filter.addAction(INTENT_ACTION_ACT_SEEK);
		filter.addAction(UPDATE_THE_DATA_OF_SONGS);
		filter.addAction(CURRENT_FRAGMRNT_IS_LOCATION_FRAGMRNT);
		filter.addAction(CURRENT_FRAGMRNT_IS_ONLINE_FRAGMRNT);
		filter.addAction(INTENT_ACTION_ACT_CLEAR_NOTIFICATION);
		// 注册广播接收者
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
				filter);
		registerReceiver(receiver, filter);
		// 为播放工具设置监听器
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// 如果播放完，播放下一首
				if (isStartPlay) {
					next();
				}
			}
		});
	}

	/**
	 * 广播接收者的子类，用于接收Activity发送的广播
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 接收广播，获取Activity发送的Action
			String action = intent.getAction();
			if (UPDATE_THE_DATA_OF_SONGS.equals(action)) {
				currentFragment = intent.getStringExtra("currentFragment");
				MusicData musicData = (MusicData) intent
						.getSerializableExtra("musicData");
				if (musicData != null) {
					// 更新数据源
					musics = musicData.getSongs();
				}
			} else if (INTENT_ACTION_ACT_PLAY_OR_PAUSE.equals(action)) {
				// 如果正在播放，则暂停，否则播放；
				if (player.isPlaying()) {
					pause();
				} else {
					play();
				}
			} else if (INTENT_ACTION_ACT_PREVIOUS.equals(action)) {
				// 播放上一首
				previous();
			} else if (INTENT_ACTION_ACT_NEXT.equals(action)) {
				// 播放下一首
				next();
			} else if (INTENT_ACTION_ACT_PLAY_NEW.equals(action)) {
				// 播放新的歌曲
				play(intent.getIntExtra("position", 0));
			} else if (INTENT_ACTION_ACT_SEEK.equals(action)) {
				// 跳到SeekBar停止的位置
				int percent = intent.getIntExtra("seekTo", 0);
				pausePosition = percent
						* musics.get(currentIndex).getDuration() / 100;
				play();
			} else if (INTENT_ACTION_ACT_CLEAR_NOTIFICATION.equals(action)) {
				//获取NotificationManager
				NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				//根据通知ID清除通知或清除所有通知
				manager.cancelAll();
			}
		}
	}

	/**
	 * 判断是否正在播放
	 */
	public static boolean isRunning;
	/**
	 * 更新进度条线程
	 */
	private UpdateProgressBarThread updateProgressBarThread;

	/**
	 * 开始更新进度条线程
	 */
	private void startThread() {
		if (!isRunning) {
			// 更改线程状态
			isRunning = true;
			updateProgressBarThread = new UpdateProgressBarThread();
			// 开启线程
			updateProgressBarThread.start();
		}
	}

	/**
	 * 停止更新进度条线程
	 */
	private void stopThread() {
		// 更改线程状态
		isRunning = false;
		// 停止线程
		updateProgressBarThread = null;
	}

	/**
	 * 更新进度条子线程
	 */
	public class UpdateProgressBarThread extends Thread {
		@Override
		public void run() {
			// 由于在service中并不能设置进度条，故只能每隔一段时间给Activity发送广播通知更新进度条
			Intent intent = new Intent();
			while (isRunning) {
				// 发送广播给Activity通知更新进度条
				intent.putExtra("currentPosition", player.getCurrentPosition());
				intent.setAction(INTENT_ACTION_SERVICE_UPDATE_PROGRESS);
				sendBroadcast(intent);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 设置为非粘性
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		//获取NotificationManager
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//根据通知ID清除通知或清除所有通知
		manager.cancelAll();
		// 取消接收广播
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		unregisterReceiver(receiver);
		// 停止线程
		stopThread();
		// 判断是否正在播放
		if (player.isPlaying()) {
			player.pause();
		}
		// 释放资源
		player.release();
		// 通知回收资源
		player = null;
	}

	/**
	 * 播放歌曲
	 */
	private void play() {
		try {
			// 重置
			player.reset();
			// 准备数据源
			player.setDataSource(musics.get(currentIndex).getSrc());
			// 采用异步准备及播放
			player.setOnPreparedListener(this);
			player.prepareAsync(); // prepare async to not block main thread
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 播放一首新的歌曲
	 */
	private void play(int position) {
		// 将索引切换到所点击的位置
		currentIndex = position;
		// 清除暂停位置
		pausePosition = 0;
		// 播放
		play();
	}

	/**
	 * 暂停播放
	 */
	private void pause() {
		// 判断是否正在播放
		if (player.isPlaying()) {
			// 记录暂停位置
			pausePosition = player.getCurrentPosition();
			// 暂停播放
			player.pause();
			// 创建Intent对象
			Intent intent = new Intent();
			intent.setAction(INTENT_ACTION_SERVICE_PAUSE);
			// 发送广播：暂停
			sendBroadcast(intent);
			// 停止线程
			stopThread();
			// 发送通知栏通知
			sendNotification();
		}
	}

	/**
	 * 播放上一首
	 */
	private void previous() {
		currentIndex--;
		// 判断是否为第一首
		if (currentIndex < 0) {
			currentIndex = musics.size() - 1;
		}
		// 清除暂停位置
		pausePosition = 0;
		// 播放
		play();
	}

	/**
	 * 播放下一首
	 */
	private void next() {
		currentIndex++;
		// 判断是否为最后一首
		if (currentIndex >= musics.size()) {
			currentIndex = 0;
		}
		// 清除暂停位置
		pausePosition = 0;
		// 播放
		play();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
