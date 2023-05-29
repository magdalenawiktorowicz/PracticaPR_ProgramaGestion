package academiaIngles;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LibrosConsulta implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Libros - Consulta");
	Label lblAlta = new Label("____________________ CONSULTA DE LIBROS ____________________");
	TextArea txtaConsulta = new TextArea(10,90);
	Label lblExportar = new Label("Exportar en:");
	Button btnPDF = new Button("PDF");
	Button btnEXCEL = new Button("EXCEL");
	
	BDConexion conexion = new BDConexion();

	String usuario = "";
	
	LibrosConsulta(String u) {
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		// color de fondo
		ventana.setBackground(new Color(204, 224, 233));
		
		ventana.add(lblAlta);
		// bloquear la edición de datos
		txtaConsulta.setEditable(false);
		// poner el encabezado del listado
		txtaConsulta.append("ID \t Nombre \t \t \t \t Nivel \t \t Autor \t \t \t \t Editorial \t \n");
		// obtener los datos de todos registros de la tabla libros
		txtaConsulta.append(conexion.obtenerLibros());
		
		String sentencia = "SELECT * FROM libros";
		conexion.apunteLog(usuario, sentencia);
		txtaConsulta.setFocusable(false);
		ventana.add(txtaConsulta);
		ventana.add(lblExportar);
		btnPDF.addActionListener(this);
		btnPDF.addKeyListener(this);
		ventana.add(btnPDF);
		btnEXCEL.addActionListener(this);
		btnEXCEL.addKeyListener(this);
		ventana.add(btnEXCEL);
		
		ventana.setSize(780, 270);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getSource().equals(btnPDF)) {
			conexion.generatePDFLibros();
		}
		else if (e.getSource().equals(btnEXCEL)) {
			conexion.generateExcelLibros();
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
		if (e.getSource().equals(btnPDF)) {
			conexion.generatePDFLibros();
		}
		else if (e.getSource().equals(btnEXCEL)) {
			conexion.generateExcelLibros();
		}
	}

	@Override
	public void windowOpened(WindowEvent e)
	{}

	@Override
	public void windowClosing(WindowEvent e)
	{
		ventana.setVisible(false);
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
