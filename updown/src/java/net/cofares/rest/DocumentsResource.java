/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cofares.rest;

import com.dvmr.poc.exception.NotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cofares.service.BlobService;
import net.cofares.service.impl.BlobServiceImpl;
import net.cofares.util.MultipartUtil;
import net.cofares.xmlEntities.FileBean;

/**
 * REST Web Service
 *
 * @author pascalfares
 */
@Path("documents")
public class DocumentsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DocumentsResource
     */
    public DocumentsResource() {
    }

    private final BlobService blobService = new BlobServiceImpl();

    public BlobService getBlobService() {
        return blobService;
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@Context HttpServletRequest request,
            @Context HttpServletResponse res) throws Exception {
        String response = "Unable to attach files";
        FileBean bean = MultipartUtil.parseMultipart(request, getBlobService());
        if (null != bean) {
            response = "{\"name\":\"" + bean.getFilename() + "\",\"type\":\""
                    + bean.getContentType() + "\",\"size\":\"" + bean.getSize()
                    + "\"}";
        }
        return Response.ok(response).build();
    }

    /**
     * In Memory solution
     *
     * @param blobKey
     * @return
     * @throws Exception
     */
    @GET
    @Path("download/{blobKey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadFile(
            @PathParam(value = "blobKey") String blobKey) throws IOException {

        byte[] docStream = getBlobService().getBlob(blobKey);
        return docStream;
        //return blobKey.getBytes();
    }

    /**
     * list all valid files in a directory
     *
     * @return
     * @throws JSONException
     */
    @GET
    @Path("list")
    @Produces({MediaType.APPLICATION_JSON})
    public List<FileBean> listFiles() {

        List<FileBean> list = getBlobService().getBlobs();

        return list;
    }

    /**
     * remove file from directory
     *
     * @param blobKey
     * @return
     */
    @GET
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteFile(@DefaultValue("empty") @QueryParam(value = "blobKey") String blobKey) {

        if (blobKey.equals("empty")) {
            throw new NotFoundException("blobKey cannot be empty!");
        }

        getBlobService().deleteBlob(blobKey);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getDirectoryLoc() {
        return blobService.getDirctoryLoation();
    }
}
