package com.homni.multiroom.activity;

import java.util.ArrayList;
import java.util.List;

import com.homni.multiroom.R;
import com.homni.multiroom.adapter.ChannelAdapter;
import com.homni.multiroom.command.AudioCommand;
import com.homni.multiroom.command.ControlCommand.ChannelType;
import com.homni.multiroom.model.CurrentSelectedChannel;
import com.homni.multiroom.util.GlobalValue;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * ���ֿ��ƣ����š���ͣ��ֹͣ
 * 
 * @author Dawin
 *
 */
public class PlayMusicActivity extends BaseActivity
{
	private static final String TAG = "MusicPlayActivity";
	// ������ƵЭ��
	private AudioCommand audioCommand = new AudioCommand();	
	//�����л�ѡ��
	private Spinner spnChangeChannel;
	private ChannelAdapter channelAdapter;
	// ��ǰ��������
	//String[] itemsChannel = getResources().getStringArray(R.array.spinnername);
	//������Դ���������
	private ChannelType channelType;
	private List<CurrentSelectedChannel> selectedChannelList;
	/**���л��������������λ�ã���ִ���л��󣬸��ݴ˱���ɾ�����������Ŀ*/
	//private int selectPosition;
	private final int SOURCE_LEFT_CHANNEL=0;
	private final int SOURCE_RIGHT_CHANNEL=1;
	//��Դ��������,Ĭ�ϲ�����Դ������
	private int sourceChannelType=SOURCE_LEFT_CHANNEL;
	/** ����ID��1,2,3...�ź�*/
	private int channelID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_play);		
		selectedChannelList= getChannelSelected();
		channelAdapter=new ChannelAdapter(context,selectedChannelList);
		sourceChannelType = getIntent().getIntExtra("channelType", -1);
		spnChangeChannel.setAdapter(channelAdapter);		
		spnChangeChannel.setOnItemSelectedListener(channelItemtClick);			
	}

	OnItemSelectedListener channelItemtClick=new OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			Toast.makeText(context, "onItemSelected position="+position,1).show();		
			channelID=selectedChannelList.get(position).getChannelID();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{	
			
		}	
	};
	
	@Override
	public void initView()
	{
		spnChangeChannel=(Spinner)findViewById(R.id.from_channel);		
	}
	
	/** ��ѯ���б�ѡ������� */
	public List<CurrentSelectedChannel> getChannelSelected()
	{
		int speakersSize = GlobalValue.speakers.size();
		List<CurrentSelectedChannel> selectedChannelList = new ArrayList<CurrentSelectedChannel>();
		for (int i = 0; i < speakersSize; i++)
		{
			// TODO:���л�����ѡ���в���ʾ��ǰ����
			if ( GlobalValue.speakers.get(i).getLeftChannel().isSelect()&&(GlobalValue.currentSpeakerId * 2 + 1!= i * 2 + 1) )
			{
				CurrentSelectedChannel currentSelectedChannel = new CurrentSelectedChannel();
				currentSelectedChannel.setDeviceName(GlobalValue.speakers.get(i).getIpStr());
				currentSelectedChannel.setChannelName("Left");	
				currentSelectedChannel.setChannelID(i * 2 + 1);
				selectedChannelList.add(currentSelectedChannel);
			}
			if (GlobalValue.speakers.get(i).getRightChannel().isSelect()&&(GlobalValue.currentSpeakerId * 2 + 2 != i * 2 +2)  )
			{
				CurrentSelectedChannel currentSelectedChannel = new CurrentSelectedChannel();
				currentSelectedChannel.setDeviceName(GlobalValue.speakers.get(i).getIpStr());
				currentSelectedChannel.setChannelID(i * 2 + 2);
				currentSelectedChannel.setChannelName("Right");
				selectedChannelList.add(currentSelectedChannel);
			}
		}
		return selectedChannelList;
	}
	/**
	 * �л�����
	 * 
	 * @param view
	 * 
	 */
	public void changeChannel(View view)
	{
		switch (channelID)
		{
		/*=========================1,3,5,7������===========================*/
		case 1:
			GlobalValue.speakers.get(0).getLeftChannel().setSelect(false);
		    //�ı�ip
			GlobalValue.ip1=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			//TODO:�ı�port
			break;		
		case 3:
			GlobalValue.speakers.get(1).getLeftChannel().setSelect(false);
			GlobalValue.ip3=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;
		case 5:
			GlobalValue.speakers.get(2).getLeftChannel().setSelect(false);
			GlobalValue.ip5=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;
		case 7:
			GlobalValue.speakers.get(3).getLeftChannel().setSelect(false);
			GlobalValue.ip7=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;
			/*=========================2,4,6,8������===========================*/
		case 2:
			GlobalValue.speakers.get(0).getRightChannel().setSelect(false);
			GlobalValue.ip2=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;
		case 4:
			GlobalValue.speakers.get(1).getRightChannel().setSelect(false);
			GlobalValue.ip4=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;	
		case 6:
			GlobalValue.speakers.get(2).getRightChannel().setSelect(false);
			GlobalValue.ip6=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;	
		case 8:
			GlobalValue.speakers.get(3).getRightChannel().setSelect(false);
			GlobalValue.ip8=GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getIpStr();
			break;
		}
	}
	
	
	// ѡ����Դ����
	public void pickSourceChanelType(View view)
	{
		switch (view.getId())
		{
		case R.id.source_left_channel:
			sourceChannelType=SOURCE_LEFT_CHANNEL;
			break;
		case R.id.source_right_channel:
			sourceChannelType=SOURCE_RIGHT_CHANNEL;
			break;
		}
	}
	
	/** ���ſ��� */
	public void playControl(View view)
	{	
		if (GlobalValue.musics == null)
		{
			Toast.makeText(context, "δѡ����", 1).show();
			return;
		}

		if (sourceChannelType == SOURCE_LEFT_CHANNEL)
		{
			channelType = ChannelType.LEFT;
		} else if (sourceChannelType == SOURCE_RIGHT_CHANNEL)
		{
			channelType = ChannelType.RIGHT;
		}
		// TODO:�ж��Ƿ��ѿ���
		switch (view.getId())
		{
		//TODO:���������ѯ�豸״̬�������豸���ڲ��ţ���ʾռ�ã���ֹ���Ų���
		// ���ŷ����������1����--����2��ʼ״̬--����
		case R.id.play_button:
			Log.d(TAG, "PLAY");
			// 1 ��δ����ת������״̬
			//if (audioCommand.getState() == AudioCommand.IDLE)
			//{
			//TODO:�ж��Ƿ��Ѿ��ڲ���
				new Thread()
				{
					public void run()
					{
						Log.d(TAG, "IDLE to PLAY");
						// ��¼��ѡ������
						if (channelType == ChannelType.LEFT)
						{
							GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getLeftChannel().setSelect(true);
							channelID=GlobalValue.currentSpeakerId*2+1;
						} else
						{
							GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getRightChannel().setSelect(true);
							channelID=GlobalValue.currentSpeakerId*2+2;
						}						
						//TODO:���ѡ���л�����,�����л�������channel select��Ϊfalse					
						audioCommand.play(
								GlobalValue.musics.get(GlobalValue.currentMusicId).getUrl(), 
								GlobalValue.speakers.get(GlobalValue.currentSpeakerId),
								channelType,
								channelID
								);
					};
				}.start();
		  //}
		  // 2 ��ͣ״̬ת������
	      /*else if (audioCommand.getState() == AudioCommand.PAUSE)
			{
				Log.d(TAG, "PAUSE to PLAY");
				audioCommand.setState(AudioCommand.PLAY);
				audioCommand.wakeUp();
			}*/
			break;
		// ��ͣ
		case R.id.pause_button:
			audioCommand.pause();
			break;
		// ֹͣ
		case R.id.stop_button:
			audioCommand.stop();
			break;
		}
	}
	
}
