package de.uniko.west.winter.core;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;

public class JenaQueryResult implements QueryResult {
	
	protected static transient Logger logger = LoggerFactory.getLogger(JenaQueryResult.class.getSimpleName());
	
	private Map<String, Set<String>> bindingMap = new HashMap<String, Set<String>>();
	private ResultSet rs = null;
	private Boolean wasSuccessful = null;
	private boolean isBooleanResult = false;
	
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1"; 
	public static final String CHARSET_DEFAULT = "ISO-8859-1"; 
	
	public JenaQueryResult(ResultSet rs) {
		this.rs = rs;
		isBooleanResult = false;
		logger.debug("Converting ResultSet...");
		while(rs.hasNext()){
			Binding binding = rs.nextBinding();
			Iterator<Var> vars= binding.vars();
			while(vars.hasNext()){
				Var var = vars.next();
				Node node = binding.get(var);
				String varName = var.getName();
				String value = node.toString();
				logger.debug("   var: " + varName + "  ->  " + value);
				if(!bindingMap.containsKey(varName)){
					Set<String> bindings = new HashSet<String>();
					bindings.add(value);
					bindingMap.put(varName, bindings);
					continue;
				}
				bindingMap.get(varName).add(value);
			}
		}
	}
	
	public JenaQueryResult(boolean success) {
		this.wasSuccessful = success;
		isBooleanResult = true;
	}
	
	@Override
	public Set<String> getBindingForVar(String var) {
		return bindingMap.get(var);
	}
	
	public String toJsonResult() throws UnsupportedEncodingException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(baos, rs);
		return baos.toString(CHARSET_DEFAULT);
	}
	
	@Override
	public boolean wasSuccessful() {
		return wasSuccessful;
	}

	@Override
	public Set<String> getVars() {
		return bindingMap.keySet();
	}
	
	@Override
	public boolean isBooleanResult() {
		return isBooleanResult;
	}
	

}
