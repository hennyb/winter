package de.uniko.isweb.m3o.patterns;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import de.uniko.isweb.m3o.implementations.InformationEntityImpl;
import de.uniko.isweb.m3o.interfaces.Entity;
import de.uniko.isweb.m3o.interfaces.InformationEntity;
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
		query = "?DecompositionDescription <dul:defines> ?ComponentConcept." +
				"?ComponentConcept <dul:classifies> ?Component." +
				"?Component <dul:hasSetting> ?DecompositionSituation." +
//				"?DecompositionSituation <rdf:type> ?DecompositionSituationType." +
//				"?DecompositionDescription <rdf:type> ?DecompositionDescriptionType." +
				"?ComponentConcept <rdf:type> ?ComponentConceptType"
		)
public class Component implements RdfSerialisable {

	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private IndividualURI uri = null;
	
	//Component
	@winter(type=winter.Type.EXTERNALOBJECT, var = "Component")
	InformationEntity component = null;
	
	//Component Concept & ConceptType
	@winter(var = "DecompositionSituation")
	protected IndividualURI decompositionSituation = null;
//	@winter(var = "DecompositionSituationType")
//	protected ClassURI decompositionSituationType = null;
	@winter(var = "DecompositionDescription")
	protected IndividualURI decompositionDescription = null;
//	@winter(var = "DecompositionDescriptionType")
//	protected ClassURI decompositionDescriptionType = null;
	@winter(var = "ComponentConcept")
	protected IndividualURI componentConcept = null;
	@winter(var = "ComponentConceptType")
	protected ClassURI componentConceptType = null;
	
	//Constructors
	public Component(){
		this.component = new InformationEntityImpl();
		this.componentConcept = new IndividualURI(Utils.createURI("", "ComponentConcept"));
		this.componentConceptType = new ClassURI(Utils.createURI("", "ComponentConceptType"));
		
		this.decompositionDescription = new IndividualURI(Utils.createURI("", "DecompositionDescription"));
//		this.decompositionDescriptionType = new ClassURI(Utils.createURI("", "DecompositionDescriptionType"));
		this.decompositionSituation = new IndividualURI(Utils.createURI("", "DecompositionSituation"));
//		this.decompositionSituationType = new ClassURI(Utils.createURI("", "DecompositionSituationType"));
	}
	
	public Component(InformationEntity component){
		this();
		if(component != null)this.component = component;
	}
	
	public Component(InformationEntity component, URI componentConcept){
		this(component);
		if(componentConcept != null)this.componentConcept = new IndividualURI(componentConcept);
	}
	
	public Component(InformationEntity component, URI componentConcept, URI componentConceptType){
		this(component, componentConcept);
		if(componentConceptType != null)this.componentConceptType = new ClassURI(componentConceptType);
	}
	
	public Component(InformationEntity component, URI componentConcept, URI componentConceptType, IndividualURI decompositionDescription, IndividualURI decompositionSituation){
		this(component, componentConcept, componentConceptType);
		if(decompositionDescription != null) this.decompositionDescription = decompositionDescription;
		if(decompositionSituation != null) this.decompositionSituation = decompositionSituation;
	}
	
	//Getter & Setter
	public InformationEntity getComponent() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(Component.class.getDeclaredField("component"), this);
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
		return component;
	}

	public void setComponent(InformationEntity component) {
		if(writemapper != null){
			try {
				Field field = Component.class.getDeclaredField("component");
				if (component != null){
					Object newObj = component;
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
		this.component = component;
	}
	
	/**
	 * @return the componentConcept
	 */
	public IndividualURI getComponentConcept() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("componentConcept"), this);
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
		return componentConcept;
	}

	/**
	 * @param componentConcept the componentConcept to set
	 */
	public void setComponentConcept(IndividualURI componentConcept) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("componentConcept");
				if (componentConcept != null){
					Object newObj = componentConcept;
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
		this.componentConcept = componentConcept;
	}

	/**
	 * @return the componentConceptType
	 */
	public ClassURI getComponentConceptType() {
		try {
			if(readmapper != null)readmapper.updateURIField(DecompositionPattern.class.getDeclaredField("componentConceptType"), this);
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
		return componentConceptType;
	}

	/**
	 * @param componentConceptType the componentConceptType to set
	 */
	public void setComponentConceptType(ClassURI componentConceptType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DecompositionPattern.class.getDeclaredField("componentConceptType");
				if (componentConceptType != null){
					Object newObj = componentConceptType;
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
		this.componentConceptType = componentConceptType;
	}
	
	//Utility Methods
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
