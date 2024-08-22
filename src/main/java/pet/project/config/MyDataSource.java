package pet.project.config;

import org.postgresql.ds.PGSimpleDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyDataSource {
    private final static PGSimpleDataSource INSTANCE = new PGSimpleDataSource();
    static {
        String url;
        String username;
        String password;

        try (InputStream input = MyDataSource.class.getResourceAsStream("/db.properties")){
            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        INSTANCE.setURL(url);
        INSTANCE.setUser(username);
        INSTANCE.setPassword(password);
    }

    private MyDataSource(){}

    public static PGSimpleDataSource getInstance(){
        return INSTANCE;
    }
}
