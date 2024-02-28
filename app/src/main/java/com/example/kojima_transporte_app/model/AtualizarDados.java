package com.example.kojima_transporte_app.model;

import static com.example.kojima_transporte_app.telaTransporte.CREATEPDF;

import android.app.DatePickerDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kojima_transporte_app.R;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AtualizarDados extends AppCompatActivity {

    EditText data2, motorista, veiculo, placa, kmsaida, kmfinal, hfinal,
            hinicial, data_inicial, data_final, pernoites, chegada2, saida2, destino2, saidaDestino2,
            horaChegada2, horaSaida2, horaDestino2, HoraDes2, observacoes2;

    String id, data_, motorista_, veiculo_, placa_, km_saida, km_final, h_final, h_inicial,
            Data_inicial, Data_final, pernoites_, chegada_, saida_, destino_, saidaDestino_, horaChegada_,
    horaSaida_,horaDestino_,dataDes_, observacoes_;

    Button botao_atualizar;
    Button BotaoExluir;

    Button BotaoGerar;
    private double distanciaPercorrida;

    public static final int CREATEPDF = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_dados);

        data2 = findViewById(R.id.DataOT2);
        motorista = findViewById(R.id.MotoristaEdit2);
        veiculo = findViewById(R.id.veiculoEdit2);
        placa = findViewById(R.id.placaEdit2);
        kmsaida = findViewById(R.id.KmSaidaEdit2);
        kmfinal = findViewById(R.id.KmFinalEdit2);
        hfinal = findViewById(R.id.HoraFinalEdit2);
        hinicial = findViewById(R.id.HoraInicialEdit2);
        data_inicial = findViewById(R.id.DataHI2);
        data_final = findViewById(R.id.DataHF2);
        pernoites = findViewById(R.id.PernoitesEdit2);
        observacoes2 = findViewById(R.id.Observacoes2);

        chegada2 = findViewById(R.id.ChegadaCliente2);
        saida2 = findViewById(R.id.saidaDoCliente2);
        destino2 = findViewById(R.id.Destino2);
        saidaDestino2 = findViewById(R.id.DestinoSaida2);
        horaChegada2 = findViewById(R.id.HoraChegada2);
        horaSaida2 = findViewById(R.id.HoraSaida2);
        horaDestino2 = findViewById(R.id.HoraDestino2);
        HoraDes2 = findViewById(R.id.HoraSaidaCliente2);


        botao_atualizar = findViewById(R.id.botaoatualizar);
        BotaoExluir = findViewById(R.id.botaoexcluir);
        BotaoGerar = findViewById(R.id.botaogerar);

        BotaoGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(kmsaida.getText())) && !(TextUtils.isEmpty(kmfinal.getText()))) {
                    double kmSaida = Double.parseDouble(kmsaida.getText().toString().trim());
                    double kmFinal = Double.parseDouble(kmfinal.getText().toString().trim());
                    calcularDistancia(kmSaida, kmFinal);
                    criarPDF("OrdemDeTransporte", distanciaPercorrida);
                }
            }
        });


        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(motorista_);
        }
        botao_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase DB = new DataBase(AtualizarDados.this);
                data_ = data2.getText().toString().trim();
                motorista_ = motorista.getText().toString().trim();
                veiculo_ = veiculo.getText().toString().trim();
                placa_ = placa.getText().toString().trim();
                km_saida = kmsaida.getText().toString().trim();
                km_final = kmfinal.getText().toString().trim();
                Data_inicial = data_inicial.getText().toString().trim();
                Data_final = data_final.getText().toString().trim();
                h_final = hfinal.getText().toString().trim();
                h_inicial = hinicial.getText().toString().trim();
                pernoites_ = pernoites.getText().toString().trim();
                chegada_ = chegada2.getText().toString().trim();
                saida_ = saida2.getText().toString().trim();
                destino_ = destino2.getText().toString().trim();
                saidaDestino_ = saidaDestino2.getText().toString().trim();
                horaChegada_ = horaChegada2.getText().toString().trim();
                horaSaida_ = horaSaida2.getText().toString().trim();
                horaDestino_ = horaDestino2.getText().toString().trim();
                dataDes_ = HoraDes2.getText().toString().trim();
                observacoes_ = observacoes2.getText().toString().trim();


                DB.atualizarDados(id, motorista_, veiculo_, placa_, data_, Data_inicial, Data_final, km_final,
                        km_saida, h_final, h_inicial, pernoites_, chegada_, saida_, destino_, saidaDestino_,horaChegada_,horaSaida_,
                        horaDestino_,dataDes_, observacoes_);

                finish();
            }
        });

        BotaoExluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    private void calcularDistancia(double kmSaida, double kmFinal) {
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


    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") &&
                getIntent().hasExtra("data") &&
                getIntent().hasExtra("motorista") &&
                getIntent().hasExtra("veiculo") &&
                getIntent().hasExtra("placa") &&
                getIntent().hasExtra("kmsaida") &&
                getIntent().hasExtra("kmfinal") &&
                getIntent().hasExtra("datainicial") &&
                getIntent().hasExtra("datafinal") &&
                getIntent().hasExtra("horainicial") &&
                getIntent().hasExtra("horafinal") &&
                getIntent().hasExtra("pernoites") &&
                getIntent().hasExtra("chegada") &&
                getIntent().hasExtra("saidaDoCliente") &&
                getIntent().hasExtra("destino") &&
                getIntent().hasExtra("saidaDestino") &&
                getIntent().hasExtra("horaChegada") &&
                getIntent().hasExtra("horaSaida") &&
                getIntent().hasExtra("horaDestino") &&
                getIntent().hasExtra("dataDestino") &&
                getIntent().hasExtra("observacoes")) {

            //pegando os dados da tela
            id = getIntent().getStringExtra("id");
            data_ = getIntent().getStringExtra("data");
            motorista_ = getIntent().getStringExtra("motorista");
            veiculo_ = getIntent().getStringExtra("veiculo");
            placa_ = getIntent().getStringExtra("placa");
            Data_inicial = getIntent().getStringExtra("datainicial");
            Data_final = getIntent().getStringExtra("datafinal");
            km_final = getIntent().getStringExtra("kmfinal");
            km_saida = getIntent().getStringExtra("kmsaida");
            h_final = getIntent().getStringExtra("horafinal");
            h_inicial = getIntent().getStringExtra("horainicial");
            pernoites_ = getIntent().getStringExtra("pernoites");
            chegada_ = getIntent().getStringExtra("chegada");
            saida_ = getIntent().getStringExtra("saidaDoCliente");
            destino_ = getIntent().getStringExtra("destino");
            saidaDestino_ = getIntent().getStringExtra("saidaDestino");
            horaChegada_ = getIntent().getStringExtra("horaChegada");
            horaSaida_ = getIntent().getStringExtra("horaSaida");
            horaDestino_ = getIntent().getStringExtra("horaDestino");
            dataDes_ = getIntent().getStringExtra("dataDestino");
            observacoes_ =  getIntent().getStringExtra("observacoes");

            data2.setText(data_);
            motorista.setText(motorista_);
            veiculo.setText(veiculo_);
            placa.setText(placa_);
            data_inicial.setText(Data_inicial);
            data_final.setText(Data_final);
            kmfinal.setText(km_final);
            kmsaida.setText(km_saida);
            hfinal.setText(h_final);
            hinicial.setText(h_inicial);
            pernoites.setText(pernoites_);
            chegada2.setText(chegada_);
            saida2.setText(saida_);
            destino2.setText(destino_);
            saidaDestino2.setText(saidaDestino_);
            horaChegada2.setText(horaChegada_);
            horaSaida2.setText(horaSaida_);
            horaDestino2.setText(horaDestino_);
            HoraDes2.setText(dataDes_);
            observacoes2.setText(observacoes_);


        } else {
            Toast.makeText(this, "Não Há dados", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar a ordem de transporte Numero " + id + " ?");
        builder.setMessage("Voce não poderá recuperar !");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBase db = new DataBase(AtualizarDados.this);
                db.DeletarDados(id);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    public void showDatePicker(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        data2.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    public void showDatePicker2(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        data_inicial.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    public void showDatePicker3(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        data_final.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    public void Chegada(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        chegada2.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    public void Saida(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        saida2.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }
    public void Destino(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        destino2.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }
    public void SaidaDoDestino(View view) {
        final Calendar c = Calendar.getInstance();
        int ano = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataselecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        saidaDestino2.setText(dataselecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    public void hora1(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                hinicial.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void hora2(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                hfinal.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }
    public void hora3(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horaChegada2.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void hora4(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horaSaida2.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void hora5(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                horaDestino2.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void hora6(View view) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CalendarTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String hora1 = sdf.format(calendar.getTime());

                HoraDes2.setText(hora1);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void criarPDF(String title, double distanciaPercorrida) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra("distanciaPercorrida", distanciaPercorrida);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {
                if (!(TextUtils.isEmpty(data2.getText())) && !(TextUtils.isEmpty(motorista.getText())) && !(TextUtils.isEmpty(veiculo.getText()))
                        && !(TextUtils.isEmpty(placa.getText())) && !(TextUtils.isEmpty(kmsaida.getText())) && !(TextUtils.isEmpty(kmfinal.getText()))
                        && !(TextUtils.isEmpty(hfinal.getText())) && !(TextUtils.isEmpty(hinicial.getText())) && !(TextUtils.isEmpty(data_inicial.getText()))
                        && !(TextUtils.isEmpty(data_final.getText())) && !(TextUtils.isEmpty(pernoites.getText()))) {
                    Uri CaminhoDoArquivo = data.getData();

                    String item1 = data2.getText().toString();
                    String item2 = motorista.getText().toString();
                    String item3 = veiculo.getText().toString();
                    String item4 = placa.getText().toString();
                    String item5 = kmsaida.getText().toString();
                    String item6 = kmfinal.getText().toString();
                    String item7 = hinicial.getText().toString();
                    String item8 = hfinal.getText().toString();
                    String item9 = data_inicial.getText().toString();
                    String item10 = data_final.getText().toString();
                    String item11 = pernoites.getText().toString();
                    String item12 = chegada2.getText().toString();
                    String item13 = saida2.getText().toString();
                    String item14 = destino2.getText().toString();
                    String item15 = saidaDestino2.getText().toString();
                    String item16 = horaChegada2.getText().toString();
                    String item17 = horaSaida2.getText().toString();
                    String item18 = horaDestino2.getText().toString();
                    String item19 = HoraDes2.getText().toString();
                    String item20 = observacoes2.getText().toString();

                    // chamada para calcular tempo parado no cliente
                    String dataChegadaCliente = chegada2.getText().toString(); // Supondo que você tenha um EditText para a data de chegada
                    String horarioChegadaCliente = horaChegada2.getText().toString();
                    String dataSaidaCliente = saida2.getText().toString(); // Supondo que você tenha um EditText para a data de saída
                    String horarioSaidaCliente = horaSaida2.getText().toString();
                    Double tempoParadoCliente = calcularTempoParado(dataChegadaCliente, horarioChegadaCliente, dataSaidaCliente, horarioSaidaCliente);

                    // chamada para calcular tempo parado no destino
                    String dataChegadaDestino = destino2.getText().toString(); // Supondo que você tenha um EditText para a data de chegada no destino
                    String horarioChegadaDestino = horaDestino2.getText().toString();
                    String dataSaidaDestino = saidaDestino2.getText().toString(); // Supondo que você tenha um EditText para a data de saída do destino
                    String horarioSaidaDestino = HoraDes2.getText().toString();
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
                    // ...

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

    private void gravarpdf(Uri CaminhoDoArquivo, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(getContentResolver().openOutputStream(CaminhoDoArquivo));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "PDF gerado", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {

            Toast.makeText(this, "Arquivo não encontrado", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Erro de entrada e saida", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Erro 404564", Toast.LENGTH_SHORT).show();
        }
    }
}