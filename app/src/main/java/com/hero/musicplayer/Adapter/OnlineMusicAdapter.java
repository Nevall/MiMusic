package com.hero.musicplayer.Adapter;

import java.util.List;

import com.hero.musicplayer.R;
import com.hero.musicplayer.entity.Music;
import com.hero.musicplayer.view.MusicVisualizer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OnlineMusicAdapter extends BaseAdapter<Music>{

	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	boolean isSingle = true;
	int old = -1;
	SparseBooleanArray selected;
	public boolean isRunning;
	
	public OnlineMusicAdapter(Context context, List<Music> data) {
		super(context, data);
		selected = new SparseBooleanArray();
		options = new DisplayImageOptions.Builder()  
				.cacheInMemory(true)  
				.cacheOnDisc(true)  
				.showImageForEmptyUri(R.drawable.ic_empty_music2)
				.build();
	}

	/**
	 * ����ԭ����
	 */
	public void addList(List<Music> items) {
		this.data.addAll(items);
		notifyDataSetChanged();
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// �ж�convertView�Ƿ����ظ�ʹ�õģ�������ǣ���convertViewΪnull����Ҫ��ͷ���ز��ֵȣ�
		//����convertView�Ǳ��ظ�ʹ�õģ��������ٴθ���ģ����ض���
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
			imageLoader.displayImage(data.get(position).getPicture(), holder.imAblumArt,options);
			//��ȡר��ͼƬ·��������ΪBit����
			//			if(data.get(position).getAblumArt()!=null){
			//				Bitmap bm = BitmapFactory.decodeFile(data.get(position).getAblumArt());
			//				holder.imAblumArt.setImageBitmap(bm);//������ʾͼƬ
			//			}else{
			//				holder.imAblumArt.setImageResource(R.drawable.ic_launcher);
			//			}
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
			holder.tvDuration.setText(data.get(position).getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���������ݺ�ģ����װ�ɵ��б������
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
	 * �ؼ��ĳ�����
	 */
	private class ViewHolder{
		ImageView imAblumArt;
		TextView tvTitle;
		TextView tvArtist;
		TextView tvDuration;
		MusicVisualizer visualizer;
	}
}
