package com.example.buscaminas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO mirar de como hacer nuevas instancias del game al cambiar la dificultad
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button buttonFacil = view.findViewById(R.id.buttonFacil);
        Button buttonNormal = view.findViewById(R.id.buttonNormal);
        Button buttonDificil = view.findViewById(R.id.buttonDificil);
        if (buttonDificil.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_marked).getConstantState()){
            buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
            buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
            buttonDificil.setBackgroundResource(R.drawable.button_marked);
        }
        else if (buttonNormal.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_marked).getConstantState()){
            buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
            buttonNormal.setBackgroundResource(R.drawable.button_marked);
            buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
        }
        else if (buttonFacil.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_marked).getConstantState()){
            buttonFacil.setBackgroundResource(R.drawable.button_marked);
            buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
            buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
        }
        else{
            buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
            buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
            buttonDificil.setBackgroundResource(R.drawable.button_unmarked);

        }
        Bundle bundle = new Bundle();
        buttonFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("rows",10);
                bundle.putInt("columns",10);
                bundle.putInt("mines",11);
                buttonFacil.setBackgroundResource(R.drawable.button_marked);
                buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
                buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
                GameFragment targetFragment = MainActivity.gameFragment;
                targetFragment.setArguments(bundle);
            }
        });
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("rows",15);
                bundle.putInt("columns",15);
                bundle.putInt("mines",15);
                buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
                buttonNormal.setBackgroundResource(R.drawable.button_marked);
                buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
                GameFragment targetFragment = MainActivity.gameFragment;
                targetFragment.setArguments(bundle);            }
        });
        buttonDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("rows",25);
                bundle.putInt("columns",25);
                bundle.putInt("mines",25);
                buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
                buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
                buttonDificil.setBackgroundResource(R.drawable.button_marked);
                GameFragment targetFragment = MainActivity.gameFragment;
                targetFragment.setArguments(bundle);
            }
        });
        return view;
    }
}