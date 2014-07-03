package net.java.rdf.winter;


import net.java.rdf.annotations.complex;
import net.java.rdf.annotations.typeinfo;
import net.java.rdf.annotations.winter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.URI;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import net.java.rdf.util.WinterSet;


/**
 * These are the methods required by JavassistClassRewriter
 *
 * @author Stefan Scheglmann
 */
public interface WriteMapper {
	    
		// ALL FOR MAPPING AND PATTERN -------------------------------
	

		/**
		 * Maps all information contained by the <code>west.Type.PATTERN</code> annotated object (the pattern object) to the repository. 
		 * Maps only the single <code>object</code> no recursive mapping of referenced objects is performed. 
		 * 
		 * @param objann	The annotation of the object
		 * @param object 	The <code>west.Type.PATTERN</code> annotated object (the pattern object)
		 * @return success
		 */
		public boolean setPattern(winter objann, Object object);

		/**
		 * Maps a single <code>west.Type.PATTERN</code> annotated objects referenced by a <code>west.Type.MAPPING</code> annotated field.
		 * Maps only the single <code>object</code> no recursive mapping of referenced objects is performed.
		 * 
		 * @param fieldann		The <code>west.Type.MAPPING</code> annotation of the referencing <code>field</code>
		 * @param object 		The <code>west.Type.PATTERN</code> annotated object referred by the <code>field</code>  
		 * @return success		Returns true if succeeded to map
		 */
		public boolean setMapping(winter fieldann, Object object, QueryBindingSet parentBindingSet);
		
		/**
		 * Maps exactly one <code>WinterMappingSet</code> member, a object annotated with <code>west.Type.PATTERN</code>. 
		 * Such <code>WinterMappingSet</code> is referenced by a <code>west.Type.MAPPING</code> annotated field.
		 * Maps only the single <code>object</code> no recursive mapping of referenced objects is performed.
		 * 
		 * @param fieldann		The <code>west.Type.MAPPING</code> annotation of the referencing <code>field</code>
		 * @param object		The <code>west.Type.PATTERN</code> annotated object referred by the <code>field</code>  
		 * @param parentBindingSet	The <code>BindingSet</code> of the object declaring the referencing field.
		 * @return				Returns true if succeeded to map
		 */
		public boolean setMappingSetMember(winter fieldann, Object memberObject, Object declaringObject);
		
		/**
		 * Starts a deletion of the specified <code>object</code> from the repository. If the <code>declaringObject</code> and the <code>fieldann</code> 
		 * is empty, it is assumed that we deal with a first order object of the winter object structure otherwise we assume to have a lower order object.
		 * if the <code>recursive</code> flag is set the objects and all referenced subobjects will be deleted.
		 * 
		 * @param fieldann		The annotation of the field referencing the <code>object</code>
		 * @param object		The <code>object</code> to delete
		 * @param declaringObject	The <code>object</code> declaring the field referencing the object to delete
		 * @param recursive		Trigger for recursive deletion of object and all referenced objects
		 * @return				Returns true if succeeded to map
		 */
		public boolean deleteMapping(winter fieldann, Object object, Object declaringObject, boolean recursive);
		
		/**
		 * Deletes a single <code>west.Type.PATTERN</code> annotated <code>object</code> from the repository. Such an <code>object</code> is referenced 
		 * by the field annotated with <code>fieldann</code>. Just deletes the single object, no recursive deletion for referenced objects in performed.
		 * <code>variables</code> 
		 * 
		 * @param fieldann 
		 * @param variables
		 * @param object
		 * @return
		 */
		public boolean deleteMappingInRepo(winter fieldann ,Set<String> variables, Object object);
		
		/**
		 * Replaces a single <code>object</code> anntotated with <code>winter.Type.PATTERN</code> and referenced by a field annotated with 
		 * <code>winter.Type.MAPPING</code> by <code>newObject</code> (has to be of the same type and also be annotated with <code>west.Type.Pattern</code>).
		 * 
		 * NOT YET IMPLEMENTED !!!!
		 * 
		 * @param fieldann		The annotation of the referencing field
		 * @param declaringObject		The object declaring the field referencing the <code>object</code> that should be declared
		 * @param object 		The <code>object</code> that should be replaced
		 * @param newObject		The <code>newObject</code> the <code>object</code> should be replaced with
		 * @return				Returns true if succeeded to replace
		 */
		public boolean replaceMapping(winter fieldann, Object declaringObject, Object object, Object newObject);
		
		
		// ALL FOR THE LITERALS ---------------------------------------
		
		
		/**
		 * Initially maps a single literal to the repository
		 * 
		 * @param fieldann	The annotation of type <code>winter.Type.LITERAL</code> of the field referring to the literal.
		 * @param object	The literal object to be added to the repository
		 * @return			Returns true if succeeded to map
		 */
		public boolean setLiteral(winter fieldann, Object object, String var);
		
		/**
		 * Initially maps a single <code>WinterLiteralSet</code> <code>memberObject</code> to the repository
		 * 
		 * @param fieldann		The annotation of type <code>winter.Type.Literal</code> of the field reffering to the <code>WinterLiteralSet</code>
		 * @param memberObject	The member object to be added
		 * @param declaringObject	The object declaring the field refferencing the <code>WinterLiteralSet</code>
		 * @return		Returns true if succeeded to map
		 */
		public boolean setLiteralSetMember(winter a, Object memberObject, Object declaringObject, String var);
		
		/**
		 * 
		 * 
		 * @param typeann
		 * @param parentBindingSet
		 * @param var
		 * @return
		 */
		@Deprecated
		public boolean setSetTypeInfoForLiteralInSet( typeinfo typeann, QueryBindingSet parentBindingSet, String var);
		
		/**
		 * Replaces a literal in the repository
		 * 
		 * @param fieldann		The annotation of type <code>winter.Type.LITERAL</code> of the field referring to the literal.
		 * @param declaringObject	The object declaring the literal field.
		 * @param literal		The new literal object  
		 * @return
		 */
		public boolean replaceLiteral(winter fieldann, Object declaringObject, String literal);
		
		/**
		 * Delete a specific literal from the repository
		 * 
		 * @param fieldann The annotation of type <code>winter.Type.LITERAL</code> of the field referring to the literal
		 * @param object   The object declaring the literal field
		 * @param var the 	var in fieldann //TODO: do i really need this?
		 * @param recursive indicates if also removing declaring object, then essential fields could be removed
		 * @param litToDel TODO
		 * @return
		 */
		public boolean deleteLiteral(winter fieldann, Object object, String var, boolean recursive, Object litToDel);
		

		
		//ALL FOR THE OBJECTS ------------------------------------------
		
		
		/**
		 * Initialy maps all <code>winter.class</code> annotated fields contained by an object into the repository. Only the object itself is mapped no 
		 * recursive mapping of referenced subobjects is performed.
		 * 
		 * @param annotation  the annotation of the field containing the object or the object itself if its first lvl
		 * @param object  
		 * @param declaringObjectBindingSet
		 * @param var
		 * @return
		 */
		public boolean setObject(winter annotation, Object object, QueryBindingSet declaringObjectBindingSet, String var);
		
		/**
		 * Maps a member of an <code>WinterObjectSet</code>. Maps all <code>winter.class</code> annotated fields of such an object to the repository. Only 
		 * the object itself is mapped, no recursive mapping of referenced subobjects is performed.
		 * 
		 * @param fieldann
		 * @param memberObject
		 * @param declaringObject
		 * @return
		 */
		public boolean setObjectSetMember(winter fieldann, Object memberObject, Object declaringObject, String var);
		
		/**
		 * Maps a member of an <code>WinterURISet</code>. Maps the individual URI contained by the set to a IndividualURI <rdf:Type> ClassURI statement.
		 * 
		 * @param fieldann
		 * @param memberObject
		 * @param object
		 * @return
		 */
		public boolean setURISetMember(winter fieldann, Object memberObject, Object declaringObject, String var);
		
		/**
		 * 
		 * 
		 * @param fieldann
		 * @param object
		 * @param parentBindingSet
		 * @return
		 */
		@Deprecated
		public boolean setSetTypeInfoForURIInSet(typeinfo typeann, java.net.URI uri, QueryBindingSet parentBindingSet, String var);
		
		/**
		 * @param fieldann	the annotation of the Field declaring the object
		 * @param declaringObject	the object declaring this field
		 * @param newObject		the new Object the fields member should be replaced with.
		 * @return
		 */
		public boolean replaceObject(winter fieldann, Object declaringObject, Object newObject); 
		
		/**
		 * Deletes an object from the repository
		 * 
		 * @param objann	the annotation of the Field holding the Object to be removed
		 * @param object	the object to be removed
		 * @param declaringObject	the declaringObject
		 * @param var		the var in fieldann
		 * @param recursive indicates if also removing declaring object, then essential fields could be removed
		 * @return
		 */
		public boolean deleteObject(winter objann, Object object, Object declaringObject, String var, boolean recursive);
		
		
		// ADDITIONAL HELPERS
		
		/**
		 * This method maps all fields of a given object and maps <ul>recursively</ul> maps all referenced objects to the repository. 
		 * 
		 * @param object
		 * @param readmapper
		 * @param writemapper
		 * @return
		 */
		public boolean mapTheFields(Object object, ReadMapper readmapper, WriteMapper writemapper);
		
		/**
		 * This method removes all fields of a given objects and <ul>recursively</ul> removes all referenced objects to the repository. 
		 * 
		 * @param object
		 * @return
		 */
		public boolean removeTheFields(Object object);
}

