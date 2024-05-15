package com.example.restful.guiuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.restful.R;
import com.example.restful.adapter.ProductAdapter;
import com.example.restful.enitities.Product;
import com.example.restful.sqlite.Helpers;
import com.example.restful.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView productListView;
    private ViewFlipper viewFlipper;
    private TextView cartItemCountTextView;
    private static final int YOUR_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartItemCountTextView = findViewById(R.id.cart_item_count_text_view);
        ImageButton cartButton = findViewById(R.id.cart_button);
        ImageButton loginButton = findViewById(R.id.admin_manageproduct);

        productListView = findViewById(R.id.product_list);
        ActionViewFlipper();
        fetchProductList();

        if (Helpers.manggiohang == null) {
            Helpers.manggiohang = new ArrayList<>();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        fetchProductList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fetchProductList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCartItemCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCartState();
    }

    private void saveCartState() {

    }

    private void restoreCartState() {

    }

    private void updateCartItemCount() {
        restoreCartState();
        int cartItemCount = Helpers.manggiohang.size();
        cartItemCountTextView.setText(String.valueOf(cartItemCount));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://file.hstatic.net/200000722513/file/pc-slider_b09b86cba7914c59affaaa41df4d38ec.png");
        mangquangcao.add("https://file.hstatic.net/200000722513/file/gearvn-laptop-asus-vivobook-14-oled-m1405ya-km047w-slider_46dfc8f3a021418fb33af9be6052b315.png");
        mangquangcao.add("https://file.hstatic.net/200000722513/file/banner_slide_4_b4d3c996f3e64796b83e224fd13f0479.png");
        mangquangcao.add("https://file.hstatic.net/200000722513/file/loa_xin_slider_55571db8742146cd85eef265cf950b35.png");
        mangquangcao.add("https://file.hstatic.net/200000722513/file/gearvn_800x400_rog_86e915f416414660bd9e38091e622d27.jpg");

        viewFlipper = findViewById(R.id.viewlipper);

        if (viewFlipper != null) {
            for (int i = 0; i < mangquangcao.size(); i++) {
                ImageView imageView = new ImageView(getApplicationContext());
                Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewFlipper.addView(imageView);
            }

            viewFlipper.setFlipInterval(3000);
            viewFlipper.setAutoStart(true);
            Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
            Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
            viewFlipper.setInAnimation(slide_in);
            viewFlipper.setOutAnimation(slide_out);
        } else {
            Log.e("MainActivity", "ViewFlipper not found in layout XML");
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
                    Toast.makeText(MainActivity.this, "Unable to load product list", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connect error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showProductList(List<Product> productList) {
        ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.product_list_item, productList);
        productListView.setAdapter(adapter);
    }

}
