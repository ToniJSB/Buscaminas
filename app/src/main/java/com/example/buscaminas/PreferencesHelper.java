package com.example.buscaminas;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static final String PREFS_NAME = "Users";

    private static SharedPreferences getSharedPreferences(Context context, String userName){
        if (userName.isEmpty()){
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }else{
            return context.getSharedPreferences(PREFS_NAME +"_"+ userName, Context.MODE_PRIVATE);
        }
    }
    public static void saveData(Context context, String userName, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, userName);
        // Obtener instancia de SharedPreferences

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Guardar el dato
        editor.putString(key, value);

        // Confirmar los cambios
        editor.apply(); // Usa apply() para guardar de manera asíncrona
    }
    public static String getData(Context context, String userName, String key, String defaultValue) {
        // Obtener instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(context, userName);
        // Recuperar el valor
        return sharedPreferences.getString(key, defaultValue);
    }


}