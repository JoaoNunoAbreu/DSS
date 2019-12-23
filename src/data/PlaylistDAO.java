package data;

import business.playlist.Playlist;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import business.media.Media;

public class PlaylistDAO implements Map<Integer, Playlist> {

    private Connection conn;


     public int size() {

        int res=0;

        try{
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT count(*) FROM playlist"); 
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

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Object key) {
        boolean r = false;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT idPlaylist FROM playlist WHERE idPlaylist=?");
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
    
    public void addRelacao(int idMedia,int idPlaylist){

        try {
            conn = Connect.connect();
        PreparedStatement st = conn.prepareStatement("INSERT INTO bibGeral_has_Playlist\n" +
                "VALUES (?,?)\n"+
                "ON DUPLICATE KEY UPDATE bibGeral_idbibGeral=VALUES(bibGeral_idbibGeral),Playlist_idPlaylist=VALUES(Playlist_idPlaylist)", Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, idMedia);
        st.setInt(2, idPlaylist);
        st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
    }
    
    

    @Override
    public boolean containsValue(Object value) {
        Playlist p = (Playlist) value;
        return containsKey(p.getId());
    }

    @Override
    public Playlist get(Object key) {
        Playlist p = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM playlist WHERE idPlaylist=?");
            stm.setInt(1, (Integer)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                p = new Playlist( rs.getInt("id"),rs.getString("nome"),rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return p;
    }
    
    public List<String> idPlaylistToMedias(int idPlaylist, String email){
        List<String> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT idbibGeral, nome, autor, duracao, genero, path, tipo FROM mediacenter.bibGeral_has_Playlist, mediacenter.bibGeral\n" +
                                                        "WHERE Playlist_idPlaylist = ?\n" +
                                                        "AND bibGeral_idbibGeral = idbibGeral;");
            stm.setInt(1,idPlaylist);
            ResultSet rs = stm.executeQuery();
            /*while (rs.next()) {
                res.add(new Media(rs.getInt("idBibGeral"),rs.getString("nome"),rs.getString("autor"),(rs.getString("duracao")),(rs.getString("genero")),(rs.getString("path")),(rs.getString("tipo"))).toStringEnters());
            }*/
            
            while (rs.next()) {
                int id = rs.getInt("idbibGeral");
                PreparedStatement stm2 = conn.prepareStatement("SELECT genero from bibInd where bibGeral_idbibGeral = ? and Residente_email = ?");
                stm2.setInt(1,id);
                stm2.setString(2,email);
                ResultSet rs2 = stm2.executeQuery();

                if(rs2.next()) {
                    res.add(new Media(rs.getInt("idBibGeral"),rs.getString("nome"),rs.getString("autor"),(rs.getString("duracao")),(rs2.getString("genero")),(rs.getString("path")),(rs.getString("tipo"))).toStringEnters()) ;
                }
                else res.add(new Media(rs.getInt("idBibGeral"),rs.getString("nome"),rs.getString("autor"),(rs.getString("duracao")),(rs.getString("genero")),(rs.getString("path")),(rs.getString("tipo"))).toStringEnters()) ;
            }
            
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return res;
    }

    @Override
    public Playlist put(Integer key, Playlist value) {
        Playlist p = null;
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("INSERT INTO playlist\n" +
                    "VALUES(?,?,?)\n" +
                    "ON DUPLICATE KEY UPDATE nome=VALUES(nome),Residente_email=VALUES(Residente_email)", Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1,value.getId());
            stm.setString(2, value.getNome());
            stm.setString(3,value.getEmail());
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                int newID = rs.getInt(1);
                value.setId(newID);
            }
            p = value;
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
        return p;
    }

    @Override
    public Playlist remove(Object key) {
        Playlist p = this.get(key);
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("delete from playlist where id = ?");
            stm.setInt(1, (Integer)key);
            stm.executeUpdate();
            conn.commit();
        } catch (SQLException e){
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
        return p;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Playlist> m) {
        for(Playlist p : m.values()) {
            put(p.getId(), p);
        }
    }

    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("DELETE FROM playlist");
            stm.executeUpdate();
            conn.commit();
        }catch(SQLException e){
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
    }

    @Override
    public Set<Integer> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    @Override
    public Collection<Playlist> values() {
        Collection<Playlist> col = new HashSet<Playlist>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM playlist");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                col.add(new Playlist(rs.getInt("idPlaylist"),rs.getString("nome"),rs.getString("Residente_email")));
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
    public Set<Entry<Integer, Playlist>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Playlist>> entrySet() not implemented!");
    }
}