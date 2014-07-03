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

/* Generated By:JJTree: Do not edit this line. ASTQName.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniko.west.winter.utils.parser.triplepatternastree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

public
class ASTQName extends SimpleNode {

	protected static transient Logger logger = LoggerFactory.getLogger(ASTQName.class.getName());
	private String value;


	public ASTQName(int id) {
		super(id);
	}

	public ASTQName(SPARQLPatternParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(SPARQLPatternParserVisitor visitor, Object data) throws VisitorException {
		return visitor.visit(this, data);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + value + ")";
	}

	public String dumpToString(boolean samePredicateFlag, boolean sameSubjectFlag) {
		logger.debug("Dumping {} with  SAMESUB {}, SAMEPRED {}",new Object[]{ this.toString(), sameSubjectFlag, samePredicateFlag});
		String dump = "";
		if (parent.getClass() == ASTRDFLiteral.class){
			if (value != null)dump = "^^"+value+" ";
		}else{
			if (value != null)dump = value+" ";
		}
		if(children != null){
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode)children[i];
				if(n != null) {
					dump += n.dumpToString(samePredicateFlag, sameSubjectFlag);
				}
			}
		}
		logger.debug("Dumping {}", dump);
		return dump;
	}

	public ASTQName jjtClone(){
		logger.debug("Cloning {}", this);
		ASTQName clone =  new ASTQName(this.id);
		logger.debug("Setting Parent {}", this.parent);
		clone.jjtSetParent(this.parent);
		clone.value = this.value;
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
	public boolean jjtCompareTo(Node node) {
		ASTQName qNameNode = null;
		if(this.getClass()==node.getClass()){
			qNameNode = (ASTQName)node;
			if(this.jjtGetNumChildren()==qNameNode.jjtGetNumChildren() &&
					this.getValue().equals(qNameNode.getValue())){
				return true;
			} else 
				return false;
		} else
			return false;
	}
}
/* JavaCC - OriginalChecksum=0b5e706b66c103f1c7444aba772321f4 (do not edit this line) */
