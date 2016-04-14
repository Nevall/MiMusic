package com.hero.musicplayer.Adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hero.musicplayer.R;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.service.MusicService;
import com.hero.musicplayer.util.CommonUtils;
import com.hero.musicplayer.view.MusicVisualizer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.request.GetRequest;

public class MusicAdapter extends BaseAdapter<Music>{

	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private boolean isSingle = true;
	private int old = -1;
	private SparseBooleanArray selected;
	public boolean isRunning;

	public MusicAdapter(Context context, List<Music> data) {
		super(context, data);
		selected = new SparseBooleanArray();
		options = new DisplayImageOptions.Builder()  
		.cacheInMemory(true)  
		.cacheOnDisc(true)  
		.showImageOnFail(R.drawable.ic_empty_music2)
		.build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// 判断convertView是否是重复使用的，如果不是，则convertView为null，需要从头加载布局等，
		//否则，convertView是被重复使用的，则无须再次根据模板加载对象
		if(convertView == null){
			convertView = getInflater().inflate(R.layout.music_player_list_item,null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_music_name);
			holder.tvArtist = (TextView) convertView.findViewById(R.id.tv_item_music_artist);
			holder.tvDuration = (TextView) convertView.findViewById(R.id.tv_item_music_duration);
			holder.imAblumArt = (ImageView)convertView.findViewById(R.id.iv_album_image);
			holder.visualizer = (MusicVisualizer) convertView.findViewById(R.id.visualizer);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			imageLoader.displayImage(CommonUtils.getAlbumArtUri(data.get(position).getAlbumId()).toString(), holder.imAblumArt,options);
			//读取专辑图片路径的数据为Bit数据
			//			if(data.get(position).getAblumArt()!=null){
			//				Bitmap bm = BitmapFactory.decodeFile(data.get(position).getAblumArt());
			//				holder.imAblumArt.setImageBitmap(bm);//设置显示图片
			//			}else{
			//				holder.imAblumArt.setImageResource(R.drawable.ic_launcher);
			//			}
			//改变正在播放的字体颜色
			if(selected.get(position)){
				if (isRunning) {
					holder.visualizer.setColor(context.getResources().getColor(R.color.colorAccent));
					holder.visualizer.startAnimateView();
				}else{
					holder.visualizer.setColor(context.getResources().getColor(R.color.colorAccent));
					holder.visualizer.stopAnimateView();
				}
				holder.visualizer.setVisibility(View.VISIBLE);
				holder.tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
				holder.tvArtist.setTextColor(context.getResources().getColor(R.color.colorAccent));
			}else{
				holder.tvTitle.setTextColor(context.getResources().getColor(R.color.music_item_name_defaule_color));
				holder.tvArtist.setTextColor(context.getResources().getColor(R.color.music_item_artist_defaule_color));
				holder.visualizer.setVisibility(View.GONE);
			}
			holder.tvTitle.setText(data.get(position).getName());
			holder.tvArtist.setText(data.get(position).getArtist());
			holder.tvDuration.setText(CommonUtils.getFormatTime(data.get(position).getDuration()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回由数据和模板组装成的列表项对象
		return convertView;
	}

	public void setSelectedItem(int selected){
		if(isSingle = true && old != -1){
			this.selected.put(old, false);
		}
		this.selected.put(selected, true);
		old = selected;
	}

	/**
	 * 控件的持有者
	 */
	private class ViewHolder{
		ImageView imAblumArt;
		TextView tvTitle;
		TextView tvArtist;
		TextView tvDuration;
		MusicVisualizer visualizer;
	}
}
