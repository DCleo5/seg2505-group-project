package com.utaste.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public final class OpenFoodFactsClient {
    private final OpenFoodFactsService service;

    public OpenFoodFactsClient() {
        OkHttpClient ok = new OkHttpClient.Builder()
                .connectTimeout(7, TimeUnit.SECONDS)
                .readTimeout(7, TimeUnit.SECONDS)
                .build();
        Retrofit rt = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ok)
                .build();
        service = rt.create(OpenFoodFactsService.class);
    }
    public OpenFoodFactsService svc() { return service; }
}
