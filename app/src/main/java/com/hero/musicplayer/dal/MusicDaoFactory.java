package com.hero.musicplayer.dal;

import android.content.Context;


public class MusicDaoFactory {
	/**
	 * 私有化构造方法，不允许外界创建对象
	 */
	private MusicDaoFactory(){
		
	}
	public static IMusicDao newInstance(Context context){
		return new MediaStoreDaoImpl(context);
	}
}
