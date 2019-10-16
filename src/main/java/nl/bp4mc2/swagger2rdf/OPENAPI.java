package nl.bp4mc2.swagger2rdf;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class OPENAPI {

  public static final String uri = "http://bp4mc2.org/def/openapi-v3#";

  public static String getUri() {
    return uri;
  }

  protected static final Resource resource( String local ) {
    return ResourceFactory.createResource( uri + local );
  }

  protected static final Property property( String local ) {
    return ResourceFactory.createProperty( uri, local );
  }

  public static final Resource Api             = Init.Api();
  public static final Resource Server          = Init.Server();
  public static final Resource Path            = Init.Path();
  public static final Resource Operation       = Init.Operation();
  public static final Property openapi         = Init.openapi();
  public static final Property version         = Init.version();
  public static final Property title           = Init.title();
  public static final Property description     = Init.description();
  public static final Property server          = Init.server();
  public static final Property path            = Init.path();
  public static final Property pattern         = Init.pattern();
  public static final Property summary         = Init.summary();
  public static final Property getOperation    = Init.getOperation();
  public static final Property putOperation    = Init.putOperation();
  public static final Property postOperation   = Init.postOperation();
  public static final Property deleteOperation = Init.deleteOperation();

  public static class Init {
    public static Resource Api()             { return resource("Api"); }
    public static Resource Server()          { return resource("Server"); }
    public static Resource Path()            { return resource("Path"); }
    public static Resource Operation()       { return resource("Operation"); }
    public static Property openapi()         { return property("openapi"); }
    public static Property version()         { return property("version"); }
    public static Property title()           { return property("title"); }
    public static Property description()     { return property("description"); }
    public static Property server()          { return property("server"); }
    public static Property path()            { return property("path"); }
    public static Property summary()         { return property("summary"); }
    public static Property pattern()         { return property("pattern"); }
    public static Property getOperation()    { return property("getOperation"); }
    public static Property putOperation()    { return property("putOperation"); }
    public static Property postOperation()   { return property("postOperation"); }
    public static Property deleteOperation() { return property("deleteOperation"); }
  }

}
