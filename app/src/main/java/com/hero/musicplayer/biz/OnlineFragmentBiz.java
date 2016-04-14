package com.hero.musicplayer.biz;


import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hero.musicplayer.entity.MusicData;
import com.hero.musicplayer.util.Consts;
import com.hero.musicplayer.util.HttpUtils;
import com.hero.musicplayer.util.ParseJson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * MainFragment��ҵ����
 */
public class OnlineFragmentBiz implements Consts{
	private Context context;
	private MusicData onlineMusic;
	private ParseJson parse = new ParseJson();

	public OnlineFragmentBiz(Context context) {
		this.context = context;
	}

	/**
	 * ����Http�����������������Ϣ
	 * @return 
	 */
	public void loadOnlineMusic(final Handler handler) {
		//TODO ���ع���MyApplication��������Ƿ�����
		if (HttpUtils.isNetworkConnected(context)) {
			String url = ONLINE_MUSIC_BASE_Url;
			try { 
				OkHttpUtils.get().url(url).build().execute(new StringCallback() {
					
					@Override
					public void onResponse(String response) {
						try {
							response = response.substring(response.indexOf("{"), response.lastIndexOf("}")+1);
							//����JSON����
							onlineMusic = parse.parseMusicJson(response);
							Log.d("HH", onlineMusic.getSongs().toString());
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							//����Ϣ���͸����߳�
							Message msg = Message.obtain();
							msg.what = LOAD_ONLINE_MUSIC_DATA_SUCCESS;
							Bundle data = new Bundle();
							data.putSerializable("onlineMusic", onlineMusic);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}

					@Override
					public void onError(Call arg0, Exception arg1) {
						//����Ϣ���͸����߳�
						handler.sendEmptyMessage(LOAD_ONLINE_MUSIC_DATA_FAILUE);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//����Ϣ���͸����߳�
			handler.sendEmptyMessage(LOAD_ONLINE_MUSIC_DATA_FAILUE);
		}
	}
	
}
