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
	 * ����֪ͨ��֪ͨ
	 */
	private void sendNotification() {
		//�����Զ���֪ͨ����
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_nowplaying_card);
		//�����Զ���֪ͨ�еĿؼ�������
		//���ĵ�ǰ���ŵø�����Ϣ
		Uri uri = Uri.parse("https://img3.doubanio.com/view/site/median/public/494be3a2a3d7095.jpg");;

		views.setImageViewUri(R.id.notification_album_art, uri);
		//����PendingIntent��ʱ��ͼ����,���֪ͨʱ��һ���µ�Ŀ��Activity
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pi = PendingIntent.
				getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//���Զ���֪ͨ������һ�װ�ť�ؼ����ü�����
		views.setOnClickPendingIntent(R.id.notification_play_pause, pi);
		//����NotificationManager
		NotificationManager manager = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		//����Notification֪ͨ
		Notification.Builder builder = new Notification.Builder(this);
		//����Notification
		builder.setContent(views)
		.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		//����֪ͨ
		manager.notify(0, notification);
	}
	
	/**
	 * �㲥�����ߵ����࣬���ڽ��չ㲥
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//���չ㲥����ȡActivity���͵�Action
			String action = intent.getAction();
			Log.d("HH", "intent action = "+action);
		}
	}

}
