package de.uniko.west.winter.utils.visitors;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.infostructure.InfoNode;
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
 *Reduces binded queries. If this visitor finds a var in a specific statement, this statement will be removed from the queryPatternContainer
 */
public class PatternReductionVisitor  implements SPARQLPatternParserVisitor {
protected static transient Logger logger = LoggerFactory.getLogger(PatternReductionVisitor.class.getName());
	
	InfoNode infoNode;
	private Set<String> exceptVars;
	
//	public VarEliminationVisitor(InfoNode infoNode){
//		this(infoNode, null);
//	}
//	
//	public VarEliminationVisitor(InfoNode infoNode, Set<String> exceptVars){
//		super();
//		this.infoNode = infoNode;
//		this.exceptVars = exceptVars;
//	}
	
	@Override
	public Object visit(SimpleNode node, Object data) throws VisitorException {
		logger.debug("Visiting SimpleNode: {}", node.toString());
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTQueryContainer node, Object data) throws VisitorException {
		logger.debug("Visiting ASTQueryContainer: {}", node.toString());
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTTriplesSameSubject node, Object data) throws VisitorException {
		logger.debug("Visiting ASTTriplesSameSubject: {} for Subject {}", node, node.jjtGetChild(0));
		node.childrenAccept(this, node);
		return null;
	}

	@Override
	public Object visit(ASTPropertyList node, Object data) throws VisitorException {
		if (data.getClass() != ASTTriplesSameSubject.class && data.getClass() != ASTPropertyList.class){
			logger.warn("Parent of ASTPropertyList is not ASTTriplesSameSubject or ASTPropertyList this should not happen: {}", data.getClass());
			return null;
		}
		//TODO: check this could also be made with jjGetParent ?? 
		logger.debug("Visiting ASTPropertyList: {} for Subject {}", node, ((SimpleNode)data).jjtGetChild(0));
		node.childrenAccept(this, node);
		return null;
	}

	public Object visit(ASTVar node, Object data) throws VisitorException {
		logger.info("Visiting ASTVar: {}", node);
		// Var found in ObjectList
		if (node.jjtGetParent() instanceof ASTObjectList){
			logger.info("Is ASTObject List proceeding ... ");
			ASTObjectList objectList = (ASTObjectList)node.jjtGetParent();
			// More than one Object -- just remove
			if (objectList.jjtGetNumChildren() > 1){
				logger.info("Object List has more than one child just removing");
				objectList.jjtRemoveChild(node);
			// Single Object remove property also
			} else {
				logger.info("Object List has single child just, predicate should be removed");
				Node propList = objectList.jjtGetParent();
				// More than one property -- just remove
				if(propList.jjtGetNumChildren() > 2){
					logger.info("More than one property and property in between...");
					Node rest = propList.jjtGetChild(2);
					propList.jjtGetParent().jjtReplaceChild(propList, rest);
					propList.jjtGetParent().jjtRemoveChild(rest);
					rest.jjtSetParent(propList.jjtGetParent());
				// Single Property remove parent TripleSameSubject
				}else if (propList.jjtGetParent() instanceof ASTPropertyList){
					logger.info("More than one property and property in the end of Props...");
					propList.jjtGetParent().jjtRemoveChild(propList);
				}else if (propList.jjtGetParent() instanceof ASTTriplesSameSubject && propList.jjtGetNumChildren() == 2){
					logger.info("Single property ...");
					Node tss = propList.jjtGetParent();
					tss.jjtGetParent().jjtRemoveChild(tss);
				}
			}
		// Var found in TripleSameSubject
		}else if(node.jjtGetParent() instanceof ASTTriplesSameSubject){
			logger.info("Is TripleSameSubject proceeding ... ");
			Node tss = node.jjtGetParent();
			tss.jjtGetParent().jjtRemoveChild(tss);
		}
        node.childrenAccept(this, node);
		return null;
	}
		
	@Override
	public Object visit(ASTIRI node, Object data) throws VisitorException {
		logger.debug("Visiting ASTIRI: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTObjectList node, Object data) throws VisitorException {
		logger.debug("Visiting ASTObjectList: {}", node);
		node.childrenAccept(this, node);
		return null;
	}

	@Override
	public Object visit(ASTCollection node, Object data) throws VisitorException {
		logger.debug("Visiting ASTCollection: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data)
			throws VisitorException {
		logger.debug("Visiting ASTBlankNodePropertyList: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTQName node, Object data) throws VisitorException {
		logger.debug("Visiting ASTQName: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTBlankNode node, Object data) throws VisitorException {
		logger.debug("Visiting ASTBlankNode: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTRDFLiteral node, Object data) throws VisitorException {
		logger.debug("Visiting ASTRDFLiteral: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTNumericLiteral node, Object data) throws VisitorException {
		logger.debug("Visiting ASTNumericLiteral: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTTrue node, Object data) throws VisitorException {
		logger.debug("Visiting ASTTrue: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTFalse node, Object data) throws VisitorException {
		logger.debug("Visiting ASTFalse: {}", node);
		node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) throws VisitorException {
		logger.debug("Visiting ASTString: {}", node);
		node.childrenAccept(this, null);
		return null;
	}
	
	private boolean isInExceptList(ASTVar node){
		if (exceptVars == null){
			return false;
		}
		
		for (String var : exceptVars) {
			if (node.getName().equals(var)){
				return true;
			}
		}
		
		return false;
	}
}
