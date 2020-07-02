package com.example.crudusuario.usuario;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UsuarioDAO {

    private SQLiteDatabase banco;
    public static final String NOME_BANCO = "crud";
    public static final String NOME_TABELA = "usuarios";

    public UsuarioDAO(SQLiteDatabase banco){
        try {
            this.banco = banco;
            //Criar ou abrir a tabela
            banco.execSQL("CREATE TABLE IF NOT EXISTS "+NOME_TABELA+
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR (100)," +
                    "login VARCHAR (30)," +
                    "senha VARCHAR (30))");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void salvar(Usuario usuario){
        try{
            // se vrdd cadastra
            if(usuario.getId() == 0){
                banco.execSQL("INSERT INTO "+NOME_TABELA+
                        "(nome, login, senha) VALUES" +
                        "('"+usuario.getNome()+"'," +
                        "'"+usuario.getLogin()+"'," +
                        "'"+usuario.getSenha()+"')");
            }else{
                // se nao, edita
                banco.execSQL("UPDATE "+NOME_TABELA+" SET "+
                        "nome = '"+usuario.getNome() + "',"+
                        "login = '"+usuario.getLogin() + "',"+
                        "senha = '"+usuario.getSenha() + "' WHERE "+
                        "id = "+usuario.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean deletar(Usuario usuario){
        try {
            banco.execSQL("DELETE FROM "+NOME_TABELA+" WHERE id="+usuario.getId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Usuario> listar(){
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        Usuario usuario;

        try{
            Cursor cursor = banco.rawQuery("SELECT * FROM "+ NOME_TABELA, null);

            cursor.moveToFirst();
            while(cursor != null){
                //capturando dados do usuário do cursor
                usuario = new Usuario();
                usuario.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));

                //adicionando usuário na lista
                listaUsuarios.add(usuario);

                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    public ArrayList<Usuario> listar(String campo, String valor){
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        Usuario usuario;

        try{
            Cursor cursor = banco.rawQuery("SELECT * FROM "+ NOME_TABELA +" WHERE "+campo+
            " LIKE '%"+valor+"%'", null);

            cursor.moveToFirst();
            while(cursor != null){
                //capturando dados do usuário do cursor
                usuario = new Usuario();
                usuario.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));

                //adicionando usuário na lista
                listaUsuarios.add(usuario);

                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    public boolean autenticarUsuario(String login, String senha){
        return true;
    }

}
