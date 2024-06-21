
package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    private static final Logger logger = Logger.getLogger("Log");
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=BookStore";
    private static final String USER = "sa";
    private static final String PASS = "12345";

    private static final int POOL_SIZE = 10;
    private static BlockingQueue<Connection> connectionPool;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                connectionPool.offer(conn);
            }
            logger.log(Level.INFO, "Connection pool initialized with {0} connections.", POOL_SIZE);
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Error initializing connection pool.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = connectionPool.take();
            logger.log(Level.INFO, "One connection retrieved from pool.");
            logger.log(Level.INFO, "Available connections: {0}", connectionPool.size());
            return connection;
        } catch (InterruptedException e) {
            throw new SQLException("Failed to get a connection from the pool.", e);
        }
    }

    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.offer(connection);
            logger.log(Level.INFO, "One Connection returned to pool. Available connections: " + connectionPool.size());
        }
    }
}
