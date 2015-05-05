package com.homni.multiroom.activity;

import com.homni.multiroom.R;
import com.homni.multiroom.model.Speaker;
import com.homni.multiroom.util.GlobalValue;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 *���ý���
 * @author Dawin
 *
 */
public class SettingActivity extends BaseActivity
{
	private final String TAG="SpeakerSettingActivity";
	private CheckBox leftChannelCb;
	private CheckBox rightChannelCb;
	//���ø���
	private Button musicBtn;
	//�û�ѡ��Ҫ���õ�����
	private Speaker userPickSpeaker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker_setting);
		initView();
		//�����豸�б��ݵ����ȶ���
		userPickSpeaker = (Speaker) getIntent().getSerializableExtra("speakerObj");
		//��ǰѡ��ĸ���
		if (GlobalValue.musics != null)
		{
			musicBtn.setText(GlobalValue.musics.get(GlobalValue.currentMusicId).getMusicName());
		}
	}
	
	public void initView()
	{
		musicBtn = (Button) findViewById(R.id.music_button);
		leftChannelCb = (CheckBox) findViewById(R.id.left_channel);
		rightChannelCb = (CheckBox) findViewById(R.id.right_channel);
	}

	
   /**
	* ѡ�񲥷ŵ���������
	* 
	* @param view
	* 
	*/
	public void selectChannel(View view)
	{
		switch (view.getId())
		{
		//ѡ��������
		case R.id.left_channel:
			if (leftChannelCb.isChecked())
			{
				userPickSpeaker.setLeftStatus(true);
				userPickSpeaker.getLeftChannel().setSelect(true);
			} else
			{
				userPickSpeaker.setLeftStatus(false);
				userPickSpeaker.getLeftChannel().setSelect(false);
			}
			break;
		//ѡ��������
		case R.id.right_channel:
			if (rightChannelCb.isChecked())
			{
				userPickSpeaker.setRightStatus(true);
				userPickSpeaker.getRightChannel().setSelect(true);
			} else
			{
				userPickSpeaker.setRightStatus(false);
				userPickSpeaker.getRightChannel().setSelect(false);
			}
			break;
		}
	}

	/**
	 * �л��������б�,ѡ�����
	 * @param view
	 */
	public void selectMusic(View view)
	{
		Intent intent = new Intent(context, SongsActivity.class);		
		// ������ >=0 this code will be returned in onActivityResult() when the activity exits
		startActivityForResult(intent, 1);		
	}
	
	/**
	 * �����û�����
	 */
	public void confirm(View view)
	{
		int defaultValue=0;
		GlobalValue.currentSpeakerId=getIntent().getIntExtra("currentSpeakerId", defaultValue);
		Log.d(TAG, "GlobalValue.currentSpeakerId="+GlobalValue.currentSpeakerId);			
		
		GlobalValue.speakers.get(GlobalValue.currentSpeakerId).setRightStatus(userPickSpeaker.isRightStatus());		
		GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getRightChannel().setSelect(userPickSpeaker.getRightChannel().isSelect());
		GlobalValue.speakers.get(GlobalValue.currentSpeakerId).setLeftStatus(userPickSpeaker.isLeftStatus());
		GlobalValue.speakers.get(GlobalValue.currentSpeakerId).getLeftChannel().setSelect(userPickSpeaker.getLeftChannel().isSelect());
		Intent intent = new Intent();			
		setResult(1, intent);		
		finish();
	}

	/**
	 * ����ת��activity(�������activity)ʹ�����, ���ٵ�ʱ����ø÷���
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 1)
		{
			// ��ʾѡ��ĸ�����
			musicBtn.setText(GlobalValue.musics.get(GlobalValue.currentMusicId).getMusicName());
		}
	}
    
}
