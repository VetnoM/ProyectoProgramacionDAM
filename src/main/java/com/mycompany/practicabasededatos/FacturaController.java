package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.database.FacturaDAO;
import com.mycompany.practicabasededatos.modelo.Factura;
import com.mycompany.practicabasededatos.modelo.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FacturaController {

    @FXML
    private Label lblNumeroReserva, lblCliente, lblFechaInicio, lblFechaFin, lblPrecioBase, lblIva, lblPrecioTotal,
            lblTotalDias, lblServicio, lblDescuento;

    @FXML
    private ComboBox<String> cmbMetodoPago;

    private FacturaDAO facturaDAO = new FacturaDAO();
    private Reserva reserva;
    private double iva;
    private boolean facturacionCompletada = false;

    @FXML
    public void initialize() {
        // Configurar las opciones del ComboBox para el método de pago
        cmbMetodoPago.getItems().addAll("Efectivo", "Tarjeta de Crédito", "Transferencia Bancaria");
        cmbMetodoPago.setPromptText("Selecciona un método de pago");

          // Escuchar cambios en el método de pago
    cmbMetodoPago.setOnAction(e -> aplicarDescuentoSiCorresponde());
    }

    public void setReserva(Reserva reserva, double iva) {
        this.reserva = reserva;
        this.iva = iva;
        cargarDatosReserva();
    }

    private void cargarDatosReserva() {
        if (reserva != null) {
            lblNumeroReserva.setText(String.valueOf(reserva.getId_reserva()));
            lblCliente.setText(String.valueOf(reserva.getId_cliente()));
            lblFechaInicio.setText(reserva.getFecha_inicio().toString());
            lblFechaFin.setText(reserva.getFecha_fin().toString());

            long totalDias = ChronoUnit.DAYS.between(reserva.getFecha_inicio(), reserva.getFecha_fin());
            lblTotalDias.setText(String.valueOf(totalDias));

            double precioBase = reserva.getPrecio_total();
            lblPrecioBase.setText(String.format("%.2f", precioBase).replace(",", "."));

            double montoIva = precioBase * (iva / 100);
            lblIva.setText(String.format("%.2f", montoIva).replace(",", "."));

            double precioTotal = precioBase + montoIva;
            lblPrecioTotal.setText(String.format("%.2f", precioTotal).replace(",", "."));

            lblDescuento.setText("");
        }
    }

    @FXML
    private void crearFactura() {
        try {
            String metodoPago = cmbMetodoPago.getValue();
            if (metodoPago == null || metodoPago.isEmpty()) {
                mostrarAlerta("Campos Vacíos", "Por favor, selecciona un método de pago.");
                return;
            }

            double precioBase = Double.parseDouble(lblPrecioBase.getText().replace(",", "."));
            double montoIva = Double.parseDouble(lblIva.getText().replace(",", "."));
            double precioTotal = precioBase + montoIva;
            double descuento = 0.0;

                  // Aplicar descuento si el pago es en efectivo
        if (metodoPago.equalsIgnoreCase("Efectivo")) {
            descuento = (precioTotal * 0.10);
            precioTotal -= descuento;
            lblDescuento.setText("-10%");
        } else {
            lblDescuento.setText("");
        }

            LocalDate fechaEmision = LocalDate.now();
            int idReserva = reserva.getId_reserva();

            // Crear la factura
            Factura factura = new Factura(fechaEmision, metodoPago, precioBase, montoIva, precioTotal, idReserva);
            facturaDAO.crearFactura(factura);

            // Actualizar el estado de la reserva a "Facturado"
            boolean exito = facturaDAO.actualizarEstadoReserva(idReserva, "Facturado");
            if (exito) {
                facturacionCompletada = true; // ✅ Marcar que la facturación fue completada
                mostrarAlerta("Estado actualizado", "La reserva fue marcada como 'Facturado'.");
            } else {
                mostrarAlerta("Error", "Factura creada pero no se pudo actualizar el estado de la reserva.");
            }

            // Cerrar la ventana de facturación
            Stage stage = (Stage) cmbMetodoPago.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Formato de número inválido. Asegúrate de que los valores sean correctos.");
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al crear la factura.");
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana() {
        // Cerrar la ventana actual
        Stage stage = (Stage) lblNumeroReserva.getScene().getWindow();
        stage.close();
    }

    /**
     * Método para mostrar alertas en la interfaz.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public boolean isFacturacionCompletada() {
        return facturacionCompletada;
    }

    private void aplicarDescuentoSiCorresponde() {
    String metodoPago = cmbMetodoPago.getValue();
    if (metodoPago == null || metodoPago.isEmpty()) return;

    double precioBase = Double.parseDouble(lblPrecioBase.getText().replace(",", "."));
    double montoIva = precioBase * (iva / 100);
    double precioTotal = precioBase + montoIva;

    if (metodoPago.equalsIgnoreCase("Efectivo")) {
        double descuento = precioTotal * 0.10;
        precioTotal -= descuento;
        lblDescuento.setText("-10%");
    } else {
        lblDescuento.setText("");
    }

    lblIva.setText(String.format("%.2f", montoIva).replace(",", "."));
    lblPrecioTotal.setText(String.format("%.2f", precioTotal).replace(",", "."));
}
}