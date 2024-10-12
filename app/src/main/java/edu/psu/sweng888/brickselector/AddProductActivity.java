package edu.psu.sweng888.brickselector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddProductActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private ImageView productImageView;
    private EditText editTextProductName, editTextProductDescription, editTextProductPrice;
    private byte[] selectedImageBytes; // Store image bytes for saving in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImageView = findViewById(R.id.imageViewProduct);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);

        Button btnAddProduct = findViewById(R.id.buttonAddProduct);
        Button btnCancel = findViewById(R.id.buttonCancelProduct);

        // Handle image selection
        productImageView.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openImagePicker();
            } else {
                requestStoragePermission();
            }
        });

        // Handle the Add Product button click
        btnAddProduct.setOnClickListener(v -> {
            String productName = editTextProductName.getText().toString().trim();
            String productDescription = editTextProductDescription.getText().toString().trim();
            String productPriceStr = editTextProductPrice.getText().toString().trim();

            if (productName.isEmpty() || productDescription.isEmpty() || productPriceStr.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    double productPrice = Double.parseDouble(productPriceStr);

                    // Create a new Product object (replace "Seller" with actual seller name if needed)
                    Product newProduct = new Product(
                            0,  // Auto-incremented ID
                            productName,
                            productDescription,
                            "Seller",  // Placeholder for now
                            productPrice,
                            selectedImageBytes  // Use the selected image bytes
                    );

                    // Add the product to the database
                    DBHelper dbHelper = new DBHelper(AddProductActivity.this);
                    dbHelper.addProduct(newProduct);

                    // Set the result as RESULT_OK and finish the activity
                    setResult(RESULT_OK);
                    finish();

                } catch (NumberFormatException e) {
                    Toast.makeText(AddProductActivity.this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle the Cancel button click
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                productImageView.setImageURI(data.getData());
                // Convert the selected image into byte array (for saving into the database)
                selectedImageBytes = ImageUtil.getBytesFromUri(this, data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}