package com.abacaba.ha.rv;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abacaba.ha.activities.MainActivity;
import com.abacaba.ha.R;
import com.abacaba.ha.activities.AddTransActivity;
import com.abacaba.ha.actors.TransactionList;

import java.util.List;

public class RVTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADD = 0;
    private static final int WATCH = 1;
    private static final int LAYOUT_BUTTON= 0;
    private static final int LAYOUT_PERSON= 1;
    MainActivity context;
    Integer id;
    Integer trans_id;
    @Override
    public int getItemViewType(int position)
    {
        if(position==transactionList.size())
            return LAYOUT_BUTTON;
        else
            return LAYOUT_PERSON;
    }

    List<TransactionList> transactionList;
    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView personName;
        public TransactionViewHolder(@NonNull View itemView)  {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.transactionTextView);
        }
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder{
        Button btn;
        public ButtonViewHolder(View itemView) {
            super(itemView);
            btn = (Button)itemView.findViewById(R.id.addTransactionBtn);
        }
    }

    public RVTransactionAdapter(List<TransactionList> transactionList, MainActivity context, Integer id){
        this.transactionList = transactionList;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==LAYOUT_BUTTON)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ransaction,parent,false);
            viewHolder = new RVTransactionAdapter.ButtonViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction,parent,false);
            viewHolder= new RVTransactionAdapter.TransactionViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == LAYOUT_BUTTON){
            RVTransactionAdapter.ButtonViewHolder btnHolder = (RVTransactionAdapter.ButtonViewHolder)holder;
            btnHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddTransActivity.class);
                    intent.putExtra("TABLE_ID", id);
                    intent.putExtra("TYPE", ADD);
                    context.startActivity(intent);
                }
            });
        }
        else{
            RVTransactionAdapter.TransactionViewHolder personViewHolder = (RVTransactionAdapter.TransactionViewHolder)holder;
            personViewHolder.personName.setText(transactionList.get(position).getName());
            personViewHolder.personName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddTransActivity.class);
                    intent.putExtra("TABLE_ID", id);
                    intent.putExtra("TYPE", WATCH);
                    intent.putExtra("TRANS_ID", transactionList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size() + 1;
    }

}
