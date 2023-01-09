package com.example.drunk_o_meter.chat_list;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drunk_o_meter.ChatsFragment;
import com.example.drunk_o_meter.R;

import java.util.ArrayList;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder>{
    /**
     * List of chats that are getting represented
     */
    private final ArrayList<ChatInfo> items;
    private final FragmentManager fragmentManager;

    public ChatItemAdapter(ArrayList<ChatInfo> items, FragmentManager fragmentManager) {
        this.items = items;
        this.fragmentManager = fragmentManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView selfieImage;
        public TextView dateText;
        public TextView timeText;
        public TextView contentText;
        public ImageView safeToTextImage;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            selfieImage = (ImageView) v.findViewById(R.id.chat_item_selfie);
            dateText = (TextView) v.findViewById(R.id.chat_item_date);
            timeText = (TextView) v.findViewById(R.id.chat_item_time);
            contentText = (TextView) v.findViewById(R.id.chat_item_content);
            safeToTextImage = (ImageView) v.findViewById(R.id.chat_item_check);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.fragment_chat_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO: get selfie
        String date = items.get(position).date;
        String time = items.get(position).time;
        String content = items.get(position).content_short;
        boolean safeToText = items.get(position).safeToText;

        holder.dateText.setText(date);
        holder.timeText.setText(time);
        holder.contentText.setText(content);

        //TODO: add selfie of text message
        //holder.selfieImage.setImageResource(...);

        if (safeToText) {
            holder.safeToTextImage.setBackgroundColor(Color.GREEN);
            holder.safeToTextImage.setImageResource(R.drawable.ic_safe_to_text);
        } else {
            holder.safeToTextImage.setBackgroundColor(Color.RED);
            holder.safeToTextImage.setImageResource(R.drawable.ic_not_safe_to_text);
        }

        ChatsFragment chatsFragment = (ChatsFragment)fragmentManager.findFragmentByTag("chatsFragment");
        holder.itemView.setOnClickListener(view -> {
                chatsFragment.onClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
