package com.homni.multiroom.util;
/**
 * ������
 * @author admin
 *
 */
public class BufferUtil
{

	/**
	 * ������������
	 * 
	 * @param audio
	 *            ��Ƶ,PCM����
	 * @return byte[LEFT][] byte[RIGHT][]
	 */
	public static byte[][] getLeftChannel(byte[] audio)
	{
		// ����2�У�audio.length / 2��������
		byte[][] leftRight = new byte[2][audio.length / 2];
		int count = 0;
		int leftIndex = 0;
		int rightIndex = 0;
		// ��������:ǰ���ֽ��������������ֽ��������������ظ���
		for (int i = 0; i < audio.length; i++)
		{
			count++;
			// ������
			if (count <= 2)
			{
				leftRight[0][leftIndex] = audio[i];
				leftIndex++;
			} else
			{
				// ������
				leftRight[1][rightIndex] = audio[i];
				rightIndex++;

				if (count == 4)
				{
					count = 0;
				}
			}
		}
		return leftRight;
	}
	/**
	 * �������ݲ�ֳɼ���
	 * 
	 * @param totalLength
	 *            �����ܳ�
	 * @param toLength
	 *            ��ֵĳ���
	 * @return
	 */
	public static int countPacketSize(int totalLength, int toLength)
	{
		int capacity = 0;
		// �պ�length�ı���
		if (totalLength % toLength == 0)
		{
			capacity = totalLength / toLength;
		} else
		{
			// �ж�
			if (totalLength > toLength)
			{
				capacity = totalLength / toLength + 1;
			} else
			{
				// С��length
				capacity = 1;
			}
		}
		return capacity;
	}
}
