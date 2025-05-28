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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Callback;

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
    @FXML
    private Button btnFacturar;

    private Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        btnFacturar.setDisable(true);

        comboTipoReserva.getItems().addAll("AD", "MP");
        cargarReservasEnLista();
        cargarHabitacionesSinReserva();
        cargarClientes();

        // Hacer que los campos txtTipoIVA y txtPrecioTotalReserva no sean editables
        txtTipoIVA.setEditable(false);
        txtPrecioTotalReserva.setEditable(false);

        listHabitacionesSinReserva.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String numeroHabitacion = newVal.split(" ")[0];
                int idHabitacion = modelo.obtenerIdHabitacionPorNumero(numeroHabitacion);
                if (idHabitacion != -1) {
                    marcarDiasOcupadosEnDatePicker(idHabitacion);
                }
            }
        });

        listPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String documentoIdentidad = newVal.split(" - ")[0];
                if (!documentoIdentidad.isEmpty()) {
                    int idCliente = modelo.obtenerIdClientePorDocumento(documentoIdentidad);
                    if (idCliente != -1) {
                        String tipoCliente = modelo.obtenerTipoClientePorId(idCliente);
                        double iva = modelo.obtenerIvaPorTipoCliente(tipoCliente);
                        txtTipoIVA.setText(String.valueOf(iva));
                    } else {
                        txtTipoIVA.setText("");
                    }
                }
            } else {
                txtTipoIVA.setText("");
            }
        });

        listReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String estado = newVal.getEstado();
                if ("Facturado".equalsIgnoreCase(estado)) {
                    btnFacturar.setDisable(true);
                } else {
                    btnFacturar.setDisable(false);
                }
            } else {
                btnFacturar.setDisable(true); // Nada seleccionado
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

            // ✅ 2. Extraer el número de la habitación (suponiendo que el formato es "101 -
            // Habitación Doble")
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
            if (dateFechaInicio.getValue() == null || dateFechaFin.getValue() == null
                    || dateFechaReserva.getValue() == null) {
                mostrarAlertaError("Error", "Por favor, selecciona todas las fechas.");
                return;
            }

            if (dateFechaInicio.getValue().isAfter(dateFechaFin.getValue())) {
                mostrarAlertaError("Error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
                return;
            }

            // 🆕 Verificar si la habitación ya tiene una reserva que se solapa con las
            // fechas seleccionadas
            LocalDate fechaInicio = dateFechaInicio.getValue();
            LocalDate fechaFin = dateFechaFin.getValue();

            if (modelo.existeSolapamientoReserva(idHabitacion, fechaInicio, fechaFin)) {
                mostrarAlertaError("Error", "La habitación ya está reservada durante ese período.");
                return;
            }

            // ✅ 9. Calcular precio total con IVA
            double precioBase = calcularPrecioBase(dateFechaInicio.getValue(), dateFechaFin.getValue(), tipoReserva);
            double precioTotal = precioBase; // Guardar solo el precio base, sin IVA

            // ✅ 10. Crear objeto Reserva con el ID real de la habitación
            Reserva nuevaReserva = new Reserva(
                    0, // ID generado automáticamente en la base de datos
                    TipoReserva.valueOf(tipoReserva),
                    dateFechaInicio.getValue(),
                    dateFechaFin.getValue(),
                    dateFechaReserva.getValue(),
                    precioTotal,
                    idCliente,
                    idHabitacion, // Ahora usamos el ID real obtenido de la BD
                    "Pendiente de facturar",
                    habitacionSeleccionada);

            // 11. Registrar la reserva en la base de datos
            boolean exito = modelo.registrarReserva(nuevaReserva);
            if (exito) {
                // 12. Actualizar estado de la habitación a "Ocupada"
                modelo.actualizarEstadoHabitacion(idHabitacion, "Ocupada");
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
                    String numeroHabitacion = reserva.getNumeroHabitacion() != null ? reserva.getNumeroHabitacion()
                            : "Sin asignar";
                    setText("Habitación: " + numeroHabitacion + " | Tipo: " + reserva.getTipo_reserva()
                            + " | Fechas: " + reserva.getFecha_inicio() + " a " + reserva.getFecha_fin()
                            + " | Estado: " + reserva.getEstado());
                }
            }
        });
    }

    // Método para cargar las habitaciones sin reserva
    private void cargarHabitacionesSinReserva() {
        try {
            ObservableList<String> habitaciones = modelo.obtenerHabitacionesConUltimaFecha();
            listHabitacionesSinReserva.setItems(habitaciones);
        } catch (Exception e) {
            mostrarAlertaError("Error", "No se pudieron cargar las habitaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarClientes() {
        ObservableList<String> personas = modelo.obtenerTodosLosClientes();
        listPersonas.setItems(personas);
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
            if (tipoCliente == null) {
                mostrarAlertaError("Error", "No se pudo obtener el tipo de cliente.");
                return;
            }

            switch (tipoCliente) {
                case "Cliente":
                    iva = 21.0;
                    break;
                case "Empleado":
                    iva = 7.0;
                    break;
                case "Ambos":
                    iva = 10.0;
                    break;
                default:
                    mostrarAlertaError("Error", "No se pudo determinar el tipo de cliente.");
                    return;
            }

            facturaController.setReserva(reservaSeleccionada, iva);

            if (facturaController.isFacturacionCompletada()) {
                // solo si se completó la facturación
                actualizarTablaReservas(); // o lo que uses para actualizar la vista
            }



            // Crear la ventana emergente
            Stage stage = new Stage();
            stage.setTitle("Facturación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal mientras esta está abierta
            stage.showAndWait();

            // 🆕 Actualizar estado de la reserva a "Facturado"
if (facturaController.isFacturacionCompletada()) {
    cargarReservasEnLista(); // ✅ Solo actualizar la vista
}

        } catch (IOException e) {
            mostrarAlertaError("Error", "No se pudo abrir la ventana de facturación: " + e.getMessage());
            e.printStackTrace();
        }
        Reserva seleccionada = listReservas.getSelectionModel().getSelectedItem();
        if (seleccionada != null && "Facturado".equalsIgnoreCase(seleccionada.getEstado())) {
            mostrarAlertaError("Error", "Esta reserva ya fue facturada.");
            return;
        }

    }

    private void actualizarTablaReservas() {
        List<Reserva> reservas = modelo.obtenerReservas(); // o reservaDAO.obtenerReservas()
        listReservas.getItems().setAll(reservas);
    }

    private void marcarDiasOcupadosEnDatePicker(int idHabitacion) {
        List<LocalDate[]> fechasOcupadas = modelo.obtenerFechasReservadasHabitacion(idHabitacion);

        Callback<DatePicker, DateCell> dayCellFactory = (DatePicker picker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                for (LocalDate[] rango : fechasOcupadas) {
                    LocalDate inicio = rango[0];
                    LocalDate fin = rango[1];
                    if (!empty && (date.isEqual(inicio) || date.isEqual(fin)
                            || (date.isAfter(inicio) && date.isBefore(fin)))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ff6666;"); // rojo claro
                    }
                }
            }
        };

        dateFechaInicio.setDayCellFactory(dayCellFactory);
        dateFechaFin.setDayCellFactory(dayCellFactory);
        dateFechaReserva.setDayCellFactory(dayCellFactory);

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
