package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasActivity extends AppCompatActivity {

    EditText etcodigo,etfecha,etidentificacion,etplaca;
    TextView tvnombre,tvtelefono,tvmarca,tvvalor;
    CheckBox cbactivo;
    Button btadicionar,btanular;
    String codigo;
    //esto me permite vincularme con la BD Concesionario.db establecer conexion
    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);
        //ocultar barra de titulo
        getSupportActionBar().hide();
        //Asociar los objetos java con xml

        etcodigo=findViewById(R.id.etcodigo);
        etfecha=findViewById(R.id.etfecha);
        etidentificacion=findViewById(R.id.etidentificacion);
        etplaca=findViewById(R.id.etplaca);
        tvnombre=findViewById(R.id.tvnombre);
        tvtelefono=findViewById(R.id.tvtelefono);
        tvmarca=findViewById(R.id.tvmarca);
        tvvalor=findViewById(R.id.tvvalor);
        cbactivo=findViewById(R.id.cbactivo);
        btadicionar=findViewById(R.id.btadicionar);
        btanular=findViewById(R.id.btanular);
        etcodigo.requestFocus();

    }//fin del método oncreate

    public void Consultar(View view){
        //codigo es una variable definida la inicio
        codigo=etcodigo.getText().toString();
        //voy a buscar si el codigo si fue digitado
        if (!codigo.isEmpty()){
            //abrimos la conexion y bd en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //llamamos el cursor para definir los campos que se necesitan en la bd, inicia el inner join con las otras tablas
            Cursor registro=db.rawQuery("select Nom_cliente," +
                    "Tel_cliente,fecha,TblFactura.Ident_cliente," +
                    "TblFactura.Activo,TblDetalle_Factura.Placa,Marca," +
                    "Valor from TblCliente inner join TblFactura on " +
                    "TblCliente.Ident_cliente=TblFactura.Ident_cliente " +
                    "inner join TblDetalle_Factura on " +
                    "TblFactura.Cod_factura=TblDetalle_Factura.Cod_factura " +
                    "inner join TblVehiculo on TblDetalle_Factura.Placa=" +
                    "TblVehiculo.Placa where TblFactura.Cod_factura=" +
                    "'"+codigo+"'",null);
            if (registro.moveToNext()){
                //voy a mostrar la fecha
                etfecha.setText(registro.getString(2));
                tvnombre.setText(registro.getString(0));
                tvvalor.setText(registro.getString(7));
                if (registro.getString(4).equals("Si"))
                    cbactivo.setChecked(true);
                else
                    cbactivo.setChecked(false);
            }else{
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }

            //cerrar la conexion con la bd
            db.close();
        }else{
            Toast.makeText(this, "Codigo es requerido", Toast.LENGTH_SHORT).show();
            //esto es para que el cursor aparezca en este punto
            etcodigo.requestFocus();
        }


    }//fin del método consultar
}