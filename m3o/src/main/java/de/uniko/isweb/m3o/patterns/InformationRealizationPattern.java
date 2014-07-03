package de.uniko.isweb.m3o.patterns;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.IdentifiedByURI;
import net.java.rdf.util.WrapsURI;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;
import de.uniko.isweb.m3o.implementations.InformationObjectImpl;
import de.uniko.isweb.m3o.implementations.InformationRealizationImpl;
import de.uniko.isweb.m3o.interfaces.InformationEntity;
import de.uniko.isweb.m3o.interfaces.InformationObject;
import de.uniko.isweb.m3o.interfaces.InformationRealization;
import de.uniko.isweb.m3o.utils.IndividualURI;

@winter(type = winter.Type.PATTERN, 
		query = "?InformationObject <dul:realizes> ?InformationRealization")
public class InformationRealizationPattern implements RdfSerialisable{
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	private IndividualURI uri = null;
	
	//Objects
	@winter(type = winter.Type.EXTERNALOBJECT, var = "InformationObject")
	InformationObject informationObject = null;
	
	@winter(type = winter.Type.EXTERNALOBJECT, var = "InformationRealization")
	Set<InformationRealization> informationRealizations = new HashSet<InformationRealization>();
	
	//Constructors
	public InformationRealizationPattern(){
		this.informationObject = new InformationObjectImpl();
		validility = true;
	}
	
	public InformationRealizationPattern(InformationRealization informationRealization, InformationObject informationObject) {
		this();
		if(informationObject != null)this.informationObject = informationObject;
		this.informationRealizations.add(informationRealization);
	}
	
	public InformationRealizationPattern(Collection<InformationRealization> informationRealization, InformationObject informationObject) {
		this();
		if(informationObject != null)this.informationObject = informationObject;
		this.informationRealizations.addAll(informationRealization);
	}
	
	//Getter & Setter Methods
	public InformationObject getInformationObject() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(InformationRealizationPattern.class.getDeclaredField("informationObject"), this);
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
		this.informationObject = informationObject;
		
		return informationObject;
	}

	public void setInformationObject(InformationObject informationObject) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = InformationRealizationPattern.class.getDeclaredField("informationObject");
				if (informationObject != null){
					Object newObj = informationObject;
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
		this.informationObject = informationObject;
	}
	
	public void addInformationRealization(InformationRealization informationRealization){
		informationRealizations.add(informationRealization);
	}
	
	public Set<InformationRealization> getAllRealizations(){
		return informationRealizations;
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
