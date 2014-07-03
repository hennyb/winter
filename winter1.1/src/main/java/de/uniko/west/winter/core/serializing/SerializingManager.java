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
package de.uniko.west.winter.core.serializing;

import java.net.URI;
import java.util.Hashtable;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.core.interfaces.WinterSerializer;
import de.uniko.west.winter.infostructure.FieldNode;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public abstract class SerializingManager {
	
	protected static transient Logger logger = LoggerFactory.getLogger(SerializingManager.class.getSimpleName());
	
	private static Hashtable<String, WinterSerializer> defaultSerializers = new Hashtable<String, WinterSerializer>();
	private static Hashtable<String, WinterSerializer> customSerializers = new Hashtable<String, WinterSerializer>();
	
	public static void initDefaultSerializers(Hashtable<URI, Class<?>> typeMap){
		initPreprocessClasses(typeMap);
		registerDefaultSerializer(new LiteralSerializer());
		registerDefaultSerializer(new UriSerializer());
	}
	
	private static void initPreprocessClasses(Hashtable<URI, Class<?>> typeMap){
		WinterSerializer winterSerializer = null;
		
		for (URI uri : typeMap.keySet()){
			try{
				winterSerializer = new DefaultClassSerializer(typeMap.get(uri));
				registerDefaultSerializer(winterSerializer, uri.toString());
			}catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
	}

	private static String registerDefaultSerializer(WinterSerializer winterSerializer){
		String key = winterSerializer.toString() +"_"+ UUID.randomUUID();
		defaultSerializers.put(key, winterSerializer);
		return key;
	}
	
	private static void registerDefaultSerializer(WinterSerializer winterSerializer, String id){
		defaultSerializers.put(id, winterSerializer);
	}
	
	public static void registerCustomSerializer(WinterSerializer winterSerializer, FieldNode node){
		String generatedID = node.getType() + UUID.randomUUID();
		customSerializers.put(generatedID, winterSerializer);
		node.setSerializerID(generatedID);
	}
	
	
	
//	public static String serialize(FieldNode fieldNode){
//		WinterSerializer serializer = checkSerializer(fieldNode);
//		return serializer.serialize(fieldNode.getValue());
//	}
//	
//	public static void deserialize(FieldNode fieldNode, String value){
//		WinterSerializer serializer = checkSerializer(fieldNode);
//		Object newValue = serializer.deserialize(value);
//		fieldNode.setValue(newValue);
//	}
		
//	static WinterSerializer checkSerializer(FieldNode fieldNode){
//		WinterSerializer serializer = null;
//		if (fieldNode.getSerializerID() == null){
//			serializer = findDefaultSerializer(fieldNode.getValue());
////			fieldNode.setSerializerID(findDefaultSerializerIDforSerializer(serializer));
//		}else{
//			serializer = getSerializer(fieldNode.getSerializerID());
//		}				
//		
//		if (serializer == null){
//			throw new WinterException("Could not find serializer for field: " + fieldNode);
//		}
//		return serializer;
//	}
	
	
	static String findDefaultSerializerID(Object object){
		for (String key : defaultSerializers.keySet()) {
			if (defaultSerializers.get(key).canSerialize(object.getClass())){
				return key;
			}
		}
		return null;
	}
	
	static String findDefaultSerializerIDforSerializer(WinterSerializer serializer){
		for (String key : defaultSerializers.keySet()) {
			if (defaultSerializers.get(key) == serializer){
				return key;
			}
		}
		return null;
	}
	
	static WinterSerializer getSerializer(String id){
		if (customSerializers.containsKey(id)){
			return customSerializers.get(id);
		}
		if (defaultSerializers.containsKey(id)){
			return defaultSerializers.get(id);
		}
		return null;
	}
	
	static WinterSerializer findSerializer(Object object){
		WinterSerializer result = findCustomSerializer(object);
		if (result==null){
			result = findDefaultSerializer(object);
		}
		return result;
	}

	/**
	 * @return
	 */
	public static WinterSerializer findDefaultSerializer(Object object) {
		for (WinterSerializer serializer : defaultSerializers.values()) {
			if (serializer.canSerialize(object.getClass())){
				return serializer;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	static WinterSerializer findCustomSerializer(Object object) {
		for (WinterSerializer serializer : customSerializers.values()) {
			if (serializer.canSerialize(object.getClass())){
				return serializer;
			}
		}
		return null;
	}
	
	
}
