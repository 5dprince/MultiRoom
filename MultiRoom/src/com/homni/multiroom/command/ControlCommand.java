package com.homni.multiroom.command;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.util.Log;

import com.homni.multiroom.model.Recorder;
import com.homni.multiroom.model.Speaker;
import com.homni.multiroom.util.GlobalValue;
import com.homni.multiroom.util.PhoneInfoHelp;




/**
 * ¼���������豸����Э��
 * @author Dawin
 *
 */
public class ControlCommand extends SocketHelper
{
	private final String TAG="ControlCommand";
	
	/*===================================CMD========================================*/
	/**Server����״̬��ѯ*/
	private final byte DEVICES_STATUS=(byte)0x41;
	/**�����豸״̬*/
	private final byte SETTING_DEVICE_INFO=(byte)0x42;
	/**������*/
	private final byte KEEP_ALIVE=(byte)0x99;	
	/*==============================================================================*/
	
	/*==================================Query Buffer State Command==================================*/
	/**Э��ͷ��*/
	//Э�鳤��
	byte[] cmdLength=new byte[2];	
	/**У���*/	
	/**Э��汾*/
	/**����*/
	/**��������*/
	byte[] cmdType=new byte[1];
	/**����IP*/
	/**�����˿�*/
	/**Ŀ��IP*/
	/**Ŀ��˿�*/
	/**����Id*/
	/**�ỰId*/
	/**����4�ֽ�*/
	byte[] other=new byte[4];	
	//buffer status response
	byte[] year = new byte[2];
	byte[] month = new byte[1];
	byte[] day = new byte[1];
	byte[] hour = new byte[4];
	byte[] minute = new byte[4];
	byte[] second = new byte[1];
	byte[] mSecond = new byte[2];
	byte[] leftCurrentSize = new byte[4];
	byte[] rightCurrentSize = new byte[4];
	byte[] otherRes = new byte[4];

	byte[] deviceType=new byte[1];
	/**
	 * ��������
	 * 
	 * @author Dawin
	 *
	 */
	public static enum ChannelType {	
		LEFT, RIGHT;
	}
	/**
	 * ��������������ǰ���ݴ�С
	 * 
	 * @param speaker
	 *            ����
	 * @param channelType
	 *            ��������{ChannelType.LEFT,ChannelType.RIGHT}
	 * @return ��������ǰ���ݴ�С
	 */
	public int getCurrentBufferNum(/*Speaker speaker,*/ ChannelType channelType,int channelID)
	{
		int bufferSize;
		/* =================================����================================= */
		// ƴ�ӷ���Э��
		byte[] queryBufferStateCMD = new byte[39];
		System.arraycopy(head, 0, queryBufferStateCMD, 0, 6);
		System.arraycopy(cmdLength, 0, queryBufferStateCMD, 6, 2);
		System.arraycopy(checkSum, 0, queryBufferStateCMD, 8, 2);
		System.arraycopy(protocalVersion, 0, queryBufferStateCMD, 10, 1);
		System.arraycopy(new byte[] { 0x33 }, 0, queryBufferStateCMD, 11, 1);
		System.arraycopy(cmdType, 0, queryBufferStateCMD, 12, 1);
		System.arraycopy(localIP, 0, queryBufferStateCMD, 13, 4);
		System.arraycopy(localPort, 0, queryBufferStateCMD, 17, 2);
		System.arraycopy(destIP, 0, queryBufferStateCMD, 19, 4);
		System.arraycopy(destPort, 0, queryBufferStateCMD, 23, 2);
		System.arraycopy(localId, 0, queryBufferStateCMD, 25, 4);
		System.arraycopy(sessionId, 0, queryBufferStateCMD, 29, 4);
		System.arraycopy(other, 0, queryBufferStateCMD, 33, 4);
		System.arraycopy(end, 0, queryBufferStateCMD, 37, 2);
			
		try
		{
			// �����͵�Э��������
			//��һ���������ݵ�����������DatagramPacket���󣬴�����DatagramPacket����ʱ��ָ����IP��ַ�Ͷ˿�--��;����˸����ݱ���Ŀ�ĵ�
			
			switch (channelID)
			{
			case 1:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip1), 9200);
				break;
			case 2:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip2), 9200);
				break;
			case 3:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip3), 9200);
				break;
			case 4:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip4), 9200);
				break;
			case 5:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip5), 9200);
				break;
			case 6:	
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip6), 9200);
				break;
			case 7:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip7), 9200);
				break;
			case 8:
				mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(GlobalValue.ip8), 9200);
				break;
			}
			
		    //mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, /*speaker.getIp()*/, 9200);
			// ����
			//ʹ��DatagramSocket�������ݱ�ʱ��DatagramSocket����֪���������ݱ����͵����������DatagramPacket����������ݱ���Ŀ�ĵء�
			//������ͷ����֪��ÿ����װ���Ŀ�ĵأ���ͷֻ�ǽ���Щ��װ�䷢�ͳ�ȥ������װ�䱾������˸ü�װ���Ŀ�ĵء�
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "getCurrentBufferNum sendData() send error:" + e.getMessage());
		}
		/* ============================����============================== */
		byte[] res = new byte[56];
		//��һ��������������DatagramPacket���󣬸ö���������ǽ���DatagramSocket�е�����
		mPacket = new DatagramPacket(res, 56);
		try
		{
			//receive()��һֱ�ȴ����÷������������ø÷������̣߳���ֱ���յ�һ�����ݱ�Ϊֹ
			mSocket.receive(mPacket);
		} catch (InterruptedIOException e) { 
	 
			Log.e(TAG, "===============================Timed out!=======================================");           
        } catch (IOException e)
		{
        	 System.out.println("receive error:"+e.getMessage());  
		}  
		StringBuffer bufferSizeStringBuffer = new StringBuffer();
		//Log.e(TAG, "============================��ʼ16�����ַ���ת����============================");
		if (channelType == ChannelType.LEFT)
		{
			// ������:�ֽڴӵ�λ����λ�����±�42-45
			for (int i = 45; i > 41; i--)
			{
				bufferSizeStringBuffer.append(String.format("%02X", res[i]));
			}
			// 16�����ַ���ת10��������
			bufferSize = Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
			System.out.println(" ������bufferSize " + bufferSizeStringBuffer + "=" + bufferSize);			
			return bufferSize;
		} else
		{
			//������:�ֽڴӵ�λ����λ�����±�46-49
			for (int i = 49; i > 45; i--)
			{
				bufferSizeStringBuffer.append(String.format("%02X", res[i]));
			}
			bufferSize = Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
			System.out.println(" ������bufferSize " + bufferSizeStringBuffer + "=" + bufferSize);
			return Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
		}
	}
	
	/**
	 * ��������������ǰ���ݴ�С
	 * 
	 * @param ip
	 * @param port
	 * @return ��������ǰ���ݴ�С
	 */
	public int getCurrentBufferNum(String ip,int port)
	{
		int bufferSize;
		/* =================================����================================= */
		// ƴ�ӷ���Э��
		byte[] queryBufferStateCMD = new byte[39];
		System.arraycopy(head, 0, queryBufferStateCMD, 0, 6);
		System.arraycopy(cmdLength, 0, queryBufferStateCMD, 6, 2);
		System.arraycopy(checkSum, 0, queryBufferStateCMD, 8, 2);
		System.arraycopy(protocalVersion, 0, queryBufferStateCMD, 10, 1);
		System.arraycopy(new byte[] { 0x33 }, 0, queryBufferStateCMD, 11, 1);
		System.arraycopy(cmdType, 0, queryBufferStateCMD, 12, 1);
		System.arraycopy(localIP, 0, queryBufferStateCMD, 13, 4);
		System.arraycopy(localPort, 0, queryBufferStateCMD, 17, 2);
		System.arraycopy(destIP, 0, queryBufferStateCMD, 19, 4);
		System.arraycopy(destPort, 0, queryBufferStateCMD, 23, 2);
		System.arraycopy(localId, 0, queryBufferStateCMD, 25, 4);
		System.arraycopy(sessionId, 0, queryBufferStateCMD, 29, 4);
		System.arraycopy(other, 0, queryBufferStateCMD, 33, 4);
		System.arraycopy(end, 0, queryBufferStateCMD, 37, 2);
		try{
	    mPacket = new DatagramPacket(queryBufferStateCMD, queryBufferStateCMD.length, InetAddress.getByName(ip), 9200);
	    // ����
		//ʹ��DatagramSocket�������ݱ�ʱ��DatagramSocket����֪���������ݱ����͵����������DatagramPacket����������ݱ���Ŀ�ĵء�
		//������ͷ����֪��ÿ����װ���Ŀ�ĵأ���ͷֻ�ǽ���Щ��װ�䷢�ͳ�ȥ������װ�䱾������˸ü�װ���Ŀ�ĵء�
		mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "getCurrentBufferNum sendData() send error:" + e.getMessage());
		}
		/* ============================����============================== */
		byte[] res = new byte[56];
		//��һ��������������DatagramPacket���󣬸ö���������ǽ���DatagramSocket�е�����
		mPacket = new DatagramPacket(res, 56);
		try
		{
			//receive()��һֱ�ȴ����÷������������ø÷������̣߳���ֱ���յ�һ�����ݱ�Ϊֹ
			mSocket.receive(mPacket);
		} catch (InterruptedIOException e) { 
	 
			Log.e(TAG, "===============================Timed out!=======================================");           
        } catch (IOException e)
		{
        	 System.out.println("receive error:"+e.getMessage());  
		}  
		StringBuffer bufferSizeStringBuffer = new StringBuffer();
		//Log.e(TAG, "============================��ʼ16�����ַ���ת����============================");
		if (port == 8900/*ChannelType.LEFT*/)
		{
			// ������:�ֽڴӵ�λ����λ�����±�42-45
			for (int i = 45; i > 41; i--)
			{
				bufferSizeStringBuffer.append(String.format("%02X", res[i]));
			}
			// 16�����ַ���ת10��������
			bufferSize = Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
			System.out.println(" ������bufferSize " + bufferSizeStringBuffer + "=" + bufferSize);			
			return bufferSize;
		} else
		{
			//������:�ֽڴӵ�λ����λ�����±�46-49
			for (int i = 49; i > 45; i--)
			{
				bufferSizeStringBuffer.append(String.format("%02X", res[i]));
			}
			bufferSize = Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
			System.out.println(" ������bufferSize " + bufferSizeStringBuffer + "=" + bufferSize);
			return Integer.parseInt(bufferSizeStringBuffer.toString(), 16);
		}
	}
	
    private final int CMD_INDEX=11;
	
	/** 
	 * 
	 * ���ҷ�����IP
	 *  
	 */
	public String getServerIP(){
		Log.d(TAG, "send 0xAA cmd"); 
		//������IP
		StringBuffer serverIP=new StringBuffer();
		/*===================����=====================*/
		byte []reqByte=new byte[49];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte)0xAA }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);			
		System.arraycopy(year, 0, reqByte, 33, 2);
		System.arraycopy(month, 0, reqByte, 35, 1);
		System.arraycopy(day, 0, reqByte, 36, 1);		
		System.arraycopy(hour, 0, reqByte, 37, 1);
		System.arraycopy(minute, 0, reqByte, 38, 1);
		System.arraycopy(second, 0, reqByte, 39, 1);
		System.arraycopy(mSecond, 0, reqByte, 40, 2);		
		System.arraycopy(deviceType, 0, reqByte, 42, 1);
		System.arraycopy(other, 0, reqByte, 43, 4);
		System.arraycopy(end, 0, reqByte, 47, 2);			
		try
		{
			mPacket=new DatagramPacket(reqByte, 49, InetAddress.getByName("255.255.255.255"),9200);
			mSocket.send(mPacket);
		}		
		catch (InterruptedIOException e) { 
			// ��receive������Ϣ����receiveʱ�䳬��3��ʱ������������ط�����       
			Log.e(TAG, "===============================getServerIP Timed out!======================================="); 
			//return;
        }catch (Exception e) {
			Log.e(TAG,"getServerIP() receive error:"+e.getMessage());  
		}  
		
		/*===================����=====================*/
		byte[] res = new byte[285];
		mPacket=new DatagramPacket(res, 285);
		try
		{
			mSocket.receive(mPacket);
		} catch (IOException e)
		{
			Log.e(TAG, "���ҷ�����IP :"+e.getMessage());//��ȡ�豸Э�� ���ճ���
		}	
		System.out.println("res[CMD_INDEX]="+res[CMD_INDEX]);
		//���ҷ�����Э����� 0xAA=-86
		if (res[CMD_INDEX]==-86)
		{
			System.out.println("=============���ҷ�����IP=============");
			/*for (int i = 0; i < 285; i++)
			{
				System.out.println("[" + i + "]=" + String.format("%02X ", res[i]));
			}*/
			//ip��ַ��43-46�ֽ�
			//�ֽ�ת��16�����ַ�������ת10����
			serverIP.append(Integer.parseInt(String.format("%02X", res[43]),16)).append(".").append(Integer.parseInt(String.format("%02X", res[44]),16)).append(".").append(Integer.parseInt(String.format("%02X", res[45]),16)).append(".").append(Integer.parseInt(String.format("%02X", res[46]),16));				
		} else
		{
			Log.e(TAG, "��ѯ������IPʧ��");
		}	
		System.out.println("������IP:"+serverIP);
		return serverIP.toString();
	}
	
	/**
	 * ����
	 */
	private final int ON=1;
	/**
	 * ����
	 */
	private final int OFF=0;
	
	public static List<Recorder> recorders;
	
	/**
	 * ��ȡ�豸�б�(Player,Recorder)
	 */
	public List<Speaker> getDevices(String ip){
		Log.d(TAG, "send 0x40 getDevices cmd"); 
		List<Speaker> speakers=new ArrayList<Speaker>();
	    recorders=new ArrayList<Recorder>();
		// ����
		byte []reqByte=new byte[35/*47*/];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte)0x40 }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);	//33
		
		/*System.arraycopy(other, 0, reqByte, 33, 4);
		System.arraycopy(other, 0, reqByte, 37, 4);
		System.arraycopy(other, 0, reqByte, 41, 4);	*/	
		System.arraycopy(end, 0, reqByte, 33/*45*/, 2);			
		try
		{			
			mPacket = new DatagramPacket(reqByte, 35, InetAddress.getByName(ip), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, " ��ȡ�豸�б�Э�� error:" + e.getMessage());
		}
		// ����
		byte[] res=new byte[215];
		
		mPacket = new DatagramPacket(res, 215);
		try
		{
			mSocket.receive(mPacket);
		} catch (IOException e)
		{
			Log.e(TAG, "��ȡ�豸Э�� ���ճ��� :" + e.getMessage());
		}
		System.out.println("=====================��ȡ�豸�б�cmd ���=====================");
		//�豸����res[33]
    	int countSpeaker=0;
		//��ȡ'P'(50):�����豸ip,״̬
		//index=34-37...  n���豸
		for(int i=34;i<=34+(res[33]-1)*9;i+=9){
			System.out.println("[" + i + "]=" + String.format("%02X ", res[i]));
			//======================�����豸'P'=0x50============================
			if(String.format("%02X", res[i]).equals("50")){
			Speaker speaker=new Speaker();
			countSpeaker++;
			    //ip
				speaker.setIp(
				Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
				Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
				Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
				Integer.parseInt(String.format("%02X", res[i+4]),16)				
				);
				
				//״̬
				System.out.println("�����豸"+
						Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+4]),16)				
						+" �豸״̬="+res[i+5]);
				speaker.setStatus(res[i+5]);	
				speaker.setId(countSpeaker);			
				speakers.add(speaker);
			}else
			//============================¼���豸'R'=0x52	============================		
			if(String.format("%02X", res[i]).equals("52")){				
				Recorder recorder=new Recorder();				
				    //ip
			    	recorder.setIp(
					Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+4]),16)				
					);
					
					//״̬
					System.out.println("¼���豸"+
							Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+4]),16)				
							+" �豸״̬="+res[i+5]);
					recorder.setStatus(res[i+5]);						
					recorders.add(recorder);					
			}else 	
			//================================�豸A==============================================
				if(String.format("%02X", res[i]).equals("10")){
					//״̬
					System.out.println("A�豸"+
							Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
							Integer.parseInt(String.format("%02X", res[i+4]),16)				
							+" �豸״̬="+res[i+5]);
			}else if(String.format("%02X", res[i]).equals("93")){
				//״̬
				System.out.println("M�豸"+
						Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+3]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+4]),16)				
						+" �豸״̬="+res[i+5]);
			}			
		}
		Log.d(TAG, "recorder size="+recorders.size());
		return speakers;
	}
	
	
	/**true:����������false:���ӶϿ�*/
	public static boolean isAlive;
	private DatagramPacket resPacket;

	public void androidToServerKeepAlive(String ipStr)
	{
		Log.w(TAG, "Android--->Server cmd=0x99");
		byte[] reqByte = new byte[44];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { KEEP_ALIVE }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4); // 33

		System.arraycopy(year, 0, reqByte, 33, 2);
		System.arraycopy(month, 0, reqByte, 35, 1);
		System.arraycopy(day, 0, reqByte, 36, 1);
		System.arraycopy(hour, 0, reqByte, 37, 1);
		System.arraycopy(minute, 0, reqByte, 38, 1);
		System.arraycopy(second, 0, reqByte, 39, 1);
		System.arraycopy(mSecond, 0, reqByte, 40, 2);

		System.arraycopy(end, 0, reqByte, 42, 2);
		try
		{
			mPacket = new DatagramPacket(reqByte, reqByte.length, InetAddress.getByName(ipStr), 9200);
		} catch (UnknownHostException e1)
		{
			Log.e(TAG, "keep alive cmd error:" + e1.getMessage());
		}
		try
		{
			mSocket.send(mPacket);
		} catch (IOException e1)
		{
			Log.e(TAG, "keep alive cmd send:" + e1.getMessage());
		}
		// ����
		byte[] res = new byte[50];

		resPacket = new DatagramPacket(res, res.length);
		try
		{
			mSocket.receive(resPacket);
			if (String.format("%02X", resPacket.getData()[11]).equals("99"))
			{
				System.out.println("alive");
			} else
			{
				System.out.println("not alive");
			}
		} catch (IOException e)
		{
			Log.e(TAG, "keep alive cmd receive:" + e.getMessage());
		}
	}
	// ������,����5��û���յ���Ӧ������Ϊ�ѶϿ�
	public /*boolean*/void isAlive(String ipStr)
	{
		Log.w(TAG, "Android--->Server cmd=0x99");
		int count = 0;
		byte[] reqByte = new byte[44];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { KEEP_ALIVE }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4); // 33

		System.arraycopy(year, 0, reqByte, 33, 2);
		System.arraycopy(month, 0, reqByte, 35, 1);
		System.arraycopy(day, 0, reqByte, 36, 1);
		System.arraycopy(hour, 0, reqByte, 37, 1);
		System.arraycopy(minute, 0, reqByte, 38, 1);
		System.arraycopy(second, 0, reqByte, 39, 1);
		System.arraycopy(mSecond, 0, reqByte, 40, 2);

		System.arraycopy(end, 0, reqByte, 42, 2);
		try
		{
			mPacket = new DatagramPacket(reqByte, reqByte.length, InetAddress.getByName(ipStr), 9200);
		} catch (UnknownHostException e1)
		{
			Log.e(TAG, "keep alive cmd error:" + e1.getMessage());
		}

		while (true && GlobalValue.isAppOpen)
		{
			try
			{
				mSocket.send(mPacket);
			} catch (IOException e1)
			{			
				Log.e(TAG, "keep alive cmd send:" + e1.getMessage());
			}
			// ����
			byte[] res = new byte[50];

			resPacket = new DatagramPacket(res, res.length);
			if (count == 5)
			{
				System.out.println("not alive");
				isAlive = false;
				count=0;
			}
			try
			{
				mSocket.receive(resPacket);
				if (String.format("%02X", resPacket.getData()[11]).equals("99"))
				{
					System.out.println("alive");
					isAlive = true;
				} else
				{
					System.out.println("not alive");
					count++;
				}
			} catch (IOException e)
			{
				count++;
				Log.e(TAG, "keep alive cmd receive:" + e.getMessage());
			}
			Log.d(TAG, "sleep 1000ms");
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				Log.e(TAG, "keep alive cmd error:" + e.getMessage());
			}
		}
	}


	/**����¼���豸�Ĳ�������
	 * @param playerId  id[0,1,2...] ���õĲ����豸��Ϣ��ip,id,��Դ����
	 * @param soundSrc ��Դ'R' ,'L'
	 */
	public void setDevicesStatus(int []playerId,char soundSrc,String deviceIp)
	{
		//TODO:���ǿ��� return 	
		
		/*=========================0x41��ѯ״̬==========================*/
		byte[] res=getDevicesStatusBytes("192.168.1.99");			 
		if (deviceIp == null)
		{
			Log.e(TAG, "deviceIp==null");
			return;
		}
		//0x42����״̬
		//����IP��ַת4��byte/
		String literals[] = deviceIp.split("\\.", 4);
		byte ip[] = ipStr2Byte(deviceIp)/*new byte[4]*/, c = 0;
		/*for (String d : literals)
		{
			ip[c++] = (byte) Short.parseShort(d);		
		}
		
		System.out.println("ת�����ֽ�:" + Arrays.toString(ip));	*/
		
		for(int i=0;i<playerId.length;i++){
			//��playerId��������Ϣ��λ��			
			int startIndex=38+(playerId[i]/2)*14;//[38,41]¼��ip		
			//������
			if (playerId[i] == 0 || playerId[i] == 2 || playerId[i] == 4 || playerId[i] == 6)
			{
				/**�豸������ ¼��ipλ��*/	
				res[startIndex]=ip[0];
				res[startIndex+1]=ip[1];
				res[startIndex+2]=ip[2];
				res[startIndex+3]=ip[3];		
				/**�豸������  ������Դλ��*/
				res[startIndex+4]=(byte)soundSrc;
			}
			//������
			if (playerId[i] == 1 || playerId[i] == 3 || playerId[i] == 5 || playerId[i] == 7)
			{
				/**�豸������ ¼��ipλ��*/
				res[startIndex+5]=ip[0];
				res[startIndex+6]=ip[1];
				res[startIndex+7]=ip[2];
				res[startIndex+8]=ip[3];
				/**�豸������  ������Դλ��*/
				res[startIndex+9]=(byte)soundSrc;
			}		
		}	
		
		//SETTING_DEVICE_INFO	
		try
		{
			res[11]=0x42;
			// ����0x42	
			mPacket = new DatagramPacket(res, res.length, InetAddress.getByName("192.168.1.99"), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "setDevicesStatus error:"+e.getMessage());
		}		
	}
	/**
	 * ip�ַ���ת��4���ֽ�
	 * @param ipStr ip�ַ���
	 * @return
	 */
	public byte[] ipStr2Byte(String ipStr){
		String literals[] = (ipStr).split("\\.", 4);
		byte ip[] = new byte[4], c = 0;
		for (String d : literals)
		{
			ip[c++] = (byte) Short.parseShort(d);		
		}
		return ip;	
	}
	/**
	 * ���ָ�����ȵ��豸��Ϣ
	 * 
	 * @param playerId  id[0,1,3...]
	 * @param soundSrc ��Դ'R' ,'L'
	 */
	public void clearDevicesStatus(int []playerId)
	{
		//TODO:���ǿ��� return 	
		
		/*=========================0x41��ѯ״̬==========================*/
		byte[] res=getDevicesStatusBytes("192.168.1.99");			 
		if (GlobalValue.localIp == null)
		{
			Log.e(TAG, "GlobalValue.localIp==null");
			return;
		}
		//0x42����״̬
		//����IP��ַת4��byte/
		String literals[] = (GlobalValue.localIp).split("\\.", 4);
		byte ip[] = new byte[4], c = 0;
		for (String d : literals)
		{
			ip[c++] = (byte) Short.parseShort(d);		
		}
		System.out.println("ת�����ֽ�:" + Arrays.toString(ip));	
		
		for(int i=0;i<playerId.length;i++){
			//��playerId��������Ϣ��λ��			
			int startIndex=38+(playerId[i]/2)*14;//[38,41]¼��ip		
			//������
			if (playerId[i]%2==0 /*== 0 || playerId[i] == 2 || playerId[i] == 4 || playerId[i] == 6*/)
			{
				/**�豸������ ¼��ipλ��*/	
				res[startIndex]=0;
				res[startIndex+1]=0;
				res[startIndex+2]=0;
				res[startIndex+3]=0;		
				/**�豸������  ������Դλ��*/
				res[startIndex+4]=0;
			}else
			//������
			/*if (playerId[i] == 1 || playerId[i] == 3 || playerId[i] == 5 || playerId[i] == 7)*/
			{
				/**�豸������ ¼��ipλ��*/
				res[startIndex+5]=0;
				res[startIndex+6]=0;
				res[startIndex+7]=0;
				res[startIndex+8]=0;
				/**�豸������  ������Դλ��*/
				res[startIndex+9]=0;
			}		
		}	
		
		//SETTING_DEVICE_INFO	
		try
		{
			res[11]=0x42;
			// ����0x42	
			mPacket = new DatagramPacket(res, res.length, InetAddress.getByName("192.168.1.99"), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "setDevicesStatus error:"+e.getMessage());
		}		
	}
	
	/**
	 * ��ȡ�豸�б�״̬
	 * 
	 * ����:�� ������ԴIP��ַ 0.0.0.0
	 * ���ã��� ������ԴIP��ַ
	 * 
	 * TODO:��0x42ǰ����0x41
	 */
	public List<Speaker> getDevicesStatus(String ip/*,List<Speaker> speakers*/){	
		Log.d(TAG, "��ȡ�豸�б�״̬ send 0x41 cmd");
		List<Speaker> speakers=new ArrayList<Speaker>();
		// ����
		byte[] reqByte = new byte[35/* 47 */];
		System.arraycopy(getReqHead(0x41), 0, reqByte, 0, 33); ;
	   /*	
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte) DEVICES_STATUS }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);*/
		
		System.arraycopy(end, 0, reqByte, 33, 2);
		try
		{			
			mPacket = new DatagramPacket(reqByte, 35, InetAddress.getByName(ip), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "��ѯ�豸״̬���� error:" + e.getMessage());
		}
		
		// ����
		byte[] res = new byte[200];

		mPacket = new DatagramPacket(res, 200);
		try
		{
			mSocket.receive(mPacket);
		} catch (IOException e)
		{
			Log.e(TAG, "���ա���ѯ�豸״̬����  ���� :" + e.getMessage());
		}
		System.out.println("=====================��ѯ�豸״̬  �豸����["+res[33]+"]=====================");

	/*for (int i = 0; i < res.length; i++)
		{
			System.out.println(res[i]+" [" + i + "]=" + String.format("%02X ", res[i])+"ʮ����="+Integer.parseInt(String.format("%02X", res[i]),16));
		}*/
	 
		int count=0;
		for (int i = 34; i <= 34+(res[33]-1)*14/*&&res[i]!=0*/; i+=14)
		{
			/*===================����===================*/						
			if(count==5/*res[33]*/){break;}
			count++;
			/*======================================*/			
			
		/*	IP��ַ	����		4�ֽ�	������IP��ַ
			�����豸����������ԴIP��ַ	����		4�ֽ�	¼���豸IP��ַ
			�����豸����������Դ����	�ַ�		1�ֽ�	L':������, 'R':������
			�����豸����������ԴIP��ַ	����		4�ֽ�	¼���豸IP��ַ
			�����豸����������Դ����	�ַ�		1�ֽ�	L':������, 'R':������
 		*/
			Speaker speaker=new Speaker();			
			//�豸�����ȣ�ip
			speaker.setIp(Integer.parseInt(String.format("%02X", res[i]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
						Integer.parseInt(String.format("%02X", res[i+3]),16)				
						);
			
			Log.w(TAG, "�豸IP  "+Integer.parseInt(String.format("%02X", res[i]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+3]),16)				
					);			
		
			//��������ԴIP  38
			speaker.getLeftChannel().setFromIP(Integer.parseInt(String.format("%02X", res[i+4]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+5]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+6]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+7]),16));
			System.out.println("��������ԴIP  "+Integer.parseInt(String.format("%02X", res[i+4]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+5]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+6]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+7]),16));
			
			//��������Դ�������� 42
			speaker.getLeftChannel().setFromChanneal(res[i+8]);			
			System.out.println("������������Դ����   "+(char)res[i+8]);
			//����������ԴIP
			speaker.getRightChannel().setFromIP(Integer.parseInt(String.format("%02X", res[i+9]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+10]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+11]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+12]),16));
			System.out.println("��������ԴIP  "+Integer.parseInt(String.format("%02X", res[i+9]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+10]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+11]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+12]),16));
			
			//����������Դ����	
			speaker.getRightChannel().setFromChanneal(res[i+13]);
			System.out.println("����������Դ����   "+(char)res[i+13]);
			speakers.add(speaker);
		}
		return speakers;
	}

	/**
	 * ��ȡָ��ͷ
	 * @param cmdNo ָ���
	 * @return ָ��ͷ����
	 */
	public byte[] getReqHead(int cmdNo)
	{
		byte[] req = new byte[33];
		System.arraycopy(head, 0, req, 0, 6);
		//System.arraycopy(cmdLength, 0, req, 6, 2);
		//System.arraycopy(checkSum, 0, req, 8, 2);
		//System.arraycopy(protocalVersion, 0, req, 10, 1);
		System.arraycopy(new byte[] { (byte) cmdNo }, 0, req, 11, 1);
		//System.arraycopy(cmdType, 0, req, 12, 1);
		//System.arraycopy(localIP, 0, req, 13, 4);
		//System.arraycopy(localPort, 0, req, 17, 2);
		//System.arraycopy(destIP, 0, req, 19, 4);
		//System.arraycopy(destPort, 0, req, 23, 2);
		//System.arraycopy(localId, 0, req, 25, 4);
		//System.arraycopy(sessionId, 0, req, 29, 4);
		return req;
	}
	/**
	 * ��ѯ�豸�б�״̬
	 * <p>
	 * ����:�� ������ԴIP��ַ 0.0.0.0
	 * ���ã��� ������ԴIP��ַ
	 * 
	 * 
	 * TODO:��0x42ǰ����0x41
	 */
	public byte[]  getDevicesStatusBytes(String ip){	
		Log.d(TAG, "�豸�б��״̬ send 0x41 cmd");
		// ����
		byte[] reqByte = new byte[35/* 47 */];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte) DEVICES_STATUS }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);
		
		System.arraycopy(end, 0, reqByte, 33, 2);
		try
		{			
			mPacket = new DatagramPacket(reqByte, 35, InetAddress.getByName(ip), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "��ѯ�豸״̬���� error:" + e.getMessage());
		}
		
		// ����
		byte[] res = new byte[200];

		mPacket = new DatagramPacket(res, 200);
		try
		{
			mSocket.receive(mPacket);
		} catch (IOException e)
		{
			Log.e(TAG, "���ա���ѯ�豸״̬����  ���� :" + e.getMessage());
		}
		System.out.println("=====================��ѯ�豸״̬  �豸����["+res[33]+"]=====================");

	/*    for (int i = 0; i < res.length; i++)
		{
			System.out.println(res[i]+" [" + i + "]=" + String.format("%02X ", res[i])+"ʮ����="+Integer.parseInt(String.format("%02X", res[i]),16));
		}*/
	 
		int count=0;
		for (int i = 34; i <= 34+(res[33]-1)*14/*&&res[i]!=0*/; i+=14)
		{
			/*===================����===================*/						
			if(count==5/*res[33]*/){break;}
			count++;
			/*======================================*/				
			Log.w(TAG, "�豸IP  "+Integer.parseInt(String.format("%02X", res[i]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+1]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+2]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+3]),16)				
					);			
		
		
			System.out.println("��������ԴIP  "+Integer.parseInt(String.format("%02X", res[i+4]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+5]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+6]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+7]),16));
			
			//��������Դ�������� 42	
			System.out.println("������������Դ����   "+(char)res[i+8]);
			//����������ԴIP
			System.out.println("��������ԴIP  "+Integer.parseInt(String.format("%02X", res[i+9]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+10]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+11]),16)+"."+
					Integer.parseInt(String.format("%02X", res[i+12]),16));
			
			//����������Դ����	
			System.out.println("����������Դ����   "+(char)res[i+13]);
		}
		return res;
	}
	
	
	
	
	/**
	 * @param playIp
	 *            �����豸IP
	 * @param playChannel
	 *            �����豸����
	 */
	public void swapChannel(String playIp, int playChannel)
	{
		//ipת��Ϊ4�ֽڣ�"."Ϊ��ַ�
		byte[]reqByte=new byte[69];
		
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte)0x35 }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);	//33	
		//TODO:...	
	}
	
	 DatagramPacket packet;	

	 DatagramSocket socket;
	
	byte[] uuid=new byte[36];
	/**
	 * ע���豸
	 * isLogin=0:login ,isLogin=1:logout
	 * @return
	 * ����������ʱ��ע���ֻ�
	 */
	/**
	 * @param isLogin 0:login ,1:logout
	 * @param serverIp
	 * @return
	 */
	public boolean  isRegisterDeviceOK(byte isLogin,String serverIp){
		//ע���Ƿ�ɹ�
		boolean isSuccess;
		//����IP
		localIP=ipStr2Byte(GlobalValue.localIp);
		//����mac uuid
		String[] dst=PhoneInfoHelp.getMac().split("\\:", 6);
		//�����6�ֽ�
		int index=30;	   
		for (String str : dst)
		{
			// 16�����ַ���ת������
			int integer =12/* Integer.valueOf(str, 16)*/;//132 122 114
			uuid[index] = (byte) integer;
			index++;
		}	
		
		//req
		byte[]reqByte=new byte[76];			
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte)0x11 }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4);	//33
		
		System.arraycopy(uuid, 0, reqByte, 33, 36);
		System.arraycopy(new byte[]{isLogin}, 0, reqByte, 69, 1);
		System.arraycopy(other, 0, reqByte, 70, 4);
		System.arraycopy(end, 0, reqByte, 74, 2);
		
		try
		{			
			mPacket = new DatagramPacket(reqByte, reqByte.length, InetAddress.getByName(serverIp), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "ע���豸 send error:" + e.getMessage());
		}		
		// ����
		byte[] res = new byte[42];

		mPacket = new DatagramPacket(res, res.length);
		try
		{
			mSocket.receive(mPacket);

			for (int i = 0; i < res.length; i++)
			{
				System.out.println("byte=" + res[i] + " [" + i + "]=0x" + String.format("%02X ", res[i]) + "ʮ����=" + Integer.parseInt(String.format("%02X", res[i]), 16));
			}
		} catch (IOException e)
		{
			Log.e(TAG, "ע���豸  receive ���� :" + e.getMessage());
		}	
	
			
		//ע���Ƿ�ɹ�  1���ɹ���0��ʧ�� 
		//res[34]		
		if (res[34] == 1)
		{			
		
			Log.d(TAG, "ע��ɹ�");
			isSuccess=true;
			//����������Ľ�ɫ���豸���ͣ�
			//A�����ֻ�65��M���ֲ����ֻ�77
		    //res[35]
			if (res[35] == 'A')
			{
				Log.d(TAG, "A�����ֻ�");
			} 
			else if (res[35] == 'M')
			{
				Log.d(TAG, "M���ֲ����ֻ� ");
			}
			
			//�ɹ��󣬽��շ�����������
			try
			{
				//socket = new DatagramSocket();
				//��������ʱ�� 
				//socket.setSoTimeout(1500);
				packet=new DatagramPacket(new byte[100], 100,InetAddress.getByName("192.168.1.100")/* InetAddress.getByName(GlobalValue.localIp)*/, 9200);
		    } catch (Exception e)
			{			
				e.printStackTrace();
			}		
			
		} else
		{			
			Log.d(TAG, "ע��ʧ�� ");
			isSuccess=false;
		}		
		return isSuccess;
	}

	/**
	 * ����AllsIn ¼���豸
	 * 
	 * ���Ź���
	 * @param opCode 0:��ͣ, 1:�ָ�, 2:ֹͣ
	 * @param playIP �����豸IP
	 * @param channelType L:Left Channel, R:Right Channel, A:ALL Channel
	 */
	public void playManage(byte opCode, String playIP, char channelType,String serverIp)
	{
		Log.w(TAG, "send 0x36 cmd");
		//===========================����========================
		// TODO ipStr to ip byte
		byte ip[] = ipStr2Byte(playIP);
		// req
		byte[] reqByte = new byte[41];
		System.arraycopy(head, 0, reqByte, 0, 6);
		System.arraycopy(cmdLength, 0, reqByte, 6, 2);
		System.arraycopy(checkSum, 0, reqByte, 8, 2);
		System.arraycopy(protocalVersion, 0, reqByte, 10, 1);
		System.arraycopy(new byte[] { (byte) 0x36 }, 0, reqByte, 11, 1);
		System.arraycopy(cmdType, 0, reqByte, 12, 1);
		System.arraycopy(localIP, 0, reqByte, 13, 4);
		System.arraycopy(localPort, 0, reqByte, 17, 2);
		System.arraycopy(destIP, 0, reqByte, 19, 4);
		System.arraycopy(destPort, 0, reqByte, 23, 2);
		System.arraycopy(localId, 0, reqByte, 25, 4);
		System.arraycopy(sessionId, 0, reqByte, 29, 4); // 33
		// �����豸IP
		System.arraycopy(ip, 0, reqByte, 33, 4);
		// �����豸����
		System.arraycopy(new byte[] { (byte) channelType }, 0, reqByte, 37, 1);
		// ָ��
		System.arraycopy(new byte[] { opCode }, 0, reqByte, 38, 1);
		System.arraycopy(end, 0, reqByte, 39, 2);
		try
		{			
			mPacket = new DatagramPacket(reqByte, reqByte.length, InetAddress.getByName(serverIp), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "���ſ���Э�� send error:" + e.getMessage());
		}	
		//===================================����===========================
		Log.w(TAG, "receive 0x36 cmd");
		byte[] res=new byte[60];
		try
		{			
			mPacket = new DatagramPacket(res, res.length);
			mSocket.receive(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "���ſ���Э�� send error:" + e.getMessage());
		}	
		printRes(res);		
	}
	/**
	 * �����������
	 */
	public void printRes(byte[]res){
		for (int i = 0; i < res.length; i++)
		{
			System.out.println("byte=" + res[i] + " [" + i + "]=0x" + String.format("%02X ", res[i]) + "ʮ����=" + Integer.parseInt(String.format("%02X", res[i]), 16));
		}
	}

	/**
	 * �л�¼���豸������
	 * @param srcNo ��ǰ���ȵ�ip
	 * @param dstIp �л���������ip
	 */
	
	/**
	 * �л�¼���豸������
	 * @param deviceIp �豸Ip
	 * @param srcNo Դ�������
	 * @param dstNo Ŀ���������
	 * @param channelType ��������
	 */
	public void changeChanel(String deviceIp, int srcNo, int dstNo, char channelType)
	{
		// 0x41��ѯ״̬
		byte[] res = getDevicesStatusBytes("192.168.1.99");
		int startIndex = 38 + (srcNo / 2) * 14;
		/***********************step1  ԭ������Ϣ���***********************/
		if (srcNo % 2 == 0)
		{
			// �豸������ ¼��ip
			res[startIndex] = 0;
			res[startIndex + 1] = 0;
			res[startIndex + 2] = 0;
			res[startIndex + 3] = 0;
			// �豸������ ������Դ
			res[startIndex + 4] = 0;
		} else
		{
			// �豸������ ¼��ip
			res[startIndex + 5] = 0;
			res[startIndex + 6] = 0;
			res[startIndex + 7] = 0;
			res[startIndex + 8] = 0;
			// �豸������ ������Դ
			res[startIndex + 9] = 0;
		}
		/***********************step2  ������������Ϣ***********************/
		byte ip[] = ipStr2Byte(deviceIp);
		int startIndexDst = 38 + (dstNo / 2) * 14;
		if (dstNo % 2 == 0)
		{
			/** �豸������ ¼��ipλ�� */
			res[startIndexDst] = ip[0];
			res[startIndexDst + 1] = ip[1];
			res[startIndexDst + 2] = ip[2];
			res[startIndexDst + 3] = ip[3];
			/** �豸������ ������Դλ�� */
			res[startIndexDst + 4] = (byte) channelType;
		} else
		{
			/** �豸������ ¼��ipλ�� */
			res[startIndexDst + 5] = ip[0];
			res[startIndexDst + 6] = ip[1];
			res[startIndexDst + 7] = ip[2];
			res[startIndexDst + 8] = ip[3];
			/** �豸������ ������Դλ�� */
			res[startIndexDst + 9] = (byte) channelType;
		}
		
		// SETTING_DEVICE_INFO
		try
		{
			res[11] = 0x42;
			// ����0x42
			mPacket = new DatagramPacket(res, res.length, InetAddress.getByName("192.168.1.99"), 9200);
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "changeChanel error:" + e.getMessage());
		}
	}
	
	public void 
}

/*
"%02X"˵����
X ��ʾ��ʮ��������ʽ���
02 ��ʾ������λ��ǰ�油0�����������λ����Ӱ��
������
printf("%02X", 0x123);  //��ӡ����123
printf("%02X", 0x1); //��ӡ����01	
*/
