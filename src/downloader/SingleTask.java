package downloader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;


public class SingleTask extends Observable implements Runnable {
		/** The URL to download the file */
		private URL mURL;
		
		/** Output folder for downloaded file */
		private String mOutputFolder;
		
		/** The file name, extracted from URL */
		private String mFileName;
		
		/** Size of the downloaded file (in bytes) */
		private int mFileSize;
		
		/** The state of the download, it needs to be marked volatile */
		private volatile int mState;
		
		/** downloaded size of the file (in bytes) */
		private volatile int mDownloaded;
		
		//the priority of the download task.
		private volatile int priority;
		
		public static final String PRIORITIES[]={"High","Low"};
		
		/**executor*/
		private final ExecutorService downloaderPool;
		//private final Scheduler poolScheduler;
		private final BlockingQueue<Runnable> downloadQueue;

		// Contants for block and buffer size, 1M
		private static final int BLOCK_SIZE = 1024000;
		private static final int BUFFER_SIZE = 1024;
		
		// These are the status names.
	    public static final String STATUSES[] = {"Downloading",
	    				"Paused", "Complete", "Cancelled", "Error","Starting"};
		
		// Contants for download's state
		public static final int DOWNLOADING = 0;
		public static final int PAUSED = 1;
		public static final int COMPLETED = 2;
		public static final int CANCELLED = 3;
		public static final int ERROR = 4;
		public static final int START=5;

		private LinkedBlockingQueue<Future<?>> futureQueue;
		
		private int numRunnables;
		private final Scheduler scheduler;
		
	
	public SingleTask(URL url, String outputFolder,ExecutorService downloaderPool, int priority,Scheduler scheduler) {
		mURL = url;
		mOutputFolder = outputFolder;
		this.downloaderPool=downloaderPool;
		downloadQueue=new LinkedBlockingQueue<Runnable>();
		String fileURL = url.getFile();
		mFileName = fileURL.substring(fileURL.lastIndexOf('/') + 1);
		System.out.println("Start downloading file: " + mFileName);
		mFileSize = -1;
		mState = START;
		mDownloaded = 0;
		this.priority=priority;
		futureQueue=new LinkedBlockingQueue<Future<?>>();
		this.scheduler=scheduler;
	}
	
	public void enqueue(Future<?> future)
	{
		futureQueue.add(future);
	}
	
	
	private void error() {
		System.out.println("ERROR");
		setState(ERROR);
	}
	
	
	@Override
	public void run() {
		HttpURLConnection conn = null;
		try {
			// Open connection to URL
			conn = (HttpURLConnection)mURL.openConnection();
			conn.setConnectTimeout(10000);
			// Connect to server
			conn.connect();
			// Make sure the response code is in the 200 range.
            if (conn.getResponseCode() / 100 != 2) {
            	// edit   
                error();
            }
            
            // Check for valid content length.
            int contentLength = conn.getContentLength();
            if (contentLength < 1) {
                error();
            }
			
            if (mFileSize == -1) {
            	mFileSize = contentLength;
            	stateChanged();
            	System.out.println("File size: " + mFileSize);
    			if (conn != null)
    				conn.disconnect();
            }
                    
		    if(mFileSize>=BLOCK_SIZE)
				  {  // parallelize the download work, divide the whole file in to multiple Runnable
            		    // each Runnable is responsible to download Block_size.
						numRunnables = Math.round(((float)mFileSize /BLOCK_SIZE));
						//System.out.println("Part size: " + partSize);
						System.out.println("Number of runnables is"+numRunnables);
						// start/end Byte for each thread
						long startByte = 0;
						long endByte = BLOCK_SIZE - 1;				

						while (endByte < mFileSize) {
							HttpDownloadThread aThread = new HttpDownloadThread(startByte, endByte);
							aThread.download();
							startByte = endByte + 1;
							endByte += BLOCK_SIZE;
						}
						endByte=mFileSize-1;
						HttpDownloadThread aThread = new HttpDownloadThread(startByte, endByte);
						aThread.download();
						setState(DOWNLOADING);
				  }
				   else
						{
					    numRunnables=1;
						long startByte=0;
						long endByte=mFileSize-1;
						HttpDownloadThread aThread = new HttpDownloadThread(startByte, endByte);
						aThread.download();
						setState(DOWNLOADING);			
						}
	      while(numRunnables>0)
	       { 
	    	   Future<?> future=futureQueue.take(); 
	    	   future.get();
	    	   numRunnables--;
	       }
	   //    finish();
		  setState(COMPLETED);		   
		} catch (Exception e) {
			System.out.println(e.toString());
			Thread.dumpStack();
			error();
		} 
	}
	      
		public int getPriority()
		{
			return priority;
		}
		
		public void setPriority()
		{
			priority=1-priority;
			stateChanged();
		}
		
		
		public int getQueueLength()
		{
			return downloadQueue.size();
		}
		
		public Runnable getRunnableFromQueue() throws InterruptedException
		{
			return downloadQueue.take();
		}
		
		public void pause() {
			setState(PAUSED);
		}	
		/**
		 * Resume the downloader
		 */
		public void resume() {
			setState(DOWNLOADING);
		}
		
		/**
		 * Cancel the downloader
		 */
		public void cancel() {
			setState(CANCELLED);
		}
		
		/**
		 * Get the URL (in String)
		 */
		public String getURL() {
			return mURL.toString();
		}
		
		/**
		 * Get the downloaded file's size
		 */
		public long getFileSize() {
			return mFileSize;
		}
		
		/**
		 * Get the current progress of the download
		 */
		public float getProgress() {
			return ((float)mDownloaded / mFileSize) * 100;
		}
		
		/**
		 * Get current state of the downloader
		 */
		public int getState() {
			return mState;
		}
		
		/**
		 * Set the state of the downloader
		 */
		public synchronized void setState(int value) {
			mState = value;
			stateChanged();
		}
		
		/**
		 * Start or resume download
		 */
		protected void download() {
			downloaderPool.execute(this);
		}
		
		/**
		 * Increase the downloaded size
		 */
		protected synchronized void downloaded(int value) {
			mDownloaded += value;
			stateChanged();
		}
		
		protected synchronized void finish()
		{
			mDownloaded=mFileSize;
			stateChanged();
		}
		
		/**
		 * Set the state has changed and notify the observers
		 */
		protected void stateChanged() {
			setChanged();
			notifyObservers();
	}
	
	/**
	 * Thread using Http protocol to download a part of file
	 */
	protected class HttpDownloadThread implements Runnable{
		
		private long tStartByte;
		private long tEndByte;

		public HttpDownloadThread( long startByte, long endByte) {
			tStartByte = startByte;
			tEndByte = endByte;
		}
		
		public void download() {
				downloadQueue.add(this);
		}
		public synchronized int getState()
		{
			return mState;
		}
		
		@Override
		public void run() {
			BufferedInputStream in = null;
			RandomAccessFile raf = null;
			
			try {
				// open Http connection to URL
				HttpURLConnection conn = (HttpURLConnection)mURL.openConnection();
				
				// set the range of byte to download
				String byteRange = tStartByte + "-" + tEndByte;
				conn.setRequestProperty("Range", "bytes=" + byteRange);

				// connect to server
				conn.connect();
				
				// Make sure the response code is in the 200 range.
	            if (conn.getResponseCode() / 100 != 2) {
	                error();
	            }
				
				// get the input stream
				in = new BufferedInputStream(conn.getInputStream());
				
				// open the output file and seek to the start location
				raf = new RandomAccessFile(mOutputFolder+mFileName, "rw");
				raf.seek(tStartByte);
				
				byte data[] = new byte[BUFFER_SIZE];
				int numRead;
				while((numRead = in.read(data,0,BUFFER_SIZE)) != -1 && mState==DOWNLOADING)
				{
					// write to buffer
					raf.write(data,0,numRead);
					
					tStartByte+=numRead;
					// increase the downloaded size
					downloaded(numRead);
				}
				if(mState!=DOWNLOADING)
				{
				   scheduler.resubmit(this);
				}
			} catch (IOException e) {
				System.out.println(e.toString());
				Thread.dumpStack();
				error();
			} finally {
				if (raf != null) {
					try {
						raf.close();
					} catch (IOException e) {}
				}
				
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {}
				}
			}
		}
	}
}
