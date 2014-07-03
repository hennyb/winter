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

package de.uniko.west.winter.preprocess;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import de.uniko.west.winter.annotation.winter_ref;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.exceptions.WinterAnnotationException;
import de.uniko.west.winter.utils.ClassReflection;

/**
 * @author Stefan Scheglmann, Frederik Jochum {
 *
 */
public class AnnotationValidation {
	//TODO
	
//	
//	
//	/**
//	 *	checks, if variables occuring in a @winter_query anntoted class are
//	 *	existent in @winter_var or @winter_ref annotated fields 
//	 */
//	private static boolean checkMissingVars = true;
//
//	
//	/**
//	 * checks if one field contains both annotations (@winter_var and @winter_ref).
//	 * 
//	 * compares @winter_var and @winter_ref annotated entries for duplicates.
//	 * 
//	 * also performs a check in "hybrid" classes (content and structure):
//	 * when no @winter_var annotation is given, winter will use fieldnames for queries.
//	 * check will fail, when these collide with given vars / refs.
//	 */
//	private static boolean checkDuplicateVars = true;
//	
//	/**
//	 * currently checked:
//	 * [x] @winter_ref annotated fields must be de.uniko.west.winter.core.interfaces.RDFSerializable or collections containing them
//	 * [x] all annotated java.util.Collections must be java.util.Sets (if ignored, elements can be lost)
//	 * [ ] @winter_id annotated fields must be java.net.URI
//	 */
//	private static boolean checkAnnotatedTypes = true;
//	
//	
//	public static void validate(ObjectNode on){
//		
//		
//	}
//	
//
//	public static boolean validateREFexlusiveVAR(ObjectNode on){
//		boolean checkFailed = false;
//		List<InfoNode> children = on.getChildren();
//		FieldNode fieldNode = null;
//		for (InfoNode infoNode : children) {
//			try{
//				fieldNode = (FieldNode) infoNode;
//				if ( fieldNode.getField().getAnnotation(winter_var.class) != null && 
//						fieldNode.getField().getAnnotation(winter_ref.class) != null	){
//						throw new WinterAnnotationException("Illegal annotations on field: " +
//								fieldNode + ". Field has @winter_ref and @winter_var annotation.");
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//				checkFailed = true;
//			}
//			
//		}
//		return checkFailed;
//	}
//	
//	
//	
//	public static void validateExlusiveVARNames(ObjectNode on) {
//		Vector<String> allVars = new Vector<String>();
//
//		boolean checkFailed = false;
//		
//		Vector<Field> children = ClassReflection.getFields(on.getObject().getClass());
//		winter_var varAnno = null;
//		winter_ref refAnno = null;
//		for (Field field : children) {
//			if ( (varAnno=field.getAnnotation(winter_var.class)) != null){
//				if ( !isAnnotationValueEmpty(refAnno) ){
//					allVars.addAll( Arrays.asList(refAnno.value()) );
//				}
//				if ( !isAnnotationValueEmpty(varAnno) ){
//					allVars.add( varAnno.value() );
//				}else{
////					TODO
//				}
//				
//			}
//			
//		} 
//	}
//
//	private static boolean isAnnotationValueEmpty(Annotation annotation){
//		try {
//			Object val = annotation.getClass().getDeclaredMethod("value", new Class[]{}).invoke(annotation, null);
//			if (val==null){
//				return true;
//			}
//			if (val instanceof String && ((String)val).equals("") ){
//				return true;
//			}
//			if (val instanceof String[]){
//				for (String item: (String[])val) {
//					if (item==""){
//						return true;
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return true;
//		}
//		return false;
//	}
//	
//	public static void validateExlusiveREFNames(ObjectNode on){
////		Hashtable<String, FieldNode> refs = on.getRefName2Info();
////		for (String refKey: refs.keySet()) {
////			if (on.getVarNames2Info().containsKey(refKey)){
////				throw new WinterAnnotationException("Illegal annotations on field: " + 
////						refs.get(refKey) + ". Duplicate usage of var name '" + refKey + "'." );
////			}
////		}
//	}
//	
//	public static void validateREFAnnotatesRDFSerializableOnly(ObjectNode on){
//		
//	}
//	
//	public static void validateQUERYVarExistence(ObjectNode on){
//		
//	}
//	
	

}
