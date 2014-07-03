/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.uniko.west.winter.test.basics;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import org.apache.log4j.lf5.util.Resource;
import org.json.JSONObject;
import org.openjena.atlas.json.JsonObject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.resultset.ResultSetFormat;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;



public class JenaTests {
	public static void main(String[] args) {
		
		Model newModel = ModelFactory.createDefaultModel();
//		
		Model newModel2 = ModelFactory.createModelForGraph(ModelFactory.createMemModelMaker().getGraphMaker().createGraph("http://www.defaultgraph.de/graph1"));
		StringBuilder updateString = new StringBuilder();
		updateString.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		updateString.append("PREFIX xsd: <http://bla.org/dc/elements/1.1/>");
		updateString.append("INSERT { ");
		updateString.append("<http://example/egbook1> dc:title  <http://example/egbook1/#Title1>, <http://example/egbook1/#Title2>. ");
		//updateString.append("<http://example/egbook1> dc:title  \"Title1.1\". ");
		//updateString.append("<http://example/egbook1> dc:title  \"Title1.2\". ");
		updateString.append("<http://example/egbook21> dc:title  \"Title2\"; ");
		updateString.append("dc:title  \"2.0\"^^xsd:double. ");
		updateString.append("<http://example/egbook3> dc:title  \"Title3\". ");
		updateString.append("<http://example/egbook4> dc:title  \"Title4\". ");
		updateString.append("<http://example/egbook5> dc:title  \"Title5\". ");
		updateString.append("<http://example/egbook6> dc:title  \"Title6\" ");
		updateString.append("}");
		
		
		UpdateRequest update = UpdateFactory.create(updateString.toString());
		UpdateAction.execute(update, newModel);
		
		StmtIterator iter = newModel.listStatements();
		System.out.println("After add");
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
			
		}
		
		StringBuilder constructQueryString = new StringBuilder();
		constructQueryString.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		constructQueryString.append("CONSTRUCT {?sub dc:title <http://example/egbook1/#Title1>}");
		constructQueryString.append("WHERE {");
		constructQueryString.append("?sub dc:title <http://example/egbook1/#Title1>");
		constructQueryString.append("}");
		
		StringBuilder askQueryString = new StringBuilder();
		askQueryString.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		askQueryString.append("ASK {");
		askQueryString.append("?sub dc:title <http://example/egbook1/#Title1>");
		askQueryString.append("}");
		
		StringBuilder selectQueryString = new StringBuilder();
		selectQueryString.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		selectQueryString.append("SELECT * ");
		selectQueryString.append("WHERE {");
		selectQueryString.append("?sub ?pred ?obj");
		selectQueryString.append("}");
		
		Query cquery = QueryFactory.create(constructQueryString.toString());
		System.out.println(cquery.getQueryType());
		Query aquery = QueryFactory.create(askQueryString.toString());
		System.out.println(aquery.getQueryType());
		Query query = QueryFactory.create(selectQueryString.toString());
		System.out.println(query.getQueryType());
		QueryExecution queryExecution = QueryExecutionFactory.create(query, newModel);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		URI test = null;
		try {
			test = new URI("http://bla.org/dc/elements/1.1/double");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(test.getPath().toString().substring(test.getPath().lastIndexOf("/")+1));
		System.out.println("java.lang."+test.getPath().toString().substring(test.getPath().lastIndexOf("/")+1).substring(0,1).toUpperCase()+test.getPath().toString().substring(test.getPath().lastIndexOf("/")+1).substring(1));
		
		
		String typ = "java.lang.Boolean";
		String val = "true";
			
		try {
			Object typedLiteral = Class.forName(typ, 
					true, 
					ClassLoader.getSystemClassLoader())
						.getConstructor(String.class).newInstance(val);
			
			System.out.println("Type: "+typedLiteral.getClass().getName()+" Value: "+typedLiteral);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		try {
			System.out.println("Query...");
			com.hp.hpl.jena.query.ResultSet results = queryExecution.execSelect();
			System.out.println("RESULT:");
			ResultSetFormatter.output(System.out, results, ResultSetFormat.syntaxJSON);
//			
//			
//			ResultSetFormatter.outputAsJSON(baos, results);
//			System.out.println(baos.toString());
//			System.out.println("JsonTest: ");
//			JSONObject result = new JSONObject(baos.toString("ISO-8859-1"));
//			for (Iterator key = result.keys(); result.keys().hasNext(); ){
//				System.out.println(key.next());
//				
//				for (Iterator key2 = ((JSONObject)result.getJSONObject("head")).keys(); key2.hasNext(); ){
//					System.out.println(key2.next());
//				}
//			}
			
	
			
//			Model results = queryExecution.execConstruct();
			
//			results.write(System.out, "TURTLE");
//			for ( ; results.hasNext() ; ){
//				QuerySolution soln = results.nextSolution() ;
//				RDFNode x = soln.get("sub") ;   
//				System.out.println("result: "+soln.get("sub")+" hasTitle "+soln.get("obj"));
//			    Resource r = soln.getResource("VarR") ; 
//			    Literal l = soln.getLiteral("VarL") ; 
//
//			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		
	
		
//		StringBuilder updateString2 = new StringBuilder();
//		updateString2.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
//		updateString2.append("DELETE DATA { ");
//		updateString2.append("<http://example/egbook3> dc:title  \"Title3\" ");
//		updateString2.append("}");
//
//		UpdateAction.parseExecute(updateString2.toString(), newModel);
//		
//		iter = newModel.listStatements();
//		System.out.println("After delete");
//		while (iter.hasNext()) {
//			System.out.println(iter.next().toString());
//				
//		}
//		
//		StringBuilder updateString3 = new StringBuilder();
//		updateString3.append("PREFIX dc: <http://purl.org/dc/elements/1.1/>");
//		updateString3.append("DELETE DATA { ");
//		updateString3.append("<http://example/egbook6> dc:title  \"Title6\" ");
//		updateString3.append("}");
//		updateString3.append("INSERT { ");
//		updateString3.append("<http://example/egbook6> dc:title  \"New Title6\" ");
//		updateString3.append("}");
//	
//		UpdateAction.parseExecute(updateString3.toString(), newModel);
		
		
		
//		UpdateAction.parseExecute(	"prefix exp: <http://www.example.de>"+
//									"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
//									"INSERT { graph <http://www.defaultgraph.de/graph1> {"+
//									"	<http://www.test.de#substructure1> <exp:has_relation3> <http://www.test.de#substructure2> ."+ 
//									"	<http://www.test.de#substructure1> <rdf:type> <http://www.test.de#substructuretype1> ."+ 
//									"	<http://www.test.de#substructure2> <rdf:type> <http://www.test.de#substructuretype2> ."+
//									"}}", newModel2);
//		
//		iter = newModel.listStatements();
//		System.out.println("After update");
//		while (iter.hasNext()) {
//			System.out.println(iter.next().toString());
//				
//		}
	}
}
