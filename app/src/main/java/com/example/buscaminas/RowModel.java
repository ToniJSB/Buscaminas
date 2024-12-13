package com.example.buscaminas;

public class RowModel {
    private CeldaModel[] cells;

    public RowModel(CeldaModel[] cells) {
        this.cells = cells;
    }

    public CeldaModel[] getCells() {
        return cells;
    }
}
