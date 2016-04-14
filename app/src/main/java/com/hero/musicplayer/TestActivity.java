package com.hero.musicplayer;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.ui.MainActivity;
import com.hero.musicplayer.util.CommonUtils;


public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendNotification();
			}
		});
	}

	/**
	 * 发送通知栏通知
	 */
	private void sendNotification() {
		//加载自定义通知布局
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_nowplaying_card);
		//设置自定义通知中的控件的内容
		//更改当前播放得歌曲信息
		Uri uri = Uri.parse("https://img3.doubanio.com/view/site/median/public/494be3a2a3d7095.jpg");;

		views.setImageViewUri(R.id.notification_album_art, uri);
		//设置PendingIntent延时意图对象,点击通知时打开一个新的目标Activity
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pi = PendingIntent.
				getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//对自定义通知动的上一首按钮控件设置监听器
		views.setOnClickPendingIntent(R.id.notification_play_pause, pi);
		//创建NotificationManager
		NotificationManager manager = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		//创建Notification通知
		Notification.Builder builder = new Notification.Builder(this);
		//设置Notification
		builder.setContent(views)
		.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		//发送通知
		manager.notify(0, notification);
	}
	
	/**
	 * 广播接收者的子类，用于接收广播
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//接收广播，获取Activity发送的Action
			String action = intent.getAction();
			Log.d("HH", "intent action = "+action);
		}
	}

}
