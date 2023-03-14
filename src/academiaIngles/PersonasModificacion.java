package academiaIngles;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PersonasModificacion implements WindowListener, ActionListener, KeyListener
{
	// componentes de la ventana de elegir una persona a modificar
	Frame ventana = new Frame("Personas - Modificación");
	Label lblElegir = new Label("Elige a la persona a modificar:");
	Choice choPersonas = new Choice();
	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");
	
	// componentes de diálogo de la modificación
	Dialog dlgModificacion = new Dialog(ventana, "Personas - Modificación", true);
	Label lblModificacion = new Label("______ MODIFICACIÓN DE PERSONA ______");
	Label lblIdentificador = new Label("Identificador          ");
	TextField txtIdentificador = new TextField(15);
	Label lblNombre = new Label("Nombre                ");
	TextField txtNombre = new TextField(15);
	Label lblPrimerApellido = new Label("Primer Apellido     ");
	TextField txtPrimerApellido = new TextField(15);
	Label lblSegundoApellido = new Label("Segundo Apellido  ");
	TextField txtSegundoApellido = new TextField(15);
	Label lblTelefono = new Label("Teléfono                ");
	TextField txtTelefono = new TextField(15);
	Label lblCorreoElectronico = new Label("Correo Electrónico");
	TextField txtCorreoElectronico = new TextField(15);
	Label lblDireccion = new Label("Dirección               ");
	TextField txtDireccion = new TextField(15);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar2 = new Button("Cancelar");
	
	// diálogo de confirmación
	Dialog dlgConfirmar = new Dialog(ventana, "", true);
	Label lblConfirmar = new Label("¿Estás seguro que quieres modificar...");
	TextField txtPersona = new TextField(20);
	Label lblConfirmar2 = new Label("?");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	
	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");
	
	BDConexion conexion = new BDConexion();
	
	PersonasModificacion() {
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventana.setBackground(new Color(249, 245, 198));
		
		// montar una ventana donde se puede elegir un registro
		// dentro de un choice (usando el mismo método de rellenarChoicePersonas que en la baja)
		ventana.add(lblElegir);
		conexion.rellenarChoicePersonas(choPersonas);
		ventana.add(choPersonas);
		btnModificar.addActionListener(this);
		btnModificar.addKeyListener(this);
		ventana.add(btnModificar);
		btnCancelar.addActionListener(this);
		btnCancelar.addKeyListener(this);
		ventana.add(btnCancelar);
		
		ventana.setSize(265, 170);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
		
		// montar los diálogos de error o de éxito
		dlgMensajeError.setLayout(new FlowLayout());
		dlgMensajeError.setSize(265, 100);
		dlgMensajeError.addWindowListener(this);
		dlgMensajeError.setResizable(false);
		dlgMensajeError.setLocationRelativeTo(null);
		dlgMensajeExito.setLayout(new FlowLayout());
		dlgMensajeExito.setSize(265, 100);
		dlgMensajeExito.addWindowListener(this);
		dlgMensajeExito.setResizable(false);
		dlgMensajeExito.setLocationRelativeTo(null);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(e.getSource().equals(btnCancelar)) {
			ventana.setVisible(false);
		}
		else if(e.getSource().equals(btnModificar)) {
			if (choPersonas.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choPersonas -> 'Selecciona la persona a modificar...'
				// un array con los campos de la persona elegida en el choice
				String tabla[] = choPersonas.getSelectedItem().split(" "); 
				String idPersona = tabla[0]; // el primer campo - idPersona
				// obtener una cadena con todos los datos de la persona según el id pasado como parámetro
				String datosPersona = conexion.editarPersona(idPersona);
				// separar los campos del registro
				String[] tablaDatos = datosPersona.split("-");
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblIdentificador);
				// poner el id en el campo de Identificador
				txtIdentificador.setText(tablaDatos[0]);
				// bloquear la edición de id
				txtIdentificador.setEditable(false);
				dlgModificacion.add(txtIdentificador);
				dlgModificacion.add(lblNombre);
				// poner el nombre en el campo de Nombre
				txtNombre.setText(tablaDatos[1]);
				dlgModificacion.add(txtNombre);
				dlgModificacion.add(lblPrimerApellido);
				txtPrimerApellido.setText(tablaDatos[2]);
				dlgModificacion.add(txtPrimerApellido);
				dlgModificacion.add(lblSegundoApellido);
				txtSegundoApellido.setText(tablaDatos[3]);
				dlgModificacion.add(txtSegundoApellido);
				dlgModificacion.add(lblTelefono);
				txtTelefono.setText(tablaDatos[4]);
				dlgModificacion.add(txtTelefono);
				dlgModificacion.add(lblCorreoElectronico);
				txtCorreoElectronico.setText(tablaDatos[5]);
				dlgModificacion.add(txtCorreoElectronico);
				dlgModificacion.add(lblDireccion);
				txtDireccion.setText(tablaDatos[6]);
				dlgModificacion.add(txtDireccion);
				btnAceptar.addActionListener(this);
				btnAceptar.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				btnCancelar2.addActionListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnCancelar2);
			
				dlgModificacion.setSize(300, 290);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
					
			}
			// si está seleccionado la primera opción del choice
			else {
				lblMensaje.setText("No has elegido ninguna persona.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		}
		else if (e.getSource().equals(btnCancelar2)) {
			dlgModificacion.setVisible(false);
		}
		else if (e.getSource().equals(btnAceptar)) {
			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtPrimerApellido.getText().length() == 0
					|| txtSegundoApellido.getText().length() == 0 || txtTelefono.getText().length() == 0 || txtCorreoElectronico.getText().length() == 0 || txtDireccion.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			// mostrar un diálogo de confirmación
			else {
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(249, 245, 198));
				dlgConfirmar.setSize(240, 127);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				txtPersona.setText(txtNombre.getText() + " " + txtPrimerApellido.getText() + " " + txtSegundoApellido.getText());
				txtPersona.setEditable(false);
				dlgConfirmar.add(txtPersona);
				dlgConfirmar.add(lblConfirmar2);
				btnSi.addActionListener(this);
				btnSi.addKeyListener(this);
				dlgConfirmar.add(btnSi);
				btnNo.addActionListener(this);
				btnNo.addKeyListener(this);
				dlgConfirmar.add(btnNo);
				
				dlgConfirmar.setVisible(true);
			}
		}
		// volver al diálogo de modificación
		else if (e.getSource().equals(btnNo)) {
			dlgConfirmar.setVisible(false);
		}
		// actualizar los valores de la persona elegida
		else if (e.getSource().equals(btnSi)) {
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE personas SET nombrePersona = '" + txtNombre.getText()
			+ "', primerApellidoPersona = '" + txtPrimerApellido.getText() + "', segundoApellidoPersona = '"
			+ txtSegundoApellido.getText() + "', telPersona = '" + txtTelefono.getText() + "', correoElectronicoPersona = '" + txtCorreoElectronico.getText() + "', direccionPersona = '" + txtDireccion.getText() + "' WHERE idPersona = " + txtIdentificador.getText() + ";";
			
			// método de modificar una persona donde pasamos la sentencia como parámetro y que devuelve un número entero
			// según si se ejecuta correctamente
			int respuesta = conexion.modificarPersona(sentencia);
			if (respuesta != 0)
			{
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			// si el método de modificar devuelve un 0, todo se ha ejecutado bien
			else if (respuesta == 0)
			{
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				lblMensaje.setText("La operación se ha ejecutado correctamente.");
				dlgMensajeExito.add(lblMensaje);
				dlgMensajeExito.setVisible(true);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{}

	@Override
	public void keyReleased(KeyEvent e)
	{}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(btnCancelar)) {
			ventana.setVisible(false);
		}
		else if(e.getSource().equals(btnModificar)) {
			if (choPersonas.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choPersonas -> 'Selecciona la persona a modificar...'
				// un array con los campos de la persona elegida en el choice
				String tabla[] = choPersonas.getSelectedItem().split(" "); 
				String idPersona = tabla[0]; // el primer campo - idPersona
				// obtener una cadena con todos los datos de la persona según el id pasado como parámetro
				String datosPersona = conexion.editarPersona(idPersona);
				// separar los campos del registro
				String[] tablaDatos = datosPersona.split("-");
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblIdentificador);
				// poner el id en el campo de Identificador
				txtIdentificador.setText(tablaDatos[0]);
				// bloquear la edición de id
				txtIdentificador.setEditable(false);
				dlgModificacion.add(txtIdentificador);
				dlgModificacion.add(lblNombre);
				// poner el nombre en el campo de Nombre
				txtNombre.setText(tablaDatos[1]);
				dlgModificacion.add(txtNombre);
				dlgModificacion.add(lblPrimerApellido);
				txtPrimerApellido.setText(tablaDatos[2]);
				dlgModificacion.add(txtPrimerApellido);
				dlgModificacion.add(lblSegundoApellido);
				txtSegundoApellido.setText(tablaDatos[3]);
				dlgModificacion.add(txtSegundoApellido);
				dlgModificacion.add(lblTelefono);
				txtTelefono.setText(tablaDatos[4]);
				dlgModificacion.add(txtTelefono);
				dlgModificacion.add(lblCorreoElectronico);
				txtCorreoElectronico.setText(tablaDatos[5]);
				dlgModificacion.add(txtCorreoElectronico);
				dlgModificacion.add(lblDireccion);
				txtDireccion.setText(tablaDatos[6]);
				dlgModificacion.add(txtDireccion);
				btnAceptar.addActionListener(this);
				btnAceptar.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				btnCancelar2.addActionListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnCancelar2);
			
				dlgModificacion.setSize(300, 290);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
					
			}
			// si está seleccionado la primera opción del choice
			else {
				lblMensaje.setText("No has elegido ninguna persona.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		}
		else if (e.getSource().equals(btnCancelar2)) {
			dlgModificacion.setVisible(false);
		}
		else if (e.getSource().equals(btnAceptar)) {
			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtPrimerApellido.getText().length() == 0
					|| txtSegundoApellido.getText().length() == 0 || txtTelefono.getText().length() == 0 || txtCorreoElectronico.getText().length() == 0 || txtDireccion.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			// mostrar un diálogo de confirmación
			else {
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(249, 245, 198));
				dlgConfirmar.setSize(240, 127);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				txtPersona.setText(txtNombre.getText() + " " + txtPrimerApellido.getText() + " " + txtSegundoApellido.getText());
				txtPersona.setEditable(false);
				dlgConfirmar.add(txtPersona);
				dlgConfirmar.add(lblConfirmar2);
				btnSi.addActionListener(this);
				btnSi.addKeyListener(this);
				dlgConfirmar.add(btnSi);
				btnNo.addActionListener(this);
				btnNo.addKeyListener(this);
				dlgConfirmar.add(btnNo);
				
				dlgConfirmar.setVisible(true);
			}
		}
		// volver al diálogo de modificación
		else if (e.getSource().equals(btnNo)) {
			dlgConfirmar.setVisible(false);
		}
		// actualizar los valores de la persona elegida
		else if (e.getSource().equals(btnSi)) {
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE personas SET nombrePersona = '" + txtNombre.getText()
			+ "', primerApellidoPersona = '" + txtPrimerApellido.getText() + "', segundoApellidoPersona = '"
			+ txtSegundoApellido.getText() + "', telPersona = '" + txtTelefono.getText() + "', correoElectronicoPersona = '" + txtCorreoElectronico.getText() + "', direccionPersona = '" + txtDireccion.getText() + "' WHERE idPersona = " + txtIdentificador.getText() + ";";
			
			// método de modificar una persona donde pasamos la sentencia como parámetro y que devuelve un número entero
			// según si se ejecuta correctamente
			int respuesta = conexion.modificarPersona(sentencia);
			
			if (respuesta != 0)
			{
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			// si el método de modificar devuelve un 0, todo se ha ejecutado bien
			else if (respuesta == 0)
			{
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				lblMensaje.setText("La operación se ha ejecutado correctamente.");
				dlgMensajeExito.add(lblMensaje);
				dlgMensajeExito.setVisible(true);
			}
			dlgConfirmar.setVisible(false);
			dlgModificacion.setVisible(false);
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (dlgMensajeError.isActive() || dlgMensajeExito.isActive()) {
			dlgConfirmar.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
			conexion.rellenarChoicePersonas(choPersonas);
		}
		else if (dlgConfirmar.isActive()) {
			dlgConfirmar.setVisible(false);
		}
		else if (dlgModificacion.isActive()) {
			dlgModificacion.setVisible(false);
		}
		else if (ventana.isActive()) {
			ventana.setVisible(false);
		}
	}

	@Override
	public void windowClosed(WindowEvent e)
	{}

	@Override
	public void windowIconified(WindowEvent e)
	{}

	@Override
	public void windowDeiconified(WindowEvent e)
	{}

	@Override
	public void windowActivated(WindowEvent e)
	{}

	@Override
	public void windowDeactivated(WindowEvent e)
	{}
	
}
