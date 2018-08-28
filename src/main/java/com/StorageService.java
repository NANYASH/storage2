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
        Storage storageFromDB = storageDAO.findById(Storage.class, file.getStorage().getId());
        Validator.validateAll(storageFromDB, file);
        return fileDAO.save(file);
    }

    public File transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError {
        File file = fileDAO.findById(File.class, fileId);
        Storage storageFrom = storageDAO.findById(Storage.class, storageFromId);
        Storage storageTo = storageDAO.findById(Storage.class, storageToId);
        Validator.validateId(storageFrom, file);
        Validator.validateAll(storageTo, file);
        file.setStorage(storageTo);
        return fileDAO.update(file);
    }

    public String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError {
        Storage storageFrom = storageDAO.findById(Storage.class, storageFromId);
        Storage storageTo = storageDAO.findById(Storage.class, storageToId);
        Validator.validateStorageSize(storageFrom, storageTo);
        Validator.validateFormat(storageFrom, storageTo);
        storageDAO.transferAll(storageFrom.getId(),storageTo.getId());
        return "Transfer is done";
    }

    public void delete(Long fileId) throws BadRequestException, InternalServerError {
        fileDAO.delete(fileId);
    }


}
