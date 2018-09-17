package com.dao;


import com.exception.BadRequestException;
import com.exception.InternalServerError;

public interface StorageDAO {

     int transferAll(long from,long to) throws InternalServerError, BadRequestException;

     void delete(long id) throws InternalServerError;
}
