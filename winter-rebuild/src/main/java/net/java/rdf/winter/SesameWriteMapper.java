package net.java.rdf.winter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.net.URI;

 

import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.TupleExprToStatement;

import net.java.rdf.util.ClassAnalysis;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;
import net.java.rdf.util.WinterLiteralSet;
import net.java.rdf.util.WinterMappingSet;
import net.java.rdf.util.WinterObjectSet;
import net.java.rdf.util.WinterSet;
import net.java.rdf.util.FieldNotSetException;
import net.java.rdf.util.MalformedAnnotationException;
import net.java.rdf.util.TupleExprQueryExtractor;
import net.java.rdf.util.WinterURISet;
import net.java.rdf.util.WrapsURI;
import net.java.rdf.annotations.complex;
import net.java.rdf.annotations.typeinfo;
import net.java.rdf.annotations.winter;

import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.openrdf.query.algebra.evaluation.impl.BindingAssigner;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.QueryParser;
import org.openrdf.query.parser.sparql.SPARQLParser;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SesameWriteMapper implements WriteMapper {
	
	//TODO check if its right to use tuplequery-objects or better to use strings and queryparser
	
	protected static transient Logger logger = LoggerFactory.getLogger(SesameWriteMapper.class.getName());
	
	ValueFactory vf;
	RepositoryConnection con;
	URI graph;
	ReadMapper readmapper;
	
	public SesameWriteMapper(){}
	
	/**
	 * @param init
	 * @param graph
	 */
	public SesameWriteMapper(Init init, URI graph, ReadMapper readmapper) {
		this.vf = init.getValueFactory();
		this.con = init.getConnection();
		this.graph = graph;
		logger.debug("SesameWriteMapper:SesameWriteMapper New Writemapper with init {} created", init.toString());
	}

	// ALL FOR PATTERN -------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setPattern(net.java.rdf.annotations.winter, java.lang.Object)
	 */
	
	//TODO: if we have to maptheFields here??
	public boolean setPattern(winter objann, Object object) {
		logger.debug("SesameWriteMapper:setPattern with parameters : \n		{}  \n		{} ", new Object[]{objann.toString(), graph.toString()});
		
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();

			// Build the query and get parsedQueryObject
		try{
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(objann, graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:setPattern MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setPattern MalformedAnnotationException : {}", e);
		}
		// Get the tupleExpression from parsedQuery and initialize BindingAssigner
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		
		// Get the bindingSet from the object, if Binding is not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
		if(bindingSet == null){
			logger.debug("	 No BindingSet found try to build new one for PATTERN {}", object.toString());
			bindingSet = ClassAnalysis.buildBindingSet(object, vf);
			logger.debug("	 BindingSet {} created", bindingSet.toString());
			((RdfSerialisable)object).setBindingSet(bindingSet);
		}
		// Bind bindingSet to query
		logger.debug("	TupleExpr {}", te);
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	TupleExpr {}", te);
		logger.debug("	SesameWriteMapper:setPattern binded Query {}", te.toString());
		
		//create and add statements
		try{
			tets.create(te);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setPattern RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:setPattern Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setPattern finished\n");
		return true;
	}

	/**
	 * @param annotation
	 * @param vars
	 * @param object
	 * @return
	 */
	public boolean deletePatternInRepo(winter annotation, Set<String> vars, Object object){
		// TODO Auto-generated method stub
		return true;
	}

	// ALL FOR MAPPING -------------------------------------------	
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setMapping(net.java.rdf.annotations.winter, java.lang.Object, org.openrdf.query.algebra.evaluation.QueryBindingSet)
	 */
	public boolean setMapping(winter fieldann, Object object, QueryBindingSet declaringObjectBindingSet) {
		logger.debug("SesameWriteMapper:setMapping with parameters : \n		{}  \n		{} ", new Object[]{fieldann.toString(), graph.toString()});
		
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		// Build the query and get parsedQueryObject
		try{			
			logger.debug("	 For Object {}\n with annotation {}", object.toString(), ClassAnalysis.getAnnotation(object.getClass(), winter.class));
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(object.getClass().getAnnotation(winter.class), graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:setMapping MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setMapping MalformedAnnotationException : {}", e);
		}
		// Get the tupleExpression from parsedQuery and initialize BindingAssigner
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
		if(bindingSet == null){
			logger.debug("	 No BindingSet found try to build new one for PATTERN {}", object.toString());
			try{
			bindingSet = ClassAnalysis.buildBindingSetForMapping(declaringObjectBindingSet, fieldann.src(), fieldann.dst());
			}catch (MalformedAnnotationException e) {
				logger.error("SesameWriteMapper:setMapping calling ClassAnalysis.buildBindingSet with params \n{}, \n{}, \n{}, \n: throws {}", new Object[]{object, vf, fieldann, declaringObjectBindingSet, e});
			}
			logger.debug("	 BindingSet {} created", bindingSet.toString());
			((RdfSerialisable)object).setBindingSet(bindingSet);
		}
		// Bind bindingSet to query
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", te.toString());
		//create and add statements
		try{
			HashSet<String> vars = new HashSet<String>();
			for (String var : fieldann.dst()) vars.add(var);
			tets.create(te, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setMapping RepositoryException : {}", e);
		}catch(Exception e){
			logger.error(" SesameWriteMapper:setMapping Exception : {}", e);
		}
		
		logger.debug("SesameWriteMapper:setMapping finished!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setMappingSetMember(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object)
	 */
	public boolean setMappingSetMember(winter fieldann, Object memberObject, Object declaringObject) {
		logger.debug("SesameWriteMapper:setMappingSetMember with parameters : \n		{}  \n		{} \n		{}", new Object[]{fieldann.toString(), memberObject.toString(), declaringObject.toString()});
		
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		// Build the query and get parsedQueryObject
		try{			
			logger.debug("	 For Object {}\n with annotation {}", memberObject.toString(), 
					ClassAnalysis.getAnnotation(memberObject.getClass(), winter.class));
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(memberObject.getClass().getAnnotation(winter.class), graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:setMappingSetMember MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setMappingSetMember MalformedAnnotationException : {}", e);
		}
		// Get the tupleExpression from parsedQuery and initialize BindingAssigner
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)memberObject).getBindingSet();
		if(bindingSet == null){
			logger.debug("	 No BindingSet found try to build new one for PATTERN {}", memberObject.toString());
			try{
				bindingSet = ClassAnalysis.buildBindingSet(memberObject, vf, fieldann, ((RdfSerialisable)declaringObject).getBindingSet());
			}catch (MalformedAnnotationException e) {
				logger.error("SesameWriteMapper:setMappingSetMember calling ClassAnalysis.buildBindingSet with params \n{}, \n{}, \n{}, \n: throws {}", 
						new Object[]{memberObject, vf, fieldann, e});
			}catch (FieldNotSetException e) {
				logger.error("SesameWriteMapper:setMappingSetMember calling ClassAnalysis.buildBindingSet with params \n{}, \n{}, \n{}, \n: throws {}", 
						new Object[]{memberObject, vf, fieldann, e});
			}
			logger.debug("	 BindingSet {} created", bindingSet);
			((RdfSerialisable)memberObject).setBindingSet(bindingSet);
		}
		// Bind bindingSet to query
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", te.toString());
		
		//create and add statements
		try{
			tets.create(te);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setMappingSetMember RepositoryException : {}", e);
		}catch(Exception e){
			logger.error(" SesameWriteMapper:setMappingSetMember Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setMappingSetMember finished!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#replaceMapping(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object)
	 */
	public boolean replaceMapping(winter fieldann, Object declaringObject, Object object, Object newObject) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#deleteMappingInRepo(net.java.rdf.annotations.winter, java.util.Set, java.lang.Object)
	 */
	//TODO check functionality carefully what happens with these variables
	public boolean deleteMappingInRepo(winter fieldann ,Set<String> variables, Object object){
		logger.debug("SesameWriteMapper:deleteMappingInRepo with parameters : \n		{}  \n			{} ", new Object[]{fieldann.toString(), graph.toString()});
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		try{			
			logger.debug("	 For Object {}\n with annotation {}", object.toString(), ClassAnalysis.getAnnotation(object.getClass(), winter.class));
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(object.getClass().getAnnotation(winter.class), graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:deleteMappingInRepo MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:deleteMappingInRepo MalformedAnnotationException : {}", e);
		}
		
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
		if(bindingSet == null){
			logger.error("SesameWriteMapper:deleteMappingInRepo No BindingSet found this indicates that the object is not mapped yet", object.toString());
		}
		// Bind bindingSet to query
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", te.toString());
		//create and add statements
		try{
			HashSet<String> vars = new HashSet<String>();
			for (String var : fieldann.dst()) vars.add(var);
			tets.create(te, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.remove(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:deleteMappingInRepo RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:deleteMappingInRepo Exception : {}", e);
		}
		
		logger.debug("SesameWriteMapper:deleteMappingInRepo finished!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#deleteMapping(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object, boolean)
	 */
	//TODO Check if this works as expected
	public boolean deleteMapping(winter fieldann, Object object, Object declaringObject, boolean recursive) {
		logger.debug("SesameWriteMapper.deleteMapping with parameters : \n		{}  \n		{} ", new Object[]{fieldann.toString(), graph.toString()});
		winter annotation = fieldann;
		if (declaringObject != null && fieldann != null){
			if (recursive){
				logger.debug("	 Lower lvl optional Pattern, delete recursive");
				removeTheFields(object);
				logger.debug("	 Object {} delete", object);
				logger.debug("SesameWriteMapper.deleteMapping finished!!\n");
				return true;
			}else{
				logger.debug("	 Lower lvl optional only in object, Object is still present in Repo");
				QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
				annotation = (winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class);
				deleteObjectInRepo(annotation, null, bindingSet);
				// Build query for remove and add operation
				bindingSet.removeAll(bindingSet.getBindingNames());
				logger.debug("	 Object {} delete", object);
				logger.debug("SesameWriteMapper.deleteMapping finished!!\n");
				return true;
			}
		}else{
			logger.debug("	 First order Object");
			removeTheFields(object);
			logger.debug("	 Object {} delete", object);
			logger.debug("SesameWriteMapper.deleteMapping finished!!\n");
			return true;
		}
	}
	
	
	// ALL FOR THE LITERALS ---------------------------------------
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setLiteral(net.java.rdf.annotations.winter, java.lang.Object, java.lang.String)
	 */
	public boolean setLiteral(winter annotation, Object object, String var) {
		logger.debug("SesameWriteMapper:setLiteral with parameters : \n		{}  \n		{} ", new Object[]{annotation.toString(), graph.toString()});
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setLiteral MalformedAnnotationException : {}", e);
		} catch (MalformedQueryException e) {
			logger.error("SesameWriteMapper:setLiteral MalformedQueryException : {}", e);
		}
		TupleExpr te = pq.getTupleExpr();
		QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
		BindingAssigner ba = new BindingAssigner();
		if(bindingSet == null){
			//TODO maybe exception here
			logger.warn("	 Empty BindingSet from Parental Object, this could mean that object is not set couldnt set Literal");
			return false;
		}
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", te.toString());
		
		try {
			
			HashSet<String> vars = new HashSet<String>();
			vars.add(var);
			logger.debug("	 Var : {}", var);
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(te, vars);
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Statements : {}", statement.toString());
				con.add(statement, vf.createURI(graph.toString()));
			}
		} catch (RepositoryException e) {
			logger.error("SesameWriteMapper:setLiteral RepositoryException : {}", e);
		} catch (Exception e) {
			logger.error("SesameWriteMapper:setLiteral Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setLiteral finished!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setLiteralSetMember(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object, java.lang.String)
	 */
	public boolean setLiteralSetMember(winter annotation, Object memberObject, Object declaringObject, String var) {
		logger.debug("SesameWriteMapper:setLiteralSetMember with parameters :  \n		{} \n		{} \n		{} \n		{} \n		{}", new Object[]{annotation.toString(),
				memberObject.toString(), declaringObject.toString(), var,graph.toString()});
		
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		// Build the query and get parsedQueryObject
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedQueryException e) {
			logger.error("SesameWriteMapper:setLITERALSetMember MalformedQueryException : {}", e);
		} catch (Exception e) {
			logger.error("SesameWriteMapper:setLITERALSetMember Exception {}", e);
		}
		
		TupleExpr tupleExpr = pq.getTupleExpr();
		QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		logger.debug("		Variable {} : Literal {}", var, memberObject.toString());
		bindingSet.removeBinding(var);
		bindingSet.addBinding(var, vf.createLiteral(memberObject.toString()));
		BindingAssigner ba = new BindingAssigner();
		ba.optimize(tupleExpr, pq.getDataset(), bindingSet);
		logger.debug("		TempTupleExpr {}", tupleExpr.toString());
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		try{
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(tupleExpr, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setLITERALSetMember RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:setLITERALSetMember Exception : {}", e);
		}
		logger.debug("		SesameWriteMapper:setLiteralSetMember finished!!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setSetTypeInfoForLiteralInSet(net.java.rdf.annotations.typeinfo, org.openrdf.query.algebra.evaluation.QueryBindingSet, java.lang.String)
	 */
	public boolean setSetTypeInfoForLiteralInSet( typeinfo typeann, QueryBindingSet parentBindingSet, String var){
		logger.debug("SesameWriteMapper:setSetTypeInfoswith parameters :  \n		{} \n		{} \n		{} \n", new Object[]{typeann.toString(), parentBindingSet.toString(), var.toString()});
		QueryParser qparser = new SPARQLParser();
		ParsedQuery pq = null;
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(typeann, graph);
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		ba.optimize(te, pq.getDataset(), parentBindingSet);
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		try {
			tets.create(te);
			for (Statement statement : tets.getStatements()){
				logger.debug("SesameWriteMapper:setSetTypeInfo adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("TypeInfo mapped!!");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#replaceLiteral(net.java.rdf.annotations.winter, java.lang.Object, java.lang.String)
	 */
	//FIXME: Change literal to Object !!!!
	public boolean replaceLiteral(winter fieldann, Object object, String literal) {
		logger.debug("SesameWriteMapper:replaceLiteral with parameters : \n		{}  \n		{} \n		{}  \n", new Object[]{fieldann.toString(), object.toString(), literal});
		winter annotation = fieldann;
		String var = fieldann.var();
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		QueryBindingSet bindingSet = ((RdfSerialisable)object).getBindingSet();
		if(bindingSet.hasBinding(var)){
			if (fieldann.query().equals("")){
				annotation = (winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class);	
				logger.debug("	 Var : {}", fieldann.var());
			}
			deleteLiteralInRepo(annotation, vars, bindingSet);
			// Build query for remove and add operation
			bindingSet.removeBinding(var);
		}else{
			//TODO maybe exception here
			logger.warn("	 Var not in BindingSet, this could mean that Literal is not set yet");
		}
		bindingSet.addBinding(var, vf.createLiteral(literal));
		setLiteral(annotation, object, var);
		logger.debug("SesameWriteMapper:replaceLiteral finished!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#deleteLiteral(net.java.rdf.annotations.winter, java.lang.Object, java.lang.String, boolean, java.lang.Object)
	 */
	public boolean deleteLiteral(winter fieldann, Object declaringObject, String var, boolean recursive, Object litToDel) {
		winter annotation = fieldann;
		if (fieldann.query().equals("")){
			logger.warn("SesameWriteMapper.deleteLiteral LITERAL MUST BE SET !! Could not be removed");
			return false;
		}else if (fieldann.query().equals("") && recursive){
			annotation = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);
		}
		QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		if (litToDel != null) {
			logger.debug("SesameWriteMapper.deleteLiteral with parameters : \n{}  \n{} \n{}  \n{} \n{} \n ", 
					new Object[]{fieldann.toString(), declaringObject.toString(), var, recursive, litToDel.toString()});
			bindingSet.setBinding(var, vf.createLiteral(litToDel.toString()));
			}
		if(bindingSet.hasBinding(var)){
			logger.debug("SesameWriteMapper.deleteLiteral BindingSet {} has var {}", bindingSet.toString(), var);
			HashSet<String> vars = new HashSet<String>();
			vars.add(fieldann.var());	
			deleteLiteralInRepo(annotation, vars, bindingSet);
			// Build query for remove and add operation
			bindingSet.removeBinding(var);
			return true;
		}
		logger.debug("SesameWriteMapper.deleteLiteral BindingSet {} hasNot var {}", bindingSet.toString(), var);
		return false;
	}
	
	/**
	 * @param annotation
	 * @param vars
	 * @param bindingSet
	 * @return
	 */
	private boolean deleteLiteralInRepo(winter annotation, HashSet<String> vars, QueryBindingSet bindingSet) {
		logger.debug("SesameWriteMapper.replaceLiteral with parameters : \n{}  \n{} \n{}  \n", new Object[]{annotation.toString(), vars.toString(), bindingSet.toString()});
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper.replaceLiteral MalformedAnnotationException : {}", e);
		} catch (MalformedQueryException e) {
			logger.error("SesameWriteMapper.replaceLiteral MalformedQueryException : {}", e);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		// TupleExpr for remove and add
		TupleExpr teDel = pq.getTupleExpr();
		//new BindingAssigner
		BindingAssigner ba = new BindingAssigner();	
		// Optimize TupleExpr for delete operation
		ba.optimize(teDel, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", teDel.toString());
		// create Statements for Remove and Add perform both
		try {
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(teDel,vars);
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Statements : {}", statement.toString());
				con.remove(statement, vf.createURI(graph.toString()));
			}
		} catch (RepositoryException e) {
			logger.error("SesameWriteMapper.replaceLiteral RepositoryException : {}", e);
		} catch (Exception e) {
			logger.error("SesameWriteMapper.replaceLiteral Exception : {}", e);
		}
		return true;
	}
	
	// ALL FOR THE OBJECTS ---------------------------------------
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setObject(net.java.rdf.annotations.winter, java.lang.Object, org.openrdf.query.algebra.evaluation.QueryBindingSet, java.lang.String)
	 */
	public boolean setObject(winter annotation, Object object, QueryBindingSet parentalBindingSet, String var) {
		logger.debug("SesameWriteMapper:setObject with parameters :  \n		{} \n		{} ", new Object[]{annotation.toString(), graph.toString()});
		
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		// Build the query and get parsedQueryObject
		try{
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	 Query for Object {} with annotation :\n {}:\n {}\n",new Object[]{object.toString(), annotation.toString(), query.toString()});
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:setObject MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setObject MalformedAnnotationException : {}", e);
		}

		// Get the tupleExpression from parsedQuery and initialize BindingAssigner
		logger.debug("	 Parsed Query {}", pq.toString());
		TupleExpr te = pq.getTupleExpr();
		logger.debug("	 TupleExpr{}", te.toString());
		BindingAssigner ba = new BindingAssigner();
		
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		
		QueryBindingSet bindingSet = parentalBindingSet;
		if(bindingSet == null){
			bindingSet =  new QueryBindingSet();
			logger.debug("	 No BindingSet found try to build new one for PATTERN {}", object.toString());
			bindingSet = ClassAnalysis.buildBindingSet(object, vf);
			logger.debug("	 BindingSet {} created", bindingSet.toString());
			((RdfSerialisable)object).setBindingSet(bindingSet);
		}		
		// Bind bindingSet to query
		logger.debug("	 BindingSet{}", bindingSet.toString());
		ba.optimize(te, pq.getDataset(), bindingSet);
		logger.debug("	 Object {} binded Query {}", object.toString(), te.toString());
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		logger.debug("	Variable {}", var);
		//create and add statements
		try{
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			logger.debug("	 Calling tets for {}", object.toString());
			tets.create(te, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setObject RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:setObject Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setObject finished!!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setObjectSetMember(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object, java.lang.String)
	 */
	public boolean setObjectSetMember(winter annotation, Object memberObject, Object declaringObject, String var) {
		// TODO separate this method maybe something of it in the Winterset or the queryextractor part.
		logger.info("SesameWriteMapper:setObjectSetMember with parameters :  \n		{} \n		{} \n		{} \n		{}", new Object[]{annotation.toString(), 
				memberObject.toString(), declaringObject.toString(), graph.toString()});
		
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		winter declaringObjAnn = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);

		// Build the query and get parsedQueryObject
		try{
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("	SesameWriteMapper:setObjectSetMember MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("	SesameWriteMapper:setObjectSetMember MalformedAnnotationException : {}", e);
		}

		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		if(bindingSet == null){
			logger.debug("	No BindingSet found try to build new one for PATTERN {}", declaringObject.toString());
			bindingSet = ClassAnalysis.buildBindingSet(declaringObject, vf);
			logger.debug("	BindingSet {} created", bindingSet.toString());
			((RdfSerialisable)declaringObject).setBindingSet(bindingSet);
		}
		logger.debug("	Variable {} : URI {}", var, ((IdentifiedByURI)memberObject).getURI().getURI().toString());
		bindingSet.removeBinding(var);
		bindingSet.addBinding(var, vf.createURI(((IdentifiedByURI)memberObject).getURI().getURI().toString()));
		ba.optimize(te, pq.getDataset(), bindingSet);
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		//create and add statements
		try{
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(te, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setURISetMember RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:setURISetMember Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setObjectSetMember finished!!!\n");
		mapTheFields(memberObject, readmapper, this);
		return true;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setURISetMember(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object, java.lang.String)
	 */
	public boolean setURISetMember(winter annotation, Object memberObject, Object declaringObject, String var) {
		// TODO separate this method maybe something of it in the Winterset or the queryextractor part.
		logger.debug("SesameWriteMapper:setURISetMember with parameters :  \n		{} \n		{} \n		{} \n		{}", new Object[]{annotation.toString(), 
				memberObject.toString(), declaringObject.toString(), graph.toString()});
		
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		winter declaringObjAnn = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);

		// Build the query and get parsedQueryObject
		try{
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		}catch(MalformedQueryException e){
			logger.error("SesameWriteMapper:setURISetMember MalformedQueryException : {}", e);
		}catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:setURISetMember MalformedAnnotationException : {}", e);
		}

		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		// Get the bindingSet from the object, if not set yet create bindingSet and set it in object
		QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		if(bindingSet == null){
			logger.debug("	 No BindingSet found try to build new one for PATTERN {}", declaringObject.toString());
			bindingSet = ClassAnalysis.buildBindingSet(declaringObject, vf);
			logger.debug("	 BindingSet {} created", bindingSet.toString());
			((RdfSerialisable)declaringObject).setBindingSet(bindingSet);
		}
		logger.debug("	 Variable {} : URI {}", var, ((WrapsURI)memberObject).getURI().toString());
		bindingSet.removeBinding(var);
		bindingSet.addBinding(var, vf.createURI(((WrapsURI)memberObject).getURI().toString()));
		ba.optimize(te, pq.getDataset(), bindingSet);
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		//create and add statements
		try{
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(te, vars);
			// add statements
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		}catch(RepositoryException e){
			logger.error("SesameWriteMapper:setURISetMember RepositoryException : {}", e);
		}catch(Exception e){
			logger.error("SesameWriteMapper:setURISetMember Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:setURISetMember finished!!!\n");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setSetTypeInfoForURIInSet(net.java.rdf.annotations.typeinfo, java.net.URI, org.openrdf.query.algebra.evaluation.QueryBindingSet, java.lang.String)
	 */
	public boolean setSetTypeInfoForURIInSet(typeinfo typeann, URI uri, QueryBindingSet parentBindingSet , String var) {
		logger.debug("SesameWriteMapper:setSetTypeInfos with parameters :  \n{} \n{} \n{} \n{}", new Object[]{typeann.toString(), uri.toString(), parentBindingSet.toString(), var.toString()});
		QueryParser qparser = new SPARQLParser();
		ParsedQuery pq = null;
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(typeann, graph);
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		parentBindingSet.removeBinding(var);
		parentBindingSet.setBinding(var, vf.createURI(uri.toString()));
		ba.optimize(te, pq.getDataset(), parentBindingSet);
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		try {
			tets.create(te);
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("SesameWriteMapper:setSetTypeInfos finished!!");
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#replaceObject(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object)
	 */
	public boolean replaceObject(winter fieldann, Object declaringObject, Object newObject){
		logger.debug("SesameWriteMapper.replaceLiteral with parameters : \n{}  \n{} ", new Object[]{fieldann.toString(), graph.toString()});
		winter annotation = fieldann;
		String var = fieldann.var();
		HashSet<String> vars = new HashSet<String>();
		vars.add(var);
		QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		if(bindingSet.hasBinding(var)){
			if (fieldann.query().equals("")){
				annotation = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);	
				logger.debug("	 Var : {}", fieldann.var());
			}
			deleteObjectInRepo(annotation, vars, bindingSet);
			// Build query for remove and add operation
			bindingSet.removeBinding(var);
		}else{
			//TODO maybe exception here
			logger.warn("SesameWriteMapper.replaceLiteral var not in BindingSet, this could mean that Literal is not set yet");
		}
		bindingSet.addBinding(var, vf.createURI(((IdentifiedByURI)declaringObject).getURI().getURI().toString()));
		setObject(annotation, newObject, bindingSet, fieldann.var());
		mapTheFields(newObject, readmapper, this);
		logger.debug("SesameWriteMapper.replaceLiteral finished!!");
		return false;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#deleteObject(net.java.rdf.annotations.winter, java.lang.Object, java.lang.Object, java.lang.String, boolean)
	 */
	public boolean deleteObject(winter fieldann, Object object, Object declaringObject, String var, boolean recursive) {
		logger.debug("SesameWriteMapper.deleteObject with parameters : \n{}  \n{} ", new Object[]{fieldann.toString(), graph.toString()});
		winter annotation = fieldann;
		if (declaringObject != null){
			if (recursive){
				if (fieldann.query().equals("")){ 
					logger.debug("	 Lower lvl required Object, delete recursive");
					annotation = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);
				}
				logger.debug("	 Lower lvl optional Object, delete recursive");
				QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
				HashSet<String> vars = new HashSet<String>();
				vars.add(fieldann.var());
				deleteObjectInRepo(annotation, vars, bindingSet);
				// Build query for remove and add operation
				bindingSet.removeBinding(var);
				removeTheFields(object);
				logger.debug("	 Object {} delete", object);
				//TODO remove typeinfo
				logger.debug("SesameWriteMapper.deleteObject finished");
				return true;
			}else{
				if(fieldann.query().equals("")){
					logger.warn("	 OBJECT MUST BE SET !! Could not be removed");
					logger.debug("	 SesameWriteMapper.deleteObject finished");
					return false;
				}else{
					logger.debug("	 Lower lvl optional only in object, Object is still present in Repo");
					QueryBindingSet bindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
					HashSet<String> vars = new HashSet<String>();
					vars.add(fieldann.var());
					deleteObjectInRepo(annotation, vars, bindingSet);
					// Build query for remove and add operation
					bindingSet.removeBinding(var);
					//TODO remove typeinfo
					logger.debug("	 Object {} delete", object);
					logger.debug("SesameWriteMapper.deleteObject finished");
					return true;
				}
			}
		}else{
			logger.debug("	 First order Object");
			removeTheFields(object);
			logger.debug("SesameWriteMapper.deleteObject finished");
			return true;
		}
	}

	/**
	 * @param annotation
	 * @param vars
	 * @param bindingSet
	 * @return
	 */
	private boolean deleteObjectInRepo(winter annotation, HashSet<String> vars, QueryBindingSet bindingSet) {
		logger.debug("SesameWriteMapper:deleteObjectInRepo with parameters : \n		{}\n		{}\n		{}\n", new Object[]{annotation.toString(), vars.toString(), bindingSet.toString()});
		ParsedQuery pq = null;
		QueryParser qparser = new SPARQLParser();
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(annotation, graph);
			logger.debug("	 Query : {}", query.toString());
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			logger.error("SesameWriteMapper:deleteObjectInRepo MalformedAnnotationException : {}", e);
		} catch (MalformedQueryException e) {
			logger.error("SesameWriteMapper:deleteObjectInRepo MalformedQueryException : {}", e);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		// TupleExpr for remove and add
		TupleExpr teDel = pq.getTupleExpr();
		//new BindingAssigner
		BindingAssigner ba = new BindingAssigner();	
		// Optimize TupleExpr for delete operation
		ba.optimize(teDel, pq.getDataset(), bindingSet);
		logger.debug("	 Binded Query {}", teDel.toString());
		// create Statements for Remove and Add perform both
		try {
			TupleExprToStatement tets = new TupleExprToStatement(vf);
			tets.create(teDel,vars);
			for (Statement statement : tets.getStatements()){
				logger.debug("	 Statements : {}", statement.toString());
				con.remove(statement, vf.createURI(graph.toString()));
			}
		} catch (RepositoryException e) {
			logger.error("SesameWriteMapper:deleteObjectInRepo RepositoryException : {}", e);
		} catch (Exception e) {
			logger.error(" SesameWriteMapper:deleteObjectInRepo Exception : {}", e);
		}
		logger.debug("SesameWriteMapper:deleteObjectInRepo finished!!\n");
		return true;
	}
			
	
	// ADDITIONAL HELPERS
	
	
	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#setSetTypeInfo(net.java.rdf.annotations.winter, net.java.rdf.annotations.winter, java.net.URI, java.lang.Object, java.lang.Object)
	 */
	@Deprecated
	public boolean setSetTypeInfo(winter fieldann, winter objann, URI uri, Object object, Object declaringObject) {
		logger.debug("SesameWriteMapper:setSetTypeInfo");
		QueryParser qparser = new SPARQLParser();
		ParsedQuery pq = null;
		
		try {
			StringBuilder query = ClassAnalysis.buildSelectAllQuery(objann, graph);
			pq = qparser.parseQuery(query.toString(), null);
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TupleExpr te = pq.getTupleExpr();
		BindingAssigner ba = new BindingAssigner();
		//TODO Fix it, very dirty hack
		QueryBindingSet parentBindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		String var = fieldann.var().concat("Type");
		parentBindingSet.addBinding(var, vf.createURI(((HasConcept)object).getConcept().getURI().toString()));
		((RdfSerialisable)declaringObject).setBindingSet(parentBindingSet);
		
		ba.optimize(te, pq.getDataset(), parentBindingSet);
		TupleExprToStatement tets = new TupleExprToStatement(vf);
		try {
			tets.create(te);
			for (Statement statement : tets.getStatements()){
				logger.debug("SesameWriteMapper:setSetTypeInfo adding statement {} to connection {}", statement.toString(), con.toString());
				//TODO add connection buffer here
				con.add(statement, vf.createURI(graph.toString()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("TypeInfo mapped!!");
		return true;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#mapTheFields(java.lang.Object, net.java.rdf.winter.ReadMapper, net.java.rdf.winter.WriteMapper)
	 */
	public boolean mapTheFields(Object object, ReadMapper readmapper, WriteMapper writemapper){
		Field[] fields = object.getClass().getDeclaredFields();
		winter objann = object.getClass().getAnnotation(winter.class);
		Object memberObject;
		logger.debug("SesameWriteMapper:mapTheFields Adding {} fields for object {}", fields.length, object.toString());
		//For all fields of object do
		for (Field field : fields){
			// We need this cause field access may be restricted
			field.setAccessible(true);
			// Check if the field is annotated
			winter fieldann = field.getAnnotation(winter.class);	
			if(fieldann != null){
				logger.debug("Field {} of type {}",field.toString(), field.getType().toString());
				// only switch over MAPPING, CLASS, OBJECT and LITERAL because PATTERNS included by PATTERNS should be annotated with MAPPING
				// logger.debug("SesameWriteMapper:mapTheFields -- SWITCHING {}\n {}\n {} ",new Object[]{ fieldann.type(), fieldann, objann});
	    		switch (fieldann.type()) {
				case MAPPING:
					logger.debug("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH MAPPING object {} is of type {}", field.toString(), winter.Type.MAPPING);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						// TODO: handle exception
					}
					if(memberObject != null){
						if(memberObject instanceof Collection){
							WinterMappingSet<Object> tmpSet = null;
							if(memberObject instanceof WinterMappingSet){
								Collection tmpCol = ((WinterMappingSet)memberObject).set;
								tmpSet = new WinterMappingSet<Object>(fieldann, null, object, vf, readmapper, this);
								if(!tmpCol.isEmpty())tmpSet.addAll(tmpCol);
							}else{
								logger.debug("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH MAPPING COLLECTION {} {} {}", new Object[]{field.getName(), field.getType(), ((Collection)memberObject).isEmpty()});
								tmpSet = new WinterMappingSet<Object>(fieldann, null, object, vf, readmapper, this);
								if(!((Collection)memberObject).isEmpty())tmpSet.addAll((Collection) memberObject);
							}
							
//							WinterSet tmpSet = new WinterSet<Object>(fieldann, null,object, vf, readmapper, writemapper);
//							tmpSet.addAllMAPPINGs((Collection) memberObject);
							try {
								field.set(object, tmpSet);
								//TODO Exception message!
							} catch (IllegalArgumentException e) {
								logger.error("", e);
							} catch (IllegalAccessException e) {
								logger.error("", e);
							}
						}else{
							writemapper.setMapping(fieldann, memberObject, ((RdfSerialisable)object).getBindingSet());
						}
					}else{
						logger.warn("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH MAPPING no MAPPING present");
					}
					break;
					
				case EXTERNALOBJECT:
					logger.info("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH OBJECT object {} is of type {}", field.toString(), winter.Type.EXTERNALOBJECT);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH MAPPING Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it
					//or no if clause 
					if(memberObject instanceof Collection){
						WinterObjectSet<Object> tmpSet = null;
						if(memberObject instanceof WinterObjectSet){
							logger.info("		FIELDTYPESWITCH OBJECT COLLECTION winter-collection found in field {}", field.getName());
							Collection tmpCol = ((WinterObjectSet)memberObject).map.values();
							tmpSet = new WinterObjectSet<Object>(fieldann, null, object, vf, readmapper, this);
							if(!tmpCol.isEmpty())tmpSet.addAll((Collection) memberObject);
						}else{
							logger.info("	 	FIELDTYPESWITCH OBJECT COLLECTION {} no winter-collection found in field {}", field.getName());
							tmpSet = new WinterObjectSet<Object>(fieldann, null, object, vf, readmapper, this);
							logger.info("	 Setting Field to {}", tmpSet.getClass().getName());
							if(!((Collection) memberObject).isEmpty())tmpSet.addAll((Collection) memberObject);
						}
						try {
							field.set(object, tmpSet);
							//TODO Exception message!
						} catch (IllegalArgumentException e) {
							logger.error("", e);
						} catch (IllegalAccessException e) {
							logger.error("", e);
						}
					}else{
						//winter objann = (winter)ClassAnalysis.getAnnotation(field.getType(), winter.class);
						logger.debug("SesameWriteMapper:mapTheFields in OBJECT calling setObject {} \n {}", objann.toString(), memberObject.toString());
						if(!fieldann.query().equals("")) writemapper.setObject(fieldann, memberObject, ((RdfSerialisable)object).getBindingSet(), fieldann.var());
						else writemapper.setObject(objann, memberObject, ((RdfSerialisable)object).getBindingSet(), fieldann.var());
						//if(!objann.query().equals("")) writemapper.setObject(objann, memberObject);
					}
					break;

				case INTERNALOBJECT:
					logger.info("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH URI object {} is of type {}", field.toString(), winter.Type.INTERNALOBJECT);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH MAPPING Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it
					//if(fieldann.query() != null){
					if(!fieldann.query().equals("")){
						if(memberObject instanceof Collection){
							WinterURISet tmpSet = null;
							if(memberObject instanceof WinterURISet){
								Collection tmpCol = ((WinterURISet)memberObject).map.values();
								tmpSet = new WinterURISet(fieldann, field.getAnnotation(typeinfo.class), object, vf, readmapper, writemapper);
								if(!tmpCol.isEmpty())tmpSet.addAll((Collection) memberObject);
							}else{
								logger.debug("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH COLLECTION {}", field.getName());
								logger.debug("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH OBJECT COLLECTION {}", field.getName());
								tmpSet = new WinterURISet(fieldann, field.getAnnotation(typeinfo.class), object, vf, readmapper, writemapper);
								if(!((Collection) memberObject).isEmpty())tmpSet.addAll((Collection) memberObject);
							}
							try {
								field.set(object, tmpSet);
								//TODO Exception message!
							} catch (IllegalArgumentException e) {
								logger.error("", e);
							} catch (IllegalAccessException e) {
								logger.error("", e);
							}
						}else{
							// TODO check this 
							logger.debug("SesameWriteMapper:mapTheFields in URI calling setObject {} \n {}", fieldann.toString(), memberObject.toString());
							writemapper.setObject(fieldann, object, ((RdfSerialisable)object).getBindingSet(), fieldann.var());
						}
					}
					break;
					
				case LITERAL:
					logger.info("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH LITERAL object {} is of type {}", field.toString(), winter.Type.LITERAL);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH LITERAL Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it
//						if((memberObject instanceof Collection)){
						if((Collection.class.isInstance(memberObject))){
							WinterLiteralSet tmpSet = null;
							if(memberObject instanceof WinterLiteralSet){
								Collection tmpCol = ((WinterLiteralSet)memberObject).set;
								tmpSet = new WinterLiteralSet<String>(fieldann, field.getAnnotation(typeinfo.class) ,object, vf, readmapper, writemapper);
								if(!tmpCol.isEmpty())tmpSet.addAll((Collection) memberObject);
							}else{
								logger.debug("SesameWriteMapper:mapTheFields -- FIELDTYPESWITCH LITERAL COLLECTION {}", field.getName());
								tmpSet = new WinterLiteralSet<String>(fieldann, field.getAnnotation(typeinfo.class) ,object, vf, readmapper, writemapper);
								if(!((Collection) memberObject).isEmpty())tmpSet.addAll((Collection) memberObject);
							}
							try {
								field.set(object, tmpSet);
								//TODO Exception message!
							} catch (IllegalArgumentException e) {
								logger.error("", e);
							} catch (IllegalAccessException e) {
								logger.error("", e);
							}
						}else{
							if(!fieldann.query().equals(""))writemapper.setLiteral(fieldann, object, fieldann.var());
							else writemapper.setLiteral(objann, object, fieldann.var());
						}
					break;
					
				default:
					break;
				}
			}else{
				logger.info("		Field {} is not annotated --> skipped", field.toString());
			}
			field.setAccessible(false);
		}
		logger.info("SesameWriteMapper:mapTheFields for Object {} finished!!\n\n",object.toString());
		return true;
	}

	/* (non-Javadoc)
	 * @see net.java.rdf.winter.WriteMapper#removeTheFields(java.lang.Object)
	 */
	public boolean removeTheFields(Object object){
		Field[] fields = object.getClass().getDeclaredFields();
		winter objann = object.getClass().getAnnotation(winter.class);
		Object memberObject;
		logger.debug("SesameWriteMapper:removeTheFields Adding {} fields for object {}", fields.length, object.toString());
		//For all fields of object do
		for (Field field : fields){
			// We need this cause field access may be restricted
			field.setAccessible(true);
			// Check if the field is annotated
			winter fieldann = field.getAnnotation(winter.class);	
			if(fieldann != null){
	    		switch (fieldann.type()) {
				case MAPPING:
					logger.debug("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH MAPPING object {} is of type {}", field.toString(), winter.Type.MAPPING);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						// TODO: handle exception
					}
					if(memberObject != null){
						if(memberObject instanceof Collection){
							((WinterMappingSet)memberObject).clear();
						}else{
							
						}
					}else{
						logger.warn("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH MAPPING no MAPPING present");
					}
					break;
				case EXTERNALOBJECT:
					logger.debug("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH OBJECT object {} is of type {}", field.toString(), winter.Type.EXTERNALOBJECT);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH MAPPING Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it
					//or no if clause 
					if(memberObject instanceof Collection){
						((WinterObjectSet)memberObject).clear();
					}else{
						deleteObject(fieldann, memberObject, object,fieldann.var(), true);
					}
					break;

				case INTERNALOBJECT:
					logger.debug("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH URI object {} is of type {}", field.toString(), winter.Type.INTERNALOBJECT);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH MAPPING Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it
					//if(fieldann.query() != null){
					if(!fieldann.query().equals("")){
						if(memberObject instanceof Collection){
							((WinterURISet)memberObject).clear();
						}else{
							deleteObject(fieldann, memberObject, object,fieldann.var(), true);
						}
					}
					break;
				case LITERAL:
					logger.debug("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH LITERAL object {} is of type {}", field.toString(), winter.Type.LITERAL);
					memberObject = null;
					try {
						memberObject = field.get(object);
					} catch (IllegalAccessException e) {
						logger.error("SesameWriteMapper:removeTheFields -- FIELDTYPESWITCH LITERAL Could not access Field {} Exception  : {}", field.toString(), e);
					}
					//TODO test it

						if((Collection.class.isInstance(memberObject))){
							((WinterLiteralSet)memberObject).clear();
						}else{
							if(!fieldann.query().equals("")) deleteLiteral(fieldann, object, fieldann.var(), true, null);
							else deleteLiteral((winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class), memberObject, fieldann.var(), true, null);
						}
					break;
					
				default:
					break;
				}
			}
		
		}
		return true;
	}
}
