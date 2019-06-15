package com.example.aluno.petzdaily_cadastro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.petzdaily_cadastro.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class carteira_vacina extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText edtNomeVacina, edtDataVacina, edtReacaoVacina;
    Button btnSalvar;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carteira_vacina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        edtDataVacina = (EditText) findViewById(R.id.edtDataVacina);
        edtNomeVacina = (EditText) findViewById(R.id.edtNomeVacina);
        edtReacaoVacina = (EditText) findViewById(R.id.edtReacaoVacina);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        loadingBar = new ProgressDialog(this);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();

            }
        });

        View headerview = navigationView.getHeaderView(0);
        TextView nomeUsuario = headerview.findViewById(R.id.nomeUsuario);
        CircleImageView fotoUsuario = headerview.findViewById(R.id.fotoUsuario);

        nomeUsuario.setText(Prevalent.currentOnlineUser.getNome());
    }



    private void CreateAccount(){
        String nome = edtNomeVacina.getText().toString();
        String data = edtDataVacina.getText().toString();
        String reacao = edtReacaoVacina.getText().toString();


        if(TextUtils.isEmpty(nome)){
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(data)){
            Toast.makeText(this, "Digite a data", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Adicionando vacina");
            loadingBar.setMessage("Aguarde");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateLogin(nome, data, reacao);
        }

    }

    private void ValidateLogin( final String nome, final String data, final String reacao){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Evento").child(nome).exists()){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("nome", nome);
                    userdataMap.put("data", data);
                    userdataMap.put("reacao", reacao);

                    RootRef.child("Vacina").child(nome).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(carteira_vacina.this, "Vacina criada com sucesso!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(carteira_vacina.this, PetzDailyVacinaNav.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(carteira_vacina.this, "Deu erro velho", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(carteira_vacina.this, "Esse" + nome + "ja existe.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(carteira_vacina.this, "Tente outro nome de vacina.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(carteira_vacina.this, PetzDailyVacinaNav.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_evento) {
            Intent it = new Intent(this,PetzDailyEventoNav.class);
            startActivity(it);
        }

        if (id == R.id.nav_perfil){
            Intent it = new Intent(this,PetzDailyPerfilPetNav.class);
            startActivity(it);
        }

        if (id == R.id.nav_sair){
            Intent it = new Intent(this, PetzDailyLogin.class);
            startActivity(it);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
