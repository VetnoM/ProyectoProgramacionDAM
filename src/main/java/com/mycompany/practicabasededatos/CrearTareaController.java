package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class CrearTareaController {

    @FXML
    private TextField txtDescripcionTarea;

    @FXML
    private DatePicker dpFechaCreacionTarea;

    @FXML
    private DatePicker dpFechaEjecucionTarea;

    @FXML
    private TableView<Tarea> tableTareas;

    @FXML
    private TableColumn<Tarea, String> colDescripcion;

    @FXML
    private TableColumn<Tarea, String> colEstado;

    @FXML
    private TableColumn<Tarea, String> colFechaCreacion;

    Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        // Configurar las columnas del TableView
        colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDescripcion()));

        colEstado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getEstadoTarea().toString()));

        colFechaCreacion.setCellValueFactory(cellData -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String fechaStr = cellData.getValue().getFecha_creacion() != null
                    ? formatter.format(cellData.getValue().getFecha_creacion())
                    : "";
            return new javafx.beans.property.SimpleStringProperty(fechaStr);
        });

        cargarTareasEnTabla();
    }

    private void cargarTareasEnTabla() {
        try {
            ObservableList<Tarea> tareas = FXCollections.observableArrayList(modelo.obtenerTareas());
            tableTareas.setItems(tareas);
        } catch (Exception e) {
            mostrarAlertaError("Error", "No se pudieron cargar las tareas.");
            e.printStackTrace();
        }
    }

    @FXML
    private void crearTarea() {
        String descripcion = txtDescripcionTarea.getText();
        Date fechaCreacion = (dpFechaCreacionTarea.getValue() != null)
                ? Date.valueOf(dpFechaCreacionTarea.getValue())
                : Date.valueOf(LocalDate.now());
                Date fechaEjecucion = (dpFechaEjecucionTarea.getValue() != null)
        ? Date.valueOf(dpFechaEjecucionTarea.getValue())
        : null;


        if (descripcion.isEmpty()) {
            mostrarAlertaError("Error", "Por favor, completa la descripción de la tarea.");
            return;
        }

        try {
int idTarea = modelo.crearTarea(descripcion, fechaCreacion, fechaEjecucion);
            if (idTarea > 0) {
                mostrarAlertaInformacion("Éxito", "Tarea creada correctamente.");
                cargarTareasEnTabla();
            } else {
                mostrarAlertaError("Error", "No se pudo crear la tarea.");
            }
        } catch (Exception e) {
            mostrarAlertaError("Error", "Error al crear la tarea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
