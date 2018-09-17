package com;

import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.service.FileService;
import com.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;


@org.springframework.stereotype.Controller
public class Controller {

    private StorageService storageService;
    private FileService fileService;
    private ObjectMapper mapper;

    @Autowired
    public Controller(StorageService storageService, FileService fileService) {
        this.storageService = storageService;
        this.fileService = fileService;
        this.mapper = new ObjectMapper();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/put", produces = "text/plain")
    @ResponseBody
    public String put(HttpServletRequest req) throws InternalServerError, BadRequestException {
        try {
            return fileService.put(mapToFile(req)).toString();
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transferFile", produces = "text/plain")
    @ResponseBody
    public String transferFile(@RequestParam long fromId, @RequestParam long toId, @RequestParam long id) throws InternalServerError, BadRequestException {
        try {
            return fileService.transferFile(fromId,toId,id);
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
            fileService.delete(id);
            message = "Item with id :"+id+" is deleted.";
            return message;
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    private File mapToFile(HttpServletRequest req) throws BadRequestException {
        try {
            return mapper.readValue(
                    mapper.writeValueAsString(mapper.readTree(req.getInputStream()).path("file")),
                    new TypeReference<File>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("Internal server error");
        }
    }
}




