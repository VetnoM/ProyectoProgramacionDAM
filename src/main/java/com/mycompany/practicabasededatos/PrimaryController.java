package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.database.DatabaseConnection;
import com.mycompany.practicabasededatos.database.TareaDAO;
import com.mycompany.practicabasededatos.modelo.Modelo;
import com.mycompany.practicabasededatos.modelo.Persona;
import com.mycompany.practicabasededatos.modelo.TipoCliente;
import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

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

    @FXML
    private Button btnRetroceder;

    // Campos para agregar Tareas
    @FXML
    private TextField txtDescripcionTarea, txtDocumentoEmpleado;
    @FXML
    private DatePicker dpFechaEjecucionTarea;
    @FXML
    private Button btnCrearTarea, btnAsignarTarea;

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
        cmbEstadoLaboral.getItems().addAll("Activo", "Inactivo", "Suspendido");

        // Deshabilitar todos los campos excepto el ComboBox de tipo de persona
        deshabilitarCampos();
    }

    // Método para deshabilitar todos los campos excepto el ComboBox de tipo de
    // persona
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
    // Método para cargar la lista de personas desde la base de datos


 @FXML
private void agregarUsuario() {
    limpiarErrores(); // ← Limpia errores visuales anteriores

    String documento = txtDocumento.getText();
    String nombre = txtNombre.getText();
    String apellido = txtApellido.getText();
    String telefono = txtTelefono.getText();
    String email = txtEmail.getText();
    String direccion = txtDireccion.getText();
    String tipoSeleccionado = cmbTipoPersona.getValue();

    // Validación inicial
    if (documento.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
        telefono.isEmpty() || email.isEmpty() || direccion.isEmpty() || tipoSeleccionado == null) {
        mostrarAlertaError("Error", "Por favor, completa todos los campos obligatorios.");
        return;
    }

    // Validación de campos de texto
    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        mostrarAlertaError("Error", "El nombre no debe contener números ni caracteres especiales.");
        return;
    }

    if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
        mostrarAlertaError("Error", "El apellido no debe contener números ni caracteres especiales.");
        return;
    }

    if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
        mostrarAlertaError("Error", "Por favor, ingresa un correo electrónico válido.");
        return;
    }

    // Validación de fecha de nacimiento
    if (dpFechaNacimiento.getValue() == null) {
        mostrarAlertaError("Error", "Por favor, selecciona la fecha de nacimiento.");
        return;
    }

    java.time.LocalDate hoy = java.time.LocalDate.now();

    if (dpFechaNacimiento.getValue().isAfter(hoy)) {
        mostrarAlertaError("Error", "La fecha de nacimiento no puede ser futura.");
        return;
    }

    Date fechaNacimiento = Date.valueOf(dpFechaNacimiento.getValue());

    // Crear la persona
    Persona persona = new Persona(0, documento, nombre, apellido, fechaNacimiento, telefono, email, direccion);
    int idPersona = modelo.insertarPersona(persona);

    if (idPersona <= 0) {
        mostrarAlertaError("Error", "No se pudo agregar la persona.");
        return;
    }

    // Cliente
    if ("Cliente".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
        String tipoCliente = cmbTipoCliente.getValue();
        if (tipoCliente == null) {
            mostrarAlertaError("Error", "Por favor, selecciona el tipo de cliente.");
            return;
        }

        if (dpFechaRegistro.getValue() == null) {
            mostrarAlertaError("Error", "Por favor, selecciona la fecha de registro.");
            return;
        }

        if (dpFechaRegistro.getValue().isAfter(hoy)) {
            mostrarAlertaError("Error", "La fecha de registro no puede ser futura.");
            return;
        }

        String tarjetaCredito = txtTarjetaCredito.getText();
        Date fechaRegistro = Date.valueOf(dpFechaRegistro.getValue());

        Cliente cliente = new Cliente(idPersona, fechaRegistro, tarjetaCredito,
                TipoCliente.valueOf(tipoCliente.toUpperCase()));
        modelo.insertarCliente(idPersona, cliente);
    }

    // Empleado
    if ("Empleado".equals(tipoSeleccionado) || "Ambos".equals(tipoSeleccionado)) {
        String lugarTrabajo = txtLugarTrabajo.getText();
        if (lugarTrabajo.isEmpty()) {
            mostrarAlertaError("Error", "Por favor, completa el campo de lugar de trabajo.");
            return;
        }

        double salario;
        try {
            salario = Double.parseDouble(txtSalario.getText());
            if (salario < 0) {
                mostrarAlertaError("Error", "El salario debe ser un número positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlertaError("Error", "Por favor, ingresa un salario válido.");
            return;
        }

        String estadoLaboral = cmbEstadoLaboral.getValue();
        if (estadoLaboral == null) {
            mostrarAlertaError("Error", "Por favor, selecciona el estado laboral.");
            return;
        }

        if (dpFechaContratacion.getValue() == null) {
            mostrarAlertaError("Error", "Por favor, selecciona la fecha de contratación.");
            return;
        }

        if (dpFechaContratacion.getValue().isAfter(hoy) ||
            dpFechaContratacion.getValue().getYear() < 2000 ||
            dpFechaContratacion.getValue().getYear() > 2024) {
            mostrarAlertaError("Error", "La fecha de contratación debe estar entre 2000 y 2024.");
            return;
        }

        Date fechaContratacion = Date.valueOf(dpFechaContratacion.getValue());

        Empleado empleado = new Empleado(idPersona, lugarTrabajo, salario,
                EstadoLaboral.valueOf(estadoLaboral.toUpperCase()), fechaContratacion);
        modelo.insertarEmpleado(idPersona, empleado);
    }

    mostrarAlertaInformacion("Éxito", "Usuario agregado correctamente.");
    actualizarLista();
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

    // // Método para crear una tarea
    // @FXML
    // private void crearYAsignarTarea() {
    //     String descripcion = txtDescripcionTarea.getText();
    //     Date fechaEjecucion = (dpFechaEjecucionTarea.getValue() != null) ? Date.valueOf(dpFechaEjecucionTarea.getValue()) : null;
    
    //     if (descripcion.isEmpty()) {
    //         mostrarAlertaError("Error", "Por favor, ingresa la descripción de la tarea.");
    //         return;
    //     }
    
    //     String documentoEmpleado = txtDocumentoEmpleado.getText();
    //     if (documentoEmpleado.isEmpty()) {
    //         mostrarAlertaError("Error", "Por favor, ingresa el documento del empleado.");
    //         return;
    //     }
    
    //     try (Connection conn = DatabaseConnection.getConnection()) {
    //         TareaDAO tareaDAO = new TareaDAO();
    //         int idTarea = tareaDAO.crearTarea(descripcion, fechaEjecucion);
    
    //         if (idTarea > 0) { // Si la tarea se creó correctamente
    //             boolean asignada = tareaDAO.asignarTarea(documentoEmpleado, idTarea);
    //             if (asignada) {
    //                 mostrarAlertaInformacion("Éxito", "Tarea creada y asignada correctamente.");
    //             } else {
    //                 mostrarAlertaError("Error", "No se pudo asignar la tarea.");
    //             }
    //         } else {
    //             mostrarAlertaError("Error", "No se pudo crear la tarea.");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         mostrarAlertaError("Error", "Ocurrió un problema al asignar la tarea.");
    //     }
    // }
    
    private void marcarError(Control campo) {
    if (!campo.getStyleClass().contains("error")) {
        campo.getStyleClass().add("error");
    }
}

private void limpiarErrores() {
    txtNombre.getStyleClass().remove("error");
    txtApellido.getStyleClass().remove("error");
    txtEmail.getStyleClass().remove("error");
    txtSalario.getStyleClass().remove("error");
    dpFechaNacimiento.getStyleClass().remove("error");
    dpFechaContratacion.getStyleClass().remove("error");
    cmbEstadoLaboral.getStyleClass().remove("error");
    cmbTipoCliente.getStyleClass().remove("error");
    dpFechaRegistro.getStyleClass().remove("error");
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
