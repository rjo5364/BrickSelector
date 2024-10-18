package edu.psu.sweng888.brickselector;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    RecyclerView recyclerView;
    Button btnEmail;
    List<Product> selectedProducts;
    ImageButton buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        recyclerView = findViewById(R.id.recyclerViewSelectedProducts);
        btnEmail = findViewById(R.id.btnEmail);
        buttonHome = findViewById(R.id.buttonHome);  // Initialize buttonHome here after setContentView

        // Retrieves the Parcelable ArrayList of Products
        selectedProducts = getIntent().getParcelableArrayListExtra("selectedProducts");

        // Initializes the adapter and assign it to the class-level variable
        productAdapter = new ProductAdapter(selectedProducts, null);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sends email when the button is clicked
        btnEmail.setOnClickListener(v -> sendEmail(selectedProducts));

        // Handles returning to the home screen
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Close the current activity
        });
    }

    private void sendEmail(List<Product> selectedProducts) {
        StringBuilder emailContent = new StringBuilder();
        for (Product product : selectedProducts) {
            emailContent.append("ID: ").append(product.getId()).append(", ")
                    .append(product.getName()).append(", ")
                    .append(product.getDescription()).append(", ")
                    .append(product.getSeller()).append(", ")
                    .append(product.getPrice()).append("\n");
        }

        String email = getString(R.string.targetEmail);

        // Gets the current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());

        // Sets up the email intent
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Selected Products - " + currentTime);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent.toString());

        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {  // Check for the requestCode used in startActivityForResult
            // Displays a toast when the user returns from the chooser
            Toast.makeText(this, "Information Sent!", Toast.LENGTH_SHORT).show();


            selectedProducts.clear();
            productAdapter.notifyDataSetChanged();
        }
    }
}