package com.hero.musicplayer.dal;

import java.util.List;

import com.hero.musicplayer.entity.Music;

/**
 * 接口IMusicPlayer,封装对Music的操作的方法
 * @author Android
 *
 */
public interface IMusicDao {
	List<Music> getData();
}
