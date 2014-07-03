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

package de.uniko.west.winter.utils.constants;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Constants for RDF primitives and for the RDF namespace.
 */

public class RDF {
	/** http://www.w3.org/1999/02/22-rdf-syntax-ns# */
    public static final String NAMESPACE =
            "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#type */
    public final static URI TYPE;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#Property */
    public final static URI PROPERTY;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral */
    public final static URI XMLLITERAL;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#subject */
    public final static URI SUBJECT;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate */
    public final static URI PREDICATE;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#object */
    public final static URI OBJECT;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement */
    public final static URI STATEMENT;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#Bag */
    public final static URI BAG;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#Alt */
    public final static URI ALT;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#Seq */
    public final static URI SEQ;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#value */
    public final static URI VALUE;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#li */
    public final static URI LI;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#List */
    public final static URI LIST;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#first */
    public final static URI FIRST;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#rest */
    public final static URI REST;

    /** http://www.w3.org/1999/02/22-rdf-syntax-ns#nil */
    public final static URI NIL;
    
    
    static{
        TYPE = assignURI(RDF.NAMESPACE, "type");
        PROPERTY = assignURI(RDF.NAMESPACE, "Property");
        XMLLITERAL = assignURI(RDF.NAMESPACE, "XMLLiteral");
        SUBJECT = assignURI(RDF.NAMESPACE, "subject");
        PREDICATE = assignURI(RDF.NAMESPACE, "predicate");
        OBJECT = assignURI(RDF.NAMESPACE, "object");
        STATEMENT = assignURI(RDF.NAMESPACE, "Statement");
        BAG = assignURI(RDF.NAMESPACE, "Bag");
        ALT = assignURI(RDF.NAMESPACE, "Alt");
        SEQ = assignURI(RDF.NAMESPACE, "Seq");
        VALUE = assignURI(RDF.NAMESPACE, "value");
        LI = assignURI(RDF.NAMESPACE, "li");
        LIST = assignURI(RDF.NAMESPACE, "List");
        FIRST = assignURI(RDF.NAMESPACE, "first");
        REST = assignURI(RDF.NAMESPACE, "rest");
        NIL = assignURI(RDF.NAMESPACE, "nil");
    }
    
    public static URI assignURI (String namespace, String uriString){
    	URI uri = null;
    	try {
    		 uri = new URI(namespace+uriString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (uri != null) return uri;
		return null;
    }
}
