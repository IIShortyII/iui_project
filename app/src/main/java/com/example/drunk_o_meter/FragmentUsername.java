package com.example.drunk_o_meter;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUsername extends Fragment {

    private Button next;

    private View layout;


    public FragmentUsername() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.layout = inflater.inflate(R.layout.fragment_username, container, false);

        // Set next button
        this.next = layout.findViewById(R.id.saveUsername);
        next.setText(getResources().getString(R.string.next_name));
        next.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return layout;
    }

}