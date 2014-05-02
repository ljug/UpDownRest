package net.cofares.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.cofares.xmlEntities.FileBean;

public interface BlobService {
	/**
	 * upload files to a directory
	 * @param inputStream
	 * @param filename
	 * @return
	 */
	public long uploadBlob(InputStream inputStream, String filename);
	/**
	 * returns the file as a byte[]
	 * @param blobKey
	 * @return
	 */
	public byte[] getBlob(String blobKey)  throws IOException;
	
	/**
	 * Deletes file form the directory
	 * @param blobKey
	 */
	public void deleteBlob(String blobKey);
	
	/**
	 * get all files from the directory
	 * @return
	 */
	public List<FileBean> getBlobs();
        
        public String getDirctoryLoation();
}
