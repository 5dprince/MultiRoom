package com.homni.multiroom.activity;

import com.homni.multiroom.util.ActivityUtil;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity�Ļ���
 * 
 * @author Dawin
 * 
 *         ����Activity���̳д���
 */
public abstract class BaseActivity extends Activity
{
	//������ť
	public Button topLeftBtn;
	//�����Ұ�ť
	public Button topRightBtn;
	//��������
	public TextView titleBarNameTv;
	/** �����Ķ���:ָ��������Ļ��� */
	public Context context;
	/** �Ƿ�����ȫ�� */
	private boolean mAllowFullScreen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		context = this;
		// ��Activity����List����
		ActivityUtil.getInstance().addActivity(this);
		// ȥ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��������
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// �Ƿ�ȫ��
		if (mAllowFullScreen)
		{
			// ȡ������
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		// ���������
		//hideSoftInputView();
	}

	public abstract void initView();

	/**
	 * ���������
	 */
	public void hideSoftInputView()
	{
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
		{
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/** ���� */
	public void return0(View v)
	{
		finish();
	}

}
