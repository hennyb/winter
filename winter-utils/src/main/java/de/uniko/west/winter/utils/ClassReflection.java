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
package de.uniko.west.winter.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_predicate;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_ref;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.exceptions.WinterException;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
//TODO: USE typemap
public class ClassReflection {
	
	public static boolean includeSuperClassFields = true;
	private static final Hashtable<AccessibleObject, Vector<Annotation>> winterAnnotationsCache = new Hashtable<AccessibleObject, Vector<Annotation>>();
	private static final Hashtable<Class<?>, Vector<Field>> serializableFieldsCache = new Hashtable<Class<?>, Vector<Field>>();
	private static final Hashtable<URI, Class<?>> typeMap = new Hashtable<URI, Class<?>>();
	
	public static Hashtable<URI, Class<?>> initTypeMap(){
		
		WinterConfiguration wc = PreprocessTransfer.loadTransferFile();
		List<String> listedClassNames = wc.getListProperty(PreprocessTransfer.KEY_WINTER_CLASSES);
		
		for (String className : listedClassNames) {
			try{
				try {
					Class<?> clazz = Class.forName(className);
					//skip Interfaces
					if (clazz.isInterface()) continue;
					
					String id = wc.getProperty(PreprocessTransfer.KEY_WINTER_ID_PREFIX+className);
					System.out.println("ClassReflection: className: " + className + " - id: " + id);
					URI typeURI = new URI(id);
					typeMap.put(typeURI, clazz);
				}catch (ClassNotFoundException e) {
					throw new WinterException("Preprocessed class "+ className +" could not be found.", e);
				}catch (URISyntaxException e2) {
					throw new WinterException("URI Syntax of preprocessed class "+ className +" is invalid.", e2);
				}
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
		return typeMap;
	}
	
	public static Vector<Field> getFields(Class<?> clazz){
		Vector<Field> result;
		if (serializableFieldsCache.containsKey(clazz)){
			result = serializableFieldsCache.get(clazz);
		}else{
			result = getWinterAnnotatedFields(clazz);
			serializableFieldsCache.put(clazz, result);
		}
		return new Vector<Field>(result); // use a copy here!! otherwise cache could be manipulated...
	}
		
		
	
	private static Vector<Field> getWinterAnnotatedFields(Class<?> clazz){
		
		Class<?> currentClass = clazz;
		Vector<Field> fields = new Vector<Field>();
		do {				
			Field[] currentFields = currentClass.getDeclaredFields();
			
			processCurrentFields:
			for (int i = 0; i < currentFields.length; i++) { // fields of current (super-)class
				if ( ! hasWinterAnnotation(currentFields[i])) {
//					not winter annotated -> skip
					continue processCurrentFields;
				}
				for (int j = 0; j < fields.size(); j++) { // fields found so far
					if ( currentFields[i].getName().equals( fields.get(j).getName() )){
						System.out.println("skipped -> already there");
//						field with that name already found (with annotation) in subclass -> skip
						continue processCurrentFields;
					}
				}
//				has annotation & unique name ?  -> add
				fields.add(currentFields[i]);
			}
			
			currentClass = currentClass.getSuperclass();
		} while (includeSuperClassFields && currentClass!=null);
		
		return fields;
	}

	
	
	/**
	 * @return Vector containing winter-specific annotations of a field, Vector
	 *         is empty if there is no
	 */
	public static Vector<Annotation> getWinterAnnotations(AccessibleObject accessObject) {
		Vector<Annotation> result;
		if (winterAnnotationsCache.containsKey(accessObject)) {
			result = winterAnnotationsCache.get(accessObject);
		} else {
			Annotation[] annotations = accessObject.getAnnotations();
			result = new Vector<Annotation>();
			for (int i = 0; i < annotations.length; i++) {
				if ((annotations[i] instanceof winter_id) ||
						(annotations[i] instanceof winter_var) ||
						(annotations[i] instanceof winter_ref) ||
						(annotations[i] instanceof winter_predicate) ||
						(annotations[i] instanceof winter_query)) {
					result.add(annotations[i]);
				}
			}
			winterAnnotationsCache.put(accessObject, result);
		}
		return new Vector<Annotation>(result); // use a copy here!! otherwise cache could be manipulated...
	}
	
	public static Vector<Method> getWinterAnnotatedMethods(Class<?> clazz){
		
		Class<?> currentClass = clazz;
		Vector<Method> methods = new Vector<Method>();
		do {				
			Method[] currentMethods = currentClass.getDeclaredMethods();
			
			processCurrentMethods:
			for (int i = 0; i < currentMethods.length; i++) { // fields of current (super-)class
				if ( ! hasWinterAnnotation(currentMethods[i])) {
//					not winter annotated -> skip
					continue processCurrentMethods;
				}
				for (int j = 0; j < methods.size(); j++) { // fields found so far
					if ( currentMethods[i].getName().equals( methods.get(j).getName() )){
						System.out.println("skipped -> already there");
//						field with that name already found (with annotation) in subclass -> skip
						continue processCurrentMethods;
					}
				}
//				has annotation & unique name ?  -> add
				methods.add(currentMethods[i]);
			}
			
			currentClass = currentClass.getSuperclass();
		} while (includeSuperClassFields && currentClass!=null);
		
		return methods;
	}
	
	public static boolean hasWinterAnnotation(AccessibleObject accessObject){
		return !getWinterAnnotations(accessObject).isEmpty();
	}
	
	
	/**
	 * Generates a set of RDFSerializables including all referenced subclasses of type RDFSerializable.
	 * Ensures that no duplicates are in result, and circular references are resolved. 
	 * 
	 * @param obj root object, that is going to be added and analyzed on references
	 * @param present list of already added classes
	 * @return set of unique, primary/referenced RDFSerializable objects 
	 */
//	TODO IMPORTANT! Check fields 4 collections, that may contain rdfserializables and follow these leads!!
	private static Set<RDFSerializable> createObjectSetRek(RDFSerializable obj, Set<RDFSerializable> present){
		
		Set<RDFSerializable> result = new HashSet<RDFSerializable>();
		
		present.add(obj);
		
		Vector<Field> fields = ClassReflection.getFields(obj.getClass());
		
		for (Field field : fields) {
			field.setAccessible(true);
			Object memberObj = null;
			try {
				memberObj = field.get(obj);
				if (memberObj == null){
					throw new NullPointerException("Member "+ field.getName() +" was null. ignoring.");
				}
				if ( memberObj instanceof RDFSerializable && !present.contains(memberObj) ){
					present.addAll(createObjectSetRek( (RDFSerializable) memberObj, present));
				}else if ( memberObj instanceof Collection<?> ){
					for (Object element : (Iterable<?>)memberObj ) {
						if ( element instanceof RDFSerializable && !present.contains(element) ){
							present.addAll(createObjectSetRek( (RDFSerializable) element, present));
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		result.addAll(present);
		
		return result;
		
	}
	
	
	/**
	 * Generates a set of RDFSerializables including all referenced subclasses of type RDFSerializable.
	 * Ensures that no duplicates are in result, and circular references are resolved. 
	 * 
	 * @param givenSerializables root objects, that are going to be added and analyzed on references
	 * @return set of unique, primary/referenced RDFSerializable objects 
	 */
	public static Set<RDFSerializable> createObjectSet(Set<RDFSerializable> givenSerializables){
		Set<RDFSerializable> result = new HashSet<RDFSerializable>();
		
		for (RDFSerializable rdfSerialisable : givenSerializables) {
			result.addAll( createObjectSetRek(rdfSerialisable, new HashSet<RDFSerializable>()) );	
		}
		return result;
	}
	
}
