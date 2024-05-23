package com.example.nako;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    private EditText edit1, edit2, edit3;
    private Button btn1;
    private TextView textResult1, textResult2;
    private String height, weight, goalweight, BMI, result2;
    private double result1, goal1, goal2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        edit1 = edit2.findViewById(R.id.Edit1);
        edit2 = edit2.findViewById(R.id.Edit2);
        edit2 = edit3.findViewById(R.id.Edit3);
        btn1 = btn1.findViewById(R.id.BtnAdd);
        textResult1 = textResult1.findViewById(R.id.TextResult1);
        textResult2 = textResult2.findViewById(R.id.TextResult2);
        textResult2 = textResult2.findViewById(R.id.TextResult2);
        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                height = edit1.getText().toString();
                weight = edit2.getText().toString();
                goalweight = edit3.getText().toString();
                result1 = Double.parseDouble(weight) / ((Double.parseDouble(height) / 100) * (Double.parseDouble(height) / 100));
                if (result1 < 20) {
                    result2 = "저체중";
                } else if (result1 <= 24 && result1 > 20) {
                    result2 = "정상";
                } else if (result1 <= 30 && result1 > 24) {
                    result2 = "과체중";
                } else {
                    result2 = "비만";
                }
                String BMI = String.format("%.2f", result1);
                textResult1.setText("귀하의 BMI = " + BMI + "이며, " + result2 + "입니다.");

                double weight_d = Double.parseDouble(weight);
                goal1 = weight_d - (result1 - 20);
                goal2 = weight_d - (result1 - 24);
                textResult2.setText("권장 몸무게는 " + goal1 + "과 "+ goal2 + "사이입니다.");

                return false;
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}
