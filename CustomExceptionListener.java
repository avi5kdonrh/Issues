package com.mycompany.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

public class CustomExceptionListener implements ExceptionListener {
    private static Logger logger  = LoggerFactory.getLogger(CustomExceptionListener.class);
    private Connection connection;
    private final ConnectionFactory connectionFactory;
    public CustomExceptionListener(ConnectionFactory connectionFactory,Connection connection) {
        this.connectionFactory = connectionFactory;
        this.connection = connection;
    }
    @Override
    public void onException(JMSException exception) {
        logger.info("In Exception listener >>>>> "+exception.getMessage());
        try {
            if(connection != null) {
                connection.close();
                if(connectionFactory != null) {
                    connection = connectionFactory.createConnection();
                    connection.setExceptionListener(this);
                    connection.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
