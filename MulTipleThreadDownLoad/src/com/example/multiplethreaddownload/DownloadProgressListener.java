package com.example.multiplethreaddownload;

public interface DownloadProgressListener {
	/**
	 * ���ؽ��ȼ��������� ��ȡ�ʹ������ص����ݵĴ�С
	 * @param size
	 */
	public void onDownloadSize(int size);
}
