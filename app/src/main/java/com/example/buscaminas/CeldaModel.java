package com.example.buscaminas;

public class CeldaModel {


    private Enum<State> state;
    private int minesAround;
    private String position;
    private boolean isMine;

    private boolean visible;

    public CeldaModel(String position) {
        this.state = State.TAPADO;
        this.minesAround = 0;
        this.position = position;
        this.isMine = false;
        this.visible = false;
    }

    public Enum<State> getState() {
        return state;
    }

    public void setState(Enum<State> state) {
        this.state = state;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public int getMinesAround() {
        return minesAround;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
