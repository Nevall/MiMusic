package com.hero.musicplayer.dal;

import java.util.List;

import com.hero.musicplayer.entity.Music;

/**
 * �ӿ�IMusicPlayer,��װ��Music�Ĳ����ķ���
 * @author Android
 *
 */
public interface IMusicDao {
	List<Music> getData();
}
