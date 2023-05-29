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

public class Login implements WindowListener, ActionListener, KeyListener
{
	Frame ventanaLogin = new Frame("Login");
	Label lblUsuario = new Label("Usuario:");
	TextField txtUsuario = new TextField(12);
	Label lblClave = new Label("   Clave:");
	TextField txtClave = new TextField(12);
	Button btnAcceder = new Button("Acceder");

	BDConexion conexion = new BDConexion();

	Dialog dlgMensaje = new Dialog(ventanaLogin, "Error", true);
	Label lblCredencialesIncorrectas = new Label("   Credenciales incorrectas.");

	Login()
	{
		ventanaLogin.setLayout(new FlowLayout());
		ventanaLogin.addWindowListener(this);
		// diferentes colores para alta, baja, modificacion y consulta
		ventanaLogin.setBackground(new Color(202, 212, 231));
		// añadir los componentes a la ventana
		ventanaLogin.add(lblUsuario);
		ventanaLogin.add(txtUsuario);
		ventanaLogin.add(lblClave);
		// ocultar la clave
		txtClave.setEchoChar('*');
		ventanaLogin.add(txtClave);
		btnAcceder.addKeyListener(this);
		btnAcceder.addActionListener(this);
		ventanaLogin.add(btnAcceder);

		ventanaLogin.setSize(240, 135);
		ventanaLogin.setResizable(false);
		ventanaLogin.setLocationRelativeTo(null);

		ventanaLogin.setVisible(true);

	}

	public static void main(String[] args)
	{
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(btnAcceder))
		{
			// obtener el texto introducido y guardarlo en las variables 'usuario' y 'clave'
			String usuario = txtUsuario.getText();
			String clave = txtClave.getText();
			// comprobar las credenciales introducidas y obtener el tipo de usuario
			int res = conexion.comprobarCredenciales(usuario, clave);
			
			if (res == -1) {
				// mostrar un diálogo con mensaje de credenciales incorrectas
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.addWindowListener(this);
				dlgMensaje.setSize(175, 80);
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.add(lblCredencialesIncorrectas);

				dlgMensaje.setVisible(true);
			}
			else {
				conexion.apunteLog(usuario, "LOGIN");
				// las credenciales con correctas abrir el menú principal
				// pasando el tipo de usuario como parámetro
				new MenuPrincipal(res, usuario);
				// ocultar la ventana de Login
				ventanaLogin.setVisible(false);
			}
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		} else
		{
			System.exit(0);
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
		if (e.getSource().equals(btnAcceder))
		{
			// obtener el texto introducido y guardarlo en las variables 'usuario' y 'clave'
			String usuario = txtUsuario.getText();
			String clave = txtClave.getText();
			// comprobar las credenciales introducidas y obtener el tipo de usuario
			int res = conexion.comprobarCredenciales(usuario, clave);
			
			if (res == -1) {
				// mostrar un diálogo con mensaje de credenciales incorrectas
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.addWindowListener(this);
				dlgMensaje.setSize(175, 80);
				dlgMensaje.setResizable(false);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.add(lblCredencialesIncorrectas);

				dlgMensaje.setVisible(true);
			}
			else {
				conexion.apunteLog(usuario, "LOGIN");
				// las credenciales con correctas abrir el menú principal
				// pasando el tipo de usuario como parámetro
				new MenuPrincipal(res, usuario);
				// ocultar la ventana de Login
				ventanaLogin.setVisible(false);
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
