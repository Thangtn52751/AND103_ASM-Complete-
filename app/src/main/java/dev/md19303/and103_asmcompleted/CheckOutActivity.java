package dev.md19303.and103_asmcompleted;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.md19303.and103_asmcompleted.Adapter.CartAdapter;
import dev.md19303.and103_asmcompleted.Model.Cake;

import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView rvSelectedItems;
    private CartAdapter cartAdapter;
    private List<Cake> cartItems;
    private TextView tvTotalAmount;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Initialize views
        rvSelectedItems = findViewById(R.id.rvSelectedItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnOrder = findViewById(R.id.btnOrder);

        // Initialize RecyclerView and adapter
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, this);
        rvSelectedItems.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedItems.setAdapter(cartAdapter);

        // Get the Cake object passed from the Intent
        Cake selectedCake = (Cake) getIntent().getSerializableExtra("selectedCake");
        if (selectedCake != null) {
            addCakeToCart(selectedCake);
        }

        // Handle Order Button click
        btnOrder.setOnClickListener(v -> {
            Toast.makeText(CheckOutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            // finish(); // Comment this out to debug the app first
        });
    }

    private void addCakeToCart(Cake cake) {
        if (cake != null) {
            cartItems.add(cake);
            cartAdapter.notifyItemInserted(cartItems.size() - 1); // Notify that item was added
            updateTotalAmount();
        }
    }

    private void updateTotalAmount() {
        int totalAmount = 0;
        for (Cake cake : cartItems) {
            totalAmount += cake.getPrice();
        }
        tvTotalAmount.setText("Total Amount: ¥" + totalAmount);
    }
}
