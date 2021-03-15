package com.example.asynctask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatModel> chatList;
//    Integer[] fruitsImg;
//    String[] fruitsName;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;
//        this.fruitsImg = fruitsImg;
//        this.fruitsName = fruitsName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.text.setText(fruitsName[position]);
//        holder.img.setImageResource(fruitsImg[position]);

        switch (chatList.get(position).getSender()) {
            case 1:
                holder.rightText.setText(chatList.get(position).getMessage());
                holder.rightText.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.lefttext.setText(chatList.get(position).getMessage());
                holder.lefttext.setVisibility(View.VISIBLE);
                break;
        }





    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        ImageView img;
//        TextView text;

        TextView lefttext,rightText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//
//            img = itemView.findViewById(R.id.listImageView);
//            text = itemView.findViewById(R.id.listText);

            lefttext = itemView.findViewById(R.id.left_chat);
            rightText = itemView.findViewById(R.id.right_chat);

        }
    }
}
