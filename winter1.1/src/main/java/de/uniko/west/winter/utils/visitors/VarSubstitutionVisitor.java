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

import java.net.URI;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.core.serializing.Binding;
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
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserTreeConstants;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

public class VarSubstitutionVisitor implements SPARQLPatternParserVisitor {

	protected static transient Logger logger = LoggerFactory.getLogger(VarSubstitutionVisitor.class.getName());
	
	private InfoNode infoNode;
	private String[] exceptVars;
	
	public VarSubstitutionVisitor(InfoNode infoNode){
		this(infoNode, null);
	}
	
	public VarSubstitutionVisitor(InfoNode infoNode, String[] exceptVars){
		super();
		this.infoNode = infoNode;
		this.exceptVars = exceptVars;
	}
	
	
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
		if (data.getClass() == ASTTriplesSameSubject.class){
			logger.info("Variable {} is Subject, substituting ...", node.getName());
			Binding bindings = infoNode.getBindingForVar(node.getName());
			substituteTripleSameSubject(node, data, bindings);
		}else if (data.getClass() == ASTObjectList.class){
			logger.info("Variable {} is Object, adding ...", node.getName());
			Binding bindings = infoNode.getBindingForVar(node.getName());
			substitueObject(node, data, bindings);
		}else {
			logger.warn("Parent of ASTVar is not ASTTriplesSameSubject or ASTObjectList this should not happen");
			return null;
		}
        node.childrenAccept(this, node);
		return null;
	}
		
    /**
	 * 
	 */
	private void substitueObject(ASTVar node, Object data, Binding bindings) {
		logger.info("Substituting {} with {} Bindings", node.getName(), (bindings != null)? bindings.size(): "null" );
		if (isInExceptList(node)){
			return;
		}
		for (Object binding : bindings){
			String stringBinding = binding.toString();
			// Is the first replacement
			// Is the object an URI
			logger.info("Binding \"{}\" is of type {}", binding);
			if (binding instanceof URI){
				logger.info("Binding {} is a URI", binding);
				ASTIRI uriBinding = new ASTIRI(SPARQLPatternParserTreeConstants.JJTIRI);
				uriBinding.setValue(stringBinding);
			//TODO validate once
				uriBinding.validateIRI();
			//TODO Test
//							uriBinding.jjtSetParent(((ASTObjectList)data));
				logger.info("Adding {} to children of {}", new Object[]{uriBinding, data});
//							((ASTObjectList)data).jjtAddChild(uriBinding, ((ASTObjectList)data).jjtGetNumChildren());
				((ASTObjectList)data).jjtReplaceAndAppendObject(node, uriBinding);
				logger.info("ObjectList {}: {}", data, (ASTObjectList)data);
			// Is the Object a Literal	
			}else if(binding instanceof String){
				logger.info("Binding \"{}\" is a Literal", binding);
				//TODO: fix this for right filling
				String lbl = stringBinding;
				String dtt = null;
				String lng = null;
				logger.info("New Lang {}", lng);
				// Build the ASTString label
				ASTString label = new ASTString(SPARQLPatternParserTreeConstants.JJTSTRING);
				if (lbl != null)label.setValue(lbl);
				logger.info("New Label {}", label);
				// Build the ASTQName datatype node
				ASTQName datatype = new ASTQName(SPARQLPatternParserTreeConstants.JJTQNAME);
				if (dtt != null) datatype.setValue(dtt);
				logger.info("New DataType", datatype);
				// Build and fill the ASTRDFLiteral parent node
				ASTRDFLiteral astRDFLiteral = new ASTRDFLiteral(SPARQLPatternParserTreeConstants.JJTRDFLITERAL);
				if(lng != null) astRDFLiteral.setLang(lng);
				label.jjtSetParent(astRDFLiteral);
				if(label != null) astRDFLiteral.setLabel(label);
				datatype.jjtSetParent(astRDFLiteral);
				if(datatype != null) astRDFLiteral.setDatatype(datatype);
				((ASTObjectList)data).jjtReplaceAndAppendObject(node, astRDFLiteral);
				logger.info("New Literal added : {}",stringBinding);
			}
		}
	}

	/**
	 * 
	 */
	private void substituteTripleSameSubject(ASTVar node, Object data, Binding bindings) {
		logger.info("Substitution for {} are: ", node.getName());
		if (isInExceptList(node)){
			return;
		}
		for (int i = 0; i < bindings.size(); i++){
			String stringBinding = bindings.get(i).toString();
			logger.info("		{} : {}",i , bindings.get(i).toString());
			if (i == 0){
				ASTIRI uri =  new ASTIRI(SPARQLPatternParserTreeConstants.JJTIRI);
				logger.debug("First binding {} will replace old variable", stringBinding);
				uri.setValue(stringBinding);
				//TODO validate once
				logger.debug("Setting up IRI value Subject {}",uri);
				((ASTTriplesSameSubject)data).jjtReplaceSubject(uri);
				logger.debug("Variable {} substituted with {}", node.getName(), uri.getValue() );
			}else{
				logger.debug("Following binding {} will add as new TripleSameSubject", stringBinding);
				// Cloning TripleSameSubject
				//TODO Check this why can i not cast the clone to ASTTripleSameSubject??
				logger.debug("Type check {}", ((SimpleNode)data).jjtClone());
				Node clone; 
				if(data.getClass() == ASTTriplesSameSubject.class) clone = ((ASTTriplesSameSubject)data).jjtClone();
				else clone = ((SimpleNode)data).jjtClone();
				// Adding Bindings
				ASTIRI uri = (ASTIRI)clone.jjtGetChild(0);
				uri.setValue(stringBinding);
				//TODO validate once
				logger.debug("Replacing cloned Subject {}",uri);
				((ASTTriplesSameSubject)clone).jjtReplaceSubject(uri);
				((ASTTriplesSameSubject)data).jjtGetParent().jjtAddChild(clone, ((ASTTriplesSameSubject)data).jjtGetParent().jjtGetNumChildren());
				logger.debug("Variable {} substituted with {}", node.getName(), uri.getValue() );
			}
		}		
	}

//	@Override
//	public Object visit(ASTVar node, Object data) throws VisitorException {
//		logger.debug("Visiting ASTVar: {}", node);
//		if (bindingMap.keySet().contains(node.getName()) && !isInExceptList(node) ){
//			logger.debug("Binding for variable {} found", node.getName());
//			Set<Object> bindings = bindingMap.get(node.getName());
//			if (data.getClass() == ASTTriplesSameSubject.class){
//				logger.debug("Variable is Subject, substituting ...", node.getName());			
//				if(bindings.isEmpty() || bindings.iterator().next().getClass() != URI.class){
//					logger.warn("Binding {} is not of type URI. Only URIs are allowed to be bound to Subject");
//					return null;
//				}
//				int i = 1;
//				for (Object binding : bindings){
//					if (i == 1){
//						ASTIRI uri =  new ASTIRI(SPARQLPatternParserTreeConstants.JJTIRI);
//						logger.debug("First binding {} will replace old variable", binding);
//						uri.setValue(binding.toString());
//						logger.debug("Setting up IRI value Subject {}",uri);
//						((ASTTriplesSameSubject)data).jjtReplaceSubject(uri);
//						logger.info("Variable {} substituted with {}", node.getName(), uri.getValue() );
//					}else{
//						logger.debug("Following binding {} will add as new TripleSameSubject", binding);
//						// Cloning TripleSameSubject
//						//TODO Check this why can i not cast the clone to ASTTripleSameSubject??
//						logger.debug("Type check {}", ((SimpleNode)data).jjtClone());
//						Node clone; 
//						if(data.getClass() == ASTTriplesSameSubject.class) clone = ((ASTTriplesSameSubject)data).jjtClone();
//						else clone = ((SimpleNode)data).jjtClone();
//						// Adding Bindings
//						ASTIRI uri = (ASTIRI)clone.jjtGetChild(0);
//						uri.setValue(binding.toString());
//						logger.debug("Replacing cloned Subject {}",uri);
//						((ASTTriplesSameSubject)clone).jjtReplaceSubject(uri);
//						((ASTTriplesSameSubject)data).jjtGetParent().jjtAddChild(clone, ((ASTTriplesSameSubject)data).jjtGetParent().jjtGetNumChildren());
//						logger.debug("Variable {} substituted with {}", node.getName(), uri.getValue() );
//					}
//					i++;
//				}
//				
////				String binding = bindingMap.get(node.getName()).iterator().next().toString();
////				uri.setValue(binding);
////				((ASTTriplesSameSubject)data).jjtReplaceSubject(uri);
////				logger.debug("Variable {} substituted with {}", node.getName(), uri.getValue() );
//			}else if (data.getClass() == ASTObjectList.class){
//				//XXX {} fehlt?
//				logger.debug("Variable is Object, adding ...", node.getName());
//				for (Object binding : bindingMap.get(node.getName())){
////					if (i == 1){
//						// Is the first replacement
//						// Is the object an URI
//						if (binding.getClass() == URI.class){
//							logger.debug("Binding {} is a URI", binding);
//							ASTIRI uriBinding = new ASTIRI(SPARQLPatternParserTreeConstants.JJTIRI);
//							uriBinding.setValue(binding.toString());
//						//TODO Test
////							uriBinding.jjtSetParent(((ASTObjectList)data));
//							logger.debug("Adding {} to children of {}", new Object[]{uriBinding, data});
////							((ASTObjectList)data).jjtAddChild(uriBinding, ((ASTObjectList)data).jjtGetNumChildren());
//							((ASTObjectList)data).jjtReplaceObject(node, uriBinding);
//						// Is the Object a Literal	
//						}else if(binding.getClass() == String.class){
//							logger.debug("Binding \"{}\" is a Literal", binding);
//							//TODO: fix this for right filling
//							String lbl = binding.toString();
//							String dtt = null;
//							String lng = null;
//							logger.debug("New Lang {}", lng);
//							// Build the ASTString label
//	 						ASTString label = new ASTString(SPARQLPatternParserTreeConstants.JJTSTRING);
//							if (lbl != null)label.setValue(lbl);
//							logger.debug("New Label {}", label);
//							// Build the ASTQName datatype node
//							ASTQName datatype = new ASTQName(SPARQLPatternParserTreeConstants.JJTQNAME);
//							if (dtt != null) datatype.setValue(dtt);
//							logger.debug("New DataType", datatype);
//							// Build and fill the ASTRDFLiteral parent node
//							ASTRDFLiteral stringBinding = new ASTRDFLiteral(SPARQLPatternParserTreeConstants.JJTRDFLITERAL);
//							if(lng != null) stringBinding.setLang(lng);
//							label.jjtSetParent(stringBinding);
//							if(label != null) stringBinding.setLabel(label);
//							datatype.jjtSetParent(stringBinding);
//							if(datatype != null) stringBinding.setDatatype(datatype);
//							((ASTObjectList)data).jjtReplaceObject(node, stringBinding);
//							logger.debug("New Literal added : {}",stringBinding);
//						}
////					}else{
////						//Is the second replacement
////					}
////					i++;
//				}
////				((ASTObjectList)data).jjtAddChild(n, i)
//			}else {
//				logger.warn("Parent of ASTVar is not ASTTriplesSameSubject or ASTObjectList this should not happen");
//				return null;
//			}
//		}
//		node.childrenAccept(this, node);
//		return null;
// 	}

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
