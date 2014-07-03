package net.java.rdf.util;

import java.net.URISyntaxException;
import java.util.Collection;
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

public class WinterLiteralSet<E> implements Set<E> {

	protected static transient Logger logger = LoggerFactory.getLogger(WinterSet.class.getName());
	
	public HashSet<String> set = new HashSet<String>();
	HashSet<String> vars = new HashSet<String>();
	QueryBindingSet parentBindingSet = null;
	winter fieldann = null;
	typeinfo typeann = null;
	// if it doesnt work try this Class clazz;
	ValueFactory vf;
	ReadMapper readmapper;
	WriteMapper writemapper;
	Object declaringObject;
	
	public WinterLiteralSet(winter fieldann, typeinfo typeann,Object declaringObject, ValueFactory vf, ReadMapper readmapper, WriteMapper writemapper){
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
		boolean flag = false;
		//TODO fix it, very dirty hack, think about the cache
		set.add(object.toString());
		if(!fieldann.query().equals("")){
			flag = writemapper.setLiteralSetMember(fieldann, object, declaringObject, fieldann.var());
		}else{
			flag = writemapper.setLiteralSetMember(declaringObject.getClass().getAnnotation(winter.class), object, declaringObject, fieldann.var());
		}
		if(typeann != null){
			if (!typeann.query().equals(""))
			writemapper.setSetTypeInfoForLiteralInSet(typeann, parentBindingSet, fieldann.var());
		}
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
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.contains(object);
	}

	public boolean containsAll(Collection<?> collection) {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.containsAll(collection);
	}

	public boolean isEmpty() {
		try {
			update();
		} catch (FieldNotMappedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldMemberTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set.isEmpty();
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
		return set.size();
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
		return set.toArray();
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
		return set.toArray(a);
	}

	public boolean update() throws FieldNotMappedException, FieldMemberTypeException{
		Boolean flag = true;
		Set<String> literals = readmapper.getLITERALs(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), parentBindingSet);
		removeDeprecatedfromCache(literals);
		for (String literal : literals){
			Iterator<String> lit = literals.iterator();
			if(!set.contains(literal)){
				try {
					Object newObj = this.getClass().getComponentType().newInstance();
					newObj = literal;
					//TOTO Hier schreibt er einmal zuviel auch in den anderen Sets checken
					if(!add((E)newObj)) flag = false;
				} catch (IllegalArgumentException e) {
					logger.error("{}",e);
				} catch (IllegalAccessException e) {
					logger.error("{}",e);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
		
	public boolean removeDeprecatedfromCache(Set<String> literals){
		set.retainAll(literals);
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
		removeAll(set);
	}
	
	public boolean removeAll(Collection<?> collection) {
		for (Object lit : collection){
			String literal = lit.toString();
			if(set.contains(literal)){
				remove(literal);
				set.remove(literal);
			}
		}
		return true;
	}
	
	public boolean remove(Object o) {
		set.remove(o);
		writemapper.deleteLiteral(fieldann, declaringObject, fieldann.var(), false, o);
		return true;
	}
	
	//TODO --------------------------------
	
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}



	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
}
