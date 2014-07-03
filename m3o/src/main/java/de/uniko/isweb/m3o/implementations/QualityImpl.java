package de.uniko.isweb.m3o.implementations;


import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;

import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.Utils;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;
import de.uniko.isweb.m3o.interfaces.Quality;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;
import net.java.rdf.annotations.winter;


public class QualityImpl implements Quality, RdfSerialisable {
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	
	@winter(var = "Quality")
	IndividualURI uri = null;
	@winter(var = "QualityType")
	ClassURI concept = null;
	
	public QualityImpl() {
		this.uri = new IndividualURI(Utils.createURI("", "Quality"));
		this.concept = new ClassURI(Utils.createURI("", "QualityType"));
		validility = true;
	}
	
	/**
	 * @param uri
	 */
	public QualityImpl(IndividualURI uri) {
		this();
		if(uri != null)this.uri = uri;
	}

	/**
	 * @param concept
	 */
	public QualityImpl(ClassURI concept) {
		this();
		if(concept != null)this.concept = concept;
	}	
	
	/**
	 * @param uri
	 * @param concept
	 */
	public QualityImpl(IndividualURI uri, ClassURI concept) {
		this();
		if(uri != null)this.uri = uri;
		if(concept != null)this.concept = concept;
	}
	
	/* (non-Javadoc)
	 * @see de.uniko.isweb.m3o.interfaces.IdentifiedByURI#getURI()
	 */
	public IndividualURI getURI() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(QualityImpl.class.getDeclaredField("uri"), this);
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
	
	public void setURI(URI uri) {
		if(validility){
			try {
				Field field = QualityImpl.class.getDeclaredField("uri");
				if (uri != null){
					Object newObj = new IndividualURI(uri);
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
		this.uri = new IndividualURI(uri);
	}
	
	/* (non-Javadoc)
	 * @see de.uniko.isweb.m3o.interfaces.HasConcept#getConcept()
	 */
	public ClassURI getConcept() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(QualityImpl.class.getDeclaredField("concept"), this);
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

	/**
	 * @param concept
	 */
	public void setConcept(URI concept) {
		if(validility){
			try {
				Field field = QualityImpl.class.getDeclaredField("concept");
				if (concept != null){
					Object newObj = new ClassURI(concept);
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
		this.concept = new ClassURI(concept);
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
