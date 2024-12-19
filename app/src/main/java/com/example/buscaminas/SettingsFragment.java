package com.example.buscaminas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button buttonFacil = view.findViewById(R.id.buttonFacil);
        Button buttonNormal = view.findViewById(R.id.buttonNormal);
        Button buttonDificil = view.findViewById(R.id.buttonDificil);
        if (MainActivity.gameFragment.difficulty.get("rows") == 10){
            buttonFacil.setBackgroundResource(R.drawable.button_marked);
            buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
            buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
        }
        else if (MainActivity.gameFragment.difficulty.get("rows") == 15){
            buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
            buttonNormal.setBackgroundResource(R.drawable.button_marked);
            buttonDificil.setBackgroundResource(R.drawable.button_unmarked);
        }
        else if (MainActivity.gameFragment.difficulty.get("rows") == 25){
            buttonFacil.setBackgroundResource(R.drawable.button_unmarked);
            buttonNormal.setBackgroundResource(R.drawable.button_unmarked);
            buttonDificil.setBackgroundResource(R.drawable.button_marked);
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
                targetFragment.setArguments(bundle);
            }
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
        setupCustomDifficulty(view);
        return view;
    }

    private void setupCustomDifficulty(View view){
        // Referenciar los componentes del layout

        LinearLayout layoutForm = view.findViewById(R.id.layoutForm);
        Button btnToggleForm = view.findViewById(R.id.btnToggleForm);
        EditText inputRows = view.findViewById(R.id.inputRows);
        EditText inputObstacles = view.findViewById(R.id.inputMines);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnToggleForm.setBackgroundResource(R.drawable.button_unmarked);
        layoutForm.setBackgroundResource(R.drawable.form_background);

        inputObstacles.setTextColor(getResources().getColor(R.color.platinum_light));

        // Configurar el botón para mostrar/ocultar el formulario
        btnToggleForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFormVisibility(layoutForm, btnToggleForm);
            }
        });

        // Configurar el botón de guardar
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(inputRows,inputObstacles);
            }
        });
    }
    private void toggleFormVisibility(LinearLayout layoutForm, Button btnToggleForm) {
        // Mostrar/ocultar el formulario
        if (layoutForm.getVisibility() == View.GONE) {
            layoutForm.setVisibility(View.VISIBLE);
            btnToggleForm.setText("Ocultar Formulario");
        } else {
            layoutForm.setVisibility(View.GONE);
            btnToggleForm.setText("Mostrar Formulario");
        }
    }

    private void submitForm(EditText inputRows, EditText inputObstacles) {
        // Validar y procesar los datos del formulario
        String rowsText = inputRows.getText().toString().trim();
        String minesText = inputObstacles.getText().toString().trim();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(rowsText) || TextUtils.isEmpty(minesText)) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int rows = Integer.parseInt(rowsText);
        int mines = Integer.parseInt(minesText);

        // Validaciones adicionales (opcional)
        if (rows <= 0 || mines <= 0) {
            Toast.makeText(getContext(), "The values should be bigger than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mines > rows*9) {
            Toast.makeText(getContext(), "There can't be so many Mines.", Toast.LENGTH_SHORT).show();
            return;
        }
        bundle.putInt("rows",rows);
        bundle.putInt("columns",15);
        bundle.putInt("mines",mines);
        GameFragment targetFragment = MainActivity.gameFragment;
        targetFragment.setArguments(bundle);

        // Procesar los valores
        Toast.makeText(getContext(), "Rows: " + rows + ", Mines: " + mines, Toast.LENGTH_SHORT).show();
    }
}