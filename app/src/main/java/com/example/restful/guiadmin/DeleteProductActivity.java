package com.example.restful.guiadmin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restful.R;
import com.example.restful.services.ProductService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DeleteProductActivity extends AppCompatActivity {
    private Button btnDelete;
    private EditText delteTextProductID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        btnDelete = findViewById(R.id.btn_delete_product);
        delteTextProductID = findViewById(R.id.id_delete);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId = Integer.parseInt(delteTextProductID.getText().toString());

                deleteProduct(productId);
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

    private void deleteProduct(int productId){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<Void> call = productService.deleteProduct(productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DeleteProductActivity.this, "Product deletion successful", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(DeleteProductActivity.this, "Product cannot be deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DeleteProductActivity.this, "Connect error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
