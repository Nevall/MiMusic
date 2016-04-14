package com.hero.musicplayer.entity;

import java.io.Serializable;

public class Song implements Serializable{
	
	private transient int count;
	
	private String picture;

	private String name;

	private String artist;

	private transient int rank;

	private transient String id;

	private String length;

	private transient  String artist_id;

	private  String src;

	private transient String widget_id;

//	public void setCount(int count){
//		this.count = count;
//	}
//	public int getCount(){
//		return this.count;
//	}
	public void setPicture(String picture){
		this.picture = picture;
	}
	public String getPicture(){
		return this.picture;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setArtist(String artist){
		this.artist = artist;
	}
	public String getArtist(){
		return this.artist;
	}
	public void setRank(int rank){
		this.rank = rank;
	}
	public int getRank(){
		return this.rank;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
	public void setLength(String length){
		this.length = length;
	}
	public String getLength(){
		return this.length;
	}
	public void setArtist_id(String artist_id){
		this.artist_id = artist_id;
	}
	public String getArtist_id(){
		return this.artist_id;
	}
	public void setSrc(String src){
		this.src = src;
	}
	public String getSrc(){
		return this.src;
	}
	public void setWidget_id(String widget_id){
		this.widget_id = widget_id;
	}
	public String getWidget_id(){
		return this.widget_id;
	}
	@Override
	public String toString() {
		return "Song [count=" + count + ", picture=" + picture + ", name="
				+ name + ", artist=" + artist + ", rank=" + rank + ", id="
				+ id + ", length=" + length + ", artist_id=" + artist_id
				+ ", src=" + src + ", widget_id=" + widget_id + "]";
	}
}
