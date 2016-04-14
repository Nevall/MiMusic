package com.hero.musicplayer.Adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hero.musicplayer.R;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.entity.Song;
import com.hero.musicplayer.util.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NavigationAdapter extends BaseAdapter<String>{

	public NavigationAdapter(Context context, List<String> data) {
		super(context, data);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// 判断convertView是否是重复使用的，如果不是，则convertView为null，需要从头加载布局等，
		//否则，convertView是被重复使用的，则无须再次根据模板加载对象
		if(convertView == null){
			convertView = getInflater().inflate(R.layout.nav_list_item,null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.ivTitle = (ImageView)convertView.findViewById(R.id.iv_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//读取专辑图片路径的数据为Bit数据
		if( position == 0 ){
			holder.ivTitle.setImageResource(R.drawable.ic_locationmusic_black_24dp);
		}else if (position == 1) {
			holder.ivTitle.setImageResource(R.drawable.ic_onlinemusic_black_24dp);
		}
		holder.tvTitle.setText(data.get(position).toString());
		// 返回由数据和模板组装成的列表项对象
		return convertView;
	}

	/**
	 * 控件的持有者
	 */
	private class ViewHolder{
		ImageView ivTitle;
		TextView tvTitle;
	}
}
