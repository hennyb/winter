package de.uniko.isweb.m3o.patterns;


// import net.java.rdf.annotations.winter;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import net.java.rdf.annotations.typeinfo;
import net.java.rdf.annotations.winter;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.IdentifiedByURI;
import net.java.rdf.util.Utils;
import net.java.rdf.util.WrapsURI;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;
import de.uniko.isweb.m3o.utils.IndividualURI;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.implementations.EntityImpl;
import de.uniko.isweb.m3o.implementations.InformationEntityImpl;
import de.uniko.isweb.m3o.interfaces.Entity;
import de.uniko.isweb.m3o.interfaces.InformationEntity;



@winter(type = winter.Type.PATTERN,
		query = "?AnnotationDescription <dul:defines> ?AnnotatedIEConcept." +
				"?AnnotationSituation <dul:satisfies>  ?AnnotationDescription." +
				"?AnnotatedIEConcept <dul:classifies> ?InformationEntity." +
				"?InformationEntity <dul:hasSetting> ?AnnotationSituation." +
				"?AnnotationSituation <rdf:type> ?AnnotationSituationType." +
				"?AnnotationDescription <rdf:type> ?AnnotationDescriptionType." +
				"?AnnotatedIEConcept <rdf:type> ?AnnotatedIEConceptType."
				
		)
public class AnnotationPattern implements RdfSerialisable{
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private IndividualURI uri = null;
	
	//Annotated Object
	@winter(type = winter.Type.EXTERNALOBJECT, var = "InformationEntity")
	InformationEntity informationEntity = null;
	
	//AnnotationObject
	@winter(type = winter.Type.MAPPING, src = {"AnnotationDescription", "AnnotationSituation"}, dst = {"AnnotationDescription", "AnnotationSituation"})
	Set<Annotation> annotations = new HashSet<Annotation>();
	
	// Optional provenance awareness information 
	@winter(type = winter.Type.MAPPING, src = {"AnnotationDescription", "AnnotationDescriptionType", "AnnotationSituation", "AnnotationSituationType"}, dst = {"Description", "DescriptionType", "Situation", "SituationType"})
	Set<ProvenanceInformationPattern> provenanceInformations = new HashSet<ProvenanceInformationPattern>();
	
	//Members
	@winter(var = "AnnotationSituation")
	protected IndividualURI annotationSituation = null;
	@winter(var = "AnnotationSituationType")
	protected ClassURI annotationSituationType = null;
	@winter(var = "AnnotationDescription")
	protected IndividualURI annotationDescription = null;
	@winter(var = "AnnotationDescriptionType")
	protected ClassURI annotationDescriptionType = null;
	@winter(var = "AnnotatedIEConcept")
	protected IndividualURI annotatedInformationEntityConcept = null;
	@winter(var = "AnnotatedIEConceptType")
	protected ClassURI annotatedInformationEntityConceptType = null;
	
	//Constructors
	public AnnotationPattern(){
		this.informationEntity = new InformationEntityImpl();
		this.annotationSituation = new IndividualURI(Utils.createURI("", "AnnotationSituation"));
		this.annotationSituationType = new ClassURI(Utils.createURI("", "AnnotationSituationType"));
		this.annotationDescription = new IndividualURI(Utils.createURI("", "AnnotationDiscription"));
		this.annotationDescriptionType = new ClassURI(Utils.createURI("", "AnnotationDiscriptionType"));
		this.annotatedInformationEntityConcept = new IndividualURI(Utils.createURI("", "AnnotatedIEConcept"));
		this.annotatedInformationEntityConceptType = new ClassURI(Utils.createURI("", "AnnotatedIEConceptType"));
		
		validility = true;
	}
	
	public AnnotationPattern(InformationEntity informationEntity, Entity annotationEntity){
		this();
		this.informationEntity = informationEntity;
		//this.annotationEntity = annotationEntity;
	}

	//Getter & Setter
	public InformationEntity getInformationEntity() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("informationEntity"), this);
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
		return informationEntity;
	}

	public void setInformationEntity(InformationEntity informationEntity) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("informationEntity");
				if (informationEntity != null){
					Object newObj = informationEntity;
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
		this.informationEntity = informationEntity;
	}
	
	/**
	 * @return the annotationSituation
	 */
	public IndividualURI getAnnotationSituation() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotationSituation"), this);
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
		return annotationSituation;
	}

	/**
	 * @param annotationSituation the annotationSituation to set
	 */
	public void setAnnotationSituation(IndividualURI annotationSituation) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotationSituation");
				if (annotationSituation != null){
					Object newObj = annotationSituation;
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
		this.annotationSituation = annotationSituation;
	}

	/**
	 * @return the annotationSituationType
	 */
	public ClassURI getAnnotationSituationType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotationSituationType"), this);
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
		return annotationSituationType;
	}

	/**
	 * @param annotationSituationType the annotationSituationType to set
	 */
	public void setAnnotationSituationType(ClassURI annotationSituationType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotationSituationType");
				if (annotationSituationType != null){
					Object newObj = annotationSituationType;
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
		this.annotationSituationType = annotationSituationType;
	}

	/**
	 * @return the annotationDescription
	 */
	public IndividualURI getAnnotationDescription() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotationDescription"), this);
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
		return annotationDescription;
	}

	/**
	 * @param annotationDescription the annotationDescription to set
	 */
	public void setAnnotationDescription(IndividualURI annotationDescription) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotationDescription");
				if (annotationDescription != null){
					Object newObj = annotationDescription;
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
		this.annotationDescription = annotationDescription;
	}

	/**
	 * @return the annotationDescriptionType
	 */
	public ClassURI getAnnotationDescriptionType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotationDescriptionType"), this);
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
		return annotationDescriptionType;
	}

	/**
	 * @param annotationDescriptionType the annotationDescriptionType to set
	 */
	public void setAnnotationDescriptionType(ClassURI annotationDescriptionType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotationDescriptionType");
				if (annotationDescriptionType != null){
					Object newObj = annotationDescriptionType;
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
		this.annotationDescriptionType = annotationDescriptionType;
	}

	/**
	 * @return the annotatedInformationEntityConcept
	 */
	public IndividualURI getAnnotatedInformationEntityConcept() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotatedInformationEntityConcept"), this);
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
		return annotatedInformationEntityConcept;
	}

	/**
	 * @param annotatedInformationEntityConcept the annotatedInformationEntityConcept to set
	 */
	public void setAnnotatedInformationEntityConcept(IndividualURI annotatedInformationEntityConcept) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotatedInformationEntityConcept");
				if (annotatedInformationEntityConcept != null){
					Object newObj = annotatedInformationEntityConcept;
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
		this.annotatedInformationEntityConcept = annotatedInformationEntityConcept;
	}

	/**
	 * @return the annotatedInformationEntityConceptType
	 */
	public ClassURI getAnnotatedInformationEntityConceptType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(AnnotationPattern.class.getDeclaredField("annotatedInformationEntityConceptType"), this);
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
		return annotatedInformationEntityConceptType;
	}

	/**
	 * @param annotatedInformationEntityConceptType the annotatedInformationEntityConceptType to set
	 */
	public void setAnnotatedInformationEntityConceptType(ClassURI annotatedInformationEntityConceptType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = AnnotationPattern.class.getDeclaredField("annotatedInformationEntityConceptType");
				if (annotatedInformationEntityConceptType != null){
					Object newObj = annotatedInformationEntityConceptType;
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
		this.annotatedInformationEntityConceptType = annotatedInformationEntityConceptType;
	}


	public Set<ProvenanceInformationPattern> getProvenanceInformations() {
		return provenanceInformations;
	}

	public void addProvenanceInformation(ProvenanceInformationPattern provenanceInformation) {
		this.provenanceInformations.add(provenanceInformation);
	}
	
	public void addAnnotation(Entity annotation, URI annotationConcept, URI annotationConceptType){
		Annotation newAnnotation = new Annotation(annotation, annotationConcept, annotationConceptType, annotationDescription, annotationSituation);
		if (newAnnotation != null)annotations.add(newAnnotation);
	}
	
	public Set<Entity> getAnnotations(){
		Set<Entity> annotationEntities = new HashSet<Entity>();	
		return annotationEntities;
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
