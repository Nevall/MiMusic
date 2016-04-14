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
	 * Ӧ�ó���app
	 */
	private MyMusicApplication app;
	/**
	 * ���ֲ��Ź���
	 */
	private MediaPlayer player;
	/**
	 * List<Music>����Դ
	 */
	List<Music> musics;
	/**
	 * ��ǰ���ŵĸ�������
	 */
	private int currentIndex = 0;
	/**
	 * ��ͣλ��
	 */
	private int pausePosition;
	/**
	 * �㲥������
	 */
	private BroadcastReceiver receiver;
	/**
	 * �Ƿ�ʼ����
	 */
	private boolean isStartPlay;
	private String currentFragment = "LocationFragment";
	private DisplayImageOptions options;

	/**
	 * MediaPlayer�첽׼������
	 */
	@Override
	public void onPrepared(MediaPlayer player) {
		// �첽׼����󣬲�������
		// ������ͣλ��
		player.seekTo(pausePosition);
		// ���Ÿ���
		player.start();
		// �����ͣλ��
		pausePosition = 0;
		// ������״̬��Ϊ��ʼ����
		isStartPlay = true;
		// ����Intent����
		Intent intent = new Intent();
		intent.setAction(INTENT_ACTION_SERVICE_PLAY);
		// ��currentIndex��duration��װ��Intent������
		intent.putExtra("currentIndex", currentIndex);
		intent.putExtra("duration", player.getDuration());
		// ���͹㲥������
		sendBroadcast(intent);
		// �����߳�
		startThread();
		// ����֪ͨ��֪ͨ
		sendNotification();
	}

	/**
	 * ����֪ͨ��֪ͨ
	 */
	private void sendNotification() {
		// �����Զ���֪ͨ������
		final RemoteViews views = new RemoteViews(getPackageName(),
				R.layout.notification_nowplaying_card);
		// �����Զ���֪ͨ�еĿؼ�������
		views.setTextViewText(R.id.notification_title, musics.get(currentIndex)
				.getName());
		views.setTextViewText(R.id.notification_artist, musics
				.get(currentIndex).getArtist());
		// ������ڲ��ţ�����ʾ��ͣ��ť�����򲥷Ű�ť��
		Bitmap bitmap = null;
		if (player.isPlaying()) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_pause_white);
		} else {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_play_arrow_white);
		}
		views.setImageViewBitmap(R.id.notification_play_pause, bitmap);
		// ����PendingIntent��ʱ��ͼ����,���֪ͨʱ��һ���µ�Ŀ��Activity
		// ��Activity
		Intent activityIntent = new Intent(this, MainActivity.class);
		final PendingIntent activityPi = PendingIntent.getActivity(this, 0,
				activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// ��ͣ/����
		Intent pauseOrPlayIntent = new Intent(INTENT_ACTION_ACT_PLAY_OR_PAUSE);
		PendingIntent pauseOrPlayPi = PendingIntent.getBroadcast(this, 1,
				pauseOrPlayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_play_pause,
				pauseOrPlayPi);
		// ��һ��
		Intent nextIntent = new Intent(INTENT_ACTION_ACT_NEXT);
		PendingIntent nextPi = PendingIntent.getBroadcast(this, 2, nextIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_next, nextPi);
		// ���֪ͨ��
		Intent clearIntent = new Intent(INTENT_ACTION_ACT_CLEAR_NOTIFICATION);
		PendingIntent clearPi = PendingIntent.getBroadcast(this, 3, clearIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.notification_clear, clearPi);
		// ����NotificationManager
		final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// ����Notification֪ͨ
		final Notification.Builder builder = new Notification.Builder(this);
		// ��ȡ��ǰ���Ÿ�����ͼƬ,��������ͼƬ����֪ͨ
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
						// ����Notification
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.ic_empty_music2);
						views.setImageViewBitmap(R.id.notification_album_art,
								bitmap);
						builder.setContentIntent(activityPi)
								// Ϊ֪ͨ������ʱ��ͼ
								.setContent(views)
								.setSmallIcon(R.drawable.ic_launcher);
						Notification notification = builder.build();
						notification.flags = Notification.FLAG_NO_CLEAR;
						// ����֪ͨ
						manager.notify(0, notification);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap bitmap) {
						views.setImageViewBitmap(R.id.notification_album_art,
								bitmap);
						// ����Notification
						builder.setContentIntent(activityPi)
								// Ϊ֪ͨ������ʱ��ͼ
								.setContent(views)
								.setSmallIcon(R.drawable.ic_launcher);
						Notification notification = builder.build();
						notification.flags = Notification.FLAG_NO_CLEAR;
						// ����֪ͨ
						manager.notify(0, notification);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
	}

	@Override
	public void onCreate() {
		// ����ImageLoader��������
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageOnFail(R.drawable.ic_empty_music2)
				.showImageForEmptyUri(R.drawable.ic_empty_music2).build();

		musics = new ArrayList<Music>();
		musics = ((MyMusicApplication) getApplication()).getMusic();
		// ��ʼ��ý�岥�Ź���
		player = new MediaPlayer();
		// �����㲥������
		receiver = new InnerBrodcastReceiver();
		// ������ͼ������,����Activity���͵Ĺ㲥
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
		// ע��㲥������
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
				filter);
		registerReceiver(receiver, filter);
		// Ϊ���Ź������ü�����
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// ��������꣬������һ��
				if (isStartPlay) {
					next();
				}
			}
		});
	}

	/**
	 * �㲥�����ߵ����࣬���ڽ���Activity���͵Ĺ㲥
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ���չ㲥����ȡActivity���͵�Action
			String action = intent.getAction();
			if (UPDATE_THE_DATA_OF_SONGS.equals(action)) {
				currentFragment = intent.getStringExtra("currentFragment");
				MusicData musicData = (MusicData) intent
						.getSerializableExtra("musicData");
				if (musicData != null) {
					// ��������Դ
					musics = musicData.getSongs();
				}
			} else if (INTENT_ACTION_ACT_PLAY_OR_PAUSE.equals(action)) {
				// ������ڲ��ţ�����ͣ�����򲥷ţ�
				if (player.isPlaying()) {
					pause();
				} else {
					play();
				}
			} else if (INTENT_ACTION_ACT_PREVIOUS.equals(action)) {
				// ������һ��
				previous();
			} else if (INTENT_ACTION_ACT_NEXT.equals(action)) {
				// ������һ��
				next();
			} else if (INTENT_ACTION_ACT_PLAY_NEW.equals(action)) {
				// �����µĸ���
				play(intent.getIntExtra("position", 0));
			} else if (INTENT_ACTION_ACT_SEEK.equals(action)) {
				// ����SeekBarֹͣ��λ��
				int percent = intent.getIntExtra("seekTo", 0);
				pausePosition = percent
						* musics.get(currentIndex).getDuration() / 100;
				play();
			} else if (INTENT_ACTION_ACT_CLEAR_NOTIFICATION.equals(action)) {
				//��ȡNotificationManager
				NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				//����֪ͨID���֪ͨ���������֪ͨ
				manager.cancelAll();
			}
		}
	}

	/**
	 * �ж��Ƿ����ڲ���
	 */
	public static boolean isRunning;
	/**
	 * ���½������߳�
	 */
	private UpdateProgressBarThread updateProgressBarThread;

	/**
	 * ��ʼ���½������߳�
	 */
	private void startThread() {
		if (!isRunning) {
			// �����߳�״̬
			isRunning = true;
			updateProgressBarThread = new UpdateProgressBarThread();
			// �����߳�
			updateProgressBarThread.start();
		}
	}

	/**
	 * ֹͣ���½������߳�
	 */
	private void stopThread() {
		// �����߳�״̬
		isRunning = false;
		// ֹͣ�߳�
		updateProgressBarThread = null;
	}

	/**
	 * ���½��������߳�
	 */
	public class UpdateProgressBarThread extends Thread {
		@Override
		public void run() {
			// ������service�в��������ý���������ֻ��ÿ��һ��ʱ���Activity���͹㲥֪ͨ���½�����
			Intent intent = new Intent();
			while (isRunning) {
				// ���͹㲥��Activity֪ͨ���½�����
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
		// ����Ϊ��ճ��
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		//��ȡNotificationManager
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//����֪ͨID���֪ͨ���������֪ͨ
		manager.cancelAll();
		// ȡ�����չ㲥
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		unregisterReceiver(receiver);
		// ֹͣ�߳�
		stopThread();
		// �ж��Ƿ����ڲ���
		if (player.isPlaying()) {
			player.pause();
		}
		// �ͷ���Դ
		player.release();
		// ֪ͨ������Դ
		player = null;
	}

	/**
	 * ���Ÿ���
	 */
	private void play() {
		try {
			// ����
			player.reset();
			// ׼������Դ
			player.setDataSource(musics.get(currentIndex).getSrc());
			// �����첽׼��������
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
	 * ����һ���µĸ���
	 */
	private void play(int position) {
		// �������л����������λ��
		currentIndex = position;
		// �����ͣλ��
		pausePosition = 0;
		// ����
		play();
	}

	/**
	 * ��ͣ����
	 */
	private void pause() {
		// �ж��Ƿ����ڲ���
		if (player.isPlaying()) {
			// ��¼��ͣλ��
			pausePosition = player.getCurrentPosition();
			// ��ͣ����
			player.pause();
			// ����Intent����
			Intent intent = new Intent();
			intent.setAction(INTENT_ACTION_SERVICE_PAUSE);
			// ���͹㲥����ͣ
			sendBroadcast(intent);
			// ֹͣ�߳�
			stopThread();
			// ����֪ͨ��֪ͨ
			sendNotification();
		}
	}

	/**
	 * ������һ��
	 */
	private void previous() {
		currentIndex--;
		// �ж��Ƿ�Ϊ��һ��
		if (currentIndex < 0) {
			currentIndex = musics.size() - 1;
		}
		// �����ͣλ��
		pausePosition = 0;
		// ����
		play();
	}

	/**
	 * ������һ��
	 */
	private void next() {
		currentIndex++;
		// �ж��Ƿ�Ϊ���һ��
		if (currentIndex >= musics.size()) {
			currentIndex = 0;
		}
		// �����ͣλ��
		pausePosition = 0;
		// ����
		play();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
