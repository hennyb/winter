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
package de.uniko.west.winter.core.interfaces;

/**
 * Winter is capable of mapping Java primitives, Java.lang.String objects, Java.net.URL objects and RDFSerializables.
 * In order to map other objects, winterserializer objects can be registered.<p>
 *
 * Example:<p>   
 * 
 * usage of custom object:<p>
 * <pre>
 * class ClassA implements RDFSerializable{
 * 	@winter_var("myValue")
 * 	MyCustomClass someValueField;
 * 	(...)
 * }</pre><p>
 *
 *
 * example declaration of the custom class:<p>
 * <pre>
 * Class MyCustomClass{
 * 	public Integer aValue;
 * }</pre><p>
 * 
 * 
 * implementation of a custom serializer:
 * <pre>
 * class MyCustomSerializer implements WinterSerializer{
 * 	public String serialize(Object object){
 * 		MyCustomClass mcc = (MyCustomClass)object;
 * 		return Integer.toString(mcc.aValue);
 * 	}
 * 	public Object deserialize(String serObject){
 * 		MyCustomClass mcc = new MyCustomClass();
 * 		mcc.aValue = Integer.parseInt(serObject);
 * 		return mcc;
 * 	}
 * 
 * 	public boolean canSerialize(Class<?> clazz){
 * 		return (MyCustomClass.isAssignableFrom(clazz));
 * 	}</pre><p>
 * 
 * 
 * registering the custom serializer:
 * <pre>
 * classAObject.winter().registerSerializer(myCustomSerializerObject, "someValueField");
 * </pre><p>
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public interface WinterSerializer {

	/**
	 * Converts (serializes) the given object to a String.
	 * 
	 * @param object to be serialized.
	 * @return String serialization of the give object 
	 */
	public String serialize(Object object);
	
	/**
	 * Converts (deserializes) the given String to an object.
	 * 
	 * @param serObject String to be deserialized
	 * @return object deserialized from String
	 */
	public Object deserialize(String serObject);
	
	/**
	 * Returns true if the Class given by the clazz parameter can be
	 * (de-)serialized by this implementation of WinterSerializer. 
	 * 
	 * @param clazz to check compatibility on.
	 * @return true if can be (de-)serialized
	 */
	public boolean canSerialize(Class<?> clazz);
}
