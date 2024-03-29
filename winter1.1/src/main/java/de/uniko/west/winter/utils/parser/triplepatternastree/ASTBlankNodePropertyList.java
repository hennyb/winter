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

/* Generated By:JJTree: Do not edit this line. ASTBlankNodePropertyList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniko.west.winter.utils.parser.triplepatternastree;

import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

public
class ASTBlankNodePropertyList extends SimpleNode {
	public ASTBlankNodePropertyList(int id) {
		super(id);
	}

	public ASTBlankNodePropertyList(SPARQLPatternParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(SPARQLPatternParserVisitor visitor, Object data) throws VisitorException {
		return visitor.visit(this, data);
	}
	@Override
	public boolean jjtCompareTo(Node node) {
		boolean result;
		ASTBlankNodePropertyList blankPropertyListNode = null;
		if(this.getClass()==node.getClass()){
			blankPropertyListNode = (ASTBlankNodePropertyList)node;
			if(this.jjtGetNumChildren()==blankPropertyListNode.jjtGetNumChildren()){
				if(this.jjtGetNumChildren()!=0){
					for(int i = 0;i<this.jjtGetNumChildren();i++){
						result = this.jjtGetChild(i).jjtCompareTo(blankPropertyListNode.jjtGetChild(i));
						if(result){
							continue;
						} else
							return false;
					}
					return true;
				} else
					return true;
			} else 
				return false;
		} else
			return false;
	}
}
/* JavaCC - OriginalChecksum=2106e0b958c1e656334b6b84982254ee (do not edit this line) */
