package com.example.aluno.petzdaily_cadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PetzDailyLogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petz_daily_login);

    }

    public void onClickRegistrar(View v){
        Intent it = new Intent(PetzDailyLogin.this , MainActivity.class);

        startActivity(it);
    }
}
