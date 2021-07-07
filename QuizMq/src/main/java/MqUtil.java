import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.bytebuddy.agent.VirtualMachine;

public class MqUtil {

    public static void main(String[] args) throws Exception {

    }

    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(1111);
        factory.setUsername("root1");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        return connection;
    }
}
