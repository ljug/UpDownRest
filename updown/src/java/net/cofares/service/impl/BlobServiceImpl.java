package net.cofares.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;

import net.cofares.xmlEntities.FileBean;
import net.cofares.service.BlobService;


/**
 * 
 * @author vreddy.fp
 *
 */
public class BlobServiceImpl implements BlobService {


	public long uploadBlob(InputStream inputStream, String filename) {
		FileOutputStream output = null;
		CountingInputStream countingInputStream = null;
		long filesize = 0;
		try {
			// if uploading form IE it get complete path
			filename = FilenameUtils.getName(filename);
			//output = new FileWriter(new File(getDirctoryLoation()+ filename));
			//IOUtils.copy(countingInputStream, output);
			output = new FileOutputStream(new File(getDirctoryLocation()+ filename));
			countingInputStream = new CountingInputStream(inputStream);
			IOUtils.copyLarge(countingInputStream, output);
			filesize = countingInputStream.getByteCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(countingInputStream);
			IOUtils.closeQuietly(output);
		}
		return filesize;
	}

	/**
	 * 
	 */
	public byte[] getBlob(String blobKey) throws IOException {
		File file = new File(getDirctoryLocation()+ blobKey);
		byte[] docStream = null;
		docStream = FileUtils.readFileToByteArray(file);		
		return docStream;
	}

	public void deleteBlob(String blobKey) {
		FileUtils.deleteQuietly(new File(getDirctoryLocation()+ blobKey));
	}

	/**
	 * 
	 */
	public List<FileBean> getBlobs() {
	
		List<FileBean> list = new ArrayList<FileBean>();
		Iterator<File> files = FileUtils.iterateFiles(
				new File(getDirctoryLocation()),
				getValidFileExtentions(),
				false);

		while (files.hasNext()) {
			File file = files.next();
			FileBean fileBean = new FileBean(
					file.getName(), 
					file.length(),
					file.getAbsolutePath(), 
					FilenameUtils.getExtension(file.getName()));
			list.add(fileBean);
		}
		return list;
	}

	public String getDirctoryLocation() {
		return net.cofares.resources.Config.directoryLocation;
	}

	public String[] getValidFileExtentions() {
		return net.cofares.resources.Config.validFileExtentions;
	}

}
