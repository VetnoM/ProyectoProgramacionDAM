package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.EstadoHabitacion;
import com.mycompany.practicabasededatos.modelo.Habitacion;
import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.TipoHabitacion;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HabitacionController {
    @FXML
    private TextField txtNumeroHabitacion;
    @FXML
    private ComboBox<TipoHabitacion> comboTipoHabitacion;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private ComboBox<EstadoHabitacion> comboEstado;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtPrecioNocheAD;
    @FXML
    private TextField txtPrecioNocheMP;
    @FXML
    private ListView<Habitacion> listHabitaciones;

    private Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        comboTipoHabitacion.getItems().setAll(TipoHabitacion.values());
        comboEstado.getItems().setAll(EstadoHabitacion.values());
        cargarHabitacionesEnLista();
        listHabitaciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesHabitacion(newValue));
    }

    // Método para cargar las habitaciones en la lista
    private void cargarHabitacionesEnLista() {
        ObservableList<Habitacion> habitaciones = modelo.obtenerHabitaciones();
        listHabitaciones.setItems(habitaciones);

        listHabitaciones.setCellFactory(param -> new ListCell<Habitacion>() {
            @Override
            protected void updateItem(Habitacion habitacion, boolean empty) {
                super.updateItem(habitacion, empty);
                if (empty || habitacion == null) {
                    setText(null);
                } else {
                    setText("Habitación " + habitacion.getNumero_habitacion() + " - " + habitacion.getTipo());
                }
            }
        });
    }

    // Método para mostrar los detalles de la habitación seleccionada
    private void mostrarDetallesHabitacion(Habitacion habitacion) {
        if (habitacion != null) {
            txtNumeroHabitacion.setText(habitacion.getNumero_habitacion());
            comboTipoHabitacion.setValue(habitacion.getTipo());
            txtCapacidad.setText(String.valueOf(habitacion.getCapacidad()));
            comboEstado.setValue(habitacion.getEstado());
            txtDescripcion.setText(habitacion.getDescripcion());
            txtPrecioNocheAD.setText(String.valueOf(habitacion.getPrecio_noche_ad()));
            txtPrecioNocheMP.setText(String.valueOf(habitacion.getPrecio_noche_mp()));
        } else {
            txtNumeroHabitacion.clear();
            comboTipoHabitacion.setValue(null);
            txtCapacidad.clear();
            comboEstado.setValue(null);
            txtDescripcion.clear();
            txtPrecioNocheAD.clear();
            txtPrecioNocheMP.clear();
        }
    }

    // Método para crear una habitación
    @FXML
    private void crearHabitacion() {
        String numeroHabitacion = txtNumeroHabitacion.getText();
        TipoHabitacion tipoHabitacion = comboTipoHabitacion.getValue();
        int capacidad = Integer.parseInt(txtCapacidad.getText());
        EstadoHabitacion estado = comboEstado.getValue();
        String descripcion = txtDescripcion.getText();
        double precioNocheAD = Double.parseDouble(txtPrecioNocheAD.getText());
        double precioNocheMP = Double.parseDouble(txtPrecioNocheMP.getText());

        Habitacion habitacion = new Habitacion(0, numeroHabitacion, 0, tipoHabitacion, capacidad, estado, descripcion, precioNocheAD, precioNocheMP);
        int idHabitacion = modelo.crearHabitacion(habitacion);

        if (idHabitacion > 0) {
            mostrarAlertaInformacion("Éxito", "Habitación creada correctamente.");
            cargarHabitacionesEnLista(); // Actualizar la lista de habitaciones
        } else {
            mostrarAlertaError("Error", "No se pudo crear la habitación.");
        }
    }

    // Método para actualizar una habitación
    @FXML
    private void actualizarHabitacion() {
        Habitacion habitacionSeleccionada = listHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada == null) {
            mostrarAlertaError("Error", "Por favor, selecciona una habitación.");
            return;
        }

        String numeroHabitacion = txtNumeroHabitacion.getText();
        TipoHabitacion tipoHabitacion = comboTipoHabitacion.getValue();
        int capacidad = Integer.parseInt(txtCapacidad.getText());
        EstadoHabitacion estado = comboEstado.getValue();
        String descripcion = txtDescripcion.getText();
        double precioNocheAD = Double.parseDouble(txtPrecioNocheAD.getText());
        double precioNocheMP = Double.parseDouble(txtPrecioNocheMP.getText());

        Habitacion habitacionActualizada = new Habitacion(habitacionSeleccionada.getId_habitacion(), numeroHabitacion, habitacionSeleccionada.getId_reserva(), tipoHabitacion, capacidad, estado, descripcion, precioNocheAD, precioNocheMP);
        boolean actualizada = modelo.actualizarHabitacion(habitacionActualizada);

        if (actualizada) {
            mostrarAlertaInformacion("Éxito", "Habitación actualizada correctamente.");
            cargarHabitacionesEnLista(); // Actualizar la lista de habitaciones
        } else {
            mostrarAlertaError("Error", "No se pudo actualizar la habitación.");
        }
    }

    // Método para eliminar una habitación
    @FXML
    private void eliminarHabitacion() {
        Habitacion habitacionSeleccionada = listHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada == null) {
            mostrarAlertaError("Error", "Por favor, selecciona una habitación.");
            return;
        }

        boolean eliminada = modelo.eliminarHabitacion(habitacionSeleccionada.getId_habitacion());

        if (eliminada) {
            mostrarAlertaInformacion("Éxito", "Habitación eliminada correctamente.");
            cargarHabitacionesEnLista(); // Actualizar la lista de habitaciones
        } else {
            mostrarAlertaError("Error", "No se pudo eliminar la habitación.");
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
