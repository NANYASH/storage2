package com;

import com.dao.FileDAO;
import com.entity.File;
import com.entity.Storage;
import com.exception.InternalServerError;

import java.util.Arrays;

public class Demo {
    public static void main(String[] args) throws InternalServerError {
        FileDAO fileDAO = new FileDAO();
        File file = fileDAO.findById(File.class,15);
        Storage storage = new Storage();
        storage.setFormatsSupported("txt");
        storage.setStorageCountry("Kiev");
        storage.setId(1);;
        file.setName("Test");

        fileDAO.update(file);
    }
}
