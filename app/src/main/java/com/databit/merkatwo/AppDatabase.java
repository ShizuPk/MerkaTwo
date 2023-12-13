package com.databit.merkatwo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
@Database(entities = {Usuario.class, Producto.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract ProductoDao productoDao();

    // Definir la migración de la versión 1 a la versión 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Añadir la columna 'cantidad' a la tabla 'productos'
            database.execSQL("ALTER TABLE productos ADD COLUMN cantidad INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "MerkaTwoo")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)  // Agregar las migraciones
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
