package com.homni.multiroom.command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.homni.multiroom.command.ControlCommand.ChannelType;
import com.homni.multiroom.model.Speaker;
import com.homni.multiroom.util.AudioDecoder;
import com.homni.multiroom.util.BufferUtil;
import com.homni.multiroom.util.FileIOUtil;
import com.homni.multiroom.util.GlobalValue;


/**
 * ��ƵЭ��
 * @author Dawin
 * 
 *1������ѯ��������С�߳�
 *2����:�߽�߷�
 */
public class AudioCommand extends SocketHelper  
{
	private static final String TAG = "AudioCommand";
	//��ʼ״̬
	public static final int IDLE=-1;
	public static final int PLAY=1;
	public static final int PAUSE=2;
	public static final int STOP=3;
	//����״̬
	private int state=IDLE;
	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public static boolean isSend;
	private AudioDecoder mAudioDecode;
	private ControlCommand controlCommand;
	private final int LEFT = 0;
	/** ��������С */
	public static int bufferSize;
	/* ==================================Music Command================================== */
	/** Э��ͷ�� */
	byte[] head = new byte[] { 'X', 'X', 'X', 'C', 'M', 'D' };
	/** У��� */
	byte[] checkSum = new byte[2];
	/** Э��汾 */
	byte[] protocalVersion = new byte[1];
	/** ���� */
	byte[] cmd = new byte[1];
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
	/** ������ */
	byte[] packetNo = new byte[4];
	/** ������ */
	byte[] sample = new byte[1];
	/** �������� */
	byte[] bitDepth = new byte[1];
	/** ʱ��� */
	byte[] timeStamp = new byte[4];
	/** ͨ������ */
	byte[] data = new byte[1200];
	/** ������ */
	byte[] end = new byte[] { (byte) 0xff, (byte) 0xff };

	public AudioCommand()
	{ 
		//super();
		if (mSocketHelper == null)
		{
			mSocketHelper = new SocketHelper();
		}
		controlCommand = new ControlCommand();
		mAudioDecode = new AudioDecoder();
	}

	/**
	 * @param path
	 * @param speaker
	 * @param channelType
	 * @param channelID ����ID,
	 */
	public void play(String path, Speaker speaker, ChannelType channelType,int channelID)
	{		
		isSend = true;
		state=PLAY;
		String fileFormat = path.substring(path.lastIndexOf(".") + 1).toUpperCase();
		//����MP3,WMA
		if ("MP3".equals(fileFormat) || "WMA".equals(fileFormat))
		{
			playMP3WMA(path, speaker, channelType,channelID);
		} 
		//����WAV
		else if ("WAV".equals(fileFormat))
		{
			playWAV(path, speaker, channelType);
		}
		state=IDLE;
	}

	public void pause()
	{
		state=PAUSE;
	}
	public void stop()
	{
		isSend = false;
		state=STOP;
	}
	public void decode()
	{		
	}
	
	public void playWAV(String path, Speaker speaker, ChannelType channelType)
	{
		byte[] fileData = null;
		try
		{
			fileData = FileIOUtil.read(path);
		} catch (IOException e)
		{
			Log.e(TAG, "��ȡ�ļ�ʧ�ܣ� " + e.getMessage());
			return;
		}
		int capacity = BufferUtil.countPacketSize(fileData.length, 1200);
		for (int i = 0; i < capacity && isSend; i++)
		{
			if (i * 1200 + 1200 >= fileData.length)
			{
				return;
			}
			data = Arrays.copyOfRange(fileData, i * 1200, i * 1200 + 1200);
			byte[] datas = toMusicCommand(data);
			// �����udp,������
			try
			{
				sendAudio(datas, 8900, speaker.getIp());
			} catch (Exception e1)
			{
				Log.e(TAG, "������Ƶ����:" + e1.getMessage());
			}

			// ��9ms��һ��udp��
			try
			{
				Thread.sleep(9);
			} catch (InterruptedException e)
			{
				Log.e(TAG, "�ӳٳ��� error:" + e.getMessage());
			}
		}

	}

	/**
	 * ʱ���̣߳�������������С
	 * 
	 * @author Dawin
	 *
	 */
	class BufferThread implements Runnable
	{
	    //	Speaker speaker;
		ChannelType channelType;
		int channelID;
		public BufferThread(/*Speaker speaker,*/ ChannelType channelType,int channelID)
		{
			this.channelID=channelID;
		  //	this.speaker = speaker;
			this.channelType = channelType;
		}

		@Override
		public void run()
		{
			while (isSend)
			{
				bufferSize = controlCommand.getCurrentBufferNum(/*GlobalValue.speakerSelect1,*/channelType,channelID);
			
				try
				{
					Thread.sleep(500);
				} catch (InterruptedException e)
				{
					Log.e(TAG, "������ʱ���߳�sleepʧ��" + e.getMessage());
				}
			}
		}
	}

	/**
	 * ����MP3��WMA
	 * @param path
	 * @param speaker
	 * @param channelType �������ͣ���������������
	 */
	public void playMP3WMA(String path, Speaker speaker, ChannelType channelType,int channelID)
	{
		new Thread(new BufferThread(/*speaker, */channelType,channelID)).start();
		
		mAudioDecode.decode(path,speaker,channelType,channelID);
		//��������
		isSend = false;
		state=IDLE;	
	}

	/**
	 * ��ͣ 
	 * A pause mechanism that would block current thread when pause flag is set
	 */
	public synchronized void waitPlay()
	{
		while (state == PAUSE)
		{			
			try
			{	
				Log.e(TAG, " waitPlay() ...");	
				wait();
			} catch (InterruptedException e)
			{
				Log.e(TAG, " waitPlay() error:"+e.getMessage());		
			}
		}
	}
	public synchronized void wakeUp(){
		notifyAll();
	}
    
	/**
	 * ������Ƶ
	 * 
	 */
	private void sendAudio(byte[] audioData, int port, InetAddress ip)
	{
		try
		{
			// �����͵�byte������
			mPacket = new DatagramPacket(audioData, audioData.length, ip, port);
			// ����
			mSocket.send(mPacket);
		} catch (Exception e)
		{
			Log.e(TAG, "sendData() error:" + e.getMessage());
		}
	}

	/**
	 * �ϲ�Byte[]���飬��װ����ƵЭ��
	 * 
	 * @param datas
	 *            ����͵���ƵЭ��
	 * @param audioData
	 *            �ְ�����Ƶ����
	 * @return
	 */
	public byte[] toMusicCommand(byte[] audioData)
	{
		byte[] datas = new byte[1252];
		// ===================��װ����ƵЭ��=================
		System.arraycopy(head, 0, datas, 0, 6);
		//cmdLength 2
		System.arraycopy(checkSum, 0, datas, 6, 2);
		System.arraycopy(protocalVersion, 0, datas, 8, 1);
		//TODO:Э��ID 0x40
		System.arraycopy(cmd, 0, datas, 9, 1);
		System.arraycopy(localIP, 0, datas, 10, 4);
		System.arraycopy(localPort, 0, datas, 14, 2);
		System.arraycopy(destIP, 0, datas, 16, 4);
		System.arraycopy(destPort, 0, datas, 20, 2);
		System.arraycopy(localId, 0, datas, 22, 4);
		System.arraycopy(sessionId, 0, datas, 26, 4);
		System.arraycopy(packetNo, 0, datas, 30, 4);
		System.arraycopy(sample, 0, datas, 34, 1);
		System.arraycopy(bitDepth, 0, datas, 35, 1);
		System.arraycopy(timeStamp, 0, datas, 36, 4);
		System.arraycopy(audioData, 0, datas, 40, 1200);// �±�40��ʼ
		System.arraycopy(end, 0, datas, 1240, 2);
		return datas;
	}
/*=================================================����============================================================*/	
		private MediaCodec mMediaCodec;
		/** ������ȡ��Ƶ�ļ� */
		private MediaExtractor extractor;
		private MediaFormat format;
		private String mime = null;
		private int sampleRate = 0, channels = 0, bitrate = 0;
		private long presentationTimeUs = 0, duration = 0;
		//private final long timeoutUs = 1000;   
		private final int lEFT=0;
		private final int RIGHT=1;
		/**���뻺���� */
		byte[] decodeBuffer=new byte[0];


		/**
		 * ������Ƶ������
		 * @param path  �ļ�·��
		 * @param speaker ����
		 * @param channelType ��������
		 * @param channelID ����ID
		 */
		public void decode(String path,Speaker speaker,ChannelType channelType,int channelID)
		{		
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);		
			extractor = new MediaExtractor();
			// ����·����ȡԴ�ļ�
			try
			{
				extractor.setDataSource(path);
			} catch (Exception e)
			{
				Log.e("MainActivity", "[MediaCodec run() �����ļ�·������]" + e.getMessage());
			}
			try
			{
				// ��Ƶ�ļ���Ϣ
				format = extractor.getTrackFormat(0);
				mime = format.getString(MediaFormat.KEY_MIME);
				sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
				// ������������������˫����
				channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
				// if duration is 0, we are probably playing a live stream
				duration = format.getLong(MediaFormat.KEY_DURATION);
				//System.out.println("������ʱ����:"+duration/1000000);
				bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
			} catch (Exception e)
			{
				Log.e(TAG, "��Ƶ�ļ���Ϣ��ȡ����" + e.getMessage());
				// ��Ҫ�˳�����������ж�
			}
			Log.d(TAG, "Track info: mime:" + mime + " ������sampleRate:" + sampleRate + " channels:" + channels + " bitrate:" + bitrate + " duration:" + duration);
			// ����Ƿ�Ϊ��Ƶ�ļ�
			if (format == null || !mime.startsWith("audio/"))
			{
				Log.e(TAG, "������Ƶ�ļ���");
				return ;
			}

			// ʵ����һ��ָ�����͵Ľ�����,�ṩ�������
			// Instantiate an encoder supporting output data of the given mime type
			mMediaCodec = MediaCodec.createDecoderByType(mime);

			if (mMediaCodec == null)
			{
				Log.e(TAG, "����������ʧ�ܣ�");
				return ;
			}
			mMediaCodec.configure(format, null, null, 0);

			mMediaCodec.start();
			// �������Ŀ���ļ�������
			ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
			// ����������
			ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
			// ������������:AudioFormat.CHANNEL_OUT_MONO��������AudioFormat.CHANNEL_OUT_STEREO˫����
			int channelConfiguration = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
			Log.i(TAG, "channelConfiguration=" + channelConfiguration);	
			extractor.selectTrack(0);
			//�洢��Ƶ��������
			byte[][] leftRightChannel;
			//��������
			int channelTypeInt=channelType==ChannelType.LEFT?lEFT:RIGHT;
			//�����˿�
			int port=channelType==ChannelType.LEFT?8900:8901;
			/* ==================================================================��ʼ����==================================================================*/
			boolean sawInputEOS = false;
			boolean sawOutputEOS = false;
			final long kTimeOutUs = 10/*1000*/;
			MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
			while (!sawOutputEOS && AudioCommand.isSend)
			{
				try
				{
					if (!sawInputEOS)
					{
						int inputBufIndex = mMediaCodec.dequeueInputBuffer(kTimeOutUs);
						if (inputBufIndex >= 0)
						{
							ByteBuffer dstBuf = inputBuffers[inputBufIndex];
							
							int sampleSize = extractor.readSampleData(dstBuf, 0);
							if (sampleSize < 0)
							{
								Log.d(TAG, "saw input EOS. Stopping playback");
								sawInputEOS = true;
								sampleSize = 0;
							} else
							{
								presentationTimeUs = extractor.getSampleTime();
							}

							mMediaCodec.queueInputBuffer(inputBufIndex, 0, sampleSize, presentationTimeUs, sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);

							if (!sawInputEOS){
								extractor.advance();
							}							

						} else
						{
							Log.e(TAG, "inputBufIndex " + inputBufIndex);
						}
					} // !sawInputEOS

					// decode to PCM and push it to the AudioTrack player
					int res = mMediaCodec.dequeueOutputBuffer(info, kTimeOutUs);

					if (res >= 0)
					{
						int outputBufIndex = res;
						ByteBuffer buf = outputBuffers[outputBufIndex];
						final byte[] chunk = new byte[info.size];
						System.out.println("info.size="+info.size);
						buf.get(chunk);
						buf.clear();
						if (chunk.length > 0)
						{								
							//��ȡ��Ƶ��������
						    leftRightChannel =BufferUtil.getLeftChannel(chunk);
							
							// �߽�߷���
							if (decodeBuffer.length ==0)
							{
								//�������ݱ���Ŀ
								int num = leftRightChannel[channelTypeInt].length / 1200;
								decodeBuffer = new byte[leftRightChannel[channelTypeInt].length - num * 1200];
								//����������ݴ浽�����������´ν���ƴ��
								System.arraycopy(leftRightChannel[channelTypeInt], num * 1200, decodeBuffer, 0, decodeBuffer.length);

								/*=======================send=======================*/ 
								for (int i = 0; i < num && AudioCommand.isSend; i++)
								{
									Log.w(TAG, "bufferSize=" + AudioCommand.bufferSize);
									// 1M,����������������1m,ֹͣ�������ݣ��ȴ����������ݼ���
									if (AudioCommand.bufferSize > 1048576)
									{
										try
										{
											// sleep 10s,ֻҪ��֤������������������
											Thread.sleep(10000);
										} catch (InterruptedException e)
										{
											Log.w(TAG, "sleep error:" + e.getMessage());
										}
									}

									byte[] datas = byte2MusicCommand(Arrays.copyOfRange(leftRightChannel[channelTypeInt], i * 1200, i * 1200 + 1200));
									// �����udp,������
									try
									{								
										switch (channelID)
										{
										case 1:
											sendAudio(datas,  port,GlobalValue.ip1);
											break;
										case 2:
											sendAudio(datas,  port,GlobalValue.ip2);
											break;
										case 3:
											sendAudio(datas,  port,GlobalValue.ip3);
											break;
										case 4:
											sendAudio(datas,  port,GlobalValue.ip4);
											break;
										case 5:
											sendAudio(datas,  port,GlobalValue.ip5);
											break;
										case 6:	
											sendAudio(datas,  port,GlobalValue.ip6);

											break;
										case 7:
											sendAudio(datas,  port,GlobalValue.ip7);
											break;
										case 8:
											sendAudio(datas,  port,GlobalValue.ip8);
											break;
										}
																												
									} catch (Exception e1)
									{
										Log.e(TAG, "������Ƶ����:" + e1.getMessage());
									}
									// ��10ms��һ��udp��
									try
									{
										Thread.sleep(7);
									} catch (InterruptedException e)
									{
										Log.e(TAG, "�ӳٳ��� error:" + e.getMessage());
									}
								}

							} else 
							{
								byte[] add = new byte[decodeBuffer.length + leftRightChannel[channelTypeInt].length];
								System.arraycopy(decodeBuffer, 0, add, 0, decodeBuffer.length);
								System.arraycopy(leftRightChannel[channelTypeInt], 0, add, decodeBuffer.length, leftRightChannel[channelTypeInt].length);

								int num = add.length / 1200;
								decodeBuffer = new byte[add.length - num * 1200];
								System.arraycopy(add, num * 1200, decodeBuffer, 0, decodeBuffer.length);
								// send

								for (int i = 0; i < num && AudioCommand.isSend; i++)
								{
									Log.w(TAG, "bufferSize=" + AudioCommand.bufferSize);
									// 1M
									if (AudioCommand.bufferSize > 1048576)
									{
										try
										{
											// sleep 10s,ֻҪ��֤������������������
											Thread.sleep(10000);
										} catch (InterruptedException e)
										{
											Log.w(TAG, "sleep error:" + e.getMessage());
										}
									}

									byte[] datas = byte2MusicCommand(Arrays.copyOfRange(add, i * 1200, i * 1200 + 1200));
									// �����udp,������
									try
									{
										switch (channelID)
										{
										case 1:
											sendAudio(datas,  port,GlobalValue.ip1/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 2:
											sendAudio(datas,  port,GlobalValue.ip2/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 3:
											sendAudio(datas,  port,GlobalValue.ip3/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 4:
											sendAudio(datas,  port,GlobalValue.ip4/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 5:
											sendAudio(datas,  port,GlobalValue.ip5/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 6:	
											sendAudio(datas,  port,GlobalValue.ip6/* GlobalValue.speakerSelect1.getIp()*/);

											break;
										case 7:
											sendAudio(datas,  port,GlobalValue.ip7/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										case 8:
											sendAudio(datas,  port,GlobalValue.ip8/* GlobalValue.speakerSelect1.getIp()*/);
											break;
										}														
									} catch (Exception e1)
									{
										Log.e(TAG, "������Ƶ����:" + e1.getMessage());
									}
									// ��10ms��һ��udp��
									try
									{
										Thread.sleep(7);
									} catch (InterruptedException e)
									{
										Log.e(TAG, "�ӳٳ��� error:" + e.getMessage());
									}
								}

							}
						}
						mMediaCodec.releaseOutputBuffer(outputBufIndex, false);
						if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)
						{
							Log.d(TAG, "saw output EOS.");
							sawOutputEOS = true;
						}

					} else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED)
					{
						outputBuffers = mMediaCodec.getOutputBuffers();
						Log.w(TAG, "[AudioDecoder]output buffers have changed.");
					} else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED)
					{
						MediaFormat oformat = mMediaCodec.getOutputFormat();
						Log.w(TAG, "[AudioDecoder]output format has changed to " + oformat);
					} else
					{
						Log.w(TAG, "[AudioDecoder] dequeueOutputBuffer returned " + res);
					}

				} catch (RuntimeException e)
				{
					Log.e(TAG, "[decodeMP3] error:" + e.getMessage());
				}
			}    	 
	    	 
			// =================================================================================
			if (mMediaCodec != null)
			{
				mMediaCodec.stop();
				mMediaCodec.release();
				mMediaCodec = null;
			}
			if (extractor != null)
			{
				extractor.release();
				extractor = null;
			}
			// clear source and the other globals
			duration = 0;
			mime = null;
			sampleRate = 0;
			channels = 0;
			bitrate = 0;
			presentationTimeUs = 0;
			duration = 0;
		}
		
		/* ==================================Music Command================================== */

		   
		/**
		 * ������Ƶ
		 * 
		 */
		private void sendAudio(byte[] audioData, int port,String /*InetAddress*/ ip)
		{
			try
			{
				// �����͵�byte������
				mPacket = new DatagramPacket(audioData, audioData.length, InetAddress.getByName(ip), port);
				// ����
				mSocket.send(mPacket);
				Log.i(TAG, "�ɹ�����");
			} catch (Exception e)
			{
				Log.e(TAG, "sendData() error:" + e.getMessage());
			}
		}

		/**
		 * �ϲ�Byte[]���飬��װ����ƵЭ��
		 * 
		 * @param datas
		 *            ����͵���ƵЭ��
		 * @param audioData
		 *            �ְ�����Ƶ����
		 * @return
		 */
		public byte[] byte2MusicCommand(byte[] audioData)
		{
			byte[] datas = new byte[1242];
			// ===================��װ����ƵЭ��=================
			System.arraycopy(head, 0, datas, 0, 6);
			System.arraycopy(checkSum, 0, datas, 6, 2);
			System.arraycopy(protocalVersion, 0, datas, 8, 1);
			System.arraycopy(cmd, 0, datas, 9, 1);
			System.arraycopy(localIP, 0, datas, 10, 4);
			System.arraycopy(localPort, 0, datas, 14, 2);
			System.arraycopy(destIP, 0, datas, 16, 4);
			System.arraycopy(destPort, 0, datas, 20, 2);
			System.arraycopy(localId, 0, datas, 22, 4);
			System.arraycopy(sessionId, 0, datas, 26, 4);
			System.arraycopy(packetNo, 0, datas, 30, 4);
			System.arraycopy(sample, 0, datas, 34, 1);
			System.arraycopy(bitDepth, 0, datas, 35, 1);
			System.arraycopy(timeStamp, 0, datas, 36, 4);
			System.arraycopy(audioData, 0, datas, 40, 1200);// �±�40��ʼ
			System.arraycopy(end, 0, datas, 1240, 2);
			return datas;
		}
		
}
