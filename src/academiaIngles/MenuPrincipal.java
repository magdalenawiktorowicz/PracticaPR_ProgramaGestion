package academiaIngles;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Academia de Inglés"); 
	MenuBar barraMenu = new MenuBar();
	
	Menu menuPersonas = new Menu("Personas");
	Menu menuLibros = new Menu("Libros");
	
	MenuItem mniPersonasAlta = new MenuItem("Alta");
	MenuItem mniPersonasBaja = new MenuItem("Baja");
	MenuItem mniPersonasModificacion = new MenuItem("Modificación");
	MenuItem mniPersonasConsulta = new MenuItem("Consulta");
	
	MenuItem mniLibrosAlta = new MenuItem("Alta");
	MenuItem mniLibrosBaja = new MenuItem("Baja");
	MenuItem mniLibrosModificacion = new MenuItem("Modificación");
	MenuItem mniLibrosConsulta = new MenuItem("Consulta");
	
	// montar la ventana de Menú principal según el tipo de usuario (res)
	MenuPrincipal(int res) {
		ventana.setLayout(new FlowLayout());
		ventana.setMenuBar(barraMenu);
		ventana.setBackground(new Color(202, 212, 231));
		ventana.addWindowListener(this);
		
		mniPersonasAlta.addActionListener(this);
		mniPersonasBaja.addActionListener(this);
		mniPersonasModificacion.addActionListener(this);
		mniPersonasConsulta.addActionListener(this);
		
		mniLibrosAlta.addActionListener(this);
		mniLibrosBaja.addActionListener(this);
		mniLibrosModificacion.addActionListener(this);
		mniLibrosConsulta.addActionListener(this);
		
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
		
		barraMenu.add(menuPersonas);
		barraMenu.add(menuLibros);
		
		ventana.setLocationRelativeTo(null);
		ventana.setResizable(false);
		ventana.setSize(300,200); 
		ventana.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(mniPersonasAlta)) {
			new PersonasAlta();
		}
		else if (e.getSource().equals(mniPersonasConsulta)) {
			new PersonasConsulta();
		}
		else if (e.getSource().equals(mniPersonasBaja)) {
			new PersonasBaja();
		}
		else if (e.getSource().equals(mniPersonasModificacion)) {
			new PersonasModificacion();
		}
		else if (e.getSource().equals(mniLibrosAlta)) {
			new LibrosAlta();
		}
		else if (e.getSource().equals(mniLibrosBaja)) {
			new LibrosBaja();
		}
		else if (e.getSource().equals(mniLibrosConsulta)) {
			new LibrosConsulta();
		}
		else if (e.getSource().equals(mniLibrosModificacion)) {
			new LibrosModificacion();
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
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
