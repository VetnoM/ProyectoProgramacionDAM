package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Persona;
import com.mycompany.practicabasededatos.modelo.TipoCliente;
import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class PrimaryController {
    //panel principal
    @FXML
    AnchorPane anchorPaneCentre;
    //datos para crear persona
    @FXML
    private ComboBox<String> cmbTipoPersona;
    @FXML
    private TextField txtDocumento, txtNombre, txtApellido, txtTelefono, txtEmail, txtDireccion;
    @FXML
    private DatePicker dpFechaNacimiento;

    // Campos específicos de Cliente
    @FXML
    private Label lblTipoCliente, lblTarjetaCredito, lblFechaRegistro, tituloCliente;
    @FXML
    private TextField txtTarjetaCredito;
    @FXML
    private DatePicker dpFechaRegistro;
    @FXML
    private ComboBox<String> cmbTipoCliente;

    // Campos específicos de Empleado
    @FXML
    private Label lblLugarTrabajo, lblSalario, lblEstadoLaboral, lblFechaContratacion, tituloEmpleado;
    @FXML
    private TextField txtLugarTrabajo, txtSalario;
    @FXML
    private ComboBox<String> cmbEstadoLaboral;
    @FXML
    private DatePicker dpFechaContratacion;

    // Lista para mostrar personas
    @FXML
    private ListView<String> listPersonas;

    @FXML
    private Button btnRetroceder;

    // Llamado al modelo
    Modelo modelo = new Modelo();


    @FXML
    public void initialize() {
        // Agregar opciones al ComboBox de tipo de persona
        cmbTipoPersona.getItems().addAll("Cliente", "Empleado", "Ambos");
        cmbTipoPersona.setOnAction(event -> cambiarVista());

        // Configurar ComboBox de tipo de Cliente
        cmbTipoCliente.getItems().addAll("REGULAR", "VIP");

        // Configurar ComboBox de estado laboral
        cmbEstadoLaboral.getItems().addAll("Activo", "Baja", "Permiso");

        // Deshabilitar todos los campos excepto el ComboBox de tipo de persona
        deshabilitarCampos();

        // Cargar lista de personas
        modelo.cargarListaPersonas(listPersonas);

   
    }

    // Método para deshabilitar todos los campos excepto el ComboBox de tipo de persona
    private void deshabilitarCampos() {
        // Cliente
        lblTipoCliente.setDisable(true);
        lblTarjetaCredito.setDisable(true);
        lblFechaRegistro.setDisable(true);
        txtTarjetaCredito.setDisable(true);
        dpFechaRegistro.setDisable(true);
        cmbTipoCliente.setDisable(true);
        tituloCliente.setDisable(true);

        // Empleado
        lblLugarTrabajo.setDisable(true);
        lblSalario.setDisable(true);
        lblEstadoLaboral.setDisable(true);
        lblFechaContratacion.setDisable(true);
        txtLugarTrabajo.setDisable(true);
        txtSalario.setDisable(true);
        dpFechaContratacion.setDisable(true);
        cmbEstadoLaboral.setDisable(true);
        tituloEmpleado.setDisable(true);

        // Campos comunes
        txtDocumento.setDisable(true);
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtTelefono.setDisable(true);
        txtEmail.setDisable(true);
        txtDireccion.setDisable(true);
        dpFechaNacimiento.setDisable(true);

      
 
    }

    // Método para habilitar los campos según el tipo de persona seleccionado
    private void habilitarCampos() {
        // Campos comunes
        txtDocumento.setDisable(false);
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        txtTelefono.setDisable(false);
        txtEmail.setDisable(false);
        txtDireccion.setDisable(false);
        dpFechaNacimiento.setDisable(false);

        String seleccion = cmbTipoPersona.getValue();

        if ("Cliente".equals(seleccion)) {
            // Habilitar solo campos de Cliente
            lblTipoCliente.setDisable(false);
            lblTarjetaCredito.setDisable(false);
            lblFechaRegistro.setDisable(false);
            txtTarjetaCredito.setDisable(false);
            dpFechaRegistro.setDisable(false);
            cmbTipoCliente.setDisable(false);
            tituloCliente.setDisable(false);

        } else if ("Empleado".equals(seleccion)) {
            // Habilitar solo campos de Empleado
            lblLugarTrabajo.setDisable(false);
            lblSalario.setDisable(false);
            lblEstadoLaboral.setDisable(false);
            lblFechaContratacion.setDisable(false);
            txtLugarTrabajo.setDisable(false);
            txtSalario.setDisable(false);
            dpFechaContratacion.setDisable(false);
            cmbEstadoLaboral.setDisable(false);
            tituloEmpleado.setDisable(false);

        } else if ("Ambos".equals(seleccion)) {
            // Habilitar todos los campos
            lblTipoCliente.setDisable(false);
            lblTarjetaCredito.setDisable(false);
            lblFechaRegistro.setDisable(false);
            txtTarjetaCredito.setDisable(false);
            dpFechaRegistro.setDisable(false);
            cmbTipoCliente.setDisable(false);
            tituloCliente.setDisable(false);

            lblLugarTrabajo.setDisable(false);
            lblSalario.setDisable(false);
            lblEstadoLaboral.setDisable(false);
            lblFechaContratacion.setDisable(false);
            txtLugarTrabajo.setDisable(false);
            txtSalario.setDisable(false);
            dpFechaContratacion.setDisable(false);
            cmbEstadoLaboral.setDisable(false);
            tituloEmpleado.setDisable(false);
        }


    }

    // Método para cambiar la vista según el tipo de persona
    private void cambiarVista() {
        deshabilitarCampos(); // Deshabilita todos los campos antes de actualizar
        habilitarCampos(); // Habilita los campos correspondientes según la selección
    }

    // // Método para cargar la lista de personas desde la base de datos
    // private void cargarListaPersonas() {
    //     ArrayList<Persona> personas = modelo.obtenerPersonas();
    //     if (personas != null) {
    //         listPersonas.getItems().clear();
    //         for (Persona p : personas) {
    //             listPersonas.getItems().add(p.getNombre() + " " + p.getApellido());
    //         }
    //     } else {
    //         System.out.println("No se pudieron obtener las personas.");
    //     }
    // }

    @FXML
    private void agregarUsuario() {
        // Obtener datos del formulario
        String documento = txtDocumento.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String direccion = txtDireccion.getText();
        Date fechaNacimiento = Date.valueOf(dpFechaNacimiento.getValue());

        String tipoSeleccionado = cmbTipoPersona.getValue();

        if (documento.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty() || fechaNacimiento == null || tipoSeleccionado == null) {
            mostrarAlertaError("Error", "Por favor, completa todos los campos.");
            return;
        }

        // Crear la persona en la base de datos a través del Modelo
        Persona persona = new Persona(0, documento, nombre, apellido, fechaNacimiento, telefono, email, direccion);
        int idPersona = modelo.insertarPersona(persona);

        if (idPersona > 0) { // Si la inserción fue exitosa
            // Agregar detalles adicionales dependiendo del tipo de persona (Cliente o Empleado)
            if ("Cliente".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
                String tipoCliente = cmbTipoCliente.getValue();
                if (tipoCliente == null) {
                    mostrarAlertaError("Error", "Por favor, selecciona el tipo de cliente.");
                    return;
                }
                String tarjetaCredito = txtTarjetaCredito.getText();
                Date fechaRegistro = Date.valueOf(dpFechaRegistro.getValue());
                Cliente cliente = new Cliente(idPersona, fechaRegistro, tarjetaCredito, TipoCliente.valueOf(tipoCliente.toUpperCase()));
                modelo.insertarCliente(idPersona, cliente);
            }

            if ("Empleado".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
                String lugarTrabajo = txtLugarTrabajo.getText();
                if (lugarTrabajo.isEmpty()) {
                    mostrarAlertaError("Error", "Por favor, completa el campo de lugar de trabajo.");
                    return;
                }
                double salario;
                try {
                    salario = Double.parseDouble(txtSalario.getText());
                } catch (NumberFormatException e) {
                    mostrarAlertaError("Error", "Por favor, ingresa un salario válido.");
                    return;
                }
                String estadoLaboral = cmbEstadoLaboral.getValue();
                if (estadoLaboral == null) {
                    mostrarAlertaError("Error", "Por favor, selecciona el estado laboral.");
                    return;
                }
                Date fechaContratacion = Date.valueOf(dpFechaContratacion.getValue());
                Empleado empleado = new Empleado(idPersona, lugarTrabajo, salario, EstadoLaboral.valueOf(estadoLaboral.toUpperCase()), fechaContratacion);
                modelo.insertarEmpleado(idPersona, empleado);
            }

            mostrarAlertaInformacion("Éxito", "Usuario agregado correctamente.");
            actualizarLista();
        } else {
            mostrarAlertaError("Error", "No se pudo agregar la persona.");
        }
    }

    public void actualizarLista() {
        // Obtener la lista de personas desde el Modelo
        ArrayList<Persona> personas = modelo.obtenerPersonas();

        // Limpiar el ListView antes de llenarlo
        listPersonas.getItems().clear();

        // Llenar el ListView con los nombres de las personas
        for (Persona p : personas) {
            listPersonas.getItems().add(p.getNombre() + " " + p.getApellido());
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

    // Método para mostrar alerta de confirmación
    public static boolean mostrarAlertaConfirmacion(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // Esperar la respuesta del usuario
        ButtonType respuesta = alerta.showAndWait().orElse(ButtonType.CANCEL);
        return respuesta == ButtonType.OK; // Devuelve true si el usuario seleccionó OK
    }

    // Método para mostrar alerta de advertencia
    public static void mostrarAlertaAdvertencia(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}