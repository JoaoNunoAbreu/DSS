package data;

import business.media.Media;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BibGeralDAO implements Map<Integer, Media> {

    private Connection conn;

    @Override
    public int size() {

        int res=0;

        try{
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT count(*) FROM bibGeral"); 
            ResultSet rs = stm.executeQuery();
            if(rs.next()) {
                res = rs.getInt(1);
            }
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r = false;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT nome FROM bibGeral WHERE idbibGeral=?");
            stm.setInt(1,(Integer)key);
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    public boolean temRelacao(Integer id,String em) {
        boolean r = false;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT bibGeral_idbibGeral FROM bibInd WHERE bibGeral_idbibGeral=? AND  Residente_email=?");
            stm.setInt(1,id);
            stm.setString(2,em);
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Media r = (Media) value;
        return containsKey(r.getId());
    }

    public Integer codMedia(Object value) {
        Media m = (Media) value;
       
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT idbibGeral FROM bibGeral WHERE nome=? AND autor=?");
            stm.setString(1,m.getNome());
            stm.setString(2,m.getAutor());
            ResultSet rs = stm.executeQuery();
            int x;
            if (rs.next()){
                x=rs.getInt(1);
                return x;
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return -1;
    }

    @Override
    public Media get(Object key) {
        Media r = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM bibgeral WHERE idbibGeral=?");
            stm.setInt(1, (Integer)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                r = new Media(rs.getInt("idbibGeral"),rs.getString("nome"),rs.getString("autor"),rs.getString("duracao"),rs.getString("genero"),rs.getString("path"),rs.getString("tipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    public boolean pertenceBibInd(Integer id,String email){
        Boolean res = false;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT pertence FROM bibind WHERE Residente_email=? AND bibGeral_idbibGeral=?");
            stm.setString(1,email );
            stm.setInt(2,id);
            ResultSet rs = stm.executeQuery();
            rs.next();
            res = rs.getBoolean(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return res;
    }

    public void addRelacao(Media value,String email, Boolean b){

        try {
            conn = Connect.connect();
        PreparedStatement st = conn.prepareStatement("INSERT INTO bibind\n" +
                "VALUES (?,?,?,?)\n"+
                "ON DUPLICATE KEY UPDATE Residente_email=VALUES(Residente_email),bibGeral_idbibGeral=VALUES(bibGeral_idbibGeral),genero=VALUES(genero),pertence=VALUES(pertence)", Statement.RETURN_GENERATED_KEYS);
        st.setString(1,email);
        st.setInt(2, value.getId());
        st.setString(3, value.getGenero());
        st.setBoolean(4, b);
        st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }

    }

    public void removeRelacao(int id,String email) {
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("delete from bibind where Residente_email = ? AND bibGeral_idbibGeral=?");
            stm.setString(1,email);
            stm.setInt(2,id);
            stm.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new NullPointerException(ex.getMessage());
            }
           }catch (Exception e) {
            e.printStackTrace();
        }finally {
            Connect.close(conn);
        }
    }
    
    public List<String> toTable(String email){
        List<String> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * from bibGeral");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("idbibGeral");
                PreparedStatement stm2 = conn.prepareStatement("SELECT genero from bibInd where bibGeral_idbibGeral = ? and Residente_email = ?");
                stm2.setInt(1,id);
                stm2.setString(2,email);
                ResultSet rs2 = stm2.executeQuery();

                if(rs2.next()) {
                    res.add(rs.getString("autor")+"\n"+rs.getString("nome")+"\n"+rs2.getString("genero")+"\n"+ (rs.getString("duracao"))+"\n"+(rs.getString("path"))+"\n"+(rs.getInt("idBibGeral"))) ;
                }
                else res.add(rs.getString("autor")+"\n"+rs.getString("nome")+"\n"+rs.getString("genero")+"\n"+ (rs.getString("duracao"))+"\n"+(rs.getString("path"))+"\n"+(rs.getInt("idBibGeral"))) ;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return res;
    }
    
    public List<String> toTableInd(String email){
        List<String> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT bg.autor, bg.nome, bi.genero, bg.duracao, bg.path, bg.idBibGeral FROM bibind bi, bibGeral bg \n" +
                                                          "WHERE bi.Residente_email = ?\n" +
                                                          "AND bi.bibGeral_idbibGeral = bg.idbibGeral\n" +
                                                          "AND bi.pertence = ?;");
            stm.setString(1,email);
            stm.setBoolean(2,true);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                res.add(rs.getString("autor")+"\n"+rs.getString("nome")+"\n"+rs.getString("genero")+"\n"+ (rs.getString("duracao"))+"\n"+(rs.getString("path"))+"\n"+(rs.getInt("idBibGeral"))) ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return res;
    }
    

    public boolean unico(Integer id,String email){
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("select count(*) from bibind where Residente_email <> ?  AND bibGeral_idbibGeral=? AND pertence=?");
            stm.setString(1,email);
            stm.setInt(2,id);
            stm.setBoolean(3,true);
            ResultSet rs =stm.executeQuery();
            rs.next();
            Integer x =rs.getInt(1);
            return x>=1;
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }


    public List<Media> toPlaylist(String email, String genero){
        List<Media> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM bibGeral\n" +
                                                        "WHERE idBibGeral IN(\n" +
                                                        "SELECT bibGeral_idbibGeral FROM bibInd \n" +
                                                        "WHERE Residente_email = ? \n" +
                                                        "AND genero = ?\n" +
                                                        ");");
            stm.setString(1,email);
            stm.setString(2,genero);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                res.add(new Media(rs.getInt("idBibGeral"),rs.getString("nome"),rs.getString("autor"),(rs.getString("duracao")),(rs.getString("genero")),(rs.getString("path")),(rs.getString("tipo")))) ;
            }
            
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return res;
    }
    
    @Override
    public Media put(Integer key, Media value) {
        Media r = null;
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("INSERT INTO bibgeral\n" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE nome=VALUES(nome),  autor=VALUES(autor), duracao=VALUES(duracao),genero=VALUES(genero),path=VALUES(path),tipo=VALUES(tipo)", Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, value.getId());
            stm.setString(2, value.getNome());
            stm.setString(3, value.getAutor());
            stm.setString(4, value.getDuracao());
            stm.setString(5, value.getGenero());
            stm.setString(6, value.getPath());
            stm.setString(7, value.getTipo());
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                int newId = rs.getInt(1);
                value.setId(newId);
            }

            r = value;

            if(value.getTipo().equals("M")) {
                PreparedStatement st = conn.prepareStatement("INSERT INTO musica\n" +
                        "VALUES(?)\n" +
                        "ON DUPLICATE KEY UPDATE bibGeral_idbibGeral=VALUES(bibGeral_idbibGeral)");
                st.setInt(1,value.getId());
                st.executeUpdate();
            }
            else if(value.getTipo().equals("V")) {
                PreparedStatement st = conn.prepareStatement("INSERT INTO video\n" +
                        "VALUES(?)\n" +
                        "ON DUPLICATE KEY UPDATE bibGeral_idbibGeral=VALUES(bibGeral_idbibGeral)");
                st.setInt(1,value.getId());
                st.executeUpdate();
            }
        conn.commit();
        }catch(SQLException e){
            try {    
                conn.rollback();
            } catch (SQLException ex) {
               throw new NullPointerException(ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public Media remove(Object key) {
        Media r = this.get(key);
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("delete from bibgeral where idbibGeral = ?");
            stm.setInt(1, (Integer)key);
            stm.executeUpdate();
            conn.commit();
        } catch(SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new NullPointerException(ex.getMessage());
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Media> m) {
        for(Media r : m.values()) {
            put(r.getId(), r);
        }
    }

    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("DELETE FROM bibgeral");
            stm.executeUpdate();
            conn.commit();
        } catch (SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new NullPointerException(ex.getMessage());
            }
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    @Override
    public Collection<Media> values() {
        Collection<Media> col = new HashSet<Media>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM bibgeral");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                col.add(new Media(rs.getInt("idBibGeral"),rs.getString("nome"),rs.getString("autor"),rs.getString("duracao"),rs.getString("genero"),rs.getString("path"),rs.getString("tipo")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return col;
    }

    @Override
    public Set<Entry<Integer, Media>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<Integer,Media>> entrySet() not implemented!");
    }

}