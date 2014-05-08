package com.dvmr.poc.rest;

import com.dvmr.poc.bean.FileBean;
import com.dvmr.poc.exception.NotFoundException;
import com.dvmr.poc.service.BlobService;
import com.dvmr.poc.service.impl.BlobServiceImpl;
import com.dvmr.poc.util.MultipartUtil;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

/**
 * This rest based document service
 * @author vreddy.fp
 *
 */

@Path("/document")
public class DocumentService {

	/**
	 * Replace with Inject annotation in JEE6 or with Spring 3.0
	 */
	private final BlobService blobService = new BlobServiceImpl();

	public DocumentService() {
		super();
	}
	
	public BlobService getBlobService() {
		return blobService;
	}


	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FormDataMultiPart form) throws Exception {
		FormDataBodyPart filePart = form.getField("file");
	 
	         filePart.getContentDisposition().getFileName();
	 
	         InputStream fileInputStream = filePart.getValueAs(InputStream.class);
	 
	         String filePath = ;
	 
	        // save the file to the server
	        saveFile(fileInputStream, filePath);
	 
	        String output = "File saved to server location using FormDataMultiPart : " + filePath;
	 
	        return Response.status(200).entity(output).build();
	}

	/**
	 * In Memory solution
	 * 
	 * @param blobKey
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(
			@DefaultValue("empty") @QueryParam(value = "blobKey") String blobKey)
			throws Exception {
		if(blobKey.equals("empty"))
			throw new NotFoundException("blobKey cannot be empty!");
		
		byte[] docStream = getBlobService().getBlob(blobKey);
		return Response
				.ok(docStream, MediaType.APPLICATION_OCTET_STREAM)
				.header("content-disposition", "attachment; filename = " + blobKey).build();
	}
	
	/**
	 * list all valid files in a directory
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("list")
	@Produces( { MediaType.APPLICATION_JSON})
	public JSONArray listFiles() throws JSONException{
		JSONArray arr = new JSONArray();
		List<FileBean> list = getBlobService().getBlobs();
		for(Iterator<FileBean> i = list.iterator(); i.hasNext();){
			arr.put(i.next().toJson());
		}
		return arr;
	}
	
	/**
	 * remove file from directory
	 * @param blobKey
	 * @return
	 */
	@GET
	@Path("delete")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteFile(@DefaultValue("empty") @QueryParam(value = "blobKey") String blobKey){
		
		if(blobKey.equals("empty"))
			throw new NotFoundException("blobKey cannot be empty!");
		
		getBlobService().deleteBlob(blobKey);
		return Response.status(Status.OK).build();
	}
	
}
