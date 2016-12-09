package com.leonelatencio.pokedexqr.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonelatencio.pokedexqr.models.Pokemon;
import com.leonelatencio.pokedexqr.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Custom ArrayAdapter Pokemon Adapter for displaying list items in listview
 *
 * @author API Plug
 * @version v1.0
 *
 */
public class PokemonAdapter extends ArrayAdapter<Pokemon> {
    private static final String PREFS_NAME = "ScannedPokemon";
    SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
    Set<String> scanned = settings.getStringSet("scanned", null);

    private static class ViewHolder {
        TextView number;
        TextView name;
        TextView type1;
        TextView type2;
        TextView shiny;
        TextView sex;
        TextView scanned;
        ImageView imageView;

    }

    public PokemonAdapter(Context context, List<Pokemon> pokemon) {
        super(context, 0, pokemon);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Pokemon pokemon = getItem(position);

        ViewHolder viewHolder; //check for cache
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_pokemon, parent, false);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.type1 = (TextView) convertView.findViewById(R.id.type1);
            viewHolder.type2 = (TextView) convertView.findViewById(R.id.type2);
            viewHolder.shiny = (TextView) convertView.findViewById(R.id.drawable_shiny);
            viewHolder.sex = (TextView) convertView.findViewById(R.id.drawable_sex);
            viewHolder.scanned = (TextView) convertView.findViewById(R.id.drawable_scanned);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the pokemon object
        viewHolder.number.setText(pokemon.getNumber());
        viewHolder.name.setText(pokemon.getName());

        if (pokemon.getShiny() == 1) {
            viewHolder.shiny.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_circle, 0, 0, 0);
        } else {
            viewHolder.shiny.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        switch (pokemon.getSex()) {
            case 1:
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gender_female, 0, 0, 0);
                break;
            case 2:
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gender_male, 0, 0, 0);
                break;
            default:
                viewHolder.sex.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        if (scanned != null) {
            if (scanned.contains(Integer.toString(pokemon.getId()))) {
                viewHolder.scanned.setCompoundDrawablesWithIntrinsicBounds(R.drawable.catched, 0, 0, 0);
            } else {
                viewHolder.scanned.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }

        viewHolder.type1.setText(pokemon.getType1());
        int color = getColorByName(pokemon.getType1().toLowerCase());
        viewHolder.type1.setTextColor(ContextCompat.getColor(getContext(), color));

        if (pokemon.getType2() != null) {
            viewHolder.type2.setText(pokemon.getType2());
            int color2 = getColorByName(pokemon.getType2().toLowerCase());
            Log.d("color", "Color: " + color2 + " type: " + pokemon.getType2());
            viewHolder.type2.setTextColor(ContextCompat.getColor(getContext(), color2));
        } else {
            viewHolder.type2.setText("");
        }

        if(pokemon.getFilename()!=""){
            String filename = "http://leonelatencio.com/PokemonQR/Pics/"
                    + pokemon.getNumber() + "-" + pokemon.getFilename();
            Picasso.with(getContext())
                    .load(filename)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(viewHolder.imageView);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public int getColorByName( String name ) {
        int colorId = 0;

        try {
            Class res = R.color.class;
            Field field = res.getField( name );
            colorId = field.getInt(null);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return colorId;
    }
}
