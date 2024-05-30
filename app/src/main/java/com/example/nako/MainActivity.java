package com.example.nako;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    Fragment fragment[];

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager= getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //fragment를 초기화
        initializeFragments();

        //하단 네비게이션 바를 통해 트레그먼트 전환을 관리
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.navigation_bar_item_1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contents_layout, new HomeFragment()).commit();
                } else if (id == R.id.navigation_bar_item_2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contents_layout, new PredictKcalFragment()).commit();
                } else if (id == R.id.navigation_bar_item_3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contents_layout, new ExerciseFragment()).commit();
                } else if (id == R.id.navigation_bar_item_4) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.contents_layout, new SettingFragment()).commit();
                }

                return true;
            }
        });
    }
    private void initializeFragments() {
        fragment = new Fragment[4];
        fragment[0] = new HomeFragment();
        fragment[1] = new PredictKcalFragment();
        fragment[2] = new ExerciseFragment();
        fragment[3] = new SettingFragment();

        fragmentManager.beginTransaction().add(R.id.contents_layout, fragment[0]).commit();
    }
}