package com.example.aluno.petzdaily_cadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class PetzDailyLogin extends AppCompatActivity {
	
    private EditText edtLogin, edtPassword;
    private Button btnAcessar;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petz_daily_login);

        edtLogin = (EditText) findViewById(R.id.edtLogin)
        edtPassword = (EditText) findViewById(R.id.edtPassword);
	btnAcessar = (Button) findViewById(R.id.btnAcessar);
	loadingBar = new ProgressDialog(this);
	

	btnAcessar.setOnClickListener(new View.OnClickListener{
		@Override
		public void onClick (View view){
			loginUser();
		}
	});

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
		final DatabaseReference RootRef;
		RootRef = FirebaseDatabase.getInstance().getReference();

		RootRef.addListenerForSingleValueEvent(new ValueEventListener(){
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot){
				if (dataSnapshot.child(parentDbName).child(login).exists()){
					
				}
				
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError){
				Toast.makeText(PetzDailyLogin.this, "Esse login não existe", Toast.LENGTH_SHORT).show();
			}
		});
	}



    public void onClickRegistrar(View v){
        Intent it = new Intent(PetzDailyLogin.this , MainActivity.class);

        startActivity(it);
    }

    public void onClickAcessar(View v){
        Intent it = new Intent(PetzDailyLogin.this , PetzDailyPrinciPerfilNav.class);

        startActivity(it);
    }
}
