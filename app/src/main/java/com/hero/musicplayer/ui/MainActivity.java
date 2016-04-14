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
	 * �㲥������
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
	private String [] str = {"��������","��������"};
	private List<String> nav = Arrays.asList(str);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new initQuickControls().execute("");
		//��ʼ���ؼ�
		initViews();
		//������̨����ע��㲥������
		startMyService();
		//��ֹFragment�ص������ϴα����fragment����
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
		//ʵ��Toolbar�Ĳ˵���ť�ļ�ͷ���ع���
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
							//�л�Fragment����
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
	 * ������̨����ע��㲥������
	 */
	private void startMyService() {
		//����service
		Intent intent = new Intent(this,MusicService.class);
		startService(intent);
		//������ͼ������������Service���͵Ĺ㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction(INTENT_ACTION_SERVICE_PLAY);
		filter.addAction(INTENT_ACTION_SERVICE_PAUSE);
		filter.addAction(INTENT_ACTION_SERVICE_UPDATE_PROGRESS);
		//ע��Activity�Ĺ㲥������
		receiver = new InnerBrodcastReceiver();
		registerReceiver(receiver,filter);
	}

	/**
	 * �㲥�����ߵ����࣬���ڽ���Service���͵Ĺ㲥
	 */
	private class InnerBrodcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				FragmentManager manager = getSupportFragmentManager();
				QuickControlsFragment controlsFragment = (QuickControlsFragment) manager.findFragmentById(R.id.quickcontrols_container);
				Fragment fragment = manager.findFragmentById(R.id.fragment_container);
				//����Service���͵Ĺ㲥����
				String action = intent.getAction();
				if(INTENT_ACTION_SERVICE_PLAY.equals(action)){
					//��ǰ���Ÿ�������
					controlsFragment.startCurrentMusic(intent);
					controlsFragment.updateState();
					//����ListView�е�ǰ���ŵ�item������ɫ������ͼ��״̬
					if(LOCATIONFRAGMENT_ID == currentFragmentId) {
						((LocationFragment)fragment).setItemTextColor(intent);
						((LocationFragment)fragment).setIconState(true);
					}else if (ONLINEFRAGMENT_ID == currentFragmentId) {
						((OnLineFragment)fragment).setItemTextColor(intent);
						((OnLineFragment)fragment).setIconState(true);
					}
				}else if(INTENT_ACTION_SERVICE_PAUSE.equals(action)){
					//���Service���͵Ĺ㲥Ϊ��ͣ���򽫽����Ż���ͣ��ť��ͼƬ��Ϊ�����š�
					if(LOCATIONFRAGMENT_ID == currentFragmentId) {
						((LocationFragment)fragment).setIconState(false);
					}else if (ONLINEFRAGMENT_ID == currentFragmentId) {
						((OnLineFragment)fragment).setIconState(false);
					}
				}else if(INTENT_ACTION_SERVICE_UPDATE_PROGRESS.equals(action)){
					//���½�����
					controlsFragment.updateSeekbar(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// ȡ��ע��㲥������
		unregisterReceiver(receiver);
		//ֹͣService
		Intent intent = new Intent(this,MusicService.class);
		stopService(intent);
		super.onDestroy();
	}

	//	/**
	//	 * ����ϵͳ����
	//	 */
	//	@Override
	//	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//		//�жϰ�ť�Ƿ���Back��
	//		if(keyCode == KeyEvent.KEYCODE_BACK){
	//			//�����Ի���
	//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//			builder.setIcon(R.drawable.ic_launcher);
	//			builder.setTitle(R.string.app_name);
	//			builder.setMessage("��ȷ��Ҫ�˳���");
	//			builder.setCancelable(false);
	////			builder.setView(view);//�Զ���Message�е���ʾ����
	//			
	//			builder.setPositiveButton("ȷ��", this );
	//			builder.setNegativeButton("ȡ��", this);
	//			builder.setNeutralButton("��̨����", this);
	//			exitDialog = builder.create();
	//			exitDialog.show();
	//			
	////			builder
	////			.setIcon(R.drawable.ic_launcher)
	////			.setTitle(R.string.app_name)
	////			.setMessage("��ȷ��Ҫ�˳���")
	////			.setPositiveButton("ȷ��", null)
	////			.setNegativeButton("ȡ��", null)
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
	//	 * �����Ի�
	//	 */
	//	@Override
	//	public void onClick(DialogInterface dialog, int which) {
	//		//�ж����ĸ��Ի���,������˳���
	//		if(exitDialog == dialog){
	//			//�жϵ����ĶԻ����е�������ĸ���ť
	//			switch (which) {
	//			case Dialog.BUTTON_POSITIVE:
	//				//�����ȷ���˳���ť������Activity
	//				finish();
	//				break;
	//
	//			case Dialog.BUTTON_NEGATIVE:
	//				//�����ȡ����ť�����ضԻ���
	//				exitDialog.dismiss();
	//				break;
	//				
	//			case Dialog.BUTTON_NEUTRAL:
	//				//����Ǻ�̨���Ű�ť������Home����
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
	 * ����QuickControlsFragment���ػ�չ��
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
	 * ���QuickControlsFragment
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
