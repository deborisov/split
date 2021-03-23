package com.abacaba.ha.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.abacaba.ha.R;
import com.abacaba.ha.actors.Company;
import com.abacaba.ha.db.BillsContract;
import com.abacaba.ha.db.BillsDbHelper;
import com.abacaba.ha.rv.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Company> companies = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private MyPagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SQLiteDatabase db = openOrCreateDatabase(BillsDbHelper.DATABASE_NAME, MODE_PRIVATE, null);
       //db.execSQL(BillsDbHelper.SQL_DELETE_ENTRIES);
        //db.close();
        readCompanies();
        makeToolBar();
    }

    private void makeToolBar(){
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Split");
        setSupportActionBar(toolbar);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), companies, this);
        tabLayout = findViewById(R.id.title_container);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        tabLayout.setTabsFromPagerAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);
        mPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (companies.size() > 0) {
            TabLayout.Tab tab = tabLayout.getTabAt(companies.size() - 1);
            tab.select();
            mPager.setCurrentItem(companies.size() - 1);
        }
        toolbar.findViewById(R.id.btnInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new
                        AlertDialog.Builder(MainActivity.this);
                dialog.setMessage(
                        "Program for splitting the bills\r\n\n" + getResources().getString(R.string.fio));
                dialog.setTitle("About the program");
                dialog.setNeutralButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.setIcon(R.drawable.ic_baseline_info_24);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    private void readCompanies(){
        BillsDbHelper dbHelper = new BillsDbHelper(getApplicationContext());
        //dbHelper.onCreate(dbHelper.getReadableDatabase());
        String[] projection = {
                BaseColumns._ID,
                BillsContract.Company.COLUMN_NAME_NAME
        };
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                BillsContract.Company.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        companies = new ArrayList<>();
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(BillsContract.Company.COLUMN_NAME_NAME));
            Integer id = cursor.getInt(cursor.getColumnIndex(BillsContract.Company._ID));
            companies.add(new Company(name, id));
        }
        cursor.close();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/

//

    public void updateTabs(){
        readCompanies();
        mAdapter.setCompanies(companies, this);
    }
}


