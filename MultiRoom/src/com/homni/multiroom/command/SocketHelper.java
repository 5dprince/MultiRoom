package com.homni.multiroom.command;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;

/**
 * Socket�շ�UDP
 * 
 * @author kingdawin
 *
 */
public class SocketHelper
{
	private final String TAG="SocketHelper";
	public  SocketHelper mSocketHelper;
	public  DatagramSocket mSocket;	
	public DatagramPacket mPacket;
	// ���ͻ�����
	public byte[] sendBuffer;
	// ���ջ�����
	public byte[] receiveBuffer;
	/**���ó�ʱΪ200ms�� */ 
	private static final int TIMEOUT = 200/*1000*/;   
	
	/** Э��ͷ�� */
	public byte[] head = new byte[] { 'X', 'X', 'X', 'C', 'M', 'D' };
	/** У��� */
	public byte[] checkSum = new byte[2];
	/** Э��汾 */
	byte[] protocalVersion = new byte[1];
	/** ����IP */
	byte[] localIP = new byte[4];
	/** �����˿� */
	byte[] localPort = new byte[2];
	/** Ŀ��IP */
	byte[] destIP = new byte[4];
	/** Ŀ��˿� */
	byte[] destPort = new byte[2];
	/** ����Id */
	byte[] localId = new byte[4];
	/** �ỰId */
	byte[] sessionId = new byte[4];
	/** ���� */
	public byte[] cmd = new byte[1];
	
	/** ������ */
	public byte[] end = new byte[] { (byte) 0xff, (byte) 0xff };

	public SocketHelper()
	{
		// ����UDP datagram socket
		//����һ��DatagramSocketʵ���������ö���󶨵�����Ĭ��IP��ַ���������п��ö˿������ѡ���ĳ���˿�
		try
		{
			mSocket = new DatagramSocket();
			//��������ʱ�� 
			mSocket.setSoTimeout(TIMEOUT);			
		} catch (SocketException e)
		{
			// create socket fail
			Log.e(TAG, "SocketException error:"+e.getMessage());
		}
	}
}
