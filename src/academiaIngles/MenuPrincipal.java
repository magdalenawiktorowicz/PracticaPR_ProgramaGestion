package academiaIngles;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	MenuBar barraMenu = new MenuBar();
	
	Menu menuPersonas = new Menu("Personas");
	Menu menuLibros = new Menu("Libros");
	Menu menuPersonasLibros = new Menu("Personas-Libros");
	
	Menu menuAyuda = new Menu("Ayuda");
	
	MenuItem mniPersonasAlta = new MenuItem("Alta");
	MenuItem mniPersonasBaja = new MenuItem("Baja");
	MenuItem mniPersonasModificacion = new MenuItem("Modificación");
	MenuItem mniPersonasConsulta = new MenuItem("Consulta");
	
	MenuItem mniLibrosAlta = new MenuItem("Alta");
	MenuItem mniLibrosBaja = new MenuItem("Baja");
	MenuItem mniLibrosModificacion = new MenuItem("Modificación");
	MenuItem mniLibrosConsulta = new MenuItem("Consulta");
	
	MenuItem mniPersonasLibrosAlta = new MenuItem("Alta");
	MenuItem mniPersonasLibrosBaja = new MenuItem("Baja");
	MenuItem mniPersonasLibrosModificacion = new MenuItem("Modificación");
	MenuItem mniPersonasLibrosConsulta = new MenuItem("Consulta");
	
	MenuItem mniAyuda = new MenuItem("Ayuda");
	
	BDConexion conexion = new BDConexion();
	
	String usuario = "";
	
	Image backgroundImg;
	Toolkit herramienta;
	
	// montar la ventana de Menú principal según el tipo de usuario (res)
	MenuPrincipal(int res, String u) {
		usuario = u;
		setLayout(new FlowLayout());
		setBackground(new Color(211, 251, 248));
		setMenuBar(barraMenu);
		
		mniPersonasAlta.addActionListener(this);
		mniPersonasBaja.addActionListener(this);
		mniPersonasModificacion.addActionListener(this);
		mniPersonasConsulta.addActionListener(this);
		
		mniLibrosAlta.addActionListener(this);
		mniLibrosBaja.addActionListener(this);
		mniLibrosModificacion.addActionListener(this);
		mniLibrosConsulta.addActionListener(this);
		
		mniPersonasLibrosAlta.addActionListener(this);
		mniPersonasLibrosBaja.addActionListener(this);
		mniPersonasLibrosModificacion.addActionListener(this);
		mniPersonasLibrosConsulta.addActionListener(this);
		
		mniAyuda.addActionListener(this);
		
		menuPersonas.add(mniPersonasAlta);
		// si el tipo de usuario (res) es 0 (Administrador) mostrar también
		// menuItems de Baja, Modificacion y Consulta
		if (res == 0) {
			menuPersonas.add(mniPersonasBaja);
			menuPersonas.add(mniPersonasModificacion);
			menuPersonas.add(mniPersonasConsulta);
		}
		// si el tipo de usuario es 1 - mostrar solo el menuItem de Alta
		menuLibros.add(mniLibrosAlta);
		
		if (res == 0) {
			menuLibros.add(mniLibrosBaja);
			menuLibros.add(mniLibrosModificacion);
			menuLibros.add(mniLibrosConsulta);
		}
		
		menuPersonasLibros.add(mniPersonasLibrosAlta);
		
		if (res == 0) {
			menuPersonasLibros.add(mniPersonasLibrosBaja);
			menuPersonasLibros.add(mniPersonasLibrosModificacion);
			menuPersonasLibros.add(mniPersonasLibrosConsulta);
		}
		
		menuAyuda.add(mniAyuda);
		
		barraMenu.add(menuPersonas);
		barraMenu.add(menuLibros);
		barraMenu.add(menuPersonasLibros);
		barraMenu.add(menuAyuda);
		
		setTitle("Academia de Inglés");
		addWindowListener(this);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(300,250); 
		
		herramienta = getToolkit();
		backgroundImg = herramienta.getImage("e5.png");
		
		setVisible(true);
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(backgroundImg, 5, 5,this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(mniPersonasAlta)) {
			new PersonasAlta(usuario);
		}
		else if (e.getSource().equals(mniPersonasConsulta)) {
			new PersonasConsulta(usuario);
		}
		else if (e.getSource().equals(mniPersonasBaja)) {
			new PersonasBaja(usuario);
		}
		else if (e.getSource().equals(mniPersonasModificacion)) {
			new PersonasModificacion(usuario);
		}
		else if (e.getSource().equals(mniLibrosAlta)) {
			new LibrosAlta(usuario);
		}
		else if (e.getSource().equals(mniLibrosBaja)) {
			new LibrosBaja(usuario);
		}
		else if (e.getSource().equals(mniLibrosConsulta)) {
			new LibrosConsulta(usuario);
		}
		else if (e.getSource().equals(mniLibrosModificacion)) {
			new LibrosModificacion(usuario);
		}
		else if (e.getSource().equals(mniPersonasLibrosAlta)) {
			new PersonasLibrosAlta(usuario);
		}
		else if (e.getSource().equals(mniPersonasLibrosBaja)) {
			new PersonasLibrosBaja(usuario);
		}
		else if (e.getSource().equals(mniPersonasLibrosConsulta)) {
			new PersonasLibrosConsulta(usuario);
		}
		else if (e.getSource().equals(mniPersonasLibrosModificacion)) {
			new PersonasLibrosModificacion(usuario);
		}
		else if(e.getSource().equals(mniAyuda)) {
			conexion.ayuda();
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		conexion.apunteLog(usuario, "LOGOUT");
		System.exit(0);
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
