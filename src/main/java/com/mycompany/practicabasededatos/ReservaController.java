package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Reserva;
import com.mycompany.practicabasededatos.modelo.TipoReserva;
import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.Modelo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ReservaController {
    @FXML
    private TextField txtIdReserva;
    @FXML
    private DatePicker dateDataReserva;
    @FXML
    private DatePicker dateDataInici;
    @FXML
    private DatePicker dateDataFi;
    @FXML
    private ComboBox<String> comboTipusReserva;
    @FXML
    private TextField txtTipusIVA;
    @FXML
    private TextField txtPreuTotalReserva;
    @FXML
    private ListView<Reserva> listReserves;
    @FXML
    private Button btnCrearReserva;
    @FXML
    private Button btnGuardarCanvis;
    @FXML
    private Button btnEliminarReserva;
    @FXML
    private ComboBox<Cliente> comboClientes;

    private Modelo modelo = new Modelo();

    @FXML
    public void initialize() {
        comboTipusReserva.getItems().addAll("AD", "MP");
        carregarReservesEnLlista();
        carregarClientsEnCombo(); // Cargar los clientes en el ComboBox

        // Listener para alternar botones al seleccionar un elemento de la lista
        listReserves.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarDetallsReserva(newValue);
                alternarBotons(false); // Mostrar "Guardar Canvis" y "Eliminar", ocultar "Crear"
            }
        });
    }

    // Método para cargar los clientes en el ComboBox
    private void carregarClientsEnCombo() {
        ObservableList<Cliente> clientes = modelo.obtenerClientes(); // Obtener clientes desde la base de datos
        comboClientes.setItems(clientes);

        comboClientes.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getDocumento_identidad() + " - " + cliente.getNombre() + " "
                            + cliente.getApellido());
                }
            }
        });

        comboClientes.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getDocumento_identidad() + " - " + cliente.getNombre() + " "
                            + cliente.getApellido());
                }
            }
        });
    }

    // Método para cargar las reservas en la lista
    private void carregarReservesEnLlista() {
        ObservableList<Reserva> reserves = modelo.obtenerReserves();
        listReserves.setItems(reserves);

        listReserves.setCellFactory(param -> new ListCell<Reserva>() {
            @Override
            protected void updateItem(Reserva reserva, boolean empty) {
                super.updateItem(reserva, empty);
                if (empty || reserva == null) {
                    setText(null);
                } else {
                    // Usar los métodos correctos de la clase Reserva
                    setText("Reserva " + reserva.getId_reserva() + " - " + reserva.getTipo_reserva() + " ("
                            + reserva.getFecha_inicio() + " a " + reserva.getFecha_fin() + ")");
                }
            }
        });
    }

    // Método para mostrar los detalles de la reserva seleccionada
    private void mostrarDetallsReserva(Reserva reserva) {
        if (reserva != null) {
            txtIdReserva.setText(String.valueOf(reserva.getId_reserva()));
            dateDataReserva.setValue(reserva.getFecha_reserva());
            dateDataInici.setValue(reserva.getFecha_inicio());
            dateDataFi.setValue(reserva.getFecha_fin());
            comboTipusReserva.setValue(reserva.getTipo_reserva().name());
            txtTipusIVA.setText(String.valueOf(reserva.getPrecio_total())); // Cambiar si es necesario
            txtPreuTotalReserva.setText(String.valueOf(reserva.getPrecio_total()));
        } else {
            netejarCamps();
        }
    }

    // Mètode per netejar els camps
    private void netejarCamps() {
        txtIdReserva.clear();
        dateDataReserva.setValue(null);
        dateDataInici.setValue(null);
        dateDataFi.setValue(null);
        comboTipusReserva.setValue(null);
        txtTipusIVA.clear();
        txtPreuTotalReserva.clear();
    }

    // Mètode per alternar la visibilitat dels botons
    private void alternarBotons(boolean mostrarCrear) {
        btnCrearReserva.setVisible(mostrarCrear);
        btnGuardarCanvis.setVisible(!mostrarCrear);
        btnEliminarReserva.setVisible(!mostrarCrear);
    }

    // Mètode per crear una reserva
    @FXML
private void crearReserva() {
    try {
        // Obtener los valores de los campos
        LocalDate dataReserva = dateDataReserva.getValue();
        LocalDate dataInici = dateDataInici.getValue();
        LocalDate dataFi = dateDataFi.getValue();
        String tipusReserva = comboTipusReserva.getValue();
        double tipusIVA = Double.parseDouble(txtTipusIVA.getText()); // Obtener el tipo de IVA
        double preuTotalReserva = Double.parseDouble(txtPreuTotalReserva.getText());
        Cliente clienteSeleccionado = comboClientes.getValue(); // Obtener el cliente seleccionado

        // Validar que todos los campos estén completos
        if (dataReserva == null || dataInici == null || dataFi == null || tipusReserva == null || clienteSeleccionado == null) {
            mostrarAlertaError("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Obtener el DNI del cliente seleccionado
        String dniCliente = clienteSeleccionado.getDocumento_identidad();

        // Buscar el ID del cliente en la base de datos
        int idCliente = modelo.obtenerIdClientePorDni(dniCliente);
        if (idCliente == -1) {
            mostrarAlertaError("Error", "No se encontró el cliente en la base de datos.");
            return;
        }

        // Generar el nuevo ID de reserva
        int ultimoIdReserva = modelo.obtenerProximoIdReserva();
        int nuevoIdReserva = ultimoIdReserva + 1;

        // Convertir el tipo de reserva (String) al enum TipoReserva
        TipoReserva tipoReservaEnum = TipoReserva.valueOf(tipusReserva.toUpperCase());

        // Crear la reserva con el nuevo ID
        Reserva reserva = new Reserva(
            nuevoIdReserva,          // ID de la reserva generado manualmente
            tipoReservaEnum,         // Tipo de reserva (enum)
            dataInici,               // Fecha de inicio
            dataFi,                  // Fecha de fin
            dataReserva,             // Fecha de la reserva
            preuTotalReserva,        // Precio total
            tipusIVA,                // Tipo de IVA
            idCliente                // ID del cliente
        );

        // Guardar la reserva en la base de datos
        boolean creada = modelo.crearReserva(reserva);

        if (creada) {
            mostrarAlertaInformacio("Éxito", "Reserva creada correctamente.");
            carregarReservesEnLlista();
            netejarCamps();
            alternarBotons(true);
        } else {
            mostrarAlertaError("Error", "No se pudo crear la reserva.");
        }
    } catch (NumberFormatException e) {
        mostrarAlertaError("Error", "Por favor, ingresa valores válidos.");
    }
}

    @FXML
    private void guardarCanvis() {
        // Obtener la reserva seleccionada
        Reserva reservaSeleccionada = listReserves.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlertaError("Error", "Por favor, selecciona una reserva para actualizar.");
            return;
        }

        try {
            // Obtener los valores actualizados de los campos
            int idReserva = Integer.parseInt(txtIdReserva.getText());
            LocalDate dataReserva = dateDataReserva.getValue();
            LocalDate dataInici = dateDataInici.getValue();
            LocalDate dataFi = dateDataFi.getValue();
            String tipusReserva = comboTipusReserva.getValue();
            double tipusIVA = Double.parseDouble(txtTipusIVA.getText());
            double preuTotalReserva = Double.parseDouble(txtPreuTotalReserva.getText());
            Cliente clienteSeleccionado = comboClientes.getValue(); // Obtener el cliente seleccionado

            // Validar que todos los campos estén completos
            if (dataReserva == null || dataInici == null || dataFi == null || tipusReserva == null
                    || clienteSeleccionado == null) {
                mostrarAlertaError("Error", "Todos los campos son obligatorios.");
                return;
            }

            // Obtener el DNI del cliente seleccionado
            String dniCliente = clienteSeleccionado.getDocumento_identidad();

            // Buscar el ID del cliente en la base de datos
            int idCliente = modelo.obtenerIdClientePorDni(dniCliente);
            if (idCliente == -1) {
                mostrarAlertaError("Error", "No se encontró el cliente en la base de datos.");
                return;
            }

            // Crear una nueva instancia de Reserva con los valores actualizados
            Reserva reservaActualizada = new Reserva(
                idReserva,               // ID de la reserva
                TipoReserva.valueOf(tipusReserva.toUpperCase()), // Tipo de reserva (enum)
                dataInici,               // Fecha de inicio
                dataFi,                  // Fecha de fin
                dataReserva,             // Fecha de la reserva
                preuTotalReserva,        // Precio total
                tipusIVA,                // Tipo de IVA
                idCliente                // ID del cliente
            );

            // Llamar al modelo para actualizar la reserva
            boolean actualizada = modelo.actualitzarReserva(reservaActualizada);

            if (actualizada) {
                mostrarAlertaInformacio("Éxito", "Reserva actualizada correctamente.");
                carregarReservesEnLlista(); // Refrescar la lista de reservas
                netejarCamps(); // Limpiar los campos
                alternarBotons(true); // Alternar los botones
            } else {
                mostrarAlertaError("Error", "No se pudo actualizar la reserva.");
            }
        } catch (NumberFormatException e) {
            mostrarAlertaError("Error", "Por favor, ingresa valores válidos.");
        }
    }

    // Mètode per eliminar una reserva
    @FXML
    private void eliminarReserva() {
        Reserva reservaSeleccionada = listReserves.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            mostrarAlertaError("Error", "Si us plau, selecciona una reserva.");
            return;
        }

        Alert confirmacio = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacio.setTitle("Confirmació");
        confirmacio.setHeaderText("Estàs segur que vols eliminar aquesta reserva?");
        confirmacio.setContentText("Aquesta acció no es pot desfer.");

        if (confirmacio.showAndWait().get() == ButtonType.OK) {
            boolean eliminada = modelo.eliminarReserva(reservaSeleccionada.getId_reserva());

            if (eliminada) {
                mostrarAlertaInformacio("Èxit", "Reserva eliminada correctament.");
                carregarReservesEnLlista();
                netejarCamps();
                alternarBotons(true);
            } else {
                mostrarAlertaError("Error", "No s'ha pogut eliminar la reserva.");
            }
        }
    }

    // Mètode per mostrar alerta d'informació
    public static void mostrarAlertaInformacio(String titol, String missatge) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titol);
        alerta.setHeaderText(null);
        alerta.setContentText(missatge);
        alerta.showAndWait();
    }

    // Mètode per mostrar alerta d'error
    public static void mostrarAlertaError(String titol, String missatge) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titol);
        alerta.setHeaderText(null);
        alerta.setContentText(missatge);
        alerta.showAndWait();
    }
}
