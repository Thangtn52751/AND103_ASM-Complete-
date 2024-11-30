package dev.md19303.and103_asmcompleted.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.md19303.and103_asmcompleted.LocationActivity;
import dev.md19303.and103_asmcompleted.Model.Cake;
import dev.md19303.and103_asmcompleted.R;

public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.CakeViewHolder> {
    private Context context;
    private List<Cake> cakeList;
    private OnCakeInteractionListener listener;
    private OnAddToCartListener cartListener;

    public CakeAdapter(List<Cake> cakeList, Context context, OnCakeInteractionListener listener, OnAddToCartListener cartListener) {
        this.cakeList = cakeList;
        this.context = context;
        this.listener = listener;
        this.cartListener = cartListener;
    }


    @NonNull
    @Override
    public CakeAdapter.CakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cake, parent, false);
        return new CakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeAdapter.CakeViewHolder holder, int position) {
        Cake cake = cakeList.get(position);
        // Gán giá trị cho các view trong item
        holder.tvProductName.setText(cake.getName());
        holder.tvdescription.setText(cake.getDescription());
        holder.tvProductPrice.setText(String.valueOf(cake.getPrice()));
        // Xử lý nhấn giữ để xóa
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDeleteCake(position);
            }
            return true; // Trả về true để chỉ ra rằng sự kiện đã được xử lý
        });

        // Xử lý nhấn để sửa
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditCake(position);
            }
        });

        holder.btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cakeList != null ? cakeList.size() : 0;
    }

    public class CakeViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnAddProduct;
        TextView tvProductName, tvProductPrice, tvdescription;
        public CakeViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvdescription = itemView.findViewById(R.id.tvdescription);

        }
    }

    public interface OnCakeInteractionListener {
        void onEditCake(int position);

        void onDeleteCake(int position);
    }

    // Cập nhật danh sách trong adapter
    public void updateList(List<Cake> newList) {
        cakeList.clear();
        cakeList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnAddToCartListener {
        void onAddToCart(Cake cake);
    }
}
