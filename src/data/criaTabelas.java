package data;

import java.sql.Connection;
import java.sql.Statement;

public class criaTabelas {


    private Connection conn;
    
    public void createSchema(){
        Connect.createSchema();
    }

    public void criartabelaResidente(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Residente` (\n" +
                        "  `email` VARCHAR(45) NOT NULL,\n" +
                        "  `password` VARCHAR(45) NOT NULL,\n" +
                        "  `nome` VARCHAR(45) NOT NULL,\n" +
                        "  `tipo` VARCHAR(45) NOT NULL,\n" +
                        "  PRIMARY KEY (`email`),\n" +
                        "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)";

            stmt.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
    public void criartabelaAdmin(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Admin` (\n" +
                        "  `Residente_email` VARCHAR(45) NOT NULL,\n" +
                        "  PRIMARY KEY (`Residente_email`),\n" +
                        "  INDEX `fk_Admin_Residente1_idx` (`Residente_email` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_Admin_Residente1`\n" +
                        "    FOREIGN KEY (`Residente_email`)\n" +
                        "    REFERENCES `mediacenter`.`Residente` (`email`)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE CASCADE)";

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
   
    public void criartabelaUtilizador(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Utilizador` (\n" +
                        "  `Residente_email` VARCHAR(45) NOT NULL,\n" +
                        "  PRIMARY KEY (`Residente_email`),\n" +
                        "  INDEX `fk_Utilizador_Residente1_idx` (`Residente_email` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_Utilizador_Residente1`\n" +
                        "    FOREIGN KEY (`Residente_email`)\n" +
                        "    REFERENCES `mediacenter`.`Residente` (`email`)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE CASCADE)";

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
    
    public void criartabelaMusica(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Musica` (\n" +
                        "  `bibGeral_idbibGeral` INT NOT NULL,\n" +
                        "  PRIMARY KEY (`bibGeral_idbibGeral`),\n" +
                        "  INDEX `fk_Musica_bibGeral1_idx` (`bibGeral_idbibGeral` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_Musica_bibGeral1`\n" +
                        "    FOREIGN KEY (`bibGeral_idbibGeral`)\n" +
                        "    REFERENCES `mediacenter`.`bibGeral` (`idbibGeral`)\n" +
                        "    ON DELETE NO ACTION\n" +
                        "    ON UPDATE NO ACTION)";

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }

    public void criartabelaVideo(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Video` (\n" +
                        "  `bibGeral_idbibGeral` INT NOT NULL,\n" +
                        "  PRIMARY KEY (`bibGeral_idbibGeral`),\n" +
                        "  INDEX `fk_Video_bibGeral1_idx` (`bibGeral_idbibGeral` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_Video_bibGeral1`\n" +
                        "    FOREIGN KEY (`bibGeral_idbibGeral`)\n" +
                        "    REFERENCES `mediacenter`.`bibGeral` (`idbibGeral`)\n" +
                        "    ON DELETE NO ACTION\n" +
                        "    ON UPDATE NO ACTION)";

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }


    public void criartabelaBibGeral(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`bibGeral` (\n" +
                        "  `idbibGeral` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `nome` VARCHAR(45) NULL,\n" +
                        "  `autor` VARCHAR(45) NULL,\n" +
                        "  `duracao` VARCHAR(45) NULL,\n" +
                        "  `genero` VARCHAR(45) NULL,\n" +
                        "  `path` TEXT NULL,\n" +
                        "  `tipo` VARCHAR(45) NULL,\n" +
                        "  PRIMARY KEY (`idbibGeral`),\n" +
                        "  UNIQUE INDEX `idbibGeral_UNIQUE` (`idbibGeral` ASC) VISIBLE)";

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }

    public void criartabelaBibInd(){
        try{
            conn = Connect.connect();

            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`bibInd` (\n" +
                         "  `Residente_email` VARCHAR(45) NOT NULL,\n" +
                         "  `bibGeral_idbibGeral` INT NOT NULL,\n" +
                         "  `genero` VARCHAR(45) NULL,\n" +
                         "  `pertence` TINYINT NULL,\n" +
                         "  PRIMARY KEY (`Residente_email`, `bibGeral_idbibGeral`),\n" +
                         "  INDEX `fk_Residente_has_bibGeral_bibGeral1_idx` (`bibGeral_idbibGeral` ASC) VISIBLE,\n" +
                         "  INDEX `fk_Residente_has_bibGeral_Residente1_idx` (`Residente_email` ASC) VISIBLE,\n" +
                         "  CONSTRAINT `fk_Residente_has_bibGeral_Residente1`\n" +
                         "    FOREIGN KEY (`Residente_email`)\n" +
                         "    REFERENCES `mediacenter`.`Residente` (`email`)\n" +
                         "    ON DELETE CASCADE\n" +
                         "    ON UPDATE CASCADE,\n" +
                         "  CONSTRAINT `fk_Residente_has_bibGeral_bibGeral1`\n" +
                         "    FOREIGN KEY (`bibGeral_idbibGeral`)\n" +
                         "    REFERENCES `mediacenter`.`bibGeral` (`idbibGeral`)\n" +
                         "    ON DELETE NO ACTION\n" +
                         "    ON UPDATE NO ACTION)"
                    ;

            stmt.executeUpdate(sql);

        }catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
    
    
    public void criartabelaPlaylist(){
        try{
            conn = Connect.connect();
            
            Statement stmt = conn.createStatement();
            
            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`Playlist` (\n" +
                        "  `idPlaylist` INT NOT NULL AUTO_INCREMENT,\n" +
                        "  `nome` VARCHAR(45) NULL,\n" +
                        "  `Residente_email` VARCHAR(45) NOT NULL,\n" +
                        "  PRIMARY KEY (`idPlaylist`, `Residente_email`),\n" +
                        "  UNIQUE INDEX `idPlaylist_UNIQUE` (`idPlaylist` ASC) VISIBLE,\n" +
                        "  INDEX `fk_Playlist_Residente_idx` (`Residente_email` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_Playlist_Residente`\n" +
                        "    FOREIGN KEY (`Residente_email`)\n" +
                        "    REFERENCES `mediacenter`.`Residente` (`email`)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE CASCADE)";
            
            stmt.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
    
    public void criartabelabibGeral_has_Playlist(){
        try{
            conn = Connect.connect();
            
            Statement stmt = conn.createStatement();
            
            String sql = "CREATE TABLE IF NOT EXISTS `mediacenter`.`bibGeral_has_Playlist` (\n" +
                        "  `bibGeral_idbibGeral` INT NOT NULL,\n" +
                        "  `Playlist_idPlaylist` INT NOT NULL,\n" +
                        "  `Playlist_Residente_email` VARCHAR(45) NOT NULL,\n" +
                        "  PRIMARY KEY (`bibGeral_idbibGeral`, `Playlist_idPlaylist`, `Playlist_Residente_email`),\n" +
                        "  INDEX `fk_bibGeral_has_Playlist_Playlist1_idx` (`Playlist_idPlaylist` ASC, `Playlist_Residente_email` ASC) VISIBLE,\n" +
                        "  CONSTRAINT `fk_bibGeral_has_Playlist_bibGeral1`\n" +
                        "    FOREIGN KEY (`bibGeral_idbibGeral`)\n" +
                        "    REFERENCES `mediacenter`.`bibGeral` (`idbibGeral`)\n" +
                        "    ON DELETE NO ACTION\n" +
                        "    ON UPDATE NO ACTION,\n" +
                        "  CONSTRAINT `fk_bibGeral_has_Playlist_Playlist1`\n" +
                        "    FOREIGN KEY (`Playlist_idPlaylist` , `Playlist_Residente_email`)\n" +
                        "    REFERENCES `mediacenter`.`Playlist` (`idPlaylist` , `Residente_email`)\n" +
                        "    ON DELETE NO ACTION\n" +
                        "    ON UPDATE NO ACTION)";
            
            stmt.executeUpdate(sql);
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
    }
}
