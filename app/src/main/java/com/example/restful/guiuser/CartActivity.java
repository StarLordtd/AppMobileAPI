package com.example.restful.guiuser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restful.R;
import com.example.restful.adapter.CartItemAdapter;
import com.example.restful.enitities.CartItem;
import com.example.restful.sqlite.Helpers;
import com.example.restful.sqlite.CartDatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnQuantityChangeListener {

    private TextView subtotalTextView;
    private TextView totalTaxTextView;
    private TextView totalTextView;
    private ListView cartListView;
    private CartItemAdapter cartItemAdapter;
    private EditText fullNameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private CartDatabaseHelper cartDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private static final String PREF_CART = "cart_prefs";
    private static final String KEY_CART_ITEMS = "cart_items";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        sharedPreferences = getSharedPreferences(PREF_CART, MODE_PRIVATE);

        subtotalTextView = findViewById(R.id.subtotal_textview);
        totalTaxTextView = findViewById(R.id.total_tax_textview);
        totalTextView = findViewById(R.id.total_textview);
        cartListView = findViewById(R.id.cart_list);
        fullNameEditText = findViewById(R.id.fullname_edittext);
        phoneEditText = findViewById(R.id.phone_edittext);
        addressEditText = findViewById(R.id.address_edittext);
        cartDatabaseHelper = new CartDatabaseHelper(this);
        cartItemAdapter = new CartItemAdapter(this, Helpers.manggiohang, this);
        cartListView.setAdapter(cartItemAdapter);
        restoreCart();

        Button checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (fullName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    removeAllItemsFromCart();
                    Toast.makeText(CartActivity.this, "The request has been sent", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });



        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        updateCartSummary();
    }
    private void removeAllItemsFromCart() {
        Helpers.manggiohang.clear();
        cartItemAdapter.notifyDataSetChanged();
    }


    private int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem item : Helpers.manggiohang) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    private void removeFromCart(int position) {
        Helpers.manggiohang.remove(position);
        updateCartSummary();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCart();
    }

    private void saveCart() {
        for (CartItem item : Helpers.manggiohang) {
            cartDatabaseHelper.addCartItem(item);
        }
    }

    public void updateCartSummary() {
        int totalQuantity = calculateTotalQuantity();
        double subtotal = calculateSubtotal();
        double totalTax = calculateTotalTax(totalQuantity);
        double total = subtotal + totalTax;

        subtotalTextView.setText(String.valueOf(subtotal));
        totalTaxTextView.setText(String.valueOf(totalTax));
        totalTextView.setText(String.format("%.2f", total));
    }

    public void updateCartItemCount() {
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        List<CartItem> cartItems = Helpers.manggiohang;
        for (CartItem item : cartItems) {
            subtotal += item.getProductPrice() * item.getQuantity();
        }
        return subtotal;
    }

    private double calculateTotalTax(int totalQuantity) {
        double taxRatePerProduct = 0.002;
        double totalTax = calculateSubtotal() * taxRatePerProduct * totalQuantity;
        return Math.round(totalTax * 100.0) / 100.0;
    }

    private void restoreCart() {
        Gson gson = new Gson();
        String jsonCart = sharedPreferences.getString(KEY_CART_ITEMS, "");
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(jsonCart, type);

        if (cartItems != null) {
            Helpers.manggiohang.addAll(cartItems);
            cartItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onQuantityChange(int position, int newQuantity) {
        CartItem cartItem = Helpers.manggiohang.get(position);
        int oldQuantity = cartItem.getQuantity();
        int updatedQuantity = oldQuantity + newQuantity;

        if (updatedQuantity <= 0) {
            removeFromCart(position);
        } else {
            cartItem.setQuantity(updatedQuantity);
            updateCartItemCount();
            updateCartSummary();
            cartItemAdapter.notifyDataSetChanged();
        }
    }

}
