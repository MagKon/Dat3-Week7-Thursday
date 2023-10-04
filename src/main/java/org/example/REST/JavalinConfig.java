package org.example.REST;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.RouteOverviewPlugin;

/**
 * This class is used to create a Javalin instance.
 */
public class JavalinConfig {

    /**
     * Creates a Javalin instance with the given context path and default content type. This method is used to create a Javalin instance for the REST API.
     * @param contextPath The context path for the Javalin instance. This should NEVER be null or empty.
     * @param defaultContentType The default content type for the Javalin instance. This can be null or empty, in which case it will default to "application/json".
     * @return A Javalin instance with the given context path and default content type that is ready to be used on port 7007.
     */
    public static Javalin create(String contextPath, String defaultContentType) {
        Javalin app = Javalin.create(config -> {
            config.routing.contextPath = "/api"; // base path for all routes
            if (defaultContentType != null && !defaultContentType.isEmpty()) {
                config.http.defaultContentType = defaultContentType; // default content type for requests
            }
            else {
                config.http.defaultContentType = "application/json"; // default content type for requests
            }
            config.plugins.enableDevLogging(); // enables extensive development logging in terminal
            config.plugins.register(new RouteOverviewPlugin("/routes")); // html overview of all registered routes at /routes for api documentation: https://javalin.io/news/2019/08/11/javalin-3.4.1-released.html
        });

        app.start(7007);

        return app;
    }
}
