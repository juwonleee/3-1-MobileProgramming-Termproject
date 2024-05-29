package com.example.nako;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    private EditText edit1, edit2, edit3;
    private Button btn1;
    private TextView textResult1, textResult2;
    private String height, weight, goalweight, BMI, result2;
    private double result1, goal1, goal2;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        edit1 = view.findViewById(R.id.Edit1);
        edit2 = view.findViewById(R.id.Edit2);
        edit3 = view.findViewById(R.id.Edit3);
        btn1 = view.findViewById(R.id.BtnAdd);
        textResult1 = view.findViewById(R.id.TextResult1);
        textResult2 = view.findViewById(R.id.TextResult2);

        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                height = edit1.getText().toString();
                weight = edit2.getText().toString();
                goalweight = edit3.getText().toString();

                if(!height.isEmpty() && !weight.isEmpty()){
                double heightValue = Double.parseDouble(height);
                double weightValue = Double.parseDouble(weight);

                result1 = weightValue / ((heightValue / 100) * (heightValue / 100));

                if (result1 < 20) {
                    result2 = "저체중";
                } else if (result1 <= 24) {
                    result2 = "정상";
                } else if (result1 <= 30) {
                    result2 = "과체중";
                } else {
                    result2 = "비만";
                }

                BMI = String.format("%.2f", result1);
                textResult1.setText("귀하의 BMI = " + BMI + "이며, " + result2 + "입니다.");

                goal1 = weightValue - (result1 - 20);
                goal2 = weightValue - (result1 - 24);

                textResult2.setText("권장 몸무게는 " +String.format("%.2f",goal1)+"kg" + "과 "+ String.format("%.2f",goal2)+"kg" + "사이입니다.");

                }else{
                    textResult1.setText("키와 체중을 입력해주세요.");
                    textResult2.setText("");
                }

                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
