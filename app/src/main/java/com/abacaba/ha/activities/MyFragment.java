package com.abacaba.ha.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abacaba.ha.R;
import com.abacaba.ha.actors.Company;
import com.abacaba.ha.actors.GraphVertex;
import com.abacaba.ha.actors.Person;
import com.abacaba.ha.actors.TransactionList;
import com.abacaba.ha.actors.TransactionObj;
import com.abacaba.ha.actors.Transfer;
import com.abacaba.ha.db.BillsContract;
import com.abacaba.ha.db.BillsDbHelper;
import com.abacaba.ha.rv.DividerItemDecorator;
import com.abacaba.ha.rv.RVPersonAdapter;
import com.abacaba.ha.rv.RVTransactionAdapter;
import com.abacaba.ha.rv.RVTransferAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "arg_page";
    public static final String ARG_LAST = "arg_last";
    public static final String ARG_NAME = "arg_name";
    public static final String ARG_ID = "arg_id";
    private MainActivity activity;
    int cycle_st, cycle_end;
    ArrayList<Transfer> transfers;
    public MyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Bundle args = getArguments();
        TextView textView = new TextView(getActivity());
        if (args.getBoolean(ARG_LAST)) {
            View view = inflater.inflate(R.layout.add_company, null, false);
            Button btn = view.findViewById(R.id.addCompBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddCompActivity.class);
                    startActivity(intent);
                }
            });
            return view;
        } else {
            View view = inflater.inflate(R.layout.company, null, false);
            RecyclerView rv = view.findViewById(R.id.personsRecyclerview);
            rv.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.divider)));
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            rv.setLayoutManager(llm);
            RVPersonAdapter adapter = new RVPersonAdapter(getPersons(args.getInt(ARG_ID)), activity, args.getInt(ARG_ID)); //!!!!!!!!!!!!!!!!!!!
            rv.setAdapter(adapter);

            llm = new LinearLayoutManager(activity);
            RecyclerView rvTrans = view.findViewById(R.id.transactionsRecyclerview);
            rvTrans.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.divider)));
            rvTrans.setLayoutManager(llm);
            RVTransactionAdapter adapterTrans = new RVTransactionAdapter(getTransactionsList(), activity, args.getInt(ARG_ID)); //!!!!!!!!!!!!!!!!!!!
            rvTrans.setAdapter(adapterTrans);

            /*ArrayList<ArrayList<GraphVertex>> graph = findMinGraph(args.getInt(ARG_ID));
            createTransfers(graph, args.getInt(ARG_ID));*/
            getTransfers(args.getInt(ARG_ID));
            llm = new LinearLayoutManager(activity);
            RecyclerView rvTransfers = view.findViewById(R.id.transfersRecyclerview);
            rvTransfers.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.divider)));
            rvTransfers.setLayoutManager(llm);
            RVTransferAdapter adapterTransfers = new RVTransferAdapter(transfers, activity, args.getInt(ARG_ID));
            rvTransfers.setAdapter(adapterTransfers);
            Button btn = view.findViewById(R.id.deleteCompBtn);
            btn.setOnClickListener(this);
            return view;
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public MyFragment newInstance(int pageNumber, ArrayList<Company> companies, MainActivity activity) {
        MyFragment fragment = new MyFragment();
        fragment.activity = activity;
        this.activity = activity;
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        if (pageNumber != companies.size()) {
            arguments.putString(ARG_NAME, companies.get(pageNumber).getName());
            arguments.putInt(ARG_ID, companies.get(pageNumber).getId());
            arguments.putBoolean(ARG_LAST, false);
        } else {
            arguments.putBoolean(ARG_LAST, true);
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new
                AlertDialog.Builder(getContext());
        dialog.setMessage("Are you sure? Group will be deleted.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Bundle args = getArguments();
                        BillsDbHelper dbHelper = new BillsDbHelper(activity.getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int deletedRows = db.delete(BillsContract.Company.TABLE_NAME, BillsContract.Company._ID + "=" + args.getInt(ARG_ID), null);
                        deletedRows = db.delete(BillsContract.Person.TABLE_NAME, BillsContract.Person.COLUMN_NAME_COMPANYID + "=" + args.getInt(ARG_ID), null);
                        activity.updateTabs();
                    }
                });
        dialog.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public ArrayList<Person> getPersons(int id) {
        BillsDbHelper dbHelper = new BillsDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BillsContract.Person._ID,
                BillsContract.Person.COLUMN_NAME_NAME,
                BillsContract.Person.COLUMN_NAME_COMPANYID,
                BillsContract.Person.COLUMN_NAME_DEBT
        };
        String selection = BillsContract.Person.COLUMN_NAME_COMPANYID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
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
        while (cursor.moveToNext()) {
            Person person = new Person(cursor.getString(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_DEBT)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Person.COLUMN_NAME_COMPANYID)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.Person._ID)));
            people.add(person);
        }
        return people;
    }

    private ArrayList<TransactionList> getTransactionsList() {
        ArrayList<TransactionList> transactions = new ArrayList<>();
        final Bundle args = getArguments();
        BillsDbHelper dbHelper = new BillsDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = BillsContract.TransactionList.COLUMN_NAME_COMPANY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(args.getInt(ARG_ID))};
        Cursor cursor = db.query(
                BillsContract.TransactionList.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while (cursor.moveToNext()) {
            TransactionList tl = new TransactionList(cursor.getString(cursor.getColumnIndex(BillsContract.TransactionList.COLUMN_NAME_NAME)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.TransactionList._ID)),
                    cursor.getInt(cursor.getColumnIndex(BillsContract.TransactionList.COLUMN_NAME_COMPANY_ID)));
            transactions.add(tl);
        }
        return transactions;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    private ArrayList<ArrayList<GraphVertex>> findMinGraph(int id) {
        ArrayList<ArrayList<GraphVertex>> graph = createGraph(id);
        deleteMultipleEdges(graph);
        deleteCycles(graph);
        return graph;
    }

    private void getTransfers(int id){
        transfers = new ArrayList<>();
        ArrayList<Person> persons = getPersons(id);
        ArrayList<TransactionList> transactionLists = getTransactionsList();
        ArrayList<TransactionObj> transactions = getTransactions(transactionLists);
        ArrayList<Double> debts = new ArrayList<>();
        for (int i = 0; i < persons.size(); ++i) {
            debts.add(0.0);
            for (int j = 0; j < transactions.size(); ++j) {
                if (transactions.get(j).getFrom() == persons.get(i).getId()) {
                    debts.set(i, debts.get(i) - transactions.get(j).getSum());
                }
                if (transactions.get(j).getTo() == persons.get(i).getId()){
                    debts.set(i, debts.get(i) + transactions.get(j).getSum());
                }
            }
        }
        int i = 0, j = 0;
        while (i < persons.size() && j < persons.size()){
            while (i < persons.size() && debts.get(i) >= 0){
                ++i;
            }
            while (j < persons.size() && debts.get(j) <= 0){
                ++j;
            }
            if (i >= persons.size() || j >= persons.size()){
                break;
            }
            double sum = Math.min(-debts.get(i), debts.get(j));
            transfers.add(new Transfer(persons.get(i).getName(), persons.get(j).getName(), sum));
            debts.set(i, debts.get(i) + sum);
            debts.set(j, debts.get(j) - sum);
        }
    }

    private void createTransfers(ArrayList<ArrayList<GraphVertex>> graph, int id){
        transfers = new ArrayList<>();
        for (int i = 0; i < graph.size(); ++i){
            for (int j = 0; j < graph.get(i).size(); ++j){
                ArrayList<Person> persons = getPersons(id);
                transfers.add(new Transfer(persons.get(i).getName(), persons.get(graph.get(i).get(j).getNumber()).getName(), graph.get(i).get(j).getWeight()));
            }
        }
    }

    private void deleteMultipleEdges(ArrayList<ArrayList<GraphVertex>> graph){
        for (int i = 0; i < graph.size(); ++i){
            HashMap<Integer, Double> map = new HashMap<>();
            ArrayList<GraphVertex> vertexes = graph.get(i);
            for (int j = 0; j < vertexes.size(); ++j){
                if (map.containsKey(vertexes.get(j).getNumber())){
                    map.put(vertexes.get(j).getNumber(), map.get(vertexes.get(j).getNumber()) + vertexes.get(j).getWeight());
                }
                else{
                    map.put(vertexes.get(j).getNumber(), vertexes.get(j).getWeight());
                }
            }
            ArrayList<GraphVertex> new_vertexes = new ArrayList<>();
            for (Integer key: map.keySet()){
                new_vertexes.add(new GraphVertex(key, map.get(key)));
            }
            graph.remove(i);
            graph.add(i, new_vertexes);
        }
    }

    private void deleteCycles(ArrayList<ArrayList<GraphVertex>> graph) {
        for (int i = 0; i < graph.size(); ++i) {
            ArrayList<Integer> color = new ArrayList<>(graph.size());
            ArrayList<Integer> parent = new ArrayList<>(graph.size());
            ArrayList<Double> weights = new ArrayList<>(graph.size());
            cycle_st = -1;
            do {
                if (cycle_st != -1) {
                    int start_vertex, end_vertex, min_vertex = cycle_st;
                    ArrayList<Integer> cycle = new ArrayList<>();
                    cycle.add(cycle_st);
                    Double min_weight = weights.get(cycle_st);
                    for (int v = cycle_end; v != cycle_st; v = parent.get(v)){
                        cycle.add(v);
                        if (weights.get(v) < min_weight){
                            min_vertex = v;
                            min_weight = weights.get(v);
                        }
                    }
                    int min_parent = parent.get(min_vertex);
                    for (Integer index: cycle){
                        int par = parent.get(index);
                        for (int j = 0; j < graph.get(par).size(); ++j){
                            if (graph.get(par).get(j).getNumber() == index){
                                graph.get(par).get(j).setWeight(graph.get(par).get(j).getWeight() - min_weight);
                                if (graph.get(par).get(j).getWeight() == 0){
                                    graph.get(par).remove(j);
                                }
                                break;
                            }
                        }
                    }
                }
                color = new ArrayList<>(graph.size());
                parent = new ArrayList<>(graph.size());
                for (int j = 0; j < graph.size(); ++j) {
                    color.add(0);
                    parent.add(-1);
                    weights.add(0.0);
                }
                cycle_st = -1;
            } while (dfs(i, graph, color, parent, weights));
        }
    }

    private boolean dfs(int v, ArrayList<ArrayList<GraphVertex>> graph, ArrayList<Integer> color, ArrayList<Integer> parent, ArrayList<Double> weights){
        color.set(v, 1);
        for (int i = 0; i < graph.get(v).size(); ++i){
            int to = graph.get(v).get(i).getNumber();
            if (color.get(to) == 0){
                parent.set(to, v);
                weights.set(to, graph.get(v).get(i).getWeight());
                if (dfs(to, graph, color, parent, weights)){
                    return true;
                }
            }
            else if (color.get(to) == 1){
                cycle_end = v;
                cycle_st = to;
                weights.set(to, graph.get(v).get(i).getWeight());
                parent.set(to, v);
                return true;
            }
        }
        color.set(v, 2);
        return false;
    }

    private ArrayList<ArrayList<GraphVertex>> createGraph(int id) {
        ArrayList<Person> persons = getPersons(id);
        ArrayList<TransactionList> transactionLists = getTransactionsList();
        ArrayList<ArrayList<GraphVertex>> graph = new ArrayList<>();
        ArrayList<TransactionObj> transactions = getTransactions(transactionLists);
        for (int i = 0; i < persons.size(); ++i) {
            graph.add(new ArrayList<GraphVertex>());
            for (int j = 0; j < transactions.size(); ++j) {
                if (transactions.get(j).getFrom() == persons.get(i).getId()) {
                    for (int k = 0; k < persons.size(); ++k) {
                        if (transactions.get(j).getTo() == persons.get(k).getId()) {
                            graph.get(i).add(new GraphVertex(k, transactions.get(j).getSum()));
                            break;
                        }
                    }
                }
            }
        }
        return graph;
    }

    private ArrayList<TransactionObj> getTransactions(ArrayList<TransactionList> transactionLists) {
        ArrayList<TransactionObj> transactions = new ArrayList<>();
        BillsDbHelper dbHelper = new BillsDbHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for (int i = 0; i < transactionLists.size(); ++i) {
            String selection = BillsContract.Transaction.COLUMN_NAME_TRANSACTION_ID + " = ?";
            String[] selectionArgs = {String.valueOf(transactionLists.get(i).getId())};
            Cursor cursor = db.query(
                    BillsContract.Transaction.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            while (cursor.moveToNext()) {
                TransactionObj transaction = new TransactionObj(cursor.getInt(cursor.getColumnIndex(BillsContract.Transaction.COLUMN_NAME_FROM_PERSON)),
                        cursor.getInt(cursor.getColumnIndex(BillsContract.Transaction.COLUMN_NAME_TO_PERSON)),
                        cursor.getDouble(cursor.getColumnIndex(BillsContract.Transaction.COLUMN_NAME_SUM)));
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}