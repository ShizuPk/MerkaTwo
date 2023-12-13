package com.databit.merkatwo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class Producto implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private String descripcion;
    private double precio;
    private String imageUrl;
    private int cantidad;


    public Producto(){

    }

    protected Producto(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readDouble();
        imageUrl = in.readString();
        cantidad = in.readInt();
    }


    // Constructor
    public Producto(String nombre, String descripcion, double precio, String imageUrl,int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imageUrl = imageUrl;
        this.cantidad = 0;

    }
    // Incrementa la cantidad de productos en el carrito
    public void incrementarCantidad() {
        cantidad++;
    }

    // Métodos getter y setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeDouble(precio);
        dest.writeString(imageUrl);
        dest.writeInt(cantidad);
    }
    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };
}
