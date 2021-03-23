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
import com.abacaba.ha.activities.AddMemberActivity;
import com.abacaba.ha.actors.Person;

import java.util.List;

public class RVPersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LAYOUT_BUTTON= 0;
    private static final int LAYOUT_PERSON= 1;
    MainActivity context;
    Integer id;

    @Override
    public int getItemViewType(int position)
    {
        if(position==personList.size())
            return LAYOUT_BUTTON;
        else
            return LAYOUT_PERSON;
    }

    List<Person> personList;
    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView personName;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.personTextView);
        }
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder{
        Button btn;
        public ButtonViewHolder(View itemView) {
            super(itemView);
            btn = (Button)itemView.findViewById(R.id.addMemberBtn);
        }
    }

    public RVPersonAdapter(List<Person> persons, MainActivity context, Integer id){
        this.personList = persons;
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_member,parent,false);
            viewHolder = new ButtonViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member,parent,false);
            viewHolder= new PersonViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LAYOUT_BUTTON){
            ButtonViewHolder btnHolder = (ButtonViewHolder)holder;
            btnHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddMemberActivity.class);
                    intent.putExtra("TABLE_ID", id);
                    context.startActivity(intent);
                }
            });
        }
        else{
            PersonViewHolder personViewHolder = (PersonViewHolder)holder;
            personViewHolder.personName.setText(personList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return personList.size() + 1;
    }
}
