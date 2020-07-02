package com.example.crudusuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.crudusuario.usuario.Usuario;
import com.example.crudusuario.usuario.UsuarioDAO;

import java.util.ArrayList;

public class ListarUsuarios extends AppCompatActivity {

    Button btPesquisar;
    ListView listViewUsuarios;
    EditText txtPesquisar;

    UsuarioDAO usuarioDAO;
    ArrayList<Usuario> listaUsuarios;

    ArrayAdapter usuarioAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);

        btPesquisar = findViewById(R.id.btPesquisar);
        listViewUsuarios = findViewById(R.id.listUsuarios);
        txtPesquisar = findViewById(R.id.txtPesquisar);

        usuarioDAO = new UsuarioDAO(openOrCreateDatabase(usuarioDAO.NOME_BANCO, MODE_PRIVATE, null));

        //PESQUISA E LISTA TODOS OS USUÁRIOS
        listaUsuarios = usuarioDAO.listar();
        listarUsuarios(listaUsuarios);

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtPesquisar.getText().toString().isEmpty()){
                    listaUsuarios = usuarioDAO.listar("nome", txtPesquisar.getText().toString());
                    listarUsuarios(listaUsuarios);
                }
            }
        });

        listViewUsuarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListarUsuarios.this, MainActivity.class);
                intent.putExtra("usuario", listaUsuarios.get(position));

                startActivity(intent);

                return false;
            }
        });


    }

    public void listarUsuarios(ArrayList<Usuario> lista){
        usuarioAdaptador = new ArrayAdapter<Usuario>(this,
                android.R.layout.simple_expandable_list_item_2,
                android.R.id.text2,
                lista);
        listViewUsuarios.setAdapter(usuarioAdaptador);
    }
}
