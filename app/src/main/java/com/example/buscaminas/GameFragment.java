package com.example.buscaminas;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
//    private RowViewModel rowViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String COLUMNS = "columns";
    private static final String ROWS = "rows";
    private static final String MINES = "mines";
    private static final int COLUMNSQ = 10;

    // TODO: Rename and change types of parameters
    private String rows;
    private String[] mines;
    private int minesFlagged;
    private int clicks;
    private boolean firstTouch = true;
    private String timer = "00:00";
    private final Map<String, Integer> difficulty = new HashMap<>(){{
        put(ROWS, 10);
        put(MINES, 11);
    }};
    int[] colors = {R.color.casilla_1, R.color.casilla_2, R.color.casilla_3,R.color.casilla_4, R.color.casilla_5 };

    private TextView timerTextView;
    private Handler handler = new Handler();
    private long startTime;



    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2, String param3) {
        //pasar datos con bundle
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ROWS, param2);
        args.putString(MINES, param3);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            for(String argument : getArguments().keySet()){
                difficulty.put(argument, getArguments().getInt(argument));
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_game, container, false);
        firstTouch = true;
        minesFlagged = 0;
        clicks = 0;

        createCanvas(view);

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Calcular minutos y segundos
            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) (elapsedTime / (1000 * 60));

            // Actualizar el TextView
            timer = String.format("%02d:%02d", minutes, seconds);
            timerTextView.setText(timer);
            // Volver a ejecutar después de 1 segundo
            handler.postDelayed(this, 1000);
        }
    };


    private void createChrono(View parentView){
        timerTextView = parentView.findViewById(R.id.chrono);
        startTime = System.currentTimeMillis();
        // Iniciar el cronómetro
        handler.post(timerRunnable);
    }
    private void stopChrono(View parentView){
        timerTextView = parentView.findViewById(R.id.chrono);
        startTime = System.currentTimeMillis();
        // Iniciar el cronómetro
        handler.removeCallbacks(timerRunnable);
    }


    private void createCanvas(View view){
        int [][] simplyfied = new int[difficulty.get(ROWS)][COLUMNSQ];
        CeldaModel[][] board = new CeldaModel[difficulty.get(ROWS)][COLUMNSQ];


        System.out.println(difficulty.get(ROWS));
        for (int i = 0; i < difficulty.get(ROWS); i++) {
            for (int j = 0; j < COLUMNSQ; j++) {
                board[i][j] = new CeldaModel("0");
                simplyfied[i][j] = 0;
            }
        }

        GridLayout grdL = view.findViewById(R.id.grid_buscaminas);
        for (int i = 0; i < difficulty.get(ROWS); i++) {
            LinearLayout ll = getLinearLayout(COLUMNSQ, board[i], i);
            grdL.addView(ll);
            grdL.setOrientation(GridLayout.VERTICAL);
        }
        System.out.println(simplyfied);
    }
    @NonNull
    private LinearLayout getLinearLayout(int columns, CeldaModel[] board, int row) {

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int parentWidth = metrics.widthPixels; // ancho absoluto en pixels
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        for (int j = 0; j < columns; j++) {
            Button button = new Button(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                button.setLeftTopRightBottom(0,0,0,0);
            }else{
                button.setBottom(0);
                button.setLeft(0);
                button.setRight(0);
                button.setTop(0);
            }
            CeldaModel cm = board[j];

            button.setTag(R.attr.position, row+ "|"+ j);
            button.setTag(R.attr.isMine,false);
            button.setTag(R.attr.visible,false);
            button.setTag(R.attr.state,State.TAPADO);
            button.setTag(R.attr.minesNear,0);

//            button.setId(row+j);
            button.setOnClickListener(cellClick);
            button.setOnLongClickListener(cellLongClick);
            button.setLayoutParams( new GridLayout.LayoutParams( new ViewGroup.LayoutParams(parentWidth/COLUMNSQ,parentWidth/COLUMNSQ)));
            ll.addView(button);
        }
        return ll;
    }


    public void setCanvasBombs(View view){

        View parentView = (View) view.getParent().getParent();
        String positionTag = view.getTag(R.attr.position).toString();
        String[] splitted = positionTag.split("\\|");
        int row = Integer.parseInt(splitted[0]);
        int column = Integer.parseInt(splitted[1]);
        int prevRow = row - 1;
        int prevColumn = column - 1;
        int nextRow = row + 1;
        int nextColumn = column + 1;
        mines = new String[difficulty.get(MINES)];


        int minesSetted = 0;
        int futureMineRow;
        int futureMineColumn;
        while(minesSetted < difficulty.get(MINES)){
            List<String> listMines  = Arrays.asList(mines);
            futureMineRow = (int) (Math.random() * difficulty.get(ROWS));
            futureMineColumn = (int) (Math.random() * COLUMNSQ);
            String futureMinePosition = futureMineRow+"|"+futureMineColumn;
            if (!listMines.contains(futureMinePosition) && isValidMine(futureMinePosition, prevRow, prevColumn, nextRow, nextColumn)){
                Button futureMine = (Button) ViewFinder.findViewsWithTag(parentView, futureMinePosition, R.attr.position ).get(0);
                futureMine.setTag(R.attr.isMine,true);
                mines[minesSetted] = futureMinePosition;
                minesSetted++;
            }
        }

        if (row == 0 && column == 0){
            topLeftClick(parentView, row, column);
        } else if (row == difficulty.get(ROWS)-1 && column == 0){
            bottomLeftClick(parentView,row,column);
        } else if (row == 0 && column == COLUMNSQ -1){
            topRightClick(parentView,row,column);
        } else if (row == difficulty.get(ROWS)-1 && column == COLUMNSQ-1){
            bottomRightClick(parentView,row,column);
        } else{
            innerClick(parentView, row, column);
        }
        setAroundMines(mines, parentView);
        setAroundClick(parentView, positionTag, new HashSet<>(),true);
        printMines(parentView, mines);


    }

    private void topLeftClick(View parentView, int row, int column) {
        int nextCol = column + 1;
        int nextRow = row + 1;
        View currRowCurrColView = ViewFinder.findViewsWithTag(parentView, row+"|"+column, R.attr.position ).get(0);
        View currRowNextColView = ViewFinder.findViewsWithTag(parentView, row+"|"+nextCol, R.attr.position ).get(0);
        View nextRowCurrColView = ViewFinder.findViewsWithTag(parentView, nextRow+"|"+column, R.attr.position ).get(0);
        View nextRowNextColView = ViewFinder.findViewsWithTag(parentView, nextRow+"|"+ nextCol, R.attr.position ).get(0);

        int color = colors[(int) (Math.random() * colors.length)];

        currRowNextColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        nextRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        nextRowNextColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setTag(R.attr.state,State.VOLTEADO);
        currRowCurrColView.setTag(R.attr.visible,true);
    }
    private void topRightClick(View parentView, int row, int column) {
        int prevCol = column - 1;
        int nextRow = row + 1;
        View currRowPrevColView = ViewFinder.findViewsWithTag(parentView, row+"|"+ prevCol, R.attr.position ).get(0);
        View currRowCurrColView = ViewFinder.findViewsWithTag(parentView, row+"|"+column, R.attr.position ).get(0);
        View nextRowPrevColView = ViewFinder.findViewsWithTag(parentView, nextRow+"|"+ prevCol, R.attr.position ).get(0);
        View nextRowCurrColView = ViewFinder.findViewsWithTag(parentView, nextRow+"|"+column, R.attr.position ).get(0);

        currRowPrevColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        nextRowPrevColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        nextRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);

        currRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setTag(R.attr.state,State.VOLTEADO);
        currRowCurrColView.setTag(R.attr.visible,true);

    }
    private void bottomRightClick(View parentView, int row, int column) {
        int prevRow = row - 1;
        int prevCol = column - 1;
        View prevRowPrevColView = ViewFinder.findViewsWithTag(parentView, prevRow+"|"+prevCol, R.attr.position ).get(0);
        View prevRowCurrColView = ViewFinder.findViewsWithTag(parentView, prevRow+"|"+column, R.attr.position ).get(0);
        View currRowPrevColView = ViewFinder.findViewsWithTag(parentView, row+"|"+prevCol, R.attr.position ).get(0);
        View currRowCurrColView = ViewFinder.findViewsWithTag(parentView, row+"|"+column, R.attr.position ).get(0);

        prevRowPrevColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        prevRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowPrevColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setTag(R.attr.state,State.VOLTEADO);
        currRowCurrColView.setTag(R.attr.visible,true);

    }
    private void bottomLeftClick(View parentView, int row, int column) {
        int prevRow = row - 1;
        int nextCol = column + 1;
        View prevRowCurrColView = ViewFinder.findViewsWithTag(parentView, prevRow+"|"+column, R.attr.position ).get(0);
        View prevRowNextColView = ViewFinder.findViewsWithTag(parentView, prevRow+"|"+nextCol, R.attr.position ).get(0);
        View currRowCurrColView = ViewFinder.findViewsWithTag(parentView, row+"|"+column, R.attr.position ).get(0);
        View currRowNextColView = ViewFinder.findViewsWithTag(parentView, row+"|"+nextCol, R.attr.position ).get(0);

        prevRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        prevRowNextColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowNextColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setTag(R.attr.state,State.VOLTEADO);
        currRowCurrColView.setTag(R.attr.visible,true);
    }
    private void innerClick(View parentView, int row, int column) {
        String currPosition = row+"|"+column;
        String[] aroundCells = getAroundCells(currPosition);
        List<View> aroundCellsViews = new ArrayList<>();
        for (int i = 0; i < aroundCells.length; i++) {
            if(isValidPosition(aroundCells[i], difficulty.get(ROWS))){
                aroundCellsViews.add(ViewFinder.findViewsWithTag(parentView, aroundCells[i], R.attr.position ).get(0));
            }
        }
        for(View aroundView : aroundCellsViews){
            aroundView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
            aroundView.setTag(R.attr.state,State.VOLTEADO);
            aroundView.setTag(R.attr.visible,true);
        }
        Button currRowCurrColView = (Button)ViewFinder.findViewsWithTag(parentView, currPosition, R.attr.position ).get(0);
        currRowCurrColView.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
        currRowCurrColView.setTag(R.attr.state,State.VOLTEADO);
        currRowCurrColView.setTag(R.attr.visible,true);
        if ((int) currRowCurrColView.getTag(R.attr.minesNear) != 0){
            currRowCurrColView.setText(currRowCurrColView.getTag(R.attr.minesNear).toString());
        }

    }
    private void setAroundClick(View parentView, String positionTag, Set<String> positionDone, boolean firstTouch){
        //Probar a trabajar con conjuntos
        if (positionDone.contains(positionTag)){
            return;
        }
        positionDone.add(positionTag);
        String[] aroundClicked;
        if (firstTouch){
            aroundClicked = getAroundCells(positionTag);
        }
        else {
            aroundClicked = getCrossCells(positionTag);
        }
        for (int i = 0; i < aroundClicked.length; i++) {
            int color = colors[(int) (Math.random() * colors.length)];
            if (isValidPosition(aroundClicked[i],difficulty.get(ROWS))) {
                Button cell = (Button) ViewFinder.findViewsWithTag(parentView, aroundClicked[i], R.attr.position ).get(0);
                Button origin = (Button) ViewFinder.findViewsWithTag(parentView, positionTag, R.attr.position ).get(0);
                if(origin.getTag(R.attr.minesNear).equals(0)){
                    cell.setBackgroundResource(color);
                    cell.setTag(R.attr.state,State.VOLTEADO);
                    cell.setTag(R.attr.visible,true);
                    if ((int) cell.getTag(R.attr.minesNear) != 0){
                        cell.setText(cell.getTag(R.attr.minesNear).toString());
                    }
                    setAroundClick(parentView, aroundClicked[i], positionDone, false);
                }
            }
        }
    }


    private String[] getAroundCells(String position){
        String[] mineSplitted = position.split("\\|");
        int cellRow = Integer.parseInt(mineSplitted[0]);
        int cellColumn = Integer.parseInt(mineSplitted[1]);
        int cellPrevRow = cellRow - 1;

        int cellPrevColumn = cellColumn - 1;
        int cellNextRow = cellRow + 1;
        int cellNextColumn = cellColumn + 1;

        String[] aroundCells = new String[8];

        aroundCells[0] = cellPrevRow+"|"+cellPrevColumn; // TopLeftPosition
        aroundCells[1] = cellPrevRow+"|"+cellColumn; // TopTopPosition
        aroundCells[2] = cellPrevRow+"|"+cellNextColumn; // TopRightPosition

        aroundCells[3] = cellRow+"|"+cellPrevColumn; // LeftLeftPosition
        aroundCells[4] = cellRow+"|"+cellNextColumn; // RightRightPosition

        aroundCells[5] = cellNextRow+"|"+cellPrevColumn; // BottomLeftPosition
        aroundCells[6] = cellNextRow+"|"+cellColumn; // BottomBottomPosition
        aroundCells[7] = cellNextRow+"|"+cellNextColumn; // BottomRightPosition
        return aroundCells;
    }
    private String[] getCrossCells(String position){
        String[] mineSplitted = position.split("\\|");
        int cellRow = Integer.parseInt(mineSplitted[0]);
        int cellColumn = Integer.parseInt(mineSplitted[1]);
        int cellPrevRow = cellRow - 1;

        int cellPrevColumn = cellColumn - 1;
        int cellNextRow = cellRow + 1;
        int cellNextColumn = cellColumn + 1;

        String[] aroundCells = new String[4];

        aroundCells[0] = cellPrevRow+"|"+cellColumn; // TopPosition
        aroundCells[1] = cellRow+"|"+cellPrevColumn; // LeftPosition
        aroundCells[2] = cellNextRow+"|"+cellColumn; // BottomPosition
        aroundCells[3] = cellRow+"|"+cellNextColumn; // rightPosition

        return aroundCells;
    }
    private void setAroundMines(String[] mines, View parentView){

        for (String mina : mines){
            String[] aroundMines = getAroundCells(mina);
            for (String aroundMinePosition : aroundMines){
                if (isValidPosition(aroundMinePosition,difficulty.get(ROWS))){
                    String[] aroundCellsMines = getAroundCells(aroundMinePosition);
                    int minesNear = 0;
                    for (String aroundCellMineCounter : aroundCellsMines){
                        if(isValidPosition(aroundCellMineCounter,difficulty.get(ROWS))){
                            View cellRoundMine = ViewFinder.findViewsWithTag(parentView, aroundCellMineCounter, R.attr.position ).get(0);
                            if (cellRoundMine.getTag(R.attr.isMine).equals(true)){
                                minesNear++;
                            }
                        }
                    }
                    Button cellRoundMine = (Button) ViewFinder.findViewsWithTag(parentView, aroundMinePosition, R.attr.position ).get(0);
                    if (cellRoundMine.getTag(R.attr.isMine).equals(false)){
                        cellRoundMine.setTag(R.attr.minesNear,  minesNear);
                        if (minesNear == 1){
                            cellRoundMine.setTextColor(getResources().getColor(R.color.green));
                        }else if (minesNear == 2){
                            cellRoundMine.setTextColor(getResources().getColor(R.color.yellow));
                        }else if (minesNear == 3){
                            cellRoundMine.setTextColor(getResources().getColor(R.color.orange));
                        }else if (minesNear >= 4){
                            cellRoundMine.setTextColor(getResources().getColor(R.color.brown));
                        }

                    }
                }

            }
        }


    }

    private void printMines(View parentView, String[] mines){
        for (String mina : mines){
            Button mine =(Button) ViewFinder.findViewsWithTag(parentView, mina, R.attr.position ).get(0);
            mine.setBackgroundResource(R.color.red);

        }
    }

    private View.OnLongClickListener cellLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag(R.attr.visible).equals(false)){
                showPopupMenu(v);


            }
            return true;
        }
    };
    private View.OnClickListener cellClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            System.out.println(v.getTag(R.attr.position).toString());
            System.out.println(v.getTag(R.attr.minesNear).toString());

            if (firstTouch){
                firstTouch = false;
                setCanvasBombs(button);
                clicks++;
                createChrono((View) button.getParent().getParent().getParent().getParent().getParent());

            }
            if (!button.getTag(R.attr.state).equals(State.BANDERA)){
                if(button.getTag(R.attr.isMine).equals(true)){
                    clicks++;
                    Toast toast = new Toast(getContext());
                    toast.setText("Has perdido con "+clicks+" clicks");
                    toast.show();
                    stopChrono((View) button.getParent().getParent());
                    System.out.println(v.findViewById(R.id.chrono));
                    printMines((View) button.getParent().getParent(), mines);
                    invalidAllButtons((View) button.getParent().getParent());
                    showFinishForm((View) button.getParent().getParent());
                    return;
                }else if(button.getTag(R.attr.state).equals(State.TAPADO)){
                    clicks++;
                    button.setBackgroundResource(colors[(int) (Math.random() * colors.length)]);
                    button.setTag(R.attr.state,State.VOLTEADO);
                    button.setTag(R.attr.visible,true);
                    if ((int) button.getTag(R.attr.minesNear) != 0){

                        button.setText(v.getTag(R.attr.minesNear).toString());
                    }
                    setAroundClick((View)v.getParent().getParent(), v.getTag(R.attr.position).toString(), new HashSet<>(), false);
                }
            }
            for(String mina : mines){
                System.out.println(mina);
            }
//            printMines((View) v.getParent().getParent(),mines);
        }
    };

    private void showPopupMenu(View anchor) {
        // Inflar el diseño del menú flotante
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_menu, null);

        // Crear el PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true); // True para que el popup sea interactivo

        // Configurar los botones del menú
        Button blackFlag = popupView.findViewById(R.id.redFlag);
        Button whiteFlag = popupView.findViewById(R.id.whiteFlag);
        Button quitFlag = popupView.findViewById(R.id.quitFlag);

        View.OnClickListener menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View menuButton) {
                if (anchor instanceof Button) {
                    // Establecer el texto del botón seleccionado en el botón mantenido
                    if (menuButton.getId() == R.id.redFlag) {
                        anchor.setBackgroundResource(R.drawable.red_flag);
                        anchor.setTag(R.attr.state, State.BANDERA);
                        if (anchor.getTag(R.attr.isMine).equals(true) && anchor.getBackground() != getResources().getDrawable(R.drawable.red_flag) ){
                            minesFlagged++;
                            if (minesFlagged == difficulty.get(MINES)){
                                showFinishForm(anchor);
                                Toast toast = new Toast(getContext());
                                toast.setText("Has ganado con "+clicks+" clicks");
                                toast.show();
//                                Cancelar la opcion de seguir jugando
                            }
                        }
                    } else if(menuButton.getId() == R.id.whiteFlag){
                        anchor.setBackgroundResource(R.drawable.white_flag);
                        anchor.setTag(R.attr.state, State.BANDERA);
                    } else if(menuButton.getId() == R.id.quitFlag){
                        if (anchor.getTag(R.attr.isMine).equals(true) && anchor.getBackground() != getResources().getDrawable(R.drawable.red_flag) ){
                            minesFlagged--;
                        }

                        anchor.setBackgroundResource(R.color.light_grey);
                        anchor.setTag(R.attr.state, State.TAPADO);
                    }
                }
                popupWindow.dismiss(); // Cerrar el menú flotante
            }
        };

        // Asignar el mismo listener a los botones del menú
        blackFlag.setOnClickListener(menuClickListener);
        whiteFlag.setOnClickListener(menuClickListener);
        quitFlag.setOnClickListener(menuClickListener);

        // Mostrar el PopupWindow anclado al botón que se mantuvo presionado
        popupWindow.showAtLocation(anchor, Gravity.CENTER, (int) anchor.getTranslationX(), (int) anchor.getTranslationY());


    }

    private void showFinishForm(View parent) {
        // Inflar el diseño del menú flotante
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.game_finished, null);

        TextInputEditText textName = popupView.findViewById(R.id.editTextName);

        Button buttonSend = popupView.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    // recoger data
                    String userName = textName.getText().toString();
                    String userNameSaved = PreferencesHelper.getData(getContext(), "", "names", "");
                    if (userNameSaved.isEmpty()){
                        PreferencesHelper.saveData(getContext(), "", "names", userName);
                    }else{
                        PreferencesHelper.saveData(getContext(), "", "names", userNameSaved + "|" +userName);
                    }

                    PreferencesHelper.saveData(getContext(), userName, "clicks", String.valueOf(clicks));
                    PreferencesHelper.saveData(getContext(), userName, "time", timer);
                    PreferencesHelper.saveData(getContext(), userName, "mines", String.valueOf(minesFlagged));
                    int score = calcScore();
                    PreferencesHelper.saveData(getContext(), userName, "score", String.valueOf(score));

                  }
              });
        // Crear el PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true); // True para que el popup sea interactivo

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0,0);

    }

    private int calcScore() {
        int score = 0;
        for (int i = 0; i < minesFlagged; i++) {
            score += 100;
        }
        score -= calcTimeScore(timer);
        score -= clicks;
        return score;
    }

    private int calcTimeScore(String tiempo){
        String[] partes = tiempo.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        int totalSegundos = minutos * 60 + segundos;

        // Calcular puntos (1 punto por cada 15 segundos)
        return totalSegundos / 30;
    }

    private void invalidAllButtons(View parent) {
        List<View> buttonsVolteado = ViewFinder.findViewsWithTag(parent, State.VOLTEADO, R.attr.state);
        List<View> buttonsTapados = ViewFinder.findViewsWithTag(parent, State.TAPADO, R.attr.state);
        List<View> buttonsBandera = ViewFinder.findViewsWithTag(parent, State.BANDERA, R.attr.state);
        List<View> allButtons = new ArrayList<>();
        allButtons.addAll(buttonsBandera);
        allButtons.addAll(buttonsTapados);
        allButtons.addAll(buttonsVolteado);
        for (View view : allButtons){
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    return;
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }

    private boolean isValidMine(String parse, int previousRow, int previousColumn, int nextRow, int nextColumn){
        String qPattern = null;

        String rowRange = IntStream.rangeClosed(previousRow, nextRow)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("|"));
        String columnRange = IntStream.rangeClosed(previousColumn, nextColumn)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("|"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            qPattern = "^(%s)\\|(%s)$".formatted(rowRange, columnRange);
        }
        else{
            qPattern = String.format("^(%s)\\|(%s)$",rowRange,columnRange);
        }

        return !Pattern.compile(qPattern).matcher(parse).matches();

    }

    private boolean isValidPosition(String parse,  int finalRow){
        String qPattern = null;

        String rowRange = IntStream.rangeClosed(0, finalRow-1)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("|"));
        String columnRange = IntStream.rangeClosed(0, COLUMNSQ-1)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("|"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            qPattern = "^(%s)\\|(%s)$".formatted(rowRange, columnRange);
        }
        else{
            qPattern = String.format("^(%s)\\|(%s)$",rowRange,columnRange);
        }
        return Pattern.compile(qPattern).matcher(parse).matches();

    }

}