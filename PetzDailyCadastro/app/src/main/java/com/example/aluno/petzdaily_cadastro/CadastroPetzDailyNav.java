package com.example.aluno.petzdaily_cadastro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.petzdaily_cadastro.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class CadastroPetzDailyNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String Petzada, nome, raca, especie, idade, sexo, saveCurrentData, saveCurrentTime ;
    private Button CadastrarPet;
    private ImageView AdicionarImgPet;
    private EditText edtNome, edtRaca, edtEspecie, edtIdade, edtSexo;
    private Uri ImageUri;
    private static final int FotoGaleria = 1;
    private String PetChaveRandom, downloadImageURL;
    private StorageReference PetImgRef;
    private DatabaseReference PetRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_petz_daily_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Petzada = getIntent().getExtras().get("Pet").toString();
        PetImgRef = FirebaseStorage.getInstance().getReference().child("Fotos Pet");
        PetRef = FirebaseDatabase.getInstance().getReference().child("Pets");

        CadastrarPet = (Button) findViewById(R.id.btnCadastrarPet);
        AdicionarImgPet = (ImageView) findViewById(R.id.addPet);
        edtNome = (EditText) findViewById(R.id.txt_CpNome);
        edtRaca = (EditText) findViewById(R.id.txt_CpRaca);
        edtEspecie = (EditText) findViewById(R.id.txt_CpEspecie);
        edtIdade = (EditText) findViewById(R.id.txt_CpIdade);
        edtSexo = (EditText) findViewById(R.id.txt_CpSexo);
        loadingBar = new ProgressDialog(this);

        AdicionarImgPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirGaleria();
            }
        });

        CadastrarPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePetData();
            }
        });

        View headerview = navigationView.getHeaderView(0);
        TextView nomeUsuario = headerview.findViewById(R.id.nomeUsuario);
        CircleImageView fotoUsuario = headerview.findViewById(R.id.fotoUsuario);

        nomeUsuario.setText(Prevalent.currentOnlineUser.getNome());



    }


    private void AbrirGaleria(){
        Intent galeriaIntent = new Intent();
        galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent, FotoGaleria);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==FotoGaleria && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            AdicionarImgPet.setImageURI(ImageUri);
        }
    }

    private void ValidatePetData(){
        nome = edtNome.getText().toString();
        raca = edtRaca.getText().toString();
        especie = edtEspecie.getText().toString();
        idade = edtIdade.getText().toString();
        sexo = edtSexo.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Foto do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nome)){
            Toast.makeText(this, "Nome do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(raca)){
            Toast.makeText(this, "Raça do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(idade)){
            Toast.makeText(this, "Idade do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(especie)){
            Toast.makeText(this, "Idade do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(sexo)){
            Toast.makeText(this, "Idade do pet eh obrigatorio", Toast.LENGTH_SHORT).show();
        }
        else{
            StorePetInformation();
        }
    }

    private void StorePetInformation() {
        loadingBar.setTitle("Adicionando novo Pet");
        loadingBar.setMessage("Aguarde");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentData = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        PetChaveRandom = saveCurrentData + saveCurrentTime;

        final StorageReference filePath = PetImgRef.child(ImageUri.getLastPathSegment() + PetChaveRandom + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(CadastroPetzDailyNav.this, "Erro: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CadastroPetzDailyNav.this, "Foto do pet  upada com sucesso", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                });
            }

        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                    downloadImageURL = task.getResult().toString();

                    Toast.makeText(CadastroPetzDailyNav.this, "Imagem salva no banco com sucesso", Toast.LENGTH_SHORT).show();

                    SavePetInfoToDatabase();
                }
            }
        });
    }

    private void SavePetInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", PetChaveRandom);
        productMap.put("date", saveCurrentData);
        productMap.put("time", saveCurrentTime);
        productMap.put("nome", nome);
        productMap.put("image", downloadImageURL);
        productMap.put("pet", Petzada);
        productMap.put("raca", raca);
        productMap.put("especie", especie);
        productMap.put("idade", idade);
        productMap.put("sexo", sexo);

        PetRef.child(PetChaveRandom).updateChildren(productMap)
                .addOnCompleteListener (new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        if(task.isSuccessful()){

                            Intent intent = new Intent(CadastroPetzDailyNav.this, PetzDailyPerfilPetNav.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(CadastroPetzDailyNav.this, "Pet cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(CadastroPetzDailyNav.this, "Erro: " + message, Toast.LENGTH_SHORT).show();
                        }
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

        if (id == R.id.nav_perfil){
            Intent it = new Intent(this,PetzDailyPerfilPetNav.class);
            startActivity(it);
        }

        if (id == R.id.nav_sair){
            Paper.book().destroy();
            Intent it = new Intent(this, PetzDailyLogin.class);
            startActivity(it);
        }


        if (id == R.id.nav_evento) {
            Intent it = new Intent(this,PetzDailyEventoNav.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
