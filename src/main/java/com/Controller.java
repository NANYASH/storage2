package com;

import com.entity.File;
import com.entity.Storage;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;


@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    StorageService storageService;
    private ObjectMapper mapper = new ObjectMapper();


    @RequestMapping(method = RequestMethod.POST, value = "/put", produces = "text/plain")
    @ResponseBody
    public String put(HttpServletRequest req) throws InternalServerError, BadRequestException {
        try {
            return storageService.put(mapToFile(req.getInputStream())).toString();
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerError("Internal server error");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transferFile", produces = "text/plain")
    @ResponseBody
    public String transferFile(HttpServletRequest req) throws InternalServerError, BadRequestException {
        ByteArrayInputStream inputStreamArray;
        Long storageFromId;
        Long storageToId;
        Long fileId;
        try {
            inputStreamArray = getInputStreamArray(req.getInputStream());
            storageFromId = mapToId(inputStreamArray,"storageFromId");
            inputStreamArray.reset();

            storageToId = mapToId(inputStreamArray,"storageToId");
            inputStreamArray.reset();

            fileId = mapToId(inputStreamArray,"fileId");
            return storageService.transferFile(storageFromId,storageToId,fileId).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerError("Internal server error");
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/transferAll", produces = "text/plain")
    @ResponseBody
    public String transferAll(HttpServletRequest req) throws InternalServerError, BadRequestException {
        ByteArrayInputStream inputStreamArray;
        Long storageFromId;
        Long storageToId;
        try {
            inputStreamArray = getInputStreamArray(req.getInputStream());
            storageFromId = mapToId(inputStreamArray,"storageFromId");
            inputStreamArray.reset();
            storageToId = mapToId(inputStreamArray,"storageToId");
            inputStreamArray.reset();
            return storageService.transferAll(storageFromId,storageToId).toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerError("Internal server error");
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete", produces = "text/plain")
    @ResponseBody
    public String delete(@RequestParam Long id) throws Exception {
        String message;
        try {
            storageService.delete(id);
            message = "Item with id :"+id+" is deleted.";
            return message;
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }



    private File mapToFile(InputStream stream) throws IOException {
        return mapper.readValue(
                mapper.writeValueAsString(mapper.readTree(stream).path("file")),
                new TypeReference<File>() {
                });


    }
    private Storage mapToStorage(InputStream stream,String node) throws IOException {
        return mapper.readValue(
                mapper.writeValueAsString(mapper.readTree(stream).path(node)),
                new TypeReference<Storage>() {
                });
    }

    private Long mapToId(InputStream stream,String node) throws IOException {
        return mapper.readValue(
                mapper.writeValueAsString(mapper.readTree(stream).path(node)),
                new TypeReference<Long>() {
                });
    }

    private ByteArrayInputStream getInputStreamArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);
        byte[] bytes = baos.toByteArray();
        return new ByteArrayInputStream(bytes);
    }







    /*private Storage mapToStorage(InputStream stream) throws IOException {
        return mapper.readValue(
                mapper.writeValueAsString(mapper.readTree(req.getInputStream()).path("storage")),
                new TypeReference<Storage>() {
                });
    }*/

    /*private File mapToFile(HttpServletRequest req) throws IOException {
      JsonParser jsonParser = mapper.getFactory().createParser(req.getInputStream());
      File file = mapper.readValue(jsonParser, File.class);
      return file;
  }*/

    /*public String put(HttpServletRequest req) throws InternalServerError, BadRequestException {
        String message;
        ByteArrayInputStream bais;
        File file;
        Storage storage;
        try {
            bais = getInputStreamArray(req.getInputStream());
            file = mapToFile(bais);
            bais.reset();
            storage = mapToStorage(bais);
            message = storageService.put(file,storage).toString();
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerError("Internal server error");
        }
        return message;

    }*/




}
