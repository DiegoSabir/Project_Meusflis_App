package com.sabir.meusflis.Common;

public class Common {

    //NODOS
    public static final String NODO_MOVIES = "Movies";
    public static final String NODO_GENERAL = "General";

    private static String categorySelect = "";

    public static String getCategorySelected() {
        return categorySelect;
    }

    public static void setCategorySelect(String categorySelect){
        Common.categorySelect = categorySelect;
    }
}
