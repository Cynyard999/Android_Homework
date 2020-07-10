package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ChatRoom  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        TextView msg1 = findViewById(R.id.tv_content_info);
        TextView name1 = findViewById(R.id.tv_with_name);
        Intent intent = getIntent();
        String msg= intent.getStringExtra("msg");
        String name = intent.getStringExtra("name");
        msg1.setText(msg);
        name1.setText(name);
    }

}
