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
 * MainFragment的业务类
 */
public class OnlineFragmentBiz implements Consts{
	private Context context;
	private MusicData onlineMusic;
	private ParseJson parse = new ParseJson();

	public OnlineFragmentBiz(Context context) {
		this.context = context;
	}

	/**
	 * 发送Http请求加载最新新闻消息
	 * @return 
	 */
	public void loadOnlineMusic(final Handler handler) {
		//TODO 可重构在MyApplication检查网络是否连接
		if (HttpUtils.isNetworkConnected(context)) {
			String url = ONLINE_MUSIC_BASE_Url;
			try { 
				OkHttpUtils.get().url(url).build().execute(new StringCallback() {
					
					@Override
					public void onResponse(String response) {
						try {
							response = response.substring(response.indexOf("{"), response.lastIndexOf("}")+1);
							//解析JSON数据
							onlineMusic = parse.parseMusicJson(response);
							Log.d("HH", onlineMusic.getSongs().toString());
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							//将信息发送给主线程
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
						//将信息发送给主线程
						handler.sendEmptyMessage(LOAD_ONLINE_MUSIC_DATA_FAILUE);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			//将信息发送给主线程
			handler.sendEmptyMessage(LOAD_ONLINE_MUSIC_DATA_FAILUE);
		}
	}
	
}
