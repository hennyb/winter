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
package de.uniko.west.winter.test.utils.parser.triplepatternparserastree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uniko.west.winter.test.data.TestData;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTIRI;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTObjectList;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTPropertyList;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQName;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTTriplesSameSubject;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTVar;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;


/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * @date 17.11.2010
 *
 */
public class CompareToTest {
	
	static String query4a;
	static ASTQueryContainer selfBuildContainer;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.query1);

		TestData.qCon2 = SPARQLPatternParser.parsePattern(TestData.query2);

		TestData.qCon3 = SPARQLPatternParser.parsePattern(TestData.query3);
		
		TestData.qCon4 = SPARQLPatternParser.parsePattern(TestData.query4);
		// Query 4 with dot
		query4a = TestData.query4 + " .";
		
		TestData.qCon5 = SPARQLPatternParser.parsePattern(TestData.query5);
	}
	
	public void init1(){
		selfBuildContainer = new ASTQueryContainer(0);
		ASTTriplesSameSubject tss= new ASTTriplesSameSubject(2);
		ASTIRI iriNode = new ASTIRI(5);
		iriNode.setValue(TestData.URI1);
		ASTPropertyList propList = new ASTPropertyList(3);
		ASTObjectList objectList = new ASTObjectList(6);
		
		ASTQName qName = new ASTQName(9);
		qName.setValue(TestData.PRED1);
		
		ASTVar varNode = new ASTVar(4);
		varNode.setName(TestData.VAR1);
		
		objectList.jjtAddChild(varNode, 0);
		propList.jjtAddChild(qName, 0);
		propList.jjtAddChild(objectList, 1);
		tss.jjtAddChild(iriNode, 0);
		tss.jjtAddChild(propList, 1);
		selfBuildContainer.jjtAddChild(tss, 0);
	}
	
	public void init2(){
		selfBuildContainer = new ASTQueryContainer(0);
		ASTTriplesSameSubject tss= new ASTTriplesSameSubject(2);
		ASTIRI iriNode = new ASTIRI(5);
		iriNode.setValue(TestData.URI1.replace("<", "").replace(">", ""));
		ASTPropertyList propList = new ASTPropertyList(3);
		ASTObjectList objectList = new ASTObjectList(6);
		
		ASTQName qName = new ASTQName(9);
		qName.setValue(TestData.PRED1);
		
		ASTVar varNode = new ASTVar(4);
		varNode.setName(TestData.VAR1.replace("?", ""));
		
		objectList.jjtAddChild(varNode, 0);
		propList.jjtAddChild(qName, 0);
		propList.jjtAddChild(objectList, 1);
		tss.jjtAddChild(iriNode, 0);
		tss.jjtAddChild(propList, 1);
		selfBuildContainer.jjtAddChild(tss, 0);
	}

	@Test
	public void testCompareTo1() throws TokenMgrError, ParseException {
		assertTrue(TestData.qCon1.jjtCompareTo(TestData.qCon1));
		assertTrue(TestData.qCon2.jjtCompareTo(TestData.qCon2));
		assertTrue(TestData.qCon3.jjtCompareTo(TestData.qCon3));
		assertTrue(TestData.qCon4.jjtCompareTo(TestData.qCon4));
		assertTrue(TestData.qCon5.jjtCompareTo(TestData.qCon5));
		
		ASTQueryContainer qCon6 = SPARQLPatternParser.parsePattern(TestData.query1);
		assertTrue(qCon6.jjtCompareTo(TestData.qCon1));
		assertTrue(TestData.qCon1.jjtCompareTo(qCon6));
		
		ASTQueryContainer qCon7 = SPARQLPatternParser.parsePattern(TestData.query2);
		assertTrue(qCon7.jjtCompareTo(TestData.qCon2));
		assertTrue(TestData.qCon2.jjtCompareTo(qCon7));
		
		ASTQueryContainer qCon8 = SPARQLPatternParser.parsePattern(TestData.query3);
		assertTrue(qCon8.jjtCompareTo(TestData.qCon3));
		assertTrue(TestData.qCon3.jjtCompareTo(qCon8));
		
		ASTQueryContainer qCon9 = SPARQLPatternParser.parsePattern(TestData.query4);
		assertTrue(TestData.qCon4.jjtCompareTo(qCon9));
		assertTrue(qCon9.jjtCompareTo(TestData.qCon4));
		
		ASTQueryContainer qCon10 = SPARQLPatternParser.parsePattern(TestData.query5);
		assertTrue(TestData.qCon5.jjtCompareTo(qCon10));
		assertTrue(qCon10.jjtCompareTo(TestData.qCon5));
		
	}
	
	@Test
	public void testCompareTo2(){
		assertFalse(TestData.qCon1.jjtCompareTo(TestData.qCon2));
		assertFalse(TestData.qCon1.jjtCompareTo(TestData.qCon3));
		assertFalse(TestData.qCon1.jjtCompareTo(TestData.qCon4));
		assertFalse(TestData.qCon1.jjtCompareTo(TestData.qCon5));
		
		assertFalse(TestData.qCon2.jjtCompareTo(TestData.qCon1));
		assertFalse(TestData.qCon2.jjtCompareTo(TestData.qCon3));
		assertFalse(TestData.qCon2.jjtCompareTo(TestData.qCon4));
		assertFalse(TestData.qCon2.jjtCompareTo(TestData.qCon5));
		
		assertFalse(TestData.qCon3.jjtCompareTo(TestData.qCon1));
		assertFalse(TestData.qCon3.jjtCompareTo(TestData.qCon2));
		assertFalse(TestData.qCon3.jjtCompareTo(TestData.qCon4));
		assertFalse(TestData.qCon3.jjtCompareTo(TestData.qCon5));
		

		assertFalse(TestData.qCon4.jjtCompareTo(TestData.qCon1));
		assertFalse(TestData.qCon4.jjtCompareTo(TestData.qCon2));
		assertFalse(TestData.qCon4.jjtCompareTo(TestData.qCon3));
		assertFalse(TestData.qCon4.jjtCompareTo(TestData.qCon5));
		
		assertFalse(TestData.qCon5.jjtCompareTo(TestData.qCon1));
		assertFalse(TestData.qCon5.jjtCompareTo(TestData.qCon2));
		assertFalse(TestData.qCon5.jjtCompareTo(TestData.qCon3));
		assertFalse(TestData.qCon5.jjtCompareTo(TestData.qCon4));
	}
	
	@Test
	public void testCompareTo3() throws Exception {
		String tempQuery1 = TestData.query1.replace("\n", "");
		ASTQueryContainer tempQCon1 = SPARQLPatternParser.parsePattern(tempQuery1);
		String tempQuery2 = TestData.query2.replace("\n", "");
		ASTQueryContainer tempQCon2 = SPARQLPatternParser.parsePattern(tempQuery2);
		String tempQuery3 = TestData.query3.replace("\n", "");
		ASTQueryContainer tempQCon3 = SPARQLPatternParser.parsePattern(tempQuery3);
		
		assertTrue(TestData.qCon1.jjtCompareTo(tempQCon1));
		assertTrue(TestData.qCon2.jjtCompareTo(tempQCon2));
		assertTrue(TestData.qCon3.jjtCompareTo(tempQCon3));
	}
	
	@Test
	public void testCompareTo4() throws Exception {
		ASTQueryContainer tempQCon = SPARQLPatternParser.parsePattern(query4a);
		assertTrue(tempQCon.jjtCompareTo(TestData.qCon4));
		assertTrue(TestData.qCon4.jjtCompareTo(tempQCon));
	}
	
	@Test
	public void testCompareTo5() throws Exception {
		init1();
		ASTQueryContainer tempQCon = SPARQLPatternParser.parsePattern(TestData.query6);
		assertFalse(tempQCon.jjtCompareTo(selfBuildContainer));
		assertFalse(selfBuildContainer.jjtCompareTo(tempQCon));
	}
	
	@Test
	public void testCompareTo6() throws Exception {
		init2();
		ASTQueryContainer tempQCon = SPARQLPatternParser.parsePattern(TestData.query8);
		assertTrue(tempQCon.jjtCompareTo(selfBuildContainer));
		assertTrue(selfBuildContainer.jjtCompareTo(tempQCon));
	}
	
}
