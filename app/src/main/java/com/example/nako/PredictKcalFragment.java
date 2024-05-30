package com.example.nako;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PredictKcalFragment extends Fragment {
    TextView result;
    private String URL = "https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=%EC%84%9C%EC%9A%B8+%EB%82%A0%EC%94%A8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predict_kcal, container, false);
        result = view.findViewById(R.id.result);

        //  Retrofit을 활용한 식품영양DB에 api요청
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.foodsafetykorea.go.kr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroitAPI api = retrofit.create(RetroitAPI.class);

        api.getData().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.isSuccessful()) {
                    ResponseModel data = response.body();
                    List<ResponseModel.RowItem> kcal = data.getI2790Data().getRow();
                    result.setText(kcal.get(0).getFood_name());
                    Log.d("food_api_result", "success");
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("food_api_result", "fail" + t.getMessage());
                t.printStackTrace();
                result.setText("fail..");
            }
        });
        final Bundle bundle = new Bundle();
        new Thread() {
            @Override
            public void run() {
                org.jsoup.nodes.Document doc = null;
                boolean isEmpty;
                String tem;
                try {
                    //크롤링 할 구문
                    doc = Jsoup.connect(URL).get();	//URL 웹사이트에 있는 html 코드를 다 끌어오기
                    Elements temele = doc.select(".temperature_text");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
                    isEmpty = temele.isEmpty(); //빼온 값 null체크
                    Log.d("Tag", "isNull? : " + isEmpty); //로그캣 출력
                    if(isEmpty == false) { //null값이 아니면 크롤링 실행
                        tem = temele.get(0).text().substring(5); //크롤링 하면 "현재온도30'c" 이런식으로 뽑아와지기 때문에, 현재온도를 잘라내고 30'c만 뽑아내는 코드
                        bundle.putString("temperature", tem); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        // Inflate the layout for this fragment
        return view;
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
////            super.handleMessage(msg);
//            Bundle bundle = msg.getData();    //new Thread에서 작업한 결과물 받기
//            textView.setText(bundle.getString("temperature"));    //받아온 데이터 textView에 출력
//        }
//    };
}