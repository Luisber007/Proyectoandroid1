package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }
    public void Clientes(View View){
        Intent intClientes=new Intent(this, ClientesActivity.class);
        startActivity(intClientes);
    }
    public void Vehiculo(View View){
        Intent intVehiculo=new Intent(this, VehiculoActivity.class);
        startActivity(intVehiculo);
    }
    public void Facturas(View View){
        Intent intFacturas=new Intent(this, FacturasActivity.class);
        startActivity(intFacturas);
    }

}