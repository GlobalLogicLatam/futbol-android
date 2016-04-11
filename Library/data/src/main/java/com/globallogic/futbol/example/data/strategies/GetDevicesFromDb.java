package com.globallogic.futbol.example.data.strategies;

import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.strategies.mock.StrategyDbMock;
import com.globallogic.futbol.core.responses.StrategyDbResponse;
import com.globallogic.futbol.example.data.analyzers.GetDevicesDbAnalyzer;
import com.globallogic.futbol.example.data.entities.DeviceEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class GetDevicesFromDb extends StrategyDbMock<ArrayList<DeviceEntity>> {
    public GetDevicesFromDb(Operation anOperation) {
        super(anOperation, new GetDevicesDbAnalyzer(), 0f);

        ArrayList<DeviceEntity> mockedList = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Class<DeviceEntity> aClass = DeviceEntity.class;
        mockedList.add(gson.fromJson("{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"1\",\"name\":\"Samsung S2\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}", aClass));
        mockedList.add(gson.fromJson("{\"createdAt\":\"2015-08-05T11:14:45.374Z\",\"id\":\"2\",\"name\":\"Motorola Moto G\",\"resolution\":\"720x1280\",\"updatedAt\":\"2015-08-05T11:14:45.374Z\"}", aClass));
        add(new StrategyDbResponse<>(mockedList));
    }
}
