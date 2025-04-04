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
    
    // cambiar a personas
      @FXML
    private void cambiarAltaPersonas() throws IOException {
        switchFxml("primary.fxml");
    }

    //cambiar a modificar datos de personas
    @FXML
    private void cambiarModificarPersonas() throws IOException {
        switchFxml("modificarUsuario.fxml");
    }
    
    
// tareas
    @FXML
    private void cambiarCrearTarea() throws IOException {
        switchFxml("crearTarea.fxml");
    }
    @FXML
    private void cambiarAsignarTarea() throws IOException {
        switchFxml("asignarTarea.fxml");
    }
    
    // habitacion
    
      @FXML
    private void cambiarHabitacion() throws IOException {
        switchFxml("habitacion.fxml");
    }

    //cambiar a reserva
    @FXML
    private void cambiarReservas() throws IOException {
        switchFxml("reserva.fxml");
    }
    
    @FXML
    private void switchFxml(String nomFxml) throws IOException {
        anchorPaneCentre.getChildren().clear();
        FXMLLoader fxmlPersones = new FXMLLoader(getClass().getResource(nomFxml));
        AnchorPane vistaAcarregar = fxmlPersones.load();

        anchorPaneCentre.getChildren().add(vistaAcarregar);
    }
    
    
}
