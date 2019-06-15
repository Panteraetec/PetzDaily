package com.example.aluno.petzdaily_cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aluno.petzdaily_cadastro.Prevalent.Prevalent;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetzDailyPerfilPetNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imgPet;
    private FloatingActionButton adcPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petz_daily_perfil_pet_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imgPet = (ImageView) findViewById(R.id.imgPet);

        adcPet = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        adcPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Irpara();
            }
        });

        View headerview = navigationView.getHeaderView(0);
        TextView nomeUsuario = headerview.findViewById(R.id.nomeUsuario);
        CircleImageView fotoUsuario = headerview.findViewById(R.id.fotoUsuario);

        nomeUsuario.setText(Prevalent.currentOnlineUser.getNome());

        imgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PetzDailyPerfilPetNav.this, PetzDailyPrinciPerfilNav.class);
                startActivity(i);
            }
        });
    }

    private void Irpara() {
        Intent i = new Intent (PetzDailyPerfilPetNav.this, CadastroPetzDailyNav.class);
        i.putExtra("Pet", "Pet");
        startActivity(i);
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
        if (id == R.id.nav_manage){
            Intent it = new Intent (this, PetzDailyConfig.class);
            startActivity(it);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
