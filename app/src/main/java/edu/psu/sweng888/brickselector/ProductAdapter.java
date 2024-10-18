package edu.psu.sweng888.brickselector;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;
    private Set<Product> selectedProducts = new HashSet<>();  // Tracking selected products

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
        // Bind the view with the product and set the checkbox state
        holder.bind(product, listener, selectedProducts.contains(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to get the selected products
    public Set<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, seller, price;
        ImageView imageView;
        CheckBox checkBox;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDescription);
            seller = itemView.findViewById(R.id.productSeller);
            price = itemView.findViewById(R.id.productPrice);
            imageView = itemView.findViewById(R.id.productImage);
            checkBox = itemView.findViewById(R.id.productCheckBox);
        }

        public void bind(Product product, OnItemClickListener listener, boolean isSelected) {
            name.setText(product.getName());
            description.setText(product.getDescription());
            seller.setText(product.getSeller());
            price.setText(String.valueOf(product.getPrice()));
            checkBox.setChecked(isSelected);  // Set the checkbox state

            // Handle checkbox clicks directly within the adapter
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedProducts.add(product);  // Add to selected products
                } else {
                    selectedProducts.remove(product); // Remove from selected products
                }
            });

            // Convert byte array to Bitmap for the product image
            byte[] image = product.getImage();
            if (image != null && image.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            } else {
                // Set a default image if no image is available
                imageView.setImageResource(R.drawable.default_image);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(product));  // Handle item clicks
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