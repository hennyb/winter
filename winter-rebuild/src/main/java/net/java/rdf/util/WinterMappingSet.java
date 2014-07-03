package net.java.rdf.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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

public class WinterMappingSet<E> implements Set<E> {

	protected static transient Logger logger = LoggerFactory.getLogger(WinterMappingSet.class.getName());
	
//	Map<Set<java.net.URI>,E> cache = new HashMap<Set<java.net.URI>,E>();
	public HashSet<E> set = new HashSet<E>();
	HashSet<String> vars = new HashSet<String>();
	QueryBindingSet parentBindingSet = null;
	winter fieldann = null;
	typeinfo typeann = null;
	// if it doesnt work try this Class clazz;
	ValueFactory vf;
	ReadMapper readmapper;
	WriteMapper writemapper;
	Object declaringObject;
	
	public WinterMappingSet(winter fieldann, typeinfo typeann, Object declaringObject, ValueFactory vf, ReadMapper readmapper, WriteMapper writemapper){
		this.fieldann = fieldann;
		this.typeann = typeann;
		this.parentBindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		this.vf = vf;
		this.readmapper = readmapper;
		this.writemapper = writemapper;
		this.declaringObject = declaringObject;
		logger.info("New MappingSet created");
		//this.clazz = clazz;
	}
	
	public boolean add(E object) {
		boolean flag = false;
		// how to cache it and maybe Literals also

		logger.debug("Adding Object", object.toString());
		set.add(object);

		flag = writemapper.setMappingSetMember(fieldann, object, declaringObject);
		return flag;
	}

	public boolean addAll(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!add(object)) flag = false;
		}
		return flag;
	}
	
	public boolean contains(Object object) {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.contains(object);
	}

	public boolean containsAll(Collection<?> collection) {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.containsAll(collection);
	}

	public boolean isEmpty() {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.isEmpty();
	}
	
	public int size() {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.size();
	}

	public Object[] toArray() {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.toArray();
	}

	public <T> T[] toArray(T[] a) {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.toArray(a);
	}
	
	public void clear() {
		try {
			update();
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		removeAll(set);
	}
	
	public boolean removeAll(Collection<?> c) {
		for (Object obj : c){
			remove(obj);
		}
		return true;
	}
	
	public boolean remove(Object o) {
		set.remove(o);
		writemapper.deleteMapping(fieldann, o, declaringObject, false);
		return false;
	}
	
	public boolean update() throws EntityIncompleteException, MalformedAnnotationException{
		Boolean flag = true;
		Set<Object> patterns = readmapper.getPATTERNs(fieldann, ((RdfSerialisable)declaringObject).getBindingSet(), this);
		Object pattern = null;
		set.clear();
		for (Iterator<Object> pit = patterns.iterator(); pit.hasNext(); pattern = pit.next()){
			//TODO add internal caching mechanism
			readmapper.updateObject(pattern);
			if(!add((E)pattern)) flag = false;
		}
		return flag;
	}
	
	//TODO --------------------------------------------
	
	public Iterator<E> iterator() {
		return null;
	}

	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}



}
