package com.utaste.data.network;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenFoodFactsService {
    @GET("api/v2/product/{barcode}")
    Call<ProductResponse> getProduct(@Path("barcode") String barcode);

    class ProductResponse {
        @SerializedName("status") public int status;
        @SerializedName("product") public Product product;
    }
    class Product {
        @SerializedName("product_name") public String productName;
        @SerializedName("nutriments") public Nutriments nutriments;
        @SerializedName("code") public String code;
    }
    class Nutriments {
        @SerializedName("energy-kcal_100g") public Double energyKcal100g;
        @SerializedName("carbohydrates_100g") public Double carbs100g;
        @SerializedName("proteins_100g") public Double proteins100g;
        @SerializedName("fat_100g") public Double fat100g;
        @SerializedName("salt_100g") public Double salt100g;
    }
}
