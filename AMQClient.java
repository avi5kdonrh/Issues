
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.CountDownLatch;

public class AMQClient {
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String url = "(tcp://localhost:61616,tcp://localhost:61716)?reconnectAttempts=3";
        String user = "admin",password = "admin";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url,user,password);
        connectionFactory.setUseTopologyForLoadBalancing(false);
        Connection connection = connectionFactory.createConnection();
        connection.setExceptionListener(new CustomExceptionListener(connectionFactory,connection));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("input");
        session.createConsumer(queue).setMessageListener(message -> {
            try {
                System.out.println(">> NEW MESSAGE "+((TextMessage)message).getText());
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
     
        countDownLatch.await();
      
    }
}
