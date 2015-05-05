package com.homni.multiroom.command;

import com.homni.multiroom.util.GlobalValue;
import com.homni.multiroom.util.PhoneInfoHelp;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * ��Ԫ����
 * @author admin
 *
 */
public class AndroidUnitTest extends AndroidTestCase
{
	private final String TAG="AndroidUnitTest";
	ControlCommand controlCommand = new ControlCommand();

	public void testGetServerIP()
	{
		System.out.println("R ascii=" + (int) 'R');// 82
		System.out.println("0.0.0.0.length()=" + "0.0.0.0".length());// 7

		byte b = (byte) 192;
		b = (byte) 168;
		b = (byte) 223;
		String.format("%02X", b);
		Byte bytes = -42;
		int result = bytes & 0xff;
		System.out.println("�޷�����: \t" + result);
		System.out.println("2����bitλ: \t" + Integer.toBinaryString(result));
		// controlCommand.getServerIP();	
	}
	
	public void testGetDevicesStatus()
	{
		//0x41 �豸״̬������Դip=ռ�ã�0û����Դ=����
		controlCommand.getDevicesStatus("192.168.1.99");
	}
	
	public void testKeepAlive()
	{
		controlCommand.isAlive("192.168.1.99");
	}
	public void testGetDevices()
	{
		//0x40
		controlCommand.getDevices("192.168.1.99");
	}
	
	public void testSetDevicesStatus()
	{
		//0x42
		controlCommand.setDevicesStatus(new int[]{0},'L',GlobalValue.localIp);
	}
	//ע���ֻ�
	public void testRegisterDevice(){
		//0x11
		if(controlCommand.isRegisterDeviceOK((byte)0,"192.168.1.99")){
		//TODO: debug=true,please delete when code ok
		GlobalValue.isAppOpen=true;
	    new Thread()
		{
			public void run()
			{
				System.out.println("==========================new Thread()============================");
				while (true)
				{
					try
					{
						Log.d(TAG, "receive...");
					//	mSocket.receive(packet);
					//	Log.e(TAG, "���������ص�����" + packet.getData()[11]);
						Thread.sleep(200);
					} catch (Exception e)
					{
						Log.e(TAG, "���շ����������� error��" + e.getMessage());
					}				
				}
			};
		}.start();
		}
	}
	public void testGetMac(){
		PhoneInfoHelp.getMac();
	}
	
	public void test16Str2Byte()
	{
		// ����Ipת��4�ֽ�
		byte[] localId = ipStr2Byte("192.168.1.100"/* GlobalValue.localIp */);
		// uuid
		String[] dst = PhoneInfoHelp.getMac().split("\\:", 6);
		int index = 0;
		for (String str : dst)
		{
            //0x84=4*+8*16=132
			/* uuid[index]= (byte) */System.out.println(Integer.valueOf(str, 16));
			index++;
		}
	}

	/**
	 * ip�ַ���ת��4���ֽ�
	 * 
	 * @param ipStr
	 *            ip�ַ���
	 * @return
	 */
	public byte[] ipStr2Byte(String ipStr)
	{
		String literals[] = (ipStr).split("\\.", 4);
		byte ip[] = new byte[4], c = 0;
		for (String d : literals)
		{
			ip[c++] = (byte) Short.parseShort(d);
		}
		return ip;
	}

	public void testStopRecorder()
	{
		// 0:��ͣ, 1:�ָ�, 2:ֹͣ
		controlCommand.playManage((byte) 2, "192.168.1.201", 'L', "192.168.1.99");
	}
	
	public void testChangeChannel(){
		//�ı�¼���豸���ŵ����ȡ�
		//No:from 0 to 2n
		controlCommand.changeChanel("192.168.1.201",3, 0, 'L');
	}
}
