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
 * Edit Query.
 * New Query should only contain statements, which are important.
 * Important statements consist of at least variable, which stand in Set of Strings.
 * All other statements will be ignored.
 */
package de.uniko.west.winter.utils.visitors;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.utils.parser.triplepatternastree.ASTBlankNode;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTBlankNodePropertyList;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTCollection;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTFalse;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTIRI;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTNumericLiteral;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTObjectList;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTPropertyList;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQName;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTQueryContainer;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTRDFLiteral;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTString;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTTriplesSameSubject;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTTrue;
import de.uniko.west.winter.utils.parser.triplepatternastree.ASTVar;
import de.uniko.west.winter.utils.parser.triplepatternastree.Node;
import de.uniko.west.winter.utils.parser.triplepatternastree.SimpleNode;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * @date 25.10.2010
 *
 */
public class SelectVisitor implements SPARQLPatternParserVisitor {
	
	protected transient Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	// Set with varialbles, which are important
	Set<String> varSet;
	
	// Result of this visitor after use it on some query (ASTQueryContainer)
	private ASTQueryContainer editedQuery;
	// ASTQueryContainer editedQuery
	public SelectVisitor(Set<String> varSet){
		super();
		this.varSet = varSet;
	}
	
	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.SimpleNode, java.lang.Object)
	 */
	@Override
	public Object visit(SimpleNode node, Object data) throws VisitorException {
		logger.debug("Visiting SimpleNode: {}", node.toString());
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTQueryContainer, java.lang.Object)
	 */
	@Override
	public Object visit(ASTQueryContainer node, Object data) throws VisitorException {
		logger.debug("Visiting ASTQueryContainer: {}", node.toString());
		ASTQueryContainer newQContainer = (ASTQueryContainer)node.jjtFlatClone();
		logger.debug("Flat cloning of ASTQueryContainer...");
		editedQuery = newQContainer;
		logger.debug("Setting new qContainer to edited query...");
		node.childrenAccept(this, node);
//		node = this.editedQuery;
//		return null;
		return this.editedQuery;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTTriplesSameSubject, java.lang.Object)
	 */
	@Override
	public Object visit(ASTTriplesSameSubject node, Object data) throws VisitorException {
		ASTTriplesSameSubject newTriplesSameSubject = node.jjtFlatClone();
		logger.debug("Flat cloning of ASTTriplesSameSubject...");
		if(data.getClass()!=ASTQueryContainer.class){
			logger.warn("[WARN] Visit ASTTriplesSameSubject without visiting ASTQueryContainer before! Should not happen...");
		}
		else {
			if(node.jjtGetChild(0).getClass()==ASTVar.class){ //Subject is a variable
				ASTVar subjectNode = (ASTVar) node.jjtGetChild(0);
				if(varSet.contains(subjectNode.getName())){ // Subject is a important variable
					logger.debug("Subject {} is an important variable. Cloning all TripleSameSubject and add to editedQuery",subjectNode.getName());
					newTriplesSameSubject = node.jjtClone();
					newTriplesSameSubject.jjtSetParent(editedQuery);
					editedQuery.jjtAddChild(newTriplesSameSubject, editedQuery.jjtGetNumChildren());
				} else {
					logger.debug("Subject isn't important variable... Looking further the tree...");
					node.childrenAccept(this, node);
				}
			} else { // Subject isn't important variable... looking further the Tree
					logger.debug("Subject isn't a variable... Looking further the tree...");
					node.childrenAccept(this, node);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTPropertyList, java.lang.Object)
	 */
	@Override
	public Object visit(ASTPropertyList node, Object data) throws VisitorException {
		logger.debug("Visiting ASTPropertyList: {}", node);
		if (data != null && data.getClass() == ASTTriplesSameSubject.class){
			logger.debug("The class of data is ASTTriplesSameSubject. Looking for important variable...");
			ASTTriplesSameSubject workTSS = ((ASTTriplesSameSubject)data); // for not more casting
			// Clone TSS and Subject
			ASTTriplesSameSubject newTriplesSameSubject = workTSS.jjtFlatClone();
			Node subnode = workTSS.jjtGetChild(0).jjtClone();
			subnode.jjtSetParent(newTriplesSameSubject);
			newTriplesSameSubject.jjtAddChild(subnode, 0);
			// >>> new TSS with 1 Child as Subject
			// Detect, how many PropertyList are in TSS
			int count = 0;
			ASTPropertyList tempPropertyList = (ASTPropertyList)workTSS.jjtGetChild(1);
			for(int c = 1;tempPropertyList.jjtGetNumChildren()==3;c++){
				count = c;
				tempPropertyList = (ASTPropertyList)tempPropertyList.jjtGetChild(2);
			}
			// Look in all PropertyList after important variables
			tempPropertyList = (ASTPropertyList)workTSS.jjtGetChild(1);
			for(int c = 0;c<=count;c++){
				ASTObjectList newObjectList = (ASTObjectList)tempPropertyList.jjtGetChild(1).jjtFlatClone();
				for(int i = 0;i<tempPropertyList.jjtGetChild(1).jjtGetNumChildren();i++){
					if(tempPropertyList.jjtGetChild(1).jjtGetChild(i).getClass()== ASTVar.class){
						ASTVar tempVar = (ASTVar)tempPropertyList.jjtGetChild(1).jjtGetChild(i);
						if(varSet.contains(tempVar.getName())){ // Found important variable in ObjectList
							ASTVar newVar = (ASTVar)tempVar.jjtClone();
							newVar.jjtSetParent(newObjectList);
							newObjectList.jjtAddChild(newVar, newObjectList.jjtGetNumChildren());
							
						}
					}
				} // Gone over all objects in ObjectList
				if(newObjectList.jjtGetNumChildren()!=0){
					ASTPropertyList newPropertyList = (ASTPropertyList)tempPropertyList.jjtFlatClone();
					ASTQName newQName = (ASTQName)tempPropertyList.jjtGetChild(0);
					newQName.jjtSetParent(newPropertyList);
					newObjectList.jjtSetParent(newPropertyList);
					newPropertyList.jjtAddChild(newQName, 0);
					newPropertyList.jjtAddChild(newObjectList, 1);
					if(newTriplesSameSubject.jjtGetNumChildren()==2){
						((ASTPropertyList)newTriplesSameSubject.jjtGetChild(1)).jjtAddPropertyList(newPropertyList);
					} else {
						newPropertyList.jjtSetParent(newTriplesSameSubject);
						newTriplesSameSubject.jjtAddChild(newPropertyList, 1);
					}
				}
				if(count !=0){
					if(c != count){
						tempPropertyList = (ASTPropertyList)tempPropertyList.jjtGetChild(2);
					}
				}
			}
			if(newTriplesSameSubject.jjtGetNumChildren()==2){
				newTriplesSameSubject.jjtSetParent(editedQuery);
				editedQuery.jjtAddChild(newTriplesSameSubject, editedQuery.jjtGetNumChildren());
			}
		} else {
			logger.debug("This PropertyList have another PropertyList as a parent. Shall treated already.");
		}
		
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTVar, java.lang.Object)
	 */
	@Override
	public Object visit(ASTVar node, Object data) throws VisitorException {
		logger.debug("Visiting ASTVar: {}",node);
		if(varSet.contains(node.getName())){
			logger.debug("Variable {} in set found", node.getName());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTIRI, java.lang.Object)
	 */
	@Override
	public Object visit(ASTIRI node, Object data) throws VisitorException {
		logger.debug("Visiting ASTIRI: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTObjectList, java.lang.Object)
	 */
	@Override
	public Object visit(ASTObjectList node, Object data) throws VisitorException {
		logger.debug("Visiting ASTObjectList: {}", node);
		node.childrenAccept(this, node);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTCollection, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCollection node, Object data) throws VisitorException {
		logger.debug("Visiting ASTCollection: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTBlankNodePropertyList, java.lang.Object)
	 */
	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data)
			throws VisitorException {
		logger.debug("Visiting ASTBlankNodePropertyList: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTQName, java.lang.Object)
	 */
	@Override
	public Object visit(ASTQName node, Object data) throws VisitorException {
		logger.debug("Visiting ASTQName: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTBlankNode, java.lang.Object)
	 */
	@Override
	public Object visit(ASTBlankNode node, Object data) throws VisitorException {
		logger.debug("Visiting ASTBlankNode: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTRDFLiteral, java.lang.Object)
	 */
	@Override
	public Object visit(ASTRDFLiteral node, Object data) throws VisitorException {
		logger.debug("Visiting ASTRDFLiteral: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTNumericLiteral, java.lang.Object)
	 */
	@Override
	public Object visit(ASTNumericLiteral node, Object data) throws VisitorException {
		logger.debug("Visiting ASTNumericLiteral: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTTrue, java.lang.Object)
	 */
	@Override
	public Object visit(ASTTrue node, Object data) throws VisitorException {
		logger.debug("Visiting ASTTrue: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTFalse, java.lang.Object)
	 */
	@Override
	public Object visit(ASTFalse node, Object data) throws VisitorException {
		logger.debug("Visiting ASTFalse: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.parser.triplepatternparser.SPARQLPatternParserVisitor#visit(de.west.winter.parser.ASTree.ASTString, java.lang.Object)
	 */
	@Override
	public Object visit(ASTString node, Object data) throws VisitorException {
		logger.debug("Visiting ASTString: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

}
