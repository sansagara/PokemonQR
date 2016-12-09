package com.leonelatencio.pokedexqr.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonelatencio.pokedexqr.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Aboutt Fragment
 * @author LA
 * @version v1.0
 */
public class AboutFragment extends Fragment {
    public AboutFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment About.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String filename = "http://leonelatencio.com/PokemonQR/About/";
        String image1 = filename + "desc1.jpg";
        String image2 = filename + "desc2.jpg";
        String image3 = filename + "desc3.jpg";
        String image4 = filename + "desc4.png";

        ImageView desc1_iv = (ImageView) view.findViewById(R.id.desc1_image);
        ImageView desc2_iv = (ImageView) view.findViewById(R.id.desc2_image);
        ImageView desc3_iv = (ImageView) view.findViewById(R.id.desc3_image);
        ImageView desc4_iv = (ImageView) view.findViewById(R.id.desc4_image);

        TextView desc4_tv = (TextView) view.findViewById(R.id.desc4_body);
        String desc4_text = getResources().getString(R.string.desc4_body);
        desc4_tv.setText(Html.fromHtml(desc4_text));

        Picasso.with(getContext())
                .load(image1)
                .into(desc1_iv);
        Picasso.with(getContext())
                .load(image2)
                .into(desc2_iv);
        Picasso.with(getContext())
                .load(image3)
                .into(desc3_iv);
        Picasso.with(getContext())
                .load(image4)
                .into(desc4_iv);
    }
}
