package com.imperial.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {
    
    private ConexionBD(){
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada...");
    }
    
    private static final String NAME_BD = "imperialmotors";
    private static final String IP = "localhost";
    private static final String PORT  = "3306";
    private static final String URL = "jdbc:mysql://"+ IP + ":" + PORT + "/" + NAME_BD
            + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Mexico_City";
    private static final String USER = "admnistrador";
    private static final String PASSWORD = "<4DM1N1STR4D0R>";
    private static Connection conexion = null; //
    
    public static Connection abrirConexion(){

        try{
            
            conexion = DriverManager.getConnection(URL, USER, PASSWORD); //
            
        }catch (SQLException sqle){ //
            sqle.printStackTrace();
        }
        
        return conexion; //
        
    }
    
    public static void cerrarConexionBD(){
        try{
            if(conexion!=null){
                conexion.close();
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
    
}