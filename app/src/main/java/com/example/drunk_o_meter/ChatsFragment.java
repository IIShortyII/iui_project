package com.example.drunk_o_meter;

import android.os.Bundle;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drunk_o_meter.chat_list.ChatDetailViewFragment;
import com.example.drunk_o_meter.chat_list.ChatInfo;
import com.example.drunk_o_meter.chat_list.ChatItemAdapter;
import com.example.drunk_o_meter.chat_list.OnItemClickListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment implements OnItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<ChatInfo> allChats = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //TODO: where are chats saved? --> enter here
        ChatInfo exampleChat1 = new ChatInfo("Example text goes like this and like this", false);
        ChatInfo exampleChat2 = new ChatInfo("Example text goes like that and like that", true);
        allChats.add(exampleChat1);
        allChats.add(exampleChat2);

        RecyclerView recyclerView = getView().findViewById(R.id.chats_item_list);
        recyclerView.setHasFixedSize(true);
        ChatItemAdapter adapter = new ChatItemAdapter(allChats, this.getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        String date = allChats.get(position).date;
        String time = allChats.get(position).time;
        String content = allChats.get(position).content;
        boolean safeToText = allChats.get(position).safeToText;

        //TODO: does this work? --> test
        ChatDetailViewFragment chatDetailViewFragment= ChatDetailViewFragment.newInstance(date, time, content, safeToText);

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_chats, chatDetailViewFragment)
                .addToBackStack(null)
                .commit();
    }
}