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
package de.uniko.west.winter.test.infostructure;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.uniko.west.winter.core.JenaInit;
import de.uniko.west.winter.core.WinterImpl;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;
import de.uniko.west.winter.utils.repcon.JenaRepositoryConnection;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class TestInfoStructure{
	
	public Vector<RDFSerializable> init(){
		Vector<RDFSerializable> serializables = new Vector<RDFSerializable>();
//		serializables.add(new SmallObject());
//		serializables.add(new LargeObject("Object1"));
		serializables.add(new LargeObject("Object2"));
//		serializables.add(new RelationObject(new LargeObject("Object3")));
//		serializables.add(new SubrelationObject(new LargeObject("Object4")));
		return serializables;
	}
	
	public static void main(String[] args) {
		new TestInfoStructure().run();
	}
	
	public void run() {
		
			
		Vector<RDFSerializable> serializables  = init();
		
//		Winter.init(new JenaInit(), URI.create("http://www.testbeispiel.com/abc"));
		Map<String, URI> prefixMap = new Hashtable<String, URI>();
		prefixMap.put("rdf", URI.create("www.test.de/"));
		prefixMap.put("xsd", URI.create("www.test2.de/"));
		Model model = ModelFactory.createDefaultModel();
		WinterImpl.init(new JenaInit(
				new JenaRepositoryConnection(model),
				prefixMap), null);
		
		LargeObject lobj = new LargeObject("Object1");
		
		WinterImpl.getSharedInstance().add(lobj);
		
		System.out.println(" == REPO AFTER ADD == ");
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
		System.out.println();
		
		lobj.winter.deleteField("name");

		System.out.println(" == REPO AFTER REMOVE NAMEFIELD== ");
		iter = model.listStatements();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
		
		lobj.winter.writeField("name");
		
		System.out.println(" == REPO AFTER WRITE NAMEFIELD== ");
		iter = model.listStatements();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
		
//		lobj.winter.delete();
//		Winter.deleteRek(lobj);
		
//		System.out.println(" == REPO AFTER REMOVE LARGEOBJECT == ");
//		iter = model.listStatements();
//		while (iter.hasNext()) {
//			System.out.println(iter.next().toString());
//		}
		
		lobj.winter.readField("name");
		
		System.out.println(" == REPO AFTER WRITE NAMEFIELD== ");
		iter = model.listStatements();
		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
		System.out.println(lobj.id);
	}
}
