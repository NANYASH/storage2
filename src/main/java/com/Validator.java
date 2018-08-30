package com;

import com.entity.File;
import com.exception.BadRequestException;

import java.util.List;

public class Validator {

    public static void validateId(long storageId, File file) throws BadRequestException {
        if (storageId!=file.getStorage().getId())
            throw new BadRequestException("File with id: "+file.getId()+" does not exist in the storage with id: "+storageId);
    }

    public static void validateFormat(long storageId,long fileOrStorageId,String storageFormat, String format) throws BadRequestException {
        if (!storageFormat.equals(format))
        throw new BadRequestException("Unsupported file format id: " + fileOrStorageId + ", file(s) cannot be added to the storage with id:" + storageId);
    }

    public static void validateStorageSize(long storageToId,long storageToSize,List<Long> storageFromUnAvailable, long fileId, long fileSize) throws BadRequestException {
        if (storageToSize - checkSize(storageFromUnAvailable) < fileSize)
            throw new BadRequestException("Storage with id: " + storageToId + " is overloaded and file with id: " + fileId + " cannot be added.");
    }

    public static void validateStorageSize(long storageToId,long storageToSize,List<Long> storageToUnavailable, List<Long> storageToRequired) throws BadRequestException {
        if (storageToSize - checkSize(storageToUnavailable) < checkSize(storageToRequired))
            throw new BadRequestException("Storage with id: " + storageToId+"is overloaded ");
    }

    public static long checkSize(List<Long> size) {
        long unavailableStorage = 0;
        for (Long fileSize :size) {
            if (fileSize != null)
                unavailableStorage += fileSize;
        }
        return unavailableStorage;
    }

}
