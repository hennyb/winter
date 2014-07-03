package de.uniko.isweb.m3o.patterns;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import de.uniko.isweb.m3o.implementations.EntityImpl;
import de.uniko.isweb.m3o.interfaces.Entity;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.Utils;
import net.java.rdf.util.WrapsURI;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;

@winter(type = winter.Type.PATTERN,
		query = "?AnnotationDescription <dul:defines> ?AnnotationConcept." +
				"?AnnotationConcept <dul:classifies> ?Entity." +
				"?Entity <dul:hasSetting> ?AnnotationSituation." +
//				"?DecompositionSituation <rdf:type> ?DecompositionSituationType." +
//				"?DecompositionDescription <rdf:type> ?DecompositionDescriptionType." +
				"?AnnotationConcept <rdf:type> ?AnnotationConceptType"
		)

public class Annotation implements RdfSerialisable {

	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private IndividualURI uri = null;
	
	//Annotation
	@winter(type = winter.Type.EXTERNALOBJECT, var = "Entity")
	Entity annotationEntity = null;
	
	//Annotation Concept & Concept Type
	@winter(var = "AnnotationSituation")
	protected IndividualURI annotationSituation = null;
//	@winter(var = "AnnotationSituationType")
//	protected ClassURI annotationSituationType = null;
	@winter(var = "AnnotationDescription")
	protected IndividualURI annotationDescription = null;
//	@winter(var = "AnnotationDescriptionType")
//	protected ClassURI annotationDescriptionType = null;
	@winter(var = "AnnotationConcept")
	protected IndividualURI annotationConcept = null;
	@winter(var = "AnnotationConceptType")
	protected ClassURI annotationConceptType = null; 
	
	//Constructors
	
	public Annotation(){
		this.annotationEntity = new EntityImpl();
		this.annotationConcept = new IndividualURI(Utils.createURI("", "AnnotationConcept"));
		this.annotationConceptType = new ClassURI(Utils.createURI("", "AnnotationConceptType"));
		
		this.annotationSituation = new IndividualURI(Utils.createURI("", "AnnotationSituation"));
		this.annotationDescription = new IndividualURI(Utils.createURI("", "AnnotationDiscription"));

	}
	
	public Annotation(Entity annotationEntity){
		this();
		if(annotationEntity != null)this.annotationEntity = annotationEntity;
	}
	
	public Annotation(Entity annotationEntity, URI annotationConcept){
		this(annotationEntity);
		if(annotationConcept != null)this.annotationConcept = new IndividualURI(annotationConcept);
	}
	
	public Annotation(Entity annotationEntity, URI annotationConcept, URI annotationConceptType){
		this(annotationEntity, annotationConcept);
		if(annotationConceptType != null)this.annotationConceptType = new ClassURI(annotationConceptType);
	}
	
	public Annotation(Entity annotationEntity, URI annotationConcept, URI annotationConceptType, IndividualURI annotationDescription, IndividualURI annotationSituation){
		this(annotationEntity, annotationConcept, annotationConceptType);
		if(annotationDescription != null)this.annotationDescription = annotationDescription;
		if(annotationSituation != null)this.annotationSituation = annotationSituation;
	}
	
	//Getter/Setters Methods

	public Entity getAnnotationEntity() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(Annotation.class.getDeclaredField("annotationEntity"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotationEntity;
	}

	public void setAnnotationEntity(Entity annotationEntity) {
		if(writemapper != null){
			try {
				Field field = Annotation.class.getDeclaredField("annotationEntity");
				if (annotationEntity != null){
					Object newObj = annotationEntity;
					writemapper.replaceObject(field.getAnnotation(winter.class), this, newObj);
					}else{
						writemapper.deleteObject(field.getAnnotation(winter.class), null, this, field.getAnnotation(winter.class).var(), false);
					}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.annotationEntity = annotationEntity;
	}
	

	/**
	 * @return the annotationConcept
	 */
	public IndividualURI getAnnotationConcept() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(Annotation.class.getDeclaredField("annotationConcept"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotationConcept;
	}

	/**
	 * @param annotationConcept the annotationConcept to set
	 */
	public void setAnnotationConcept(IndividualURI annotationConcept) {
		if(writemapper != null){
			try {
				Field field = Annotation.class.getDeclaredField("annotationConcept");
				if (annotationConcept != null){
					Object newObj = annotationConcept;
					writemapper.replaceObject(field.getAnnotation(winter.class), this, newObj);
					}else{
						writemapper.deleteObject(field.getAnnotation(winter.class), null, this, field.getAnnotation(winter.class).var(), false);
					}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.annotationConcept = annotationConcept;
	}
	
	/**
	 * @return the annotationConceptType
	 */
	public ClassURI getAnnotationConceptType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(Annotation.class.getDeclaredField("annotationConceptType"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return annotationConceptType;
	}

	/**
	 * @param annotationConceptType the annotationConceptType to set
	 */
	public void setAnnotationConceptType(ClassURI annotationConceptType) {
		if(writemapper != null){
			try {
				Field field = Annotation.class.getDeclaredField("annotationConceptType");
				if (annotationConceptType != null){
					Object newObj = annotationConceptType;
					writemapper.replaceObject(field.getAnnotation(winter.class), this, newObj);
					}else{
						writemapper.deleteObject(field.getAnnotation(winter.class), null, this, field.getAnnotation(winter.class).var(), false);
					}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.annotationConceptType = annotationConceptType;
	}
	
	// Utility Methods
	public QueryBindingSet getBindingSet() {
		return bindingSet;
	}

	public HashMap<String, URI> getVarMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBindingSet(QueryBindingSet bindingSet) {
		this.bindingSet = bindingSet;
	}

	public void setSesameReadMapper(SesameReadMapper readmapper) {
		this.readmapper = readmapper;
	}

	public void setSesameWriteMapper(SesameWriteMapper writemapper) {
		this.writemapper = writemapper;
	}
	
	public SesameReadMapper getSesameReadmapper() {
		return readmapper;
	}

	public SesameWriteMapper getSesameWriteMapper() {
		return writemapper;
	}
	
	public boolean isValid() {
		return validility;
	}

	public void setValidility(boolean flag) {
		this.validility = flag;
	}

	public WrapsURI getURI() {
		return uri;
	}

	public void setURI(URI uri) {
		this.uri.setURI(uri); 	
	}

}
