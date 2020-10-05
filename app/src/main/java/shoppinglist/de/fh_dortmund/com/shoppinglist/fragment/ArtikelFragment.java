package shoppinglist.de.fh_dortmund.com.shoppinglist.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nullable;

import shoppinglist.de.fh_dortmund.com.shoppinglist.R;


public class ArtikelFragment extends Fragment {

    public ArtikelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artikel, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_artikel_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
