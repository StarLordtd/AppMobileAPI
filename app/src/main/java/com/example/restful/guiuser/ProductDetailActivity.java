package com.example.restful.guiuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restful.R;
import com.example.restful.enitities.CartItem;
import com.example.restful.enitities.Product;
import com.bumptech.glide.Glide;
import com.example.restful.sqlite.Helpers;
import com.example.restful.sqlite.CartDatabaseHelper;
import com.google.gson.Gson;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView cartItemCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String productJson = getIntent().getStringExtra("PRODUCT_JSON");
        Gson gson = new Gson();
        Product product = gson.fromJson(productJson, Product.class);

        boolean updateCartItemCount = getIntent().getBooleanExtra("UPDATE_CART_ITEM_COUNT", false);
        if (updateCartItemCount) {
            updateCartItemCount();
        }
        cartItemCountTextView = findViewById(R.id.cart_item_count_text_view);
        CartDatabaseHelper db = new CartDatabaseHelper(this);
        CartItem cartItem = new CartItem(product.getId(), product.getName(), product.getPrice(), product.getImage(), 1);
        db.addCartItem(cartItem);


        TextView nameTextView = findViewById(R.id.product_name);
        TextView priceTextView = findViewById(R.id.product_price);
        ImageView imageView = findViewById(R.id.product_image);
        TextView descriptionTextView = findViewById(R.id.product_description);
        Button btnAddToCart = findViewById(R.id.add_to_cart_button);
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton cartButton = findViewById(R.id.cart_button);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameTextView.setText(product.getName());
        priceTextView.setText(String.valueOf(product.getPrice()));
        descriptionTextView.setText(product.getDetail().replace("\\n", "\n"));
        Glide.with(this).load(product.getImage()).into(imageView);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isProductExistInCart = false;
                for (CartItem item : Helpers.manggiohang) {
                    if (item.getProductId() == product.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        isProductExistInCart = true;
                        break;
                    }
                }
                if (!isProductExistInCart) {
                    CartItem cartItem = new CartItem(product.getId(), product.getName(), product.getPrice(), product.getImage(), 1);
                    Helpers.manggiohang.add(cartItem);
                }
                showToast("Added to cart");

                updateCartItemCount();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateCartItemCount();
    }

    private void updateCartItemCount() {
        int cartItemCount = Helpers.manggiohang.size();
        cartItemCountTextView.setText(String.valueOf(cartItemCount));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
