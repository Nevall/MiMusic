package com.hero.musicplayer.entity;

import java.io.Serializable;

/**
 * Music类封装歌曲信息(本地、网络)
 * @author Android
 *
 */
public class Music implements Serializable{
	private transient String widget_id;//Gson不解析该字段
	private transient  String artist_id;//Gson不解析该字段
	private transient int rank;//Gson不解析该字段
	private transient String id;//Gson不解析该字段

	private String picture;
	/**
	 * 歌曲标题
	 */
	private String name;
	/**
	 * 歌曲路径
	 */
	private String src;
	/**
	 * 文件名
	 */
	//	private String displayName;
	/**
	 * 歌曲总时长
	 */
	private int duration;
	/**
	 * 歌曲总时长
	 */
	private String length;
	/**
	 * 专辑作者
	 */
	//	private String albumArtist;

	/**
	 * 专辑id
	 */
	private int albumId;
	/**
	 * 作者
	 */
	private String artist;
	/**
	 * 专辑名
	 */
	private String album;

	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAlbumId() {
		return albumId;
	}
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	@Override
	public String toString() {
		return "Music [picture=" + picture + ", name=" + name + ", src=" + src
				+ ", duration=" + duration + ", length=" + length
				+ ", albumId=" + albumId + ", artist=" + artist + ", album="
				+ album + "]";
	}

}
