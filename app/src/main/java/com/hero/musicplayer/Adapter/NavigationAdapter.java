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
		// �ж�convertView�Ƿ����ظ�ʹ�õģ�������ǣ���convertViewΪnull����Ҫ��ͷ���ز��ֵȣ�
		//����convertView�Ǳ��ظ�ʹ�õģ��������ٴθ���ģ����ض���
		if(convertView == null){
			convertView = getInflater().inflate(R.layout.nav_list_item,null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.ivTitle = (ImageView)convertView.findViewById(R.id.iv_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//��ȡר��ͼƬ·��������ΪBit����
		if( position == 0 ){
			holder.ivTitle.setImageResource(R.drawable.ic_locationmusic_black_24dp);
		}else if (position == 1) {
			holder.ivTitle.setImageResource(R.drawable.ic_onlinemusic_black_24dp);
		}
		holder.tvTitle.setText(data.get(position).toString());
		// ���������ݺ�ģ����װ�ɵ��б������
		return convertView;
	}

	/**
	 * �ؼ��ĳ�����
	 */
	private class ViewHolder{
		ImageView ivTitle;
		TextView tvTitle;
	}
}
