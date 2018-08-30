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
        Storage storageFromDB = storageDAO.findStorageFieldsById(file.getStorage().getId());

        Validator.validateId(storageFromDB.getId(), file);
        Validator.validateFormat(storageFromDB.getId(),
                file.getId(),storageFromDB.getFormatsSupported(),file.getFormat());
        Validator.validateStorageSize(storageFromDB.getId(),storageFromDB.getStorageSize(),
                fileDAO.findSizeByStorageId(storageFromDB.getId()),file.getId(), file.getSize());

        return fileDAO.save(file);
    }

    public File transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError {
        File file = fileDAO.findById(File.class, fileId);
        Storage storageTo = storageDAO.findStorageFieldsById(storageToId);

        Validator.validateId(storageFromId, file);
        Validator.validateFormat(storageTo.getId(), file.getId(),
                storageTo.getFormatsSupported(),file.getFormat());
        Validator.validateStorageSize(storageTo.getId(),storageTo.getStorageSize(),
                fileDAO.findSizeByStorageId(storageToId),file.getId(),file.getSize());

        file.setStorage(storageTo);
        return fileDAO.update(file);
    }

    public String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError {
        Storage storageFrom = storageDAO.findStorageFieldsById(storageFromId);
        Storage storageTo = storageDAO.findStorageFieldsById(storageToId);

        Validator.validateStorageSize(storageTo.getId(),storageTo.getStorageSize(),
                fileDAO.findSizeByStorageId(storageToId),fileDAO.findSizeByStorageId(storageFromId));

        Validator.validateFormat(storageFrom.getId(), storageTo.getId(),
                storageFrom.getFormatsSupported(),storageTo.getFormatsSupported());

        storageDAO.transferAll(storageFrom.getId(),storageTo.getId());
        return "Transfer is done";
    }

    public void delete(Long fileId) throws BadRequestException, InternalServerError {
        fileDAO.delete(fileId);
    }


}
