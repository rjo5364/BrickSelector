package edu.psu.sweng888.brickselector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    RecyclerView recyclerView;
    Button btnEmail;
    List<Product> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = findViewById(R.id.recyclerViewSelectedProducts);
        btnEmail = findViewById(R.id.btnEmail);

        // Retrieve the Parcelable ArrayList of Products
        selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        // Initialize the adapter and assign it to the class-level variable
        productAdapter = new ProductAdapter(selectedProducts, null);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnEmail.setOnClickListener(v -> {
            sendEmail(selectedProducts);
        });
    }

    private void sendEmail(List<Product> selectedProducts) {
        StringBuilder emailContent = new StringBuilder();
        for (Product product : selectedProducts) {
            emailContent.append(product.getName()).append(", ")
                    .append(product.getDescription()).append(", ")
                    .append(product.getSeller()).append(", ")
                    .append(product.getPrice()).append("\n");
        }

        String email = getString(R.string.email);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Selected Products");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent.toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
            Toast.makeText(this, "Email sent successfully!", Toast.LENGTH_SHORT).show();

            // Clear the selected products and notify adapter
            selectedProducts.clear();
            productAdapter.notifyDataSetChanged();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}