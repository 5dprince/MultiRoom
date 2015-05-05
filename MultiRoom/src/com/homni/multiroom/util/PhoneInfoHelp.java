package com.homni.multiroom.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
/**�ֻ��豸��Ϣ������*/
public class PhoneInfoHelp
{
	private final static String TAG="PhoneInfoHelp";

	/** ��ȡIP��ַ */
	public static String getLocalIP(Context context)
	{
		// ��ȡwifi����
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// �ж�wifi�Ƿ���
		if (!wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String localIP = intToIp(ipAddress);
		Log.e(TAG, "Phone local IP:" + localIP);
		return localIP;
	}
	
	private static String intToIp(int i)
	{
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}  
	
	public static String getMac()
	{
		String macSerial = null;
		String str = "";
		try
		{
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;)
			{
				str = input.readLine();
				if (str != null)
				{// ȥ�ո�
					macSerial = str.trim();
					break;
				}
			}
		} catch (IOException ex)
		{
			// ����Ĭ��ֵ
			ex.printStackTrace();
		}
		// 84:7a:88:72:97:d1
		return macSerial;
	}	
	
	/*	public void getDeviceId(Context context)
	{
		// ��õ��豸��Ψһ��ţ� TelephonyManager.getDeviceId() ���㹻�ˡ�
		// �������Ա�¶��DeviceID��ʹһЩ�û�������������ð���Щid�����ˡ�
		// ʵ���ϼ��ܺ�������Ȼ����Ψһ��ʶ����豸�����Ҳ������Եı�¶�û����ض��豸��
		// ���磬ʹ�� String.hashCode() �����UUID��
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();// htc one 99000146898635 meid
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		System.out.println("uniqueId=" + uniqueId);
	}*/
	
}
