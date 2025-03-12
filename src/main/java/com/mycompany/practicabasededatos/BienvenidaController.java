/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.practicabasededatos;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author eric_
 */
public class BienvenidaController  {

    @FXML
    AnchorPane anchorPaneCentre;
    @FXML
    AnchorPane anchorPanePrincipal;
    
    
      @FXML
    private void cambiarAltaPersonas() throws IOException {
        switchFxml("primary.fxml");
    }

    @FXML
    private void cambiarCrearTarea() throws IOException {
        switchFxml("crearTarea.fxml");
    }
    @FXML
    private void cambiarAsignarTarea() throws IOException {
        switchFxml("asignarTarea.fxml");
    }
    
    @FXML
    private void switchFxml(String nomFxml) throws IOException {
      
        FXMLLoader fxmlPersones = new FXMLLoader(getClass().getResource(nomFxml));
        AnchorPane vistaAcarregar = fxmlPersones.load();

        anchorPaneCentre.getChildren().add(vistaAcarregar);
    }
}
