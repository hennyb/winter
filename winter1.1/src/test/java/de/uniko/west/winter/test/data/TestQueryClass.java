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
package de.uniko.west.winter.test.data;

import java.util.HashSet;
import java.util.Set;

import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;
import de.uniko.west.winter.utils.visitors.SelectVisitor;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * @date 03.11.2010
 *
 */
public class TestQueryClass {
	
	// URIs
	protected static final String URI1 = "<http://uri1.com>";
	protected static final String URI2 = "<http://uri2.com>";
	protected static final String URI3 = "<http://uri3.com>";
	protected static final String URI4 = "<http://uri4.com>";
	protected static final String URI5 = "<http://uri5.com>";
	protected static final String URI6 = "<http://uri6.com>";
	protected static final String URI7 = "<http://uri7.com>";
	
	// Variables
	protected static final String VAR1 = "?var1";
	protected static final String VAR2 = "?var2";
	protected static final String VAR3 = "?var3";
	protected static final String VAR4 = "?var4";
	protected static final String VAR5 = "?var5";
	protected static final String VAR6 = "?var6";
	protected static final String VAR7 = "?var7";
	
	// PREDICATEs
	protected static final String PRED1 = "exp:pred1";
	protected static final String PRED2 = "exp:pred2";
	protected static final String PRED3 = "exp:pred3";
	protected static final String PRED4 = "exp:pred4";
	protected static final String PRED5 = "exp:pred5";
	
	// Query 1
	protected static final String QUERY1 = 
						URI1 + " " + PRED1 + " " + URI2 + " .\n" +
						URI2 + " " + PRED4 + " " + URI4 + " .\n" +
						URI3 + " " + PRED2 + " " + URI2 + ", " + URI4 + " .\n" +
						URI5 + " " + PRED3 + " " + URI3 + ";\n"+
									 PRED5 + " " + URI4 + ", " + URI1 + ".";
	
	// Query 2
	protected static final String QUERY2 = 
		VAR2 + " " + PRED3 + " " + URI1 + " .\n" +
		URI1 + " " + PRED1 + " " + URI2 + " .\n" +
		URI2 + " " + PRED4 + " " + URI4 + " .\n" +
		URI3 + " " + PRED2 + " " + VAR2 + ", " + URI4 + " .\n" +
		URI5 + " " + PRED3 + " " + URI3 + ";\n" +
					 PRED5 + " " + VAR5 + ", " + URI1 + ";\n" +
					 PRED1 + " " + VAR3 + ", " + URI5 + ".";
	
	protected static final String QUERY3 =
		VAR1 + " " + PRED1 + " " + VAR2 + ", " + VAR3 + ";\n" + 
					 PRED2 + " " + VAR3 + ", " + VAR4 + ".\n" + 
		VAR2 + " " + PRED3 + " " + VAR3 + ";\n" +
					 PRED4 + " " + VAR5 + ";\n" +
					 PRED5 + " " + VAR1 + ", " + VAR4 + ".\n" +
		VAR3 + " " + PRED3 + " " + VAR5 + ", " + VAR6 + ", " + VAR7 + ";\n" +
					 PRED1 + " " + VAR2 + ", " + VAR4 + ", " + VAR7 + ".\n";
	
	
	protected static Set<String> setWithVars1 = new HashSet<String>();
	protected static Set<String> setWithVars2 = new HashSet<String>();
	protected static Set<String> setWithVars3 = new HashSet<String>();
	protected static Set<String> setWithVars4 = new HashSet<String>();
	protected static Set<String> setWithVars5 = new HashSet<String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(QUERY1);
		System.out.println("*******************");
		System.out.println();
		
		System.out.println(QUERY2);
		System.out.println("*******************");
		System.out.println();
		
		ASTQueryContainer qContainer1 = null;
		ASTQueryContainer qContainer2 = null;
		ASTQueryContainer qContainer3 = null;
		
		ASTQueryContainer editedQuery1 = null;
		ASTQueryContainer editedQuery2 = null;
		ASTQueryContainer editedQuery3 = null;
		try {
			qContainer1 = SPARQLPatternParser.parsePattern(QUERY1);
			qContainer2 = SPARQLPatternParser.parsePattern(QUERY2);
			qContainer3 = SPARQLPatternParser.parsePattern(QUERY3);
		} catch (Exception e) {
			System.out.print(">>> [OOPS] ");
			e.printStackTrace();
			System.out.println("<<<");
		}
//		qContainer1.dump("1>>");
//		System.out.println("1<<<<<<<<<<<<");
		qContainer3.dump("3>>");
		System.out.println("3<<<<<<<<<<<<<");
		
		setWithVars1.add("var2");
		
		setWithVars2.add("var1");
		setWithVars2.add("var2");
		setWithVars2.add("var3");
		
		setWithVars3.add("var4");
		setWithVars3.add("var7");
		SelectVisitor selectVisitor1 = new SelectVisitor(setWithVars1);
		SelectVisitor selectVisitor2 = new SelectVisitor(setWithVars2);
		SelectVisitor selectVisitor3 = new SelectVisitor(setWithVars3);
		
		
		try {
			editedQuery1 = (ASTQueryContainer)qContainer3.jjtAccept(selectVisitor1, null);
			editedQuery2 = (ASTQueryContainer)qContainer3.jjtAccept(selectVisitor2, null);
			editedQuery3 = (ASTQueryContainer)qContainer3.jjtAccept(selectVisitor3, null);
		} catch (VisitorException e) {
			e.printStackTrace();
		}
		editedQuery1.dump("[E1]");
		System.out.println("[E1]<<<<<<<<<<<<");
		System.out.println();
		editedQuery2.dump("[E2]");
		System.out.println("[E2]<<<<<<<<<<<<");
		System.out.println();
		editedQuery3.dump("[E3]");
		System.out.println("[E3]<<<<<<<<<<<<");
		
		System.out.println(qContainer3.dumpToString(false, false));
		System.out.println(editedQuery2.dumpToString(true, true));

	}

}
