package business.utilizadores;

import business.utilizadores.Residente;

public class Utilizador extends Residente{

    public Utilizador(String email, String password, String nome,String tipo){
        super(email,password,nome,tipo);
    }
}