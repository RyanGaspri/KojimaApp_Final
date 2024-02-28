package com.example.kojima_transporte_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kojima_transporte_app.model.DataBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class telaPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ImageView semdados;
    private TextView no_data;

    RecyclerView recyclerView;

    DataBase mydb;
    ArrayList<String> id, motorista, veiculo, data,placa,km_saida,km_final,hora_final,hora_inicial,
            data_final, data_inicial, pernoites, chegada, saidaDoCliente, destino, saidaDestino, horaChegada,
    horaSaida, horaDestino, dataSaida, observacoes;
    CustomAdapter customAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        recyclerView = findViewById(R.id.RecyclerView);

        no_data = findViewById(R.id.bang);
        semdados = findViewById(R.id.semdados);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =  new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_logout);
        }


        FloatingActionButton Adicionar = findViewById(R.id.activity_lista_alunos_fab_novo_aluno);
        Adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(telaPrincipal.this, telaTransporte.class));
            }
        });

        mydb = new DataBase(telaPrincipal.this);
        id = new ArrayList<>();
        motorista = new ArrayList<>();
        veiculo = new ArrayList<>();
        data = new ArrayList<>();
        placa = new ArrayList<>();
        km_saida = new ArrayList<>();
        km_final = new ArrayList<>();
        hora_final = new ArrayList<>();
        hora_inicial = new ArrayList<>();
        data_final = new ArrayList<>();
        data_inicial = new ArrayList<>();
        pernoites = new ArrayList<>();
        chegada = new ArrayList<>();
        saidaDoCliente = new ArrayList<>();
        destino = new ArrayList<>();
        saidaDestino = new ArrayList<>();
        horaChegada = new ArrayList<>();
        horaSaida = new ArrayList<>();
        horaDestino = new ArrayList<>();
        dataSaida = new ArrayList<>();
        observacoes = new ArrayList<>();



        customAdapter = new CustomAdapter(telaPrincipal.this,this, id, motorista, veiculo, data,placa,km_saida,
                km_final,hora_final, hora_inicial,data_final,data_inicial,pernoites,chegada, saidaDoCliente, destino, saidaDestino,
                horaChegada, horaSaida, horaDestino, dataSaida, observacoes);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(telaPrincipal.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            recreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        storeDatainArrays();
    }

    void storeDatainArrays(){
        Cursor cursor = mydb.readAlldata();
        if (cursor.getCount() == 0){
            semdados.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                motorista.add(cursor.getString(1));
                veiculo.add(cursor.getString(2));
                data.add(cursor.getString(3));
                placa.add(cursor.getString(4));
                km_saida.add(cursor.getString(5));
                km_final.add(cursor.getString(6));
                hora_final.add(cursor.getString(7));
                hora_inicial.add(cursor.getString(8));
                data_final.add(cursor.getString(9));
                data_inicial.add(cursor.getString(10));
                pernoites.add(cursor.getString(11));
                chegada.add(cursor.getString(12));
                saidaDoCliente.add(cursor.getString(13));
                destino.add(cursor.getString(14));
                saidaDestino.add(cursor.getString(15));
                horaChegada.add(cursor.getString(16));
                horaSaida.add(cursor.getString(17));
                horaDestino.add(cursor.getString(18));
                dataSaida.add(cursor.getString(19));
                observacoes.add(cursor.getString(20));

            }
            semdados.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(telaPrincipal.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Voce saiu!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
