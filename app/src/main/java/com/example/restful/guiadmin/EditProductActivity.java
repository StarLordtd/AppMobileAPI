package com.example.restful.guiadmin;

import com.example.restful.R;
import com.example.restful.enitities.Product;
import com.example.restful.services.ProductService;

import android.app.Activity;
import android.os.Bundle;
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


public class EditProductActivity extends AppCompatActivity {
    private Button btnUpdateProduct;
    private EditText editTextProductID, editTextProductName, editTextProductImage, editTextProductPrice, editTextProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextProductID = findViewById(R.id.id_product);
        editTextProductName = findViewById(R.id.product_name_edittext);
        editTextProductImage = findViewById(R.id.image_link_edittext);
        editTextProductPrice = findViewById(R.id.product_price_edittext);
        editTextProductDetail = findViewById(R.id.detail_edittext);
        btnUpdateProduct = findViewById(R.id.save_button);

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
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

    private void updateProduct() {
        int productId = Integer.parseInt(editTextProductID.getText().toString());
        String productName = editTextProductName.getText().toString();
        double productPrice = Double.parseDouble(editTextProductPrice.getText().toString());
        String productImage = editTextProductImage.getText().toString();
        String productDetail = editTextProductDetail.getText().toString();

        Product product = new Product(productId, productName, productPrice, productImage, productDetail);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.updateProduct(productId, product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProductActivity.this, "Product update successful", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(EditProductActivity.this, "Unable to update product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditProductActivity.this, "Connect error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}