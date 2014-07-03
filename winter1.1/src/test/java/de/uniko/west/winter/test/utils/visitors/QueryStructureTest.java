package de.uniko.west.winter.test.utils.visitors;

import de.uniko.west.winter.test.data.TestData;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;
import de.uniko.west.winter.utils.visitors.BuildTreeViewPatternVisitor;
import de.uniko.west.winter.utils.visitors.Dump2QueryPatternVisitor;

public class QueryStructureTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ASTQueryContainer qContainer;
		try {
			qContainer = SPARQLPatternParser.parsePattern(TestData.query1);
//			BuildTreeViewPatternVisitor visitor = new BuildTreeViewPatternVisitor();
//			qContainer.jjtAccept((SPARQLPatternParserVisitor)visitor, null);
			Dump2QueryPatternVisitor dvisitor = new Dump2QueryPatternVisitor();
			Object data = "";
			data = qContainer.jjtAccept(dvisitor, data);
			System.out.println("=== VISITOR RESULT ===\n" + data);
			System.out.println("\n=== ORIGINAL ===\n" + TestData.query1);
			
		} catch (VisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TokenMgrError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
}
