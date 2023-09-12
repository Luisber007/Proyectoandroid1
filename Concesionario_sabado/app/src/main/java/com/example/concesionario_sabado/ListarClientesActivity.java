package com.example.concesionario_sabado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListarClientesActivity extends AppCompatActivity {

    ListView lvclientes;
    ArrayList<ClsRegistroClientes>  alclientes=new ArrayList<>();
    ArrayAdapter<ClsRegistroClientes> aaclientes;
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar objetos Java con objetos Xml
        lvclientes=findViewById(R.id.lvclientes);
        //Abrir la conexion en modo lectura
        SQLiteDatabase db=admin.getReadableDatabase();
        Cursor registro=db.rawQuery("Select * from TblCliente",null);
        //Mover la informacion del cursor a un ArrayList
        for(int k=0;k < registro.getCount();k++){
            registro.moveToNext();
            ClsRegistroClientes objregistro=new
            ClsRegistroClientes(registro.getString(0),
            registro.getString(1),registro.getString(2),
            registro.getString(3),registro.getString(4));
            alclientes.add(objregistro);
        }
        //llevar al adaptador el ArrayList
        aaclientes=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,alclientes);
        //Descargue los datos adaptados en el ListView
        lvclientes.setAdapter(aaclientes);
        db.close();
    }//fin metodo onCreate

    public void Regresar(View view){
        Intent intclientes=new Intent(this,ClientesActivity.class);
        startActivity(intclientes);
    }
}