package com.hero.musicplayer.util;

import com.google.gson.Gson;
import com.hero.musicplayer.entity.MusicData;

/**
 * ����JSON�ַ���������
 */
public class ParseJson {
	private Gson gson = new Gson();

	public MusicData parseMusicJson(String json) {
		return gson.fromJson(json, MusicData.class);
	}
}
