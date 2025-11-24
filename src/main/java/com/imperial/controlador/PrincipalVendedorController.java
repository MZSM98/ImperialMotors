package com.imperial.controlador;

import com.imperial.modelo.pojo.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PrincipalVendedorController implements Initializable {

    private Usuario usuarioSesion;
    
    public void setUsuario(Usuario usuario){
        this.usuarioSesion = usuario;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
