package com.example.crudusuario;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.crudusuario.usuario.Usuario;
import com.example.crudusuario.usuario.UsuarioDAO;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private EditText txtNome;
    private EditText txtLogin;
    private EditText txtSenha;
    private Button btSalvar;
    private Button btPesq;
    private Button btExcluir;
    private Button btSincronizar;

    UsuarioDAO usuarioDAO;
    Usuario usuario;
    Usuario usuarioCarregado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNome = findViewById(R.id.txtNome);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        btSalvar = findViewById(R.id.btSalvar);
        btPesq = findViewById(R.id.btPesq);
        btExcluir = findViewById(R.id.btExcluir);
        btSincronizar = findViewById(R.id.btSinc);


        usuarioDAO = new UsuarioDAO(openOrCreateDatabase(UsuarioDAO.NOME_BANCO, MODE_PRIVATE, null));


        btSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btSincronizar.setEnabled(false);
                btSincronizar.setText("Sincronizando!");

                ArrayList<Usuario> listaUsuariosSinc = usuarioDAO.listar();
                if(listaUsuariosSinc != null){
                    sincronizarWebServiceUsuario(listaUsuariosSinc);

                }

            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarUsuarioDuplicado(txtLogin.getText().toString());
            }
        });


        btPesq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListarUsuarios.class));
            }
        });

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioCarregado.getId() != 0) {
                    usuarioDAO.deletar(usuarioCarregado);
                    limparCampos();
                }
            }
        });


        carregarUsuario();
    }


    public void salvarUsuario(){
        usuario = new Usuario();
        if (usuarioCarregado == null) {
            usuarioCarregado = new Usuario();
        }

        if (usuarioCarregado.getId() != 0) {
            usuarioCarregado.setNome(txtNome.getText().toString());
            usuarioCarregado.setLogin(txtLogin.getText().toString());
            usuarioCarregado.setSenha(txtSenha.getText().toString());
            usuarioDAO.salvar(usuarioCarregado);
            Toast.makeText(getApplicationContext(), "Usuário Editado!", Toast.LENGTH_SHORT).show();
        } else {
            usuario.setNome(txtNome.getText().toString());
            usuario.setLogin(txtLogin.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());
            usuarioDAO.salvar(usuario);
            Toast.makeText(getApplicationContext(), "Usuário Cadastrado!", Toast.LENGTH_SHORT).show();
        }

        limparCampos();
    }

    public void carregarUsuario() {
        Intent i = getIntent();
        usuarioCarregado = (Usuario) i.getSerializableExtra("usuario");
        if (usuarioCarregado != null) {
            txtNome.setText(usuarioCarregado.getNome());
            txtLogin.setText(usuarioCarregado.getLogin());
            txtSenha.setText(usuarioCarregado.getSenha());

            btExcluir.setEnabled(true);
        } else {
            btExcluir.setEnabled(false);
        }

    }

    public void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");

        usuario = new Usuario();
        usuarioCarregado = new Usuario();
        btExcluir.setEnabled(false);
    }


    public void sincronizarWebServiceUsuario(ArrayList<Usuario> listaUsuarios) {
        OkHttpClient client = new OkHttpClient();
        Request request;
        String url = "";
        for (final Usuario usuario : listaUsuarios) {

            url = "http://192.168.1.5:8080/webservice/usuario/salvar.php?"+"id="+usuario.getId()
                    +"&nome="+usuario.getNome()
                    +"&login="+usuario.getLogin()
                    +"&senha="+usuario.getSenha();

                 request = new Request.Builder()
                         .url(url)
                         .get()
                         .build();


                 client.newCall(request).enqueue(new Callback() {
                     @Override
                     public void onFailure(Call call, IOException e) {
                         e.printStackTrace();

                         MainActivity.this.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 Toast.makeText(getApplicationContext(), "Erro ao Sincronizar!", Toast.LENGTH_SHORT).show();
                                 btSincronizar.setEnabled(true);
                                 btSincronizar.setText("Sincronizar");
                             }
                         });
                     }

                     @Override
                     public void onResponse(Call call, final Response response) throws IOException {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            boolean enviou = response.isSuccessful();
                            @Override
                            public void run() {
                                if(enviou){
                                    Toast.makeText(getApplicationContext(), "Dados Sincronizados!", Toast.LENGTH_SHORT).show();
                                    btSincronizar.setEnabled(true);
                                    btSincronizar.setText("Sincronizar");
                                    usuarioDAO.deletar(usuario);
                                }
                            }
                        });
                     }
                 });

        }
    }


    public void verificarUsuarioDuplicado(String login){
        OkHttpClient client = new OkHttpClient();
        Request request;
        String url = "http://192.168.1.5:8080/webservice/usuario/verificar_duplicado.php?"+
                "login="+login;

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resposta = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resposta.equals("false")){
                            salvarUsuario();
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuário já existe!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }



}
