import Router.JMSRouteBuilder;
import Router.SQLRouteBuilder;
import dataBase.DataBase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;


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

                String [] incomingAddressParser = mssg.split(",");

                int id = Integer.parseInt(incomingAddressParser[0]);
                String name = incomingAddressParser[1].trim();
                String lastName = incomingAddressParser[2].trim();
                String pn = incomingAddressParser[3].trim();
                String street = incomingAddressParser[4].trim();
                String postalCode = incomingAddressParser[5].trim();
                String postalAddress = incomingAddressParser[6].trim();

                System.out.println(name+" "+lastName+" "+pn+" "+street+" "+postalCode+" "+postalAddress );


                ProducerTemplate producerTemplate = messageRecorderContext.createProducerTemplate();
                producerTemplate.sendBody("direct:start", "insert into address values("+id+",\"" + name + "\",\"" + lastName + "\",\"" + pn + "\",\"" + street + "\",\"" + postalCode + "\",\"" + postalAddress + "\")");
            }




    }
}
