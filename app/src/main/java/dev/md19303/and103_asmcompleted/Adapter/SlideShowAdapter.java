package dev.md19303.and103_asmcompleted.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.md19303.and103_asmcompleted.R;

public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.SlideShowViewHolder> {
    private List<Integer> images;
    public SlideShowAdapter(List<Integer> images) {
        this.images = images;
    }


    @NonNull
    @Override
    public SlideShowAdapter.SlideShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slideshow, parent, false);
        return new SlideShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideShowAdapter.SlideShowViewHolder holder, int position) {
        holder.ivSlideshowImage.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class SlideShowViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlideshowImage;
        public SlideShowViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlideshowImage = itemView.findViewById(R.id.ivSlideshowImage);
        }
    }
}
