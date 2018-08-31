package com;

import com.entity.File;
import com.exception.BadRequestException;

public class Validator {
    public static void validateId(long storageId, File file) throws BadRequestException {
        if (storageId!=file.getStorage().getId())
            throw new BadRequestException("File with id: "+file.getId()+" does not exist in the storage with id: "+storageId);
    }

}
