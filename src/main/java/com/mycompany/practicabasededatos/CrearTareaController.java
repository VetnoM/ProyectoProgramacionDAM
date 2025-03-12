/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.Tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;

public class CrearTareaController {
    @FXML
    private TextField txtDescripcionTarea;
    @FXML
    private DatePicker dpFechaCreacionTarea;
    @FXML
    private ListView<Tarea> listTareas;

    // Llamado al modelo
    Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        // Cargar lista de tareas
        cargarTareasEnLista();
    }

    // Método para cargar las tareas en la lista
    private void cargarTareasEnLista() {
        ObservableList<Tarea> tareas = FXCollections.observableArrayList(modelo.obtenerTareas());
        listTareas.setItems(tareas);

        listTareas.setCellFactory(param -> new ListCell<Tarea>() {
            @Override
            protected void updateItem(Tarea tarea, boolean empty) {
                super.updateItem(tarea, empty);
                if (empty || tarea == null) {
                    setText(null);
                } else {
                    setText("📌 " + tarea.getDescripcion() + " | Estado: " + tarea.getEstadoTarea());
                }
            }
        });
    }

    // Método para crear una tarea
    @FXML
    private void crearTarea() {
        String descripcion = txtDescripcionTarea.getText();
        Date fechaCreacion = (dpFechaCreacionTarea.getValue() != null)
                ? Date.valueOf(dpFechaCreacionTarea.getValue())
                : Date.valueOf(LocalDate.now());

        if (descripcion.isEmpty()) {
            mostrarAlertaError("Error", "Por favor, completa la descripción de la tarea.");
            return;
        }

        // Crear la tarea y obtener su ID
        int idTarea = modelo.crearTarea(descripcion, fechaCreacion);

        if (idTarea > 0) {
            mostrarAlertaInformacion("Éxito", "Tarea creada correctamente.");
            cargarTareasEnLista(); // Actualizar la lista de tareas
        } else {
            mostrarAlertaError("Error", "No se pudo crear la tarea.");
        }
    }

    // Método para mostrar alerta de información
    public static void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para mostrar alerta de error
    public static void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
