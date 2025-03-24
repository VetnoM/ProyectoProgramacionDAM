package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.EstadoHabitacion;
import com.mycompany.practicabasededatos.modelo.Habitacion;
import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.TipoHabitacion;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

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
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnGuardarCambios;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;

    private Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        comboTipoHabitacion.getItems().setAll(TipoHabitacion.values());
        comboEstado.getItems().setAll(EstadoHabitacion.values());
        cargarHabitacionesEnLista();

        // Listener para alternar botones al seleccionar un elemento de la lista
        listHabitaciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarDetallesHabitacion(newValue);
                alternarBotones(false); // Mostrar "Guardar Cambios" y "Eliminar", ocultar "Crear"
            }
        });

        // Listener para alternar botones al escribir en los campos
        agregarListenersDeEscritura();
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
                    // Mostrar "SIN RESERVA" o "RESERVADA" según el estado de la reserva
                    String reservaTexto = (habitacion.getId_reserva() == null || habitacion.getId_reserva() == 0)
                            ? "SIN RESERVA"
                            : "RESERVADA";
                    setText("Habitación " + habitacion.getNumero_habitacion() + " - " + habitacion.getTipo() + " (" + reservaTexto + ")");
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
            limpiarCampos();
        }
    }

    // Método para limpiar los campos y restablecer el formulario
    @FXML
    private void limpiarCamposAction() {
        limpiarCampos(); // Llama al método existente para limpiar los campos
        listHabitaciones.getSelectionModel().clearSelection(); // Deselecciona cualquier elemento del ListView
        alternarBotones(true); // Vuelve a mostrar el botón "Crear"
    }

    // Método para limpiar los campos
    private void limpiarCampos() {
        txtNumeroHabitacion.clear();
        comboTipoHabitacion.setValue(null);
        txtCapacidad.clear();
        comboEstado.setValue(null);
        txtDescripcion.clear();
        txtPrecioNocheAD.clear();
        txtPrecioNocheMP.clear();
    }

    // Método para alternar la visibilidad de los botones
    private void alternarBotones(boolean mostrarCrear) {
        btnCrear.setVisible(mostrarCrear);
        btnGuardarCambios.setVisible(!mostrarCrear);
        btnEliminar.setVisible(!mostrarCrear); // Mostrar "Eliminar" solo cuando no se está creando
    }

    // Agregar listeners para detectar escritura en los campos
    private void agregarListenersDeEscritura() {
        txtNumeroHabitacion.setOnKeyTyped(this::manejarEscritura);
        txtCapacidad.setOnKeyTyped(this::manejarEscritura);
        txtDescripcion.setOnKeyTyped(this::manejarEscritura);
        txtPrecioNocheAD.setOnKeyTyped(this::manejarEscritura);
        txtPrecioNocheMP.setOnKeyTyped(this::manejarEscritura);
    }

    // Manejar escritura en los campos
    private void manejarEscritura(KeyEvent event) {
        if (listHabitaciones.getSelectionModel().getSelectedItem() == null) {
            alternarBotones(true); // Mostrar "Crear", ocultar "Guardar Cambios" y "Eliminar"
        }
    }

    // Método para crear una habitación
    @FXML
    private void crearHabitacion() {
        try {
            String numeroHabitacion = txtNumeroHabitacion.getText();
            TipoHabitacion tipoHabitacion = comboTipoHabitacion.getValue();
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            EstadoHabitacion estado = comboEstado.getValue();
            String descripcion = txtDescripcion.getText();
            double precioNocheAD = Double.parseDouble(txtPrecioNocheAD.getText());
            double precioNocheMP = Double.parseDouble(txtPrecioNocheMP.getText());
    
            // Usa 0 como id_reserva predeterminado para "SIN RESERVA"
            int idReserva = 0;
    
            Habitacion habitacion = new Habitacion(0, numeroHabitacion, idReserva, tipoHabitacion, capacidad, estado, descripcion, precioNocheAD, precioNocheMP);
            int idHabitacion = modelo.crearHabitacion(habitacion);
    
            if (idHabitacion > 0) {
                mostrarAlertaInformacion("Éxito", "Habitación creada correctamente.");
                cargarHabitacionesEnLista(); // Actualizar la lista de habitaciones
                limpiarCampos();
                alternarBotones(false); // Volver a estado inicial
            } else {
                mostrarAlertaError("Error", "No se pudo crear la habitación.");
            }
        } catch (NumberFormatException e) {
            mostrarAlertaError("Error", "Por favor, ingresa valores válidos en los campos.");
        }
    }

    // Método para guardar cambios en una habitación
    @FXML
    private void guardarCambios() {
        Habitacion habitacionSeleccionada = listHabitaciones.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada == null) {
            mostrarAlertaError("Error", "Por favor, selecciona una habitación.");
            return;
        }
    
        try {
            String numeroHabitacion = txtNumeroHabitacion.getText();
            TipoHabitacion tipoHabitacion = comboTipoHabitacion.getValue();
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            EstadoHabitacion estado = comboEstado.getValue();
            String descripcion = txtDescripcion.getText();
            double precioNocheAD = Double.parseDouble(txtPrecioNocheAD.getText());
            double precioNocheMP = Double.parseDouble(txtPrecioNocheMP.getText());
    
            // Usa 0 si id_reserva es null
            int idReserva = habitacionSeleccionada.getId_reserva() != null ? habitacionSeleccionada.getId_reserva() : 0;
    
            Habitacion habitacionActualizada = new Habitacion(
                habitacionSeleccionada.getId_habitacion(),
                numeroHabitacion,
                idReserva,
                tipoHabitacion,
                capacidad,
                estado,
                descripcion,
                precioNocheAD,
                precioNocheMP
            );
    
            boolean actualizada = modelo.actualizarHabitacion(habitacionActualizada);
    
            if (actualizada) {
                mostrarAlertaInformacion("Éxito", "Habitación actualizada correctamente.");
                cargarHabitacionesEnLista(); // Actualiza el ListView
                limpiarCampos(); // Limpia los campos después de guardar
                alternarBotones(true); // Vuelve a mostrar el botón "Crear"
            } else {
                mostrarAlertaError("Error", "No se pudo actualizar la habitación.");
            }
        } catch (NumberFormatException e) {
            mostrarAlertaError("Error", "Por favor, ingresa valores válidos en los campos.");
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

        // Mostrar mensaje de confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar esta habitación?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            boolean eliminada = modelo.eliminarHabitacion(habitacionSeleccionada.getId_habitacion());

            if (eliminada) {
                mostrarAlertaInformacion("Éxito", "Habitación eliminada correctamente.");
                cargarHabitacionesEnLista(); // Actualizar la lista de habitaciones
                limpiarCampos();
                alternarBotones(false); // Volver a estado inicial
            } else {
                mostrarAlertaError("Error", "No se pudo eliminar la habitación.");
            }
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
