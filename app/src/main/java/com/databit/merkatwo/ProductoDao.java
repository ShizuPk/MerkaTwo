package com.databit.merkatwo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert
    void insertar(Producto producto);

    @Query("SELECT * FROM productos")
    List<Producto> obtenerTodosLosProductos();
}
