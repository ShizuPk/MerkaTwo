// Clase Viaje
package com.databit.merkatwo;

import android.os.Parcel;
import android.os.Parcelable;

public class Viaje implements Parcelable {

    private String nombre;
    private String descripcion;
    private String precio;
    private int imagenResId;
    private int cantidad;

    // Constructor actualizado para incluir la cantidad
    public Viaje(String nombre, String descripcion, String precio, int imagenResId, int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenResId = imagenResId;
        this.cantidad = cantidad;
    }


    protected Viaje(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readString();
        imagenResId = in.readInt();
        cantidad = in.readInt();
    }

    public static final Creator<Viaje> CREATOR = new Creator<Viaje>() {
        @Override
        public Viaje createFromParcel(Parcel in) {
            return new Viaje(in);
        }

        @Override
        public Viaje[] newArray(int size) {
            return new Viaje[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void incrementarCantidad() {
        cantidad++;
    }

    @Override
    public int describeContents() {
        return 0;
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

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(precio);
        dest.writeInt(imagenResId);
        dest.writeInt(cantidad);
    }
}
