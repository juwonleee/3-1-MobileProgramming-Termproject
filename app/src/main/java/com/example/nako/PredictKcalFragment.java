package com.example.nako;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import com.example.nako.api.ApiClient;
import com.example.nako.api.ApiInterface;

public class PredictKcalFragment extends Fragment {

    private ApiInterface apiInterface;
    private JSONArray foodArray;

    TextView result;
    private String URL = "https://www.gachon.ac.kr/kor/7350/subview.do";
    Handler h1;
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    String today = formatter.format(calendar.getTime());

    //(일요일 = 1, 토요일 = 7)
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    ListView search_list;
    ArrayList<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predict_kcal, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup foodCategory = view.findViewById(R.id.category);
        RadioGroup groupA = view.findViewById(R.id.foodGroupA);
        RadioGroup groupB = view.findViewById(R.id.foodGroupB);

        list = new ArrayList<>();
        result = view.findViewById(R.id.result);
        result.setText("");
        Button search = view.findViewById(R.id.search);
        search_list = view.findViewById(R.id.foodListView);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // 핸들러----------------------------------
        h1 = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();    //new Thread에서 작업한 결과물 받기
                String[] a = bundle.getStringArray("lunch_1");
                String[] b = bundle.getStringArray("lunch_2");

                for (int i = 0; i< a.length; i++) {
                    RadioButton food_button = new RadioButton(getContext());
                    food_button.setText(a[i]);
                    groupA.addView(food_button);

                }

                for (int i = 0; i< b.length; i++) {
                    RadioButton food_button = new RadioButton(getContext());
                    food_button.setText(b[i]);
                    groupB.addView(food_button);

                }

//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://openapi.foodsafetykorea.go.kr")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                RetroitAPI api = retrofit.create(RetroitAPI.class);


//                api.getData(a[0]).enqueue(new retrofit2.Callback<ResponseModel>() {
//                    @Override
//                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                        if (response.isSuccessful()) {
//                            ResponseModel data = response.body();
//                            List<ResponseModel.RowItem> food = data != null ? data.getI2790Data().getRow() : null;
//                            if (food != null && !food.isEmpty() && food.get(0).getKcal() != null) {
//                                Log.d("food", a[0]);
//                                result.append("\n" + food.get(0).getKcal());
//                            } else {
//                                Log.d("food", a[0]);
//                                result.append("\nfail..");
//                            }
//                            Log.d("food_api_result", "success");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseModel> call, Throwable t) {
//                        Log.e("food_api_result", a[0] + "fail" + t.getMessage());
//                        t.printStackTrace();
//                        result.append("\nfail..");
//                    }
//                });
            //-----------------------------
            }
        };
        //---------------------------------------

        if (dayOfWeek != Calendar.SUNDAY) {
            groupA.setVisibility(View.GONE);
            groupB.setVisibility(View.GONE);

            foodCategory.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.buttonA) {
                    // A 선택 시
                    groupA.setVisibility(View.VISIBLE);
                    groupB.setVisibility(View.GONE);
                    uncheckAllChildRadioButtons(groupB);
                } else if (checkedId == R.id.buttonB) {
                    // B 선택 시
                    groupA.setVisibility(View.GONE);
                    groupB.setVisibility(View.VISIBLE);
                    uncheckAllChildRadioButtons(groupA);
                }
            });

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < groupA.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) groupA.getChildAt(i);
                        if (radioButton.isChecked()) {
                            Log.d("sadfsdfa", radioButton.getText().toString());
                            getFoodCalories(radioButton.getText().toString());
                            break;
                        }
                    }
                }
            });

            search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayFoodDetails(position);
                }
            });


            final Bundle bundle = new Bundle();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    org.jsoup.nodes.Document doc = null;
                    String tem;
                    String date;
                    String[] tem_splits;
                    int i=0;

                    try {
                        //크롤링 할 구문
                        doc = Jsoup.connect(URL).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
                        Elements table = doc.select("div.table_1");
                        Elements tBody = table.select("tBody");
                        Elements rows = tBody.select("tr");

                        //  현재 날짜에 맞는 식단 검색
                        for (i=0;i<rows.size();i++) {
                            Element row = rows.get(i);
                            tem = row.text();
                            String[] parts = tem.split(" ");
                            date = parts[0];
                            if(date.equals(today)) {
                                bundle.putString("today", tem);
                                break;
                            }
//                        else if (i == rows.size()) {
//
//                        }
                        }
                        //  얻어온 문자열 다듬을 정규표현식 패턴
                        String pattern = "\\d+|\\([^\\)]+\\)|\\[\\S+\\]|아침|점심\\(한식\\)|점심\\(푸드코트\\)|[A-Z]\\코너|[.+]|저녁";

                        // 0:morning 1:lunch_1 2:lunch_2 3:dinner
                        for (int j=0;j<4;j++) {
                            Element row = rows.get(i+j);
                            tem = row.text();
                            Log.i("origin_data ", tem);
                            tem = tem.replaceAll(pattern, "").trim();
                            Log.i("processing_data ", tem);
                            tem_splits = tem.split("[\\p{Punct}\\s]+");

                            if(j==0) {
                                bundle.putStringArray("morning", tem_splits);
                            }
                            else if(j==1) {
                                bundle.putStringArray("lunch_1", tem_splits);
                            }
                            else if(j==2) {
                                bundle.putStringArray("lunch_2", tem_splits);
                            }
                            else {
                                bundle.putStringArray("dinner", tem_splits);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message message = h1.obtainMessage();
                    message.setData(bundle);
                    h1.sendMessage(message);
                }
            }).start();
        }
        else {
            result.setText("일요일은 학식을 운영하지 않습니다");
        }
    }
    //  하위 라디오버튼 체크 해제
    private void uncheckAllChildRadioButtons(RadioGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (group.getChildAt(i) instanceof RadioButton) {
                RadioButton child = (RadioButton) group.getChildAt(i);
                child.setChecked(false);
            } else if (group.getChildAt(i) instanceof RadioGroup) {
                uncheckAllChildRadioButtons((RadioGroup) group.getChildAt(i));
            }
        }
    }

//    private void getCalories(String food_name) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://openapi.foodsafetykorea.go.kr")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetroitAPI api = retrofit.create(RetroitAPI.class);
//
//        api.getData(food_name).enqueue(new retrofit2.Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    ResponseModel data = response.body();
//                    List<ResponseModel.RowItem> foodList = data.getI2790Data().getRow();
//
//                    list.clear();
//                    for (int i = 0; i < foodList.size(); i++) {
//                        ResponseModel.RowItem food = foodList.get(i);
//                        String foodName = food.getFoodName();
//                        list.add(foodName);
//                    }
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
//                    search_list.setAdapter(adapter);
//
//                    if (foodList != null && !foodList.isEmpty() && foodList.get(0).getKcal() != null) {
//                        result.setText(foodList.get(0).getKcal());
//                    } else {
//                        result.setText("fail..");
//                        Toast err_Toast = Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT);
//                        err_Toast.show();
//                    }
//                    Log.d("food_api_result", "success");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Log.e("food_api_result", "fail" + t.getMessage());
//                t.printStackTrace();
//                result.append("\nfail..");
//            }
//        });
//    }

    private void getFoodCalories(String foodName) {
        String apiKey = "a3bac7e4582c4e9d9caa";
        String url = "http://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/I2790/json/1/5/DESC_KOR=" + foodName;

        Call<String> call = apiInterface.getFoodCalories(apiKey, foodName);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parseJson(response.body());
                } else {
                    result.setText("Error fetching data: " + response.code() + ", " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setText("Request failed: " + t.getMessage());
            }
        });
    }

    private void parseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject I2790 = jsonObject.getJSONObject("I2790");
            foodArray = I2790.getJSONArray("row");

            list.clear();
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject food = foodArray.getJSONObject(i);
                String foodName = food.getString("DESC_KOR");
                list.add(foodName);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
            search_list.setAdapter(adapter);

            if (list.isEmpty()) {
                Toast myToast = Toast.makeText(getContext(), "음식을 찾을 수 없습니다.", Toast.LENGTH_SHORT);
                myToast.show();
            } else {
                result.setText("");
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
            result.setText(foodName + "\n열량: " + calories + "kcal\n탄수화물: " + carbohydrate + "g\n단백질: " + protein + "g\n지방: " + fat + "g\n(1회제공량당)");
        } catch (Exception e) {
            Toast myToast = Toast.makeText(getContext(), "음식을 찾을 수 없습니다.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();
        }
    }
}