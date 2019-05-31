package com.example.aluno.petzdaily_cadastro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private EditText editText /*nome*/;
    private EditText editText3 /*email*/;
    private EditText editText2 /*login*/;
    private EditText edtPassword /*senha*/;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        editText = (EditText) findViewById(R.id.editText);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText2 = (EditText) findViewById(R.id.editText2);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        loadingBar = new ProgressDialog(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();

            }
        });
    }

    private void CreateAccount(){
        String nome = editText.getText().toString();
        String email = editText3.getText().toString();
        String login = editText2.getText().toString();
        String pass = edtPassword.getText().toString();


        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(nome)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Criando a conta");
            loadingBar.setMessage("Aguarde");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateLogin(login, nome, email, pass);
        }

    }

    private void ValidateLogin(final String login, final String nome, final String email, final String pass){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Users").child(login).exists()){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Login", login);
                    userdataMap.put("Nome", nome);
                    userdataMap.put("Email", email);
                    userdataMap.put("Senha", pass);

                    RootRef.child("Users").child(login).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Sua conta foi criada com sucesso!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(MainActivity.this, PetzDailyLogin.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(MainActivity.this, "Deu erro velho", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this, "Esse" + login + "já existe.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Tente outro login.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, PetzDailyLogin.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClickRegistrar(View v){
        Intent it = new Intent(MainActivity.this , PetzDailyLogin.class);

        startActivity(it);
    }
}
