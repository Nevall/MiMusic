package com.hero.musicplayer.dal;

import android.content.Context;


public class MusicDaoFactory {
	/**
	 * ˽�л����췽������������紴������
	 */
	private MusicDaoFactory(){
		
	}
	public static IMusicDao newInstance(Context context){
		return new MediaStoreDaoImpl(context);
	}
}
