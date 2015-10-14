package com.globallogic.futbol.core.operation.strategies;

import com.globallogic.futbol.core.OperationResponse;

import java.io.File;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class FileOperationResponse extends OperationResponse<Integer, File> {
    public FileOperationResponse(Integer aHttpCode, File aFile) {
        super(aHttpCode, aFile);
    }
}
