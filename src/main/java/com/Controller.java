package com;

import com.entity.File;
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
    public boolean transferFile(@RequestParam long fromId, @RequestParam long toId, @RequestParam long id) throws InternalServerError, BadRequestException {
        try {
            return storageService.transferFile(fromId,toId,id);
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/transferAll", produces = "text/plain")
    @ResponseBody
    public String transferAll(@RequestParam Long fromId,@RequestParam Long toId) throws InternalServerError, BadRequestException {
        try {
            return storageService.transferAll(fromId,toId).toString();
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

    private File mapToFile(InputStream stream) throws BadRequestException {
        try {
            return mapper.readValue(
                    mapper.writeValueAsString(mapper.readTree(stream).path("file")),
                    new TypeReference<File>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("Internal server error");
        }
    }
}




