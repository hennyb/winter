package net.java.rdf.winter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javassist.compiler.ast.Declarator;

import org.openrdf.model.ValueFactory;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.rdf.annotations.complex;
import net.java.rdf.annotations.typeinfo;
import net.java.rdf.annotations.winter;
import net.java.rdf.util.ClassAnalysis;
import net.java.rdf.util.EntityIncompleteException;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.FieldNotSetException;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;
import net.java.rdf.util.MalformedAnnotationException;
import net.java.rdf.util.WinterLiteralSet;
import net.java.rdf.util.WinterMappingSet;
import net.java.rdf.util.WinterObjectSet;
import net.java.rdf.util.WinterSet;
import net.java.rdf.util.WinterURISet;
import net.java.rdf.util.WrapsURI;

public class SesameReadMapper implements ReadMapper {
	
	protected static transient Logger logger = LoggerFactory.getLogger(SesameReadMapper.class.getName());
	
	ValueFactory vf;
	RepositoryConnection con;
	URI graph;
	Mapper mapper;
	
	public SesameReadMapper(){}
	
	/**
	 * @param init
	 * @param graph
	 */
	public SesameReadMapper(Init init, URI graph, Mapper mapper){
		this.vf = init.getValueFactory();
		this.con = init.getConnection();
		this.graph = graph;
		this.mapper = mapper;
	}
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#isMapped(net.java.rdf.annotations.winter, java.lang.Object)
	 */
	public boolean isMapped(winter objann, Object object){
		URI subj = ((IdentifiedByURI)object).getURI().getURI();
		logger.debug("In SesameReadMapper:isMapped checking if Object {} is mapped in graph {}",new Object[]{objann.toString(), graph.toString()});
		BooleanQuery bq = null;
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildAskQuery(objann, graph);
			logger.debug("SesameReadMapper:isMapped Query : {}", query.toString());
			bq = con.prepareBooleanQuery(QueryLanguage.SPARQL, query.toString());
		} catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setObject MalformedAnnotationException : {}", e);
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		//TODO fix it doesnt work like this subj not a variable
		bq.setBinding("subj", vf.createURI(subj.toString()));
		try {
			bq = con.prepareBooleanQuery(QueryLanguage.SPARQL, query.toString());
		} catch (MalformedQueryException e) {
			logger.error("MalformedQueryException : {}", e);
		} catch (RepositoryException e) {
			logger.error("Repository Exception : {}", e);
		}
		try {
			if (bq.evaluate()) return true;
		} catch (QueryEvaluationException e) {
			
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateURISetField(java.lang.reflect.Field, java.lang.Object)
	 */
	public void updateURISetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateURISetField called for Field {} in Object {}", new Object[]{field.getName(), declaringObject.toString()});
		field.setAccessible(true);
		try {
			((WinterURISet)field.get(declaringObject)).update();
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		field.setAccessible(false);
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateURIField(java.lang.reflect.Field, java.lang.Object)
	 */
	public URI updateURIField(Field field, Object declaringObject) throws FieldMemberTypeException{
		logger.debug("SesameReadMapper:updateURIField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		winter fieldann = field.getAnnotation(winter.class);
		Object memberObject = null;
		field.setAccessible(true);
		try {
			memberObject = field.get(declaringObject);
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		if (!(memberObject instanceof WrapsURI)) throw new FieldMemberTypeException(
			"Wrong fieldMemberType, winter.Type.URI annotated fields should declare members of type URI");
		// URI field with extra query, represent additional information, e.g type declaration for other URIs
		URI uri = null;
		try {
			if(!fieldann.query().equals("")) {
				uri = getINTERNALOBJECT(fieldann, fieldann, ((RdfSerialisable)declaringObject).getBindingSet());
			// URI field without query this URI should be present in object annotation
			}else{
				uri = getINTERNALOBJECT(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), ((RdfSerialisable)declaringObject).getBindingSet());
				
			}
		} catch (FieldNotMappedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
		try {
			WrapsURI newMember = (WrapsURI)memberObject.getClass().newInstance();
			newMember.setURI(uri);
			field.set(declaringObject, newMember);
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		} catch (InstantiationException e) {
			logger.error("{}",e);
		}
		// TODO test it, bindingsets should always be uptodate. does that work or do i have to write the bindingset back to object.
		if(uri != null)((RdfSerialisable)declaringObject).getBindingSet().setBinding(fieldann.var(), vf.createURI(uri.toString()));
		else((RdfSerialisable)declaringObject).getBindingSet().removeBinding(fieldann.var());
		field.setAccessible(false);
		return uri;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateLITERALSetField(java.lang.reflect.Field, java.lang.Object)
	 */
	public void updateLITERALSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateLiteralSetField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		field.setAccessible(true);
		try {
			((WinterLiteralSet)field.get(declaringObject)).update();
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		field.setAccessible(false);
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateLITERALField(java.lang.reflect.Field, java.lang.Object)
	 */
	public Object updateLITERALField(Field field, Object declaringObject) throws FieldMemberTypeException{
		logger.debug("SesameReadMapper:updateLiteralField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		winter fieldann = field.getAnnotation(winter.class);
		// FIXIT Check for object with toString implemented 
//		if ((!field.getType().isPrimitive() && !field.getType().equals(String.class))) throw new FieldMemberTypeException(
//				"Wrong fieldMemberType, winter.Type.Literal annotated fields should declare members of primitive type or String");
		Object literal = null;
//		Object literal  = field.getClass().newInstance();
		literal = getLITERAL(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), ((RdfSerialisable)declaringObject).getBindingSet());
		field.setAccessible(true);
		try {
			field.set(declaringObject, literal);
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		field.setAccessible(false);
		if(literal != null)((RdfSerialisable)declaringObject).getBindingSet().setBinding(fieldann.var(), vf.createLiteral(literal.toString()));
		else((RdfSerialisable)declaringObject).getBindingSet().removeBinding(fieldann.var());
		return literal;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateOBJECTSetField(java.lang.reflect.Field, java.lang.Object)
	 */
	public void updateOBJECTSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateLiteralField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		field.setAccessible(true);
		try {
			((WinterObjectSet)field.get(declaringObject)).update();
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		field.setAccessible(false);
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateOBJECTField(java.lang.reflect.Field, java.lang.Object)
	 */
	public Object updateOBJECTField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateLiteralField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		winter fieldann = field.getAnnotation(winter.class);
		Object memberObject = null;
		Object newObj = null;
		field.setAccessible(true);
		try {
			memberObject = field.get(declaringObject);
		} catch (IllegalAccessException e) {
			logger.error("{}",e);
		}
		if ((((winter)ClassAnalysis.getAnnotation(memberObject.getClass(), winter.class)).type() != winter.Type.EXTERNALOBJECT)) 
			throw new FieldMemberTypeException("Wrong fieldMemberType, winter.Type.MAPPING annotated fields should hold members of Type winter.Type.OBJECT");
		if(!(memberObject instanceof IdentifiedByURI)){
			logger.error("Object {} does not implements Identified by URI", memberObject.toString());
		}else{
			URI uri = getEXTERNALOBJECT(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class),
					((RdfSerialisable)declaringObject).getBindingSet());
			try {
				newObj = memberObject.getClass().newInstance();
				((IdentifiedByURI)newObj).setURI(uri);
				// TODO check if two iterations are needed here
				updateObject(newObj);
				field.set(declaringObject, newObj);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(uri != null)((RdfSerialisable)declaringObject).getBindingSet().setBinding(fieldann.var(), vf.createURI(uri.toString()));
			else((RdfSerialisable)declaringObject).getBindingSet().removeBinding(fieldann.var());
		}				
		field.setAccessible(false);
		return newObj;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateMAPPINGSetField(java.lang.reflect.Field, java.lang.Object)
	 */
	public void updateMAPPINGSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateMAPPINGSetField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		winter fieldann = field.getAnnotation(winter.class);

		Set<Object> value = new HashSet<Object>();
			try {
				WinterMappingSet memberObject = (WinterMappingSet)field.get(declaringObject);
				if (!(((winter)ClassAnalysis.getAnnotation(memberObject.getClass().getComponentType(), winter.class)).type() == winter.Type.PATTERN))
					throw new FieldMemberTypeException("Wrong fieldMemberType, winter.Type.Mapping annotated Collections should have componentType winter.Type.Pattern");
				memberObject.update();
			} catch (IllegalArgumentException e) {
				logger.error("{}", e);
			} catch (IllegalAccessException e) {
				logger.error("{}", e);
			} catch (EntityIncompleteException e) {
				logger.error("{}", e);
			} catch (MalformedAnnotationException e) {
				logger.error("{}", e);
			}
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateMAPPINGField(java.lang.reflect.Field, java.lang.Object)
	 */
	public Object updateMAPPINGField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException{
		logger.debug("SesameReadMapper:updateMAPPINGField called for Field {} in Object {}",new Object[]{field.getName(), declaringObject.toString()});
		winter fieldann = field.getAnnotation(winter.class);
		Object value = null;
		try {
			if (!(((winter)ClassAnalysis.getAnnotation(field.get(declaringObject).getClass(), winter.class)).type() == winter.Type.PATTERN)) 
				throw new FieldMemberTypeException("Wrong fieldMemberType, winter.Type.MAPPING annotated fields should hold members of Type winter.Type.PATTERN");
			value = getPATTERN(fieldann, ((RdfSerialisable)declaringObject).getBindingSet(), field.get(declaringObject));
			// Check if two iterations are needed
			field.setAccessible(true);
				field.set(declaringObject, value);
			field.setAccessible(false);
		} catch (IllegalAccessException e) {
			// TODO: handle exception
		} catch (EntityIncompleteException e) {
			// TODO: handle exception
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#updateObject(java.lang.Object)
	 */
	public void updateObject(Object object){
		//TODO Maybe two iterations here, check it
		Field[] fields = object.getClass().getDeclaredFields();
		Object memberObject =null;
		for (Field field : fields){
			winter fieldann = field.getAnnotation(winter.class);
			try {
				memberObject = field.get(object);
				switch (fieldann.type()) {
				case MAPPING:
					if (memberObject instanceof Collection){
						if(!(memberObject instanceof WinterMappingSet)){
							field.set(object, new WinterMappingSet(fieldann, field.getAnnotation(typeinfo.class), object, vf, this, mapper.writemapper));
							updateMAPPINGSetField(field, object);
						}else{
							updateMAPPINGSetField(field, object);
						}
					}else{
						updateMAPPINGField(field, object);
					}
					break;
	
				case EXTERNALOBJECT:
					if (memberObject instanceof Collection){
						if(!(memberObject instanceof WinterObjectSet)){
							field.set(object, new WinterObjectSet(fieldann, field.getAnnotation(typeinfo.class), object, vf, this, mapper.writemapper));
							updateOBJECTSetField(field, object);
						}else{
							updateOBJECTSetField(field, object);
						}

					}else{
						updateOBJECTField(field, object);
					}
					break;
					
				case INTERNALOBJECT:
					if (memberObject instanceof Collection){
						if(!(memberObject instanceof WinterURISet)){
							field.set(object, new WinterURISet(fieldann, field.getAnnotation(typeinfo.class), object, vf, this, mapper.writemapper));
							updateURISetField(field, object);
						}else{
							updateURISetField(field, object);
						}

					}else{
						updateURIField(field, object);
					}
					break;
					
				case LITERAL:
					updateLITERALField(field, object);
					if (memberObject instanceof Collection){
						if (!(memberObject instanceof WinterLiteralSet)) {
							field.set(object, new WinterLiteralSet(fieldann, field.getAnnotation(typeinfo.class), object, vf, this, mapper.writemapper));
							updateLITERALSetField(field, object);
						}else{
							updateLITERALSetField(field, object);
						}
					}else{
						updateLITERALField(field, object);
					}
					break;
				default:
					break;
				}
			} catch (FieldMemberTypeException e) {
				logger.error("{}", e);
			} catch (FieldNotMappedException e) {
				logger.error("{}", e);
			} catch (IllegalArgumentException e) {
				logger.error("{}", e);
			} catch (IllegalAccessException e) {
				logger.error("{}", e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERNs(net.java.rdf.annotations.winter, org.openrdf.query.BindingSet, net.java.rdf.util.WinterSet)
	 */
//	public Set<Object> getPATTERNs(winter fieldann, BindingSet declaringObjectBindingSet, WinterSet memberObject) throws EntityIncompleteException, MalformedAnnotationException{
	public Set<Object> getPATTERNs(winter fieldann, BindingSet declaringObjectBindingSet, WinterMappingSet memberObject) throws EntityIncompleteException, MalformedAnnotationException{
		HashSet<Object> patternHashSet = new HashSet<Object>();
		if ((fieldann.dst().length == 0) || (fieldann.src().length == 0) || (fieldann.src().length != fieldann.dst().length)) 
			throw new MalformedAnnotationException("Malformed Annotation, winter.dst() and winter.src() must be set and equal in length");
		if(memberObject != null){
			StringBuilder query = null;
			try {
				query = ClassAnalysis.buildSelectAllQuery(memberObject.getClass().getComponentType().getAnnotation(winter.class), graph);
			} catch (MalformedAnnotationException e) {
				logger.error("{}", e);
			}
			TupleQuery tq = null;
			try {
				tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
			} catch (RepositoryException e) {
				logger.error("{}", e);
			} catch (MalformedQueryException e) {
				logger.error("{}", e);
			}
			for (int i = 0; i < fieldann.src().length; i++){
				Binding binding = declaringObjectBindingSet.getBinding(fieldann.src()[i]);
				if (binding == null) { 
					throw new EntityIncompleteException("Could not get Bindings for object because declaring object is incomplete");
				}else{
					tq.setBinding(binding.getName(), binding.getValue());
				}
			}
			TupleQueryResult result = null;
			try {
				result = tq.evaluate();
			} catch (QueryEvaluationException e) {
				logger.error("{}", e);
			}
			try {
				while (result.hasNext()) {
					QueryBindingSet bindingSet = (QueryBindingSet)result.next();
					Object pattern = memberObject.getClass().getComponentType().newInstance();
					((RdfSerialisable)pattern).setBindingSet(bindingSet);
					patternHashSet.add(pattern);
				}
			} catch (QueryEvaluationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
		}
		return patternHashSet;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERN(net.java.rdf.annotations.winter, org.openrdf.query.BindingSet, java.lang.Object)
	 */
	public Object getPATTERN(winter fieldann, BindingSet declaringObjectBindingSet, Object object) throws MalformedAnnotationException, EntityIncompleteException{
		//TODO where do i need this, only one pattern in return???
		if ((fieldann.dst().length == 0) || (fieldann.src().length == 0) || (fieldann.src().length != fieldann.dst().length)) 
			throw new MalformedAnnotationException("Malformed Annotation, winter.dst() and winter.src() must be set and equal in length");
		if(object != null){
			StringBuilder query = null;
			try {
				query = ClassAnalysis.buildSelectAllQuery(object.getClass().getAnnotation(winter.class), graph);
			} catch (MalformedAnnotationException e) {
				logger.error("{}", e);
			}
			TupleQuery tq = null;
			try {
				tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
			} catch (RepositoryException e) {
				logger.error("{}", e);
			} catch (MalformedQueryException e) {
				logger.error("{}", e);
			}
			for (int i = 0; i < fieldann.src().length; i++){
				Binding binding = declaringObjectBindingSet.getBinding(fieldann.src()[i]);
				if (binding == null) { 
					throw new EntityIncompleteException("Could not get Bindings for object because declaring object is incomplete");
				}else{
					((RdfSerialisable)object).getBindingSet().setBinding(binding);
					tq.setBinding(binding.getName(), binding.getValue());
				}
			}
			TupleQueryResult result = null;
			try {
				result = tq.evaluate();
				if (result.hasNext()){
					BindingSet bindingSet = result.next();
					((RdfSerialisable)object).setBindingSet((QueryBindingSet)bindingSet);
					updateObject(object);
				}
			} catch (QueryEvaluationException e) {
				logger.error("{}", e);
			}
		}else{
			
		}
		return object;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERN(java.lang.Class, java.lang.Object)
	 */
	public Set<Object> getPATTERN(Class pattern, Object object){
		//TODO Check if it works, add error handling.
		Set<winter> fieldanns = null;
		for (Field field : object.getClass().getFields()){
			winter fieldann = (winter)field.getAnnotation(winter.class);
			if (((winter) pattern.getAnnotation(winter.class)).query().contains(fieldann.var()) && field.getType().equals(URI.class)){
				try {
					return getPATTERN(pattern, new HashMap<String, URI>().put(fieldann.var(), (URI) field.get(object)));
				} catch (IllegalArgumentException e) {
					logger.error("{}", e);
				} catch (IllegalAccessException e) {
					logger.error("{}", e);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERN(java.lang.Class, java.util.Map)
	 */
	public Set<Object> getPATTERN(Class patternType, Map<String, URI> bindingMap){
		//TODO implement this
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERN(java.lang.Class, org.openrdf.model.URI, java.lang.String)
	 */
	public Set<Object> getPATTERN(Class pattern, URI individualURI, String var){
		return getPATTERN(pattern, new HashMap<String, URI>().put(var, individualURI));
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getPATTERN(java.lang.Class)
	 */
	public Set<Object> getPATTERN(Class pattern) throws MalformedAnnotationException{
		//TODO Check this method
		Set<Object> patterns =  new HashSet<Object>();
		winter annotation = (winter)pattern.getAnnotation(winter.class);
		if (annotation == null || !annotation.type().equals(winter.Type.PATTERN)){
			logger.error("Could not read PATTERN, class {} annotation is empty or is not of type winter.Type.PATTERN", pattern.getName());
			return null;
		}
		StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();				
			while (result.hasNext()){
				Object object = pattern.newInstance();
				BindingSet bindingSet = result.next();
				((RdfSerialisable)object).setBindingSet((QueryBindingSet)bindingSet);
				((RdfSerialisable)object).setSesameReadMapper(this);
				((RdfSerialisable)object).setSesameWriteMapper(mapper.writemapper);
				updateObject(object);
				patterns.add(object);
			}
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (InstantiationException e) {
			logger.error("{}", e);
		} catch (IllegalAccessException e) {
			logger.error("{}", e);
		}
		return patterns;
	}
		
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getINTERNALOBJECT(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public URI getINTERNALOBJECT(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException{
		if (declaringObjectBindingSet == null); //TODO what if object has no binding set --> not yet mapped !!!! 
		URI uri = null;
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildSelectVarQuery(declaringObjectann, graph, new String[]{fieldann.var()});
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		logger.debug("SesameReadMapper:getURI TupleQuery {}", tq.toString());
		for(Binding binding : bindingSet){
			// check is this work, if not if(tq.getBindings().hasBinding(binding.getName()))
			tq.setBinding(binding.getName(), binding.getValue());
		}
		logger.debug("SesameReadMapper:getURI Binded TupleQuery {}", tq.getBindings().toString());
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			if (result.hasNext()) uri = new URI(result.next().getBinding(fieldann.var()).getValue().toString());
			logger.info("SesameReadMapper:getURI new URI {}", uri.toString());
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}
		//TODO check if not calling isMapped in the beginning
		if (uri == null)throw new FieldNotMappedException("Field is not mapped");
		return uri;		
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getURIs(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public Set<URI> getURIs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException{
		if (declaringObjectBindingSet == null); //TODO what if object has no binding set --> not yet mapped !!!! 
		Set<URI> uris = new HashSet<URI>();
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildSelectVarQuery(declaringObjectann, graph, new String[]{fieldann.var()});
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		for(Binding binding : bindingSet){
			// check is this work, if not if(tq.getBindings().hasBinding(binding.getName()))
			tq.setBinding(binding.getName(), binding.getValue());
		}
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			while(result.hasNext()){
				uris.add(new URI(result.next().getBinding(fieldann.var()).toString()));
			}
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}
		//TODO check if not calling isMapped in the beginning
		if (uris.isEmpty())throw new FieldNotMappedException("Field is not mapped");
		return uris;		
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getLITERAL(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public String getLITERAL(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet){
		logger.info("SesameReadMApper:getLiteral called with {}\n{}\n{}", new Object[]{fieldann.toString(), declaringObjectann.toString(), declaringObjectBindingSet.toString()});
		if (declaringObjectBindingSet == null); //TODO what if ...
		String value = null;
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		logger.debug("SesameReadMApper:getLiteral Modified BindingSet {}", bindingSet.toString());
		StringBuilder query = null;
		try {
			if(fieldann.query().equals(""))
				query = ClassAnalysis.buildSelectVarQuery(declaringObjectann, graph, new String[]{fieldann.var()});
			else
				query = ClassAnalysis.buildSelectVarQuery(fieldann, graph, new String[]{fieldann.var()});
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		}catch (RepositoryException e) {
			logger.error("{}", e);
		}catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		for(Binding binding : bindingSet){
			tq.setBinding(binding.getName(), binding.getValue());
		} 
		logger.info("SesameReadMapper:getLiteral Binded Query {}", tq.getBindings().toString());
		TupleQueryResult result = null;
		try{
			result = tq.evaluate();
			if(result.hasNext()) value = result.next().getBinding(fieldann.var()).getValue().toString();
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		}
		logger.info("SesameReadMapper:getLiteral Literal {}", value);
		return value;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getLITERALs(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public Set<String> getLITERALs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException{
		if (declaringObjectBindingSet == null); //TODO what if ...
		Set<String> literals = new HashSet<String>();
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildSelectVarQuery(declaringObjectann, graph, new String[]{fieldann.var()});
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		}catch (RepositoryException e) {
			logger.error("{}", e);
		}catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		for(Binding binding : bindingSet){
			if(tq.getBindings().hasBinding(binding.getName())){
				tq.setBinding(binding.getName(), binding.getValue());
			}
		} 
		TupleQueryResult result = null;
		try{
			result = tq.evaluate();
			while(result.hasNext()){
				literals.add(result.next().getBinding(fieldann.var()).toString());
			}
		} catch (QueryEvaluationException e) {
			// TODO: handle exception
		}
		if (literals.isEmpty()) throw new FieldNotMappedException("Field is not mapped");
		return literals;
	}
		
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getOBJECT(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public URI getEXTERNALOBJECT(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException{
		if (declaringObjectBindingSet == null); //TODO what if object has no binding set --> not yet mapped !!!! 
		URI uri = null;
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildSelectAllQuery(declaringObjectann, graph);
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		for(Binding binding : bindingSet){
			// check is this work, if not if(tq.getBindings().hasBinding(binding.getName()))
			tq.setBinding(binding.getName(), binding.getValue());
		}
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			if (result.hasNext()) uri = new URI(result.next().getBinding(fieldann.var()).toString());
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}
		//TODO check if not calling isMapped in the beginning
		if (uri == null)throw new FieldNotMappedException("Field is not mapped");
		return uri;
	} 
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getOBJECTs(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, org.openrdf.query.BindingSet)
	 */
	public Set<URI> getOBJECTs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException{
		if (declaringObjectBindingSet == null); //TODO what if object has no binding set --> not yet mapped !!!! 
		Set<URI> uris = new HashSet<URI>();
		QueryBindingSet bindingSet = new QueryBindingSet(declaringObjectBindingSet);
		bindingSet.removeBinding(fieldann.var());
		
		StringBuilder query = null;
		try {
			query = ClassAnalysis.buildSelectAllQuery(declaringObjectann, graph);
		} catch (MalformedAnnotationException e) {
			logger.error("{}", e);
		}
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		for(Binding binding : bindingSet){
			// check is this work, if not if(tq.getBindings().hasBinding(binding.getName()))
			tq.setBinding(binding.getName(), binding.getValue());
		}
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			while(result.hasNext()){
				uris.add(new URI(result.next().getBinding(fieldann.var()).toString()));
			}
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}
		if (uris.isEmpty())throw new FieldNotMappedException("Field is not mapped");
		return uris;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getConceptURIForIndividualURI(java.net.URI)
	 */
	public URI getConceptURIForIndividualURI(URI individualURI) {
		java.net.URI concept = null;
		StringBuilder query = ClassAnalysis.buildSelectConceptQuery(graph);
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		tq.setBinding("individual", vf.createURI(individualURI.toString()));
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			while(result.hasNext()){
				concept = new URI(result.next().getBinding("concept").toString());
			}
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}
		return concept;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.ReadMapper#getAllIndividualURIsForConcept(java.net.URI)
	 */
	public Set<URI> getAllIndividualURIsForConcept(URI conceptURI) {
		HashSet<java.net.URI> individualURIs = null;
		StringBuilder query = ClassAnalysis.buildSelectIndividualURIQuery(graph);
		TupleQuery tq = null;
		try {
			tq = con.prepareTupleQuery(QueryLanguage.SPARQL, query.toString());
		} catch (RepositoryException e) {
			logger.error("{}", e);
		} catch (MalformedQueryException e) {
			logger.error("{}", e);
		}
		tq.setBinding("concept", vf.createURI(conceptURI.toString()));
		TupleQueryResult result = null;
		try {
			result = tq.evaluate();
			while(result.hasNext()){
				individualURIs.add(new URI(result.next().getBinding("individual").toString()));
			}
		} catch (QueryEvaluationException e) {
			logger.error("{}", e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return individualURIs;
	}
}
