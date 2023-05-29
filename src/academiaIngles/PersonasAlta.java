package academiaIngles;

import java.awt.Button;
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

public class PersonasAlta implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Personas - Alta");
	Label lblAlta = new Label("__________ ALTA DE PERSONA __________");
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
	Button btnBorrar = new Button("Borrar");
	
	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");
	
	BDConexion conexion = new BDConexion();
	
	String usuario = "";
	
	PersonasAlta(String u) {
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		// establecer un color del fondo
		ventana.setBackground(new Color(205, 232, 198));
		
		// agregar los componentes a la ventana
		ventana.add(lblAlta);
		ventana.add(lblNombre);
		ventana.add(txtNombre);
		ventana.add(lblPrimerApellido);
		ventana.add(txtPrimerApellido);
		ventana.add(lblSegundoApellido);
		ventana.add(txtSegundoApellido);
		ventana.add(lblTelefono);
		ventana.add(txtTelefono);
		ventana.add(lblCorreoElectronico);
		ventana.add(txtCorreoElectronico);
		ventana.add(lblDireccion);
		ventana.add(txtDireccion);
		btnAceptar.addActionListener(this);
		btnAceptar.addKeyListener(this);
		ventana.add(btnAceptar);
		btnBorrar.addActionListener(this);
		btnBorrar.addKeyListener(this);
		ventana.add(btnBorrar);
		
		ventana.setSize(300, 270);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(btnBorrar)) {
			// borrar todos los campos
			txtNombre.setText("");
			txtPrimerApellido.setText("");
			txtSegundoApellido.setText("");
			txtTelefono.setText("");
			txtCorreoElectronico.setText("");
			txtDireccion.setText("");
			// obtener el enfoco en el primer campo
			txtNombre.requestFocus();
		}
		else if (e.getSource().equals(btnAceptar)) {
			// montar los diálogos de Error y de Exito
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

			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtPrimerApellido.getText().length() == 0 || txtSegundoApellido.getText().length() == 0 || txtTelefono.getText().length() == 0 || txtCorreoElectronico.getText().length() == 0 || txtDireccion.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				
			} 
			// comparar si el correo electrónico introducido corresponde a algún
			// de los correos electrónicos en la base de datos
			// para verificar si la persona ya existe en esta BD (según su correo electrónico)
			else if (conexion.devolverCorreo().contains(txtCorreoElectronico.getText()))
			{ 
				lblMensaje.setText("Esta persona ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				} 
			else // dar de alta
			{
				// montar la sentencia SQL para insertar campos en el registro
				String sentencia = "INSERT INTO personas VALUES (null, '" + txtNombre.getText() + "', '" + txtPrimerApellido.getText() + "', '" + txtSegundoApellido.getText() + "', " + txtTelefono.getText() +  ", '" + txtCorreoElectronico.getText() +  "', '" + txtDireccion.getText() + "');";
				// ejecutar el método de alta pasándole la sentencia como parámetro
				int respuesta = conexion.altaPersona(sentencia);
				// si el resultado (respuesta) del método altaPersona() es 0 - alta correcta
				// si el resultado (respuesta) es distinto de 0 mostrar mensaje de error
				if (respuesta != 0)
				{
					lblMensaje.setText("Ha ocurrido un error.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				} else // alta correcta
				{
					conexion.apunteLog(usuario, sentencia);
					// monstrar un diálogo con mensaje de alta correcta
					lblMensaje.setText("  La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
					// borrar los campos
					txtNombre.setText("");
					txtPrimerApellido.setText("");
					txtSegundoApellido.setText("");
					txtTelefono.setText("");
					txtCorreoElectronico.setText("");
					txtDireccion.setText("");
					txtNombre.requestFocus();
				}
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (dlgMensajeError.isActive() || dlgMensajeExito.isActive())
		{
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
			
		} else
		{
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

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(e.getSource().equals(btnBorrar)) {
			// borrar todos los campos
			txtNombre.setText("");
			txtPrimerApellido.setText("");
			txtSegundoApellido.setText("");
			txtTelefono.setText("");
			txtCorreoElectronico.setText("");
			txtDireccion.setText("");
			// obtener el enfoco en el primer campo
			txtNombre.requestFocus();
		}
		else if (e.getSource().equals(btnAceptar)) {
			// montar los diálogos de Error y de Exito
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

			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtPrimerApellido.getText().length() == 0 || txtSegundoApellido.getText().length() == 0 || txtTelefono.getText().length() == 0 || txtCorreoElectronico.getText().length() == 0 || txtDireccion.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				
			} 
			// si la persona ya existe en esta BD
			else if (conexion.devolverCorreo().contains(txtCorreoElectronico.getText()))
			{ 
				lblMensaje.setText("Esta persona ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				} 
			else // dar de alta
			{
				// montar la sentencia SQL para insertar campos en el registro
				String sentencia = "INSERT INTO personas VALUES (null, '" + txtNombre.getText() + "', '" + txtPrimerApellido.getText() + "', '" + txtSegundoApellido.getText() + "', " + txtTelefono.getText() +  ", '" + txtCorreoElectronico.getText() +  "', '" + txtDireccion.getText() + "');";
				// ejecutar el método de alta pasándole la sentencia como parámetro
				int respuesta = conexion.altaPersona(sentencia);
				// si el resultado (respuesta) del método altaPersona() es 0 - alta correcta
				// si el resultado (respuesta) es distinto de 0 mostrar mensaje de error
				if (respuesta != 0)
				{
					lblMensaje.setText("Ha ocurrido un error.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				} else // alta correcta
				{
					conexion.apunteLog(usuario, sentencia);
					// monstrar un diálogo con mensaje de alta correcta
					lblMensaje.setText("  La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
					// borrar los campos
					txtNombre.setText("");
					txtPrimerApellido.setText("");
					txtSegundoApellido.setText("");
					txtTelefono.setText("");
					txtCorreoElectronico.setText("");
					txtDireccion.setText("");
					txtNombre.requestFocus();
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{}

	@Override
	public void keyReleased(KeyEvent e)
	{}
	
}
