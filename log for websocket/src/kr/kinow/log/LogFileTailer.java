package kr.kinow.log;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LogFileTailer extends Thread {
	private long interval;
	private File logfile;
	private boolean startBeginning = true;
	private boolean tailing = false;
	private Set<LogFileTailerListener> listeners = new HashSet<LogFileTailerListener>();
	
	public LogFileTailer(String filepath, long interval, boolean startBeginning) {
		this.logfile = new File(filepath);
		this.interval = interval;
		this.startBeginning = startBeginning;
	}
	
	public void addListener(LogFileTailerListener l) {
		this.listeners.add(l);
	}
	
	public void removeListener(LogFileTailerListener l) {
		this.listeners.remove(l);
	}
	
	public void fireReadLine(String line) {
		LogFileTailerListener l;
		Iterator<LogFileTailerListener> it = this.listeners.iterator();
		while (it.hasNext()) {
			l = it.next();
			l.readLine(line);
		}
	}
	
	public void stopTailing() {
		this.tailing = false;
	}
	
	public void run() {
		long filePointer = 0;
		long fileLength;
		String line;
		
		if (!this.startBeginning) {
			filePointer = this.logfile.length();
		}
		
		try {
			this.tailing = true;
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			while (this.tailing) {
				try {
					fileLength = this.logfile.length();
					
					// log file maybe rotated or deleted.
					if (fileLength < filePointer) {
						// reopen log file
						file.close();
						file = new RandomAccessFile(logfile, "r");
						filePointer = 0;
					}
					if (fileLength > filePointer) {
						// move file pointer position
						file.seek(filePointer);
						line = file.readLine();
						while (line != null) {
							this.fireReadLine(line);
							line = file.readLine();
						}
						filePointer = file.getFilePointer();
					}					
					sleep(this.interval);
					
				} catch (Exception e) {}
			}
			file.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
