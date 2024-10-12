package edu.psu.sweng888.brickselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, seller, price;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDescription);
            seller = itemView.findViewById(R.id.productSeller);
            price = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.productImage);
        }

        public void bind(Product product, OnItemClickListener listener) {
            name.setText(product.getName());
            description.setText(product.getDescription());
            seller.setText(product.getSeller());
            price.setText(String.valueOf(product.getPrice()));

            // Convert byte array to Bitmap
            byte[] image = product.getImage();
            if (image != null && image.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            } else {
                // Set a default image if no image is available
                imageView.setImageResource(R.drawable.default_image);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }

    public void deleteProduct(Product product) {
        // Remove the product from the list
        productList.remove(product);
        // Notify the adapter that the data has changed to refresh the view
        notifyDataSetChanged();
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }
}