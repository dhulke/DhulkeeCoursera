package coursera;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Downloader {

	private static final int STANDARD_BUFFER_SIZE = 1048576;//4KB
	
	private DownloaderListener downloaderListener;
	private InputStream inputStream;
	private OutputStream outputStream;
	private int chunkSize;
	
	private boolean stopped;
	
	public Downloader() {}


	public Downloader(InputStream inputStream, OutputStream outputStream, int chunkSize) {
		this(inputStream, outputStream);
		this.chunkSize = chunkSize;
	}
	
	public Downloader(InputStream source, OutputStream destination) {
		this.inputStream = source;
		this.outputStream = destination;
		this.stopped = false;
	}
	
	public void download() throws IOException {
		int currentChunkSize = 0;
		int bytesRead = 0;
		byte[] buffer = new byte[STANDARD_BUFFER_SIZE];
		
		BufferedInputStream bis = new BufferedInputStream(inputStream, STANDARD_BUFFER_SIZE);

		if(downloaderListener != null)
			downloaderListener.beforeDownloading();
		
		while(!stopped && (bytesRead = bis.read(buffer)) != -1) {
			
			outputStream.write(buffer, 0, bytesRead);
	    	
	    	currentChunkSize += bytesRead;
	    	
	    	if(downloaderListener != null && currentChunkSize >= chunkSize) {
	    		downloaderListener.downloading(currentChunkSize);
	    		currentChunkSize = 0;
	    	}
		}
		
		close();
		
		if(downloaderListener != null) {
			
			//If the download is over and the last chunk didn't get to be fired
			if(currentChunkSize != 0)
				downloaderListener.downloading(currentChunkSize);
			
			if(stopped) {
				downloaderListener.stoppedDownloading();
			} else
				downloaderListener.afterDownloading();
		}
	}
	
	public void close() {
		try {
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DownloaderListener getDownloaderListener() {
		return downloaderListener;
	}

	public void setDownloaderListener(DownloaderListener downloaderListener) {
		this.downloaderListener = downloaderListener;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public int getChunckSize() {
		return chunkSize;
	}

	public void setChunckSize(int chunckSize) {
		this.chunkSize = chunckSize;
	}

	public synchronized boolean isStopped() {
		return stopped;
	}

	public synchronized void stop() {
		stopped = true;
	}

}