package com;

import com.dao.FileDAO;
import com.entity.File;
import com.entity.Storage;
import com.exception.BadRequestException;

public class Validator {
    public static boolean validateAll(Storage storage, File file) throws BadRequestException {
        validateId(storage, file);
        validateFormat(storage, file);
        validateStorageSize(storage, file);
        return true;
    }

    public static void validateId(Storage storage, File file) throws BadRequestException {
        if (storage.getId()!=file.getStorage().getId())
            throw new BadRequestException("File with id: "+file.getId()+" does not exist in the storage with id: "+storage.getId());
    }

    public static void validateFormat(Storage storage, File file) throws BadRequestException {
        if (!storage.getFormatsSupported().equals(file.getFormat()))
        throw new BadRequestException("Unsupported file format id: " + file.getId() + ", file cannot be added to the storage with id:" + storage.getId());
    }

    public static void validateFormat(Storage storageFrom, Storage storageTo) throws BadRequestException {
        if (!storageFrom.getFormatsSupported().equals(storageFrom.getFormatsSupported()))
            throw new BadRequestException("Unsupported file format id: " + storageFrom.getId() + ", files cannot be added to the storage with id:" + storageTo.getId());
    }

    public static void validateStorageSize(Storage storage, File file) throws BadRequestException {
        if (storage.getStorageSize() - checkSize(storage) < file.getSize())
            throw new BadRequestException("Storage with id: " + storage.getId() + " is overloaded and file with id: " + file.getId() + " cannot be added.");
    }

    public static void validateStorageSize(Storage storageFrom, Storage storageTo) throws BadRequestException {
        if (storageTo.getStorageSize() - checkSize(storageTo) < checkSize(storageFrom))
            throw new BadRequestException("Storage with id: " + storageTo.getId()+"is overloaded ");
    }

    public static long checkSize(Storage storage) {
        long unavailableStorage = 0;
        for (File file : storage.getFiles()) {
            if (file != null)
                unavailableStorage += file.getSize();
        }
        return unavailableStorage;
    }

}
