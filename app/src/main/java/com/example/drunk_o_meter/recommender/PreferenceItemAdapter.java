package com.example.drunk_o_meter.recommender;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drunk_o_meter.R;

import java.util.ArrayList;

public class PreferenceItemAdapter extends RecyclerView.Adapter<PreferenceItemAdapter.ViewHolder> {

    private DrinkType drinkType;

    /**
     * The list of drinks that are getting represented
     */
    private ArrayList<String> items;


    public PreferenceItemAdapter(DrinkType drinkType, ArrayList<String> items) {
        this.drinkType = drinkType;
        this.items = items;

        //TODO Remove after testing
        if (items == null) {
            this.items = new ArrayList<String>();
            this.items.add("Example Drink");
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
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items.get(position);

        //TODO @Kathrin: hier hole und sichere ich die Einstellungen je Drink Type (dann können wir später die ganze Liste rausholen und random etwas vorschlagen)
        //TODO: ganz am Anfang alle Drinks aus csv lesen --> alle aktivieren (in PreferencesFragment)
        boolean checked = false;
        switch (drinkType) {
            case WINE:
                //TODO get from user data
                //checked = Settings.DRINKS.WINE.contains(items.get(position));
                break;
            case BEER:
                //TODO get from user data
                //checked = Settings.DRINKS.BEER.contains(items.get(position));
                break;
            case COCKTAIL:
                //TODO get from user data
                //checked = Settings.DRINKS.COCKTAIL.contains(items.get(position));
                break;
            case SHOT:
                //TODO get from user data
                //checked = Settings.DRINKS.SHOT.contains(items.get(position));
                break;
            case HOT:
                //TODO get from user data
                //checked = Settings.DRINKS.HOT.contains(items.get(position));
                break;
        }

        holder.checkText.setText(text);
        holder.checkBox.setChecked(checked);

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (holder.checkBox.isChecked()) {
                switch (drinkType) {
                    case WINE:
                        //TODO save to user data
                        //Settings.DRINKS.WINE.add(items.get(holder.getAdapterPosition()));;
                        break;
                    case BEER:
                        //TODO save to user data
                        //Settings.DRINKS.BEER.add(items.get(holder.getAdapterPosition()));
                        break;
                    case COCKTAIL:
                        //TODO save to user data
                        //Settings.DRINKS.COCKTAIL.add(items.get(holder.getAdapterPosition()));
                        break;
                    case SHOT:
                        //TODO save to user data
                        //Settings.DRINKS.SHOT.add(items.get(holder.getAdapterPosition()));
                        break;
                    case HOT:
                        //TODO save to user data
                        //Settings.DRINKS.HOT.add(items.get(holder.getAdapterPosition()));
                        break;
                }
            } else {
                switch (drinkType) {
                    case WINE:
                        //TODO save to user data
                        //Settings.DRINKS.WINE.remove(items.get(holder.getAdapterPosition()));;
                        break;
                    case BEER:
                        //TODO save to user data
                        //Settings.DRINKS.BEER.remove(items.get(holder.getAdapterPosition()));
                        break;
                    case COCKTAIL:
                        //TODO save to user data
                        //Settings.DRINKS.COCKTAIL.remove(items.get(holder.getAdapterPosition()));
                        break;
                    case SHOT:
                        //TODO save to user data
                        //Settings.DRINKS.SHOT.remove(items.get(holder.getAdapterPosition()));
                        break;
                    case HOT:
                        //TODO save to user data
                        //Settings.DRINKS.HOT.remove(items.get(holder.getAdapterPosition()));
                        break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
