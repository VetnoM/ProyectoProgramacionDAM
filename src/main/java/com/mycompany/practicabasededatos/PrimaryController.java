package com.mycompany.practicabasededatos;
import com.mycompany.practicabasededatos.database.PersonaDAO;
import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Persona;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.ArrayList;
import javafx.fxml.FXML;

public class PrimaryController {
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
  
    private PersonaDAO personaDAO = new PersonaDAO();

    //llamado al modelo
    Modelo modelo = new Modelo();
    @FXML
    public void initialize() {
        // Agregar opciones al ComboBox de tipo de persona
        cmbTipoPersona.getItems().addAll("Cliente", "Empleado", "Ambos");
        cmbTipoPersona.setOnAction(event -> cambiarVista());

        // Configurar ComboBox de tipo de Cliente
        cmbTipoCliente.getItems().addAll("Regular", "VIP");

        // Configurar ComboBox de estado laboral
        cmbEstadoLaboral.getItems().addAll("Activo", "Inactivo", "Suspendido");

        // Ocultar campos extra al inicio
        ocultarCamposExtra();

        // Cargar lista de personas
        cargarListaPersonas();
    }

    // Método para ocultar los campos extra de Cliente y Empleado
    private void ocultarCamposExtra() {
        // Cliente
        lblTipoCliente.setVisible(false);
        lblTarjetaCredito.setVisible(false);
        lblFechaRegistro.setVisible(false);
        txtTarjetaCredito.setVisible(false);
        dpFechaRegistro.setVisible(false);
        cmbTipoCliente.setVisible(false);
        tituloCliente.setVisible(false);

        // Empleado
        lblLugarTrabajo.setVisible(false);
        lblSalario.setVisible(false);
        lblEstadoLaboral.setVisible(false);
        lblFechaContratacion.setVisible(false);
        txtLugarTrabajo.setVisible(false);
        txtSalario.setVisible(false);
        dpFechaContratacion.setVisible(false);
        cmbEstadoLaboral.setVisible(false);
        tituloEmpleado.setVisible(false);
        
    }

    // Método para cambiar la vista según el tipo de persona
    private void cambiarVista() {
        ocultarCamposExtra(); // Oculta todos los campos antes de actualizar

        String seleccion = cmbTipoPersona.getValue();

        if ("Cliente".equals(seleccion)) {
            // Mostrar solo campos de Cliente
            lblTipoCliente.setVisible(true);
            lblTarjetaCredito.setVisible(true);
            lblFechaRegistro.setVisible(true);
            txtTarjetaCredito.setVisible(true);
            dpFechaRegistro.setVisible(true);
            cmbTipoCliente.setVisible(true);
            tituloCliente.setVisible(true);

        } else if ("Empleado".equals(seleccion)) {
            // Mostrar solo campos de Empleado
            lblLugarTrabajo.setVisible(true);
            lblSalario.setVisible(true);
            lblEstadoLaboral.setVisible(true);
            lblFechaContratacion.setVisible(true);
            txtLugarTrabajo.setVisible(true);
            txtSalario.setVisible(true);
            dpFechaContratacion.setVisible(true);
            cmbEstadoLaboral.setVisible(true);
            tituloEmpleado.setVisible(true);
            
        } else if ("Ambos".equals(seleccion)) {
            // Mostrar todos los campos
            lblTipoCliente.setVisible(true);
            lblTarjetaCredito.setVisible(true);
            lblFechaRegistro.setVisible(true);
            txtTarjetaCredito.setVisible(true);
            dpFechaRegistro.setVisible(true);
            cmbTipoCliente.setVisible(true);
            tituloCliente.setVisible(true);
            
            lblLugarTrabajo.setVisible(true);
            lblSalario.setVisible(true);
            lblEstadoLaboral.setVisible(true);
            lblFechaContratacion.setVisible(true);
            txtLugarTrabajo.setVisible(true);
            txtSalario.setVisible(true);
            dpFechaContratacion.setVisible(true);
            cmbEstadoLaboral.setVisible(true);
            tituloEmpleado.setVisible(true);
            
        }
    }

    // Método para cargar la lista de personas desde la base de datos
    private void cargarListaPersonas() {
        ArrayList<Persona> personas = personaDAO.obtenerPersonas();
        for (Persona p : personas) {
            listPersonas.getItems().add(p.getNombre() + " " + p.getApellido());
        }
    }
    
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
    Modelo modelo = new Modelo();
    Persona persona = new Persona(0, documento, nombre, apellido, fechaNacimiento, telefono, email, direccion);
    int idPersona = modelo.insertarPersona(persona);

    if (idPersona > 0) { // Si la inserción fue exitosa
        // Agregar detalles adicionales dependiendo del tipo de persona (Cliente o Empleado)
        if ("Cliente".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
            String tipoCliente = cmbTipoCliente.getValue();
            String tarjetaCredito = txtTarjetaCredito.getText();
            Date fechaRegistro = Date.valueOf(dpFechaRegistro.getValue());
            modelo.insertarCliente(idPersona, tipoCliente, tarjetaCredito, fechaRegistro);
        }

        if ("Empleado".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
            String lugarTrabajo = txtLugarTrabajo.getText();
            double salario = Double.parseDouble(txtSalario.getText());
            String estadoLaboral = cmbEstadoLaboral.getValue();
            Date fechaContratacion = Date.valueOf(dpFechaContratacion.getValue());
            modelo.insertarEmpleado(idPersona, lugarTrabajo, salario, estadoLaboral, fechaContratacion);
        }

        mostrarAlertaInformacion("Éxito", "Usuario agregado correctamente.");
        actualizarLista();
    } else {
        mostrarAlertaError("Error", "No se pudo agregar la persona.");
    }
}

public void actualizarLista(){
   // Obtener la lista de personas desde el Modelo
    Modelo modelo = new Modelo();
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
    
    

