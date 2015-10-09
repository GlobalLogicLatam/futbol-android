package com.globallogic.futbol.core.operation;

import com.globallogic.futbol.core.OperationResponse;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public class HttpOperationResponse extends OperationResponse<String> {
    public HttpOperationResponse(int aHttpCode, String aString) {
        super(aHttpCode, aString);
    }
}
