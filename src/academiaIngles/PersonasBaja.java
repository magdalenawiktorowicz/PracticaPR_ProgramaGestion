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

public class PersonasBaja implements WindowListener, ActionListener, KeyListener
{
	// componentes de la ventana de elegir una persona para dar de baja
	Frame ventana = new Frame("Personas - Baja");
	Label lblBaja = new Label("_______ BAJA DE PERSONA _______");
	Label lblElegir = new Label("Elige a la persona a eliminar:");
	Choice choPersonas = new Choice();
	Button btnEliminar = new Button("Eliminar");
	Button btnCancelar = new Button("Cancelar");
	
	// componentes del diálogo de confirmación de baja
	Dialog dlgConfirmar = new Dialog(ventana, "", true);
	Label lblConfirmar = new Label("¿Estás seguro que quieres eliminar...");
	TextField txtPersona = new TextField(20);
	Label lblConfirmar2 = new Label("?");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	
	// diálogos de error y de éxito
	Dialog dlgMensajeError = new Dialog(ventana, "Error", true);
	Dialog dlgMensajeExito = new Dialog(ventana, "Éxito", true);
	Label lblMensaje = new Label("");
	
	BDConexion conexion = new BDConexion();
	
	PersonasBaja() {
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		// color del fondo
		ventana.setBackground(new Color(255, 221, 203));
		
		ventana.add(lblBaja);
		ventana.add(lblElegir);
		// Rellenar el Choice con los elementos de la tabla personas
		conexion.rellenarChoicePersonas(choPersonas);
		ventana.add(choPersonas);
		
		btnEliminar.addActionListener(this);
		btnEliminar.addKeyListener(this);
		ventana.add(btnEliminar);
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
		if (e.getSource().equals(btnCancelar)) {
			ventana.setVisible(false);
		}
		else if(e.getSource().equals(btnEliminar)) {
			// si está elegido alguna persona mostrar el diálogo de confirmación de su baja
			if(choPersonas.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choPersonas
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(255, 221, 203));
				dlgConfirmar.setSize(240, 127);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				// mostrar el nombre y los apellidos de la persona elegida en el diálogo de confirmación
				txtPersona.setText(choPersonas.getSelectedItem());
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
		else if(e.getSource().equals(btnNo)) {
			dlgConfirmar.setVisible(false);
		}
		else if(e.getSource().equals(btnSi)) {
			
			if(choPersonas.getSelectedIndex()!=0) {
				// rellenar la tabla[] con los datos de la persona elegida
				String tabla[] = choPersonas.getSelectedItem().split(" ");
				//eliminar una persona pasándo como parámetro el número identificador (campo clave - único)
				int resultado = conexion.eliminarPersona(tabla[0]); // idPersona --> DELETE
				if (resultado == 0) {
				// Baja correcta
					dlgConfirmar.setVisible(false);
					lblMensaje.setText("La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
				} else {
				// Error en baja
				dlgConfirmar.setVisible(false);
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
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
		if (e.getSource().equals(btnCancelar)) {
			ventana.setVisible(false);
		}
		else if(e.getSource().equals(btnEliminar)) {
			// si está elegido alguna persona mostrar el diálogo de confirmación de su baja
			if(choPersonas.getSelectedIndex()!=0) {
				// 0 es el primer elemento en choPersonas
				dlgConfirmar.setLayout(new FlowLayout());
				dlgConfirmar.addWindowListener(this);
				dlgConfirmar.setBackground(new Color(255, 221, 203));
				dlgConfirmar.setSize(240, 127);
				dlgConfirmar.setResizable(false);
				dlgConfirmar.setLocationRelativeTo(null);
				dlgConfirmar.add(lblConfirmar);
				// mostrar el nombre y los apellidos de la persona elegida en el diálogo de confirmación
				txtPersona.setText(choPersonas.getSelectedItem());
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
		else if(e.getSource().equals(btnNo)) {
			dlgConfirmar.setVisible(false);
		}
		else if(e.getSource().equals(btnSi)) {
			
			if(choPersonas.getSelectedIndex()!=0) {
				// rellenar la tabla[] con los datos de la persona elegida
				String tabla[] = choPersonas.getSelectedItem().split(" ");
				//eliminar una persona pasándo como parámetro el número identificador (campo clave - único)
				int resultado = conexion.eliminarPersona(tabla[0]); // idPersona --> DELETE
				if (resultado == 0) {
				// Baja correcta
					dlgConfirmar.setVisible(false);
					lblMensaje.setText("La operación se ha ejecutado correctamente.");
					dlgMensajeExito.add(lblMensaje);
					dlgMensajeExito.setVisible(true);
				} else {
				// Error en baja
				dlgConfirmar.setVisible(false);
				lblMensaje.setText("Ha ocurrido un error.");
				dlgMensajeError.add(lblMensaje);
				dlgMensajeError.setVisible(true);
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
		if (dlgConfirmar.isActive())
		{
			dlgConfirmar.setVisible(false);
		} 
		else if (dlgMensajeError.isActive() || dlgMensajeExito.isActive())
		{
			dlgConfirmar.setVisible(false);
			dlgMensajeError.setVisible(false);
			dlgMensajeExito.setVisible(false);
			// Rellenar el Choice con los elementos de la tabla personas
			// de nuevo después de haber borrado un registro
			conexion.rellenarChoicePersonas(choPersonas);
		} 
		else
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
