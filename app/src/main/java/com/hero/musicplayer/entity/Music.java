package com.hero.musicplayer.entity;

import java.io.Serializable;

/**
 * Music���װ������Ϣ(���ء�����)
 * @author Android
 *
 */
public class Music implements Serializable{
	private transient String widget_id;//Gson���������ֶ�
	private transient  String artist_id;//Gson���������ֶ�
	private transient int rank;//Gson���������ֶ�
	private transient String id;//Gson���������ֶ�

	private String picture;
	/**
	 * ��������
	 */
	private String name;
	/**
	 * ����·��
	 */
	private String src;
	/**
	 * �ļ���
	 */
	//	private String displayName;
	/**
	 * ������ʱ��
	 */
	private int duration;
	/**
	 * ������ʱ��
	 */
	private String length;
	/**
	 * ר������
	 */
	//	private String albumArtist;

	/**
	 * ר��id
	 */
	private int albumId;
	/**
	 * ����
	 */
	private String artist;
	/**
	 * ר����
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
