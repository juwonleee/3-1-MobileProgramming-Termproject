package com.example.nako;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {
    private EditText foodNameInput;
    private Button searchButton;
    private TextView calorieResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        foodNameInput = view.findViewById(R.id.food_name_input);
        searchButton = view.findViewById(R.id.btn_search);
        calorieResult = view.findViewById(R.id.calorie_result);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = foodNameInput.getText().toString().trim();
                if(!foodName.isEmpty()){
                    fetchCalorieData(foodName);
                }
            }
        });
        return view;
    }

    private void fetchCalorieData(final String foodName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String apiKey = "79867aeeb17a44edb903";  //API 키
                    String apiUrl = "https://www.foodsafetykorea.go.kr/api/I2790/xml/" + apiKey + "/1/5/DESC_KOR=" + foodName;

                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();

                    //XML 응답을 JSON으로 변환하여 파싱하기
                    JSONObject jsonObject = XML.toJSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("I2790").getJSONArray("row").getJSONObject(0);
                    final String calorie = data.getString("NUTR_CONT1");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calorieResult.setText("칼로리: " + calorie + "kcal");
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();;
                }
            }
        });
    }
}
