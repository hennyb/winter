package de.uniko.isweb.m3o.patterns;


import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

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
import de.uniko.isweb.m3o.implementations.EntityImpl;
import de.uniko.isweb.m3o.implementations.MethodImpl;
import de.uniko.isweb.m3o.interfaces.Entity;

import de.uniko.isweb.m3o.interfaces.Method;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;


@winter(type = winter.Type.PATTERN, 
		query = "?Description <dul:defines> ?AppliedMethodRole." +
				"?Description <dul:defines> ?MethodConcept." +
				"?AppliedMethodRole <dul:classifies> ?Method." +
				"?MethodConcept <dul:classifies> ?ProvenanceEntity." +
				"?Method <dul:hasSetting> ?Situation." +
				"?ProvenanceEntity <dul:hasSetting> ?Situation." +
				"?Situation <dul:satisfies> ?Description." +
				"?Situation <rdf:type> ?SituationType." +
				"?Description <rdf:type> ?DescriptionType." +
				"?AppliedMethodRole <rdf:type> ?AppliedMethodRoleType." +
				"?MethodConcept <rdf:type> ?MethodConceptType"
				)
public class ProvenanceInformationPattern implements RdfSerialisable{
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private IndividualURI uri = new IndividualURI();
	
	//Objects 
	@winter(type = winter.Type.EXTERNALOBJECT, var = "Method")
	Method method = null;

	@winter(type = winter.Type.EXTERNALOBJECT, var = "ProvenanceEntity")
	Entity provenanceEntity = null;
	
	//Members
	@winter(var = "Description")
	protected IndividualURI description = null;
	@winter(var = "DescriptionType")
	protected ClassURI descriptionType = null;
	@winter(var = "Situation")
	protected IndividualURI situation = null;
	@winter(var = "SituationType")
	protected ClassURI situationType = null;
	@winter(var = "AppliedMethodRole")
	protected IndividualURI appliedMethodRole = null;
	@winter(var = "AppliedMethodRoleType")
	protected ClassURI appliedMethodRoleType = null;
	@winter(var = "MethodConcept")
	protected IndividualURI methodConcept = null;
	@winter(var = "MethodConceptType")
	protected ClassURI methodConceptType = null;
	
	public ProvenanceInformationPattern(){
		this.method = new MethodImpl();
		this.provenanceEntity = new EntityImpl();
		
		this.description = new IndividualURI(Utils.createURI("", "Description"));
		this.descriptionType = new ClassURI(Utils.createURI("", "DescriptionType"));
		this.situation = new IndividualURI(Utils.createURI("", "Situation"));
		this.situationType = new ClassURI(Utils.createURI("", "SituationType"));
		this.appliedMethodRole = new IndividualURI(Utils.createURI("", "AppliedMethodRole"));
		this.appliedMethodRoleType = new ClassURI(Utils.createURI("", "AppliedMethodRoleType"));
		this.methodConcept = new IndividualURI(Utils.createURI("", "MethodConcept"));
		this.methodConceptType = new ClassURI(Utils.createURI("", "MethodConceptType"));
		validility = true;
	}
	
	/**
	 * @param method
	 * @param provenanceEntity
	 */
	public ProvenanceInformationPattern(Method method, Entity provenanceEntity) {
		this();
		if(method != null)this.method = method;
		if(provenanceEntity != null)this.provenanceEntity = provenanceEntity;
		// TODO Auto-generated constructor stub
	}
	
	public ProvenanceInformationPattern(IndividualURI description, IndividualURI situation, ClassURI descriptionType, ClassURI situationType){
		this();
		if(description != null)this.description = description;
		if(descriptionType != null)this.descriptionType = descriptionType;
		if(situation != null)this.situation = situation;
		if(situationType != null)this.situationType = this.situationType;
	}
	
	public Method getMethod() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("appliedMethodRole"), this);
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
		return method;
	}

	public void setMethod(Method method) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("method");
				if (method != null){
					Object newObj = method;
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
		this.method = method;
	}

	public Entity getProvenanceEntity() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("method"), this);
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
		return provenanceEntity;
	}

	public void setProvenanceEntity(Entity provenanceEntity) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("provenanceEntity");
				if (provenanceEntity != null){
					Object newObj = provenanceEntity;
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
		this.provenanceEntity = provenanceEntity;
	}
	
	/**
	 * @return the appliedMethodRole
	 */
	public IndividualURI getAppliedMethodRole() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("provenanceEntity"), this);
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
		return appliedMethodRole;
	}

	/**
	 * @param appliedMethodRole the appliedMethodRole to set
	 */
	public void setAppliedMethodRole(IndividualURI appliedMethodRole) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("appliedMethodRole");
				if (appliedMethodRole != null){
					Object newObj = appliedMethodRole;
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
		this.appliedMethodRole = appliedMethodRole;
	}

	/**
	 * @return the appliedMethodRoleType
	 */
	public ClassURI getAppliedMethodRoleType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("appliedMethodRoleType"), this);
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
		return appliedMethodRoleType;
	}

	/**
	 * @param appliedMethodRoleType the appliedMethodRoleType to set
	 */
	public void setAppliedMethodRoleType(ClassURI appliedMethodRoleType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("appliedMethodRoleType");
				if (appliedMethodRoleType != null){
					Object newObj = appliedMethodRoleType;
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
		this.appliedMethodRoleType = appliedMethodRoleType;
	}

	/**
	 * @return the methodConcept
	 */
	public IndividualURI getMethodConcept() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("methodConcept"), this);
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
		return methodConcept;
	}

	/**
	 * @param methodConcept the methodConcept to set
	 */
	public void setMethodConcept(IndividualURI methodConcept) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("methodConcept");
				if (methodConcept != null){
					Object newObj = methodConcept;
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
		this.methodConcept = methodConcept;
	}

	/**
	 * @return the methodConceptType
	 */
	public ClassURI getMethodConceptType() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(ProvenanceInformationPattern.class.getDeclaredField("methodConceptType"), this);
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
		return methodConceptType;
	}

	/**
	 * @param methodConceptType the methodConceptType to set
	 */
	public void setMethodConceptType(ClassURI methodConceptType) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = ProvenanceInformationPattern.class.getDeclaredField("methodConceptType");
				if (methodConceptType != null){
					Object newObj = methodConceptType;
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
		this.methodConceptType = methodConceptType;
	}

	public IndividualURI getDescription() {
		return description;
	}

	public void setDescription(IndividualURI description) {
		this.description = description;
	}

	public ClassURI getDescriptionType() {
		return descriptionType;
	}

	public void setDescriptionType(ClassURI descriptionType) {
		this.descriptionType = descriptionType;
	}

	public IndividualURI getSituation() {
		return situation;
	}

	public void setSituation(IndividualURI situation) {
		this.situation = situation;
	}

	public ClassURI getSituationType() {
		return situationType;
	}

	public void setSituationType(ClassURI situationType) {
		this.situationType = situationType;
	}

	public QueryBindingSet getBindingSet() {
		return bindingSet;
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
