package com.example.nako;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nako.api.ApiClient;
import com.example.nako.api.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietFragment extends Fragment {

    private EditText foodInput;
    private Button searchButton;
    private ListView foodListView;
    private TextView resultText;
    private ApiInterface apiInterface;
    private static final String TAG = "DietFragment";
    private ArrayList<String> foodList;
    private JSONArray foodArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        foodInput = view.findViewById(R.id.foodInput);
        searchButton = view.findViewById(R.id.searchButton);
        foodListView = view.findViewById(R.id.foodListView);
        resultText = view.findViewById(R.id.resultText);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        foodList = new ArrayList<>();

        foodInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String foodName = s.toString().trim();
                if (!foodName.isEmpty()) {
                    getFoodCalories(foodName);
                } else {
                    foodList.clear();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foodList);
                    foodListView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodInput.length() == 0) {
                    Toast myToast = Toast.makeText(getContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT);
                    myToast.show();
                } else {
                    String foodName = foodInput.getText().toString().trim();
                    getFoodCalories(foodName);
                }
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayFoodDetails(position);
            }
        });

        return view;
    }

    private void getFoodCalories(String foodName) {
        String apiKey = "a3bac7e4582c4e9d9caa";
        String url = "http://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/I2790/json/1/5/DESC_KOR=" + foodName;
        Log.d(TAG, "API URL: " + url);

        Call<String> call = apiInterface.getFoodCalories(apiKey, foodName);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parseJson(response.body());
                } else {
                    resultText.setText("Error fetching data: " + response.code() + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                resultText.setText("Request failed: " + t.getMessage());
            }
        });
    }

    private void parseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject I2790 = jsonObject.getJSONObject("I2790");
            foodArray = I2790.getJSONArray("row");

            foodList.clear();
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject food = foodArray.getJSONObject(i);
                String foodName = food.getString("DESC_KOR");
                foodList.add(foodName);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foodList);
            foodListView.setAdapter(adapter);

            if (foodList.isEmpty()) {
                Toast myToast = Toast.makeText(getContext(), "음식을 찾을 수 없습니다.", Toast.LENGTH_SHORT);
                myToast.show();
            } else {
                resultText.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayFoodDetails(int position) {
        try {
            JSONObject selectedFood = foodArray.getJSONObject(position);
            String foodName = selectedFood.getString("DESC_KOR");
            float calories = (float) selectedFood.getDouble("NUTR_CONT1");
            float carbohydrate = (float) selectedFood.getDouble("NUTR_CONT2");
            float protein = (float) selectedFood.getDouble("NUTR_CONT3");
            float fat = (float) selectedFood.getDouble("NUTR_CONT4");
            resultText.setText(foodName + "\n열량: " + calories + "kcal\n탄수화물: " + carbohydrate + "g\n단백질: " + protein + "g\n지방: " + fat + "g\n(1회제공량당)");
        } catch (Exception e) {
            Toast myToast = Toast.makeText(getContext(), "음식을 찾을 수 없습니다.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();
        }
    }
}
