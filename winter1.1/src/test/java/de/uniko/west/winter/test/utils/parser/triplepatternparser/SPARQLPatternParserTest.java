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

package de.uniko.west.winter.test.utils.parser.triplepatternparser;


import org.junit.Test;

import de.uniko.west.winter.test.data.TestData;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;

public class SPARQLPatternParserTest {
	
	@Test(expected=ParseException.class)
	public void testParsePattern1() throws ParseException {
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.failQuery1);
	}
	
	@Test(expected=ParseException.class)
	public void testParsePattern2() throws ParseException {
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.failQuery2);
	}
	@Test(expected=TokenMgrError.class)
	public void testParsePattern3() throws ParseException {
		TestData.qCon1 = SPARQLPatternParser.parsePattern(TestData.failQuery3);
	}
	@Test
	public void testParsePattern4() throws ParseException {
		ASTQueryContainer qContainer = SPARQLPatternParser.parsePattern(TestData.query6);
		qContainer.dump(">>>");
		ASTQueryContainer qContainer2 = SPARQLPatternParser.parsePattern(TestData.query7);
		qContainer2.dump(">>>");
	}
	
}
