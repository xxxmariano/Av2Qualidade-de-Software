package com.example.crudusuario;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipal extends AppCompatActivity {

    Button btUsuarioMenu;
    Button btSairMenu;
    TextView txtUsuario;
    Button btContagem;
    TextView textContagem;
    private int contagem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        btSairMenu = findViewById(R.id.btSairMenu);
        btUsuarioMenu = findViewById(R.id.btUsuarioMenu);
        txtUsuario = findViewById(R.id.textUsuario);
        btContagem = findViewById(R.id.btContagem);
        textContagem = findViewById(R.id.textContagem);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            txtUsuario.setText(bundle.getString("NomeUsuario"));
        }

        btUsuarioMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
                finish();
            }
        });

        btSairMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, TelaAutenticacao.class));
                finish();
            }
        });

        btContagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contagem ++;
                textContagem.setText("Você lavou as mãos: " + contagem);
            }
        });
    }
}
