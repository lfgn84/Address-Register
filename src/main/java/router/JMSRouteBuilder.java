package router;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class JMSRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue:addressRegister").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                System.out.println(exchange.getIn().getBody() + "processing");
            }
        }).to("jdbc:dataSource");
    }
}
