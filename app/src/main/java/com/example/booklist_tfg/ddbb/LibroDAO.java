package com.example.booklist_tfg.ddbb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.booklist_tfg.Model.Libro;

import java.util.List;

@Dao
public interface LibroDAO {

    @Query("SELECT * FROM libros")
    List<Libro> getAll();

    @Query("SELECT * FROM libros WHERE id = :id")
    Libro getById(int id);

    @Insert
    void insert(Libro libro);

    @Update
    void update(Libro libro);

    @Delete
    void delete(Libro libro);
}