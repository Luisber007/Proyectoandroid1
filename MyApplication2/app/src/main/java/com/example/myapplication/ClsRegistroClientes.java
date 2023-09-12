package com.example.myapplication;

public class ClsRegistroClientes {
    // definí variables para usar el constructor
    private String identificacion;
    private String nombre;
    private String direccion;
    private String telefono;
    private String activo;

    //insertar un constructor

    public ClsRegistroClientes(String identificacion, String nombre, String direccion, String telefono, String activo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
    }
    //insertar un to String desde el click derecho generar
    //@Override indica que viene de otra clase
    @Override
    public String toString() {
        return "ClsRegistroClientes{" +
                "identificacion='" + identificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo='" + activo + '\'' +
                '}';
    }
}
