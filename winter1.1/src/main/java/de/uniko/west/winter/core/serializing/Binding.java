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
package de.uniko.west.winter.core.serializing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import de.uniko.west.winter.core.QueryResult;
import de.uniko.west.winter.core.interfaces.WinterSerializer;
import de.uniko.west.winter.exceptions.WinterException;
import de.uniko.west.winter.infostructure.FieldNode;

/**
 * Controls the main (de)serializing process.
 *  -> create a binding for a fieldnode
 * 
 *  -> call bind() to recieve the String values in collection
 *  OR
 *  -> fill collection with String values and call unbind() to write to the fields
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class Binding extends Vector<String>{

	private static final long serialVersionUID = -415775274386113479L;
	
	private FieldNode fieldNode = null;
	private WinterSerializer serializer = null;
	private boolean hasNoAttachedSerializer = true;

	public Binding(FieldNode fieldNode) {
		new Binding(fieldNode, true, true);
	}
	
	public Binding(FieldNode fieldNode, boolean bindOnCreation, boolean attachDefaultSerializer) {
		if (fieldNode == null){
			throw new WinterException("FieldNode for binding must not be null!");
		}
		
		this.fieldNode = fieldNode;
		
		if (fieldNode.getSerializerID() == null){
			if (attachDefaultSerializer){
				
				//try find a serializer
				if (fieldNode.isACollection()){
					Collection<?> col = (Collection<?>)fieldNode.getValue();
					findCollectionSerializer:
					for (Object object : col) {
						serializer = SerializingManager.findSerializer(object);
						if (serializer != null){
							break findCollectionSerializer;
						}
					}
				}else{
					serializer = SerializingManager.findSerializer(fieldNode.getValue());
				}
				if (serializer == null){ // didnt find a serializer
					System.out.println("Found no serializer for fieldNode "+ fieldNode.getVar() + " (for val: " + fieldNode.getValue().getClass() + ")" );
					hasNoAttachedSerializer = true;	
				}else{
					hasNoAttachedSerializer = false;
				}
				
//				explicit no serializer wanted
			}else{
				hasNoAttachedSerializer = true;	
			}
		}else{
			serializer = SerializingManager.getSerializer(fieldNode.getSerializerID());
			hasNoAttachedSerializer = (serializer == null);
		}
		
		
		if (bindOnCreation){
			bind();
		}
	}

	public FieldNode getFieldNode() {
		return fieldNode;
	}
	
	public void bind(){
		if (fieldNode.isACollection()){
			Collection<?> col = (Collection<?>)fieldNode.getValue();
			for (Object object : col) {
				if (hasNoAttachedSerializer){
					add( SerializingManager.findSerializer(object).serialize(object) );
				}else{
					add( serializer.serialize(object) );
				}
			}
		}else{
			if (hasNoAttachedSerializer){
				WinterSerializer tmpSerializer = SerializingManager.findSerializer(fieldNode);
				if (tmpSerializer == null){
					throw new WinterException("Cannot serialize without attached WinterSerializer!(FieldNode: " + fieldNode.getVar() + ")");
				}
				hasNoAttachedSerializer = false;
			}
			add( serializer.serialize(fieldNode.getValue()) );
		}
	}

	public void unbind(){
		//TODO use typemap/stringanalysis to find serializer ?
		if (hasNoAttachedSerializer){
			throw new WinterException("Cannot deserialize without attached WinterSerializer! (FieldNode: " + fieldNode.getVar() + ")");
		}
		if (fieldNode.isACollection()){
			Collection<Object> col = new HashSet<Object>();
			for (String val : this) {
				col.add( serializer.deserialize(val) );
			}
			fieldNode.setValue(col);
		}else{
			fieldNode.setValue(serializer.deserialize(this.get(0)));
		}
	}
	
	public void fillFromQueryResult(QueryResult qr, String var) {
		addAll(qr.getBindingForVar(var));
	}

}
