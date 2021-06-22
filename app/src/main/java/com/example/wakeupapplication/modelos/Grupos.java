package com.example.wakeupapplication.modelos;

public class Grupos {

    public String nombre;
    public int cantidad;
    public String userId;

    public Grupos(){}

    public Grupos(String nombre,int cantidad,String userId){

        this.nombre = nombre;
        this.cantidad = cantidad;
        this.userId = userId;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
