
package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.Tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


public class AsignarTareaController implements Initializable {
    @FXML
    private ListView<Tarea> listTareas;
    @FXML
    private ComboBox<Empleado> comboEmpleados;

    // Llamado al modelo
    Modelo modelo = new Modelo();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar lista de tareas
        cargarTareasEnLista();

        // Cargar empleados en el ComboBox
        cargarEmpleadosEnCombo();
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

    // Método para cargar los empleados en el ComboBox
    private void cargarEmpleadosEnCombo() {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(modelo.obtenerEmpleados());
        comboEmpleados.setItems(empleados);
    }

    // Método para asignar una tarea a un empleado
    @FXML
    private void asignarTarea() {
        Tarea tareaSeleccionada = listTareas.getSelectionModel().getSelectedItem();
        Empleado empleadoSeleccionado = comboEmpleados.getSelectionModel().getSelectedItem();

        if (tareaSeleccionada == null || empleadoSeleccionado == null) {
            mostrarAlertaError("Error", "Por favor, selecciona una tarea y un empleado.");
            return;
        }

        boolean asignada = modelo.asignarTareaAEmpleado(tareaSeleccionada, empleadoSeleccionado);
        if (asignada) {
            mostrarAlertaInformacion("Éxito", "Tarea asignada correctamente.");
        } else {
            mostrarAlertaError("Error", "No se pudo asignar la tarea.");
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
