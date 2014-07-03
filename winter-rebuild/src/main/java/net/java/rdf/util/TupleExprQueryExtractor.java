package net.java.rdf.util;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.java.rdf.winter.NotSetException;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.Var;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;
import org.openrdf.query.parser.ParsedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TupleExprQueryExtractor extends QueryModelVisitorBase<Exception> {
	
	protected static transient Logger logger = LoggerFactory.getLogger(TupleExprQueryExtractor.class.getName());
	
	ValueFactory vfactory;
	WeakHashMap<String, URI> map;
	HashSet<String> vars = null;
	StringBuilder query = new StringBuilder();
	
	public TupleExprQueryExtractor(ValueFactory vf){
		this.vfactory = vf;
		map = new WeakHashMap<String, URI>();
	}
	
	public StringBuilder getQuery (){
		return query;
	}
	
	public void extract(TupleExpr expr, HashSet<String> vars) throws Exception {
		this.vars = vars;
		if(vars == null){
			throw new ExtractorException("Could not extract, no vars set");
		}
		expr.visit(this);
		//return statement;
	}
	
	public void meet(StatementPattern node) throws Exception{
		logger.debug("TupleExprQueryExtractor:meet in StatementPattern {}", node.toString());
		String subj = node.getSubjectVar().getName();
		String obj = node.getObjectVar().getName();
		logger.debug("TupleExprQueryExtractor:meet with {} and {}", subj, obj);
		if((vars.contains(subj)) || (vars.contains(obj))){
			logger.debug("TupleExprQueryExtractor:meet Subject {}, Object {}", subj, obj);
			query.append(" ?").append(node.getSubjectVar().getName()).append(" ");
			query.append("<").append(node.getPredicateVar().getValue()).append("> ");
			query.append(" ?").append(node.getObjectVar().getName()).append(" .");
			logger.debug(query.toString());
		}
	}
}
