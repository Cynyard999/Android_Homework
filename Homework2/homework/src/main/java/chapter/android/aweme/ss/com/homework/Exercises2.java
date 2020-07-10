package chapter.android.aweme.ss.com.homework;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {
    boolean calculated = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise2);
        Button button = findViewById(R.id.button1);
        final TextView textView = findViewById(R.id.textView1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!calculated){
                    //textView.setText(textView.getText()+" "+getAllChildViewCount((ViewGroup)findViewById(android.R.id.content)));
                    textView.setText(textView.getText()+" "+getAllChildViewCount((ViewGroup)findViewById(R.id.exercise2)));
                    calculated = true;
                }
            }
        });
    }


    //BFS also works while recursion used here
    public int getAllChildViewCount(View view) {
        int total = 1;
        int numOfChild  = ((ViewGroup)view).getChildCount();
        for (int i = 0;i<numOfChild;i++){
            if (((ViewGroup)view).getChildAt(i) instanceof ViewGroup){
                //So any view parameter passed in this func must be viewGroup
                total += getAllChildViewCount(((ViewGroup)view).getChildAt(i) );
            }
            else {
                total +=1;
            }
        }
        return total;
    }
}
