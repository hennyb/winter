package de.uniko.isweb.m3o.implementations;


import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.Utils;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import de.uniko.isweb.m3o.interfaces.Region;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;
import net.java.rdf.annotations.winter;


public class RegionImpl implements Region, RdfSerialisable {
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	
	//Members
	@winter(var = "Region")
	IndividualURI uri = null;
	@winter(var = "RegionType")
	ClassURI concept = null;

	public RegionImpl() {
		this.uri = new IndividualURI(Utils.createURI("", "Region"));
		this.concept = new ClassURI(Utils.createURI("", "RegionType"));
		validility = true;
	}

	/**
	 * @param uri
	 */
	public RegionImpl(IndividualURI uri) {
		this();
		if(uri != null)this.uri = uri;
	}

	/**
	 * @param concept
	 */
	public RegionImpl(ClassURI concept) {
		this();
		if(concept != null)this.concept = concept;
	}
	
	/**
	 * @param uri
	 * @param concept
	 */
	public RegionImpl(IndividualURI uri, ClassURI concept) {
		this();
		if(uri != null)this.uri = uri;
		if(concept != null)this.concept = concept;
	}
	
	/* (non-Javadoc)
	 * @see de.uniko.isweb.m3o.interfaces.IdentifiedByURI#getURI()
	 */
	public IndividualURI getURI() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(RegionImpl.class.getDeclaredField("uri"), this);
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
		return uri;
	}
	
	public void setURI(IndividualURI uri) {
		if(validility){
			try {
				Field field = RegionImpl.class.getDeclaredField("uri");
				if (uri != null){
					Object newObj = uri;
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
		this.uri = uri;
	}
	
	/* (non-Javadoc)
	 * @see de.uniko.isweb.m3o.interfaces.HasConcept#getConcept()
	 */
	public ClassURI getConcept() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(RegionImpl.class.getDeclaredField("concept"), this);
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
		return concept;
	}

	public void setConcept(URI concept){
		setConcept(new ClassURI(concept));
	}
	
	/**
	 * @param concept
	 */
	public void setConcept(ClassURI concept) {
		if(validility){
			try {
				Field field = RegionImpl.class.getDeclaredField("concept");
				if (concept != null){
					Object newObj = concept;
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
		this.concept = concept;
	}
	
	public void setURI(URI uri){
		setURI(new IndividualURI(uri));
	}
	

	
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
}
