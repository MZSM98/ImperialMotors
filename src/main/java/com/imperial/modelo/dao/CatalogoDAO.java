
package com.imperial.modelo.dao;

import com.imperial.utilidad.Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CatalogoDAO {
    
    private CatalogoDAO(){
        
    }
    
    public static ResultSet obtenerRoles(Connection conexionBD) throws SQLException{
        
        if (conexionBD!= null){
            String consulta = "SELECT * from rol;";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            return sentencia.executeQuery();
        }
        throw new SQLException(Constantes.ERROR_BD);
    }
}
