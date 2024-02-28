package com.example.kojima_transporte_app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBase extends SQLiteOpenHelper {
    private  Context context;
    private static final String DATABASE_NAME = "OrdemTransporte.db";
    private static final int DATABASE_VERSION = 17;
    private static final String TABLE_NAME = "OrdemDeTransporte";
    private static final String DATA = "DATA";
    private static final String _ID = "_ID";
    private static final String MOTORISTA = "Motorista";
    private static final String VEICULO = "Veiculo";
    private static final String PLACA = "Placa";
    private static final String KM_SAIDA = "KmSaida";
    private static final String KM_FINAL = "KmFinal";
    private static final String HORA_INICIAL = "HoraInicial";
    private static final String HORA_FINAL = "HoraFinal";
    private static final String DATA_INICIAL = "DataInicial";
    private static final String DATA_FINAL = "DataFinal";
    private static final String PERNOITES = "Pernoites";
    private static final String CHEGADA = "Chegada";
    private static final String SAIDA = "Saida";
    private static final String DESTINO = "Destino";
    private static final String SAIDA_DESTINO = "DestinoSaida";
    private static final String HORA_CHEGADA = "HoraChegada";
    private static final String HORA_SAIDA = "HoraSaida";
    private static final String HORA_DESTINO = "HoraDestino";
    private static final String DATA_DESTINO = "DataDestino";
    private static final String OBSERVACOES =  "Observacoes";



    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MOTORISTA + "  TEXT, " +
                VEICULO + " TEXT, " +
                DATA + " TEXT, " +
                PLACA + " INTEGER, " +
                KM_SAIDA + " INTEGER, " +
                KM_FINAL + " INTEGER, " +
                HORA_FINAL + " INTEGER, " +
                HORA_INICIAL + " INTEGER, " +
                DATA_FINAL + " INTEGER, " +
                DATA_INICIAL + " INTEGER, " +
                PERNOITES + " INTEGER," +
                CHEGADA + " INTEGER, " +
                SAIDA + " INTEGER, " +
                DESTINO + " INTEGER, " +
                SAIDA_DESTINO + " INTEGER, " +
                HORA_CHEGADA + " INTEGER," +
                HORA_SAIDA + " INTEGER," +
                HORA_DESTINO + " INTEGER," +
                DATA_DESTINO + " INTEGER," +
                OBSERVACOES + " TEXT )";
                db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void AdicionarOrdem(String motorista, String veiculo, String placa, String data, String datainicial,
                               String datafinal, String kmfinal, String kmsaida, String horafinal,
                               String horainicial, String pernoites, String chegada, String saida, String destino, String saidadestino,
                               String horachegada,String horasaida,String horadestino,String datadestino, String observacoes){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MOTORISTA, motorista);
        cv.put(VEICULO, veiculo);
        cv.put(PLACA, placa);
        cv.put(DATA, data);
        cv.put(DATA_INICIAL, datainicial);
        cv.put(DATA_FINAL, datafinal);
        cv.put(KM_FINAL, kmfinal);
        cv.put(KM_SAIDA, kmsaida);
        cv.put(HORA_FINAL, horafinal);
        cv.put(HORA_INICIAL, horainicial);
        cv.put(PERNOITES, pernoites);
        cv.put(CHEGADA, chegada);
        cv.put(SAIDA, saida);
        cv.put(DESTINO, destino);
        cv.put(SAIDA_DESTINO, saidadestino);
        cv.put(HORA_CHEGADA, horachegada);
        cv.put(HORA_SAIDA, horasaida);
        cv.put(HORA_DESTINO, horadestino);
        cv.put(DATA_DESTINO, datadestino);
        cv.put(OBSERVACOES, observacoes);


        long result = db.insert(TABLE_NAME,null, cv);
        if (result == -1){
            Toast.makeText(context, "Falha", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor readAlldata() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

   public void atualizarDados(String row_id, String motorista, String veiculo, String placa, String data, String datainicial,
                              String datafinal, String kmfinal, String kmsaida, String horafinal,
                              String horainicial, String pernoites, String chegada, String saida, String destino, String saidadestino,
                              String horachegada, String horasaida, String horadestino, String datadestino, String observacoes){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

       cv.put(MOTORISTA, motorista);
       cv.put(VEICULO, veiculo);
       cv.put(PLACA, placa);
       cv.put(DATA, data);
       cv.put(DATA_INICIAL, datainicial);
       cv.put(DATA_FINAL, datafinal);
       cv.put(KM_FINAL, kmfinal);
       cv.put(KM_SAIDA, kmsaida);
       cv.put(HORA_FINAL, horafinal);
       cv.put(HORA_INICIAL, horainicial);
       cv.put(PERNOITES, pernoites);
       cv.put(CHEGADA, chegada);
       cv.put(SAIDA, saida);
       cv.put(DESTINO, destino);
       cv.put(SAIDA_DESTINO, saidadestino);
       cv.put(HORA_CHEGADA, horachegada);
       cv.put(HORA_SAIDA, horasaida);
       cv.put(HORA_DESTINO, horadestino);
       cv.put(DATA_DESTINO, datadestino);
       cv.put(OBSERVACOES, observacoes);

        long result = db.update(TABLE_NAME,cv,"_ID=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Ordem de transporte não atualizada", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Ordem de transporte atualizada", Toast.LENGTH_SHORT).show();
        }
    }

    void DeletarDados(String row_id){
        SQLiteDatabase Db = this.getWritableDatabase();
        long result = Db.delete(TABLE_NAME,"_ID=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Falha ao deletar", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Ordem de transporte deletada", Toast.LENGTH_SHORT).show();
        }
    }

}
