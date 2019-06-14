package com.example.aluno.petzdaily_cadastro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.example.aluno.petzdaily_cadastro.Model.Users;
import com.example.aluno.petzdaily_cadastro.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import io.paperdb.Paper;

public class PetzDailyLogin extends AppCompatActivity {

    private EditText edtLogin, edtPassword;
    private Button btnAcessar;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";
    private CheckBox chkboxLembrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petz_daily_login);

        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnAcessar = (Button) findViewById(R.id.btnAcessar);
        loadingBar = new ProgressDialog(this);

        chkboxLembrar = (CheckBox) findViewById(R.id.chkBoxLembrar);
        Paper.init(this);


        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        Paper.init(this);

        String UserLoginKey = Paper.book().read(Prevalent.UserLoginKey);
        String UserSenhaKey = Paper.book().read(Prevalent.UserSenhaKey);

        if (UserLoginKey != "" && UserSenhaKey != ""){
            if (!TextUtils.isEmpty(UserLoginKey) && !TextUtils.isEmpty(UserSenhaKey)){
                PermitirAcesso(UserLoginKey, UserSenhaKey);

                loadingBar.setTitle("Voce ja esta logado");
                loadingBar.setMessage("Aguarde");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }


    private void LoginUser(){
        String login = edtLogin.getText().toString();
        String pass = edtPassword.getText().toString();

        if(TextUtils.isEmpty(login)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "escreve ae", Toast.LENGTH_SHORT).show();
        }

        else{
            loadingBar.setTitle("Logando");
            loadingBar.setMessage("Aguarde");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            PermitirAcesso(login, pass);
        }
    }

    private void PermitirAcesso(final String login, final String pass){

        if(chkboxLembrar.isChecked()){
            Paper.book().write(Prevalent.UserLoginKey, login);
            Paper.book().write(Prevalent.UserSenhaKey, pass);

        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.child(parentDbName).child(login).exists()){
                    Users usersData = dataSnapshot.child(parentDbName).child(login).getValue(Users.class);

                    if (usersData.getLogin().equals(login))
                    {
                        if (usersData.getSenha().equals(pass))
                        {
                            if(parentDbName.equals("Users")) {
                                Toast.makeText(PetzDailyLogin.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Prevalent.currentOnlineUser = usersData;
                                Intent intent = new Intent(PetzDailyLogin.this, PetzDailyPerfilPetNav.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(PetzDailyLogin.this, "Esse login nao existe", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });
    }



    public void onClickRegistrar(View v){
        Intent it = new Intent(PetzDailyLogin.this , MainActivity.class);

        startActivity(it);
    }



}
