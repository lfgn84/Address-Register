package router;

import org.apache.camel.builder.RouteBuilder;

public class SQLRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start").to("jdbc:dataSource");
    }
}
