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
	 * ����ϵͳContentProvider�ṩ�����ݿ��е�external.db���ȡ��������
	 */
	@Override
	public List<Music> getData() {
		//����������������
		List<Music> musics = new ArrayList<Music>();
		//��ȡ�����ṩ���ṩ�����ݿ��е����ݵĹ���
		ContentResolver cr = context.getContentResolver();
		//Media��Uri·��
		Uri uri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		//��Ҫ��ȡ�����ݵ��ֶ��б�
		String[] projection = {
				"_data",//����·��
				"_display_name",//������(�к�׺)
				"title",//������
				"duration",//����ʱ��
				"album_artist",//ר������
				"album_id",//ר��id
				"artist",//����
				"album",//ר����
				"album_key",//ר������ؼ���
				"is_music"//�Ƿ�������
		};
		String selection = "is_music = ?";//ɸѡ����,�Ƿ��������ļ�
		String[] selectionArgs = { ""+1 };//ɸѡ����,�������ļ�
		String sortOrder = "_display_name desc";//����asc(˳��)��desc(����)
		//��ȡ�����ݿ��в��ҷ��ص��α�
		Cursor c = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		//�����α�
		for (c.moveToFirst();!c.isLast();c.moveToNext()) {
			//����Music����
			Music music = new Music();
			//��ȡ����
			music.setSrc(c.getString(c.getColumnIndex("_data")));//����·��
			music.setName(c.getString(c.getColumnIndex("title")));//������
			music.setDuration(c.getInt(c.getColumnIndex("duration")));//����ʱ��
			music.setArtist(c.getString(c.getColumnIndex("artist")));//����
			music.setAlbum(c.getString(c.getColumnIndex("album")));//ר����
//			music.setAblumKey(c.getString(c.getColumnIndex("album_key")));//ר������ؼ���
			music.setAlbumId(c.getInt(c.getColumnIndex("album_id")));//ר������id
//			music.setAblumArt(getAlbumArtByAlbumKey(
//					c.getString(c.getColumnIndex("album_key"))));//ר������ͼƬ·��
			//�����ݷ�װ��List������
			musics.add(music);
		}
		return musics;
	}

	/**
	 * ����album_key��album_art��ֵ
	 * @param albumKey
	 *            album_key
	 * @return album_art����ͼƬ��·��������޷���ȡ������ͼƬ���򷵻�null
	 */
	public String getAlbumArtByAlbumKey(String albumKey){
		// �������Ϊnull��ֱ�ӷ���null
		if (albumKey == null) {
			return null;
		}
		//��ȡ���ݿ�Ĺ���
		ContentResolver cr = context.getContentResolver();
		//׼������
		Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI; 
		String[] projection = { "album_art "};
		String selection = "album_key = ?";
		String[] selectionArgs = { albumKey };
		String sortOrder = null;
		//�������ݿ��е�����,
		Cursor c = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		//�жϲ���ȡ���
		if(c != null && c.moveToFirst()){
			return c.getString(c.getColumnIndex("album_art"));
		}
		//����null
		return null;

	}

}
