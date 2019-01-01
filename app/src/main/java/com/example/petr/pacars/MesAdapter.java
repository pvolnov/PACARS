package com.example.petr.pacars;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MesAdapter extends RecyclerView.Adapter<MesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Message> messages;
    private Context context;

    MesAdapter(Context context, List<Message> messages) {
        this.context=context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.mes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MesAdapter.ViewHolder holder, int position) {
        final Message mes = messages.get(position);
        View.OnClickListener clc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Letter.class);
                intent.putExtra("text", mes.getFullText());
                intent.putExtra("name", mes.getName());
                context.startActivity(intent);
                //Toast.makeText(context, "Сейчас все будет", Toast.LENGTH_SHORT).show();
            }
        };
        holder.imageView.setOnClickListener (clc);
        holder.textView.setOnClickListener (clc);
        holder.nameView.setOnClickListener (clc);



        mes.getImage(context,holder.imageView);
        holder.nameView.setText(mes.getName());
        holder.textView.setText(mes.getText());
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, textView;
        CardView cv;
        ViewHolder(View view){
            super(view);
            cv = (CardView)itemView.findViewById(R.id.cv);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
