package eu.esens.espdvcd.designer.util;

import spark.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ServerUtil {
    private final static Logger LOGGER = Logger.getLogger(ServerUtil.class.getName());
    private static Map<String, String> securityHeaders;

    /**
     * Configures static file hosting
     * Sets the location and the security headers
     * @param spark Current Spark instance
     */
    public static void configureStaticFiles(Service spark){
        spark.staticFiles.location("/public");
        spark.staticFiles.headers(getSecurityHeaders());
    }

    /**
     * Enables Cross Origin Requests
     * While in production this should be disabled
     * @param spark Current Spark instance
     */
    public static void enableCORS(Service spark) {
        spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null)
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null)
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);

            return "OK";
        });

        spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Content-Disposition");
        });
    }

    /**
     * Redirect URLs with trailing slashes back to the "unslashed" routes
     * @param spark Current Spark instance
     */
    public static void dropTrailingSlashes(Service spark) {
        spark.before((req, res) -> {
            String path = req.pathInfo();
            if (path.endsWith("/"))
                res.redirect(path.substring(0, path.length() - 1), 301);
        });
    }

    /**
     * Adds headers to all routes
     * @param spark Current Spark instance
     */
    public static void addSecurityHeaders(Service spark) {
        spark.before(((request, response) -> getSecurityHeaders().forEach(response::header)));
    }

    /**
     * Gets the security headers
     * @return Header Map
     */
    private static Map<String, String> getSecurityHeaders(){
        if(securityHeaders == null){
            initHeaderMap();
        }
        return securityHeaders;
    }

    /**
     * Lazy initialization for the header HashMap
     */
    private static void initHeaderMap(){
        securityHeaders = new HashMap<>();

        String contentSecurityPolicyHeaders = "default-src 'self' http://83.212.109.183/ https://europa.eu/ http://ec.europa.eu/; " +
                "font-src https://fonts.googleapis.com https://fonts.gstatic.com; " +
                "img-src 'self' ;" +
                "object-src 'none'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' ; " +
                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com";
        if(Config.isFramingAllowed()){
            securityHeaders.put("Content-Security-Policy", contentSecurityPolicyHeaders);
        }
        else {
            securityHeaders.put("Content-Security-Policy", contentSecurityPolicyHeaders+ " ; frame-ancestors 'none'");
            securityHeaders.put("X-Frame-Options", "DENY");
        }
        securityHeaders.put("X-XSS-Protection", "1; mode=block");
        securityHeaders.put("X-Content-Type-Options", "nosniff");
        securityHeaders.put("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");
    }
}
