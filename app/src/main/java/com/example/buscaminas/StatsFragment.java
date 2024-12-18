package com.example.buscaminas;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        createStats(view);

        return view;

    }

    private void createStats(View view) {

        String[] names = PreferencesHelper.getData(getContext(),"","names","").split("\\|");
        List<String> statsCols = Arrays.asList(new String[]{"Name", "Clicks", "Time", "Mines", "Score"});
        // Las metricas permiten ver el tamaño de la pantalla
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int containerWidth = metrics.widthPixels; // ancho absoluto en pixels
        int colsLenght = statsCols.size();

        GridLayout grid = view.findViewById(R.id.gridStats);

        LinearLayout titles = new LinearLayout(getContext());
        titles.setOrientation(LinearLayout.HORIZONTAL);
        titles.layout(0,0,0,0);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(10,10,10,10);
        params.width = containerWidth/colsLenght -21;
        params.height = 100;
        for (String col : statsCols){
            TextView tv = createTextView(col, params);
            tv.setBackgroundResource(R.drawable.titles_cell);
            titles.addView(tv);
        }

        grid.addView(titles);

        ScrollView scrollView = new ScrollView(getContext());
        scrollView.isVerticalScrollBarEnabled();
        ViewGroup.LayoutParams paramsScroll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(paramsScroll);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new GridLayout.LayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
        GridLayout gridLayout = new GridLayout(getContext());
        gridLayout.setOrientation(GridLayout.VERTICAL);

        for (String name : names){
            System.out.println(name);
            System.out.println("name");
            if (!name.equals("")){

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                for (String col : statsCols){
                    TextView textVValues;
                    if (col.equals("Name")){
                        textVValues = createTextView(name, params);
                    }else{
                        String value = PreferencesHelper.getData(getContext(),name,col,"");
                        System.out.println("value");
                        System.out.println(value);
                        textVValues = createTextView(value, params);
                    }
                    ll.addView(textVValues);
                }
                ll.setLayoutParams(new GridLayout.LayoutParams( new ViewGroup.LayoutParams(containerWidth,126)));
                gridLayout.addView(ll);
            }
        }
//        grid.setOrientation(GridLayout.VERTICAL);
        linearLayout.addView(gridLayout);
        scrollView.addView(linearLayout);
        grid.addView(scrollView);
    }
    private TextView createTextView(String value, GridLayout.LayoutParams params){
        TextView tv = new TextView(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tv.setLeftTopRightBottom(0,0,0,0);
        }else{
            tv.setBottom(0);
            tv.setLeft(0);
            tv.setRight(0);
            tv.setTop(0);
        }
        tv.setTextColor(getResources().getColor(R.color.platinum_light));
        tv.setBackgroundResource(R.drawable.textview_border);
        tv.setLayoutParams(params);
        tv.setText(value);
        return tv;
    }


}