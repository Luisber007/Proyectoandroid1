package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

    long respuesta, respuesta1;

    boolean sw;
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
        // Habilitar el botón "Adicionar" al inicio
        btadicionar.setEnabled(true);

    }//fin del método oncreate



    public void Consultar(View view) {
        codigo = etcodigo.getText().toString();

        if (!codigo.isEmpty()) {
            SQLiteDatabase db = admin.getReadableDatabase();

            Cursor registro=db.rawQuery("select Nom_cliente," +
                    "Tel_cliente,Fecha,TblFactura.Ident_Cliente," +
                    "TblFactura.Activo,TblDetalle_Factura.Placa,Marca," +
                    "Valor from TblCliente inner join TblFactura on " +
                    "TblCliente.Ident_Cliente=TblFactura.Ident_Cliente " +
                    "inner join TblDetalle_Factura on " +
                    "TblFactura.Cod_factura=TblDetalle_Factura.Cod_factura " +
                    "inner join TblVehiculo on TblDetalle_Factura.Placa=" +
                    "TblVehiculo.Placa where TblFactura.Cod_factura='"+codigo+"'",null);


            if (registro.moveToNext()) {
                tvtelefono.setText(registro.getString(1));
                etfecha.setText(registro.getString(2));
                etidentificacion.setText(registro.getString(3));
                etplaca.setText(registro.getString(5));
                tvmarca.setText(registro.getString(6));
                tvnombre.setText(registro.getString(0));
                tvvalor.setText(registro.getString(7));
                if (registro.getString(4).equals("Si"))
                    cbactivo.setChecked(true);
                else
                    cbactivo.setChecked(false);

                // Habilitar las funciones Cliente y Vehiculo
                etidentificacion.setEnabled(false);
                etplaca.setEnabled(false);
                btadicionar.setEnabled(false);
                btanular.setEnabled(true);
                etfecha.setEnabled(false);

            } else {
                Toast.makeText(this, "Registro no hallado", Toast.LENGTH_SHORT).show();
                etfecha.requestFocus();
            }

            db.close();
        } else {
            Toast.makeText(this, "Código es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }
    public void ConsultarCliente(View view) {
        String identificacion = etidentificacion.getText().toString();
        if (!identificacion.isEmpty()) {
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor registro = db.rawQuery("select Nom_cliente, Tel_cliente from TblCliente where Ident_cliente='" + identificacion + "'", null);
            if (registro.moveToNext()) {
                tvnombre.setText(registro.getString(0));
                tvtelefono.setText(registro.getString(1));
            } else {
                Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show();
                tvnombre.setText("");
                tvtelefono.setText("");
                etidentificacion.requestFocus();
            }
            db.close();
        } else {
            Toast.makeText(this, "Identificación del cliente requerida", Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        }
    }

    public void ConsultarVehiculo(View view) {
        String placa = etplaca.getText().toString();
        if (!placa.isEmpty()) {
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor registro = db.rawQuery("select Marca, Valor from TblVehiculo where Placa='" + placa + "'", null);
            if (registro.moveToNext()) {
                tvmarca.setText(registro.getString(0));
                tvvalor.setText(registro.getString(1));
            } else {
                Toast.makeText(this, "Vehículo no encontrado", Toast.LENGTH_SHORT).show();
                tvmarca.setText("");
                tvvalor.setText("");
                etplaca.requestFocus();
            }
            db.close();
        } else {
            Toast.makeText(this, "Placa del vehículo requerida", Toast.LENGTH_SHORT).show();
            etplaca.requestFocus();
        }
    }

    public void Adicionar(View view) {
        String codigo = etcodigo.getText().toString();
        String fecha = etfecha.getText().toString();
        String identificacion = etidentificacion.getText().toString();
        String placa = etplaca.getText().toString();
        String activo = cbactivo.isChecked() ? "Si" : "No";

        if (codigo.isEmpty() || fecha.isEmpty() || identificacion.isEmpty() || placa.isEmpty()) {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = admin.getWritableDatabase();

        // Verificar si la placa ya existe en la tabla TblDetalle_Factura
        Cursor verificarPlaca = db.rawQuery("SELECT * FROM TblDetalle_Factura WHERE Placa = ? AND Cod_factura <> ?",
                new String[]{placa, codigo});

        if (verificarPlaca.getCount() > 0) {
            Toast.makeText(this, "La placa ya está asociada a otra factura", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues registro = new ContentValues();
            registro.put("Cod_factura", codigo);
            registro.put("Fecha", fecha);
            registro.put("Ident_Cliente", identificacion);

            long respuesta = db.insert("TblFactura", null, registro);
            if (respuesta > 0) {
                // Si respuesta es diferente de -1, la inserción fue exitosa
                ContentValues registro1 = new ContentValues();
                registro1.put("Cod_factura", codigo);
                registro1.put("Placa", placa);
                registro1.put("Valor_venta", tvvalor.getText().toString());
                long respuesta1 = db.insert("TblDetalle_Factura", null, registro1);
                if (respuesta1 > 0) {
                    Toast.makeText(this, "Factura agregada con éxito", Toast.LENGTH_SHORT).show();
                    Limpiar(view);
                } else {
                    Toast.makeText(this, "Error al agregar la factura", Toast.LENGTH_SHORT).show();
                }
            }
        }

        db.close();
    }


    public void Anular(View view) {
        String codigo = etcodigo.getText().toString();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "El campo Código es requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("Activo", "No");

        int cantidad = db.update("TblFactura", registro, "Cod_factura='" + codigo + "'", null);

        /*int cantidad = db.update("TblFactura", registro, "Codigo='" + codigo + "'", null);*/
        db.close();

        if (cantidad > 0) {
            Toast.makeText(this, "Factura anulada con éxito", Toast.LENGTH_SHORT).show();
            Limpiar(view);
        } else {
            Toast.makeText(this, "No se encontró una factura con ese código", Toast.LENGTH_SHORT).show();
        }
    }//fin metodo anular

    public void Limpiar(View view){
        Limpiar_campos();
    }//fin metodo Limpiar

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }//fin metodo Regresar

    private void Limpiar_campos(){
        etcodigo.setText("");
        etfecha.setText("");
        etidentificacion.setText("");
        etplaca.setText("");
        tvnombre.setText("");
        tvtelefono.setText("");
        tvmarca.setText("");
        tvvalor.setText("");
        cbactivo.setChecked(false);
        etcodigo.setEnabled(true);
        btadicionar.setEnabled(true);
        btanular.setEnabled(true);
        etidentificacion.setEnabled(true);
        tvnombre.setEnabled(false);
        tvtelefono.setEnabled(false);
        etplaca.setEnabled(true);
        tvmarca.setEnabled(false);
        tvvalor.setEnabled(false);
        sw=false;
    }//fin del metodo Limpiar_campos
}