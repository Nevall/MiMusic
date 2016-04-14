package com.hero.musicplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;

/**
 * 通用工具类
 */
public class CommonUtils {

	/**
	 * 获取格式化后的时间
	 * @param timeMillis 毫秒值
	 * @return 格式化mm:ss后的时间
	 */
	public static String getFormatTime(long timeMillis){
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date = new Date();
		date.setTime(timeMillis);
		return sdf.format(date);
	}
	
    public static Uri getAlbumArtUri(long paramInt) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), paramInt);
    }
}
