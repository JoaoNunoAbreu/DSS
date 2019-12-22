package data;

import business.Residente;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResidenteDAO implements Map<String, Residente> {

    private Connection conn;

    @Override
    public int size() {

        int res=0;

        try{
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT count(*) FROM residente");
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
            PreparedStatement stm = conn.prepareStatement("SELECT nome FROM residente WHERE email=?");
            stm.setString(1,(String)key);                                  
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
        Residente r = (Residente) value;
        return containsKey(r.getEmail());
    }

    @Override
    public Residente get(Object key) {
        Residente r = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM residente WHERE email=?");
            stm.setString(1, (String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                r = new Residente(rs.getString("email"),rs.getString("password"),rs.getString("nome"),rs.getString("tipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public Residente put(String key, Residente value) {
        Residente r = null;
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            PreparedStatement stm = conn.prepareStatement("INSERT INTO residente\n" +
                    "VALUES(?,?,?,?)\n" +
                    "ON DUPLICATE KEY UPDATE email=VALUES(email),password=VALUES(password),nome=VALUES(nome),tipo=VALUES(tipo)");
            stm.setString(1,value.getEmail());
            stm.setString(2, value.getPassword());
            stm.setString(3, value.getNome());
            stm.setString(4,value.getTipo());
            stm.executeUpdate();

            r = value;

            if(value.getTipo().equals("A")) {
                PreparedStatement st = conn.prepareStatement("INSERT INTO admin\n" +
                        "VALUES(?)\n" +
                        "ON DUPLICATE KEY UPDATE Residente_email=VALUES(Residente_email)");
                st.setString(1,value.getEmail());
                st.executeUpdate();
            }
            else if(value.getTipo().equals("U")) {
                PreparedStatement st = conn.prepareStatement("INSERT INTO utilizador\n" +
                        "VALUES(?)\n" +
                        "ON DUPLICATE KEY UPDATE Residente_email=VALUES(Residente_email)");
                st.setString(1,value.getEmail());
                st.executeUpdate();
            }
        conn.commit();
        } catch(SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ResidenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public Residente remove(Object key) {
        Residente r = this.get(key);
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            conn.commit();
            PreparedStatement stm = conn.prepareStatement("delete from residente where email = ?");
            stm.setString(1, (String)key);
            stm.executeUpdate();
        }catch (SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ResidenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Residente> m) {
        for(Residente r : m.values()) {
            put(r.getEmail(), r);
        }
    }


    @Override
    public void clear() {
        try {
            conn = Connect.connect();
            conn.setAutoCommit(false);
            conn.commit();
            PreparedStatement stm = conn.prepareStatement("DELETE FROM residente");
            stm.executeUpdate();
        } catch(SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ResidenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
    }

    @Override
    public Set<String> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    @Override
    public Collection<Residente> values() {
        Collection<Residente> col = new HashSet<Residente>();
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM residente");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                col.add(new Residente(rs.getString("email"),rs.getString("password"),rs.getString("nome"),rs.getString("tipo")));
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
    public Set<Entry<String, Residente>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Residente>> entrySet() not implemented!");
    }
}