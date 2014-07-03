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

/* Generated By:JJTree: Do not edit this line. ASTObjectList.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniko.west.winter.utils.parser.triplepatternastree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

public class ASTObjectList extends SimpleNode {

	protected static transient Logger logger = LoggerFactory
	.getLogger(ASTObjectList.class.getName());

	public ASTObjectList(int id) {
		super(id);
	}

	public ASTObjectList(SPARQLPatternParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(SPARQLPatternParserVisitor visitor, Object data)
	throws VisitorException {
		return visitor.visit(this, data);
	}

	public Node[] jjtRemoveObject(Node node) throws VisitorException{
		for (int i = 0; i < jjtGetNumChildren(); i++){
			if (node.equals(children[i])){
				return jjtRemoveChild(i);
			}
		}
		return children;
	}
	

	
	public boolean jjtAddObject(SimpleNode object, int i) {
		// TODO throw something or warn here
		logger.info("Adding object {} in pos {}", object, i);
		if (object.getClass() != ASTRDFLiteral.class && object.getClass() != ASTIRI.class){
			logger.warn("Object should be substituted by an object not of type IRI or RDFLIteral");
			return false;
		}
		object.jjtSetParent(this);
		jjtAddChild(object, i);
		return true;
	}

	//If first call replaces Var nodes with binding for recent calls it appends bindings
	public boolean jjtReplaceAndAppendObject(SimpleNode var, SimpleNode object) {
		logger.info("Replacing Var Node {} with object {}", var, object);
		int i = 0;
		//First case: Var node could be found and replaced
		for (int j = 0; j < jjtGetNumChildren(); j++) {
			if (jjtGetChild(j).equals(var)) {
				i = j;
				logger.info("Var node found at pos: {}", i);
				jjtAddObject(object, i);
				return true;
			}
		}
		//Following cases: append new binding node to the end.
		jjtAddObject(object, jjtGetNumChildren());
		return true;
	}

	public String dumpToString(boolean samePredicateFlag,
			boolean sameSubjectFlag) {
		logger.info("Dumping {} with {} Objects",
				new Object[] { this.toString(), children.length });
		String dump = "";
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode) children[i];
				if (i < children.length - 1)
					samePredicateFlag = true;
				else if (i == children.length - 1) {
					samePredicateFlag = false;
				}
				logger.info(
						"Dumping Object {}, SamePredicate {}, SameSubject {}",
						new Object[] { i, samePredicateFlag, sameSubjectFlag });
				if (n != null) {
					dump += n.dumpToString(samePredicateFlag, sameSubjectFlag);
				}
			}
		}
		return dump;
	}

	public ASTObjectList jjtClone() {
		logger.info("Cloning {}", this);
		ASTObjectList clone = new ASTObjectList(this.id);
		logger.info("Setting Parent {}", this.parent);
		clone.jjtSetParent(this.parent);
		if (children != null) {
			logger.debug("{} children found for {} cloning them",
					children.length, this);
			for (int i = 0; i < children.length; i++) {
				logger.debug("Cloning {} child {}", i, children[i]);
				Node childClone = children[i].jjtClone();
				clone.jjtAddChild(childClone, i);
			}
		} else
			logger.debug("No children found for {}", this);
		return clone;
	}

	@Override
	public ASTObjectList jjtFlatClone(){
		logger.debug("Cloning {}...", this);
		ASTObjectList clone = new ASTObjectList(this.id);
		return clone;
	}

	@Override
	public boolean jjtCompareTo(Node node) {
		boolean result;
		ASTObjectList simpleNode = null;
		if(this.getClass()==node.getClass()){
			simpleNode = (ASTObjectList)node;
			if(this.jjtGetNumChildren()==simpleNode.jjtGetNumChildren()){
				if(this.jjtGetNumChildren()!=0){
					for(int i = 0;i<this.jjtGetNumChildren();i++){
						result = this.jjtGetChild(i).jjtCompareTo(simpleNode.jjtGetChild(i));
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
/*
 * JavaCC - OriginalChecksum=f12dcffa084d9889ba36ed73f126bbd5 (do not edit this
 * line)
 */
