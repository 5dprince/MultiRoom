package com.homni.multiroom.model;

import java.io.Serializable;

/**
 * ����
 * 
 * @author admin
 *
 */
public class Channel implements Serializable
{

	private final int R = 82;
	private final int L = 76;

	private final int ON = 1;
	private final int OFF = 0;
	private int id;
	private int port;
	private int sample;
	private int bit;
	private int state;
	/**�Ƿ�ѡ�������ж��Ƿ���Ա��л�*/
	private boolean isSelect;
	/**�����ж��Ƿ���Ա��л�*/
	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	// ��Դ������ַ
	private String fromIP;
	// ��Դ��������:�û�����
	private String fromChannelType;

	public String getFromIP()
	{
		return fromIP;
	}

	public String getFromChannelType()
	{
		return fromChannelType;
	}

	public void setFromChannelType(String fromChannelType)
	{
		this.fromChannelType = fromChannelType;
	}

	public void setFromIP(String fromIP)
	{
		this.fromIP = fromIP;
	}

	public int getFromChanneal()
	{
		return fromChanneal;
	}

	public void setFromChanneal(int fromChanneal)
	{
		this.fromChanneal = fromChanneal;
	}

	// ��Դ����
	int fromChanneal;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSample()
	{
		return sample;
	}

	public void setSample(int sample)
	{
		this.sample = sample;
	}

	public int getBit()
	{
		return bit;
	}

	public void setBit(int bit)
	{
		this.bit = bit;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

}
