package com.example.drunk_o_meter;

import static com.example.drunk_o_meter.userdata.UserData.USERNAME;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;

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
        this.next = getActivity().findViewById(R.id.nextBtn);
        next.setText(getResources().getString(R.string.next_name));
        next.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        return layout;
    }

}