package com.example.wakeupapplication.modelos;

public class Usuarios {

    public String email,name;
    public int number;

    public Usuarios(){}

    public Usuarios(String email,String name){

        this.email = email;
        this.name = name;

    }

    public Usuarios(int number,String name){

        this.number = number;
        this.name = name;

    }

}
