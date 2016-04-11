package com.globallogic.futbol.example.data.strategies;

import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.operations.OperationHelper;
import com.globallogic.futbol.core.strategies.mock.StrategyHttpMock;
import com.globallogic.futbol.core.responses.StrategyHttpResponse;
import com.globallogic.futbol.example.data.analyzers.GetDevicesHttpAnalyzer;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicesFromHttp extends StrategyHttpMock {
    public GetDevicesFromHttp(Operation anOperation) {
        super(anOperation, new GetDevicesHttpAnalyzer(), 0f);
        try {
            add(new StrategyHttpResponse(HttpURLConnection.HTTP_OK, OperationHelper.assetsReader(OperationApp.getInstance(), "json/GetDevicesOperation_1.json")));
        } catch (IOException ignored) {
        }
    }
}
