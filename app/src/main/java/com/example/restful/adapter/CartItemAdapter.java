package com.example.restful.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restful.enitities.CartItem;
import com.example.restful.R;
import com.example.restful.guiuser.CartActivity;

import java.util.List;
public class CartItemAdapter extends ArrayAdapter<CartItem> {
    private List<CartItem> cartItemList;
    private LayoutInflater inflater;
    private OnQuantityChangeListener mListener; // Định nghĩa biến mListener

    // Interface OnQuantityChangeListener
    public interface OnQuantityChangeListener {
        void onQuantityChange(int positionz, int newQuantity);
    }

    // Hàm khởi tạo
    public CartItemAdapter(Context context, List<CartItem> cartItemList, OnQuantityChangeListener listener) {
        super(context, 0, cartItemList);
        this.cartItemList = cartItemList;
        this.inflater = LayoutInflater.from(context);
        this.mListener = listener; // Gán giá trị cho biến mListener
    }

    // Các phương thức khác của Adapter ở đây

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cart_item_layout, parent, false);
            holder = new ViewHolder();
            holder.productImageView = convertView.findViewById(R.id.product_image);
            holder.productNameTextView = convertView.findViewById(R.id.product_name);
            holder.productPriceTextView = convertView.findViewById(R.id.product_price);
            holder.quantityTextView = convertView.findViewById(R.id.quantity);
            holder.deffTextView = convertView.findViewById(R.id.deff);
            holder.buffTextView = convertView.findViewById(R.id.buff);
            holder.removeButton = convertView.findViewById(R.id.remove_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CartItem cartItem = cartItemList.get(position);
        holder.productNameTextView.setText(cartItem.getProductName());
        holder.productPriceTextView.setText(String.valueOf(cartItem.getProductPrice()));
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity())); // Hiển thị số lượng sản phẩm

        // Load image using Glide
        Glide.with(convertView).load(cartItem.getProductImage()).into(holder.productImageView);

        // Xử lý sự kiện khi nhấn nút "deff" (giảm số lượng)
        holder.deffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = cartItem.getQuantity();
                if (currentQuantity > 1) {
                    mListener.onQuantityChange(position, -1); // Giảm số lượng
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút "buff" (tăng số lượng)
        holder.buffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onQuantityChange(position, 1); // Tăng số lượng
                }
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa mục tại vị trí position
                cartItemList.remove(position);
                notifyDataSetChanged(); // Cập nhật giao diện
                // Cập nhật tổng số lượng mặt hàng sau khi xóa
                updateCartItemCount();
                // Cập nhật tổng tiền
                updateCartSummary();
            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView deffTextView;
        TextView buffTextView;
        TextView quantityTextView;
        Button removeButton;

    }

    private void updateCartItemCount() {
        if (getContext() instanceof CartActivity) {
            ((CartActivity) getContext()).updateCartItemCount();
        }
    }

    private void updateCartSummary() {
        if (getContext() instanceof CartActivity) {
            ((CartActivity) getContext()).updateCartSummary();
        }
    }

}
