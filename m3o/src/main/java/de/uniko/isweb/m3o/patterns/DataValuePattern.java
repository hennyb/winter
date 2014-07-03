package de.uniko.isweb.m3o.patterns;


import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.openrdf.sail.nativerdf.ValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.rdf.util.ClassAnalysis;
import net.java.rdf.util.EntityIncompleteException;
import net.java.rdf.util.FieldMemberTypeException;
import net.java.rdf.util.FieldNotMappedException;
import net.java.rdf.util.FieldNotSetException;
import net.java.rdf.util.IdentifiedByURI;
import net.java.rdf.util.Utils;
import net.java.rdf.util.WrapsURI;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameReadMapper;
import net.java.rdf.winter.SesameWriteMapper;
import de.uniko.isweb.m3o.implementations.InformationEntityImpl;
import de.uniko.isweb.m3o.implementations.QualityImpl;
import de.uniko.isweb.m3o.implementations.RegionImpl;
import de.uniko.isweb.m3o.interfaces.InformationEntity;
import de.uniko.isweb.m3o.interfaces.Quality;
import de.uniko.isweb.m3o.interfaces.Region;
import de.uniko.isweb.m3o.utils.ClassURI;
import de.uniko.isweb.m3o.utils.IndividualURI;
import net.java.rdf.annotations.winter;


@winter(type = winter.Type.PATTERN, 
		query = "?InformationEntity <dul:hasQuality> ?Quality. " +
				"?Quality <dul:hasRegion> ?Region. " +
				"?Region <dul:hasRegionDataValue> ?Value. "
				)
public class DataValuePattern implements RdfSerialisable{
	
	private QueryBindingSet bindingSet = null;
	private SesameReadMapper readmapper = null;
	private SesameWriteMapper writemapper = null;
	private boolean validility = false;
	
	//Objects
	@winter(type=winter.Type.EXTERNALOBJECT, var = "InformationEntity")
	InformationEntity informationEntity = null;
	@winter(type=winter.Type.EXTERNALOBJECT, var = "Quality")
	Quality quality = null;
	@winter(type=winter.Type.EXTERNALOBJECT, var = "Region")
	Region region = null;
	@winter(type=winter.Type.LITERAL, var = "Value")
	Object data = null;
	
//	@winter (type=winter.Type.LITERAL, var = "Value")
//	public Collection<String> data = new HashSet<String>();

	//Members
	public InformationEntity getInformationEntity() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DataValuePattern.class.getDeclaredField("InformationEntity"), this);
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
				Field field = DataValuePattern.class.getDeclaredField("informationEntity");
				if (quality != null){
					Object newObj = quality;
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

	public Quality getQuality() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DataValuePattern.class.getDeclaredField("quality"), this);
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
		return quality;
	}

	public void setQuality(Quality quality) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DataValuePattern.class.getDeclaredField("quality");
				if (quality != null){
					Object newObj = quality;
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
		this.quality = quality;
	}

	public Region getRegion() {
		try {
			if(readmapper != null)readmapper.updateOBJECTField(DataValuePattern.class.getDeclaredField("region"), this);
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
		return region;
	}

	public void setRegion(Region region) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DataValuePattern.class.getDeclaredField("region");
				if (region != null){
					Object newObj = region;
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
		this.region = region;
	}

	public Object getData() {
		try {
			if(readmapper != null)readmapper.updateLITERALField(DataValuePattern.class.getDeclaredField("data"), this);
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
		return data;
	}

	public void setData(Object data) {
		if(writemapper != null){
		//if(validility){
			try {
				Field field = DataValuePattern.class.getDeclaredField("data");
				if (data != null){
					String newData = data.toString();
					writemapper.replaceLiteral(field.getAnnotation(winter.class), this, newData);
					}else{
						writemapper.deleteLiteral(field.getAnnotation(winter.class), this, field.getAnnotation(winter.class).var(), false, null);
					}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.data = data;
	}

	public DataValuePattern() {
		this.informationEntity = new InformationEntityImpl();
		this.quality = new QualityImpl();
		this.region = new RegionImpl();
		validility = true;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 */
	// TODO replace InformationEntity with IdentifiedByURI
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region){
		this();
		if(informationEntity != null)this.informationEntity = informationEntity;
		if(quality != null)this.quality = quality;
		if(region != null)this.region = region;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 * @param string
	 */
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region, String string){
		this(informationEntity, quality, region);
		this.data = string;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 * @param integer
	 */
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region, Integer integer){
		this(informationEntity, quality, region);
		this.data = integer;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 * @param floating
	 */
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region, Float floating){
		this(informationEntity, quality, region);
		this.data = floating;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 * @param bool
	 */
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region, Boolean bool){
		this(informationEntity, quality, region);
		this.data = bool;
	}
	
	/**
	 * @param informationEntity
	 * @param quality
	 * @param region
	 * @param doubled
	 */
	public DataValuePattern(InformationEntity informationEntity, Quality quality, Region region, Double doubled){
		this(informationEntity, quality, region);
		this.data = doubled;
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

}
