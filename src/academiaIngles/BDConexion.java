package academiaIngles;

import java.awt.Choice;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BDConexion
{
	String driver = "com.mysql.cj.jdbc.Driver"; // driver nativo tipo 4 de MySQL
	String url = "jdbc:mysql://localhost:3306/programagestionbd"; // ubicación y nombre de BD
	String user = "root"; // usuario para conectarse con BD
	String password = "Studium2022;"; // clave para conectarse con BD

	Connection connection = null; // objeto para conectar
	Statement statement = null; // objeto para lanzar las sentencias
	ResultSet resultSet = null; // objeto para guardar los datos de BD

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
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				// sacar los datos separándolos con tabulaciones
				resultado = resultado + resultSet.getInt("idPersona") + " \t " + resultSet.getString("nombrePersona") + " \t \t " + resultSet.getString("primerApellidoPersona") + " \t \t " + resultSet.getString("segundoApellidoPersona") + " \t \t " + resultSet.getInt("telPersona") + " \t " + resultSet.getString("correoElectronicoPersona") + " \t " + resultSet.getString("direccionPersona") + " \t \n"; 
			}
			return resultado;
		}
		catch (Exception e)
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
				choPersonas.add(resultSet.getInt("idPersona") + " " + resultSet.getString("nombrePersona") + " " + resultSet.getString("primerApellidoPersona") + " " + resultSet.getString("segundoApellidoPersona"));
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
				resultado = resultSet.getInt("idPersona") + "-" + resultSet.getString("nombrePersona") + "-" + resultSet.getString("primerApellidoPersona") + "-" + resultSet.getString("segundoApellidoPersona") + "-" + resultSet.getInt("telPersona") + "-" + resultSet.getString("correoElectronicoPersona") + "-" + resultSet.getString("direccionPersona"); 
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
		choLibros.add("Selecciona el libro...           ");
		// obtener los datos (id, nombre y nivel) de cada registro de la tabla libros
		String sentencia = "SELECT idLibro, nombreLibro, nivelLibro FROM libros ORDER BY nivelLibro;";
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				choLibros.add(resultSet.getInt("idLibro") + " " + resultSet.getString("nombreLibro") + " " + resultSet.getString("nivelLibro"));
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
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sentencia);
			while (resultSet.next())
			{
				resultado = resultado + resultSet.getInt("idLibro") + " \t " + resultSet.getString("nombreLibro") + " \t \t " + resultSet.getString("nivelLibro") + " \t \t " + resultSet.getString("autorLibro") + " \t " + resultSet.getString("editorialLibro") + " \t \n"; 
			}
			return resultado;
		}
		catch (Exception e)
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
				resultado = resultSet.getInt("idLibro") + "-" + resultSet.getString("nombreLibro") + "-" + resultSet.getString("nivelLibro") + "-" + resultSet.getString("autorLibro") + "-" + resultSet.getString("editorialLibro"); 
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
}
