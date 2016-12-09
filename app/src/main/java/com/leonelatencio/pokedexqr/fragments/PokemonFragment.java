package com.leonelatencio.pokedexqr.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.leonelatencio.pokedexqr.utils.EndlessScrollListener;
import com.leonelatencio.pokedexqr.MainActivity;
import com.leonelatencio.pokedexqr.PokemonDetail;
import com.leonelatencio.pokedexqr.R;
import com.leonelatencio.pokedexqr.adapters.PokemonAdapter;
import com.leonelatencio.pokedexqr.models.Pokemon;
import com.leonelatencio.pokedexqr.rest.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pokemon Fragment
 * @author LA
 * @version v1.0
 */
public class PokemonFragment extends Fragment {
    private List<Pokemon> pokemon;
    private GridView listView;
    private PokemonAdapter adapter;
    private Boolean search_made = false;
    private String pokemonType = "";

    public PokemonFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FeedFragment.
     */

    public static PokemonFragment newInstance() {
        PokemonFragment fragment = new PokemonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pokemon, container, false);
        listView = (GridView) view.findViewById(R.id.listView);
        if (savedInstanceState == null) {
            getPokemonList(false, "", 0);
            listView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    return getNextPokemon(totalItemsCount);
                }
            });
        } else {
            newInstance();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        final MenuItem fitem = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(fitem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pokemon_types, R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                pokemonType = arg0.getItemAtPosition(pos).toString();
                if (pos != 0)
                    Toast.makeText(getContext(), pokemonType, Toast.LENGTH_LONG).show();
                getPokemonList(false, "", 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit (String query){
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                getPokemonList(true, query, 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    Log.d("APIPlug", "Closing search... ");
                    search_made = false;
                    getPokemonList(false, "", 0);
                }
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }

    private void getPokemonList(final Boolean search, String search_str, int start) {
        Call<List<Pokemon>> call;
        final ProgressDialog loading = ProgressDialog.show(getActivity(),getContext().getString(R.string.loading_title),getContext().getString(R.string.loading_please_wait),false,false);
        if (search) {
            search_made = true;
            call = ApiClient.get().searchPokemon(search_str, pokemonType);
        } else {
            call = ApiClient.get().getPokemon(start, pokemonType);
        }
        call.enqueue(new Callback<List<Pokemon>>() {
            @Override
            public void onFailure(Call<List<Pokemon>> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), "Error loading the data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call<List<Pokemon>> call, Response<List<Pokemon>> response) {
                Log.d("APIPlug", "Successfully response fetched" );
                loading.dismiss();
                if (search) {
                    pokemon.clear();
                }
                // TODO: Check pokemon var is not null.
                pokemon=response.body();
                if (pokemon.size() > 0) {
                    showList();
                } else {
                    Log.d("APIPlug", "No item found");
                }
            }

        });

    }

    private Boolean getNextPokemon(int start) {
        if (!search_made) {
            Call<List<Pokemon>> call;
            Log.d("scroll", "start " + start + ", type " + pokemonType );
            call = ApiClient.get().getPokemon(start, pokemonType);
            call.enqueue(new Callback<List<Pokemon>>() {
                @Override
                public void onFailure(Call<List<Pokemon>> call, Throwable t) {
                    Log.d("APIPlug", "Error Occurred: " + t.getMessage());
                }

                @Override
                public void onResponse(Call<List<Pokemon>> call, Response<List<Pokemon>> response) {
                    Log.d("APIPlug", "Successfully fetched extra rows" );
                    List<Pokemon> more_pokemon=response.body();
                    if(more_pokemon.size()>0) {
                        pokemon.addAll(more_pokemon);
                        adapter.notifyDataSetChanged();
                    }else{
                        Log.d("APIPlug", "No more items found");
                    }
                }
            });

            return true;
        } else {
            return false;
        }
    }

    //Our method to show list
    private void showList() {
        Log.d("APIPlug", "Show List");
        adapter = new PokemonAdapter(getActivity(), pokemon);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pokemon clickedObj = (Pokemon) parent.getItemAtPosition(position);
                Intent detail = new Intent(getContext(), PokemonDetail.class);
                detail.putExtra("id", clickedObj.getId());
                startActivity(detail);
            }});
    }



}
