package dev.md19303.and103_asmcompleted.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.md19303.and103_asmcompleted.Model.Cake;
import dev.md19303.and103_asmcompleted.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cake> cartItems;
    private Context context;

    public CartAdapter(List<Cake> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cake cake = cartItems.get(position);
        if (cake != null) {
            holder.tvProductName.setText(cake.getName());
            holder.tvProductPrice.setText("¥" + cake.getPrice());
        } else {
            Log.e("ADAPTER", "Cake is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;

        public CartViewHolder(View itemView) {
            super(itemView);
            // Make sure these IDs are correct in your item layout file (item_cart.xml)
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}
