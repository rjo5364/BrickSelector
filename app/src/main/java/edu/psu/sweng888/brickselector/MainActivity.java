package edu.psu.sweng888.brickselector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    Button btnNext, btnDeleteProduct;
    List<Product> selectedProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views inside onCreate
        recyclerView = findViewById(R.id.recyclerViewProducts);
        btnNext = findViewById(R.id.btnNext);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);  // Button for deleting products
        Button btnAddProduct = findViewById(R.id.btnAddProduct);

        DBHelper dbHelper = new DBHelper(this);
        List<Product> productList = dbHelper.getAllProducts();

        // Set up the adapter and RecyclerView
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Add or remove product from selected list
                if (selectedProducts.contains(product)) {
                    selectedProducts.remove(product);
                } else {
                    selectedProducts.add(product);
                }
            }
        });

        // Handle Refresh button click to reload product list
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            // Refresh the product list from the database
            List<Product> updatedProductList = dbHelper.getAllProducts();
            productAdapter.updateProductList(updatedProductList);
            Toast.makeText(MainActivity.this, "Product list refreshed", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the 'Next' button to proceed if 3 or more products are selected
        btnNext.setOnClickListener(v -> {
            if (selectedProducts.size() < 3) {
                Toast.makeText(MainActivity.this, "You must select 3 or more to proceed", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putParcelableArrayListExtra("selectedProducts", new ArrayList<>(selectedProducts));
                startActivity(intent);
            }
        });

        // Set up the delete button
        btnDeleteProduct.setOnClickListener(v -> {
            if (selectedProducts.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please select a product to delete", Toast.LENGTH_SHORT).show();
            } else {
                for (Product product : selectedProducts) {
                    // Delete the product from the database
                    dbHelper.deleteProduct(product.getId());
                }
                // Update the list after deletion
                productAdapter.updateProductList(dbHelper.getAllProducts());
                selectedProducts.clear(); // Clear the selection after deletion
                Toast.makeText(MainActivity.this, "Product(s) deleted", Toast.LENGTH_SHORT).show();
            }
        });


        // Start AddProductActivity to add new products
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivityForResult(intent, 100); // Request code 100 for adding a product
        });
    }

    // Handle the result of AddProductActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {  // Handle the result for AddProductActivity
            if (resultCode == RESULT_OK) {
                // Reload the product list after a new product has been added
                DBHelper dbHelper = new DBHelper(this);
                List<Product> updatedProductList = dbHelper.getAllProducts();
                productAdapter.updateProductList(updatedProductList);
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Show a toast indicating that adding the product was canceled
                Toast.makeText(this, "Product add canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}