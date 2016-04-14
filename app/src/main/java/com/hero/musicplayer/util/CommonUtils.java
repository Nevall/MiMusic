package com.hero.musicplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;

/**
 * ͨ�ù�����
 */
public class CommonUtils {

	/**
	 * ��ȡ��ʽ�����ʱ��
	 * @param timeMillis ����ֵ
	 * @return ��ʽ��mm:ss���ʱ��
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
