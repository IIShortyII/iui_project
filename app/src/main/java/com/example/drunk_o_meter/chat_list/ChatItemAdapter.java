package com.example.drunk_o_meter.chat_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drunk_o_meter.ChatsFragment;
import com.example.drunk_o_meter.MainActivity;
import com.example.drunk_o_meter.R;

import java.util.ArrayList;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ViewHolder>{
    /**
     * List of chats that are getting represented
     */
    private final ArrayList<ChatInfo> items;

    private final Context context;

    public ChatItemAdapter(ArrayList<ChatInfo> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText;
        public TextView timeText;
        public TextView contentText;
        public ImageView chatImage;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            dateText = (TextView) v.findViewById(R.id.chat_item_date);
            timeText = (TextView) v.findViewById(R.id.chat_item_time);
            contentText = (TextView) v.findViewById(R.id.chat_item_content);
            chatImage = (ImageView) v.findViewById(R.id.chat_item_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.fragment_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = items.get(position).date;
        String time = items.get(position).time;
        String content = items.get(position).content_short;
        boolean safeToText = items.get(position).safeToText;

        holder.dateText.setText(date);
        holder.timeText.setText(time);
        holder.contentText.setText(content);
        if (safeToText) {
            holder.chatImage.setImageResource(R.drawable.check);
        } else {
            holder.chatImage.setImageResource(R.drawable.cross);
        }

        //TODO: Add on click listener for items which leads to full view of chat --> implementation started in ChatsFragment
        /*holder.itemView.setOnClickListener(view -> {
            if (context instanceof MainActivity) {
                ((MainActivity)context).onClick(holder.getAbsoluteAdapterPosition());
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
