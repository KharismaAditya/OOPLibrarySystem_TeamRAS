package Properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class databaseConnect {
    public Connection getConnection() throws IOException, SQLException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("D:/ADIT/MAK AKU KULIAH/SMT 2/PBO/PBO_A_KHARISMA_200/TUGAS AKHIR_PBO_KHARISMA ADITYA_200/src/com/tugasakhir/bookdata/accessible/config.properties");
        props.load(fis);

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
