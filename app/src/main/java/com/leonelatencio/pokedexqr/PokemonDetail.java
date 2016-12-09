package com.leonelatencio.pokedexqr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.leonelatencio.pokedexqr.models.Pokemon;
import com.leonelatencio.pokedexqr.rest.ApiClient;
import com.leonelatencio.pokedexqr.utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetail extends AppCompatActivity {
    public static final String PREFS_NAME = "ScannedPokemon";
    String poke_name = "";
    String poke_number = "";
    int poke_id = 0;
    TextView scanned_tv;
    Set<String> scanned;
    SharedPreferences settings;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        poke_id = extras.getInt("id");
        scanned_tv = (TextView) findViewById(R.id.scanned);

        //get scanned Pokemon
        settings = getSharedPreferences(PREFS_NAME, 0);
        scanned = settings.getStringSet("scanned", null);

        if (scanned != null) {
            if (scanned.contains(Integer.toString(poke_id))) {
                scanned_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.catched_big, 0, 0, 0);
            }
        }

        //get Layout items
        final ScrollView scroll = (ScrollView) findViewById(R.id.DetailScroll);
        final TextView num_tv = (TextView) findViewById(R.id.pokemon_number);
        final TextView name_tv = (TextView) findViewById(R.id.pokemon_name);
        final TextView loc_tv = (TextView) findViewById(R.id.location_body);
        final TextView stats_tv = (TextView) findViewById(R.id.stats_body);
        final TextView ability_tv = (TextView) findViewById(R.id.abilities_body);
        final TextView shiny_tv = (TextView) findViewById(R.id.shiny_badge);
        final TextView gender_tv = (TextView) findViewById(R.id.gender_badge);
        final TextView notes_tv = (TextView) findViewById(R.id.notes_body);
        final ImageView qr_iv = (ImageView) findViewById(R.id.pokemon_qr);
        final ImageView poke_iv = (ImageView) findViewById(R.id.pokemon_pic);

        Call<Pokemon> call;
        call = ApiClient.get().detailPokemon(poke_id);
        call.enqueue(new Callback<Pokemon>() {
            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.d("APIPlug", "Error Occurred: " + t.getMessage());
            }

            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon=response.body();

                poke_name = pokemon.getName();
                poke_number = pokemon.getNumber();

                num_tv.setText(pokemon.getNumber());
                name_tv.setText(pokemon.getName());
                if (pokemon.getLocation() != null) {
                    loc_tv.setText(pokemon.getLocation());
                }
                if (pokemon.getStats() != null) {
                    stats_tv.setText(pokemon.getStats());
                }
                if (pokemon.getNotes() != null) {
                    notes_tv.setText(pokemon.getNotes());
                }
                if (pokemon.getAbilities() != null) {
                    ability_tv.setText(pokemon.getAbilities());
                }

                if (pokemon.getShiny() == 1) {
                    shiny_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star_circle, 0, 0, 0);
                } else {
                    shiny_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                switch (pokemon.getSex()) {
                    case 1:
                        gender_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gender_female, 0, 0, 0);
                        break;
                    case 2:
                        gender_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gender_male, 0, 0, 0);
                        break;
                    default:
                        gender_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                String filename1 = "http://leonelatencio.com/PokemonQR/Pics/"
                        + pokemon.getNumber() + "-" + pokemon.getFilename();
                String filename2 = "http://leonelatencio.com/PokemonQR/Poke/"
                        + pokemon.getNumber() + "-" + pokemon.getFilename();

                Picasso.with(getApplicationContext())
                        .load(filename1)
                        .error(R.drawable.placeholder)
                        .into(qr_iv);

                Picasso.with(getApplicationContext())
                        .load(filename2)
                        .into(poke_iv);
            }
        });

        scroll.setOnTouchListener(new OnSwipeTouchListener(PokemonDetail.this) {
            public void onSwipeRight() {
                if (poke_id > 1) {
                    Intent detail = new Intent(PokemonDetail.this, PokemonDetail.class);
                    detail.putExtra("id", poke_id - 1);
                    startActivity(detail);
                    finish();
                }
            }

            public void onSwipeLeft() {
                if (poke_id < 468) {
                    Intent detail = new Intent(PokemonDetail.this, PokemonDetail.class);
                    detail.putExtra("id", poke_id + 1);
                    startActivity(detail);
                    finish();
                }
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showStatsDetails(View v) {
        Toast.makeText(this, "HP/Atk/Def/SpA/SpD/Spe", Toast.LENGTH_LONG).show();
    }

    public void addToScanned(View v) {
        scanned_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.catched_big, 0, 0, 0);

        Set<String> set = new HashSet<>();
        if (scanned != null) {
            set.addAll(scanned);
        }
        set.add(Integer.toString(poke_id));
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("scanned", set);
        editor.apply();
    }
}
