package com.hero.musicplayer.entity;

import java.io.Serializable;
import java.util.List;
public class MusicData implements Serializable{
	private List<Music> songs ;

	public void setSongs(List<Music> songs){
		this.songs = songs;
	}
	public List<Music> getSongs(){
		return this.songs;
	}
	
	@Override
	public String toString() {
		return "OnlineMusic [songs=" + songs + "]";
	}
}






