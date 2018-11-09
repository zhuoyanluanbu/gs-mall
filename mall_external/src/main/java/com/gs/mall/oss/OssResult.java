package com.gs.mall.oss;

public class OssResult {
	
	//上传文件时候返回的UUID，是文件在OSS上的key 值，
	//以后 下载或者生成签名URL 都必须使用此地址

	private String key;
	
	//默认的访问URL
	private String url;
	
	//成功标志  0 成功  非0失败
	private boolean isSuccess;
	 
	private long fileSize;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}  
	
	
	
}
