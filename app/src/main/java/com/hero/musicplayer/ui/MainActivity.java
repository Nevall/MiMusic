package com.hero.musicplayer.ui;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hero.musicplayer.R;
import com.hero.musicplayer.Adapter.NavigationAdapter;
import com.hero.musicplayer.fragment.LocationFragment;
import com.hero.musicplayer.fragment.OnLineFragment;
import com.hero.musicplayer.fragment.QuickControlsFragment;
import com.hero.musicplayer.service.MusicService;
import com.hero.musicplayer.util.Consts;
import com.hero.musicplayer.view.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity implements Consts{
	/**
	 * 广播接收者
	 */
	private BroadcastReceiver receiver;
	private AlertDialog exitDialog;
	private DrawerLayout mDrawerLayout;
	private SlidingUpPanelLayout panelLayout;
	private Toolbar toolbar;
	private ListView lvNavigation;
	private int currentFragmentId = LOCATIONFRAGMENT_ID;
	private static final int LOCATIONFRAGMENT_ID = 0;
	private static final int ONLINEFRAGMENT_ID = 1;
	private String [] str = {"本地音乐","在线音乐"};
	private List<String> nav = Arrays.asList(str);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new initQuickControls().execute("");
		//初始化控件
		initViews();
		//启动后台服务及注册广播接收器
		startMyService();
		//防止Fragment重叠，讲上次保存的fragment弹出
		if(savedInstanceState!=null){
			FragmentManager manager = getSupportFragmentManager(); 
			manager.popBackStackImmediate(null, 1); 
		}
		new Runnable() {
			public void run() {
				Fragment fragment = new LocationFragment();
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
			}
		}.run();
	}


	private void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		//实现Toolbar的菜单按钮的箭头返回功能
		//		container = (FrameLayout)findViewById(R.id.fragment_container);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				toolbar, R.string.app_name, R.string.app_name);
		mDrawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
		lvNavigation = (ListView)findViewById(R.id.lv_nav);
		BaseAdapter	adapter = new NavigationAdapter(this, nav);
		lvNavigation.setAdapter(adapter);
		lvNavigation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				try {
					if (position == LOCATIONFRAGMENT_ID) {
						currentFragmentId = LOCATIONFRAGMENT_ID;
					}else if (position == ONLINEFRAGMENT_ID) {
						currentFragmentId = ONLINEFRAGMENT_ID;
					}
					new Runnable() {

						@Override
						public void run() {
							//切换Fragment对象
							Fragment fragment = null;
							if (position == LOCATIONFRAGMENT_ID) {
								fragment = new LocationFragment();
							}else if (position == ONLINEFRAGMENT_ID) {
								fragment = new OnLineFragment();
							}
							FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
						}
					}.run();
					mDrawerLayout.closeDrawers();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		panelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		setPanelSlideListeners(panelLayout);
	}

	/**
	 * 启动后台服务及注册广播接收器
	 */
	private void startMyService() {
		//开启service
		Intent intent = new Intent(this,MusicService.class);
		startService(intent);
		//创建意图过滤器，过滤Service发送的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION_SERVICE_PLAY);
		filter.addAction(INTENT_ACTION_SERVICE_PAUSE);
		filter.addAction(INTENT_ACTION_SERVICE_UPDATE_PROGRESS);
		//注册Activity的广播接收者
		receiver = new InnerBrodcastReceiver();
		registerReceiver(receiver,filter);
	}

	/**
	 * 广播接收者的子类，用于接收Service发送的广播
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				FragmentManager manager = getSupportFragmentManager();
				QuickControlsFragment controlsFragment = (QuickControlsFragment) manager.findFragmentById(R.id.quickcontrols_container);
				Fragment fragment = manager.findFragmentById(R.id.fragment_container);
				//接收Service发送的广播数据
				String action = intent.getAction();
				if(INTENT_ACTION_SERVICE_PLAY.equals(action)){
					//当前播放歌曲索引
					controlsFragment.startCurrentMusic(intent);
					controlsFragment.updateState();
					//更改ListView中当前播放的item字体颜色及播放图表状态
					if(LOCATIONFRAGMENT_ID == currentFragmentId) {
						((LocationFragment)fragment).setItemTextColor(intent);
						((LocationFragment)fragment).setIconState(true);
					}else if (ONLINEFRAGMENT_ID == currentFragmentId) {
						((OnLineFragment)fragment).setItemTextColor(intent);
						((OnLineFragment)fragment).setIconState(true);
					}
				}else if(INTENT_ACTION_SERVICE_PAUSE.equals(action)){
					//如果Service发送的广播为暂停，则将将播放或暂停按钮的图片设为“播放”
					if(LOCATIONFRAGMENT_ID == currentFragmentId) {
						((LocationFragment)fragment).setIconState(false);
					}else if (ONLINEFRAGMENT_ID == currentFragmentId) {
						((OnLineFragment)fragment).setIconState(false);
					}
				}else if(INTENT_ACTION_SERVICE_UPDATE_PROGRESS.equals(action)){
					//更新进度条
					controlsFragment.updateSeekbar(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// 取消注册广播接收者
		unregisterReceiver(receiver);
		//停止Service
		Intent intent = new Intent(this,MusicService.class);
		stopService(intent);
		super.onDestroy();
	}

	//	/**
	//	 * 监听系统按键
	//	 */
	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//		//判断按钮是否是Back键
	//		if(keyCode == KeyEvent.KEYCODE_BACK){
	//			//创建对话框
	//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//			builder.setIcon(R.drawable.ic_launcher);
	//			builder.setTitle(R.string.app_name);
	//			builder.setMessage("您确定要退出吗？");
	//			builder.setCancelable(false);
	////			builder.setView(view);//自定义Message中的显示内容
	//			
	//			builder.setPositiveButton("确定", this );
	//			builder.setNegativeButton("取消", this);
	//			builder.setNeutralButton("后台播放", this);
	//			exitDialog = builder.create();
	//			exitDialog.show();
	//			
	////			builder
	////			.setIcon(R.drawable.ic_launcher)
	////			.setTitle(R.string.app_name)
	////			.setMessage("您确定要退出吗？")
	////			.setPositiveButton("确定", null)
	////			.setNegativeButton("取消", null)
	////			.create()
	////			.show();
	//			return true;
	//			
	//			
	//			
	//		}
	//		return super.onKeyDown(keyCode, event);
	//	}
	//	
	//	/**
	//	 * 监听对话
	//	 */
	//	@Override
	//	public void onClick(DialogInterface dialog, int which) {
	//		//判断是哪个对话框,如果是退出框
	//		if(exitDialog == dialog){
	//			//判断弹出的对话框中点击的是哪个按钮
	//			switch (which) {
	//			case Dialog.BUTTON_POSITIVE:
	//				//如果是确认退出按钮则销毁Activity
	//				finish();
	//				break;
	//
	//			case Dialog.BUTTON_NEGATIVE:
	//				//如果是取消按钮则隐藏对话框
	//				exitDialog.dismiss();
	//				break;
	//				
	//			case Dialog.BUTTON_NEUTRAL:
	//				//如果是后台播放按钮则启动Home界面
	//				Intent intent = new Intent();
	//				intent.addCategory(Intent.CATEGORY_HOME);
	//				intent.setAction(Intent.ACTION_MAIN);
	//				startActivity(intent);
	//				break;
	//			}
	//		}
	//		
	//	}
	//	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 设置QuickControlsFragment隐藏或展现
	 * @param panelLayout
	 */
	public void setPanelSlideListeners(SlidingUpPanelLayout panelLayout) {
		panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				View nowPlayingCard = QuickControlsFragment.topContainer;
				nowPlayingCard.setAlpha(1 - slideOffset);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				View nowPlayingCard = QuickControlsFragment.topContainer;
				nowPlayingCard.setAlpha(1);
			}

			@Override
			public void onPanelExpanded(View panel) {
				View nowPlayingCard = QuickControlsFragment.topContainer;
				nowPlayingCard.setAlpha(0);
			}

			@Override
			public void onPanelAnchored(View panel) {

			}

			@Override
			public void onPanelHidden(View panel) {

			}
		});
	}

	/**
	 * 添加QuickControlsFragment
	 * @author Android
	 *
	 */
	public class initQuickControls extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			QuickControlsFragment fragment1 = new QuickControlsFragment();
			FragmentManager fragmentManager1 = getSupportFragmentManager();
			fragmentManager1.beginTransaction()
			.replace(R.id.quickcontrols_container, fragment1).commitAllowingStateLoss();
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			QuickControlsFragment.topContainer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
		}

		@Override
		protected void onPreExecute() {
		}
	}

}
