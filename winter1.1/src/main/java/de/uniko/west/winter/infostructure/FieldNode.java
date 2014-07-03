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
 * 
 */
package de.uniko.west.winter.infostructure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_predicate;
import de.uniko.west.winter.annotation.winter_ref;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.WinterImpl;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;
import de.uniko.west.winter.core.serializing.Binding;
import de.uniko.west.winter.exceptions.WinterException;
import de.uniko.west.winter.utils.ClassReflection;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class FieldNode extends InnerNode{
	protected static transient Logger logger = LoggerFactory.getLogger(FieldNode.class.getSimpleName());
	
	Boolean isID = false;
	Boolean isRef = false;
	String type;

	String var;
	String predicate;
	Vector<String> refs = null;
	
	Field field = null;
	
	Method setter = null;
	Method getter = null;

	String serializerID;
	
	Vector<Annotation> annotations;

	//TODO check if values are empty or empty strings
	public FieldNode(ObjectNode parent, Field field){
		logger.info("Creating FieldNode from Field \n {}\n with parent {}",field.toString(), parent.toString() );	
		this.field = field;
		addParent(parent);
		annotations = ClassReflection.getWinterAnnotations(field);
		Annotation annotation = null;
		
		// fill fieldName2Info Map
		var = field.getName();
		parent.fieldName2Info.put(var, this);
		
		// Process @winter_var annotation
		if ((annotation = field.getAnnotation(winter_var.class)) != null){
			logger.debug("Var annotation found {}", annotation);
			var = ((winter_var)annotation).value();
		}
		
		// Process @winter_id annotation
		if ((annotation = field.getAnnotation(winter_id.class)) != null){
			logger.info("ID annotation found {}", annotation);
			isID = true;
			parent.setID(this);
			if(((winter_id)annotation).value() != null ){
				type = ((winter_id)annotation).value();
				query += " ?" + var + " rdf:type " + "<" + type + ">" + ". ";
			}
		}
		try{
		//Process @winter_predicate annotation
		if ((annotation = field.getAnnotation(winter_predicate.class)) != null){
			logger.info("Predicate annotation found {}", annotation);
			predicate = ((winter_predicate)annotation).value();
			query += " ?" + parent.getID().getVar() + " " + predicate + " " + " ?" + var + ". ";
		}
		}catch (Exception e) {
			System.out.println(parent);
			System.out.println(parent.getID());
			System.out.println(parent.getID().getVar());
			System.out.println(predicate);
		}
		if ((annotation = field.getAnnotation(winter_ref.class)) != null){
			logger.debug("Refs annotation found {}", annotation);
			refs = new Vector<String>();
			isRef = true;
			for (String ref : ((winter_ref)annotation).value()){
				logger.debug("Adding ref {} to refName2Info map", ref);
				refs.add(ref);
				parent.refName2Info.put(ref, this);
			}
		}
		
		// fill varName2Info Map
		parent.varName2Info.put(var, this);
		parent.query += query;
		
		if (predicate != null) findGetter(parent);
		if (predicate != null) findSetter(parent);
		
//		FieldSerializer.findDefaultSerializerID(object) zur laufzeit rausfinden, oder annotation abfragen
	}
	
	public Object acceptVisitor(InfoStructureVisitor visitor, Object data){
		return visitor.visit(this, data);
	}

	@Override
	public void addParent(InnerNode parent){
		parents.add(parent);
	}
	
	// Create child ObjectNode
	public InfoNode createChild(Object object){
		InfoNode node = null;
		if (object instanceof RDFSerializable){
			Winter winter;
			if((winter = ((RDFSerializable)object).winter()) != null){
				node = ((WinterImpl)winter).getObjectNode();
				node.addParent(this);
			}else{
				node = new ObjectNode(this, (RDFSerializable)object);
			}
		}else{
			node = new LeafNode(this, object);
		}
		return node;
	}
	
	private void findGetter(ObjectNode parent) {
		logger.info("Searching getter for " + predicate);
		Vector<Method> methods = ClassReflection.getWinterAnnotatedMethods( parent.getObject().getClass() );
		for (Method method : methods) {
			if (method.getAnnotation(winter_predicate.class) != null &&						// hat predicate-annotation
				method.getAnnotation(winter_predicate.class).value().equals( predicate )&&	// predicate stimmt überein
				method.getParameterTypes().length == 0 && 									// kein parameter
				method.getReturnType().isAssignableFrom(field.getType()) ){					// returntype passt zum typ des feldes
				getter = method;
				logger.info("Found getter for " + predicate);
				return;
			}
		}
	}
	
	
	private void findSetter(ObjectNode parent) {
		logger.info("Searching setter for " + predicate);
		Vector<Method> methods = ClassReflection.getWinterAnnotatedMethods( parent.getObject().getClass() );
		for (Method method : methods) {
			if (method.getAnnotation(winter_predicate.class) != null &&						// hat predicate-annotation
				method.getAnnotation(winter_predicate.class).value().equals( predicate )&&	// predicate stimmt überein
				method.getParameterTypes().length == 1 && 									// genau 1 parameter
				method.getParameterTypes()[0].isAssignableFrom(field.getType()) ){			// parameter passt zum typ des feldes
				setter = method;
				logger.info("Found setter for " + predicate);
				return;
			}
		}
	}	
	
	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoNode#getBindingForVar(java.lang.String)
	 */
	@Override
	public Binding getBindingForVar(String var) {
		logger.info("getBindingsForVar {} from Fieldnode {}", var, this.var);
		if(! var.equals(this.var) ){
			return ((ObjectNode)getParents().get(0)).getID().getBindingForVar(var);
		}
			
		if (isRef) {
			logger.debug("FieldNode {} isREF", this);
			if (this.isACollection()) {
				for (Object object : (Iterable<?>) getValue()) {
					return ((WinterImpl)((RDFSerializable) object).winter()).getObjectNode().getBindingForVar(var);
				}
			} else {
				return ((WinterImpl)((RDFSerializable) getValue()).winter()).getObjectNode().getBindingForVar(var);
			}
		}
		logger.debug("FieldNode {} !isREF", this);

		return new Binding(this);

	}
	
	
	public Field getField() {
		return field;
	}
	

	public String getSerializerID() {
		return serializerID;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return
	 */
	public Object getValue() {
		if (getter == null){
			try {
				field.setAccessible(true);
				return field.get(((ObjectNode)parents.get(0)).getObject());
			}catch (Exception e) {
				throw new WinterException("Could not get field content. ", e);
			}
		}else{
			try {
				return getter.invoke( 
						((ObjectNode)parents.get(0)).getObject(),
						new Object[]{});
				
			}catch (Exception e) {
				throw new WinterException("Could not get field content. ", e);
			}
		}
		
	}
	
	public String getVar(){
		return var;
	}
	
	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoNode#inflate()
	 */
	@Override
	public void inflate() {
			Object contents = getValue();
			logger.debug("Inflating Field {} ",field.toString());
			InfoNode node = null;
			if(isACollection()){
				for (Object content : (Iterable<?>)contents){
					node = createChild(content);
					children.add(node);
					node.inflate();
				}
			}else{
				node = createChild(contents);
				children.add(node);
				node.inflate();
			}
	}

	public boolean isACollection(){
		return Collection.class.isAssignableFrom(field.getType());
	}
	
	public void setSerializerID(String id) {
		serializerID = id;
	}
	
	/**
	 * @return
	 */
	public void setValue(Object value) {
		if (setter == null){
			try {
				field.setAccessible(true);
				field.set(((ObjectNode)parents.get(0)).getObject(), value);
			}catch (Exception e) {
				throw new WinterException("Could not set field content. ", e);
			}
		}else{
			try {
				setter.invoke( 
						((ObjectNode)parents.get(0)).getObject(),
						new Object[]{value});
				
			}catch (Exception e) {
				throw new WinterException("Could not set field content. ", e);
			}
		}
		
	}

	public String toString(){
		String result = "FieldNode[";
		result+="field: "+ field.getName() + ", ";
		if (var!=null) result += "var: " + this.var + ", ";
		if (predicate!=null) result += "pred:" + this.predicate + ", ";
		if (query!=null) result += "query: " + this.query;
		if (refs!=null && !refs.isEmpty()){
			result+= "refs:[";
			for (int i = 0; i < refs.size(); i++) {
				result += refs.get(i);	
			}
			result+= "]";
		}
		result+= "]";
		return result;
	}
	
}
