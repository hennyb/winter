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

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.core.WinterImpl;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.serializing.Binding;
import de.uniko.west.winter.utils.ClassReflection;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class ObjectNode extends InnerNode{
	protected static transient Logger logger = LoggerFactory.getLogger(ObjectNode.class.getSimpleName());
	//Lookup tables
	protected Hashtable<String, InfoNode> fieldName2Info = new Hashtable<String, InfoNode>();
	protected Hashtable<String, InfoNode> varName2Info = new Hashtable<String, InfoNode>();
	protected Hashtable<String, InfoNode> refName2Info = new Hashtable<String, InfoNode>();
	
	protected WinterImpl winter = null;
	
	//objects ID if set.
	protected FieldNode id = null;
	
	//The object itself
	protected RDFSerializable obj;
	
	public ObjectNode(InnerNode parent, RDFSerializable obj){
		logger.info("Creating ObjectNode from Object \n {}\n with parent {}",obj.toString(), (parent == null)? "null" : parent.toString() );
		this.obj = obj;
		addParent(parent);
		winter = new WinterImpl(obj);
		obj.setWinter(winter);
		winter.setObjectNode(this);
		//Get query for RelationObjects
		winter_query annotation;
		if((annotation = obj.getClass().getAnnotation(winter_query.class)) != null) {
			query = annotation.value();
		}
	}
	
	public ObjectNode(RDFSerializable obj){
		this(null, obj);
	}
	
	public Object acceptVisitor(InfoStructureVisitor visitor, Object data){
		return visitor.visit(this, data);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.west.winter.infostructure.InfoNode#getBindingForVar(java.lang.String)
	 */
	@Override
	public Binding getBindingForVar(String var) {
		logger.debug("In ObjectNode {} getting binding for {} ...",this.toString() , var);
		FieldNode fieldNode = null;
		if((fieldNode = (FieldNode) varName2Info.get(var)) == null) {
			fieldNode = (FieldNode) refName2Info.get(var);
		}
		if (fieldNode == null){
			return null;
		}
		return fieldNode.getBindingForVar(var);
	}
	
	public FieldNode getFieldNodeByFieldName(String fieldName){
		if (fieldName2Info.containsKey(fieldName)){
			return (FieldNode)fieldName2Info.get(fieldName);
		}
		return null;
	}
	
	public FieldNode getID() {
		return id;
	}
	
	public RDFSerializable getObject(){
		return this.obj;
	}
	
	public Set<String> getVarNames(){
		return varName2Info.keySet();
	}
	
	public void inflate(){
		logger.debug("Inflating Object {}", obj.toString());
		Vector<Field> fields = ClassReflection.getFields(this.obj.getClass());
		Field idField = null;
		
		// Process ID field
//		TODO multiple @winter_id annotationed fields will cause some trouble!!
		for(Field field : fields){
			if (field.getAnnotation(winter_id.class) != null) {
				FieldNode fieldNode = new FieldNode(this, field);
				addChild(fieldNode);
				idField = field;
				break;
			}
		}

		//Remove ID field, which is already processed
		fields.remove(idField);
		
		//Process other fields
		for(Field field : fields){
			FieldNode fieldNode = new FieldNode(this, field);
			addChild(fieldNode);
			fieldNode.inflate();
		}
	}
	
	/**
	 * @param fieldNode
	 */
	public void setID(FieldNode fieldNode) {
		id = fieldNode;
	}
	
	public String toString(){
		String result = "ObjectNode[" + 
			"rdfobject: " + obj.toString() +"]";
		return result;
	}
	
	public WinterImpl winter(){
		return winter;
	}
}
