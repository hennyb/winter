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

public class Dump2QueryPatternVisitor implements SPARQLPatternParserVisitor{

	protected static transient Logger logger = LoggerFactory.getLogger(Dump2QueryPatternVisitor.class.getName());
	
	@Override
	public Object visit(SimpleNode node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting SimpleNode node: {}", data);
		
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			 data = node.jjtGetChild(i).jjtAccept(this, data);
		}
		return data;
	}

	@Override
	public Object visit(ASTQueryContainer node, Object data)
			throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTQueryContainer node {}", data);
		if(data ==  null) data = "";
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			 data = node.jjtGetChild(i).jjtAccept(this, data);
		}
		return data;
	}

	@Override
	public Object visit(ASTTriplesSameSubject node, Object data)
			throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visting ASTTriplesSameSubject: {} for Subject {}, {}",new Object[]{ node, node.jjtGetChild(0), data});
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			 data = node.jjtGetChild(i).jjtAccept(this, data);
		}
		return data;
	}

	@Override
	public Object visit(ASTIRI node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTIRI node: {}", data);
		String result = data + "<" + node.getValue() +"> ";
		return result;
	}

	@Override
	public Object visit(ASTPropertyList node, Object data)
			throws VisitorException {		
		logger.debug("Data {}",data);
		logger.debug("visiting ASTPropertyList node");
//		String res;
//		if (node.jjtGetNumChildren() > 2){
//			res = ";\n";
//		}else{
//			res = ".\n";
//		}
//		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
//			 data = node.jjtGetChild(i).jjtAccept(this, data).toString().trim() +
//			 		(i>0? res : " ");
//		}
		boolean hasSubPropertyList = node.jjtGetNumChildren() == 3; 
		
		data = node.jjtGetChild(0).jjtAccept(this, data) + " "; // pred
		data = node.jjtGetChild(1).jjtAccept(this, data).toString().trim() + (hasSubPropertyList?";\n":".\n"); // object or next list
		data = hasSubPropertyList ? node.jjtGetChild(2).jjtAccept(this, data) : data; //list!
		return data;
	}
	
	@Override
	public Object visit(ASTObjectList node, Object data)
			throws VisitorException {
		logger.info("Data {}",data);
		logger.info("visiting ASTObjectList node");
		logger.info("Num of Children{}", node.jjtGetNumChildren());
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			 data = node.jjtGetChild(i).jjtAccept(this, data).toString().trim()+
			 	(i < node.jjtGetNumChildren()-1 ? ", " : " ");
		}
		return data;
	}

	@Override
	public Object visit(ASTQName node, Object data) throws VisitorException {
		if (node.getValue() == null) return data;
		String res = "<" + (String)node.getValue() + ">";
//		String res = (String)node.getValue();
		
		data = data.toString() + res;
		logger.info("Data {}",data);
		logger.info("visiting ASTQName node: {}", node.getValue());
		return data;
	}
	
	@Override
	public Object visit(ASTRDFLiteral node, Object data)
			throws VisitorException {
		logger.info("Data {}",data);
		logger.info("visiting ASTRDFLiteral node: " +
				"\n								Datatype {} " +
				"\n								Label {} " +
				"\n								Lang {}"
				, new Object[]{ node.getDatatype(), node.getLabel().toString(), node.getLang()});

		 data = node.jjtGetChild(0).jjtAccept(this, data);
		 data = node.jjtGetChild(1).jjtAccept(this, data);
		return data;
	}

	@Override
	public Object visit(ASTNumericLiteral node, Object data)
			throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTNumericLiteral node: " +
				"\n	{}^^{}"
				,node.getValue(), node.getDatatype().toString());
		String res = (String)node.getValue();
		data = data.toString() + res;
		if(node.getDatatype() != null){
			data = data.toString() + "^^" + node.getDatatype().toString();
		}
		return data;
	}

	@Override
	public Object visit(ASTString node, Object data) 
		throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTString node :{}", node.getValue());
		String res = (String)node.getValue() + " ";
		data = data.toString() + res;
		return data;
	}

	
	@Override
	public Object visit(ASTCollection node, Object data)
			throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTCollection node");
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data)
			throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTBlankNodePropertyList node");
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public Object visit(ASTBlankNode node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTBlankNode node: {}", node.getID());
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public Object visit(ASTTrue node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTTrue node");
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public Object visit(ASTFalse node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTFalse node ");
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public Object visit(ASTVar node, Object data) throws VisitorException {
		logger.debug("Data {}",data);
		logger.debug("visiting ASTVar node: {}", node.getName());
		String res = "?" + (String)node.getName() + " ";
		data = data.toString() + res;
		// TODO Auto-generated method stub
		return data;
	}
}
