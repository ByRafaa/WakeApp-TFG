package com.example.wakeupapplication.modelos;

import android.net.Uri;

import java.util.ArrayList;

public class Alarmas {

    public int horas,minutos;
    public String mensaje,grupo,userId;
    public ArrayList<Integer> alarmDays;
    public Uri ringtone;

    public Alarmas(){}

    public Alarmas(int horas,int minutos,String mensaje,ArrayList<Integer> alarmDays){

        this.horas = horas;
        this.minutos = minutos;
        this.mensaje = mensaje;
        this.alarmDays = alarmDays;

    }
    public Alarmas(int horas,int minutos,String mensaje,ArrayList<Integer> alarmDays,Uri ringtone){

        this.horas = horas;
        this.minutos = minutos;
        this.mensaje = mensaje;
        this.alarmDays = alarmDays;
        this.ringtone = ringtone;

    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Integer> getAlarmDays() {
        return alarmDays;
    }

    public void setAlarmDays(ArrayList<Integer> alarmDays) {
        this.alarmDays = alarmDays;
    }

    public Uri getRingtone() {
        return ringtone;
    }

    public void setRingtone(Uri ringtone) {
        this.ringtone = ringtone;
    }
}
