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

public class PersonasLibrosModificacion implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Personas - Libros - Modificación");
	Label lblElegir = new Label("Elige a la relación a modificar:");
	Choice choPersonasLibros = new Choice();
	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");

	Dialog dlgModificacion = new Dialog(ventana, "Personas-Libros - Modificación", true);
	Label lblModificacion = new Label("MODIFICACIÓN DE LA RELACIÓN PERSONA-LIBRO");
	Label lblPersonaModificacion = new Label("                               Persona:                               ");
	Choice choPersonasFK = new Choice();
	Label lblLibroModificacion = new Label("                              Libro:                              ");
	Choice choLibrosFK = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar2 = new Button("Cancelar");

	// diálogo de confirmación
	Dialog dlgConfirmar = new Dialog(ventana, "", true);
	Label lblConfirmar = new Label("¿Estás seguro que quieres modificar...");
	TextField txtPersonaLibroOld = new TextField(40);
	Label lblA = new Label("a...");
	TextField txtPersonaLibroNew = new TextField(40);
	Label lblConfirmar2 = new Label("?");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");

	BDConexion conexion = new BDConexion();

	String usuario = "";

	public PersonasLibrosModificacion(String u)
	{
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventana.setBackground(new Color(249, 245, 198));

		// montar una ventana donde se puede elegir un registro dentro de un choice
		// (usando el mismo método de rellenarChoicePersonasLibros que en la baja)
		ventana.add(lblElegir);
		conexion.rellenarChoicePersonasLibros(choPersonasLibros);
		ventana.add(choPersonasLibros);
		btnModificar.addActionListener(this);
		btnModificar.addKeyListener(this);
		ventana.add(btnModificar);
		btnCancelar.addActionListener(this);
		btnCancelar.addKeyListener(this);
		ventana.add(btnCancelar);

		ventana.setSize(540, 150);
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
		String idPersonaLibro;
		if (e.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		} else if (e.getSource().equals(btnCancelar2))
		{
			dlgModificacion.setVisible(false);
		}

		else if (e.getSource().equals(btnModificar))
		{
			if (choPersonasLibros.getSelectedIndex() != 0)
			{
				// 0 es el primer elemento en choPersonasLibros -> 'Selecciona la relación a
				// modificar...'
				// un array con los campos de la relación elegida en el choice
				String tabla[] = choPersonasLibros.getSelectedItem().split(" ");
				idPersonaLibro = tabla[0]; // el primer campo - idPersonaLibro
				// obtener una cadena con todos los datos de la relación según el id pasado como
				// parámetro
				String datosPersonaLibro = conexion.editarPersonaLibro(idPersonaLibro);
				// separar los campos del registro
				String[] tablaDatos = datosPersonaLibro.split("-");
				String idPersonaFK = tablaDatos[1];
				String idLibroFK = tablaDatos[2];
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblPersonaModificacion);
				conexion.rellenarChoicePersonasFK(choPersonasFK, idPersonaFK);
				dlgModificacion.add(choPersonasFK);
				dlgModificacion.add(lblLibroModificacion);
				conexion.rellenarChoiceLibrosFK(choLibrosFK, idLibroFK);
				dlgModificacion.add(choLibrosFK);
				btnAceptar.addActionListener(this);
				btnCancelar2.addActionListener(this);
				btnAceptar.addKeyListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				dlgModificacion.add(btnCancelar2);
				dlgModificacion.setSize(420, 220);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
			}
			// si está seleccionado la primera opción del choice
			else
			{
				lblMensaje.setText("No has elegido ninguna relación.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		} else if (e.getSource().equals(btnCancelar2))
		{
			dlgModificacion.setVisible(false);
		} else if (e.getSource().equals(btnAceptar))
		{
			// mostrar un diálogo de confirmación
			dlgConfirmar.setLayout(new FlowLayout());
			dlgConfirmar.addWindowListener(this);
			dlgConfirmar.setBackground(new Color(249, 245, 198));
			dlgConfirmar.setSize(380, 200);
			dlgConfirmar.setResizable(false);
			dlgConfirmar.setLocationRelativeTo(null);
			dlgConfirmar.add(lblConfirmar);
			txtPersonaLibroOld.setText(choPersonasLibros.getSelectedItem());
			txtPersonaLibroOld.setEditable(false);
			dlgConfirmar.add(txtPersonaLibroOld);
			dlgConfirmar.add(lblA);
			txtPersonaLibroNew
					.setText(choPersonasFK.getSelectedItem() + "   " + choLibrosFK.getSelectedItem().substring(2));
			txtPersonaLibroNew.setEditable(false);
			dlgConfirmar.add(txtPersonaLibroNew);
			dlgConfirmar.add(lblConfirmar2);
			btnSi.addActionListener(this);
			btnSi.addKeyListener(this);
			dlgConfirmar.add(btnSi);
			btnNo.addActionListener(this);
			btnNo.addKeyListener(this);
			dlgConfirmar.add(btnNo);
			dlgConfirmar.setVisible(true);
		}
		// volver al diálogo de modificación
		else if (e.getSource().equals(btnNo))
		{
			dlgConfirmar.setVisible(false);
		}
		// actualizar los valores de la relación elegida
		else if (e.getSource().equals(btnSi))

		{
			String tabla[] = choPersonasLibros.getSelectedItem().split(" ");
			idPersonaLibro = tabla[0];
			String tablaP[] = choPersonasFK.getSelectedItem().split(" ");
			String idPersona = tablaP[0];
			String tablaL[] = choLibrosFK.getSelectedItem().split(" ");
			String idLibro = tablaL[0];
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE personasLibros SET idPersonaFK = " + idPersona + ", idLibroFK = " + idLibro
					+ " WHERE idPersonaLibro = " + idPersonaLibro + ";";
			// si todavia no existe este registro...
			String resultado = conexion.comprobarExistencia(idPersona, idLibro);
			if (resultado != "")
			{
				lblMensaje.setText("Esta relación ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
			}
			// método de modificar una relación donde pasamos la sentencia como parámetro y
			// que devuelve un número entero
			// según si se ejecuta correctamente
			else
			{
				int respuesta = conexion.modificarPersonaLibro(sentencia);
				if (respuesta != 0)
				{
					lblMensaje.setText("Ha ocurrido un error.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				}
				// si el método de modificar devuelve un 0, todo se ha ejecutado bien
				else if (respuesta == 0)
				{
					conexion.apunteLog(usuario, sentencia);
					dlgConfirmar.setVisible(false);
					dlgModificacion.setVisible(false);
					lblMensaje.setText("La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
					dlgMensajeError.setVisible(false);
					dlgConfirmar.setVisible(false);
					dlgModificacion.setVisible(false);
				}
				dlgMensajeExito.setVisible(false);
				dlgMensajeError.setVisible(false);
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				conexion.rellenarChoicePersonasLibros(choPersonasLibros);
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String idPersonaLibro;
		if (e.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		} else if (e.getSource().equals(btnCancelar2))
		{
			dlgModificacion.setVisible(false);
		}

		else if (e.getSource().equals(btnModificar))
		{
			if (choPersonasLibros.getSelectedIndex() != 0)
			{
				// 0 es el primer elemento en choPersonasLibros -> 'Selecciona la relación a
				// modificar...'
				// un array con los campos de la relación elegida en el choice
				String tabla[] = choPersonasLibros.getSelectedItem().split(" ");
				idPersonaLibro = tabla[0]; // el primer campo - idPersonaLibro
				// obtener una cadena con todos los datos de la relación según el id pasado como
				// parámetro
				String datosPersonaLibro = conexion.editarPersonaLibro(idPersonaLibro);
				// separar los campos del registro
				String[] tablaDatos = datosPersonaLibro.split("-");
				String idPersonaFK = tablaDatos[1];
				String idLibroFK = tablaDatos[2];
				dlgModificacion.setLayout(new FlowLayout());
				dlgModificacion.addWindowListener(this);
				dlgModificacion.setBackground(new Color(249, 245, 198));
				dlgModificacion.add(lblModificacion);
				dlgModificacion.add(lblPersonaModificacion);
				conexion.rellenarChoicePersonasFK(choPersonasFK, idPersonaFK);
				dlgModificacion.add(choPersonasFK);
				dlgModificacion.add(lblLibroModificacion);
				conexion.rellenarChoiceLibrosFK(choLibrosFK, idLibroFK);
				dlgModificacion.add(choLibrosFK);
				btnAceptar.addActionListener(this);
				btnCancelar2.addActionListener(this);
				btnAceptar.addKeyListener(this);
				btnCancelar2.addKeyListener(this);
				dlgModificacion.add(btnAceptar);
				dlgModificacion.add(btnCancelar2);
				dlgModificacion.setSize(420, 220);
				dlgModificacion.setResizable(false);
				dlgModificacion.setLocationRelativeTo(null);
				dlgModificacion.setVisible(true);
			}
			// si está seleccionado la primera opción del choice
			else
			{
				lblMensaje.setText("No has elegido ninguna relación.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		} else if (e.getSource().equals(btnCancelar2))
		{
			dlgModificacion.setVisible(false);
		} else if (e.getSource().equals(btnAceptar))
		{
			// mostrar un diálogo de confirmación
			dlgConfirmar.setLayout(new FlowLayout());
			dlgConfirmar.addWindowListener(this);
			dlgConfirmar.setBackground(new Color(249, 245, 198));
			dlgConfirmar.setSize(380, 200);
			dlgConfirmar.setResizable(false);
			dlgConfirmar.setLocationRelativeTo(null);
			dlgConfirmar.add(lblConfirmar);
			txtPersonaLibroOld.setText(choPersonasLibros.getSelectedItem());
			txtPersonaLibroOld.setEditable(false);
			dlgConfirmar.add(txtPersonaLibroOld);
			dlgConfirmar.add(lblA);
			txtPersonaLibroNew
					.setText(choPersonasFK.getSelectedItem() + "   " + choLibrosFK.getSelectedItem().substring(2));
			txtPersonaLibroNew.setEditable(false);
			dlgConfirmar.add(txtPersonaLibroNew);
			dlgConfirmar.add(lblConfirmar2);
			btnSi.addActionListener(this);
			btnSi.addKeyListener(this);
			dlgConfirmar.add(btnSi);
			btnNo.addActionListener(this);
			btnNo.addKeyListener(this);
			dlgConfirmar.add(btnNo);
			dlgConfirmar.setVisible(true);
		}
		// volver al diálogo de modificación
		else if (e.getSource().equals(btnNo))
		{
			dlgConfirmar.setVisible(false);
		}
		// actualizar los valores de la relación elegida
		else if (e.getSource().equals(btnSi))

		{
			String tabla[] = choPersonasLibros.getSelectedItem().split(" ");
			idPersonaLibro = tabla[0];
			String tablaP[] = choPersonasFK.getSelectedItem().split(" ");
			String idPersona = tablaP[0];
			String tablaL[] = choLibrosFK.getSelectedItem().split(" ");
			String idLibro = tablaL[0];
			// crear una sentencia SQL para la actualización
			String sentencia = "UPDATE personasLibros SET idPersonaFK = " + idPersona + ", idLibroFK = " + idLibro
					+ " WHERE idPersonaLibro = " + idPersonaLibro + ";";
			// si todavia no existe este registro...
			String resultado = conexion.comprobarExistencia(idPersona, idLibro);
			if (resultado != "")
			{
				lblMensaje.setText("Esta relación ya existe.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
			}
				// método de modificar una relación donde pasamos la sentencia como parámetro y
				// que devuelve un número entero según si se ejecuta correctamente
			else
			{
				int respuesta = conexion.modificarPersonaLibro(sentencia);
				if (respuesta != 0)
				{
					lblMensaje.setText("Ha ocurrido un error.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				}
				// si el método de modificar devuelve un 0, todo se ha ejecutado bien
				else if (respuesta == 0)
				{
					conexion.apunteLog(usuario, sentencia);
					dlgConfirmar.setVisible(false);
					dlgModificacion.setVisible(false);
					lblMensaje.setText("La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
					dlgMensajeError.setVisible(false);
					dlgConfirmar.setVisible(false);
					dlgModificacion.setVisible(false);
				}
				dlgMensajeExito.setVisible(false);
				dlgMensajeError.setVisible(false);
				dlgConfirmar.setVisible(false);
				dlgModificacion.setVisible(false);
				conexion.rellenarChoicePersonasLibros(choPersonasLibros);
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (dlgMensajeError.isActive() || dlgMensajeExito.isActive())
		{
			dlgConfirmar.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
			conexion.rellenarChoicePersonasFK(choPersonasFK, choPersonasFK.getSelectedIndex() + "");
			conexion.rellenarChoiceLibrosFK(choLibrosFK, choLibrosFK.getSelectedIndex() + "");
		} else if (dlgConfirmar.isActive())
		{
			dlgConfirmar.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
		} else if (dlgModificacion.isActive())
		{
			dlgModificacion.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
		} else if (ventana.isActive())
		{
			ventana.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
		}
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

}
