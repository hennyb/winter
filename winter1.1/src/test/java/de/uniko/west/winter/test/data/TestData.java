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

package de.uniko.west.winter.test.data;

import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;

public class TestData {
	// URI
	public final static String URI1 = "<http://uri1.com>";
	public final static String URI2 = "<http://uri2.com>";
	public final static String URI3 = "<http://uri3.com>";
	public final static String URI4 = "<http://uri4.com>";
	public final static String URI5 = "<http://uri5.com>";
	public final static String URI6 = "<http://uri6.com>";
	public final static String URI7 = "<http://uri7.com>";

	// Variables
	public final static String VAR1 = "?var1";
	public final static String VAR2 = "?var2";
	public final static String VAR3 = "?var3";
	public final static String VAR4 = "?var4";
	public final static String VAR5 = "?var5";
	public final static String VAR6 = "?var6";
	public final static String VAR7 = "?var7";
	
	// Variales
	public final static String testcontent1 = "?testcontent1";
	public final static String testcontent2 = "?testcontent2";
	
	public final static String teststructure1 = "?teststructure1";
	public final static String teststructure2 = "?teststructure2";
	
	public final static String testtype1 = "?testtype1";
	public final static String testtype2 = "?testtype2";

	// PREDICATEs
	public final static String PRED1 = "exp:pred1";
	public final static String PRED2 = "exp:pred2";
	public final static String PRED3 = "exp:pred3";
	public final static String PRED4 = "exp:pred4";
	public final static String PRED5 = "exp:pred5";
	public final static String PRED6 = "rdf:pred6";
	

	// Blank nodes
	public final static String BLANK1 = "_:a";
	public final static String BLANK2 = "_:b";
	public final static String BLANK3 = "_:x";
	public final static String BLANK4 = "_:y";
	public final static String BLANK5 = "_:z";


	// QUERIES
	public final static String query1 = 
		URI1 + " " + 	PRED1 + " " + URI2 + " .\n" +
		URI2 + " " + 	PRED4 + " " + URI4 + " .\n" +
		URI3 + " " + 	PRED2 + " " + URI2 + ", " + URI4 + " .\n" +
		URI5 + " " + 	PRED3 + " " + URI3 + ";\n"+
						PRED5 + " " + URI4 + ", " + URI1 + ".";
	
	public final static String query2 = 
		VAR2 + " " + 	PRED3 + " " + URI1 + " .\n" +
		URI1 + " " + 	PRED1 + " " + URI2 + " .\n" +
		URI2 + " " + 	PRED4 + " " + URI4 + " .\n" +
		URI3 + " " + 	PRED2 + " " + VAR2 + ", " + URI4 + " .\n" +
		URI5 + " " + 	PRED3 + " " + URI3 + ";\n" +
						PRED5 + " " + VAR5 + ", " + URI1 + ";\n" +
						PRED1 + " " + VAR3 + ", " + URI5 + ".";
	
	public final static String query3 =
		VAR1 + " " + PRED1 + " " + VAR2 + ", " + VAR3 + ";\n" + 
		PRED2 + " " + VAR3 + ", " + VAR4 + ".\n" + 
		VAR2 + " " + PRED3 + " " + VAR3 + ";\n" +
		PRED4 + " " + VAR5 + ";\n" +
		PRED5 + " " + VAR1 + ", " + VAR4 + ".\n" +
		VAR3 + " " + PRED3 + " " + VAR5 + ", " + VAR6 + ", " + VAR7 + ";\n" +
		PRED1 + " " + VAR2 + ", " + VAR4 + ", " + VAR7 + ".\n";
	
	public final static String query4 = BLANK1 + " " + PRED1 + " " + URI1;
	
	public final static String query5 = BLANK2 + " " + PRED1 + " " + URI1 + " .\n";
	
	public final static String query6 = 
		buildStatement(URI1, PRED1, VAR1) +
		buildStatement(BLANK1, PRED2, VAR2) +
		buildStatement(VAR1, VAR3, "\"test\"@en") + 
		buildStatement(VAR2, VAR3, "\"42\"^^xsd:integer") + 
		buildStatement(URI3, VAR4, "\"abc\"^^"+URI7) +
		buildStatement(URI4, PRED5, "\"abc\"^^xsd:string") +
		buildStatement(BLANK3, PRED3, VAR2) +
		buildStatement(VAR5, PRED4, "<mailto:test@mail.de>") +
		buildStatement(BLANK3, PRED3, "\"2010-11-17T00:00:00Z\"^^xsd:dateTime");
	
	public final static String query7 = 
		buildStatement(URI1, PRED1, VAR1) +
		buildStatement(BLANK1, PRED2, VAR2) +
		buildStatement(VAR1, VAR3, "\"test\"@en") + 
		buildStatement(VAR2, VAR3, "\"42\"^^xsd:integer") + 
		buildStatement(URI3, VAR4, "\"abc\"^^"+URI7) +
		buildStatement(URI4, PRED5, "\"abc\"^^xsd:string") +
		buildStatement(BLANK3, PRED3, VAR2) +
		buildStatement(VAR5, PRED4, "<mailto:test@mail.de>") +
		buildStatement(BLANK3, PRED3, "\"simpleliteral\"");
	
	public final static String query8 = buildStatement(URI1, PRED1, VAR1);

	public final static String query9 = 
		VAR2 + " " + 	PRED3 + " " + URI1 + " .\n" +
		URI1 + " " + 	PRED1 + " " + URI2 + " .\n" +
		URI1 + " " + 	PRED1 + " " + VAR7 + " .\n" +
		URI2 + " " + 	PRED4 + " " + URI4 + " .\n" +
		URI3 + " " + 	PRED2 + " " + VAR2 + ", " + URI4 + " .\n" +
		URI5 + " " + 	PRED3 + " " + URI3 + ";\n" +
						PRED5 + " " + VAR5 + ", " + URI1 + ";\n" +
						PRED1 + " " + VAR3 + ".";
	
	// Fail queries
	public final static String failQuery1 =
		VAR1 + " " + PRED1 + " " + VAR2 + ", " + VAR3 + ";\n" + 
		PRED2 + " " + VAR3 + ", " + VAR4 + ".\n" + 
		VAR2 + " " + PRED3 + " " + VAR3 + ";\n" +
		PRED4 + " " + VAR5 + ";\n" +
		PRED5 + " " + VAR1 + ", " + VAR4 + ".\n" +
		VAR3 + " " + PRED3 + " " + VAR5 + ", " + VAR6 + ", " + VAR7 + "\n" + // fehlt ;
		PRED1 + " " + VAR2 + ", " + VAR4 + ", " + VAR7 + ".\n";
	
	public final static String failQuery2 = PRED1 + " " + URI1 + " .\n";
	public final static String failQuery3 = buildStatement(URI3, PRED2, "simpleLiteral");
	
	// Querys for bindingmap
	// *********************
	public final static String varQuery1a =
		buildStatement(testcontent2, PRED1, URI1);
	public final static String varQuery1b =
		buildStatement(URI1, testcontent2, URI2);
	public final static String varQuery1c =
		buildStatement(URI1, PRED1, testcontent2);
	
	public final static String varQuery2a = 
		buildStatement(teststructure1, PRED1, testcontent1) +
		buildStatement(BLANK1, PRED2, testcontent1) +
		buildStatement(testcontent2, PRED3, testtype1);
	
	public final static String varQuery2 = 
		buildStatement(teststructure1, PRED1, testcontent1) +
		buildStatement(URI3, PRED2, testcontent1) +
		buildStatement(testcontent2, PRED3, testtype1) + 
		buildStatement(testcontent1, PRED1, testtype2) + 
		buildStatement(teststructure1, PRED4, testcontent1) +
		buildStatement(teststructure2, PRED5, testtype2) +
		buildStatement(URI6, PRED3, testtype1) +
		buildStatement(testtype1, PRED4, testcontent1);
	
	public final static String varQuery3 = 
		testcontent1 + " " + 	PRED3 + " " + URI1 + " .\n" +
		URI1 + " " + 			PRED1 + " " + teststructure1 + " .\n" +
		teststructure2 + " " + 	PRED4 + " " + testtype1 + " .\n" +
		URI3 + " " + 			PRED2 + " " + VAR2 + ", " + testcontent2 + " .\n" +
		teststructure1 + " " + 	PRED3 + " " + testtype2 + ";\n" +
								PRED5 + " " + teststructure2 + ", " + testtype2 + ";\n" +
								PRED1 + " " + VAR3 + ", " + testcontent2 + ".";
	
	public final static String varQuery4 =
		buildStatement(testcontent2, PRED1, testcontent2) +
		buildStatement(URI1, PRED3, testtype1) +
		buildStatement(testcontent2, PRED2, testcontent1);

	// QContainers
	public static ASTQueryContainer qCon1;
	public static ASTQueryContainer qCon2;
	public static ASTQueryContainer qCon3;
	public static ASTQueryContainer qCon4;
	public static ASTQueryContainer qCon5;
	
	public static String buildStatement(String subject, String pred, String object){
		return subject + " " + pred + " " + object + " .\n";
	}
}
