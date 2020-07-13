package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.byted.camp.todolist.beans.Priority;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoContract.TodoEntry;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.util.DatePickerUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private SQLiteDatabase db;
    private TodoDbHelper dbHelper;
    private RadioGroup radioGroup;
    private RadioButton checkedRadio;
    private Button ddlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        dbHelper = new TodoDbHelper(this);
        db = dbHelper.getWritableDatabase();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        ddlBtn = findViewById(R.id.select_date);
        ddlBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtils.pickDate(NoteActivity.this, ddlBtn);
            }
        });
        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ddlBtn.setText(simpleDateFormat.format(new Date()));

        radioGroup = findViewById(R.id.radioGroup);
        checkedRadio = findViewById(R.id.radioButton3);
        checkedRadio.setChecked(true);
        checkedRadio.setBackgroundColor(Color.parseColor("#2D03A9F4"));
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedRadio.setBackgroundColor(getTitleColor());
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton1:
                        checkedRadio = findViewById(R.id.radioButton1);break;
                    case R.id.radioButton2:
                        checkedRadio = findViewById(R.id.radioButton2);break;
                    case R.id.radioButton3:
                        checkedRadio = findViewById(R.id.radioButton3);break;
                }
                checkedRadio.setChecked(true);
                checkedRadio.setBackgroundColor(Color.parseColor("#2D03A9F4"));
            }
        });
        addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(),ddlBtn.getText().toString(), getSelectedPriority());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content,String ddl, Priority priority) {
        // TODO 插入一条新数据，返回是否插入成功
        if (db==null)
            return false;
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_CONTENT,content);
        values.put(TodoContract.TodoEntry.COLUMN_DATE, System.currentTimeMillis());
        values.put(TodoEntry.COLUMN_DEADLINE,ddl);
        values.put(TodoContract.TodoEntry.COLUMN_STATE, State.TODO.intValue);
        values.put(TodoContract.TodoEntry.COLUMN_PRIORITY,priority.value);
        long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME,null,values);
        return newRowId != -1;
    }
    private Priority getSelectedPriority() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButton1:
                return Priority.High;
            case R.id.radioButton2:
                return Priority.Medium;
            default:
                return Priority.Low;
        }
    }
}
