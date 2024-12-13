package com.example.buscaminas;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class ViewFinder {

    /**
     * Busca todas las vistas con un Tag específico dentro de un ViewGroup.
     *
     * @param rootView El contenedor raíz donde buscar.
     * @param tagValue El valor del Tag que estás buscando.
     * @return Una lista con todas las vistas que contienen el Tag buscado.
     */
    public static List<View> findViewsWithTag(View rootView, Object tagValue, int tag) {
        List<View> viewsWithTag = new ArrayList<>();

        // Si el rootView coincide con el tagValue, lo añadimos a la lista
//        System.out.println(tagValue);
        if (tagValue.equals(rootView.getTag(tag))) {
            viewsWithTag.add(rootView);
        }

        // Si es un ViewGroup, iteramos sobre sus hijos
        if (rootView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) rootView;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                viewsWithTag.addAll(findViewsWithTag(child, tagValue, tag));
            }
        }

        return viewsWithTag;
    }
}
