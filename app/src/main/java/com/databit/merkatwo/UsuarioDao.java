package com.databit.merkatwo;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.databit.merkatwo.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Insert
    void insertar(Usuario usuario);

    @Update
    void actualizar(Usuario usuario);

    @Delete
    void eliminar(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE nombreUsuario = :nombre AND password = :password")
    Usuario obtenerUsuario(String nombre, String password);

    @Query("SELECT * FROM usuario WHERE nombreUsuario = :nombre AND password = :password LIMIT 1")
    Usuario buscarUsuario(String nombre, String password);
}
