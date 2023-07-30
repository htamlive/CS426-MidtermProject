package com.example.a21125060.Fragment;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.a21125060.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CelebrateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CelebrateFragment extends DialogFragment {


    public CelebrateFragment() {
        // Required empty public constructor
    }


    public static CelebrateFragment newInstance(String param1, String param2) {
        CelebrateFragment fragment = new CelebrateFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_celebrate, container, false);
    }
}