package com.abacaba.ha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.abacaba.ha.db.BillsContract;
import com.abacaba.ha.db.BillsDbHelper;
import com.abacaba.ha.R;

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        prepare();
        final EditText text = findViewById(R.id.editTextTextMemberName);
        Button btn = findViewById(R.id.createMemberBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().trim().isEmpty()){
                    BillsDbHelper dbHelper = new BillsDbHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(BillsContract.Person.COLUMN_NAME_NAME, text.getText().toString());
                    values.put(BillsContract.Person.COLUMN_NAME_DEBT, 0);
                    values.put(BillsContract.Person.COLUMN_NAME_COMPANYID, getIntent().getIntExtra("TABLE_ID", 0));
                    long newRowId = db.insert(BillsContract.Person.TABLE_NAME, null, values);
                    closeKeyboard();
                    NavUtils.navigateUpFromSameTask(AddMemberActivity.this);
                }
                else{
                    text.setText("");
                    text.setHint("Name shouldn't be empty");
                }
            }
        });
    }

    private void prepare(){
        setContentView(R.layout.activity_add_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("New member");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}