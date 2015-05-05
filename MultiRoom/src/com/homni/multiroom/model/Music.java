package com.homni.multiroom.model;

/**
 * ����
 * @author Dawin
 *
 */
public class Music {	
	// ����ID
	private long musicId;
	// ר��ID
	private long albumId;
	// ����
	private String musicName;
	// ר����
	private String albumName;
	// ��������ʱ��
	private int duration;
	private long size;
	// ����
	private String artist;
	// ����·��
	private String url;
	//�Ƿ�������
	private int isMusic;
	//��ʽ
	private String fileFormat;
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public long getMusicId() {
		return musicId;
	}
	public void setMusicId(long musicId) {
		this.musicId = musicId;
	}
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIsMusic() {
		return isMusic;
	}
	public void setIsMusic(int isMusic) {
		this.isMusic = isMusic;
	}
	
}
