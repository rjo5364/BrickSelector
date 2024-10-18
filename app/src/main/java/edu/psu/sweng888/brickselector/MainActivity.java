package edu.psu.sweng888.brickselector;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    Button btnNext, btnDeleteProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializes views inside onCreate
        recyclerView = findViewById(R.id.recyclerViewProducts);
        btnNext = findViewById(R.id.btnNext);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);  // Button for deleting products
        Button btnAddProduct = findViewById(R.id.btnAddProduct);

        DBHelper dbHelper = new DBHelper(this);
        List<Product> productList = dbHelper.getAllProducts();

        // Sets up the adapter and RecyclerView
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {

            }
        });

        // Handles Refresh button click to reload product list
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            // Refreshes the product list from the database
            List<Product> updatedProductList = dbHelper.getAllProducts();
            productAdapter.updateProductList(updatedProductList);
            Toast.makeText(MainActivity.this, "Product list refreshed", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // button to proceed if 3 or more products are selected
        btnNext.setOnClickListener(v -> {
            // Gets selected products from the adapter
            Set<Product> selectedProducts = productAdapter.getSelectedProducts();

            if (selectedProducts.size() < 3) {
                Toast.makeText(MainActivity.this, "You must select 3 or more to proceed", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putParcelableArrayListExtra("selectedProducts", new ArrayList<>(selectedProducts));
                startActivity(intent);
            }
        });

        // delete button functionality
        btnDeleteProduct.setOnClickListener(v -> {
            // Gets selected products from the adapter
            Set<Product> selectedProducts = productAdapter.getSelectedProducts();

            if (selectedProducts.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please select a product to delete", Toast.LENGTH_SHORT).show();
            } else {
                for (Product product : selectedProducts) {
                    // Deletes the product from the database
                    dbHelper.deleteProduct(product.getId());
                }
                // Updates the list after deletion
                productAdapter.updateProductList(dbHelper.getAllProducts());
                Toast.makeText(MainActivity.this, "Product(s) deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // AddProductActivity to add new products
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivityForResult(intent, 100); // Request code 100 for adding a product
        });
    }

    // Handles the result of AddProductActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {  // Handle the result for AddProductActivity
            DBHelper dbHelper = new DBHelper(this);
            List<Product> updatedProductList = dbHelper.getAllProducts();

        }
    }}