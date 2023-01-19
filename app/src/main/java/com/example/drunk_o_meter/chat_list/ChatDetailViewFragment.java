package com.example.drunk_o_meter.chat_list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drunk_o_meter.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatDetailViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatDetailViewFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String ARG_TIME = "time";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_RECIPIENT = "recipient";
    private static final String ARG_SELFIE = "selfie";
    private static final String ARG_SAFETOTEXT = "false";

    private String date;
    private String time;
    private String content;
    private String recipient;
    private Bitmap selfie;
    private boolean safeToText;

    private View layout;

    //TODO: add copy to clipboard for safe to text messages

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
     * @param recipient
     * @param selfie
     * @param safeToText
     * @return A new instance of fragment ChatDetailViewFragment.
     */
    public static ChatDetailViewFragment newInstance(String date, String time, String content, String recipient, Bitmap selfie, boolean safeToText) {
        ChatDetailViewFragment fragment = new ChatDetailViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        args.putString(ARG_TIME, time);
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_RECIPIENT, recipient);
        args.putString(ARG_SELFIE, bitMapToString(selfie));
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
            recipient = getArguments().getString(ARG_RECIPIENT);
            selfie = stringToBitMap(getArguments().getString(ARG_SELFIE));
            safeToText = getArguments().getBoolean(ARG_SAFETOTEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_chat_detail_view, container, false);

        ImageView selfieView = layout.findViewById(R.id.chat_detail_selfie);
        TextView contentView = layout.findViewById(R.id.chat_detail_content);
        TextView dateView = layout.findViewById(R.id.chat_detail_date);
        TextView timeView = layout.findViewById(R.id.chat_detail_time);
        TextView recipientView = layout.findViewById(R.id.chat_detail_recipient);
        TextView safeToTextView = layout.findViewById(R.id.chat_detail_safeToText_caption);
        ImageButton safeToTextImage = layout.findViewById(R.id.chat_detail_safeToText_image);

        contentView.setText(content);
        dateView.setText(date);
        timeView.setText(time);
        recipientView.setText("To " + recipient);
        selfieView.setImageBitmap(selfie);

        if (safeToText) {
            safeToTextView.setText("Safe to text");
            safeToTextView.setTextColor(Color.GREEN);
            safeToTextImage.setBackgroundColor(Color.GREEN);
            safeToTextImage.setImageResource(R.drawable.ic_safe_to_text);
        } else {
            safeToTextView.setText("Not safe to text");
            safeToTextView.setTextColor(Color.RED);
            safeToTextImage.setBackgroundColor(Color.RED);
            safeToTextImage.setImageResource(R.drawable.ic_not_safe_to_text);

        }

        return layout;
    }



    //Helper

    public static String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}