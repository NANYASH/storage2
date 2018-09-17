package com.service.serviceImpl;


import com.dao.FileDAO;
import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;
import com.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

    private FileDAO fileDAO;

    @Autowired
    public FileServiceImpl(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }

    @Override
    public File put(File file) throws BadRequestException, InternalServerError {
        return fileDAO.save(file);
    }

    @Override
    public String transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError {
        return "Rows updated : "+fileDAO.transferFile(storageFromId,storageToId,fileId);
    }

    @Override
    public void delete(Long fileId) throws BadRequestException, InternalServerError {
        fileDAO.delete(fileId);
    }

}
