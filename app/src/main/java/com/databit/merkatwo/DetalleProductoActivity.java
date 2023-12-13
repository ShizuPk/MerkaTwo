package com.databit.merkatwo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetalleProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        // Obtener datos del producto de la intención
        String nombre = getIntent().getStringExtra("nombreProducto");
        String descripcion = getIntent().getStringExtra("descripcionProducto");
        double precio = getIntent().getDoubleExtra("precioProducto", 0);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Enlazar vistas
        ImageView imagenDetalleProducto = findViewById(R.id.imageProductoDetalle);
        TextView textNombreDetalle = findViewById(R.id.textNombreDetalle);
        TextView textDescripcionDetalle = findViewById(R.id.textDescripcionDetalle);
        TextView textPrecioDetalle = findViewById(R.id.textPrecioDetalle);

        // Establecer datos en las vistas
        Glide.with(this).load(imageUrl).into(imagenDetalleProducto);
        textNombreDetalle.setText(nombre);
        textDescripcionDetalle.setText(descripcion);
        textPrecioDetalle.setText(String.valueOf(precio));
    }
}
