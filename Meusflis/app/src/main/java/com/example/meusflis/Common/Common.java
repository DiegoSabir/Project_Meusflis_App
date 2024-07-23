package com.example.meusflis.Common;

public class Common {

    //NODOS
    public static final String NODO_MOVIES = "Movies";
    public static final String NODO_GENERAL = "General";

    public static final String ELEMENTO_NODO_CATEGORIA = "categoryMovie";

    public static final String CATEGORY_TENDENCIAS = "Tendencias";
    public static final String CATEGORY_ANIMES = "Animes";
    public static final String CATEGORY_VARIADOS = "Variados";
    public static final String CATEGORY_ACCION = "Accion";
    public static final String NODO_MOVIES_TOPTEN = "TopTen";
    public static final String CATEGORY_COMEDIA = "Comedia";
    public static final String MOBILE_NUMBER = "+34604053218";
    public static final String NODO_ESTRENOS = "Estrenos";
    public static final String NODO_MOVIES_CATEGORIES = "Categorias";

    private static String categorySelect = "";

    public static String getCategorySelected() {
        return categorySelect;
    }

    public static void setCategorySelect(String categorySelect){
        Common.categorySelect = categorySelect;
    }
}
