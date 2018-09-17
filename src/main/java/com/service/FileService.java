package com.service;


import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;

public interface FileService {

     File put(File file) throws BadRequestException, InternalServerError;

     String transferFile(Long storageFromId, Long storageToId, Long fileId) throws BadRequestException, InternalServerError;

     void delete(Long fileId) throws BadRequestException, InternalServerError;
}
