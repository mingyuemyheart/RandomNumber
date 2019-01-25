package com.cxwl.shawn.random.number;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.view.patheffect.PathTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.widget.HeartLayout;

public class MainActivity extends Activity {

    private Context context;
    private TextView tvStart,tvToast;
    private ImageView imageView;
    private LinearLayout llContent;
    private List<Integer> numbers = new ArrayList<>();
    private Timer timer;
    private long mExitTime;//记录点击完返回按钮后的long型时间
    private HeartLayout heartLayout;
    private PathTextView pathTextView,pathTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initWidget();
    }

    private void initWidget() {
        tvStart = findViewById(R.id.tvStart);
        imageView = findViewById(R.id.imageView);
        tvToast = findViewById(R.id.tvToast);
        heartLayout = findViewById(R.id.heart_layout);
        llContent = findViewById(R.id.llContent);
        pathTextView = findViewById(R.id.pathTextView);
        pathTextView.init("come on are you ready");
        pathTextView.setTextColor(Color.WHITE);
        pathTextView.setTextSize(50);
//        pathTextView.setTextWeight(weight);
        pathTextView.setDuration(5000);
//        pathTextView.setShadow(radius, dx, dy, shadowColor);
        pathTextView.setPaintType(PathTextView.Type.SINGLE);

        pathTextView2 = findViewById(R.id.pathTextView2);
        pathTextView2.setTextColor(Color.WHITE);
        pathTextView2.setTextWeight(10);
        pathTextView2.setTextSize(200);
        pathTextView2.setDuration(2000);
        pathTextView2.setPaintType(PathTextView.Type.MULTIPLY);

        resetNumbers();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widht = dm.widthPixels;
        int height = dm.heightPixels;

        ViewGroup.LayoutParams params = llContent.getLayoutParams();
        params.width = widht-height;
        params.height = widht-height;
        llContent.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = imageView.getLayoutParams();
        params1.width = height;
        params1.height = height;
        imageView.setLayoutParams(params1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pathTextView2.setVisibility(View.VISIBLE);
                tvStart.setVisibility(View.VISIBLE);
            }
        }, 3000);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathTextView.setVisibility(View.GONE);
                if (numbers.size() <= 0) {
                    tvToast.setVisibility(View.VISIBLE);
                    pathTextView2.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    tvStart.setVisibility(View.GONE);
                    return;
                }
                if (timer != null) {//stop
                    resetTimer();
                    tvStart.setText("start");
                    int index = (int)(Math.random()*numbers.size());
                    Log.e("number", numbers.get(index)+"");
                    String number = numbers.get(index)+"";
                    pathTextView2.init(number);
                    if (index % 2 == 0) {
                        pathTextView2.setPaintType(PathTextView.Type.MULTIPLY);
                    }else {
                        pathTextView2.setPaintType(PathTextView.Type.SINGLE);
                    }
                    Bitmap bitmap = CommonUtil.getImageFromAssetsFile(context,numbers.get(index)+".jpg");
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                    numbers.remove(index);

                    String result = "";
                    for (int i : numbers) {
                        result = result+i+",";
                    }
                    Log.e("result", result);
                }else {//start
                    tvStart.setText("stop");
                    initTimer();
                }
            }
        });

    }

    private void initTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        heartLayout.addHeart(randomColor());
                    }
                });
            }
        }, 0, 100);
    }

    private int randomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void resetNumbers() {
        numbers.clear();
        for (int i = 1; i <= 25; i++) {
            numbers.add(i);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "大爷，您确定不再多玩一会儿了吗~~~", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return false;
    }

}
