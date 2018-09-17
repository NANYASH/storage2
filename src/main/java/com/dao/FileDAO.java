package com.dao;

import com.entity.File;
import com.exception.BadRequestException;
import com.exception.InternalServerError;

public interface FileDAO {

    File save(File file) throws InternalServerError;

    int transferFile(long from,long to, long fileId) throws InternalServerError, BadRequestException;

    void delete(long id) throws InternalServerError;
}
