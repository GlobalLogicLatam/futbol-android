package com.globallogic.futbol.example.data.strategies;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.example.data.analyzers.GetDeviceHttpAnalyzer;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDeviceFromHttp extends StrategyHttpMock {
    public GetDeviceFromHttp(Operation anOperation, Integer id) {
        super(anOperation, new GetDeviceHttpAnalyzer(), 0f);
        try {
            add(new StrategyHttpResponse(HttpURLConnection.HTTP_OK, String.format(OperationHelper.assetsReader(OperationApp.getInstance(), "json/GetDeviceOperation_1.json"), id)));
        } catch (IOException ignored) {
        }
    }
}
