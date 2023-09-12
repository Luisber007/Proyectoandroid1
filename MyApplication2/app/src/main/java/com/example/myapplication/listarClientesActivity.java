package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class listarClientesActivity extends AppCompatActivity {

    ListView Lvclientes;
    ArrayList<ClsRegistroClientes> alcliente = new ArrayList<>();
    ArrayAdapter<ClsRegistroClientes> aaclientes;
    //esta linea establece una conexion con la base de datos
    ClsOpenHelper admin = new ClsOpenHelper(this, "Concesionario.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);
        //ocultar la barra de titulo
        getSupportActionBar().hide();
        //Asociar objetos java con xml
        Lvclientes = findViewById(R.id.Lvclientes);
        //Abrir la conexion en modo lectura
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor registro = db.rawQuery("select * from Tblclientes", null);
        //mover la informacion del cursor a un ArrayList
        for (int k = 0; k < registro.getCount(); k++) ;
        {
            registro.moveToNext();
            //instanciar la clase con los datos registrados en el registro clientes
            ClsRegistroClientes objregistro=new ClsRegistroClientes(registro.getString(0),
                    registro.getString(1),registro.getString(2),
                    registro.getString(3),registro.getString(4));
            //esto lo que hace es un recorrdio en el objeto
            alcliente.add(objregistro);
        }
        //llevar al adaptador el Arraylisto
        //a este adaptador aaclientes quiero que me lo adapte en un nuevo adaptador y
        // quiero que me lo adapte dentro del array listo alclientes
        aaclientes=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,alcliente);
        //Descargue los datos adaptadosen el ListView
        Lvclientes.setAdapter(aaclientes);
        db.close();
    }//fin metodo oncreate

    public void Regresar(View view){
        Intent intclientes=new Intent(this, ClientesActivity.class);
        startActivity(intclientes);

    }
}