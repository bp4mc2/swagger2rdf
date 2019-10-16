# swagger2rdf
Converts a swagger file (v3, json or yaml) to rdf

## Usage

swagger2rdf <input.json or input.yaml>

## Extensions

Extensions can be mapped to RDF as well. For this, you need to add an extension to the input file at root level:

```
openapi: 3.0.0
x-bp4mc2-context:
  x-some-extension:
    url: http://some-extension-url
    type: '@id' //Set to @id when the value is an IRI. Ommit the type element for string values.
```
