package de.uniko.isweb.m3o.patterns;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.uniko.isweb.m3o.implementations.InformationEntityImpl;
import de.uniko.isweb.m3o.interfaces.Entity;
import de.uniko.isweb.m3o.interfaces.InformationEntity;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;

@winter(type = winter.Type.PATTERN,
		query = "?DecompositionDescription <dul:defines> ?CompositeConcept." +
				"?DecompositionSituation <dul:satisfies> ?DecompositionDescription." +
				"?CompositeConcept <dul:classifies> ?Composite." +
				"?Composite <dul:hasSetting> ?DecompositionSituation." +
				"?DecompositionSituation <rdf:type> ?DecompositionSituationType." +
				"?DecompositionDescription <rdf:type> ?DecompositionDescriptionType." +
				"?CompositeConcept <rdf:type> ?CompositeConceptType."
		)
public class DecompositionPattern implements RdfSerialisable{
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private WrapsURI uri = null;
	
	//Composite
	@winter(type=winter.Type.EXTERNALOBJECT, var = "Composite")
	InformationEntity composite = null;

	//Components
	@winter(type=winter.Type.MAPPING, src = {"DecompositionDescription", "DecompositionSituation"}, dst = {"DecompositionDescription", "DecompositionSituation"})
	Set<Component> components = new HashSet<Component>();
	
	// Optional provenance awareness information
	@winter(type=winter.Type.MAPPING, src = {"DecompositionDescription", "DecompositionSituation"}, dst = {"Description", "Situation"})
	Set<ProvenanceInformationPattern> provenanceInformations = new HashSet<ProvenanceInformationPattern>();
	
	public Set<ProvenanceInformationPattern> getProvenanceInformations() {
		return provenanceInformations;
	}

	public void addProvenanceInformation(ProvenanceInformationPattern provenanceInformation) {
		this.provenanceInformations.add(provenanceInformation);
	}

	
	//Members
	@winter(var = "DecompositionSituation")
	protected IndividualURI decompositionSituation = null;
	@winter(var = "DecompositionSituationType")
	protected ClassURI decompositionSituationType = null;
	@winter(var = "DecompositionDescription")
	protected IndividualURI decompositionDescription = null;
	@winter(var = "DecompositionDescriptionType")
	protected ClassURI decompositionDescriptionType = null;
	@winter(var = "CompositeConcept")
	protected IndividualURI compositeConcept = null;
	@winter(var = "CompositeConceptType")
	protected ClassURI compositeConceptType = null;
	
	public DecompositionPattern() {
		this.composite = new InformationEntityImpl();
		this.compositeConcept = new IndividualURI(Utils.createURI("", "CompositeConcept"));
		this.compositeConceptType = new ClassURI(Utils.createURI("", "CompositeConceptType"));
		this.decompositionDescription = new IndividualURI(Utils.createURI("", "DecompositionDescription"));
		this.decompositionDescriptionType = new ClassURI(Utils.createURI("", "DecompositionDescriptionType"));
		this.decompositionSituation = new IndividualURI(Utils.createURI("", "DecompositionSituation"));
		this.decompositionSituationType = new ClassURI(Utils.createURI("", "DecompositionSituationType"));
		validility = true;
	}
	
	/**
	 * @param composite
	 * @param component
	 */
	public DecompositionPattern(InformationEntity composite, InformationEntity component){
		this();
		if(composite != null)this.composite = composite;
		this.components.add(new Component(component));
	}

	/**
	 * @param composite
	 * @param component
	 */
	public DecompositionPattern(InformationEntity composite, Collection<InformationEntity> components){
		this();
		if(composite != null)this.composite = composite;
		for (Iterator<InformationEntity> it = components.iterator(); it.hasNext(); ){
			this.components.add(new Component(it.next()));
		}
	}
	//Getter & Setter Methods
	
	public void setComposite(InformationEntity composite){
		if(writemapper != null){
			//if(validility){
				try {
					Field field = DecompositionPattern.class.getDeclaredField("composite");
					if (composite != null){
						Object newObj = composite;
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
			this.composite = composite;
	}
	
	public InformationEntity getComposite(){
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DecompositionPattern.class.getDeclaredField("composite"), this);
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
		return composite;
	}
	
	/**
	 * @return the decompositionSituation
	 */
	public IndividualURI getDecompositionSituation() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DecompositionPattern.class.getDeclaredField("decompositionSituation"), this);
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
		return decompositionSituation;
	}

	/**
	 * @param decompositionSituation the decompositionSituation to set
	 */
	public void setDecompositionSituation(IndividualURI decompositionSituation) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("decompositionSituation");
				if (decompositionSituation != null){
					Object newObj = decompositionSituation;
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
		this.decompositionSituation = decompositionSituation;
	}

	/**
	 * @return the decompositionSituationType
	 */
	public ClassURI getDecompositionSituationType() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("decompositionSituationType"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decompositionSituationType;
	}

	/**
	 * @param decompositionSituationType the decompositionSituationType to set
	 */
	public void setDecompositionSituationType(ClassURI decompositionSituationType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("decompositionSituationType");
				if (decompositionSituationType != null){
					Object newObj = decompositionSituation;
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
		this.decompositionSituationType = decompositionSituationType;
	}

	/**
	 * @return the decompositionDescription
	 */
	public IndividualURI getDecompositionDescription() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DecompositionPattern.class.getDeclaredField("decompositionDescription"), this);
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
		return decompositionDescription;
	}

	/**
	 * @param decompositionDescription the decompositionDescription to set
	 */
	public void setDecompositionDescription(IndividualURI decompositionDescription) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("decompositionDescription");
				if (decompositionDescription != null){
					Object newObj = decompositionDescription.toString();
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
		this.decompositionDescription = decompositionDescription;
	}

	/**
	 * @return the decompositionDescriptionType
	 */
	public ClassURI getDecompositionDescriptionType() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("decompositionDescriptionType"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decompositionDescriptionType;
	}

	/**
	 * @param decompositionDescriptionType the decompositionDescriptionType to set
	 */
	public void setDecompositionDescriptionType(ClassURI decompositionDescriptionType) {
		if(writemapper != null){
		//if(validility){
			try{
				Field field = DecompositionPattern.class.getDeclaredField("decompositiondescriptionType");
				if (decompositionDescriptionType != null){
					Object newObj = decompositionDescriptionType;
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
		this.decompositionDescriptionType = decompositionDescriptionType;
	}

	/**
	 * @return the compositeConcept
	 */
	public IndividualURI getCompositeConcept() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("CompositeConcept"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compositeConcept;
	}

	/**
	 * @param compositeConcept the compositeConcept to set
	 */
	public void setCompositeConcept(IndividualURI compositeConcept) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("compositeConcept");
				if (compositeConcept != null){
					Object newObj = compositeConcept;
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
		this.compositeConcept = compositeConcept;
	}

	/**
	 * @return the compositeConceptType
	 */
	public ClassURI getCompositeConceptType() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("compositeConceptType"), this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compositeConceptType;
	}

	/**
	 * @param compositeConceptType the compositeConceptType to set
	 */
	public void setCompositeConceptType(ClassURI compositeConceptType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("compositeConceptType");
				if (compositeConceptType != null){
					Object newObj = compositeConceptType;
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
		this.compositeConceptType = compositeConceptType;
	}

	public void addComponent(InformationEntity component, URI componentConcept, URI componentConceptType){
		Component newComponent = new Component(component, componentConcept, componentConceptType, decompositionDescription, decompositionSituation);
		if (newComponent != null)components.add(newComponent);
	}
	
	public Set<InformationEntity> getComponents(){
		Set<InformationEntity> componentEntities = new HashSet<InformationEntity>();
		return componentEntities;
	}
	
	//Utility Methods
	public QueryBindingSet getBindingSet() {
		return this.bindingSet;
	}

	public SesameReadMapper getSesameReadmapper() {
		return this.readmapper;
	}

	public SesameWriteMapper getSesameWriteMapper() {
		return this.writemapper;
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
