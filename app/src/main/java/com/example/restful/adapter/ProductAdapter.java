package com.example.restful.adapter;
import com.example.restful.R;
import com.example.restful.enitities.Product;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bumptech.glide.Glide;
import com.example.restful.guiuser.ProductDetailActivity;
import com.google.gson.Gson;


public class ProductAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private int mResource;
    public ProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.product_id);
            holder.nameTextView = convertView.findViewById(R.id.product_name);
            holder.priceTextView = convertView.findViewById(R.id.product_price);
            holder.imageView = convertView.findViewById(R.id.product_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product product = getItem(position);
        holder.idTextView.setText(String.valueOf(product.getId()));
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));
        Glide.with(mContext)
                .load(product.getImage())
                .into(holder.imageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                Gson gson = new Gson();
                String productJson = gson.toJson(product);
                intent.putExtra("PRODUCT_JSON", productJson);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView priceTextView;
        ImageView imageView;

    }
}