package com.example.kojima_transporte_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kojima_transporte_app.MainActivity;
import com.example.kojima_transporte_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class telaCadastro extends AppCompatActivity {

    private ImageButton botaocadastrar;
    private EditText edit_cpf, edit_senha, repetir_senha;
    String[] mensagens = {"Preencha todos os campos" , "Cadastro realizado"};
    String UsuariosID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        botaocadastrar = findViewById(R.id.Bc3_button);
        EditText senhaEdit = findViewById(R.id.editSenha);
        EditText repeatPassword = findViewById(R.id.Repetirsenha);
        ImageView senhaIcone  = findViewById(R.id.Senha);
        IniciarComponentes();

        final PasswordTransformationMethod passwordTransformationMethod = new PasswordTransformationMethod();
        senhaEdit.setTransformationMethod(passwordTransformationMethod);
        repeatPassword.setTransformationMethod(passwordTransformationMethod);
        senhaIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (senhaEdit.getTransformationMethod() == null) {
                    senhaEdit.setTransformationMethod(passwordTransformationMethod);
                } else {
                    senhaEdit.setTransformationMethod(null);
                }
                senhaEdit.setSelection(senhaEdit.getText().length());

                if (senhaEdit.getTransformationMethod() == null) {
                    repeatPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                } else {
                    repeatPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
            }
        });
        botaocadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String cpf = edit_cpf.getText().toString();
              String senha = edit_senha.getText().toString();
              String repeat = repetir_senha.getText().toString();

              if (cpf.isEmpty() || senha.isEmpty() || repeat.isEmpty()){
                  Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                  snackbar.setBackgroundTint(Color.WHITE);
                  snackbar.setTextColor(Color.BLACK);
                  snackbar.show();
              }else{
                  CadastrarUsuario(v);

              }
            }
        });
    }

    private void CadastrarUsuario(View v){

        String cpf = edit_cpf.getText().toString();
        String senha = edit_senha.getText().toString();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(cpf,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    SalvarDadosUsuario();
                    Toast.makeText(telaCadastro.this, "Cadastro realizado", Toast.LENGTH_SHORT).show();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "A senha precisa ter 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Este email já está cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "email invalido";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }

                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void SalvarDadosUsuario(){
        String email = edit_cpf.getText().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("email",email);

        UsuariosID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = database.collection("Usuarios").document(UsuariosID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("database", "Dados salvos com sucesso");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("database_error", "Erro ao salvar os dados" + e.toString());
                    }
                });

    }
    private void IniciarComponentes(){
        edit_cpf = findViewById(R.id.editcpf);
        edit_senha = findViewById(R.id.editSenha);
        repetir_senha = findViewById(R.id.Repetirsenha);
    }

}