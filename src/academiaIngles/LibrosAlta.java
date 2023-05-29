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

public class LibrosAlta implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Libros - Alta");
	Label lblAlta = new Label("__________ ALTA DE LIBRO __________");
	Label lblNombre = new Label("Nombre   ");
	TextField txtNombre = new TextField(15);
	Label lblNivel = new Label("Nivel        ");
	TextField txtNivel = new TextField(15);
	Label lblAutor = new Label("Autor       ");
	TextField txtAutor = new TextField(15);
	Label lblEditorial = new Label("Editorial   ");
	TextField txtEditorial = new TextField(15);
	Button btnAceptar = new Button("Aceptar");
	Button btnBorrar = new Button("Borrar");
	
	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");
	
	BDConexion conexion = new BDConexion();
	
	String usuario = "";
	
	LibrosAlta(String u) {
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		// establecer un color del fondo
		ventana.setBackground(new Color(205, 232, 198));
		
		// agregar los componentes a la ventana
		ventana.add(lblAlta);
		ventana.add(lblNombre);
		ventana.add(txtNombre);
		ventana.add(lblNivel);
		ventana.add(txtNivel);
		ventana.add(lblAutor);
		ventana.add(txtAutor);
		ventana.add(lblEditorial);
		ventana.add(txtEditorial);
		btnAceptar.addActionListener(this);
		btnAceptar.addKeyListener(this);
		ventana.add(btnAceptar);
		btnBorrar.addActionListener(this);
		btnBorrar.addKeyListener(this);
		ventana.add(btnBorrar);
		
		ventana.setSize(270, 220);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(e.getSource().equals(btnBorrar)) {
			// borrar todos los campos
			txtNombre.setText("");
			txtNivel.setText("");
			txtAutor.setText("");
			txtEditorial.setText("");
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
			if (txtNombre.getText().length() == 0 || txtNivel.getText().length() == 0 || txtAutor.getText().length() == 0 || txtEditorial.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				
			} 
			// comparar si el nombre del libro introducido corresponde a algún
			// de los nombres de libros en la base de datos
			// para verificar si el libro ya existe en esta BD
			else if (conexion.devolverNombre().contains(txtNombre.getText()))
			{ 
				lblMensaje.setText("Este libro ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				} 
			else
			{
				// Dar de alta
				// montar la sentencia SQL para insertar campos en el registro
				String sentencia = "INSERT INTO libros VALUES (null, '" + txtNombre.getText() + "', '" + txtNivel.getText() + "', '" + txtAutor.getText() + "', '" + txtEditorial.getText() + "');";
				// ejecutar el método de alta pasándole la sentencia como parámetro
				int respuesta = conexion.altaLibro(sentencia);
				// si el resultado (respuesta) del método altaLibro() es 0 - alta correcta
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
					txtNivel.setText("");
					txtAutor.setText("");
					txtEditorial.setText("");
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(btnBorrar)) {
			// borrar todos los campos
			txtNombre.setText("");
			txtNivel.setText("");
			txtAutor.setText("");
			txtEditorial.setText("");
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
			if (txtNombre.getText().length() == 0 || txtNivel.getText().length() == 0 || txtAutor.getText().length() == 0 || txtEditorial.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				
			} 
			// comparar si el nombre del libro introducido corresponde a algún
			// de los nombres de libros en la base de datos
			// para verificar si el libro ya existe en esta BD
			else if (conexion.devolverNombre().contains(txtNombre.getText()))
			{ 
				lblMensaje.setText("Este libro ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				} 
			else
			{
				// Dar de alta
				// montar la sentencia SQL para insertar campos en el registro
				String sentencia = "INSERT INTO libros VALUES (null, '" + txtNombre.getText() + "', '" + txtNivel.getText() + "', '" + txtAutor.getText() + "', '" + txtEditorial.getText() + "');";
				// ejecutar el método de alta pasándole la sentencia como parámetro
				int respuesta = conexion.altaLibro(sentencia);
				// si el resultado (respuesta) del método altaLibro() es 0 - alta correcta
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
					txtNivel.setText("");
					txtAutor.setText("");
					txtEditorial.setText("");
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
	
}
