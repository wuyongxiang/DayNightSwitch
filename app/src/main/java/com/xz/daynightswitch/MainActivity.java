package com.xz.daynightswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.xz.daynightswitch.R;
import com.xz.daynightswitch.view.DayNightSwitch;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll1,ll2,ll3;
    DayNightSwitch  dns_1,dns_2,dns_3;

    int white = Color.WHITE; // 起始颜色
    int black = Color.BLACK; // 结束颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        dns_1 = findViewById(R.id.dns_1);
        dns_2 = findViewById(R.id.dns_2);
        dns_3 = findViewById(R.id.dns_3);

        dns_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("onCheckedChanged",isChecked+"");
                if(isChecked){
                    changeBgColor(ll1,white,black);
                }else {
                    changeBgColor(ll1,black,white);
                }
            }
        });
        dns_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    changeBgColor(ll2,white,black);
                }else {
                    changeBgColor(ll2,black,white);
                }
            }
        });
        dns_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    changeBgColor(ll3,white,black);
                }else {
                    changeBgColor(ll3,black,white);
                }
            }
        });
    }

    private void changeBgColor(View view , int startColor,int endColor){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
                colorAnim.setDuration(700);

                colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        // 获取当前动画的颜色值
                        int color = (int) animator.getAnimatedValue();
                        // 应用颜色到视图
                        view.setBackgroundColor(color);
                    }
                });
                colorAnim.start();
            }
        });

    }
}
