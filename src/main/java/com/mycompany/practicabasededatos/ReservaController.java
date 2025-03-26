package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Reserva;
import com.mycompany.practicabasededatos.modelo.TipoReserva;
import com.mycompany.practicabasededatos.modelo.Modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;
// import java.time.LocalDate;
// import com.mycompany.practicabasededatos.FacturaController;
public class ReservaController {
    @FXML
    private DatePicker dateFechaReserva;
    @FXML
    private DatePicker dateFechaInicio;
    @FXML
    private DatePicker dateFechaFin;
    @FXML
    private ComboBox<String> comboTipoReserva;
    @FXML
    private TextField txtTipoIVA;
    @FXML
    private TextField txtPrecioTotalReserva;
    @FXML
    private ListView<Reserva> listReservas;
    @FXML
    private ListView<String> listHabitacionesSinReserva;
    @FXML
    private ListView<String> listPersonas;
    @FXML
    private Button btnCrearReserva;
    @FXML
    private Button btnGuardarCambios;
    @FXML
    private Button btnEliminarReserva;

    private Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        comboTipoReserva.getItems().addAll("AD", "MP");
        cargarReservasEnLista();
        cargarHabitacionesSinReserva();
        cargarPersonas();

        // Hacer que los campos txtTipoIVA y txtPrecioTotalReserva no sean editables
        txtTipoIVA.setEditable(false);
        txtPrecioTotalReserva.setEditable(false);

        // Listener para asignar IVA según la persona seleccionada
        listPersonas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.contains("(Cliente)")) {
                    txtTipoIVA.setText("21"); // IVA para clientes
                } else if (newValue.contains("(Empleado)")) {
                    txtTipoIVA.setText("7"); // IVA para empleados
                }
            }
        });


    }
    @FXML
    private void crearReserva() {
        try {
            // ✅ 1. Validar que se haya seleccionado una habitación
            String habitacionSeleccionada = listHabitacionesSinReserva.getSelectionModel().getSelectedItem();
            if (habitacionSeleccionada == null) {
                mostrarAlertaError("Error", "Por favor, selecciona una habitación.");
                return;
            }
    
            // ✅ 2. Extraer el número de la habitación (suponiendo que el formato es "101 - Habitación Doble")
            String numeroHabitacion = habitacionSeleccionada.split(" ")[0]; // Extrae solo el número
    
            // ✅ 3. Obtener el ID real de la habitación desde la base de datos
            int idHabitacion = modelo.obtenerIdHabitacionPorNumero(numeroHabitacion);
            if (idHabitacion == -1) {
                mostrarAlertaError("Error", "No se encontró la habitación en la base de datos.");
                return;
            }
    
            // ✅ 4. Validar que se haya seleccionado una persona
            String personaSeleccionada = listPersonas.getSelectionModel().getSelectedItem();
            if (personaSeleccionada == null) {
                mostrarAlertaError("Error", "Por favor, selecciona una persona.");
                return;
            }
    
            // ✅ 5. Extraer el documento de identidad del cliente
            String documentoIdentidad = personaSeleccionada.split(" - ")[0];
            if (documentoIdentidad.isEmpty()) {
                mostrarAlertaError("Error", "No se pudo obtener el documento de identidad del cliente.");
                return;
            }
    
            // ✅ 6. Obtener el ID del cliente
            int idCliente = modelo.obtenerIdClientePorDocumento(documentoIdentidad);
            if (idCliente == -1) {
                mostrarAlertaError("Error", "No se encontró el cliente en la base de datos.");
                return;
            }
    
            // ✅ 7. Validar que se haya seleccionado un tipo de reserva
            String tipoReserva = comboTipoReserva.getValue();
            if (tipoReserva == null || tipoReserva.isEmpty()) {
                mostrarAlertaError("Error", "Por favor, selecciona un tipo de reserva.");
                return;
            }
    
            // ✅ 8. Validar fechas
            if (dateFechaInicio.getValue() == null || dateFechaFin.getValue() == null || dateFechaReserva.getValue() == null) {
                mostrarAlertaError("Error", "Por favor, selecciona todas las fechas.");
                return;
            }
    
            if (dateFechaInicio.getValue().isAfter(dateFechaFin.getValue())) {
                mostrarAlertaError("Error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
                return;
            }
    
            // ✅ 9. Calcular precio total con IVA
            double iva = Double.parseDouble(txtTipoIVA.getText());
            double precioBase = calcularPrecioBase(dateFechaInicio.getValue(), dateFechaFin.getValue(), tipoReserva);
            double precioTotal = precioBase + (precioBase * (iva / 100));
    
            // ✅ 10. Crear objeto Reserva con el ID real de la habitación
            Reserva nuevaReserva = new Reserva(
                0, // ID generado automáticamente en la base de datos
                TipoReserva.valueOf(tipoReserva),
                dateFechaInicio.getValue(),
                dateFechaFin.getValue(),
                dateFechaReserva.getValue(),
                precioTotal,
                idCliente,
                idHabitacion,  // 🔴 Ahora usamos el ID real obtenido de la BD
                "Pendiente de facturar",
                habitacionSeleccionada
            );
    
            // ✅ 11. Registrar la reserva en la base de datos
            boolean exito = modelo.registrarReserva(nuevaReserva);
            if (exito) {
                // ✅ 12. Actualizar estado de la habitación a "Ocupada"
                modelo.actualizarEstadoHabitacion(idHabitacion, "Ocupada");
                   // ✅ 13. Eliminar la persona del ListView de personas
            eliminarPersonaDelListView(personaSeleccionada);
    
                mostrarAlertaInformacion("Éxito", "Reserva creada correctamente.");
                cargarReservasEnLista(); // Actualizar lista de reservas
                cargarHabitacionesSinReserva(); // Actualizar lista de habitaciones disponibles
            } else {
                mostrarAlertaError("Error", "No se pudo crear la reserva.");
            }
    
        } catch (Exception e) {
            mostrarAlertaError("Error", "Ocurrió un error al crear la reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para eliminar una persona del ListView
private void eliminarPersonaDelListView(String persona) {
    ObservableList<String> personas = listPersonas.getItems();
    personas.removeIf(p -> p.equals(persona)); // Eliminar la persona seleccionada
    listPersonas.setItems(personas); // Actualizar el ListView
}
    // Método para calcular el precio base de la reserva
    
private double calcularPrecioBase(LocalDate fechaInicio, LocalDate fechaFin, String tipoReserva) {
    long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    double precioPorDia;

    switch (tipoReserva) {
        case "AD": // Alojamiento y desayuno
            precioPorDia = 50.0;
            break;
        case "MP": // Media pensión
            precioPorDia = 75.0;
            break;
        default:
            precioPorDia = 0.0;
            break;
    }

    return dias * precioPorDia;
}

    // Método para cargar las reservas en la lista
    private void cargarReservasEnLista() {
        ObservableList<Reserva> reservas = modelo.obtenerReservas();
        listReservas.setItems(reservas);
    
        listReservas.setCellFactory(param -> new ListCell<Reserva>() {
            @Override
            protected void updateItem(Reserva reserva, boolean empty) {
                super.updateItem(reserva, empty);
                if (empty || reserva == null) {
                    setText(null);
                } else {
                    // Mostrar información descriptiva de la reserva, incluyendo el estado
                    String numeroHabitacion = reserva.getNumeroHabitacion() != null ? reserva.getNumeroHabitacion() : "Sin asignar";
                    setText("Habitación: " + numeroHabitacion + " | Tipo: " + reserva.getTipo_reserva() +
                            " | Fechas: " + reserva.getFecha_inicio() + " a " + reserva.getFecha_fin() +
                            " | Estado: " + reserva.getEstado());
                }
            }
        });
    }
    // Método para cargar las habitaciones sin reserva
    private void cargarHabitacionesSinReserva() {
        ObservableList<String> habitaciones = modelo.obtenerHabitacionesSinReserva();
        listHabitacionesSinReserva.setItems(habitaciones);
    }

  // Método para cargar las personas (clientes y empleados) en la lista
private void cargarPersonas() {
    ObservableList<String> personas = modelo.obtenerPersonasSinReserva();
    // Ajustar el formato para mostrar primero el documento de identidad y luego el nombre
    ObservableList<String> personasFormateadas = personas.stream()
        .map(persona -> {
            String[] partes = persona.split(" "); // Suponiendo que el formato es "ID Nombre"
            if (partes.length > 1) {
                return partes[0] + " - " + String.join(" ", Arrays.copyOfRange(partes, 1, partes.length));
            }
            return persona; // Si no tiene formato esperado, devolver como está
        })
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
    listPersonas.setItems(personasFormateadas);
}



    @FXML
    private void abrirVentanaFacturacion() {
        try {
            // Cargar el archivo FXML de la ventana de facturación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("factura.fxml"));
            Parent root = loader.load();
    
            // Obtener el controlador de la ventana de facturación
            FacturaController facturaController = loader.getController();
    
            // Pasar la reserva seleccionada al FacturaController
            Reserva reservaSeleccionada = listReservas.getSelectionModel().getSelectedItem();
            if (reservaSeleccionada == null) {
                mostrarAlertaError("Error", "Por favor, selecciona una reserva para facturar.");
                return;
            }
    
            // Determinar el tipo de cliente (cliente, empleado o ambos) según el id_cliente
            String tipoCliente = modelo.obtenerTipoClientePorId(reservaSeleccionada.getId_cliente());
            double iva;
    
            switch (tipoCliente) {
                case "Cliente":
                    iva = 21.0; // IVA para clientes
                    break;
                case "Empleado":
                    iva = 7.0; // IVA para empleados
                    break;
                case "Ambos":
                    iva = 10.0; // IVA especial para ambos
                    break;
                default:
                    mostrarAlertaError("Error", "No se pudo determinar el tipo de cliente.");
                    return;
            }
    
            // Pasar la reserva y el IVA al FacturaController
            facturaController.setReserva(reservaSeleccionada, iva);
    
            // Crear la ventana emergente
            Stage stage = new Stage();
            stage.setTitle("Facturación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras esta está abierta
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlertaError("Error", "No se pudo abrir la ventana de facturación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Métodos para mostrar alertas
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
