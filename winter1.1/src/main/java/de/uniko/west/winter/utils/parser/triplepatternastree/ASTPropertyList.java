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

/* Generated By:JJTree: Do not edit this line. ASTPropertyList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniko.west.winter.utils.parser.triplepatternastree;

import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;


public
class ASTPropertyList extends SimpleNode {
	public ASTPropertyList(int id) {
		super(id);
	}

	public ASTPropertyList(SPARQLPatternParser p, int id) {
		super(p, id);
	}
	
	/** Accept the visitor. **/
	public Object jjtAccept(SPARQLPatternParserVisitor visitor, Object data) throws VisitorException {
		return visitor.visit(this, data);
	}
	
	public String dumpToString(boolean samePredicateFlag, boolean sameSubjectFlag) {
		if(children.length > 2) sameSubjectFlag = true;
		else sameSubjectFlag = false;
		String dump = "";
		if(children != null){
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				if(n != null) {
					dump += n.dumpToString(samePredicateFlag, sameSubjectFlag);
				}
			}
		}
		//	  logger.debug("Dumping {} ", dump);
		return dump;
	}

	public ASTPropertyList jjtClone(){
		logger.debug("Cloning {}", this);
		ASTPropertyList clone =  new ASTPropertyList(this.id);
		logger.debug("Setting Parent {}", this.parent);
		clone.jjtSetParent(this.parent);
		if (children != null){
			logger.debug("{} children found for {} cloning them", children.length, this);
			for (int i = 0; i < children.length; i++){
				logger.debug("Cloning {} child {}", i, children[i]);
				Node childClone = children[i].jjtClone();
				clone.jjtAddChild(childClone, i);
			}
		}else logger.debug("No children found for {}", this);
		return clone;
	}

	@Override
	public ASTPropertyList jjtFlatClone(){
		logger.debug("Cloning {}...", this);
		ASTPropertyList clone = new ASTPropertyList(this.id);
		return clone;
	}

	public void jjtAddPropertyList(ASTPropertyList propertyList){
		ASTPropertyList tempPropertyList = this;
		for(int i = 1;tempPropertyList.jjtGetNumChildren()==3;i++){
			tempPropertyList = (ASTPropertyList)tempPropertyList.jjtGetChild(2);
		}
		propertyList.jjtSetParent(tempPropertyList);
		tempPropertyList.jjtAddChild(propertyList, 2);
	}

	@Override
	public boolean jjtCompareTo(Node node) {
		boolean result;
		ASTPropertyList propListNode = null;
		if(this.getClass()==node.getClass()){
			propListNode = (ASTPropertyList)node;
			if(this.jjtGetNumChildren()==propListNode.jjtGetNumChildren()){
				if(this.jjtGetNumChildren()!=0){
					for(int i = 0;i<this.jjtGetNumChildren();i++){
						result = this.jjtGetChild(i).jjtCompareTo(propListNode.jjtGetChild(i));
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
/* JavaCC - OriginalChecksum=d76537ca7d71d72f04495205aee80048 (do not edit this line) */
