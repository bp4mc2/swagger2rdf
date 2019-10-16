package nl.bp4mc2.swagger2rdf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Map;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelBuilder {

  private static final Logger LOG = LoggerFactory.getLogger(ModelBuilder.class);

  private static final Model model = ModelFactory.createDefaultModel();

  private OpenAPI openAPI;
  private Resource apiResource;
  private String apiNamespace = "urn:api";
  private Map<String, Object> extensionMap = null;

  public ModelBuilder(OpenAPI openAPI) {
    this.openAPI = openAPI;
  }

  private static void addStatement(Resource subject, Property property, String object) {
    if (object!=null) {
      subject.addProperty(property, object);
    }
  }

  public void build() {
    if (!openAPI.getServers().isEmpty()) {
      apiNamespace = openAPI.getServers().get(0).getUrl();
    }
    apiResource = model.createResource(apiNamespace + "#");
    model.setNsPrefix("openapi",OPENAPI.getUri());
    Map<String, Object> extensions = openAPI.getExtensions();
    if (extensions!=null) {
      extensionMap = (Map<String, Object>)extensions.get("x-bp4mc2-context");
    }
    if (extensionMap==null) {
      LOG.warn("No extension map found (x-bp4mc2-context)");
    }
    buildRoot();
    buildInfo();
    buildServers();
    buildPaths();
  }

  private void buildRoot() {
    apiResource.addProperty(RDF.type, OPENAPI.Api);
    addStatement(apiResource, OPENAPI.openapi, openAPI.getOpenapi());
  }

  private void buildInfo() {
    apiResource.addProperty(OPENAPI.version, openAPI.getInfo().getVersion());
    addStatement(apiResource, OPENAPI.title, openAPI.getInfo().getTitle());
    addStatement(apiResource, OPENAPI.description, openAPI.getInfo().getDescription());
  }

  private void buildServers() {
    for (Server server : openAPI.getServers()) {
      Resource serverResource = model.createResource(server.getUrl());
      apiResource.addProperty(OPENAPI.server, serverResource);
      serverResource.addProperty(RDF.type, OPENAPI.Server);
    }
  }

  private void buildPaths() {
    for (Map.Entry<String, PathItem> path : openAPI.getPaths().entrySet()) {
      Resource pathResource = model.createResource(apiNamespace + path.getKey());
      apiResource.addProperty(OPENAPI.path, pathResource);
      pathResource.addProperty(RDF.type, OPENAPI.Path);
      pathResource.addProperty(OPENAPI.pattern, path.getKey());
      addStatement(pathResource, OPENAPI.description, path.getValue().getDescription());
      addStatement(pathResource, OPENAPI.summary, path.getValue().getSummary());
      addOperation(pathResource, OPENAPI.getOperation, "#get", path.getValue().getGet());
      addOperation(pathResource, OPENAPI.putOperation, "#put", path.getValue().getPut());
      addOperation(pathResource, OPENAPI.postOperation, "#post", path.getValue().getPost());
      addOperation(pathResource, OPENAPI.deleteOperation, "#delete", path.getValue().getDelete());
    }
  }

  private void addOperation(Resource pathResource, Property property, String localname, Operation operation) {
    if (operation!=null) {
      Resource operationResource = model.createResource(pathResource.getURI() + localname);
      pathResource.addProperty(property, operationResource);
      operationResource.addProperty(RDF.type, OPENAPI.Operation);
      addStatement(operationResource, OPENAPI.description, operation.getDescription());
      addStatement(operationResource, OPENAPI.summary, operation.getSummary());
      Map<String, Object> extensions = operation.getExtensions();
      if (extensions!=null) {
        for (Map.Entry<String, Object> extension : extensions.entrySet()) {
          if (extensionMap!=null) {
            Map<String, Object> extensionMapItem = (Map<String, Object>)extensionMap.get(extension.getKey());
            if (extensionMapItem!=null) {
              Property extensionProperty = model.createProperty(extensionMapItem.get("url").toString());
              LOG.info("Extension item found: " + extension.getKey());
              if (extensionMapItem.get("type")==null) {
                operationResource.addProperty(extensionProperty, extension.getValue().toString());
              } else {
                operationResource.addProperty(extensionProperty, model.createResource(extension.getValue().toString()));
              }
            } else {
              LOG.warn("Unmapped extension found:" + extension.getKey());
            }
          } else {
            LOG.warn("Unmapped extension found:" + extension.getKey());
          }
        }
      }
    }
  }

  public Model getModel() {
    return model;
  }

}
