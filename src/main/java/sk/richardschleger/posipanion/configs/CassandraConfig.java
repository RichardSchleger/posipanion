package sk.richardschleger.posipanion.configs;

import com.datastax.driver.core.AuthProvider;
import com.datastax.driver.core.PlainTextAuthProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration{
    
    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    protected String getContactPoints(){
        return contactPoints;
    }
    
    @Override
    protected int getPort(){
        return port;
    }

    @Override
    public SchemaAction getSchemaAction(){
        return SchemaAction.valueOf(schemaAction);
    }

    protected AuthProvider getAuthProvider() {
        return new PlainTextAuthProvider(username, password);
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"sk.richardschleger.posipanion"};
    }

}
