package net.java.rdf.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.java.rdf.annotations.typeinfo;
import net.java.rdf.annotations.winter;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.ReadMapper;
import net.java.rdf.winter.WriteMapper;

import org.openrdf.model.ValueFactory;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinterObjectSet<E> implements Set<E> {


	protected static transient Logger logger = LoggerFactory.getLogger(WinterSet.class.getName());
	
//	Map<Set<java.net.URI>,E> cache = new HashMap<Set<java.net.URI>,E>();
	public HashMap<java.net.URI, Object> map = new HashMap<java.net.URI, Object>();
	HashSet<String> vars = new HashSet<String>();
	QueryBindingSet parentBindingSet = null;
	winter fieldann = null;
	typeinfo typeann = null;
	// if it doesnt work try this Class clazz;
	ValueFactory vf;
	ReadMapper readmapper;
	WriteMapper writemapper;
	Object declaringObject;
	
	public WinterObjectSet(winter fieldann, typeinfo typeann,Object declaringObject, ValueFactory vf, ReadMapper readmapper, WriteMapper writemapper){
		this.fieldann = fieldann;
		this.typeann = typeann;
		this.parentBindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		this.vf = vf;
		this.readmapper = readmapper;
		this.writemapper = writemapper;
		this.declaringObject = declaringObject;
		//this.clazz = clazz;
	}
	
	public boolean add(E object) {
		logger.info("WinterObjectSet - WinterObjectSet:add Adding Object {}", object.toString());
		boolean flag = false;
		winter annotation = null;
		java.net.URI uri = ((IdentifiedByURI)object).getURI().getURI();
		map.put(uri, object);
		if(fieldann.query().equals("")){
			annotation = (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class);
		}else{
			annotation = fieldann;
		}
		flag = writemapper.setObjectSetMember(annotation, object, declaringObject, fieldann.var());
		winter objann = (winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class);
		return flag;
	}

	public boolean addAll(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.info("WinterSet:addAll Object {}", object.toString());
			if (!add(object)) flag = false;
		}
		return flag;
	}

	public boolean contains(Object o) {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		}
		if(map.containsValue(o)) return true;
		return false;
	}
	
	public boolean containsByURI(java.net.URI uri){
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		}
		if (map.containsKey(uri)) return true;
		return false;
	}
	
	public boolean containsAll(Collection<?> c) {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		}
		boolean flag = true;
		for (Object o : c){
			flag = map.containsValue(o);
			if (!flag) break;
		}
		return flag;
	}
	
	public boolean containsAllByURI(Collection<java.net.URI> c){
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		}
		boolean flag = true;
		for (java.net.URI u : c){
			flag = map.containsKey(u);
			if (!flag) break;
		}
		return flag;
	}
	
	public boolean isEmpty() {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		}
		return map.isEmpty();
	}

	public int size() {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map.size();
	}

	public Object[] toArray() {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map.values().toArray();
	}

	public <T> T[] toArray(T[] a) {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map.values().toArray(a);
	}

	public boolean update() throws FieldNotMappedException, FieldMemberTypeException{
		Set<java.net.URI> uris = null;
		Boolean flag = true;
		if (fieldann == null) throw new FieldNotMappedException("WinterSet is not mapped");
		if ((((winter)ClassAnalysis.getAnnotation(this.getClass().getComponentType(), winter.class)).type() != winter.Type.EXTERNALOBJECT)) 
			throw new FieldMemberTypeException("Wrong fieldMemberType, winter.Type.OBJECT annotated collection fields should have componenttype winter.Type.OBJECT");
		uris = readmapper.getOBJECTs(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), parentBindingSet);
		removeDeprecatedfromCache(uris);
		for (java.net.URI uri : uris){
			if(!map.containsKey(uri)){
				try {
					Object newObj = this.getClass().getComponentType().newInstance();
					((IdentifiedByURI)newObj).setURI(uri);
					readmapper.updateObject(newObj);
					if(!add((E)newObj)) flag = false;
					//readmapper.update(newobj);
				} catch (InstantiationException e) {
					logger.error("{}", e);
				} catch (IllegalAccessException e) {
					logger.error("{}", e);
				}
			}
		}
		return flag;
	}
	
	public boolean removeDeprecatedfromCache(Set<java.net.URI> uris){
		Set<java.net.URI> keySet = new HashSet<java.net.URI>(uris);
		keySet.retainAll(map.keySet());
		for (java.net.URI uri : uris){
			map.remove(uri);
			//this.remove(map.get(uri));
		}
		return true;
	}
	
	public void clear() {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		removeAllByURI(map.keySet());
	}

	public boolean removeAllByURI(Set<java.net.URI> uris){
		for (java.net.URI key : uris){
			remove(map.get(key));
		}
		return true;
	} 
	
	public boolean removeAll(Collection<?> c) {
		for (Object obj : c){
			remove(obj);
		}
		return false;
	}
	
	public boolean remove(Object o) {
		map.remove(((IdentifiedByURI)o).getURI().getURI());
		writemapper.deleteObject(fieldann, o, declaringObject, fieldann.var(), false);
		return true;
	}
	
	//TODO --------------------------------------------
	
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
