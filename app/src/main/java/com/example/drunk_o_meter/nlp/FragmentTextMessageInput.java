package com.example.drunk_o_meter.nlp;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.example.drunk_o_meter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTextMessageInput extends Fragment {

    private View layout;


    public FragmentTextMessageInput() {
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

        this.layout = inflater.inflate(R.layout.fragment_text_message_input, container, false);

        EditText textMessageEditText = layout.findViewById(R.id.textMessageTextInput);
        textMessageEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textMessageEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        // Inflate the layout for this fragment
        return layout;
    }

}