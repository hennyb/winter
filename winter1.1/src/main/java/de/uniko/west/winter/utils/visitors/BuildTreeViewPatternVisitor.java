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

package de.uniko.west.winter.utils.visitors;

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
import de.uniko.west.winter.utils.parser.triplepatternastree.SimpleNode;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;



/**
 * Dummy test Visitor used to analyse the outcomming ASTTree structure
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class BuildTreeViewPatternVisitor implements SPARQLPatternParserVisitor {
	
	protected static transient Logger logger = LoggerFactory.getLogger(BuildTreeViewPatternVisitor.class.getName());

	@Override
	public Object visit(SimpleNode node, Object data) throws VisitorException {
		logger.info(data+"visiting SimpleNode node: ");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTQueryContainer node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTQueryContainer node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTTriplesSameSubject node, Object data) throws VisitorException {
		logger.info("Visting ASTTriplesSameSubject: {} for Subject {}", node, node.jjtGetChild(0));
		node.childrenAccept(this, data+" ");
		return "ASTTripleSameSubject "+node.toString()+" visited \n";
	}

	@Override
	public Object visit(ASTPropertyList node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTPropertyList node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTVar node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTVar node: {}", node.getName());
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTIRI node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTIRI node: {}", node.getValue());
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTObjectList node, Object data)
			throws VisitorException {
		logger.info(data+"visiting ASTObjectList node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTCollection node, Object data)
			throws VisitorException {
		logger.info(data+"visiting ASTCollection node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data)
			throws VisitorException {
		logger.info(data+"visiting ASTBlankNodePropertyList node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTQName node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTQName node: {}", node.getValue());
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTBlankNode node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTBlankNode node: {}", node.getID());
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTRDFLiteral node, Object data)
			throws VisitorException {
		logger.info(data+"visiting ASTRDFLiteral node: " +
				"\n								Datatype {} " +
				"\n								Label {} " +
				"\n								Lang {}"
				, new Object[]{ node.getDatatype(), node.getLabel().toString(), node.getLang()});
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTNumericLiteral node, Object data)
			throws VisitorException {
		logger.info(data+"visiting ASTNumericLiteral node: " +
				"\n								{}^^{}"
				,node.getValue(), node.getDatatype().toString());
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTTrue node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTTrue node");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTFalse node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTFalse node ");
		node.childrenAccept(this, data+" ");
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) throws VisitorException {
		logger.info(data+"visiting ASTString node :{}", node.getValue());
		node.childrenAccept(this, data+" ");
		return null;
	}

}
