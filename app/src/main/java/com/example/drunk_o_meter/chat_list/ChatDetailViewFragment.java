package com.example.drunk_o_meter.chat_list;

import android.graphics.Color;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drunk_o_meter.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatDetailViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatDetailViewFragment extends Fragment {

    // TODO: Add selfie
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_SAFETOTEXT = "false";

    private String date;
    private String time;
    private String content;
    private boolean safeToText;

    private View layout;

    public ChatDetailViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date
     * @param time
     * @param content
     * @param safeToText
     * @return A new instance of fragment ChatDetailViewFragment.
     */
    public static ChatDetailViewFragment newInstance(String date, String time, String content, boolean safeToText) {
        ChatDetailViewFragment fragment = new ChatDetailViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_CONTENT, content);
        args.putBoolean(ARG_SAFETOTEXT, safeToText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
            time = getArguments().getString(ARG_TIME);
            content = getArguments().getString(ARG_CONTENT);
            safeToText = getArguments().getBoolean(ARG_SAFETOTEXT);
        }
    }

    //TODO: insert values to layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_chat_detail_view, container, false);

        Log.d("Chat Detail", "content: "+ this.content);
        ImageView selfieView = layout.findViewById(R.id.chat_detail_selfie);
        TextView contentView = layout.findViewById(R.id.chat_detail_content);
        TextView dateView = layout.findViewById(R.id.chat_detail_date);
        TextView timeView = layout.findViewById(R.id.chat_detail_time);
        TextView safeToTextView = layout.findViewById(R.id.chat_detail_safeToText);

        //TODO: change selfie
        contentView.setText(content);
        dateView.setText(date);
        timeView.setText(time);

        if (safeToText) {
            safeToTextView.setText("Safe to text");
            safeToTextView.setTextColor(Color.GREEN);
        } else {
            safeToTextView.setText("Not safe to text");
            safeToTextView.setTextColor(Color.RED);

        }

        Log.d("Chat Detail", "content view: " + contentView.getText());
        return layout;
    }


}