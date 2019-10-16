package nl.bp4mc2.swagger2rdf;

import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.FileOutputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Swagger2RDF {

  private static final Logger LOG = LoggerFactory.getLogger(Swagger2RDF.class);
  private ModelBuilder builder;

  public static void main(String[] args) {

    if (args.length == 1) {

      try {
        //Load and parse swaggerfile
        OpenAPI openAPI = new OpenAPIV3Parser().read(args[0]);
        if (openAPI!=null) {
          ModelBuilder builder = new ModelBuilder(openAPI);
          builder.build();
          Model model = builder.getModel();
          RDFDataMgr.write(new FileOutputStream("data/output.json"),model, RDFFormat.JSONLD_COMPACT_PRETTY);
          RDFDataMgr.write(new FileOutputStream("data/output.ttl"),model, RDFFormat.TURTLE_PRETTY);
          LOG.info("Done!");
        } else {
          LOG.error("Something went wrong :-(");
        }
      }
      catch (Exception e) {
        LOG.error(e.getMessage(),e);
      }

    } else {
      LOG.info("Usage: swagger2rdf <input.yaml or input.json>");
    }
  }
}
