package com.homni.multiroom.activity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.homni.multiroom.R;
import com.homni.multiroom.adapter.RecorderAdapter;
import com.homni.multiroom.command.ControlCommand;
import com.homni.multiroom.model.Recorder;
import com.homni.multiroom.model.Speaker;
import com.homni.multiroom.util.GlobalValue;
import com.homni.multiroom.util.ListItemClickHelp;
import com.homni.multiroom.util.MusicLoader;
import com.homni.multiroom.util.PhoneInfoHelp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ������
 * 
 * @author Dawin
 * ����¼���豸���̣�
 * ���ҷ�����IP--->ע���ֻ�--->�÷�����IP�����豸�б�--->����GridView��BaseAdapter��ʾ�豸�б�
 */
public class MainActivity extends BaseActivity implements ListItemClickHelp 
{
	private final String TAG = "GridViewActivity";	
	private Animation animation;
	/*************************View*************************/
	// ��������--��Ϊ-->¼���豸
	private GridView recorderDeviceGridView;
	//�����������ư�ť
	private Button leftChannelBtn;
	private Button rightChannelBtn;
	private ControlCommand controlCommand;
	/** ������Ip*/
	private String serverIp;
	// ����������
	//DeviceAdapter deviceAdapter;
	private RecorderAdapter recorderAdapter;
	/*************************Socket����Server����**************************/
	public static DatagramSocket socket;
	public static DatagramPacket packet;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speakers);
		GlobalValue.isAppOpen = true;
		initView();
		controlCommand = new ControlCommand();
		//���ñ���IP
		GlobalValue.localIp=PhoneInfoHelp.getLocalIP(context);
		// �����ֻ������б�
		if (GlobalValue.musics == null)
		{
			GlobalValue.musics = MusicLoader.getInstance(getContentResolver()).getMusicList();
		}
		// ͸���ȶ���
		animation = AnimationUtils.loadAnimation(this, R.anim.pulse);
		recorderDeviceGridView.setOnItemClickListener(recorderItemClickListener);
		//ע���ֻ�
		//registerDevice();
		
		// new Thread()
		// {
		// public void run()
		// {
		// // ��������
		// controlCommand.isAlive(/*serverIp*/"192.168.1.99");
		// };
		// }.start();
	}
	
	/**ע���ֻ�*/ 
	public void registerDevice()
	{
		initSocket();
		new Thread()
		{
			public void run()
			{
				if (controlCommand.isRegisterDeviceOK((byte) 0, "192.168.1.99"))
				{
					new Thread()
					{
						public void run()
						{
							while (true && GlobalValue.isAppOpen)
							{
								try
								{
									Log.d(TAG, "receive...");
									socket.receive(packet);
									Log.w(TAG, "Server--->Android cmd=0x" + String.format("%02X", packet.getData()[11]));
									if (String.format("%02X", packet.getData()[11]).equals("99"))
									{
										// �ظ�server
										controlCommand.androidToServerKeepAlive("192.168.1.99");
									}
								} catch (Exception e)
								{
									Log.e(TAG, "����Server������ error break!!��" + e.getMessage());
									break;
									// TODO:�˳�ע��
								}
							}
						};
					}.start();
				}
			};
		}.start();
	}
	public void initSocket()
	{
		try
		{
			socket = new DatagramSocket(9200);
			socket.setSoTimeout(15000);
			packet = new DatagramPacket(new byte[100], 100, InetAddress.getByName("192.168.1.99"), 9200);
		} catch (Exception e)
		{
			Log.e(TAG, "initSocket error" + e.getMessage());
		}
	}
	
    /**���¼���豸�¼�*/
	private OnItemClickListener recorderItemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{	
			if (ControlCommand.recorders.size() > 0)
			{
				Intent intent = new Intent(context, PlayerListActivity.class);
				// ���豸Ip
				intent.putExtra("deviceIp", ControlCommand.recorders.get(position).getIp());
				// ���豸���ͣ�¼���豸|�ֻ�
				intent.putExtra("deviceType", "recorder");
				startActivityForResult(intent, 1);
			} else
			{
				Toast.makeText(context, "û��¼���豸����������", 1).show();
			}					
		}
	};
	//�����˳��󣬸����豸�б�
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(recorderAdapter/*deviceAdapter*/==null)return;
		
		if (resultCode == 1)
		{
			Log.d(TAG, "device update");			
			//recorderAdapter/*deviceAdapter*/.setSelectSpeakerId(GlobalValue.currentSpeakerId);
			recorderAdapter/*deviceAdapter*/.notifyDataSetChanged();
		}
	}
	
	public void initView()
	{		
		leftChannelBtn = (Button) findViewById(R.id.left_channel_button);
		rightChannelBtn = (Button) findViewById(R.id.right_channel_button);
		recorderDeviceGridView = (GridView) findViewById(R.id.gridView1);
	}
	/** ���ҷ����� */
	private static final int SEARCH_SERVER = 0;
	/** ��ʾ�豸�б� */
	private static final int SHOW_DEVICES = 1;
	private List<Recorder> recorders;
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			// ��ѯ����������ȡ�豸�б�
			case SEARCH_SERVER:
				//�ҵ�������
				if (serverIp.length() > 0)
				{
					new Thread()
					{
						public void run()
						{							
							controlCommand = new ControlCommand();
							//��ip���豸�б�
							GlobalValue.speakers = controlCommand.getDevices(serverIp);		
							recorders=ControlCommand.recorders;
							handler.sendEmptyMessage(SHOW_DEVICES);
						};
					}.start();

				} else
				{
					//δ�ҵ�������������豸�б�
					GlobalValue.speakers = new ArrayList<Speaker>();
					recorders=new ArrayList<Recorder>();
					recorderAdapter=new RecorderAdapter(recorders,context);
					recorderDeviceGridView.setAdapter(recorderAdapter);
					
					//deviceAdapter = new DeviceAdapter(GlobalValue.speakers, context,SpeakersActivity.this);
					//loudSpeakerGv.setAdapter(deviceAdapter);
				}
				break;
			case SHOW_DEVICES:
				//deviceAdapter = new DeviceAdapter(GlobalValue.speakers, context,SpeakersActivity.this);			
				//loudSpeakerGv.setAdapter(deviceAdapter);
				recorderAdapter=new RecorderAdapter(recorders,context);
				recorderDeviceGridView.setAdapter(recorderAdapter);
				break;		
			}
		}
	};

	/**����豸������*/ 
	@Override
	public void onClick(View item, View widget, int position, int which)
	{
		Log.d(TAG, "list button position=" + position);
		// TODO:�ж������Ƿ��ѿ���
		if (position == GlobalValue.currentSpeakerId)
		{
			switch (which)
			{
			case R.id.left_channel_button:
				if (GlobalValue.speakers.get(GlobalValue.currentSpeakerId).isLeftStatus())
				{
					Intent intent = new Intent(context, PlayMusicActivity.class);
					//0:��������1��������
					intent.putExtra("channelType", 0);					
					startActivity(intent);
				}
				break;
			case R.id.right_channel_button:		
				if (GlobalValue.speakers.get(GlobalValue.currentSpeakerId).isRightStatus())
				{
					Intent intent = new Intent(context, PlayMusicActivity.class);
					//0:��������1��������
					intent.putExtra("channelType", 1);
					startActivity(intent);
				}
				break;
			}
		}
	}
	
	public void clickView(View view)
	{
		switch (view.getId())
		{
		// ѡ�񲥷��豸
		case R.id.pick_player_btn:
			Intent intent = new Intent(context, PlayerListActivity.class);
			intent.putExtra("deviceIp", GlobalValue.localIp);
			intent.putExtra("deviceType", "phone");
			startActivityForResult(intent, 1);
			break;
		// ����������
		case R.id.search_server_btn:
			new Thread()
			{
				@Override
				public void run()
				{
					// ���ҷ�����IP
					serverIp = controlCommand.getServerIP();
					handler.sendEmptyMessage(SEARCH_SERVER);
				}
			}.start();
			break;
		// ���ø���
		case R.id.set_music_btn:
			Intent intent0 = new Intent(context, SongsActivity.class);
			// ������ >=0 this code will be returned in onActivityResult() when the
			// activity exits
			startActivityForResult(intent0, 1);
			break;
		}
	}
	 
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		GlobalValue.isAppOpen=false;
	}	
}
