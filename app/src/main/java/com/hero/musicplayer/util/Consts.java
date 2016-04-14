package com.hero.musicplayer.util;

/**
 * �ṩ�����Ľӿڣ���Ҫʹ����Щ�������඼Ӧ��ʵ�ָýӿڣ��ýӿ���û�г��󷽷�
 * @author Android
 *
 */
public interface Consts {

	//---------��������Activity�����Ĺ㲥---------
	/**
	 * ����/��ͣ
	 */
	String INTENT_ACTION_ACT_PLAY_OR_PAUSE = "COM.HERO.INTENT.ACTION.ACTIVITY_PLAY_OR_PAUSE";
	/**
	 * ������һ��
	 */
	String INTENT_ACTION_ACT_PREVIOUS = "COM.HERO.INTENT.ACTION.ACTIVITY_PREVIOUS";
	/**
	 * ������һ��
	 */
	String INTENT_ACTION_ACT_NEXT = "COM.HERO.INTENT.ACTION.ACTIVITY_NEXT";
	/**
	 * �����µĸ���
	 */
	String INTENT_ACTION_ACT_PLAY_NEW = "COM.HERO.INTENT.ACTION.ACTIVITY_PLAY_NEW";
	/**
	 * ���
	 */
	String INTENT_ACTION_ACT_SEEK = "COM.HERO.INTENT.ACTION.ACTIVITY_SEEK";
	
	//---------��������Activity�����Ĺ㲥---------
	/**
	 * ����
	 */
	String INTENT_ACTION_SERVICE_PLAY = "com.hero.intent.action.SERVICE_PLAY";
	/**
	 * ��ͣ
	 */
	String INTENT_ACTION_SERVICE_PAUSE = "com.hero.intent.action.SERVICE_PAUSE";
	/**
	 * ���½�����
	 */
	String INTENT_ACTION_SERVICE_UPDATE_PROGRESS = "com.hero.intent.action.SERVICE_UPDATE_PROGRESS";
	
	/**
	 * ��������url
	 */
	public static final String ONLINE_MUSIC_BASE_Url = "http://music.douban.com/api/artist/chart?type=song&cb=%24.setp(0.09175920370034873)&app_name=music_artist&version=52&_=1410349381611";
	
	/**
	 * OnlineFragmentBiz���͵���Ϣ
	 */
	public static final int LOAD_ONLINE_MUSIC_DATA_SUCCESS = 100;
	public static final int LOAD_ONLINE_MUSIC_DATA_FAILUE = 101;
	
	/**
	 * ��ǰFragmentΪLocationFragment 
	 */
	public static final String CURRENT_FRAGMRNT_IS_ONLINE_FRAGMRNT = "CURRENT_FRAGMRNT_IS_ONLINE_FRAGMRNT";
	/**
	 * ��ǰFragmentΪLocationFragment 
	 */
	public static final String CURRENT_FRAGMRNT_IS_LOCATION_FRAGMRNT = "CURRENT_FRAGMRNT_IS_LOCATION_FRAGMRNT";
	
	/**
	 * ���º�̨�����List<Song> ����Դ 
	 */
	public static final String UPDATE_THE_DATA_OF_SONGS = "UPDATE_THE_DATA_OF_SONGS";
	/**
	 * ���֪ͨ��
	 */
	String INTENT_ACTION_ACT_CLEAR_NOTIFICATION = "COM.HERO.INTENT.ACTION.ACTIVITY_CLEAR_NOTIFICATION";
}
