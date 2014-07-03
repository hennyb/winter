package de.uniko.west.winter.test.utils.visitors;

import de.uniko.west.winter.test.data.TestData;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternparser.ParseException;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.TokenMgrError;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;
import de.uniko.west.winter.utils.visitors.BuildTreeViewPatternVisitor;
import de.uniko.west.winter.utils.visitors.PatternReductionVisitor;


public class VarEliminationVisitorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ASTQueryContainer qContainer;
		try {
			qContainer = SPARQLPatternParser.parsePattern(TestData.query9);
			BuildTreeViewPatternVisitor visitor = new BuildTreeViewPatternVisitor();
			qContainer.jjtAccept((SPARQLPatternParserVisitor)visitor, null);
			PatternReductionVisitor visitor2 = new PatternReductionVisitor();
			qContainer.jjtAccept((SPARQLPatternParserVisitor)visitor2, null);
			qContainer.jjtAccept((SPARQLPatternParserVisitor)visitor, null);
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
