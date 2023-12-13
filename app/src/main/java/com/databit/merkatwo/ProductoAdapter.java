package com.databit.merkatwo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;



public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private Context contexto;
    private OnCarritoClickListener carritoClickListener;

    // Constructor que recibe la lista de productos y el contexto
    public ProductoAdapter(List<Producto> listaProductos, Context contexto) {
        this.listaProductos = listaProductos;
        this.contexto = contexto;
    }


    public interface OnCarritoClickListener {
        void onAñadirAlCarritoClick(int position);
    }
    public void setOnCarritoClickListener(OnCarritoClickListener listener) {
        this.carritoClickListener = listener;
    }
    // Método para crear nuevos ViewHolder (invocado por el administrador del diseño)
    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el diseño de un elemento de producto
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        // Obtener el producto en la posición dada
        Producto producto = listaProductos.get(position);

        // Rellenar los datos en el ViewHolder
        holder.nombreProducto.setText(producto.getNombre());
        holder.descripcionProducto.setText(producto.getDescripcion());
        holder.precioProducto.setText(String.valueOf(producto.getPrecio()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions
                .transforms(new CenterCrop(), new RoundedCorners(16))
                .override(350, 250); // Ajusta el tamaño de la imagen según tus preferencias

        // Cargar la imagen usando Glide
        Glide.with(contexto)
                .load(producto.getImageUrl())
                .apply(requestOptions)
                .into(holder.imagenProducto);

        // Manejar clic en el botón de compra
        holder.btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && carritoClickListener != null) {
                    carritoClickListener.onAñadirAlCarritoClick(adapterPosition);
                    mostrarMensajeCompraRealizada(); // Muestra el mensaje cuando se hace clic en Comprar
                }
            }
        });

        // Manejar clic en el elemento del RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para pasar a la actividad de detalles del producto
                Intent intent = new Intent(contexto, DetalleProductoActivity.class);
                intent.putExtra("nombreProducto", producto.getNombre());
                intent.putExtra("descripcionProducto", producto.getDescripcion());
                intent.putExtra("precioProducto", producto.getPrecio());
                intent.putExtra("imageUrl", producto.getImageUrl());
                contexto.startActivity(intent);
            }
        });
    }


    // Método para obtener el número total de elementos en la lista (invocado por el administrador del diseño)
    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna ViewHolder para representar cada elemento de la lista de productos
    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreProducto, descripcionProducto, precioProducto;
        public Button btnComprar;
        public ImageView imagenProducto;

        public ProductoViewHolder(View view) {
            super(view);
            nombreProducto = view.findViewById(R.id.textNombreProducto);
            descripcionProducto = view.findViewById(R.id.textDescripcionProducto);
            precioProducto = view.findViewById(R.id.textPrecioProducto);
            btnComprar = view.findViewById(R.id.btnComprar);
            imagenProducto = view.findViewById(R.id.imageProducto);
        }
    }

    private void mostrarMensajeCompraRealizada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Compra Añadida al carrito");
        builder.setMessage("¡Gracias por tu compra!");
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
