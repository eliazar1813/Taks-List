package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class AdapterItems extends  RecyclerView.Adapter<AdapterItems.ViewHolder>{

    LinkedList<String> toDoList;
    Context context;

    OnLongClickListener LongClickListener;

    OnClickListener clickListener;

    public interface OnClickListener{
        void onItemClick(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClick(int position);
    }

    public AdapterItems(Context context, LinkedList<String> toDoList,
                        OnLongClickListener onLongClickListener,OnClickListener clickListener){
        this.toDoList = toDoList;
        this.context = context;
        this.LongClickListener = onLongClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    public AdapterItems.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.itemsview, parent, false));
    }

    public void onBindViewHolder(@NonNull AdapterItems.ViewHolder holder, int position) {

        String currentItem = toDoList.get(position);
        holder.bindTo(currentItem);
    }

    public int getItemCount() {
        return toDoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;
        ImageView priorityImage;
        String [] options;
        String radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.textViewItem);
            priorityImage = itemView.findViewById(R.id.priority);

            options = new String[]{"No Priority","Medium Priority","High Priority"};


            priorityImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Change Priority", "Priority OnClick Method");
                    final String currentItem = toDoList.get(getAdapterPosition());

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setTitle("Select Priority");
                    radioButton = options[0];
                    dialogBuilder.setSingleChoiceItems(options, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            radioButton = options[which];
                        }
                    });

                    dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                            if(radioButton.equals(options[0])){
                                priorityImage.setImageResource(R.drawable.ic_nopriority_);
                            }else if(radioButton.equals(options[1])){
                                priorityImage.setImageResource(R.drawable.ic_mediumpriority);
                            }else{
                                priorityImage.setImageResource(R.drawable.ic_highpriority);
                            }

                        }
                    });

                    dialogBuilder.create().show();

                }
            });
        }


        public void bindTo(String currentItem){
            itemText.setText(currentItem);
            itemText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LongClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });

            itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }


    }
}
