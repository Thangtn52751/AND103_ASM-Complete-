package dev.md19303.and103_asmcompleted;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.md19303.and103_asmcompleted.Adapter.CakeAdapter;
import dev.md19303.and103_asmcompleted.Adapter.SlideShowAdapter;
import dev.md19303.and103_asmcompleted.Model.Cake;
import dev.md19303.and103_asmcompleted.Services.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements CakeAdapter.OnCakeInteractionListener, CakeAdapter.OnAddToCartListener {
    RecyclerView rvProducts;
    FloatingActionButton fabAdd;
    private CakeAdapter cakeAdapter;
    private List<Cake> cakeList = new ArrayList<>();
    private APIService apiService;
    private TextView tvGreeting;
    private ViewPager2 viewPagerSlideshow;
    private SlideShowAdapter slideshowAdapter;
    private int currentPosition = 0;
    EditText etsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvProducts = findViewById(R.id.rvProducts);
        fabAdd = findViewById(R.id.fab_add);
        tvGreeting = findViewById(R.id.tvGreeting);
        viewPagerSlideshow = findViewById(R.id.viewPagerSlideshow);
        etsearch = findViewById(R.id.etSearch);

        cakeAdapter = new CakeAdapter(cakeList, this, this,this);
          // Set up the CakeAdapter
        rvProducts.setAdapter(cakeAdapter);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up Retrofit to work with the APIService
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN) // The base URL from your APIService
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Automatically filter the list as the user types
                searchCakes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        // Fetch the cakes list from the API
        fetchCakes();
        setUpSlideShow();

        // Handle the Add Cake button click
        fabAdd.setOnClickListener(v -> showCakeDialog(null));  // Show Add Cake dialog

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");

        // Hiển thị username lên tvGreeting
        tvGreeting.setText("Welcome, " + username);
    }

    // Fetch cakes from the API
    private void fetchCakes() {
        apiService.getCake().enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cakeList = response.body();
                    cakeAdapter.updateList(cakeList); // Update the RecyclerView with the new list
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load cakes", Toast.LENGTH_SHORT).show();
                    Log.e("HomeActivity", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display the Add/Edit Cake dialog
    private void showCakeDialog(Cake cakeToEdit) {
        View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_cake, null);
        EditText etCakeName = dialogView.findViewById(R.id.etCakeName);
        EditText etCakeDescription = dialogView.findViewById(R.id.etCakeDescription);
        EditText etCakePrice = dialogView.findViewById(R.id.etCakePrice);

        if (cakeToEdit != null) {
            etCakeName.setText(cakeToEdit.getName());
            etCakeDescription.setText(cakeToEdit.getDescription());
            etCakePrice.setText(String.valueOf(cakeToEdit.getPrice()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(cakeToEdit == null ? "Add Cake" : "Edit Cake")
                .setView(dialogView)
                .setPositiveButton(cakeToEdit == null ? "Add" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etCakeName.getText().toString().trim();
                        String description = etCakeDescription.getText().toString().trim();
                        String priceStr = etCakePrice.getText().toString().trim();

                        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                            Toast.makeText(HomeActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int price = Integer.parseInt(priceStr);

                        if (cakeToEdit == null) {
                            addCake(name, description, price);  // Add new cake
                        } else {
                            updateCake(cakeToEdit.get_id(), name, description, price);  // Update existing cake
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Add a new cake
    private void addCake(String name, String description, int price) {
        apiService.addCake(new Cake(null, description,name, price)) // Pass null _id when adding a new cake
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            fetchCakes();  // Refresh the cake list
                            Toast.makeText(HomeActivity.this, "Cake added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to add cake", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Update an existing cake
    private void updateCake(String cakeId, String name, String description, int price) {
        apiService.updateCake(cakeId, new Cake(cakeId, name, description, price))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            fetchCakes();  // Refresh the cake list after updating
                            Toast.makeText(HomeActivity.this, "Cake updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to update cake", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Delete a cake
    private void deleteCake(int position) {
        Cake cakeToDelete = cakeList.get(position);
        apiService.deleteCake(cakeToDelete.get_id()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cakeList.remove(position);
                    cakeAdapter.updateList(cakeList); // Update the RecyclerView after deletion
                    Toast.makeText(HomeActivity.this, "Cake deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to delete cake", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchCakes(String query) {
        if (query.isEmpty()) {
            // Nếu query trống, lấy lại tất cả các bánh
            fetchCakes();
            return;
        }

        // Nếu có query, gọi API để tìm kiếm bánh
        apiService.searchCakes(query).enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cake>> call, @NonNull Response<List<Cake>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cakeList.clear();  // Xóa dữ liệu hiện tại
                    cakeList.addAll(response.body());  // Thêm các kết quả tìm kiếm vào danh sách
                    cakeAdapter.notifyDataSetChanged();  // Làm mới RecyclerView với danh sách mới
                } else {
                    Toast.makeText(HomeActivity.this, "Không tìm thấy bánh nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cake>> call, @NonNull Throwable t) {
                Log.e("API ERROR", "Lỗi tìm kiếm bánh: " + t.getMessage());
            }
        });
    }



    private void setUpSlideShow() {
        // Tạo danh sách hình ảnh
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cake_banner);
        images.add(R.drawable.cake_banner);
        images.add(R.drawable.cake_banner);

        // Khởi tạo và set adapter
        slideshowAdapter = new SlideShowAdapter(images);
        viewPagerSlideshow.setAdapter(slideshowAdapter); // Đặt adapter cho ViewPager2

        // Auto-slide functionality
        viewPagerSlideshow.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPosition == images.size()) {
                    currentPosition = 0;
                }
                viewPagerSlideshow.setCurrentItem(currentPosition++, true);
                viewPagerSlideshow.postDelayed(this, 3000); // Slide every 3 seconds
            }
        }, 3000);
    }

    // Handle the edit cake click
    @Override
    public void onEditCake(int position) {
        Cake cakeToEdit = cakeList.get(position);
        showCakeDialog(cakeToEdit);  // Show Edit Cake dialog
    }

    // Handle the delete cake click
    @Override
    public void onDeleteCake(int position) {
        deleteCake(position);  // Delete the selected cake
    }

    @Override
    public void onAddToCart(Cake cake) {
        // Start CheckOutActivity and pass the selected cake
        Intent intent = new Intent(HomeActivity.this, CheckOutActivity.class);
        intent.putExtra("selectedCake", cake); // Pass Cake object
        startActivity(intent);
    }
}
