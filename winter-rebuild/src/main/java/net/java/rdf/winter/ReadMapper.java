package net.java.rdf.winter;

import net.java.rdf.annotations.winter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;

import net.java.rdf.util.WinterMappingSet;
import net.java.rdf.util.EntityIncompleteException;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.MalformedAnnotationException;


/**
 * These are the methods required by JavassistClassRewriter
 *
 * @author Stefan Scheglmann
 */
public interface ReadMapper {
	
	/**
	 * This method checks if an specified object is mapped to the repository
	 * 
	 * @param objann 			The annotation of the Object
	 * @param object 			The object itself
	 * @return
	 */
	public boolean isMapped(winter objann, Object object);
	
//---------------------------------------- The Update Methods ---------------------------------------------
	
	/**
	 * This method updates a <code>WinterSet</code> of <code>winter.Type.LITERAL</code> field. 
	 * It replaces the current <code>WinterSet</code> by a new one retrieved from the repository
	 * 
	 * @param field
	 * @param declaringObject
	 * @throws FieldMemberTypeException
	 * @throws FieldNotMappedException
	 */
	public void updateLITERALSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;
	
    /**
     * This method updates a <code>winter.Type.LITERAL</code> field. 
     * It replaces the current value through the one retrieved from the repository
     *
     * @param fieldMemberann	the winter annotation of the object declaring this field
     * @param fieldann   		the winter annotation on the field
     * @param field   			the Object inhabiting this field
     * @return TODO
     */
    public Object updateLITERALField(Field field, Object declaringObject) throws FieldMemberTypeException;  
    
    /**
     * This method updates a <code>WinterSet</code> of <code>winter.Type.INTERNALOBJECT</code>. 
     * It replaces the current <code>WinterSet</code> through the one retrieved from the repository
     * 
     * @param field
     * @param declaringObject
     * @throws FieldMemberTypeException
     * @throws FieldNotMappedException
     */
    public void updateURISetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;
    
    /**
     * This method updates a <code>winter.Type.INTERNALOBJECT</code> field. 
     * It replaces the current <code>java.net.URI</code> through the one retrieved from the repository
     *
     * @param fieldMemberann	the winter annotation of the object declaring this field
     * @param fieldann   		the winter annotation on the field
     * @param field   			the Object inhabiting this field
     * @return TODO
     */
    public java.net.URI updateURIField(Field field, Object declaringObject)throws FieldMemberTypeException;  
    
    /**
     * This method updates a <code>WinterSet</code> of <code>winter.Type.EXTERNALOBJECT</code> field.
     * It replaces the current <code>WinterSet</code> through the one retrieved from the repository
     * 
     * @param field
     * @param declaringObject
     * @throws FieldMemberTypeException
     * @throws FieldNotMappedException
     */
    public void updateOBJECTSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;
    
    /**
     * This method updates a <code>winter.Type.EXTERNALOBJECT</code> field.
     * It replaces the current object through the one retrieved from the repository.
     *
     * @param fieldMemberann	the winter annotation of the object declaring this field
     * @param fieldann   		the winter annotation on the field
     * @param field   			the Object inhabiting this field
     * @return TODO
     */
    public Object updateOBJECTField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;  
    
    /**
     * This method updates a <code>WinterSet</code> of <code>winter.Type.PATTERN</code> referred from a <code>winter.Type.PATTERN</code> field.
     * It replaces the current <code>WinterSet</code> through the one retrieved from the repository
     * 
     * @param field
     * @param declaringObject
     * @throws FieldMemberTypeException
     * @throws FieldNotMappedException
     */
    public void updateMAPPINGSetField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;
    
    /**
     * This method updates a <code>winter.Type.MAPPING</code> field.
     * It replaces the current <code>winter.Type.PATTERN</code> object through the one retrieved from the repository.
     *
     * @param fieldMemberann	the winter annotation of the object declaring this field
     * @param fieldann   		the winter annotation on the field
     * @param field   			the Object inhabiting this field
     * @return TODO
     */
    public Object updateMAPPINGField(Field field, Object declaringObject) throws FieldMemberTypeException, FieldNotMappedException;   
	
	/**
	 * Updates a whole object. The method will iterate over all <code>winter</code> annotated fields in the object and updates all field values according
	 * with the values in the repository.
	 * 
	 * @param object					The object to be updated.
	 */
	public void updateObject(Object object);

//---------------------------------------- The Getter Methods ---------------------------------------------	
	
    /**
     * This method returns a <code>Set</code> of all <code>winter.Type.Pattern</code> annotated objects (pattern) for a specified <code>WinterMappingSet</code>
     * field in a specific object from the repository
     * 
     * @param fieldann		The annotation of the <code>WinterMappingSet</code> field
     * @param declaringObjectBindingSet		The <code>BindingSet</code> of the object declaring the <code>WinterMappingSet</code> field
     * @param memberObject		The <code>WinterMappingSet</code> object
     * @return		A <code>Set</code> of <code>winter.Type.PATTERN</code> annotated objects
     * @throws MalformedQueryException
     * @throws EntityIncompleteException
     * @throws MalformedAnnotationException
     */
    public Set<Object> getPATTERNs(winter fieldann, BindingSet declaringObjectBindingSet, WinterMappingSet memberObject) throws EntityIncompleteException, MalformedAnnotationException;
	
    /**
     * This method returns a single <code>winter.Type.PATTERN</code> annotated object for a <winter.Type.MAPPING> annotated field 
     * 
	 * @param fieldann				The annotation of the field
	 * @param declaringObjectBindingSet		The <code>BindingSet</code> of the object declaring the field
	 * @param object						
	 * @return								A <code>winter.Type.PATTERN</code> annotated object
	 * @throws MalformedAnnotationException
	 * @throws EntityIncompleteException
	 */
    public Object getPATTERN(winter fieldann, BindingSet declaringObjectBindingSet, Object object) throws MalformedAnnotationException, EntityIncompleteException;
    
	/**
	 * This method returns a <code>Set</code> of all <code>winter.Type.PATTERN</code> annotated objects (pattern) of a specified type (a pattern class) 
	 * referring to a particular <code>winter.Type.EXTERNALOBJECT</code> object from the repository
	 * 
	 * @param patternType			The pattern class (type of the pattern)
	 * @param object			The object for which the patterns should be retrieved
	 * @return					A <code>Set</code> of <code>winter.Type.PATTERN</code> annotated objects of the defined type, referring to the defined obejct
	 */
	public Set<Object> getPATTERN(Class patternType, Object object);
	
	/**
	 * This method returns a <code>Set</code> of all <code>winter.Type.PATTERN</code> annotated objects (pattern) of th specified of <code>patternType</code> 
	 * from the repository
	 * 
	 * @param patternType 			The pattern class (type of the pattern)
	 * @return					A <code>Set</code> of <code>winter.Type.PATTERN</code> annotated objects of the defined type
	 */
	public Set<Object> getPATTERN(Class patternType) throws MalformedAnnotationException;
	
	/**
	 * This method returns a specific <code>winter.Type.PATTERN</code> annotated object of <code>patternType</code> in which 
	 * the bindings from the <code>bindingMap</code> are involved
	 * 
	 * @param patternType 			The pattern class (type of the pattern)
	 * @param bindingMap			A <code>java.util.HashMap</code> containing variable/<code>java.net.URI</code> pairs representing bindings
	 * to build up a <code>BindingSet</code>
	 * @return						A single object of type <code>patternType</code> 
	 */
	public Set<Object> getPATTERN(Class patternType, Map<String, java.net.URI> bindingMap);
	
	/**
	 * This method returns a specific <code>winter.Type.PATTERN</code> annotated object of <code>patternType</code> in which 
	 * the variable <code>var</code> is binded to the specific <code>java.net.URI individualURI</code>
	 * 
	 * @param patternType			The pattern class (type of the pattern)
	 * @param individualURI			The <java.net.URI> individualURI 
	 * @param var					The variable name the <code>individualURI</code> should be bound to
	 * @return						A single object of type <code>patternType</code> 
	 */
	public Set<Object> getPATTERN(Class patternType, java.net.URI individualURI, String var);
	
	/**
	 * This method returns all IndividualURIs from the repository for a specific <code>WinterSet</code> of <code>java.net.URI</code> 
	 * 
	 * @param fieldann 					The annotation of the <code>WinterSet</code> field
	 * @param declaringObjectann 		The annotation of the object declaring the <code>WinterSet</code>
	 * @param declaringObjectBindingSet The BindingSet of the object declaring the <code>WinterSet</code>
	 * @return							A Set of <code>java.net.URI</code> (IndividualURIs)
	 * @throws FieldNotMappedException
	 */
	public Set<java.net.URI> getURIs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException;
	
	/**
	 * This method returns all <code>Literals</code> for a <code>WinterSet</code> from the repository
	 * 
	 * @param fieldann 					The annotation of the <code>WinterSet</code> field
	 * @param declaringObjectann 		The annotation of the object declaring the <code>WinterSet</code>
	 * @param declaringObjectBindingSet The BindingSet of the object declaring the <code>WinterSet</code>
	 * @return							A Set of <code>Literals</code>
	 * @throws FieldNotMappedException
	 */
	public Set<String> getLITERALs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException;
	
	/**
	 * This method returns a Set of IndividualURIs from the repository for a specific <code>WinterSet</code> field of objects annotated with <code>winter.Type.EXTERNALOBJECT</code>
	 * 
	 * @param fieldann 					The annotation of the WinterSet Field
	 * @param declaringObjectann 		The annotation of the object declaring the <code>WinterSet</code> field
	 * @param declaringObjectBindingSet The BindingSet of the object declaring the <code>WinterSet</code>field
	 * @return							A Set of <code>IndividualURIs</code>
	 * @throws FieldNotMappedException
	 */
	public Set<java.net.URI> getOBJECTs(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException;
	
	/**
	 * This method returns a Literal for a specific <code>winter.Type.LITERAL</code> annotated field, in a specific object from the repository 
	 * 
	 * @param fieldann					The annotation of the field
	 * @param declaringObjectann		The annotation of the object declaring the field
	 * @param declaringObjectBindingSet	The <code>BindingSet</code> of the object declaring the field
	 * 
	 * @return							The Literal to be returned
	 */
	public String getLITERAL(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet);
	
	/**
	 * This method returns a <java.net.URI> from a <code>winter.Type.INTERNALOBJECT</code> annotated field, in a specific object from the repository
	 * 
	 * @param fieldann					The annotation of the field 
	 * @param declaringObjectann		The annotation of the object declaring the field
	 * @param declaringObjectBindingSet	The <code>BindingSet</code> of the object declaring the field
	 * @return							The <code>java.net.URI</code> (individualURI) of the <code>winter.Type.INTERNALOBJECT</code>
	 * @throws FieldNotMappedException
	 */
	public java.net.URI getINTERNALOBJECT(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException;

	/**
	 * This method returns the URI from the repository of a specific object referenced from a specific field from a specific declaring objects.
	 * 
	 * @param fieldann					The annotation of the field of the object declaring the referencing field
	 * @param declaringObjectann		The annotation of the object declaring the referencing field
	 * @param declaringObjectBindingSet	The <code>BindingSet</code> of the object declaring the referencing field
	 * @return							The <code>java.net.URI</code> of the referenced Object
	 * @throws FieldNotMappedException
	 */
	public java.net.URI getEXTERNALOBJECT(winter fieldann, winter declaringObjectann, BindingSet declaringObjectBindingSet) throws FieldNotMappedException;
	
	/**
	 * This method returns the type (ConceptURI) related by <rdf:Type> in the repository for a given individual (IndividualURI)
	 * 
	 * @param individualURI 			The <code>java.net.URI</code> (IndividualURI) to get the type declaration for
	 * @return conceptURI				The type <code>java.net.URI</code> of the Individual <code>java.net.URI</code>
	 */
	public java.net.URI getConceptURIForIndividualURI(java.net.URI individualURI); 
	
	/**
	 * this method returns all individuals (IndividualURIs) for a given type (ConceptURI) from the repository
	 * 
	 * @param conceptURI				The <code>java.net.URI</code> (ConceptURI) to get the individuals for 
	 * @return							A Set of individuals represented through their <code>java.net.URI</code>
	 */
	public Set<java.net.URI> getAllIndividualURIsForConcept(java.net.URI conceptURI); 
}
