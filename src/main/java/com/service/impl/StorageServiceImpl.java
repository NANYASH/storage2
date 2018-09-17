package com.service.impl;

import com.dao.FileDAO;
import com.dao.StorageDAO;
import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StorageServiceImpl{

    private StorageDAO storageDAO;

    @Autowired
    public StorageServiceImpl(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    public String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError {
        return "Rows updated : " + storageDAO.transferAll(storageFromId,storageToId);
    }
}
