package com.hero.musicplayer.dal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hero.musicplayer.entity.Music;

public class MediaStoreDaoImpl implements IMusicDao{
	Context context;

	public MediaStoreDaoImpl(Context context) {
		this.context = context;
	}

	/**
	 * 利用系统ContentProvider提供的数据库中的external.db表获取音乐数据
	 */
	@Override
	public List<Music> getData() {
		//声明返回数据类型
		List<Music> musics = new ArrayList<Music>();
		//读取内容提供者提供的数据库中的数据的工具
		ContentResolver cr = context.getContentResolver();
		//Media的Uri路径
		Uri uri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		//需要读取的数据的字段列表
		String[] projection = {
				"_data",//歌曲路径
				"_display_name",//歌曲名(有后缀)
				"title",//歌曲名
				"duration",//歌曲时长
				"album_artist",//专辑作者
				"album_id",//专辑id
				"artist",//作者
				"album",//专辑名
				"album_key",//专辑封面关键字
				"is_music"//是否是音乐
		};
		String selection = "is_music = ?";//筛选条件,是否是音乐文件
		String[] selectionArgs = { ""+1 };//筛选条件,是音乐文件
		String sortOrder = "_display_name desc";//排序asc(顺序)、desc(倒序)
		//获取从数据库中查找返回的游标
		Cursor c = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		//遍历游标
		for (c.moveToFirst();!c.isLast();c.moveToNext()) {
			//创建Music对象
			Music music = new Music();
			//获取数据
			music.setSrc(c.getString(c.getColumnIndex("_data")));//歌曲路径
			music.setName(c.getString(c.getColumnIndex("title")));//歌曲名
			music.setDuration(c.getInt(c.getColumnIndex("duration")));//歌曲时长
			music.setArtist(c.getString(c.getColumnIndex("artist")));//作者
			music.setAlbum(c.getString(c.getColumnIndex("album")));//专辑名
//			music.setAblumKey(c.getString(c.getColumnIndex("album_key")));//专辑封面关键字
			music.setAlbumId(c.getInt(c.getColumnIndex("album_id")));//专辑封面id
//			music.setAblumArt(getAlbumArtByAlbumKey(
//					c.getString(c.getColumnIndex("album_key"))));//专辑封面图片路径
			//将数据封装到List集合中
			musics.add(music);
		}
		return musics;
	}

	/**
	 * 根据album_key找album_art的值
	 * @param albumKey
	 *            album_key
	 * @return album_art，即图片的路径，如果无法读取到内置图片，则返回null
	 */
	public String getAlbumArtByAlbumKey(String albumKey){
		// 如果参数为null则直接返回null
		if (albumKey == null) {
			return null;
		}
		//读取数据库的工具
		ContentResolver cr = context.getContentResolver();
		//准备参数
		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI; 
		String[] projection = { "album_art "};
		String selection = "album_key = ?";
		String[] selectionArgs = { albumKey };
		String sortOrder = null;
		//查找数据库中的数据,
		Cursor c = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		//判断并读取结果
		if(c != null && c.moveToFirst()){
			return c.getString(c.getColumnIndex("album_art"));
		}
		//返回null
		return null;

	}

}
