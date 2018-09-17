package com.service;

import com.exception.BadRequestException;
import com.exception.InternalServerError;

public interface StorageService {

     String transferAll(Long storageFromId, Long storageToId) throws BadRequestException, InternalServerError;

}
