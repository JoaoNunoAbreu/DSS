package business.media;

import business.media.Media;

public class Video extends Media{
    public Video(int id, String nome, String autor, String duracao,String genero,String path, String tipo) {
        super(id,nome,autor,duracao,genero,path,tipo);
    }
}