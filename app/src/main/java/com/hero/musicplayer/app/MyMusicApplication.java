package com.hero.musicplayer.app;

import java.util.List;

import android.app.Application;
import android.graphics.Bitmap.CompressFormat;

import com.hero.musicplayer.dal.MusicDaoFactory;
import com.hero.musicplayer.entity.Music;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MyMusicApplication extends Application{
	/**
	 * 数据源List集合
	 */
	private List<Music> musics;

	@Override
	public void onCreate() {
		musics = MusicDaoFactory.newInstance(this).getData();
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)  
		.memoryCacheExtraOptions(480, 800) // default = device screen dimensions  
		.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)  
		.threadPoolSize(2) // default  
		.threadPriority(Thread.NORM_PRIORITY - 2) // default  
		.tasksProcessingOrder(QueueProcessingType.FIFO) // default  
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new WeakMemoryCache())  
		.memoryCacheSize(2 * 1024 * 1024)  
		.memoryCacheSizePercentage(13) // default  
		.discCache(new UnlimitedDiscCache(getCacheDir())) // default  
		.discCacheSize(50 * 1024 * 1024)  
		.discCacheFileCount(100)  
		.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
		.imageDownloader(new BaseImageDownloader(this)) // default  
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default  
		.writeDebugLogs()  
		.build(); 
		ImageLoader.getInstance().init(configuration);
	}

	/**
	 * 获取List<Music>数据源集合
	 * @return List<Music>数据源集合
	 */
	public List<Music> getMusic(){
		return musics;
	}
}
