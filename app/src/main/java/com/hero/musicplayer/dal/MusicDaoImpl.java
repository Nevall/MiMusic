package com.hero.musicplayer.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.hero.musicplayer.entity.Music;

public class MusicDaoImpl implements IMusicDao{

	/**
	 * ��ʵ�ַ�ʽ�Ǵ�SD����ֱ�ӻ�ȡ��MP3Ϊĩβ����Ƶ�ļ���ʽ��ȡ���ݣ�
	 * ����û����Ƶ�ļ���������Ϣ���ʲ���ȡ
	 */
	@Override
	public List<Music> getData() {
		//��������ʵ��
		List musics = new ArrayList<Music>();
		//����Musicʵ��
		Music music;
		/**
		 * 1���ж��Ƿ����sdcar
		 * 2����ȡsdcar���е�MusicĿ¼
		 * 3������MusicĿ¼�µ��ļ�
		 * 4���ж��Ƿ����ļ�
		 * 5���ж��Ƿ���MP3�ļ�
		 * 6����ȡ������Ϣ
		 * 7����װ��List������ 
		 */

		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			File [] files = dir.listFiles();
			if(files.length>0){
				for (File file : files) {
					if(file.isFile()){
						if(file.getName().toLowerCase().endsWith(".mp3")){
							music = new Music();
							music.setName(file.getName().substring(0, file.getName().length()-4));
							music.setSrc(file.getAbsolutePath());
							musics.add(music);
						}
					}
				}
			}
		}
		return musics;
	}

}
