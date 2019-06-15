package com.example.aluno.petzdaily_cadastro;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class PetzDailyConfig extends AppCompatActivity {

    private TextView closeTextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petz_daily_config);

        closeTextbtn = (TextView) findViewById(R.id.close_settings_btn);

        closeTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}


