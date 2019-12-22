package business;

public class Media {

    private int id;
    private String nome;
    private String autor;
    private String duracao;
    private String genero;
    private String path;
    private String tipo;

    public Media(int id, String nome, String autor, String duracao, String genero, String path, String tipo) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.duracao = duracao;
        this.genero = genero;
        this.path = path;
        this.tipo = tipo;
    }


    public String getNome() {
        return nome;
    }

    public String getAutor() {
        return autor;
    }

    public String getDuracao() {
        return duracao;
    }

    public int getId() {
        return id;
    }

    public String getGenero() {
        return genero;
    }

    public String getPath() {
        return path;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", autor='" + autor + '\'' +
                ", duracao=" + duracao +
                ", genero='" + genero + '\'' +
                ", path='" + path + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
    
    public String toStringEnters(){
        return id + "\n" + nome + "\n" + autor + "\n" + duracao + "\n" + genero + "\n" + path + "\n" + tipo;
    }
}