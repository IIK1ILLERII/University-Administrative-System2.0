package model;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XE";
    private static final String USER = "NUEVO_ADMIN";
    private static final String PASSWORD = "AdminPass123";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("🔌 Creando nueva conexión a Oracle...");

                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connection.setAutoCommit(true);

                System.out.println("✅ Conexión exitosa a Oracle");
                System.out.println("   📍 URL: " + URL);
                System.out.println("   👤 Usuario: " + USER);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: Driver JDBC de Oracle no encontrado");
            showErrorDialog("Driver de Oracle no encontrado.\n\nDescarga ojdbc8.jar y agrégalo al classpath.");
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión a Oracle: " + e.getMessage());
            showErrorDialog("No se pudo conectar a la base de datos.\n\nError: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("🔌 Conexión cerrada correctamente");
                }
            } catch (SQLException e) {
                System.out.println("⚠️ Error al cerrar conexión: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    public static boolean testConnection() {
        try (Connection testConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            boolean isValid = testConn.isValid(5);
            System.out.println("🧪 Test de conexión: " + (isValid ? "EXITOSO" : "FALLIDO"));
            return isValid;
        } catch (SQLException e) {
            System.out.println("❌ Test de conexión FALLIDO: " + e.getMessage());
            return false;
        }
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error de Conexión", JOptionPane.ERROR_MESSAGE);
    }

    public static String getConnectionInfo() {
        if (connection == null) {
            return "No hay conexión activa";
        }

        try {
            return String.format(
                    "Conexión Oracle\nURL: %s\nUsuario: %s\nEstado: %s",
                    URL, USER, connection.isClosed() ? "Cerrada" : "Activa"
            );
        } catch (SQLException e) {
            return "Error obteniendo información de conexión";
        }
    }
}