package com.service.serviceImpl;

import com.dao.StorageDAO;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StorageServiceImpl implements StorageService{

    private StorageDAO storageDAO;

    @Autowired
    public StorageServiceImpl(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    @Override
    public String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError {
        return "Rows updated : " + storageDAO.transferAll(storageFromId,storageToId);
    }
}
