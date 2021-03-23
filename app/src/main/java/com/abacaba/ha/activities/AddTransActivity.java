package com.abacaba.ha.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abacaba.ha.db.BillsContract;
import com.abacaba.ha.db.BillsDbHelper;
import com.abacaba.ha.rv.CreateTransactionPersonAdapter;
import com.abacaba.ha.rv.DividerItemDecorator;
import com.abacaba.ha.actors.Payment;
import com.abacaba.ha.actors.Person;
import com.abacaba.ha.R;
import com.gp89developers.calculatorinputview.activities.CalculatorActivity;

import java.util.ArrayList;

public class AddTransActivity extends AppCompatActivity {
    private static final int ADD = 0;
    private static final int WATCH = 1;
    TextView whoSum, whomSum;
    double firstSum, secondSum;
    RecyclerView rvWho, rvWhom;
    EditText name;
    ArrayList<Person> persons;
    ArrayList<Payment> payments = new ArrayList<>();
    int type;
    public int calcViewPosition;
    public int calcViewType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("TYPE", 0);
        prepare();
    }

    private void prepare(){
        setContentView(R.layout.transaction_activity);
        getPayments(getIntent().getIntExtra("TRANS_ID", -1));
        prepareWho();
        prepareWhom();
        System.out.println(rvWho.getChildCount());
        name = findViewById(R.id.editTextTextTransName);
        Button btn = findViewById(R.id.createTransBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstSum != secondSum){
                    Toast toast = Toast.makeText(AddTransActivity.this, "Sums aren't equal", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (name.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(AddTransActivity.this, "Transaction name is empty", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (firstSum == 0){
                    Toast toast = Toast.makeText(AddTransActivity.this, "Transaction is empty", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                writeTransactions(name.getText().toString(), getIntent().getIntExtra("TABLE_ID", 0));
                NavUtils.navigateUpFromSameTask(AddTransActivity.this);
            }
        });
        if (type == WATCH){
            btn.setVisibility(View.GONE);
            prepareWatchTrans(getIntent().getIntExtra("TRANS_ID", 0));
            setTotal();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("New transaction");
        if (type == WATCH){
            toolbar.setTitle("Viewing");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);    }

    void setTotal(){
        double sum = 0;
        for (Payment p : payments){
            if (p.getType() == 0){
                sum += p.getSum();
            }
        }
        whoSum.setText("Total: " + String.valueOf(sum));
        whomSum.setText("Total: " + String.valueOf(sum));
    }

    void prepareWho(){
        LinearLayout ll = findViewById(R.id.whoCard);
        rvWho = ll.findViewById(R.id.personsRecyclerview);
        rvWho.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.divider)));
        rvWho.setLayoutManager(new LinearLayoutManager(this  ));
        persons = getPersons(getIntent().getIntExtra("TABLE_ID", 0));
        CreateTransactionPersonAdapter adapterWho = new CreateTransactionPersonAdapter(persons,
                this, 0, payments, type);
        rvWho.setAdapter(adapterWho);
        TextView tv =  ll.findViewById(R.id.personCardName);
        tv.setText("Who paid?");
        whoSum = ll.findViewById(R.id.sumText);
        whoSum.setText("Total: 0");
    }

    void getPayments(int id){
        BillsDbHelper dbHelper = new BillsDbHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = BillsContract.Payment.COLUMN_NAME_TRANSACTION_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                BillsContract.Payment.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        payments = new ArrayList<>();
        while (cursor.moveToNext()){
            Payment payment = new Payment(cursor.getInt(cursor.getColumnIndex(BillsContract.Payment.COLUMN_NAME_PERSON_ID)),
                    cursor.getDouble(cursor.getColumnIndex(BillsContract.Payment.COLUMN_NAME_SUM)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Payment.COLUMN_NAME_TRANSACTION_ID)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Payment.COLUMN_NAME_TYPE)));
            payments.add(payment);
        }
    }

    void prepareWhom(){
        LinearLayout ll = findViewById(R.id.forWhomCard);
        whomSum = ll.findViewById(R.id.sumText);
        whomSum.setText("Total: 0");
        TextView tv = ll.findViewById(R.id.personCardName);
        rvWhom = ll.findViewById(R.id.personsRecyclerview);
        rvWhom.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.divider)));
        rvWhom.setLayoutManager(new LinearLayoutManager(this  ));
        CreateTransactionPersonAdapter adapterWhom = new CreateTransactionPersonAdapter(persons,
                this, 1, payments, type);
        rvWhom.setAdapter(adapterWhom);
        tv.setText("For whom?");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Person> getPersons(int id){
        BillsDbHelper dbHelper = new BillsDbHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BillsContract.Person._ID,
                BillsContract.Person.COLUMN_NAME_NAME,
                BillsContract.Person.COLUMN_NAME_COMPANYID,
                BillsContract.Person.COLUMN_NAME_DEBT
        };
        String selection = BillsContract.Person.COLUMN_NAME_COMPANYID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                BillsContract.Person.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        ArrayList<Person> people = new ArrayList<>();
        while(cursor.moveToNext()) {
            Person person = new Person(cursor.getString(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_DEBT)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_COMPANYID)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Person._ID)));
            people.add(person);
        }
        return people;
    }

    public void updateSums(int type){
        if (type == 0){
            firstSum = 0;
            for (int i = 0; i < rvWho.getChildCount(); i++) {
                CreateTransactionPersonAdapter.PersonViewHolder holder = (CreateTransactionPersonAdapter.PersonViewHolder) rvWho.findViewHolderForAdapterPosition(i);
                if (!holder.sum.getText().toString().isEmpty())
                    firstSum += Double.parseDouble(holder.sum.getText().toString());
            }
            whoSum.setText("Total: " + firstSum);
        }
        else{
            secondSum = 0;
            for (int i = 0; i < rvWhom.getChildCount(); i++) {
                CreateTransactionPersonAdapter.PersonViewHolder holder = (CreateTransactionPersonAdapter.PersonViewHolder) rvWhom.findViewHolderForAdapterPosition(i);
                if (!holder.sum.getText().toString().isEmpty())
                    secondSum += Double.parseDouble(holder.sum.getText().toString());
            }
            whomSum.setText("Total: " + secondSum);
        }
    }

    public void writeTransactions(String name, int comp_id){
        BillsDbHelper dbHelper = new BillsDbHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BillsContract.TransactionList.COLUMN_NAME_NAME, name);
        values.put(BillsContract.TransactionList.COLUMN_NAME_COMPANY_ID, comp_id);
        long trans_id = db.insert(BillsContract.TransactionList.TABLE_NAME, null, values);
        writePayments(trans_id);
        int i = 0, j = 0;
        double cur_whom = 0.0, cur_who = 0.0;
        while (i < rvWho.getChildCount() && j < rvWhom.getChildCount()){
            CreateTransactionPersonAdapter.PersonViewHolder holderWhom = (CreateTransactionPersonAdapter.PersonViewHolder) rvWhom.findViewHolderForAdapterPosition(j);
            if (cur_whom == 0) {
                cur_whom = Double.parseDouble(holderWhom.sum.getText().toString());
            }
            CreateTransactionPersonAdapter.PersonViewHolder holderWho = (CreateTransactionPersonAdapter.PersonViewHolder) rvWho.findViewHolderForAdapterPosition(i);
            if (cur_who == 0) {
                cur_who = Double.parseDouble(holderWho.sum.getText().toString());
            }
            if (cur_who == 0){
                ++i;
                continue;
            }
            if (cur_whom == 0){
                ++j;
                continue;
            }
            if (i == j){
                if (cur_whom > cur_who){
                    cur_who -= cur_whom;
                    cur_whom = 0;
                    ++j;
                }
                else{
                    cur_whom -= cur_who;
                    cur_who = 0;
                    ++i;
                }
                continue;
            }
            if (cur_whom > cur_who){
                cur_whom -= cur_who;
                values = new ContentValues();
                values.put(BillsContract.Transaction.COLUMN_NAME_FROM_PERSON, persons.get(j).getId());
                values.put(BillsContract.Transaction.COLUMN_NAME_TO_PERSON, persons.get(i).getId());
                values.put(BillsContract.Transaction.COLUMN_NAME_SUM, cur_who);
                values.put(BillsContract.Transaction.COLUMN_NAME_TRANSACTION_ID, trans_id);
                db.insert(BillsContract.Transaction.TABLE_NAME, null, values);
                cur_who = 0;
                ++i;
            }
            else{
                cur_who -= cur_whom;
                values = new ContentValues();
                values.put(BillsContract.Transaction.COLUMN_NAME_FROM_PERSON, persons.get(j).getId());
                values.put(BillsContract.Transaction.COLUMN_NAME_TO_PERSON, persons.get(i).getId());
                values.put(BillsContract.Transaction.COLUMN_NAME_SUM, cur_whom);
                values.put(BillsContract.Transaction.COLUMN_NAME_TRANSACTION_ID, trans_id);
                db.insert(BillsContract.Transaction.TABLE_NAME, null, values);
                cur_whom = 0;
                ++j;
            }
        }
    }

    public void writePayments(long trans_id){
        BillsDbHelper dbHelper = new BillsDbHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < rvWhom.getAdapter().getItemCount(); ++i){
            CreateTransactionPersonAdapter.PersonViewHolder holderWhom = (CreateTransactionPersonAdapter.PersonViewHolder) rvWhom.findViewHolderForAdapterPosition(i);
            double cur_whom =  Double.parseDouble(holderWhom.sum.getText().toString());
            CreateTransactionPersonAdapter.PersonViewHolder holderWho = (CreateTransactionPersonAdapter.PersonViewHolder) rvWho.findViewHolderForAdapterPosition(i);
            double cur_who = Double.parseDouble(holderWho.sum.getText().toString());
            if (cur_whom != 0){
                values = new ContentValues();
                values.put(BillsContract.Payment.COLUMN_NAME_PERSON_ID, persons.get(i).getId());
                values.put(BillsContract.Payment.COLUMN_NAME_SUM, cur_whom);
                values.put(BillsContract.Payment.COLUMN_NAME_TRANSACTION_ID, trans_id);
                values.put(BillsContract.Payment.COLUMN_NAME_TYPE, 1);
                db.insert(BillsContract.Payment.TABLE_NAME, null, values);
            }
            if (cur_who != 0){
                values = new ContentValues();
                values.put(BillsContract.Payment.COLUMN_NAME_PERSON_ID, persons.get(i).getId());
                values.put(BillsContract.Payment.COLUMN_NAME_SUM, cur_who);
                values.put(BillsContract.Payment.COLUMN_NAME_TRANSACTION_ID, trans_id);
                values.put(BillsContract.Payment.COLUMN_NAME_TYPE, 0);
                db.insert(BillsContract.Payment.TABLE_NAME, null, values);
            }
        }
    }

    public void prepareWatchTrans(int id){
        BillsDbHelper dbHelper = new BillsDbHelper(this.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection2 = BillsContract.TransactionList._ID + " = ?";
        String[] selectionArgs2 = { String.valueOf(id) };
        Cursor cursor2 = db.query(
                BillsContract.TransactionList.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection2,              // The columns for the WHERE clause
                selectionArgs2,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while (cursor2.moveToNext()){
            name.setText(cursor2.getString(cursor2.getColumnIndex(BillsContract.TransactionList.COLUMN_NAME_NAME)));
            setUnaditable(name);
        }
    }

    void setUnaditable(EditText text){
        text.setFocusable(false);
        text.setFocusableInTouchMode(false);
        text.setClickable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CalculatorActivity.REQUEST_RESULT_SUCCESSFUL) {
            String result = data.getStringExtra(CalculatorActivity.RESULT);
            if (calcViewType == 0){
                CreateTransactionPersonAdapter.PersonViewHolder holder = (CreateTransactionPersonAdapter.PersonViewHolder) rvWho.findViewHolderForAdapterPosition(calcViewPosition);
                holder.sum.setText(result);
            }
            else{
                CreateTransactionPersonAdapter.PersonViewHolder holder = (CreateTransactionPersonAdapter.PersonViewHolder) rvWhom.findViewHolderForAdapterPosition(calcViewPosition);
                holder.sum.setText(result);
            }
        }
    }
}
