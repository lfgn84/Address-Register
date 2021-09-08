import router.JMSRouteBuilder;
import router.SQLRouteBuilder;
import dataBase.DataBase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import service.IncomingAddressParser;


public class Main {
    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase();

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("dataSource", dataBase.dataSource());

        CamelContext messageReceiverContext = new DefaultCamelContext();
        CamelContext messageRecorderContext = new DefaultCamelContext(registry);


        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setTrustAllPackages(true);

        messageReceiverContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        messageReceiverContext.addRoutes(new JMSRouteBuilder());

        messageRecorderContext.addRoutes(new SQLRouteBuilder());





            while(true) {
                messageReceiverContext.start();
                messageRecorderContext.start();


                ConsumerTemplate consumerTemplate = messageReceiverContext.createConsumerTemplate();
                String mssg = consumerTemplate.receiveBody("seda:receivedMessage", String.class);

                System.out.println(mssg);
                IncomingAddressParser parser = new IncomingAddressParser(mssg.split(","));

                System.out.println(parser.getName()+" "+parser.getLastName()+" "+parser.getPn()+" "+parser.getStreetAddress()+" "+parser.getPostalCode()+" "+ parser.getPostalAddress() );


                ProducerTemplate producerTemplate = messageRecorderContext.createProducerTemplate();
                producerTemplate.sendBody("direct:start", "insert into address (first_name, last_name, p_n, street_address, postal_code, postal_address) values(\"" + parser.getName() + "\",\"" + parser.getLastName() + "\",\"" + parser.getPn() + "\",\"" + parser.getStreetAddress() + "\",\"" + parser.getPostalCode() + "\",\"" + parser.getPostalAddress() + "\")");


            }



    }
}
