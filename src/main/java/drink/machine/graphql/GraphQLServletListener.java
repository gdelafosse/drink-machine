package drink.machine.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import drink.machine.drinks.DrinkService;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import graphql.servlet.SimpleGraphQLServlet.Builder;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

@WebListener
public class GraphQLServletListener implements ServletContextListener {

    @Inject
    private DrinkService drinkService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("####### GRAPHQL ########");
            GraphQLSchema schema = SchemaParser.newParser()
                    .schemaString(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("/graphql/drink-machine.graphqls")))
                    .resolvers(new QueryResolver(drinkService))
                    .build().makeExecutableSchema();

            sce.getServletContext().addServlet("graphql",
                    new Builder(schema)
                            .build())
            .addMapping("/graphql");


        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
