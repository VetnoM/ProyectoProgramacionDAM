package com.mycompany.practicabasededatos;

import com.mycompany.practicabasededatos.database.ClienteDAO;
import com.mycompany.practicabasededatos.database.EmpleadoDAO;
import com.mycompany.practicabasededatos.database.PersonaDAO;
import com.mycompany.practicabasededatos.modelo.Cliente;
import com.mycompany.practicabasededatos.modelo.Empleado;
import com.mycompany.practicabasededatos.modelo.EstadoLaboral;
import com.mycompany.practicabasededatos.modelo.Persona;
import com.mycompany.practicabasededatos.modelo.TipoCliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;

public class ModificarUsuarioController {

    @FXML private TextField txtDocumento;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<TipoCliente> cmbTipoCliente;
    @FXML private TextField txtSalario;
    @FXML private TextField txtLugarTrabajo;
    @FXML private ComboBox<EstadoLaboral> cmbEstadoLaboral;
    @FXML private ListView<String> listViewUsuarios;  // Muestra coincidencias por documento
    @FXML private Button btnBuscar;

    private PersonaDAO personaDAO = new PersonaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    private Persona personaActual;
    private Cliente clienteActual;
    private Empleado empleadoActual;

    @FXML
    public void initialize() {
        // Cargar valores en los ComboBox
        cmbTipoCliente.setItems(FXCollections.observableArrayList(TipoCliente.values()));
        cmbEstadoLaboral.setItems(FXCollections.observableArrayList(EstadoLaboral.values()));

        // Manejar selección en ListView
        listViewUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtDocumento.setText(newVal.split(" - ")[0]);  // Extrae el documento del formato "Documento - Nombre"
                buscarUsuario();
            }
        });
    }

    @FXML
    public void buscarUsuario() {
        String documento = txtDocumento.getText().trim();
        
        if (documento.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un documento válido.");
            return;
        }
    
        try {
            // Buscar la persona en la base de datos
            personaActual = personaDAO.obtenerPersonaPorDocumento(documento);
            
            if (personaActual == null) {
                mostrarAlerta("Usuario no encontrado", "El documento ingresado no está registrado.");
                limpiarCampos();
                return;
            }
    
            // Mostrar datos de persona
            txtDireccion.setText(personaActual.getDireccion());
            txtCorreo.setText(personaActual.getEmail());
    
            // Buscar si es cliente
            clienteActual = clienteDAO.obtenerClientePorIdPersona(personaActual.getId_persona());
            if (clienteActual != null) {
                cmbTipoCliente.setValue(clienteActual.getTipoCliente());
            } else {
                cmbTipoCliente.setValue(null);
            }
    
            // Buscar si es empleado
            empleadoActual = empleadoDAO.obtenerEmpleadoPorIdPersona(personaActual.getId_persona());
            if (empleadoActual != null) {
                txtSalario.setText(String.valueOf(empleadoActual.getSalario_bruto()));
                txtLugarTrabajo.setText(empleadoActual.getLugar_trabajo());
                cmbEstadoLaboral.setValue(empleadoActual.getEstadolaboral());
            } else {
                txtSalario.clear();
                txtLugarTrabajo.clear();
                cmbEstadoLaboral.setValue(null);
            }
    
        } catch (Exception e) {  // Capturamos cualquier otro error que pueda ocurrir (aunque se recomienda especificar tipos de excepciones)
            mostrarAlerta("Error", "No se pudo realizar la búsqueda.");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void guardarCambios() {
        if (personaActual == null) {
            mostrarAlerta("Error", "Debe buscar un usuario antes de guardar.");
            return;
        }

        try {
            // Actualizar datos de Persona
            personaActual.setDireccion(txtDireccion.getText());
            personaActual.setEmail(txtCorreo.getText());
            personaDAO.actualizarPersona(personaActual);

            // Actualizar datos de Cliente si existe
            if (clienteActual != null) {
                clienteActual.setTipoCliente(cmbTipoCliente.getValue());
                clienteDAO.actualizarCliente(clienteActual);
            }

            // Actualizar datos de Empleado si existe
            if (empleadoActual != null) {
                empleadoActual.setSalario_bruto(Double.parseDouble(txtSalario.getText()));
                empleadoActual.setLugar_trabajo(txtLugarTrabajo.getText());
                empleadoActual.setEstadolaboral(cmbEstadoLaboral.getValue());
                empleadoDAO.actualizarEmpleado(empleadoActual);
            }

            mostrarAlerta("Éxito", "Los datos han sido actualizados correctamente.");

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo actualizar la información.");
            e.printStackTrace();
        }
    }

    @FXML
    public void buscarUsuariosPorDocumento() {
        String documento = txtDocumento.getText().trim();

        if (documento.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un documento válido para buscar.");
            return;
        }

        try {
            List<Persona> personas = personaDAO.buscarPersonasPorDocumentoParcial(documento);
            ObservableList<String> items = FXCollections.observableArrayList();

            for (Persona p : personas) {
                items.add(p.getDocumento_identidad() + " - " + p.getNombre());
            }

            listViewUsuarios.setItems(items);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo realizar la búsqueda.");
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtDireccion.clear();
        txtCorreo.clear();
        txtSalario.clear();
        txtLugarTrabajo.clear();
        cmbTipoCliente.setValue(null);
        cmbEstadoLaboral.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
