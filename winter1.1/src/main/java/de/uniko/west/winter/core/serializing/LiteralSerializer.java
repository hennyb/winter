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

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import de.uniko.west.winter.core.interfaces.WinterSerializer;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class LiteralSerializer implements WinterSerializer {
	Hashtable<String, Class<?>> string2TypeTable = new Hashtable<String, Class<?>>();
	Hashtable<Class<?>, String> type2StringTable = new Hashtable<Class<?>, String>();
	
	public LiteralSerializer() {
		string2TypeTable.put("string", String.class);
		string2TypeTable.put("char", Character.class);
		string2TypeTable.put("integer", Integer.class);
		string2TypeTable.put("short", Short.class);
		string2TypeTable.put("long", Long.class);
		string2TypeTable.put("float", Float.class);
		string2TypeTable.put("double", Double.class);
		string2TypeTable.put("byte", Byte.class);
		string2TypeTable.put("boolean", Boolean.class);
		
		type2StringTable.put(String.class, "string");
		type2StringTable.put(Character.class, "char");
		type2StringTable.put(char.class, "char");
		type2StringTable.put(Integer.class, "integer");
		type2StringTable.put(int.class, "integer");
		type2StringTable.put(Short.class, "short");
		type2StringTable.put(short.class, "short");
		type2StringTable.put(Long.class, "long");
		type2StringTable.put(long.class, "long");
		type2StringTable.put(Float.class, "float");
		type2StringTable.put(float.class, "float");
		type2StringTable.put(Double.class, "double");
		type2StringTable.put(double.class, "double");
		type2StringTable.put(Byte.class, "byte");
		type2StringTable.put(byte.class, "byte");
		type2StringTable.put(Boolean.class, "boolean");
		type2StringTable.put(boolean.class, "boolean");
	}
	
	@Override
	public Object deserialize(String serObject) {
		
		String rawString = serObject.replaceAll("\"", "");
		String valString = rawString.split("^^")[0];
		String typeString = rawString.split("^^")[1].trim().toLowerCase();

		Constructor<?> constructor = null;
		for (String key : string2TypeTable.keySet()) {
			if (typeString.endsWith(key)){
				try {
					constructor = string2TypeTable.get(key).getConstructor(String.class);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		if (constructor == null){
			return null;
		}
		try {
			return constructor.newInstance(valString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String serialize(Object object) {
		Class<?> objectType = object.getClass();
		String literal = "";
		
		if(objectType.equals(String.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:string";
		}else if(objectType.equals(int.class) || objectType.equals(Integer.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:integer";
		}else if(objectType.equals(short.class) || objectType.equals(Short.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:short";
		}else if(objectType.equals(double.class) || objectType.equals(Double.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:double";
		}else if(objectType.equals(float.class) || objectType.equals(Float.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:float";
		}else if(objectType.equals(byte.class) || objectType.equals(Byte.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:byte";
		}else if(objectType.equals(boolean.class) || objectType.equals(Boolean.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:boolean";
		}else if(objectType.equals(long.class) || objectType.equals(Long.class)){
			literal = "\""+object.toString()+"\""+"^^xsd:long";
		}
		
		return literal;
	}

	@Override
	public boolean canSerialize(Class<?> clazz) {
		return type2StringTable.containsKey(clazz);
	}
	
	

}
