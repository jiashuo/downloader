package downloader;

import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DownloadManager {
	
	public static final String DEFAULT_OUTPUT_FOLDER = ".";
	//Maximum number of simultaneous downloading tasks
	public static final int MAX_DOWNLAOD_TASK_NUM = 10;   
	public static final int DOWNLOADER_POOL_SIZE = MAX_DOWNLAOD_TASK_NUM/2;   
	public static final int CORE_WORKER_POOL_SIZE = Math.round((float)(Runtime.getRuntime().availableProcessors()*1.5));
	public static final int MAX_WORKER_POOL_SIZE=Runtime.getRuntime().availableProcessors()*2;
	private final ThreadPoolExecutor downloadTaskPool;  
	private final ExecutorService downloaderPool;
	private final Scheduler poolScheduler;	
	private CopyOnWriteArrayList<SingleTask> mDownloadList;
	
	/** Protected constructor */
	protected DownloadManager() {
		mDownloadList = new CopyOnWriteArrayList<SingleTask>();
		ArrayBlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(CORE_WORKER_POOL_SIZE*5);
		downloadTaskPool= new ThreadPoolExecutor(CORE_WORKER_POOL_SIZE,MAX_WORKER_POOL_SIZE,1,TimeUnit.MINUTES,queue);
		downloadTaskPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		downloaderPool= Executors.newFixedThreadPool(DOWNLOADER_POOL_SIZE);
		poolScheduler=new Scheduler(downloadTaskPool,mDownloadList);
	}	
	/**
	 * Get the downloader object in the list
	 * @param index
	 * @return
	 */
	public SingleTask getDownload(int index) {
		return mDownloadList.get(index);
	}
	
	public void removeDownload(int index) {
		mDownloadList.remove(index);
	}	
	/**
	 * Get the download list
	 * @return
	 */
	public synchronized CopyOnWriteArrayList<SingleTask> getDownloadList() {
		return mDownloadList;
	}
	
	public SingleTask createDownload(URL verifiedURL, String outputFolder,boolean priority) {
		if(mDownloadList.size()>=MAX_DOWNLAOD_TASK_NUM)
		{
			return null;
		}
		else{
		SingleTask fd = new SingleTask(verifiedURL, outputFolder, downloaderPool,0,poolScheduler);
		mDownloadList.add(fd);
		fd.download();
		return fd;
	    }
		}
	
	public void startScheduler()
	{
		Thread t=new Thread(poolScheduler);
		t.start();
	}
	/**
	 * Verify whether an URL is valid, called from event dispatch thread
	 * @param fileURL
	 * @return the verified URL, null if invalid
	 */
	public static URL verifyURL(String fileURL) {
		// Only allow HTTP URLs.
        if (!fileURL.toLowerCase().startsWith("http://"))
            return null;
        
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(fileURL);
        } catch (Exception e) {
            return null;
        }
        
        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 1)  
            return null;
        
        return verifiedUrl;
	}

}
