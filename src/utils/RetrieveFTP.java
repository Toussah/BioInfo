package utils;

import app.App;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class RetrieveFTP 
{
	FTPClient client = new FTPClient();
	private String hostname;
	private String genDirectory;
	private final String username = "anonymous";
	private final String password = "";
	private final String prokaryoteFileName = "prokaryotes.txt";
	private final String eukaryoteFileName = "eukaryotes.txt";
	private final String virusFileName = "viruses.txt";
	private static final int counter_timeout = 10;
	
	public RetrieveFTP(String hostname, String genDirectory) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException
	{
		this.setHostname(hostname);
		this.setGenDirectory(genDirectory);
		client.connect(hostname);
		client.login(username, password);
		client.changeDirectory(genDirectory);
	}
	
	public File getProkariotes() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException
	{
		File p = new File("FTP" + App.FILE_SEP + prokaryoteFileName);
		int failedAttempt = 0;
		Boolean success = false;
		do 
		{
			try
			{
				System.out.println(failedAttempt);
				success = true;
				failedAttempt++;
				client.download(prokaryoteFileName, p);
			}
			catch(Exception e)
			{
				client.disconnect(true);
				success = false;
				client.connect(hostname);
				client.login(username, password);
			}
		}while(failedAttempt < counter_timeout && !success);
		return p;
	}
	
	public File getEukariotes() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException
	{
		File p = new File("FTP" + App.FILE_SEP + eukaryoteFileName);
		int failedAttempt = 0;
		Boolean success = false;
		do 
		{
			try
			{
				System.out.println(failedAttempt);
				success = true;
				failedAttempt++;
				client.download(eukaryoteFileName, p);
			}
			catch(Exception e)
			{
				client.disconnect(true);
				success = false;
				client.connect(hostname);
				client.login(username, password);
			}
		}while(failedAttempt < counter_timeout && !success);
		return p;
	}
	
	public File getViruses() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException
	{
		File p = new File("FTP" + App.FILE_SEP + virusFileName);
		int failedAttempt = 0;
		Boolean success = false;
		do 
		{
			try
			{
				System.out.println(failedAttempt);
				success = true;
				failedAttempt++;
				client.download(virusFileName, p);
			}
			catch(Exception e)
			{
				client.disconnect(true);
				success = false;
				client.connect(hostname);
				client.login(username, password);
			}
		}while(failedAttempt < counter_timeout && !success);
		return p;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getGenDirectory() {
		return genDirectory;
	}

	public void setGenDirectory(String genDirectory) {
		this.genDirectory = genDirectory;
	}
}
