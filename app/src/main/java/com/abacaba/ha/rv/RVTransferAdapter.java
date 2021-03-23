package com.abacaba.ha.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abacaba.ha.activities.MainActivity;
import com.abacaba.ha.R;
import com.abacaba.ha.actors.Transfer;

import java.util.List;

public class RVTransferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MainActivity context;
    Integer id;

    List<Transfer> transactionList;
    public static class TransferViewHolder extends RecyclerView.ViewHolder{
        TextView transfer;
        public TransferViewHolder(@NonNull View itemView)  {
            super(itemView);
            transfer = (TextView)itemView.findViewById(R.id.transactionTextView);
        }
    }

    public RVTransferAdapter(List<Transfer> transactionList, MainActivity context, Integer id){
        this.transactionList = transactionList;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction,parent,false);
        viewHolder= new RVTransferAdapter.TransferViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RVTransferAdapter.TransferViewHolder personViewHolder = (RVTransferAdapter.TransferViewHolder)holder;
        personViewHolder.transfer.setText(transactionList.get(position).getFromName() + " \uD83D\uDC49 " + transactionList.get(position).getToName() + " " + transactionList.get(position).getSum() + " units");
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}