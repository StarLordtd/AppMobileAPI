package com.example.restful.guiadmin;

import com.example.restful.R;
import com.example.restful.adapter.ProductAdapter;
import com.example.restful.enitities.Product;
import androidx.annotation.Nullable;

import com.example.restful.guiuser.MainActivity;
import com.example.restful.services.ProductService;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageListProduct extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_PRODUCT = 1;
    private static final int REQUEST_CODE_EDIT_PRODUCT = 2;
    private static final int REQUEST_CODE_DELETE_PRODUCT = 3;
    private static final int REG =4;

    private ListView productListView;
    private Button btn_add;
    private Button btn_edit;
    private  Button btn_delete;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_list_product);

        productListView = findViewById(R.id.product_list);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ManageListProduct.this, MainActivity.class);
                startActivityForResult(myIntent, REG);
            }
        });
        btn_add = findViewById(R.id.add_button);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ManageListProduct.this, AddProductActivity.class);
                startActivityForResult(myIntent, REQUEST_CODE_ADD_PRODUCT);
            }
        });

        btn_edit = findViewById(R.id.edit_button);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListProduct.this, EditProductActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT);
            }
        });
        btn_delete = findViewById(R.id.delete_button);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageListProduct.this, DeleteProductActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DELETE_PRODUCT);
            }
        });

        fetchProductList();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(Activity.RESULT_OK, data);
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_PRODUCT && resultCode == Activity.RESULT_OK) {
            fetchProductList();
        }
        if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == Activity.RESULT_OK) {
            fetchProductList();
        }
        if (requestCode == REQUEST_CODE_DELETE_PRODUCT && resultCode == Activity.RESULT_OK) {
            fetchProductList();
        }
    }

    private void fetchProductList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/HelloWebApp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);

        Call<List<Product>> call = productService.getProductFromRestAPI();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    showProductList(productList);

                } else {
                    Toast.makeText(ManageListProduct.this, "Unable to load product list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ManageListProduct.this, "Connect error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProductList(List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter(ManageListProduct.this, R.layout.product_list_item, productList);
        productListView.setAdapter(adapter);
    }
}
