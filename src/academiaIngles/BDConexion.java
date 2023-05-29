package academiaIngles;

import java.awt.Choice;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

public class BDConexion
{
	String driver = "com.mysql.cj.jdbc.Driver"; // driver nativo tipo 4 de MySQL
	String url = "jdbc:mysql://localhost:3306/programagestionbd"; // ubicación y nombre de BD
	String user = "root"; // usuario para conectarse con BD
	String password = "Studium2022;"; // clave para conectarse con BD

	Connection connection = null; // objeto para conectar
	Statement statement = null; // objeto para lanzar las sentencias
	ResultSet resultSet = null; // objeto para guardar los datos de BD

	String usuario = "";

	BDConexion()
	{
		connection = this.conexion();
	}

	public Connection conexion()
	{
		try
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			// Establecer la conexión con la BD
			return (DriverManager.getConnection(url, user, password));
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return null;
	}

	// método para comprobar el usuario y su clave (y el tipo de usuario)
	public int comprobarCredenciales(String u, String p)
	{
		usuario = u;
		// valor inicial para las credenciales incorrectas
		int res = -1;
		resultSet = null;
		// Montar SQL (para el usuario introducido)
		String sentencia = "SELECT * FROM usuarios WHERE nombreUsuario = '" + u + "' AND claveUsuario = SHA2('" + p
				+ "', 256);";
		try
		{
			// Crear una sentencia típica o genérica
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto resultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			resultSet = statement.executeQuery(sentencia);
			// obtener el tipo de usuario (0 - Administrador, 1 - Usuario básico)
			// guardar el tipo en la variable res
			if (resultSet.next())
			{
				res = resultSet.getInt("tipoUsuario");
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return res;
	}

	// método de dar de alta de persona
	public int altaPersona(String sentencia)
	{
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	// método para obtener correos electrónicos de la BD
	public String devolverCorreo()
	{
		String sentencia = "SELECT correoElectronicoPersona FROM personas";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				resultado = resultSet.getString("correoElectronicoPersona");
			}
			return resultado;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	// método para obtener los datos de todos registros de la tabla personas
	public String obtenerPersonas()
	{
		String sentencia = "SELECT * FROM personas";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con tabulaciones
				resultado = resultado + resultSet.getInt("idPersona") + " \t " + resultSet.getString("nombrePersona")
						+ " \t \t " + resultSet.getString("primerApellidoPersona") + " \t \t "
						+ resultSet.getString("segundoApellidoPersona") + " \t \t " + resultSet.getInt("telPersona")
						+ " \t " + resultSet.getString("correoElectronicoPersona") + " \t "
						+ resultSet.getString("direccionPersona") + " \t \n";
			}
			return resultado;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	// rellenar un desplegable con todos los registros de la tabla personas
	public void rellenarChoicePersonas(Choice choPersonas)
	{
		// borrar todos los registros
		choPersonas.removeAll();
		// añadir lo que se muestra por defecto en choice
		choPersonas.add("Selecciona la persona...           ");
		// obtener los datos (nombre y apellidos) de cada registro de la tabla personas
		String sentencia = "SELECT idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona FROM personas ORDER BY primerApellidoPersona;";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choPersonas.add(resultSet.getInt("idPersona") + " " + resultSet.getString("nombrePersona") + " "
						+ resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	// método de eliminar un registro de la tabla personas según el id
	public int eliminarPersona(String idPersona)
	{
		// la sentencia SQL - DELETE
		String sentencia = "DELETE FROM personas WHERE idPersona = " + idPersona + ";";
		int res = 0;
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			res = 1;
		}
		return res;
	}

	// método de editar un registro
	public String editarPersona(String idPersona)
	{
		// crear una sentencia para obtener los datos del registro según el id
		String sentencia = "SELECT * FROM personas WHERE idPersona = " + idPersona + ";";
		String resultado = "";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			resultSet = statement.executeQuery(sentencia);
			if (resultSet.next())
			{
				// obtener y guardar en una variable resultado los datos del registro
				resultado = resultSet.getInt("idPersona") + "-" + resultSet.getString("nombrePersona") + "-"
						+ resultSet.getString("primerApellidoPersona") + "-"
						+ resultSet.getString("segundoApellidoPersona") + "-" + resultSet.getInt("telPersona") + "-"
						+ resultSet.getString("correoElectronicoPersona") + "-"
						+ resultSet.getString("direccionPersona");
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return resultado;
	}

	// método de modificar un registro de la tabla personas
	public int modificarPersona(String sentencia)
	{
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	// método para obtener los nombres de libros de la BD
	public String devolverNombre()
	{
		String sentencia = "SELECT nombreLibro FROM libros";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				resultado = resultSet.getString("nombreLibro");
			}
			return resultado;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	// método de dar de alta de persona
	public int altaLibro(String sentencia)
	{
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	// rellenar un desplegable con todos los registros de la tabla libros
	public void rellenarChoiceLibros(Choice choLibros)
	{
		// borrar todos los registros
		choLibros.removeAll();
		// añadir lo que se muestra por defecto en choice
		choLibros.add("Selecciona el libro...");
		// obtener los datos (id, nombre y nivel) de cada registro de la tabla libros
		String sentencia = "SELECT idLibro, nombreLibro, nivelLibro FROM libros ORDER BY nivelLibro;";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choLibros.add(resultSet.getInt("idLibro") + " " + resultSet.getString("nombreLibro") + " "
						+ resultSet.getString("nivelLibro"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	// método de eliminar un registro de la tabla libros según el id
	public int eliminarLibro(String idLibro)
	{
		String sentencia = "DELETE FROM libros WHERE idLibro = " + idLibro + ";";
		int res = 0;
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			res = 1;
		}
		return res;
	}

	// método para obtener los datos de todos registros de la tabla libros
	public String obtenerLibros()
	{
		String sentencia = "SELECT * FROM libros";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				resultado = resultado + resultSet.getInt("idLibro") + " \t " + resultSet.getString("nombreLibro")
						+ " \t \t " + resultSet.getString("nivelLibro") + " \t \t " + resultSet.getString("autorLibro")
						+ " \t " + resultSet.getString("editorialLibro") + " \t \n";
			}
			return resultado;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	// método de editar un registro
	public String editarLibro(String idLibro)
	{
		// crear una sentencia para obtener los datos del registro según el id
		String sentencia = "SELECT * FROM libros WHERE idLibro = " + idLibro + ";";
		String resultado = "";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			resultSet = statement.executeQuery(sentencia);
			if (resultSet.next())
			{
				// obtener y guardar en una variable resultado los datos del registro
				resultado = resultSet.getInt("idLibro") + "-" + resultSet.getString("nombreLibro") + "-"
						+ resultSet.getString("nivelLibro") + "-" + resultSet.getString("autorLibro") + "-"
						+ resultSet.getString("editorialLibro");
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return resultado;
	}

	// método de modificar un registro de la tabla libros
	public int modificarLibro(String sentencia)
	{
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	public void apunteLog(String usuario, String mensaje)
	{
		// Abrir el fichero para AÑADIR
		try
		{
			String fechaHora;
			// Abrir fichero
			FileWriter fw = new FileWriter("historico.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);
			// Trabajar con el fichero (contenido)
			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			fechaHora = formatterLocalDateTime.format(localDateTime);
			salida.println("[" + fechaHora + "]" + "[" + usuario + "]" + "[" + mensaje + "]" + "\n");
			// Cerrar el fichero
			salida.close();
			bw.close();
			fw.close();
		} catch (IOException ioe)
		{
			System.out.println("Error en el fichero...");
		}
	}

	public void process(Table table, String line, PdfFont font, boolean isHeader)
	{
		StringTokenizer tokenizer = new StringTokenizer(line, ";");
		while (tokenizer.hasMoreTokens())
		{
			if (isHeader)
			{
				table.addHeaderCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
			} else
			{
				table.addCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
			}
		}
	}

	public String generatePDFPersonas()
	{
		String personasConsultaPDF = "personasConsulta.pdf";
		try
		{
			// Initialize PDF writer
			PdfWriter writer = new PdfWriter(personasConsultaPDF);

			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(10, 10, 10, 10);

			PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			Table table = new Table(UnitValue.createPercentArray(new float[]
			{ 0.5f, 1.5f, 2, 2.5f, 1.5f, 4, 3 })).useAllAvailableWidth();

			String sentencia = "SELECT * FROM personas";
			String resultadoPDF = "";

			String header = "ID;Nombre;Primer Apellido;Segundo Apellido;Teléfono;Correo Electrónico;Dirección;";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				if (resultSet.isFirst())
				{
					process(table, header, bold, true);
				}
				// sacar los datos separándolos con ';'
				resultadoPDF = resultadoPDF + resultSet.getInt("idPersona") + ";" + resultSet.getString("nombrePersona")
						+ ";" + resultSet.getString("primerApellidoPersona") + ";"
						+ resultSet.getString("segundoApellidoPersona") + ";" + resultSet.getInt("telPersona") + ";"
						+ resultSet.getString("correoElectronicoPersona") + ";"
						+ resultSet.getString("direccionPersona") + ";";
			}
			process(table, resultadoPDF, font, false);
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(personasConsultaPDF));

		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";

	}

	public String generatePDFLibros()
	{
		String librosConsultaPDF = "librosConsulta.pdf";
		try
		{
			// Initialize PDF writer
			PdfWriter writer = new PdfWriter(librosConsultaPDF);

			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(10, 10, 10, 10);
			PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			Table table = new Table(UnitValue.createPercentArray(new float[]
			{ 0.3f, 2, 0.5f, 2.5f, 1.5f })).useAllAvailableWidth();

			String sentencia = "SELECT * FROM libros";
			String resultadoPDF = "";

			String header = "ID;Nombre;Nivel;Autor;Editorial;";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				if (resultSet.isFirst())
				{
					process(table, header, bold, true);
				}
				// sacar los datos separándolos con ';'
				resultadoPDF = resultadoPDF + resultSet.getInt("idLibro") + ";" + resultSet.getString("nombreLibro")
						+ ";" + resultSet.getString("nivelLibro") + ";" + resultSet.getString("autorLibro") + ";"
						+ resultSet.getString("editorialLibro") + ";";
			}
			process(table, resultadoPDF, font, false);
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(librosConsultaPDF));

		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";

	}

	public int altaPersonaLibro(String sentencia)
	{
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	// un método para comprobar si la relación entre esta persona y este libro ya
	// existe en la BD
	public String comprobarExistencia(String idPersonaFK, String idLibroFK)
	{
		// sacar solo los resultados con FK de la persona y el libro seleccionado en los
		// choices
		String sentencia = "SELECT * FROM personasLibros WHERE idPersonaFK = " + idPersonaFK + " AND idLibroFK = "
				+ idLibroFK + ";";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				resultado = resultSet.getInt("idPersonaLibro") + " - " + resultSet.getInt("idPersonaFK") + " + "
						+ resultSet.getInt("idLibroFK");
				// System.out.println(resultado);
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return resultado;

	}

	public String obtenerPersonasLibros()
	{
		// la sentencia para obtener los datos de la tabla personasLibros (junto con los
		// datos de sus tablas correspondientes - personas y libros)
		String sentencia = "SELECT idPersonaLibro, idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona, idLibro, nombreLibro, nivelLibro, autorLibro FROM programagestionbd.personasLibros JOIN programagestionbd.personas ON personasLibros.idPersonaFK = personas.idPersona JOIN programagestionbd.libros ON personasLibros.idLibroFK = libros.idLibro;";
		String resultado = "";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con tabulaciones
				resultado = resultado + resultSet.getInt("idPersonaLibro") + " \t " + resultSet.getInt("idPersona")
						+ " \t \t " + resultSet.getString("nombrePersona") + " "
						+ resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona") + "\t" + resultSet.getInt("idLibro") + " \t "
						+ resultSet.getString("nombreLibro") + " \t " + resultSet.getString("nivelLibro") + " \t "
						+ resultSet.getString("autorLibro") + " \t \n";
			}
			return resultado;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	public String generatePDFPersonasLibros()
	{
		String personasLibrosConsultaPDF = "personasLibrosConsulta.pdf";
		try
		{
			// Initialize PDF writer
			PdfWriter writer = new PdfWriter(personasLibrosConsultaPDF);

			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.setMargins(10, 10, 10, 10);

			PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			Table table = new Table(UnitValue.createPercentArray(new float[]
			{ 0.5f, 0.5f, 2.5f, 0.5f, 2.5f, 0.5f, 2.f })).useAllAvailableWidth();

			String sentencia = "SELECT idPersonaLibro, idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona, idLibro, nombreLibro, nivelLibro, autorLibro FROM programagestionbd.personasLibros JOIN programagestionbd.personas ON personasLibros.idPersonaFK = personas.idPersona JOIN programagestionbd.libros ON personasLibros.idLibroFK = libros.idLibro;";
			String resultadoPDF = "";

			String header = "ID;IdPersona;Nombre de Persona;IdLibro;Nombre de Libro;Nivel;Autor;";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				if (resultSet.isFirst())
				{
					process(table, header, bold, true);
				}
				// sacar los datos separándolos con ';'
				resultadoPDF = resultadoPDF + resultSet.getInt("idPersonaLibro") + ";" + resultSet.getInt("idPersona")
						+ ";" + resultSet.getString("nombrePersona") + " "
						+ resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona") + ";" + resultSet.getInt("idLibro") + ";"
						+ resultSet.getString("nombreLibro") + ";" + resultSet.getString("nivelLibro") + ";"
						+ resultSet.getString("autorLibro") + ";";
			}
			process(table, resultadoPDF, font, false);
			document.add(table);
			document.close();
			Desktop.getDesktop().open(new File(personasLibrosConsultaPDF));

		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return "";
	}

	public void rellenarChoicePersonasLibros(Choice choPersonasLibros)
	{
		// borrar todos los registros
		choPersonasLibros.removeAll();
		// añadir lo que se muestra por defecto en choice
		choPersonasLibros.add("Selecciona la relación...           ");
		// obtener los datos de cada registro de la tabla personasLibros
		String sentencia = "SELECT idPersonaLibro, nombrePersona, primerApellidoPersona, segundoApellidoPersona, nombreLibro, nivelLibro, autorLibro FROM programagestionbd.personasLibros JOIN programagestionbd.personas ON personasLibros.idPersonaFK = personas.idPersona JOIN programagestionbd.libros ON personasLibros.idLibroFK = libros.idLibro;";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choPersonasLibros.add(resultSet.getInt("idPersonaLibro") + " " + resultSet.getString("nombrePersona")
						+ " " + resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona") + " " + resultSet.getString("nombreLibro") + " "
						+ resultSet.getString("nivelLibro") + " " + resultSet.getString("autorLibro"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	public String editarPersonaLibro(String idPersonaLibro)
	{
		// crear una sentencia para obtener los datos del registro según el id
		String sentencia = "SELECT * FROM programagestionbd.personasLibros WHERE idPersonaLibro = " + idPersonaLibro
				+ ";";
		String resultado = "";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Ejecutar la sentencia
			resultSet = statement.executeQuery(sentencia);
			if (resultSet.next())
			{
				// obtener y guardar en una variable resultado los datos del registro
				resultado = resultSet.getInt("idPersonaLibro") + "-" + resultSet.getInt("idPersonaFK") + "-"
						+ resultSet.getInt("idLibroFK");
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return resultado;
	}

	public void rellenarChoicePersonasFK(Choice choPersonasFK, String idPersonaFK)
	{
		// borrar todos los registros
		choPersonasFK.removeAll();
		// añadir la persona original que salga en la primera opción, la que se muestra
		// por defecto
		// pasando la sentencia obtenemos la persona de la relación original
		String sentencia = "SELECT idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona FROM personas WHERE idPersona = "
				+ idPersonaFK + ";";
		// pasando la sentencia2 obtenemos todas las demás
		String sentencia2 = "SELECT idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona FROM personas WHERE idPersona != "
				+ idPersonaFK + ";";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choPersonasFK.add(resultSet.getInt("idPersona") + " " + resultSet.getString("nombrePersona") + " "
						+ resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia2);
			while (resultSet.next())
			{
				choPersonasFK.add(resultSet.getInt("idPersona") + " " + resultSet.getString("nombrePersona") + " "
						+ resultSet.getString("primerApellidoPersona") + " "
						+ resultSet.getString("segundoApellidoPersona"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void rellenarChoiceLibrosFK(Choice choLibrosFK, String idLibroFK)
	{
		// borrar todos los registros
		choLibrosFK.removeAll();
		// añadir al choLibrosFK el libro original que salga en la primera opción, la
		// que se muestra por defecto
		// pasando la sentencia obtenemos el de la relación original
		String sentencia = "SELECT idLibro, nombreLibro, nivelLibro, autorLibro FROM libros WHERE idLibro = "
				+ idLibroFK + ";";
		// pasando la sentencia2 obtenemos todos los demás
		String sentencia2 = "SELECT idLibro, nombreLibro, nivelLibro, autorLibro FROM libros WHERE idLibro != "
				+ idLibroFK + ";";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choLibrosFK.add(resultSet.getInt("idLibro") + " " + resultSet.getString("nombreLibro") + " "
						+ resultSet.getString("nivelLibro") + " " + resultSet.getString("autorLibro"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia2);
			while (resultSet.next())
			{
				choLibrosFK.add(resultSet.getInt("idLibro") + " " + resultSet.getString("nombreLibro") + " "
						+ resultSet.getString("nivelLibro") + " " + resultSet.getString("autorLibro"));
			}
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	public int modificarPersonaLibro(String sentencia)
	{
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
			return 0;
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		return 1;
	}

	public int eliminarPersonaLibro(String idPersonaLibro)
	{
		// la sentencia SQL - DELETE
		String sentencia = "DELETE FROM personasLibros WHERE idPersonaLibro = " + idPersonaLibro + ";";
		int res = 0;
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statement.executeUpdate(sentencia);
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			res = 1;
		}
		return res;
	}

	public void generateExcelPersonas()
	{
		String personasConsultaExcel = "personasConsulta.xlsx";
		//String ruta = "C:\\FicherosExcel\\" + personasConsultaExcel;
		String hoja = "Hoja1";
		XSSFWorkbook libro = new XSSFWorkbook();
		XSSFSheet hoja1 = libro.createSheet(hoja);
		String header = "ID;Nombre;Primer Apellido;Segundo Apellido;Teléfono;Correo Electrónico;Dirección;";
		String[] headerCampos = header.split(";");
				
		try
		{
			String sentencia = "SELECT * FROM personas";
			String resultadoExcel = "";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con ';'
				resultadoExcel = resultadoExcel + resultSet.getInt("idPersona") + ";" + resultSet.getString("nombrePersona")
						+ ";" + resultSet.getString("primerApellidoPersona") + ";"
						+ resultSet.getString("segundoApellidoPersona") + ";" + resultSet.getInt("telPersona") + ";"
						+ resultSet.getString("correoElectronicoPersona") + ";"
						+ resultSet.getString("direccionPersona") + "\n";	
			}
			
			String[] registros = resultadoExcel.split("\n");
			
			CellStyle style = libro.createCellStyle();
			XSSFFont font = libro.createFont();
			font.setBold(true);
			style.setFont(font);
			
			XSSFRow headerRow = hoja1.createRow(0);
			for (int i = 0; i <7; i++) {
				XSSFCell headerCell = headerRow.createCell(i);
				headerCell.setCellStyle(style);
				headerCell.setCellValue(headerCampos[i]);	
			}
			
			for (int i = 0; i < registros.length; i++) {
				XSSFRow row = hoja1.createRow(i+1);
				for (int j = 0; j <7; j++) {
					XSSFCell cell = row.createCell(j);
					cell.setCellValue((registros[i].split(";"))[j]);
				}
			}
			hoja1.setColumnWidth(0, 2500);
			hoja1.setColumnWidth(1, 4000);
			hoja1.setColumnWidth(2, 6000);
			hoja1.setColumnWidth(3, 6000);
			hoja1.setColumnWidth(4, 5000);
			hoja1.setColumnWidth(5, 9000);
			hoja1.setColumnWidth(6, 9000);
			
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		File file;
		file = new File(personasConsultaExcel);
		try (FileOutputStream fileOuS = new FileOutputStream(file)) {
			if (file.exists()) { // Si el archivo ya existe, se elimina
				file.delete();
				System.out.println("Archivo eliminado");
			}
			//Se guarda la información en el fichero
			libro.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();
			System.out.println("Archivo Creado");
			libro.close();
			Desktop.getDesktop().open(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("El archivo no se encuentra o está en uso...");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateExcelLibros()
	{
		String librosConsultaExcel = "librosConsulta.xlsx";
		String hoja = "Hoja1";
		XSSFWorkbook libro = new XSSFWorkbook();
		XSSFSheet hoja1 = libro.createSheet(hoja);
		String header = "ID;Nombre;Nivel;Autor;Editorial;";
		String[] headerCampos = header.split(";");
				
		try
		{
			String sentencia = "SELECT * FROM libros";
			String resultadoExcel = "";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con ';'
				resultadoExcel = resultadoExcel + resultSet.getInt("idLibro") + ";" + resultSet.getString("nombreLibro")
				+ ";" + resultSet.getString("nivelLibro") + ";" + resultSet.getString("autorLibro") + ";"
				+ resultSet.getString("editorialLibro") + "\n";	
			}
			
			String[] registros = resultadoExcel.split("\n");
			
			CellStyle style = libro.createCellStyle();
			XSSFFont font = libro.createFont();
			font.setBold(true);
			style.setFont(font);
			
			XSSFRow headerRow = hoja1.createRow(0);
			for (int i = 0; i <5; i++) {
				XSSFCell headerCell = headerRow.createCell(i);
				headerCell.setCellStyle(style);
				headerCell.setCellValue(headerCampos[i]);	
			}
			
			for (int i = 0; i < registros.length; i++) {
				XSSFRow row = hoja1.createRow(i+1);
				for (int j = 0; j <5; j++) {
					XSSFCell cell = row.createCell(j);
					cell.setCellValue((registros[i].split(";"))[j]);
				}
			}
			hoja1.setColumnWidth(0, 2500);
			hoja1.setColumnWidth(1, 7500);
			hoja1.setColumnWidth(2, 2500);
			hoja1.setColumnWidth(3, 8000);
			hoja1.setColumnWidth(4, 6000);
			
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		File file;
		file = new File(librosConsultaExcel);
		try (FileOutputStream fileOuS = new FileOutputStream(file)) {
			if (file.exists()) { // Si el archivo ya existe, se elimina
				file.delete();
				System.out.println("Archivo eliminado");
			}
			//Se guarda la información en el fichero
			libro.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();
			System.out.println("Archivo Creado");
			libro.close();
			Desktop.getDesktop().open(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("El archivo no se encuentra o está en uso...");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateExcelPersonasLibros()
	{
		String personasLibrosConsultaExcel = "PersonasLibrosConsulta.xlsx";
		String hoja = "Hoja1";
		XSSFWorkbook libro = new XSSFWorkbook();
		XSSFSheet hoja1 = libro.createSheet(hoja);
		String header = "ID;IdPersona;Nombre de Persona;IdLibro;Nombre de Libro;Nivel;Autor;";
		String[] headerCampos = header.split(";");
				
		try
		{
			String sentencia = "SELECT idPersonaLibro, idPersona, nombrePersona, primerApellidoPersona, segundoApellidoPersona, idLibro, nombreLibro, nivelLibro, autorLibro FROM programagestionbd.personasLibros JOIN programagestionbd.personas ON personasLibros.idPersonaFK = personas.idPersona JOIN programagestionbd.libros ON personasLibros.idLibroFK = libros.idLibro;";
			String resultadoExcel = "";
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con ';'
				resultadoExcel = resultadoExcel + resultSet.getInt("idPersonaLibro") + ";" + resultSet.getInt("idPersona")
				+ ";" + resultSet.getString("nombrePersona") + " "
				+ resultSet.getString("primerApellidoPersona") + " "
				+ resultSet.getString("segundoApellidoPersona") + ";" + resultSet.getInt("idLibro") + ";"
				+ resultSet.getString("nombreLibro") + ";" + resultSet.getString("nivelLibro") + ";"
				+ resultSet.getString("autorLibro") + "\n";	
			}
			
			String[] registros = resultadoExcel.split("\n");
			
			CellStyle style = libro.createCellStyle();
			XSSFFont font = libro.createFont();
			font.setBold(true);
			style.setFont(font);
			
			XSSFRow headerRow = hoja1.createRow(0);
			for (int i = 0; i <7; i++) {
				XSSFCell headerCell = headerRow.createCell(i);
				headerCell.setCellStyle(style);
				headerCell.setCellValue(headerCampos[i]);	
			}
			
			for (int i = 0; i < registros.length; i++) {
				XSSFRow row = hoja1.createRow(i+1);
				for (int j = 0; j <7; j++) {
					XSSFCell cell = row.createCell(j);
					cell.setCellValue((registros[i].split(";"))[j]);
				}
			}
			hoja1.setColumnWidth(0, 2500);
			hoja1.setColumnWidth(1, 3000);
			hoja1.setColumnWidth(2, 9000);
			hoja1.setColumnWidth(3, 3000);
			hoja1.setColumnWidth(4, 8000);
			hoja1.setColumnWidth(5, 2500);
			hoja1.setColumnWidth(6, 7500);
			
		} catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		File file;
		file = new File(personasLibrosConsultaExcel);
		try (FileOutputStream fileOuS = new FileOutputStream(file)) {
			if (file.exists()) { // Si el archivo ya existe, se elimina
				file.delete();
				System.out.println("Archivo eliminado");
			}
			//Se guarda la información en el fichero
			libro.write(fileOuS);
			fileOuS.flush();
			fileOuS.close();
			System.out.println("Archivo Creado");
			libro.close();
			Desktop.getDesktop().open(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("El archivo no se encuentra o está en uso...");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ayuda()
	{
		goToURL("file:///C:/Users/madzi/eclipse-workspace/Workspace/AcademiaIngles/Ayuda/ayudaNew.html");
	}

	private void goToURL(String URL)
	{
		if (java.awt.Desktop.isDesktopSupported())
		{
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

			if (desktop.isSupported(java.awt.Desktop.Action.BROWSE))
			{
				try
				{
					java.net.URI uri = new java.net.URI(URL);
					desktop.browse(uri);
				} catch (URISyntaxException | IOException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
		}
	}
}
