package com.delta.kylintask.kylinclient;


import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.exception.KylinException;
import com.delta.kylintask.service.KylinService;
import net.bytebuddy.implementation.bytecode.Throw;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class HttpClientFactory {
    @Autowired
    private KylinService kylinService;
    private Map<String, KylinClient> clientPool = new HashMap<>();

    public KylinClient getKylinClient(Kylin kylin){
        SimpleKylinClient simpleKylinClient = (SimpleKylinClient) clientPool.get(kylin.hashCode());
        if (null == simpleKylinClient) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .callTimeout(25, TimeUnit.SECONDS)
                    .connectTimeout(25, TimeUnit.SECONDS)
                    .build();
            simpleKylinClient = new SimpleKylinClient();
            simpleKylinClient.setKylin(kylin);
            simpleKylinClient.setOkHttpClient(okHttpClient);
            clientPool.put(String.valueOf(kylin.hashCode()), simpleKylinClient);
        }
        return simpleKylinClient;
    }

    public KylinClient getKylinClient(String kylinid){
        Optional<KylinClient> kylinClient = Optional.ofNullable(clientPool.get(kylinid));
        if (!kylinClient.isPresent()) {
            ServerResponse<Kylin> kylinServerResponse = kylinService.selectById(kylinid);
            if (kylinServerResponse.isSuccess()) {
                return getKylinClient(kylinServerResponse.getData());
            }
            throw new KylinException("Kylin client is null, please connect first.");
        }
        return kylinClient.get();
    }
}
