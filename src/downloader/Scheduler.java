package downloader;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler implements Runnable {
	
	private final ExecutorService downloadTaskPool;
	private final CopyOnWriteArrayList<SingleTask> mdownloaderList;
	public static final int PRIORITY_LEVEL= 2;
	private LinkedBlockingQueue<Runnable> resubmitQueue;
	
	public Scheduler(ExecutorService downloadTaskPool,CopyOnWriteArrayList<SingleTask> mdownloaderList)
	{   this.downloadTaskPool=downloadTaskPool;
		this.mdownloaderList=mdownloaderList;
		resubmitQueue=new LinkedBlockingQueue<Runnable>();
	}
	public void resubmit(Runnable r)
	{
		resubmitQueue.add(r);
	}
	
	@Override
	public void run() {
		while(true)
		{
			 for(SingleTask downloader:mdownloaderList)
			 {   int block;
				 if(downloader.getPriority()==0)
					 block=PRIORITY_LEVEL;
				 else
					 block=1;		 
				while(block>0)
			  { if(downloader.getState()==SingleTask.DOWNLOADING)
				 {
					if (downloader.getQueueLength()>=1)
					{
						try {
							downloader.enqueue(downloadTaskPool.submit(downloader.getRunnableFromQueue()));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						block--;
					}
				 }
			  else{
				  block-=PRIORITY_LEVEL;
			  }
			 }
			}
			if(!resubmitQueue.isEmpty())
			{
				try {
					downloadTaskPool.submit(resubmitQueue.take());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
	

