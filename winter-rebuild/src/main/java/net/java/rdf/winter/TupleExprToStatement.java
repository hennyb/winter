package net.java.rdf.winter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.Var;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TupleExprToStatement extends QueryModelVisitorBase<Exception>{
	
	protected static transient Logger logger = LoggerFactory.getLogger(TupleExprToStatement.class.getName());

	List<Statement> statements = null;
	ValueFactory vfactory = null;
	//TODO set namspaces
	String subjnamespace = "http://www.subtest.de";
	String objnamespace = "http://www.subtest.de";
	String prednamespace = "http://www.subtest.de";
	//TODO Weg Damit
	WeakHashMap<String, URI> map;
	HashSet<String> vars; 
	
	public TupleExprToStatement(ValueFactory vf){
		vfactory = vf;
		statements = new ArrayList<Statement>();
		map = new WeakHashMap<String, URI>();
	}
	
	public TupleExprToStatement(ValueFactory vf, String subjns){
		this(vf);
		subjnamespace = subjns;
	}
	
	public TupleExprToStatement(ValueFactory vf, String subjns, String objns){
		this(vf, subjns);
		objnamespace = objns;
	}
	
	public TupleExprToStatement(ValueFactory vf, String subjns,String objns, String prens){
		this(vf, subjns, objns);
		prednamespace = prens;
	}
	
	public String getSubjNamespace(){
		return subjnamespace;
	}
	
	public void setSubjNamespace(String subjns){
		subjnamespace = subjns;
	}
	
	public String getObjNamespace(){
		return objnamespace;
	}
	
	public void setObjNamespace(String objns){
		objnamespace = objns;
	}
	
	public String getPredNamespace(){
		return prednamespace;
	}
	
	public void setPredNamespace(String prens){
		prednamespace = prens;
	}
	
	public List<Statement> getStatements(){
		return statements;
	}
	
	public void create(TupleExpr expr, HashSet<String> vars)throws Exception{
		this.vars = vars;
		statements = new ArrayList<Statement>();
		expr.visit(this);
	}
	
	public void create(TupleExpr expr) throws Exception {
		statements = new ArrayList<Statement>();
		expr.visit(this);
		//return statement;
	}

	public void meet(StatementPattern node) throws Exception {
		if (vars == null){
			logger.info("VARS is null creating Statement {}", vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())).toString());
			
			statements.add(vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())));
		}else if (vars.isEmpty()){
			logger.info("VARS is empty creating Statement {}", vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())).toString());
			statements.add(vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())));
		}else if (vars.contains("")){
			logger.info("VARS is empty creating Statement {}", vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())).toString());
			statements.add(vfactory.createStatement(processSubj(node.getSubjectVar()), 
					processPred(node.getPredicateVar()),
					processObj(node.getObjectVar())));
		}else{
			URI subj = null;
			URI pred = null;
			Value obj = null;
			Var subject = node.getSubjectVar();
			if(!vars.contains(subject.getName())){
				Var object = node.getObjectVar();
				if (!vars.contains(object.getName())){
					pred = null;
				}else{
					subj = processSubj(subject);
					pred = processPred(node.getPredicateVar());
					obj = processObj(object);
				}
			}else{
				 subj = processSubj(subject);
				 pred = processPred(node.getPredicateVar());
				 obj = processObj(node.getObjectVar());				
			}
			if (pred != null){
			statements.add(vfactory.createStatement(subj, pred, obj));
			logger.debug("PROCESSING Vars creating Statement {}", vfactory.createStatement(subj, pred, obj).toString());
			}
			logger.debug("PROCESSING Vars no Object created no match");
		}
	}
	
	//TODO Aufr�umen
	public URI processSubj(Var subj) throws Exception{
		logger.debug("Processing subj {}", subj.toString());
		if(subj.hasValue()){
			return (URI)subj.getValue();

		}else if (subj.getName() != null){
			if (map.containsKey(subj.getName())){
				return (URI)map.get(subj.getName());
			}else{
				logger.info("	No Subject found using default namespace");
				URI id = vfactory.createURI(objnamespace + "#" + UUID.randomUUID().toString());
				map.put(subj.getName(), id);
				return id;
			}
		}else{
			throw new NotSetException("Subject not set....");
		}
	}
	
	//TODO Aufr�umen
	public URI processPred(Var pred) throws Exception{
		logger.debug("Processing pred {}", pred.toString());
		if (pred.hasValue()){
			return (URI)pred.getValue();
		}else if (pred.getName() != null){
			if (map.containsKey(pred.getName())){
				return (URI)map.get(pred.getName());
			}else{
				logger.info("	No Predicate found using default namespace");
				URI predicate = vfactory.createURI(prednamespace + "/" + UUID.randomUUID().toString());
				map.put(pred.getName(), predicate);
				return predicate;
			}
		}else{
			throw new NotSetException("Predicate not set....");
		}
	}
	
	//TODO Aufr�umen
	public Value processObj(Var obj) throws Exception{
		logger.debug("Processing obj {}", obj.toString());
		if(obj.hasValue()){
			return (Value) obj.getValue();
		}else if (obj.getName() != null){
			if (map.containsKey(obj.getName())){
				return (Value)map.get(obj.getName());
			}else{
				logger.info("	No Object found using default namespace");
				URI id = vfactory.createURI(objnamespace + "/" + UUID.randomUUID().toString());
				map.put(obj.getName(), id);
				return (Value)id;
			}
		}else{
			throw new NotSetException("Object not set....");
		}	
	}
}
