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
package de.uniko.west.winter.test.utils.visitors;


import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import de.uniko.west.winter.test.data.TestData;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;
import de.uniko.west.winter.utils.visitors.SelectVisitor;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * @date 12.11.2010
 *
 */
public class SelectVisitorTest {
	
	@Test(expected=ParseException.class)
	public void testVistitQCon1() throws ParseException, VisitorException {
		SelectVisitor selectVis = new SelectVisitor(setSetWithVars(TestData.URI1));
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.failQuery1);
		TestData.qCon1.jjtAccept(selectVis, null);
	}
	
	// Query1 and SelectVisitor with URI1
	// Should be returned empty QueryContainer
	@Test
	public void testVisitQCon2() throws TokenMgrError, ParseException, VisitorException{
		SelectVisitor selectVis = new SelectVisitor(setSetWithVars(TestData.URI1));
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query1);
		TestData.qCon1 = (ASTQueryContainer)TestData.qCon1.jjtAccept(selectVis, null);
		assertTrue(TestData.qCon1.jjtGetNumChildren()==0);
	}
	
	// Query1 and SelectVisitor with empty Set
	// Should be returned empty QueryContainer
	@Test
	public void testVisitQCon2a() throws TokenMgrError, ParseException, VisitorException {
		SelectVisitor selectVis = new SelectVisitor(new HashSet<String>());
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query1);
		TestData.qCon1 = (ASTQueryContainer)TestData.qCon1.jjtAccept(selectVis, null);
		assertTrue(TestData.qCon1.jjtGetNumChildren()==0);
	}
	
	// Query2 and SelectVisitor with VAR2
	@Test
	public void testVisitQCon3a() throws TokenMgrError, ParseException, VisitorException {
		SelectVisitor selectVis = new SelectVisitor(setSetWithVars("var2"));
		
		String query2 = 
			TestData.VAR2 + " " + TestData.PRED3 + " " + TestData.URI1 + " .\n" +
			TestData.URI3 + " " + TestData.PRED2 + " " + TestData.VAR2 + " .\n";
		
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query2);
		TestData.qCon2 = SPARQLPatternParser.parsePattern(query2);
		TestData.qCon1 = (ASTQueryContainer)TestData.qCon1.jjtAccept(selectVis, null);
//		System.out.println(TestData.qCon1.dumpToString(false, false));
//		System.out.println("***********");
		assertTrue(TestData.qCon1.jjtCompareTo(TestData.qCon2));
	}
	
	// Query2 and SelectVisitor with VAR2
	@Ignore
	public void testVisitQCon3b() throws TokenMgrError, ParseException, VisitorException {
		SelectVisitor selectVis = new SelectVisitor(setSetWithVars("var2"));
		
		String query2 = 
			TestData.VAR2 + " " + TestData.PRED3 + " " + TestData.URI1 + " .\n" +
			TestData.URI3 + " " + TestData.PRED2 + " " + TestData.VAR2 + " .\n";
		
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query2);
		TestData.qCon2 = SPARQLPatternParser.parsePattern(query2);
		TestData.qCon1.jjtAccept(selectVis, null);
		System.out.println(TestData.qCon1.dumpToString(false, false));
		assertTrue(TestData.qCon1.jjtCompareTo(TestData.qCon2));
	}
	
	// Query3 and SelectVisitor with VAR4, VAR5, VAR7
	@Test
	public void testVisitQCon4() throws TokenMgrError, ParseException, VisitorException {
		String query3 =
			TestData.VAR1 + " " + TestData.PRED2 + " " + TestData.VAR4 + ".\n" + 
			TestData.VAR2 + " " + TestData.PRED4 + " " + TestData.VAR5 + ";\n" +
			TestData.PRED5 + " " + TestData.VAR4 + ".\n" +
			TestData.VAR3 + " " + TestData.PRED3 + " " + TestData.VAR5 + ", " + TestData.VAR7 + ";\n" +
			TestData.PRED1 + " " + TestData.VAR4 + ", " + TestData.VAR7 + ".\n";
		
		SelectVisitor selectVis1 = new SelectVisitor(setSetWithVars("var4","var5","var7"));
		
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query3);
		TestData.qCon2 = SPARQLPatternParser.parsePattern(query3);
		TestData.qCon1 = (ASTQueryContainer) TestData.qCon1.jjtAccept(selectVis1, null);
		
		assertTrue(TestData.qCon1.jjtCompareTo(TestData.qCon2));
		
	}
	
	@Test
	public void testVisitQCon5() throws TokenMgrError, ParseException, VisitorException {
		String varQuery1 =
			TestData.buildStatement(TestData.testcontent2, TestData.PRED1, TestData.URI1);
		
		SelectVisitor selectVis1 = new SelectVisitor(setSetWithVars("testcontent2"));
		
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.varQuery1a);
		TestData.qCon2 = SPARQLPatternParser.parsePattern(varQuery1);
		TestData.qCon1 = (ASTQueryContainer) TestData.qCon1.jjtAccept(selectVis1, null);
		
		assertTrue(TestData.qCon1.jjtCompareTo(TestData.qCon2));
		
	}
	
	public Set<String> setSetWithVars(String... vars){
		Set<String> setWithVars = new HashSet<String>();
		for(String var : vars){
			setWithVars.add(var);
		}
		return setWithVars;
	}

}
