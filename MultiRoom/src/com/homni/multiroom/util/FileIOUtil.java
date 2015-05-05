package com.homni.multiroom.util;

import java.io.FileInputStream;
import java.io.IOException;

import android.util.Log;

public class FileIOUtil
{
	private static final String TAG = "FileIOUtil";

	// ��ȡ�ļ�
	public static byte[] read(String fileName) throws IOException
	{
		byte[] buffer = null;
		int length = 0;
		try
		{
			FileInputStream fin = new FileInputStream(fileName);
			length = fin.available();
			buffer = new byte[length];
			fin.read(buffer);
			fin.close();
		} catch (Exception e)
		{
			Log.e(TAG, "��ȡ�ļ�����" + e.getMessage());
		}
		return buffer;
	}

}
