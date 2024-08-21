package pet.project.config;

import org.postgresql.ds.PGSimpleDataSource;

public class MyDataSource {
    private final static PGSimpleDataSource INSTANCE = new PGSimpleDataSource();

    static {

        INSTANCE.setURL("jdbc:postgresql://localhost:5432/postgres");
        INSTANCE.setUser("postgres");
        INSTANCE.setPassword("root");
    }

    private MyDataSource(){}

    public static PGSimpleDataSource getInstance(){
        return INSTANCE;
    }
}
