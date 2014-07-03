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

package de.uniko.west.winter.utils.parser.triplepatternparser;

import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;

public class Parsertest {

	/**
	 * @param <T>
	 * @param args
	 * @throws ParseException 
	 * @throws TokenMgrError 
	 */
	public static void main(String[] args) {
		ASTQueryContainer qContainer = null;
		
		StringBuilder patternString = new StringBuilder();
		patternString.append("?book1 dc:title  ?title1. ");
		patternString.append("?book2 dc:title  ?title2; ");
		patternString.append("dc:date  ?date2. ");
		
		patternString.append("?book3 dc:title  ?title3, ?subtitle1, ?subtitle2; ");
		patternString.append("dc:date  ?date3. ");
		
		patternString.append("<http://example/egbook4> dc:title  ?title4. ");
		patternString.append("<http://example/egbook4> dc:date ?date4. ");
		patternString.append("?book5 dc:title  \"Title51\". ");
		patternString.append("?book5 dc:title  \"Title62\"@en. ");
		patternString.append("?book6 dc:age  \"6\"^^xsd:integer. ");
		patternString.append("?book6 dc:age  \"6.6\"^^xsd:decimal. ");
		patternString.append("?book7 dc:age  \"6\"^^<http://www.w3.org/2001/XMLSchema#unsignedInt>. ");
		
		System.out.println(patternString.toString());
		
		try {
			qContainer = SPARQLPatternParser.parsePattern(patternString.toString());
		} catch (TokenMgrError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		qContainer.dump(">>>");
		System.out.println(qContainer.dumpToString(true, true));
		
//		HashMap<String, Set<Object>> bindingMap = new HashMap<String, Set<Object>>();
//		Set<Object> book1 = new HashSet<Object>();
//		Set<Object> title1 = new HashSet<Object>();
//		
//		Set<Object> book2 = new HashSet<Object>();
//		Set<Object> title2 = new HashSet<Object>();
//		Set<Object> date2 = new HashSet<Object>();		
//		
//		Set<Object> book3 = new HashSet<Object>();
//		Set<Object> title3 = new HashSet<Object>();
//		Set<Object> subtitle1 = new HashSet<Object>();
//		Set<Object> subtitle2 = new HashSet<Object>();
//		Set<Object> date3 = new HashSet<Object>();
//		
//		Set<Object> title4 = new HashSet<Object>();
//		Set<Object> date4 = new HashSet<Object>();
//		
//		Set<Object> book5 = new HashSet<Object>();
//		Set<Object> title51 = new HashSet<Object>();
//		Set<Object> title52 = new HashSet<Object>();
//		
//		Set<Object> book6 = new HashSet<Object>();
//
//		Set<Object> book7 = new HashSet<Object>();
//		
//		try {
//			book1.add(new URI("http://example/egbook11"));
//			book1.add(new URI("http://example/egbook12"));
//			title1.add("Added Title 1");
//			
//			book2.add(new URI("http://example/egbook21"));
//			book2.add(new URI("http://example/egbook22"));
//			title2.add("Added Title 2");
//			date2.add("1.1.2010");
//			
//			
//			book3.add(new URI("http://example/egbook3"));
//			title3.add("Added Title 3");
//			subtitle1.add("Added SubTitle 1");
//			subtitle2.add("Added SubTitle 2");
//			date3.add("2.2.2010");
//			
//			title4.add("Added Title4");
//			date4.add("3.3.2010");
//			date4.add("4.4.2010");
//			
//			book5.add(new URI("http://example/egbook5"));
//			title51.add("Title 51");
//			title52.add("Title 52");
//			
//			book6.add(new URI("http://example/egbook6"));
//			book7.add(new URI("http://example/egbook7"));
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		bindingMap.put("book1",book1);
//		bindingMap.put("title1", title1);
//		bindingMap.put("title2", title2);
//		bindingMap.put("title3", title3);
//		bindingMap.put("title51", title51);
//		bindingMap.put("title52", title52);
//		bindingMap.put("title4", title4);
//		
//		bindingMap.put("subtitle1", subtitle1);
//		bindingMap.put("subtitle2", subtitle2);
//		
//		bindingMap.put("date2", date2);
//		bindingMap.put("date3", date3);
//		bindingMap.put("date4", date4);
//		
//		bindingMap.put("book2",book2);
//		bindingMap.put("book3",book3);
//		bindingMap.put("book5",book5);
//		bindingMap.put("book6",book6);
//		bindingMap.put("book7",book7);
		
		
//		VarSubstitutionVisitor vsVisitor = new VarSubstitutionVisitor(bindingMap);
//		
//		try {
//			qContainer.jjtAccept(vsVisitor, null);
//		} catch (VisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		BuildTreeViewPatternVisitor visitor =  new BuildTreeViewPatternVisitor();
//		
////		for (Node node : qContainer.children) {
//		for(int k = 0;k<qContainer.jjtGetNumChildren();k++){
//			Node node = qContainer.jjtGetChild(k);
//			try {
//				System.out.println(node.jjtAccept(visitor, null));
//			} catch (VisitorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("|-" + node.toString());
//			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
//				Node subnode = node.jjtGetChild(i);
//				System.out.println(" |-" + subnode.toString());
//				for (int j = 0; j < subnode.jjtGetNumChildren(); j++) {
//					Node subsubnode = subnode.jjtGetChild(j);
//					System.out.println(" |-" + subsubnode.toString());
//				}
//			}
//		}
//		qContainer.dump("");
//		String test = qContainer.dumpToString(false, false);
//		System.out.println(test);
//		try {
//			System.out.println(qContainer.jjtAccept(visitor, ""));
//		} catch (VisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//// 		TYPECHECK TESTS
//		
//		testcls obj = new testcls(1, 1.1, "hallo", new Object[]{title1, title2, title3});
//		for (Field fld : obj.getClass().getFields()){
//			System.out.println("Field: "+fld.getName());
//			try {
//				System.out.println("Value: "+fld.get(obj).toString());
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Class<?> type = fld.getType();
//			System.out.println("Type: "+type.toString());
//			
//			if (type.equals(Integer.class) || type.equals(int.class)) System.out.println("Is Integer");
//			else if (AbstractCollection.class.isAssignableFrom(type)) System.out.println("Is Collection");
//		}
	}
//	
//	public static void test(Object obj){
//		if(Integer.class.isInstance(obj)) System.out.println("absolutely True");
//		System.out.println("Object "+obj.toString()+" with type: "+obj.getClass().getName());
//	}
//	
//	public static class testcls{
//		
//		public int ntgr;
//		public double dbl;
//		public String strng;
//		public HashSet<Object> objst = new HashSet<Object>();
//		
//		public testcls(int ntgr, double dbl, String strng, Object[] objcts){
//			this.ntgr = ntgr;
//			this.dbl = dbl;
//			this.strng = strng;
//			this.objst = new HashSet<Object>(objst);
//		}
//	};

}
