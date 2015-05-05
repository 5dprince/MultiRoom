package com.homni.multiroom.activity;

import com.homni.multiroom.R;
import com.homni.multiroom.adapter.MusicAdapter;
import com.homni.multiroom.util.GlobalValue;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �����б����
 * 
 * @author Dawin
 *
 */
public class SongsActivity extends BaseActivity
{
	private static final String TAG = "SongsActivity";
	// ����������
	private MusicAdapter musicAdapter;
	private ListView musicListView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_songs);	
		initView();		
		musicAdapter = new MusicAdapter(context, GlobalValue.musics);
		musicListView.setAdapter(musicAdapter);
		topRightBtn.setVisibility(View.GONE);
		// ����б�����¼�
		musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				GlobalValue.currentMusicId = position;
				titleBarNameTv.setText(GlobalValue.musics.get(position).getMusicName());
			}
		});
		// ��ʾ��ѡ����
		if (GlobalValue.musics != null)
		{
			titleBarNameTv.setText(GlobalValue.musics.get(GlobalValue.currentMusicId).getMusicName());
		}
	}

	public void initView()
	{
		topRightBtn = (Button) findViewById(R.id.right_button);
		titleBarNameTv = (TextView) findViewById(R.id.title_bar_name);
		musicListView = (ListView) findViewById(R.id.song_listview);
	}

	// ����
	public void return0(View v)
	{
		Intent intent = new Intent();
		// ���뷵��ֵ,����ѡ��ĸ���ID
		intent.putExtra("currentMusicId", GlobalValue.currentMusicId);
		// ����ش���ֵ,�����һ��Code,�������ַ��ص�����
		setResult(1, intent);
		finish();
	}
}
