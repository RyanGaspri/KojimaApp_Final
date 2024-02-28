package com.example.kojima_transporte_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kojima_transporte_app.model.DataBase;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class telaTransporte extends AppCompatActivity {

    private EditText dataEditText;
    private EditText motoristaEditText;
    private EditText veiculoEditText;
    private EditText placaEditText;
    private EditText kmSaidaEditText;
    private EditText kmFinalEditText;
    private EditText horaInicialEditText;
    private EditText dataHoraInicialEditText;
    private EditText horaFinalEditText;
    private EditText dataHoraFinalEditText;
    private EditText pernoitesEditText;
    private EditText observacoesEdit;

    //detalhes do cliente
    private EditText chegadaDoCliente;
    private EditText saidaDoCliente;
    private EditText Destino;
    private EditText saidaDestino;
    private EditText horarioChegada;
    private EditText horarioSaida;
    private EditText horarioDestino;
    private EditText HoraSaidaCliente;
    public static final int CREATEPDF = 1;
    private double distanciaPercorrida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_transporte);

        dataEditText = findViewById(R.id.DataOT);
        motoristaEditText = findViewById(R.id.MotoristaEdit);
        veiculoEditText = findViewById(R.id.veiculoEdit);
        placaEditText = findViewById(R.id.placaEdit);
        kmSaidaEditText = findViewById(R.id.KmSaidaEdit);
        kmFinalEditText = findViewById(R.id.KmFinalEdit);
        horaInicialEditText = findViewById(R.id.HoraInicialEdit);
        dataHoraInicialEditText = findViewById(R.id.DataHI);
        horaFinalEditText = findViewById(R.id.HoraFinalEdit);
        dataHoraFinalEditText = findViewById(R.id.DataHF);
        pernoitesEditText = findViewById(R.id.PernoitesEdit);
        observacoesEdit = findViewById(R.id.Observacoes);

        //detalhes do cliente

        chegadaDoCliente = findViewById(R.id.ChegadaCliente);
        saidaDoCliente = findViewById(R.id.saidaDoCliente);
        Destino = findViewById(R.id.Destino);
        saidaDestino = findViewById(R.id.DestinoSaida);
        horarioChegada = findViewById(R.id.HoraChegada);
        horarioSaida = findViewById(R.id.HoraSaida);
        horarioDestino = findViewById(R.id.HoraDestino);
        horarioChegada = findViewById(R.id.HoraChegada);
        HoraSaidaCliente = findViewById(R.id.HoraSaidaCliente);

        Button BotaoSalvar= findViewById(R.id.botaosalvar);
        BotaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase DB = new DataBase(telaTransporte.this);
                DB.AdicionarOrdem(motoristaEditText.getText().toString().trim(),
                        veiculoEditText.getText().toString().trim(),
                        placaEditText.getText().toString().trim(),
                        dataEditText.getText().toString().trim(),
                        dataHoraInicialEditText.getText().toString().trim(),
                        dataHoraFinalEditText.getText().toString().trim(),
                        kmFinalEditText.getText().toString().trim(),
                        kmSaidaEditText.getText().toString().trim(),
                        horaFinalEditText.getText().toString().trim(),
                        horaInicialEditText.getText().toString().trim(),
                        pernoitesEditText.getText().toString().trim(),
                        chegadaDoCliente.getText().toString().trim(),
                        saidaDoCliente.getText().toString().trim(),
                        Destino.getText().toString().trim(),
                        saidaDestino.getText().toString().trim(),
                        horarioChegada.getText().toString().trim(),
                        horarioSaida.getText().toString().trim(),
                        horarioDestino.getText().toString().trim(),
                        HoraSaidaCliente.getText().toString().trim(),
                        observacoesEdit.getText().toString().trim());

                Intent intent = new Intent(telaTransporte.this, telaPrincipal.class);
                startActivity(intent);
                finish();
            }
        }
        );

        Button botaogerarpdf = findViewById(R.id.gerarpdf);
        botaogerarpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(kmSaidaEditText.getText())) && !(TextUtils.isEmpty(kmFinalEditText.getText()))) {
                    double kmSaida = Double.parseDouble(kmSaidaEditText.getText().toString().trim());
                    double kmFinal = Double.parseDouble(kmFinalEditText.getText().toString().trim());
                    calcularDistancia(kmSaida, kmFinal);
                    criarpdf("OrdemDeTransporte", distanciaPercorrida);
                }
            }
        });
    }

    private void calcularDistancia(double kmSaida, double kmFinal){
        distanciaPercorrida = kmFinal - kmSaida;
    }

    private double calcularTempoParado(String dataEvento1, String horarioEvento1, String dataEvento2, String horarioEvento2) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date evento1 = format.parse(dataEvento1 + " " + horarioEvento1);
            Date evento2 = format.parse(dataEvento2 + " " + horarioEvento2);

            // Calcula o tempo entre os eventos diretamente
            long tempoTotalEmMilissegundos = evento2.getTime() - evento1.getTime();

            // Calcular dias inteiros entre as datas
            long diasParados = tempoTotalEmMilissegundos / (24 * 60 * 60 * 1000);

            // Calcula o tempo em horas para os dias parados
            double horasParadas = diasParados * 24;

            // Calcula o tempo restante entre os eventos (em horas)
            long tempoRestanteEmMilissegundos = tempoTotalEmMilissegundos % (24 * 60 * 60 * 1000);
            double horasRestantes = (double) tempoRestanteEmMilissegundos / (60 * 60 * 1000);

            // Retorna o total de horas considerando dias parados e horas restantes
            return horasParadas + horasRestantes;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // ou outro valor padrão, caso haja erro na conversão
        }
    }



    public void AdicionarData1(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dataEditText.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void AdicionarData2(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dataHoraInicialEditText.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void AdicionarData3(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dataHoraFinalEditText.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void DataChegada(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        chegadaDoCliente.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void DataSaida(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        saidaDoCliente.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void DataDestino(View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Destino.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void SaidaDestino (View view){
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        saidaDestino.setText(dataselecionada);
                    }
                },ano,mes,dia);
        datePickerDialog.show();
    }

    public void TimePicker1(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horaInicialEditText.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void TimePicker2(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horaFinalEditText.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void HoraChegada(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horarioChegada.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void HoraSaida(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horarioSaida.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void HoraDestino(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horarioDestino.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void HoraSaidaDestino(View view){

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                HoraSaidaCliente.setText(hora1);
            }
        }, hour , minute, false);

        timePickerDialog.show();
    }

    public void criarpdf(String title, double distanciaPercorrida){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE,title);
        intent.putExtra("distanciaPercorrida", distanciaPercorrida);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATEPDF) {
            if (data.getData() != null){
                if (!(TextUtils.isEmpty(dataEditText.getText())) && !(TextUtils.isEmpty(motoristaEditText.getText())) && !(TextUtils.isEmpty(veiculoEditText.getText()))
                        && !(TextUtils.isEmpty(placaEditText.getText())) && !(TextUtils.isEmpty(kmSaidaEditText.getText())) && !(TextUtils.isEmpty(kmFinalEditText.getText()))
                        && !(TextUtils.isEmpty(horaInicialEditText.getText())) && !(TextUtils.isEmpty(horaFinalEditText.getText())) && !(TextUtils.isEmpty(dataHoraInicialEditText.getText()))
                        && !(TextUtils.isEmpty(dataHoraFinalEditText.getText())) && !(TextUtils.isEmpty(pernoitesEditText.getText())))
                {
                    Uri CaminhoDoArquivo = data.getData();

                    String item1 = dataEditText.getText().toString();
                    String item2 = motoristaEditText.getText().toString();
                    String item3 = veiculoEditText.getText().toString();
                    String item4 = placaEditText.getText().toString();
                    String item5 = kmSaidaEditText.getText().toString();
                    String item6 = kmFinalEditText.getText().toString();
                    String item7 = horaInicialEditText.getText().toString();
                    String item8 = horaFinalEditText.getText().toString();
                    String item9 = dataHoraInicialEditText.getText().toString();
                    String item10 = dataHoraFinalEditText.getText().toString();
                    String item11 = pernoitesEditText.getText().toString();
                    String item12 = chegadaDoCliente.getText().toString();
                    String item13 = saidaDoCliente.getText().toString();
                    String item14 = Destino.getText().toString();
                    String item15 = saidaDestino.getText().toString();
                    String item16 = horarioChegada.getText().toString();
                    String item17 = horarioSaida.getText().toString();
                    String item18 = horarioDestino.getText().toString();
                    String item19 = HoraSaidaCliente.getText().toString();
                    String item20 = observacoesEdit.getText().toString();

                    // chamada para calcular tempo parado no cliente
                    String dataChegadaCliente = chegadaDoCliente.getText().toString(); // Supondo que você tenha um EditText para a data de chegada
                    String horarioChegadaCliente = horarioChegada.getText().toString();
                    String dataSaidaCliente = saidaDoCliente.getText().toString(); // Supondo que você tenha um EditText para a data de saída
                    String horarioSaidaCliente = horarioSaida.getText().toString();
                    Double tempoParadoCliente = calcularTempoParado(dataChegadaCliente, horarioChegadaCliente, dataSaidaCliente, horarioSaidaCliente);

                    // chamada para calcular tempo parado no destino
                    String dataChegadaDestino = Destino.getText().toString(); // Supondo que você tenha um EditText para a data de chegada no destino
                    String horarioChegadaDestino = horarioDestino.getText().toString();
                    String dataSaidaDestino = saidaDestino.getText().toString(); // Supondo que você tenha um EditText para a data de saída do destino
                    String horarioSaidaDestino = HoraSaidaCliente.getText().toString();
                    Double tempoParadoDestino = calcularTempoParado(dataChegadaDestino, horarioChegadaDestino, dataSaidaDestino, horarioSaidaDestino);


                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2240, 2454,1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();

                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(64f);
                    paint.setFakeBoldText(true);

                    int titleY = 50;
                    canvas.drawText("Ordem de Transporte", pageInfo.getPageWidth() / 2, titleY, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(50f);
                    paint.setFakeBoldText(false);

                    int lineHeight = 60;

                    int infoStartY = titleY + 2 * lineHeight;

                    canvas.drawText("Data: " + item1, 50, infoStartY, paint);
                    canvas.drawLine(48, infoStartY + lineHeight, pageInfo.getPageWidth() - 100, infoStartY + lineHeight, paint);

                    canvas.drawText("Motorista: " + item2, 50, infoStartY + 2 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 3 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 3 * lineHeight, paint);

                    canvas.drawText("Veiculo: " + item3, 50, infoStartY + 4 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 5 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 5 * lineHeight, paint);

                    canvas.drawText("Placa: " + item4, 50, infoStartY + 6 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 7 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 7 * lineHeight, paint);

                    canvas.drawText("Km Saida: " + item5, 50, infoStartY + 8 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 9 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 9 * lineHeight, paint);

                    canvas.drawText("Km Final: " + item6, 50, infoStartY + 10 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 11 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 11 * lineHeight, paint);

                    canvas.drawText("Hora Inicial: " + item7, 50, infoStartY + 12 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 13 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 13 * lineHeight, paint);

                    canvas.drawText("Hora final: " + item8, 50, infoStartY + 14 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 15 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 15 * lineHeight, paint);

                    canvas.drawText("Data Inicial: " + item9, 50, infoStartY + 16 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 17 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 17 * lineHeight, paint);

                    canvas.drawText("Data Final: " + item10, 50, infoStartY + 18 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 19 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 19 * lineHeight, paint);

                    canvas.drawText("Pernoites: " + item11, 50, infoStartY + 20 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 21 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 21 * lineHeight, paint);

                    canvas.drawText("Distância Percorrida: " + distanciaPercorrida + " km", 50, infoStartY + 22 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 23 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 23 * lineHeight, paint);

                    canvas.drawText("Chegada no Cliente: " + item12 + " -- " + item16, 50, infoStartY + 24 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 25 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 25 * lineHeight, paint);

                    canvas.drawText("Saida do cliente: " + item13 + " -- " + item17, 50, infoStartY + 26 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 27 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 27 * lineHeight, paint);

                    canvas.drawText("Destino: " + item14 + " -- " + item18, 50, infoStartY + 28 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 29 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 29 * lineHeight, paint);

                    canvas.drawText("Saida do destino: " + item15 + " -- " + item19, 50, infoStartY + 30 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 31 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 31 * lineHeight, paint);

                    canvas.drawText("Tempo parado no cliente: " + tempoParadoCliente + " Horas", 50, infoStartY + 32 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 33 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 33 * lineHeight, paint);

                    canvas.drawText("Tempo parado no destino: " + tempoParadoDestino + " Horas", 50, infoStartY + 34 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 35 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 35 * lineHeight, paint);

                    canvas.drawText("Observações: " + item20, 50, infoStartY + 36 * lineHeight, paint);
                    canvas.drawLine(48, infoStartY + 37 * lineHeight, pageInfo.getPageWidth() - 100, infoStartY + 37 * lineHeight, paint);

                    pdfDocument.finishPage(page);
                    gravarpdf(CaminhoDoArquivo, pdfDocument);
                }
            }
        }

    }

    private void gravarpdf(Uri CaminhoDoArquivo, PdfDocument pdfDocument){
        try {
            BufferedOutputStream stream = new BufferedOutputStream(getContentResolver().openOutputStream(CaminhoDoArquivo));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "PDF gerado", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){

            Toast.makeText(this, "Arquivo não encontrado", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(this, "Erro de entrada e saida", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this, "Erro 404564", Toast.LENGTH_SHORT).show();

        }
    }
}