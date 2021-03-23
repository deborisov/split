package com.abacaba.ha.rv;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abacaba.ha.R;
import com.abacaba.ha.activities.AddTransActivity;
import com.abacaba.ha.actors.Payment;
import com.abacaba.ha.actors.Person;
import com.gp89developers.calculatorinputview.CalculatorBuilder;

import java.util.ArrayList;
import java.util.List;

public class CreateTransactionPersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADD = 0;
    private static final int WATCH = 1;
    AddTransActivity context;
    int type;
    List<Person> personList;
    List<Payment> payments;
    int editable;
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView personName;
        public EditText sum;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.personTextView);
            sum = (EditText)itemView.findViewById(R.id.editTextSum);
            sum.setText("0");
        }
    }

    public CreateTransactionPersonAdapter(List<Person> persons, AddTransActivity context, int type, ArrayList<Payment> payments, int editable){
        this.personList = persons;
        this.context = context;
        this.type = type;
        this.payments = payments;
        this.editable = editable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_for_trans, parent,false);
        viewHolder= new CreateTransactionPersonAdapter.PersonViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final CreateTransactionPersonAdapter.PersonViewHolder personViewHolder = (CreateTransactionPersonAdapter.PersonViewHolder)holder;
        personViewHolder.personName.setText(personList.get(position).getName());
        if(editable == WATCH){
            personViewHolder.sum.setFocusable(false);
            personViewHolder.sum.setFocusableInTouchMode(false);
            personViewHolder.sum.setClickable(false);
            for (int j = 0; j < payments.size(); ++j){
                if (payments.get(j).getPersonId() == personList.get(position).getId()){
                    if (payments.get(j).getType() == type){
                        personViewHolder.sum.setText(String.valueOf(payments.get(j).getSum()));
                    }
                }
            }
        }
        personViewHolder.sum.setFocusable(false);
        personViewHolder.sum.setFocusableInTouchMode(false);
        personViewHolder.sum.setClickable(false);
        personViewHolder.sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.calcViewPosition = position;
                context.calcViewType = type;
                new CalculatorBuilder()
                        .withTitle("TITLE")
                        .withValue(personViewHolder.sum.getText().toString())
                        .start(context);
            }
        });
        personViewHolder.sum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                context.updateSums(type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}
