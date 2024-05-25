package com.tp.transport;

import android.provider.BaseColumns;

public final class Contract {
    // Constructeur privé pour empêcher l'instanciation de cette classe
    private Contract() {}

    /* Définition du schéma de la table des utilisateurs */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "utilisateurs";
        public static final String COLUMN_NAME_PRENOM = "prenom";
        public static final String COLUMN_NAME_NOM = "nom";
        public static final String COLUMN_NAME_NUM_TELEPHONE = "num_telephone";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    // Vous pouvez ajouter d'autres classes pour définir le schéma de vos autres tables si nécessaire
}

