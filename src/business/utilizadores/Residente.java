package business.utilizadores;

public class Residente {

    private String email;
    private String password;
    private String nome;
    private String  tipo;

    public Residente(String email, String password, String nome,String tipo) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.tipo=tipo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo){this.tipo=tipo;}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo(){return this.tipo;}
    
    @java.lang.Override
    public java.lang.String toString() {
        return "Residente{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}