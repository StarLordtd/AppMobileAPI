package com.example.restful.services;
import com.example.restful.enitities.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface ProductService {
    @GET("products")
    Call<List<Product>> getProductFromRestAPI();
    @POST("products")
    Call<Void> addProduct(@Body Product product);
    @PUT("products/{id}")
    Call<Void> updateProduct(@Path("id") int productId, @Body Product product);
    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int productId);
}


