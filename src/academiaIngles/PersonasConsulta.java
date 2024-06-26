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

public class PersonasConsulta implements WindowListener, ActionListener, KeyListener
{
	Frame ventana = new Frame("Personas - Consulta");
	Label lblConsulta = new Label("____________________ CONSULTA DE PERSONA ____________________");
	TextArea txtaConsulta = new TextArea(10,100);
	Label lblExportar = new Label("Exportar en:");
	Button btnPDF = new Button("PDF");
	Button btnEXCEL = new Button("EXCEL");
	
	BDConexion conexion = new BDConexion();
	
	String usuario = "";
	
	PersonasConsulta(String u) {
		usuario = u;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		// color de fondo
		ventana.setBackground(new Color(204, 224, 233));
		
		ventana.add(lblConsulta);
		// bloquear la edici�n de datos
		txtaConsulta.setEditable(false);
		// poner el encabezado del listado
		txtaConsulta.append("ID \t Nombre \t Primer Apellido \t Segundo Apellido \t Tel�fono \t Correo Electr�nico \t \t Direcci�n \t \n");
		// obtener los datos de todos registros de la tabla personas
		txtaConsulta.append(conexion.obtenerPersonas());
		
		String sentencia = "SELECT * FROM personas";
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
		
		ventana.setSize(855, 270);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getSource().equals(btnPDF)) {
			conexion.generatePDFPersonas();
		}
		else if (e.getSource().equals(btnEXCEL)) {
			conexion.generateExcelPersonas();
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
			conexion.generatePDFPersonas();
		}
		else if (e.getSource().equals(btnEXCEL)) {
			conexion.generateExcelPersonas();
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
