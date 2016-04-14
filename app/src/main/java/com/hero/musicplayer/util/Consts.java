package com.hero.musicplayer.util;

/**
 * 提供常量的接口，需要使用这些常量的类都应该实现该接口，该接口中没有抽象方法
 * @author Android
 *
 */
public interface Consts {

	//---------以下是由Activity发出的广播---------
	/**
	 * 播放/暂停
	 */
	String INTENT_ACTION_ACT_PLAY_OR_PAUSE = "COM.HERO.INTENT.ACTION.ACTIVITY_PLAY_OR_PAUSE";
	/**
	 * 播放上一首
	 */
	String INTENT_ACTION_ACT_PREVIOUS = "COM.HERO.INTENT.ACTION.ACTIVITY_PREVIOUS";
	/**
	 * 播放下一首
	 */
	String INTENT_ACTION_ACT_NEXT = "COM.HERO.INTENT.ACTION.ACTIVITY_NEXT";
	/**
	 * 播放新的歌曲
	 */
	String INTENT_ACTION_ACT_PLAY_NEW = "COM.HERO.INTENT.ACTION.ACTIVITY_PLAY_NEW";
	/**
	 * 快进
	 */
	String INTENT_ACTION_ACT_SEEK = "COM.HERO.INTENT.ACTION.ACTIVITY_SEEK";
	
	//---------以下是由Activity发出的广播---------
	/**
	 * 播放
	 */
	String INTENT_ACTION_SERVICE_PLAY = "com.hero.intent.action.SERVICE_PLAY";
	/**
	 * 暂停
	 */
	String INTENT_ACTION_SERVICE_PAUSE = "com.hero.intent.action.SERVICE_PAUSE";
	/**
	 * 更新进度条
	 */
	String INTENT_ACTION_SERVICE_UPDATE_PROGRESS = "com.hero.intent.action.SERVICE_UPDATE_PROGRESS";
	
	/**
	 * 在线音乐url
	 */
	public static final String ONLINE_MUSIC_BASE_Url = "http://music.douban.com/api/artist/chart?type=song&cb=%24.setp(0.09175920370034873)&app_name=music_artist&version=52&_=1410349381611";
	
	/**
	 * OnlineFragmentBiz发送的消息
	 */
	public static final int LOAD_ONLINE_MUSIC_DATA_SUCCESS = 100;
	public static final int LOAD_ONLINE_MUSIC_DATA_FAILUE = 101;
	
	/**
	 * 当前Fragment为LocationFragment 
	 */
	public static final String CURRENT_FRAGMRNT_IS_ONLINE_FRAGMRNT = "CURRENT_FRAGMRNT_IS_ONLINE_FRAGMRNT";
	/**
	 * 当前Fragment为LocationFragment 
	 */
	public static final String CURRENT_FRAGMRNT_IS_LOCATION_FRAGMRNT = "CURRENT_FRAGMRNT_IS_LOCATION_FRAGMRNT";
	
	/**
	 * 更新后台服务的List<Song> 数据源 
	 */
	public static final String UPDATE_THE_DATA_OF_SONGS = "UPDATE_THE_DATA_OF_SONGS";
	/**
	 * 清除通知栏
	 */
	String INTENT_ACTION_ACT_CLEAR_NOTIFICATION = "COM.HERO.INTENT.ACTION.ACTIVITY_CLEAR_NOTIFICATION";
}
