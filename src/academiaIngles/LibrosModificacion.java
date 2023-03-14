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

public class LibrosModificacion implements WindowListener, ActionListener, KeyListener
{
	// componentes de la ventana de elegir un libro a modificar
	Frame ventana = new Frame("Libros - Modificación");
	Label lblElegir = new Label("Elige al libro a modificar:");
	Choice choLibros = new Choice();
	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");
	
	// componentes de diálogo de la modificación
	Dialog dlgModificacion = new Dialog(ventana, "Libros - Modificación", true);
	Label lblModificacion = new Label("______ MODIFICACIÓN DE LIBRO ______");
	Label lblIdentificador = new Label("Identificador");
	TextField txtIdentificador = new TextField(15);
	Label lblNombre = new Label("Nombre     ");
	TextField txtNombre = new TextField(15);
	Label lblNivel = new Label("Nivel          ");
	TextField txtNivel = new TextField(15);
	Label lblAutor = new Label("Autor         ");
	TextField txtAutor = new TextField(15);
	Label lblEditorial = new Label("Editorial     ");
	TextField txtEditorial = new TextField(15);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar2 = new Button("Cancelar");
	
	// diálogo de confirmación
	Dialog dlgConfirmar = new Dialog(ventana, "", true);
	Label lblConfirmar = new Label("¿Estás seguro que quieres modificar...");
	TextField txtLibro = new TextField(20);
	Label lblConfirmar2 = new Label("?");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	
	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");
	
	BDConexion conexion = new BDConexion();
	
	LibrosModificacion() {
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventana.setBackground(new Color(249, 245, 198));
		
		// montar una ventana donde se puede elegir un registro
		// dentro de un choice (usando el mismo método de rellenarChoiceLibros que en la baja)
		ventana.add(lblElegir);
		conexion.rellenarChoiceLibros(choLibros);
		ventana.add(choLibros);
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
			if (choLibros.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choLibros -> 'Selecciona el libro a modificar...'
				// un array con los campos del libro elegido en el choice
				String tablaLibros[] = choLibros.getSelectedItem().split(" ");
				String idLibro = tablaLibros[0]; // el primer campo - idLibro
				// obtener una cadena con todos los datos del libro según el id pasado como parámetro
				String datosLibro = conexion.editarLibro(idLibro);
				// separar los campos del registro
				String[] tablaDatosLibro = datosLibro.split("-");
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblIdentificador);
				// poner el id en el campo de Identificador
				txtIdentificador.setText(tablaDatosLibro[0]);
				// bloquear la edición de id
				txtIdentificador.setEditable(false);
				dlgModificacion.add(txtIdentificador);
				dlgModificacion.add(lblNombre);
				txtNombre.setText(tablaDatosLibro[1]);
				dlgModificacion.add(txtNombre);
				dlgModificacion.add(lblNivel);
				txtNivel.setText(tablaDatosLibro[2]);
				dlgModificacion.add(txtNivel);
				dlgModificacion.add(lblAutor);
				txtAutor.setText(tablaDatosLibro[3]);
				dlgModificacion.add(txtAutor);
				dlgModificacion.add(lblEditorial);
				txtEditorial.setText(tablaDatosLibro[4]);
				dlgModificacion.add(txtEditorial);
				btnAceptar.addActionListener(this);
				btnAceptar.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				btnCancelar2.addActionListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnCancelar2);
			
				dlgModificacion.setSize(270, 245);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
					
			}
			// si está seleccionado la primera opción del choice
			else {
				lblMensaje.setText("No has elegido ningún libro.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		}
		else if (e.getSource().equals(btnCancelar2)) {
			dlgModificacion.setVisible(false);
		}
		else if (e.getSource().equals(btnAceptar)) {
			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtNivel.getText().length() == 0 || txtAutor.getText().length() == 0 || txtEditorial.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			else {
				// mostrar un diálogo de confirmación
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(249, 245, 198));
				dlgConfirmar.setSize(240, 120);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				txtLibro.setText(txtNombre.getText());
				txtLibro.setEditable(false);
				dlgConfirmar.add(txtLibro);
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
		// actualizar los valores del libro elegido
		else if (e.getSource().equals(btnSi)) {
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE libros SET nombreLibro = '" + txtNombre.getText()
			+ "', nivelLibro = '" + txtNivel.getText() + "', autorLibro = '" + txtAutor.getText() + "', editorialLibro = '" + txtEditorial.getText() + "' WHERE idLibro = " + txtIdentificador.getText() + ";";
			
			// método de modificar un libro donde pasamos la sentencia como parámetro y que devuelve un número entero
			// según si se ejecuta correctamente
			int respuesta = conexion.modificarLibro(sentencia);
			if (respuesta != 0)
			{
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			} 
			// si el método de modificar devuelve un 0, todo se ha ejecutado bien
			else
			{
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				lblMensaje.setText("La operación se ha ejecutado correctamente.");
				dlgMensajeExito.add(lblMensaje);
				dlgMensajeExito.setVisible(true);
				conexion.rellenarChoiceLibros(choLibros);
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
			if (choLibros.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choLibros -> 'Selecciona el libro a modificar...'
				// un array con los campos del libro elegido en el choice
				String tablaLibros[] = choLibros.getSelectedItem().split(" ");
				String idLibro = tablaLibros[0]; // el primer campo - idLibro
				// obtener una cadena con todos los datos del libro según el id pasado como parámetro
				String datosLibro = conexion.editarLibro(idLibro);
				// separar los campos del registro
				String[] tablaDatosLibro = datosLibro.split("-");
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblIdentificador);
				// poner el id en el campo de Identificador
				txtIdentificador.setText(tablaDatosLibro[0]);
				// bloquear la edición de id
				txtIdentificador.setEditable(false);
				dlgModificacion.add(txtIdentificador);
				dlgModificacion.add(lblNombre);
				txtNombre.setText(tablaDatosLibro[1]);
				dlgModificacion.add(txtNombre);
				dlgModificacion.add(lblNivel);
				txtNivel.setText(tablaDatosLibro[2]);
				dlgModificacion.add(txtNivel);
				dlgModificacion.add(lblAutor);
				txtAutor.setText(tablaDatosLibro[3]);
				dlgModificacion.add(txtAutor);
				dlgModificacion.add(lblEditorial);
				txtEditorial.setText(tablaDatosLibro[4]);
				dlgModificacion.add(txtEditorial);
				btnAceptar.addActionListener(this);
				btnAceptar.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				btnCancelar2.addActionListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnCancelar2);
			
				dlgModificacion.setSize(270, 245);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
					
			}
			// si está seleccionado la primera opción del choice
			else {
				lblMensaje.setText("No has elegido ningún libro.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		}
		else if (e.getSource().equals(btnCancelar2)) {
			dlgModificacion.setVisible(false);
		}
		else if (e.getSource().equals(btnAceptar)) {
			// si algún campo está vacío
			if (txtNombre.getText().length() == 0 || txtNivel.getText().length() == 0 || txtAutor.getText().length() == 0 || txtEditorial.getText().length() == 0)
			{
				lblMensaje.setText("Los campos están vacíos.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
			else {
				// mostrar un diálogo de confirmación
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(249, 245, 198));
				dlgConfirmar.setSize(240, 120);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				txtLibro.setText(txtNombre.getText());
				txtLibro.setEditable(false);
				dlgConfirmar.add(txtLibro);
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
		// actualizar los valores del libro elegido
		else if (e.getSource().equals(btnSi)) {
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE libros SET nombreLibro = '" + txtNombre.getText()
			+ "', nivelLibro = '" + txtNivel.getText() + "', autorLibro = '" + txtAutor.getText() + "', editorialLibro = '" + txtEditorial.getText() + "' WHERE idLibro = " + txtIdentificador.getText() + ";";
			
			// método de modificar un libro donde pasamos la sentencia como parámetro y que devuelve un número entero
			// según si se ejecuta correctamente
			int respuesta = conexion.modificarLibro(sentencia);
			if (respuesta != 0)
			{
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			} 
			// si el método de modificar devuelve un 0, todo se ha ejecutado bien
			else
			{
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				lblMensaje.setText("La operación se ha ejecutado correctamente.");
				dlgMensajeExito.add(lblMensaje);
				dlgMensajeExito.setVisible(true);
				conexion.rellenarChoiceLibros(choLibros);
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (dlgMensajeError.isActive() || dlgMensajeExito.isActive()) {
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
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
