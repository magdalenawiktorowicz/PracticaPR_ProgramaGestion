package academiaIngles;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PersonasLibrosAlta implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Personas-Libros - Alta");
	Label lblAlta = new Label("ALTA DE LA RELACIÓN");
	Label lblAlta2 = new Label("PERSONA - LIBRO");
	Choice choPersonas = new Choice();
	Choice choLibros = new Choice();
	Button btnAceptar = new Button("Aceptar");

	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");

	BDConexion conexion = new BDConexion();

	String usuario = "";

	PersonasLibrosAlta(String u)
	{
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventana.setBackground(new Color(205, 232, 198));
		ventana.add(lblAlta);
		ventana.add(lblAlta2);
		conexion.rellenarChoicePersonas(choPersonas);
		ventana.add(choPersonas);
		conexion.rellenarChoiceLibros(choLibros);
		ventana.add(choLibros);
		btnAceptar.addActionListener(this);
		btnAceptar.addKeyListener(this);
		ventana.add(btnAceptar);

		ventana.setSize(270, 180);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);

		// establecer los dialogos para mensajes de éxito y de error
		dlgMensajeError.setLayout(new FlowLayout());
		dlgMensajeError.setSize(265, 80);
		dlgMensajeError.addWindowListener(this);
		dlgMensajeError.setResizable(false);
		dlgMensajeError.setLocationRelativeTo(null);
		dlgMensajeExito.setLayout(new FlowLayout());
		dlgMensajeExito.setSize(265, 80);
		dlgMensajeExito.addWindowListener(this);
		dlgMensajeExito.setResizable(false);
		dlgMensajeExito.setLocationRelativeTo(null);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getSource().equals(btnAceptar))
		{
			// comprobar que se ha seleccionado una persona y un libro
			if (choPersonas.getSelectedIndex() != 0 && choLibros.getSelectedIndex() != 0)
			{
				String tablaPersonas[] = choPersonas.getSelectedItem().split(" ");
				String tablaLibros[] = choLibros.getSelectedItem().split(" ");
				// obtener los campos claves de las tablas personas y libros (para usar los
				// mismos valores como los FKs en la nueva tabla personasLibros
				String idPersona = tablaPersonas[0];
				String idLibro = tablaLibros[0];
				String sentencia = "INSERT INTO personasLibros VALUES (null, " + idPersona + ", " + idLibro + ");";
				String sentenciaLog = "INSERT INTO personasLibros VALUES (null, " + idPersona + " (" + tablaPersonas[1]
						+ " " + tablaPersonas[2] + " " + tablaPersonas[3] + "), " + idLibro + " (" + tablaLibros[1] + " " + tablaLibros[2] + " " + tablaLibros[3] + " " + tablaLibros[4] + ");";
				// obtener el registro con los mismos FKs (si existe)
				String resultado = conexion.comprobarExistencia(idPersona, idLibro);
				// si existe un registro con los mismos FKs se mostrará un mensaje
				if (resultado != "")
				{
					lblMensaje.setText("Esta relación ya existe.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				}
				// si todavia no existe este registro...
				else
				{
					// llamar al método de para dar de alta y obtener un 0 o 1 de vuelta
					int respuesta = conexion.altaPersonaLibro(sentencia);
					// si el resultado es diferente de 0, ha ocurrido algún error
					if (respuesta != 0)
					{
						lblMensaje.setText("Ha ocurrido un error.");
						dlgMensajeError.add(lblMensaje);
						dlgMensajeError.setVisible(true);
					} else // alta correcta
					{
						conexion.apunteLog(usuario, sentenciaLog);
						lblMensaje.setText("  La operación se ha ejecutado correctamente.");
						dlgMensajeExito.add(lblMensaje);
						dlgMensajeExito.setVisible(true);
						conexion.rellenarChoicePersonas(choPersonas);
						conexion.rellenarChoiceLibros(choLibros);
					}
				}

			} else if (choPersonas.getSelectedIndex() == 0 || choLibros.getSelectedIndex() == 0)
			{
				dlgMensajeError.setSize(293, 80);
				lblMensaje.setText("  No has seleccionado ninguna persona/ningún libro.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(btnAceptar))
		{
			// comprobar que se ha seleccionado una persona y un libro
			if (choPersonas.getSelectedIndex() != 0 && choLibros.getSelectedIndex() != 0)
			{
				String tablaPersonas[] = choPersonas.getSelectedItem().split(" ");
				String tablaLibros[] = choLibros.getSelectedItem().split(" ");
				// obtener los campos claves de las tablas personas y libros (para usar los
				// mismos valores como los FKs en la nueva tabla personasLibros
				String idPersona = tablaPersonas[0];
				String idLibro = tablaLibros[0];
				String sentencia = "INSERT INTO personasLibros VALUES (null, " + idPersona + ", " + idLibro + ");";
				String sentenciaLog = "INSERT INTO personasLibros VALUES (null, " + idPersona + " (" + tablaPersonas[1]
						+ " " + tablaPersonas[2] + " " + tablaPersonas[3] + "), " + idLibro + " (" + tablaLibros[1] + " " + tablaLibros[2] + " " + tablaLibros[3] + " " + tablaLibros[4] + ");";
				// obtener el registro con los mismos FKs (si existe)
				String resultado = conexion.comprobarExistencia(idPersona, idLibro);
				// si existe un registro con los mismos FKs se mostrará un mensaje
				if (resultado != "")
				{
					lblMensaje.setText("Esta relación ya existe.");
					dlgMensajeError.add(lblMensaje);
					dlgMensajeError.setVisible(true);
				}
				// si todavia no existe este registro...
				else
				{
					// llamar al método de para dar de alta y obtener un 0 o 1 de vuelta
					int respuesta = conexion.altaPersonaLibro(sentencia);
					// si el resultado es diferente de 0, ha ocurrido algún error
					if (respuesta != 0)
					{
						lblMensaje.setText("Ha ocurrido un error.");
						dlgMensajeError.add(lblMensaje);
						dlgMensajeError.setVisible(true);
					} else // alta correcta
					{
						conexion.apunteLog(usuario, sentenciaLog);
						lblMensaje.setText("  La operación se ha ejecutado correctamente.");
						dlgMensajeExito.add(lblMensaje);
						dlgMensajeExito.setVisible(true);
						conexion.rellenarChoicePersonas(choPersonas);
						conexion.rellenarChoiceLibros(choLibros);
					}
				}

			} else if (choPersonas.getSelectedIndex() == 0 || choLibros.getSelectedIndex() == 0)
			{
				dlgMensajeError.setSize(293, 80);
				lblMensaje.setText("  No has seleccionado ninguna persona/ningún libro.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
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
	public void windowOpened(WindowEvent e)
	{
	}

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
