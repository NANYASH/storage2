package com;

import com.dao.FileDAO;
import com.dao.StorageDAO;
import com.entity.File;
import com.entity.Storage;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StorageService {

    @Autowired
    private FileDAO fileDAO;
    @Autowired
    private StorageDAO storageDAO;


    public File put(File file) throws BadRequestException, InternalServerError {
        return fileDAO.save(file);
    }

    public boolean transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError {
        return storageDAO.transferFile(storageFromId,storageToId,fileId);
    }

    public String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError {
        storageDAO.transferAll(storageFromId,storageToId);
        return "Transfer is done";
    }

    public void delete(Long fileId) throws BadRequestException, InternalServerError {
        fileDAO.delete(fileId);
    }


}
