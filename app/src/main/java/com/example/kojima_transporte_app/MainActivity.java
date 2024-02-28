package com.example.kojima_transporte_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public class MainActivity extends AppCompatActivity {

    private ImageButton botaologin;
    private ImageButton botaocadastrar;
    private TextView textView;
    private EditText edit_cpf, edit_senha;
    private FirebaseAuth auth;
    String[] mensagem = {"Preencha todos os campos"};
   



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciarComponentes();

        botaologin = findViewById(R.id.Be_button);

        EditText SenhaEdit = findViewById(R.id.senhalogin);
        ImageView iconeSenha = findViewById(R.id.iconeSenha);

        final PasswordTransformationMethod passwordTransformationMethod = new PasswordTransformationMethod();
        SenhaEdit.setTransformationMethod(passwordTransformationMethod);

        iconeSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SenhaEdit.getTransformationMethod() == null){
                    SenhaEdit.setTransformationMethod(passwordTransformationMethod);
                }else{
                    SenhaEdit.setTransformationMethod(null);
                }
                SenhaEdit.setSelection(SenhaEdit.getText().length());
            }
        });

        botaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_cpf.getText().toString();
                String senha = edit_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagem[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }else{
                    AutenticarUsuario(v);
                    Toast.makeText(MainActivity.this, "Login efetuado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        botaocadastrar = findViewById(R.id.bc2_button);
        botaocadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), telaCadastro.class);
                startActivity(intent);
            }
        });

        textView = findViewById(R.id.forgotpassword);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.forgotpassword,null);
                EditText emailbox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String UserEmail = emailbox.getText().toString();

                        if (TextUtils.isEmpty(UserEmail) && !Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
                            Toast.makeText(MainActivity.this, "Entre com seu email de registro", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth = FirebaseAuth.getInstance();

                        auth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Cheque seu email :)", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(MainActivity.this, "Não foi possivel enviar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

    }

    private void IniciarComponentes(){
        edit_cpf = findViewById(R.id.cpflogin);
        edit_senha = findViewById(R.id.senhalogin);
        textView = findViewById(R.id.forgotpassword);
        botaologin = findViewById(R.id.Be_button);
    }

    private void AutenticarUsuario(View view){
        String email = edit_cpf.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    TelaPrincipal();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao validar os dados";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

   @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(MainActivity.this,telaPrincipal.class);
        startActivity(intent);
        finish();
    }
}