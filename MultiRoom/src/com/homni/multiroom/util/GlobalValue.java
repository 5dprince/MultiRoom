package com.homni.multiroom.util;

import java.util.ArrayList;
import java.util.List;

import com.homni.multiroom.model.Device;
import com.homni.multiroom.model.Music;
import com.homni.multiroom.model.Speaker;

/**
 * ȫ�ֱ���
 * 
 * @author Dawin
 * 
 */
public class GlobalValue
{
	public static String localIp;
	public static boolean isAppOpen=false;
	//��¼��ǰ�豸���õ�����
	public static List<Device> nowDevicePlayer=new ArrayList<Device>();
	
	/**��¼��ǰѡ�������*/
	public static List<Integer> currentPickPlay=new ArrayList<Integer>();
	//8�����ȶ�Ӧ���ŵ�ip
	public static String ip1;
	public static String ip2;
	public static String ip3;
	public static String ip4;
	public static String ip5;
	public static String ip6;
	public static String ip7;
	public static String ip8;
	//8�����ȶ�Ӧ���ŵ�port
	public static String port1;
	public static String port2;
	public static String port3;
	public static String port4;
	public static String port5;
	public static String port6;
	public static String port7;
	public static String port8;
	
	/** ��ǰѡ�������Id  Ĭ�ϵ�һ�� */
	public static int currentSpeakerId;
	/** ��ǰѡ��ĸ���Id */
	public static int currentMusicId;
	public static List<Speaker> speakers;
	//�洢�洢�����и�����Ϣ
	public static ArrayList<Music> musics;
	/**��¼���ڲ��ŵ�����,�������ͬʱ����*/
	//public static List<Speaker>  speakerPlay=new ArrayList<Speaker>();	
}
