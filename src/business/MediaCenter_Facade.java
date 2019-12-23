package business;

import business.media.Media;
import business.playlist.Playlist;
import business.utilizadores.Residente;
import business.exceptions.ExtensionInvalidException;
import business.exceptions.PasswordIncorretaException;
import business.exceptions.UserIncorretoException;
import data.BibGeralDAO;
import data.PlaylistDAO;
import data.ResidenteDAO;
import data.criaTabelas;

import java.util.List;
import java.util.stream.Collectors;

public class MediaCenter_Facade{

    private Residente current_user;

    private ResidenteDAO res_dao;
    private PlaylistDAO plist_dao;
    private BibGeralDAO bib_dao;
    private criaTabelas cr_tb;

    public MediaCenter_Facade(){
        this.res_dao = new ResidenteDAO();
        this.plist_dao = new PlaylistDAO();
        this.bib_dao = new BibGeralDAO();
        this.cr_tb = new criaTabelas();
    }

    public void criarTabelas(){
        cr_tb.criartabelaResidente();
        cr_tb.criartabelaUtilizador();
        cr_tb.criartabelaAdmin();
        cr_tb.criartabelaPlaylist();
        cr_tb.criartabelaBibGeral();
        cr_tb.criartabelaBibInd();
        cr_tb.criartabelaMusica();
        cr_tb.criartabelaVideo();
        cr_tb.criartabelabibGeral_has_Playlist();
    }

    // --------------------------- Métodos dos Utilizadores ---------------------------

    public boolean existeEmail (String email){
       return (this.res_dao.containsKey(email));
    }

    public boolean confirmaLogin (String email,String password,String tipo) throws UserIncorretoException,PasswordIncorretaException {
        if(tipo.equals("A")){
            if(this.existeEmail(email))
                if(password.equals(this.res_dao.get(email).getPassword()) && tipo.equals(this.res_dao.get(email).getTipo())) return true;
                else throw new PasswordIncorretaException("Password incorreta!");
            else {
                throw new UserIncorretoException("User não existe!");
            }
        }
        else{
            if(this.existeEmail(email))
                if(password.equals(this.res_dao.get(email).getPassword())) return true;
                else throw new PasswordIncorretaException("Password incorreta!");
            else {
                throw new UserIncorretoException("User não existe!");
            }
        }
    }

    public void login (String email,String password,String tipo) throws UserIncorretoException, PasswordIncorretaException {
        if (this.confirmaLogin(email,password,tipo)) {
            this.current_user = this.res_dao.get(email);
        }
    }
    public Residente getCurrent_user(){
        return this.current_user;
    }
    
    public boolean isGuest(){
        return current_user.getEmail().equals("guest");
    }
    
    public void setGuest() {
        this.current_user = this.res_dao.get("guest");
    }

    public void addConta(String email, String pw, String nome,String tipo) throws UserIncorretoException{
        Residente res = new Residente(email,pw,nome,tipo);
        if(!res_dao.containsKey(email))
            res_dao.put(res.getEmail(),res);
        else throw new UserIncorretoException("Email já existe!");
    }

    public void editContaEmail(String email, String novo_email) throws UserIncorretoException {
        if(this.existeEmail(email)){
            Residente temp = res_dao.get(email);
            temp.setEmail(novo_email);
            res_dao.remove(email);
            res_dao.put(temp.getEmail(),temp);
        }
        else throw new UserIncorretoException("User não existe!");
    }

    public void editContaNome(String email,String nome) throws UserIncorretoException {
        if (existeEmail(email)){
            Residente tmp = this.res_dao.get(email);
            tmp.setNome(nome);
            this.res_dao.put(email,tmp);
        }
        else throw new UserIncorretoException("User não existe!");
    }

    public void editContaPassword (String email,String password) throws UserIncorretoException{
        if(existeEmail(email)) {
            Residente tmp = this.res_dao.get(email);
            tmp.setPassword(password);
            this.res_dao.put(email,tmp);
        }
        else throw new UserIncorretoException("User não existe!");
    }

    public void deleteConta(String email) throws UserIncorretoException{
        if(existeEmail(email)) {
            this.res_dao.remove(email);
        }
        else throw new UserIncorretoException("User não existe!");
    }

    public void logout(){
        this.current_user = null;
    }
    
    // ------------------------------- Métodos da Media -------------------------------

    public String validaMedia(String path) throws ExtensionInvalidException{

        int i = path.lastIndexOf('.');
        if (i > 0) {
            String extension = path.substring(i+1);
            if(extension.equals("mp3") || extension.equals("mp4"))
                return extension;
        }
        throw new ExtensionInvalidException("Extensão inválida!");
    }

    public void addBibGeral(String nome, String autor, String duracao, String genero,String path,String tipo){
        int cod;
        if ((cod=bib_dao.codMedia(new Media(0,nome,autor,duracao,genero,path,tipo)))!= -1)
            bib_dao.addRelacao((new Media(cod,nome,autor,duracao,genero,path,tipo)),current_user.getEmail(),true);
        else{
            Media m = bib_dao.put(0,new Media(0,nome,autor,duracao,genero,path,tipo));
            bib_dao.addRelacao(m,current_user.getEmail(),true);}
    }

    public boolean existeMedia(Integer id){
        return this.bib_dao.containsKey(id);
    }

    public void alteraCat(Integer id, String nome){
        if(existeMedia(id)) {
            Media tmp = this.bib_dao.get(id);
            tmp.setGenero(nome);
            if(temmusica(id) && bib_dao.pertenceBibInd(id,current_user.getEmail())) bib_dao.addRelacao(tmp,current_user.getEmail(),true);
            else bib_dao.addRelacao(tmp,current_user.getEmail(),false);
        }
    }
    
    public boolean temmusica(int id){
        return bib_dao.temRelacao(id,current_user.getEmail());
    }

    public void removeMedia (int id){
        bib_dao.removeRelacao(id,current_user.getEmail());
        if(!bib_dao.unico(id,current_user.getEmail())) bib_dao.remove(id);
    }
    
    public List<String> toTable(){
        return bib_dao.toTable(current_user.getEmail());
    }

    public List<String> toTableInd(){
        return bib_dao.toTableInd(current_user.getEmail());
    }
     
    // ------------------------------ Métodos da Playlist -----------------------------
    
    public List<String> musicaG(String genero){
        List<String> res1 = bib_dao.values().stream().filter(x->x.getGenero().equals(genero)&& !(temmusica(x.getId()))).map(x->x.toStringEnters()).collect(Collectors.toList());
        List<String> res2 = bib_dao.toPlaylist(current_user.getEmail(),genero).stream().map(x->x.toStringEnters()).collect(Collectors.toList());
        res1.addAll(res2);
        return res1;
    }
    
    public int addPlaylist(String nome_pl){
        Playlist p = plist_dao.put(0,new Playlist(0,nome_pl,this.current_user.getEmail()));
        return p.getId();
    }
    
    public void addBigGeral_has_Playlist(int idMedia, int idPlaylist){
        plist_dao.addRelacao(idMedia, idPlaylist);
    }
    
    public List<String> pList(){
        return plist_dao.values().stream().filter(x->x.getEmail().equals(this.current_user.getEmail()))
              .map(x-> x.getNome() + " " + x.getId()).collect(Collectors.toList());
    }
    
    public List<String> idPlaylistToMedias(int idPlaylist){
        return this.plist_dao.idPlaylistToMedias(idPlaylist, current_user.getEmail());
    }
}