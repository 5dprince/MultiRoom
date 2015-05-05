package com.homni.multiroom.util;

import java.util.ArrayList;

import com.homni.multiroom.model.Music;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

/**
 * ������Ϣ����
 * 
 * @author Dawin
 *
 */
public class MusicLoader {
	private static  final String TAG="MusicLoader";
	private static MusicLoader mMusicLoader;
	private static final Uri URI = Media.EXTERNAL_CONTENT_URI;
	private ArrayList<Music> musicInfos=new ArrayList<Music>();
	private static ContentResolver mContentResolver;
	
	public static MusicLoader getInstance(ContentResolver contentResolver) {
		if (mMusicLoader == null) {
			//ʹ��Context�ṩ��getContentResolver()������ȡContentResolver����
			mContentResolver=contentResolver;
			mMusicLoader = new MusicLoader();
		}
		return mMusicLoader;
	}
	public MusicLoader(){
		//�����������ⲿ�洢���ϵ������ļ�����Ϣ
		/*
		˵��
		Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
		//Uri��ָ��Ҫ��ѯ�����ݿ����Ƽ��ϱ�����ƣ���MediaStore�����ǿ����ҵ���Ӧ��Ϣ�Ĳ�����
	    //Projection: ָ����ѯ���ݿ���е��ļ��У����ص��α��н�������Ӧ����Ϣ��Null�򷵻�������Ϣ��
	    //selection: ָ����ѯ����
	    //selectionArgs������selection���� ?��������ǣ����������ʵ��ֵ��������ʺš����selection���û�У��Ļ�����ô���String�������Ϊnull��
	    //SortOrder��ָ����ѯ���������˳��	    
	    */
		Cursor cursor = mContentResolver.query(URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER/*null*/);
		if (cursor == null)
			{
				//���Ҹ���ʧ��
				LogUtil.e(TAG,"Music Loader cursor == null.  query failed, handle error.");
			} else if (!cursor.moveToFirst())
			{
				//�豸û�и���
				LogUtil.e(TAG,"Music Loader cursor.moveToFirst() returns false.   no media on the device");
			} else
			{				
				/*-----------������Ϣ�±�---------*/				
				int displayNameCol = cursor.getColumnIndex(Media.TITLE);
				int albumCol = cursor.getColumnIndex(Media.ALBUM);
				int albumIDCol=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int idCol = cursor.getColumnIndex(Media._ID);
				int durationCol = cursor.getColumnIndex(Media.DURATION);
				int sizeCol = cursor.getColumnIndex(Media.SIZE);
				int artistCol = cursor.getColumnIndex(Media.ARTIST);
				// �ļ�·��
				int urlCol = cursor.getColumnIndex(Media.DATA);					
				int isMusicCol=cursor.getColumnIndex(/*MediaStore.Audio.*/Media.IS_MUSIC);
				
				/*------------------���豸���и����ĸ�����Ϣ���浽ArrayList������-------------*/
				do
					{
					    // �Ƿ�Ϊ���֣�1��  0���ǣ���������...
					    int isMusic =cursor.getInt(isMusicCol);	
					    if(isMusic==1){
					    	//����ID��������
							long id = cursor.getLong(idCol);
							// ��ʱ��
							int duration = cursor.getInt(durationCol);
							//����
							String title = cursor.getString(displayNameCol);
							//ר����
							String album = cursor.getString(albumCol);						
							// ר��ID
							long albumid = cursor.getLong(albumIDCol);
							//����
							String artist = cursor.getString(artistCol);
							//����·��
							String url = cursor.getString(urlCol);
							String fileFormat=url.substring(url.lastIndexOf(".")+1);
							// �ļ���С
							long size = cursor.getLong(sizeCol);							
							/*--------------------*/
							Music musicInfo = new Music();	
							musicInfo.setMusicId(id);
							musicInfo.setMusicName(title+"."+fileFormat);
							musicInfo.setAlbumName(album);
							musicInfo.setDuration(duration);
							musicInfo.setSize(size);
							musicInfo.setArtist(artist);
							musicInfo.setUrl(url);
							musicInfo.setAlbumId(albumid);
							//�Ƿ�������
							musicInfo.setIsMusic(isMusic);
							musicInfo.setFileFormat(fileFormat);
							musicInfos.add(musicInfo);
					    }						
					} while (cursor.moveToNext());
			}		
	}
	public ArrayList<Music> getMusicList(){
		return musicInfos;
	}
}
