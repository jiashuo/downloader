package downloader;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Component;


public class DownloadManagerGUI extends javax.swing.JFrame implements Observer{
	
	private DownloadTableModel mTableModel;
	
	/**always point to the current Downloader **/
	private volatile SingleTask currentDownloader;
	
	private final DownloadManager downloadManager;
	
	//private boolean mIsClearing;
	
	/** Creates new form DownloadManagerGUI */
    public DownloadManagerGUI() {
        downloadManager=new DownloadManager();
        downloadManager.startScheduler();    // start scheduler thread
    	mTableModel = new DownloadTableModel(downloadManager);
        initComponents();
        initializeTable();
    }
    
    private void initializeTable() {
    	// Set up table 
    	jtbDownload.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
    	
    	// Allow only one row at a time to be selected.
    	jtbDownload.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	
    	// Set up ProgressBar as renderer for progress column.
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        jtbDownload.setDefaultRenderer(JProgressBar.class, renderer);
        
        // Set table's row height large enough to fit JProgressBar.
        jtbDownload.setRowHeight(
                (int) renderer.getPreferredSize().getHeight());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxURL = new javax.swing.JTextField();
        jbnAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbDownload = new javax.swing.JTable();
        jbnPause = new javax.swing.JButton();
        jbnRemove = new javax.swing.JButton();
        jbnCancel = new javax.swing.JButton();
        jbnExit = new javax.swing.JButton();
        jbnResume = new javax.swing.JButton();
        jbnPriority=new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Download Manager");
        setResizable(true);

        jbnAdd.setText("Add Download Task");        
        jbnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jbnAddActionPerformed(evt);
            }
        });
        
        //maybe I can add start here

        jtbDownload.setModel(mTableModel);
        jScrollPane1.setViewportView(jtbDownload);

        jbnPause.setText("Pause");
        jbnPause.setEnabled(false);
        jbnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnPauseActionPerformed(evt);
            }
        });

        jbnRemove.setText("Remove");
        jbnRemove.setEnabled(false);
        jbnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnRemoveActionPerformed(evt);
            }
        });

        jbnCancel.setText("Cancel");
        jbnCancel.setEnabled(false);
        jbnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnCancelActionPerformed(evt);
            }
        });

        jbnExit.setText("Exit");
        jbnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnExitActionPerformed(evt);
            }
        });

        jbnResume.setText("Resume");
        jbnResume.setEnabled(false);
        jbnResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbnResumeActionPerformed(evt);
            }
        });
        
        
        jbnPriority.setText("Priority");
        jbnPriority.setEnabled(false);
        jbnPriority.addActionListener(new java.awt.event.ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		jbnChangePriority(evt);
        	}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jbnPause, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(jbnResume, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(jbnCancel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(jbnRemove, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(jbnPriority, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
        					.addComponent(jbnExit, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jtxURL, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jbnAdd))
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jtxURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jbnAdd))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jbnPause)
        				.addComponent(jbnResume)
        				.addComponent(jbnCancel)
        				.addComponent(jbnRemove)
        				.addComponent(jbnExit)
        				.addComponent(jbnPriority))
        			.addContainerGap())
        );
        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {jbnExit, jbnResume, jbnPause, jbnRemove, jbnCancel});
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnPauseActionPerformed
    	currentDownloader.pause();
        updateButtons();
    }//GEN-LAST:event_jbnPauseActionPerformed

    private void jbnResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnResumeActionPerformed
    	currentDownloader.resume();
        updateButtons();
    }//GEN-LAST:event_jbnResumeActionPerformed

    private void jbnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnCancelActionPerformed
    	// cancel is not correct.
    	currentDownloader.cancel();
        updateButtons();
    }//GEN-LAST:event_jbnCancelActionPerformed

    private void jbnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnRemoveActionPerformed
  //  	mIsClearing = true;
    	int index = jtbDownload.getSelectedRow();
    	downloadManager.removeDownload(index);
    	mTableModel.clearDownload(index);
   //     mIsClearing = false;
        currentDownloader = null;
        updateButtons();
    }//GEN-LAST:event_jbnRemoveActionPerformed

    private void jbnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnExitActionPerformed
        setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jbnExitActionPerformed

    private void jbnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbnAddActionPerformed
    	URL verifiedUrl = DownloadManager.verifyURL(jtxURL.getText());
        if (verifiedUrl != null) {
        	SingleTask downloader = downloadManager.createDownload(verifiedUrl, 
        			DownloadManager.DEFAULT_OUTPUT_FOLDER,true);   //set the download job to true by default
        	if (downloader==null)
        	{ JOptionPane.showMessageDialog(this,
                        "Maximum number of running task is: "+DownloadManager.MAX_DOWNLAOD_TASK_NUM, "Error",
                        JOptionPane.ERROR_MESSAGE);}
        	else{
        	mTableModel.addNewDownload(downloader);}	  	
        	jtxURL.setText(""); // always reset add text field
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jbnAddActionPerformed
    private void jbnChangePriority(java.awt.event.ActionEvent evt)
    {
    	currentDownloader.setPriority();
    }

 // Called when table row selection changes.
    private void tableSelectionChanged() {
    	// unregister from receiving notifications from the last selected download.
        if (currentDownloader != null)
        	currentDownloader.deleteObserver(DownloadManagerGUI.this);
        int index = jtbDownload.getSelectedRow();
        if (index != -1) {
	        	currentDownloader = downloadManager.getDownload(index);
	        	currentDownloader.addObserver(DownloadManagerGUI.this);
        	} else
        		currentDownloader = null;
            updateButtons();
    }
    
    @Override
	public void update(Observable o, Object arg) {
    	// Update buttons if the selected download has changed.
    //	only event dispatch thread can update GUI
    	if (currentDownloader != null && currentDownloader.equals(o))
    		EventQueue.invokeLater(new Runnable()
    		  { 
    			public void run()
    			{
    	            updateButtons();
    			}
    		  });
	}
    /**
     * Update buttons' state
     */
    private void updateButtons() {
        if (currentDownloader != null) {
            int state = currentDownloader.getState();
            switch (state) {
                case SingleTask.DOWNLOADING:
                    jbnPause.setEnabled(true);
                    jbnResume.setEnabled(false);
                    jbnCancel.setEnabled(true);
                    jbnRemove.setEnabled(false);
                    jbnPriority.setEnabled(true);
                    break;
                case SingleTask.PAUSED:
                	jbnPause.setEnabled(false);
                	jbnResume.setEnabled(true);
                	jbnCancel.setEnabled(true);
                	jbnRemove.setEnabled(false);
                	jbnPriority.setEnabled(true);
                    break;
                case SingleTask.ERROR:
                	jbnPause.setEnabled(false);
                	jbnResume.setEnabled(true);
                	jbnCancel.setEnabled(false);
                	jbnRemove.setEnabled(true);
                	jbnPriority.setEnabled(false);
                    break;
                default: // COMPLETE or CANCELLED
                	jbnPause.setEnabled(false);
                	jbnResume.setEnabled(false);
                	jbnCancel.setEnabled(false);
                	jbnRemove.setEnabled(true);
                	jbnPriority.setEnabled(false);
            }
        } else {
            // No download is selected in table.
        	jbnPause.setEnabled(false);
        	jbnResume.setEnabled(false);
        	jbnCancel.setEnabled(false);
        	jbnRemove.setEnabled(false);
        	jbnPriority.setEnabled(false);
        }
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
    	// set to user's look and feel   	
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        EventQueue.invokeLater(new Runnable() {
            public void run() {
               DownloadManagerGUI dgui=new DownloadManagerGUI();
                dgui.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbnAdd;
    private javax.swing.JButton jbnCancel;
    private javax.swing.JButton jbnExit;
    private javax.swing.JButton jbnPause;
    private javax.swing.JButton jbnRemove;
    private javax.swing.JButton jbnResume;
    private javax.swing.JTable jtbDownload;
    private javax.swing.JTextField jtxURL;
    private javax.swing.JButton jbnPriority;
}
