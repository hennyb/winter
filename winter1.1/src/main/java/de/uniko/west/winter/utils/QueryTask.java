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

/**
 * 
 */
package de.uniko.west.winter.utils;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.exceptions.WinterException;
import de.uniko.west.winter.infostructure.InfoNode;
import de.uniko.west.winter.utils.interfaces.QueryResultListener;
import de.uniko.west.winter.utils.interfaces.RepositoryConnection;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;
import de.uniko.west.winter.utils.visitors.Dump2QueryPatternVisitor;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public abstract class QueryTask {

	private static transient Logger logger = LoggerFactory.getLogger(QueryTask.class.getSimpleName());
	
	protected URI graph = null;
	protected InfoNode node = null;
	protected Map<String, URI> prefixMap = null;
	protected QueryTaskExecutor executor = null;
	protected StringBuilder sb = null;
	protected String[] vars = null;
	
	
	
	protected void insertPrefix() {
		if (prefixMap == null){
			return;
		}
		for (String prefix : prefixMap.keySet()) {
			if (containsPrefix(prefix)){
				sb.insert(0, "PREFIX " + prefix + ": <"+ prefixMap.get(prefix).toString()+">\n");	
			}
		}
	}
	
	
	/**
	 * checks if prefix occurs in the form
	 * '<prefix:' OR '^prefix:'
	 * 
	 * @param prefix to be checked for occurance
	 * @return
	 */
	private boolean containsPrefix(String prefix){
		return sb.toString().contains("^"+ prefix + ":") || sb.toString().contains("<"+ prefix + ":");
	}
	
	protected void insertGraph(){
		if (graph == null){
			return;
		}
		sb.insert(0, "GRAPH  <" + graph.toString() + "> {");
		sb.append(" }");
	}
	
	public void visitQueryPattern(SPARQLPatternParserVisitor... parserVisitors){
		logger.debug("Initializing parsed query pattern");
		ASTQueryContainer queryPatternContainer = null;
		System.out.println("PARSING: \n" + node.getQueryPattern());
		try {
			queryPatternContainer = SPARQLPatternParser.parsePattern(node.getQueryPattern());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO add some text
			throw new WinterException(e);
		}
		
		for (SPARQLPatternParserVisitor visitor : parserVisitors){
			try {
				queryPatternContainer.jjtAccept(visitor, null);
			} catch (VisitorException e) {
				e.printStackTrace();
				throw new WinterException();
			}
		}
		
		Dump2QueryPatternVisitor visitor = new Dump2QueryPatternVisitor();
		
		try {
			sb = new StringBuilder( (String) queryPatternContainer.jjtAccept(visitor, "") );
		} catch (VisitorException e) {
			e.printStackTrace();
			// TODO add some text
			throw new WinterException(e);
		}		
		
	}
	
	public abstract void execute(RepositoryConnection<?> repCon, QueryResultListener listener);
	
	public String getQuery(){
		logger.info("Query requested... Query so far:\n{}", sb.toString());
		return sb.toString();
	}
}