package com.bytedance.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    int i = 0;
    String[] names = {"灵兽·古鲲","凶兽·骨鲲","异兽·尸鲲","奇兽·巨鲲","古兽·齿鲲"};
    String[] descriptions = {"性格温和，体型巨大，喜欢傲游四方","凶狠残暴，以巨兽为食，极其稀有","本是性格温和的鲲，误入异界魔化成尸鲲","体型巨大，由远古巨龙演化而成","遨游深海，一方霸主"};
    int[] rates = {4,5,4,4,5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity");
        Button button = findViewById(R.id.button);
        final ImageView image = findViewById(R.id.image);
        final TextView name = findViewById(R.id.name);
        final TextView description = findViewById(R.id.desc);
        final RatingBar rate = findViewById(R.id.rate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i){
                    case 0:{
                        i++;
                        name.setText(names[i]);
                        image.setBackgroundResource(R.drawable.pic2);
                        description.setText(descriptions[i]);
                        rate.setRating(rates[i]);
                        break;}
                    case 1:{
                        i++;
                        name.setText(names[i]);
                        image.setBackgroundResource(R.drawable.pic3);
                        description.setText(descriptions[i]);
                        rate.setRating(rates[i]);
                        break;}
                    case 2:{
                        i++;
                        name.setText(names[i]);
                        image.setBackgroundResource(R.drawable.pic4);
                        description.setText(descriptions[i]);
                        rate.setRating(rates[i]);
                        break;}
                    case 3:{
                        i++;
                        name.setText(names[i]);
                        image.setBackgroundResource(R.drawable.pic5);
                        description.setText(descriptions[i]);
                        rate.setRating(rates[i]);
                        break;}
                    case 4:
                    {
                        i = 0;
                        name.setText(names[i]);
                        image.setBackgroundResource(R.drawable.pic1);
                        description.setText(descriptions[i]);
                        rate.setRating(rates[i]);
                        break;}
                }
            }
        });

    }
}
