package com.homni.multiroom.model;

/**
 * ����model
 * 
 * @author Dawin
 *
 */
public class Decode
{

	// ����������
	private byte[] data;
	// �������
	private int decodeCount;

	public Decode(byte[] data, int decodeCount)
	{
		this.data = data;
		this.decodeCount = decodeCount;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	public int getDecodeCount()
	{
		return decodeCount;
	}

	public void setDecodeCount(int decodeCount)
	{
		this.decodeCount = decodeCount;
	}
}
