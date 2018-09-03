package com;


import com.dao.FileDAO;
import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;

public class FileService {
    @Autowired
    private FileDAO fileDAO;

    public File put(File file) throws BadRequestException, InternalServerError {
        return fileDAO.save(file);
    }

    public String transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError {
        return "Rows updated : "+fileDAO.transferFile(storageFromId,storageToId,fileId);
    }

    public void delete(Long fileId) throws BadRequestException, InternalServerError {
        fileDAO.delete(fileId);
    }

}
