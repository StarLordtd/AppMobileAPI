package com.example.restful.guiadmin;
import com.example.restful.R;
import com.example.restful.enitities.Product;
import com.example.restful.services.ProductService;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends AppCompatActivity {
    private EditText editTextProductName, editTextProductImage, editTextProductPrice, editTextProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextProductName = findViewById(R.id.product_name_edittext);
        editTextProductImage = findViewById(R.id.image_link_edittext);
        editTextProductPrice = findViewById(R.id.product_price_edittext);
        editTextProductDetail = findViewById(R.id.detail_edittext);


        Button btnSaveProduct = findViewById(R.id.save_button);
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveProduct();
            }
        });

        Button btnExit = findViewById(R.id.back_to_home_button);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void saveProduct() {
        String productName = editTextProductName.getText().toString();
        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());
        String productImage = editTextProductImage.getText().toString();
        String productDetail = editTextProductDetail.getText().toString();


        Product newProduct = new Product(0, productName, productPrice, productImage, productDetail);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.addProduct(newProduct);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("AddProductActivity", "Response received");
                if (response.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Add product successfully", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();


                } else {
                    Toast.makeText(AddProductActivity.this, "Unable to add product", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Connect erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}