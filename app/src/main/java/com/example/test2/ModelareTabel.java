package com.example.test2;

public class ModelareTabel {

    private int id;
    private String nume;
    private String data;
    private float temperature;

    public ModelareTabel(int id, String nume, String data, float temperature) {
        this.id = id;
        this.nume = nume;
        this.data = data;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "ModelareTabel{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", data='" + data + '\'' +
                ", temperature=" + temperature +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
