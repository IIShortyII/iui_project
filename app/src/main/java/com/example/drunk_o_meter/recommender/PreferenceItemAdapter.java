package com.example.drunk_o_meter.recommender;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drunk_o_meter.R;
import com.example.drunk_o_meter.userdata.DataHandler;
import com.example.drunk_o_meter.userdata.UserData;

import java.util.ArrayList;

public class PreferenceItemAdapter extends RecyclerView.Adapter<PreferenceItemAdapter.ViewHolder> {

    private DrinkType drinkType;

    /**
     * The list of drinks that are getting represented
     */
    private ArrayList<String> items;
    private View layout;


    public PreferenceItemAdapter(DrinkType drinkType, ArrayList<String> items) {
        this.drinkType = drinkType;
        this.items = items;

        if (items == null) {
            this.items = new ArrayList<String>();
            Log.d("Error", "No items in drinks list of type "+ drinkType.name());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView checkText;
        public CheckBox checkBox;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            checkText = v.findViewById(R.id.preference_item_text);
            checkBox = v.findViewById(R.id.preference_item_box);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.fragment_preference_item, parent, false);
        this.layout = v;
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items.get(position);

        boolean checked = false;
        switch (drinkType) {
            case WINE:
                checked = UserData.DRINKS.get(DrinkType.WINE).contains(items.get(position));
                break;
            case BEER:
                checked = UserData.DRINKS.get(DrinkType.BEER).contains(items.get(position));
                break;
            case COCKTAIL:
                checked = UserData.DRINKS.get(DrinkType.COCKTAIL).contains(items.get(position));
                break;
            case SHOT:
                checked = UserData.DRINKS.get(DrinkType.SHOT).contains(items.get(position));
                break;
            case HOT:
                checked = UserData.DRINKS.get(DrinkType.HOT).contains(items.get(position));
                break;
        }

        holder.checkText.setText(text);
        holder.checkBox.setChecked(checked);

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (holder.checkBox.isChecked()) {
                switch (drinkType) {
                    case WINE:
                        UserData.DRINKS.get(DrinkType.WINE).add(items.get(holder.getAdapterPosition()));
                        break;
                    case BEER:
                        UserData.DRINKS.get(DrinkType.BEER).add(items.get(holder.getAdapterPosition()));
                        break;
                    case COCKTAIL:
                        UserData.DRINKS.get(DrinkType.COCKTAIL).add(items.get(holder.getAdapterPosition()));
                        break;
                    case SHOT:
                        UserData.DRINKS.get(DrinkType.SHOT).add(items.get(holder.getAdapterPosition()));
                        break;
                    case HOT:
                        UserData.DRINKS.get(DrinkType.HOT).add(items.get(holder.getAdapterPosition()));
                        break;
                }
            } else {
                switch (drinkType) {
                    case WINE:
                        UserData.DRINKS.get(DrinkType.WINE).remove(items.get(holder.getAdapterPosition()));
                        break;
                    case BEER:
                        UserData.DRINKS.get(DrinkType.BEER).remove(items.get(holder.getAdapterPosition()));
                        break;
                    case COCKTAIL:
                        UserData.DRINKS.get(DrinkType.COCKTAIL).remove(items.get(holder.getAdapterPosition()));
                        break;
                    case SHOT:
                        UserData.DRINKS.get(DrinkType.SHOT).remove(items.get(holder.getAdapterPosition()));
                        break;
                    case HOT:
                        UserData.DRINKS.get(DrinkType.HOT).remove(items.get(holder.getAdapterPosition()));
                        break;
                }
            }

            // update storage
            DataHandler.storeSettings(layout.getContext());
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
