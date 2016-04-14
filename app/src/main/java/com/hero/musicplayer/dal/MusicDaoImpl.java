package com.hero.musicplayer.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.hero.musicplayer.entity.Music;

public class MusicDaoImpl implements IMusicDao{

	/**
	 * 该实现方式是从SD卡中直接获取以MP3为末尾的音频文件方式获取数据，
	 * 由于没有音频文件的其他信息，故不可取
	 */
	@Override
	public List<Music> getData() {
		//创建返回实例
		List musics = new ArrayList<Music>();
		//创建Music实例
		Music music;
		/**
		 * 1、判断是否插入sdcar
		 * 2、获取sdcar卡中的Music目录
		 * 3、遍历Music目录下的文件
		 * 4、判断是否是文件
		 * 5、判断是否是MP3文件
		 * 6、获取歌曲信息
		 * 7、封装到List集合中 
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
