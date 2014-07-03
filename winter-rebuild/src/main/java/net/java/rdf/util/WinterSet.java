/*
 New BSD license: http://opensource.org/licenses/bsd-license.php

 Copyright (c) 2003, 2004, 2005 Sun Microsystems, Inc.
 901 San Antonio Road, Palo Alto, CA 94303 USA. 
 All rights reserved.


 Redistribution and use in source and binary forms, with or without 
 modification, are permitted provided that the following conditions are met:

 - Redistributions of source code must retain the above copyright notice, 
  this list of conditions and the following disclaimer.
 - Redistributions in binary form must reproduce the above copyright notice, 
  this list of conditions and the following disclaimer in the documentation 
  and/or other materials provided with the distribution.
 - Neither the name of Sun Microsystems, Inc. nor the names of its contributors
  may be used to endorse or promote products derived from this software 
  without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
*/

package net.java.rdf.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import net.java.rdf.annotations.*;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.ReadMapper;
import net.java.rdf.winter.WriteMapper;

import org.openrdf.model.ValueFactory;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javassist.compiler.ast.Declarator;


/**
 *
 * 
 * @author Stefan Scheglmann
 *
 * 
 */
@Deprecated
public class WinterSet<E> implements Set<E>{

	protected static transient Logger logger = LoggerFactory.getLogger(WinterSet.class.getName());
	
//	Map<Set<java.net.URI>,E> cache = new HashMap<Set<java.net.URI>,E>();
	Map<java.net.URI,E> cache = new HashMap<java.net.URI,E>();
	HashSet<String> vars = new HashSet<String>();
	QueryBindingSet parentBindingSet = null;
	winter fieldann = null;
	typeinfo typeann = null;
	// if it doesnt work try this Class clazz;
	ValueFactory vf;
	ReadMapper readmapper;
	WriteMapper writemapper;
	Object declaringObject;
	
	public WinterSet(winter fieldann, typeinfo typeann,Object declaringObject, ValueFactory vf, ReadMapper readmapper, WriteMapper writemapper){
		this.fieldann = fieldann;
		this.typeann = typeann;
		this.parentBindingSet = ((RdfSerialisable)declaringObject).getBindingSet();
		this.vf = vf;
		this.readmapper = readmapper;
		this.writemapper = writemapper;
		this.declaringObject = declaringObject;
		//this.clazz = clazz;
	}
    
	public boolean addMAPPING(E object){
		boolean flag = false;
		// how to cache it and maybe Literals also
		try {
			logger.debug("Hashcode {} for Object {}", object.hashCode(), object.toString());
			String hashcode = new Integer(object.hashCode()).toString();
			URI uri = new java.net.URI("http://".concat(hashcode));
			cache.put(uri, object);
			((IdentifiedByURI)object).setURI(uri);
		} catch (URISyntaxException e) {
			logger.error("addMaping : Can not create URI for object", e);
		}
		flag = writemapper.setMappingSetMember(fieldann, object, declaringObject);
		return flag;
	}
	
	public boolean addOBJECT(E object){
		boolean flag = false;
		java.net.URI uri = ((IdentifiedByURI)object).getURI().getURI();
		cache.put(uri, object);
		flag = writemapper.setObjectSetMember(fieldann, object, declaringObject, fieldann.var());
		winter objann = (winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class);
		return flag;
	}
	
	public boolean addURI(E object){
		boolean flag = false;
		java.net.URI uri = ((WrapsURI)object).getURI();
		cache.put(uri, object);
		//flag = writemapper.setURISetMember(fieldann, object, declaringObject);
		if(!fieldann.query().equals("")){
			flag = writemapper.setURISetMember(fieldann, object, declaringObject, fieldann.var());
		}else{
			flag = writemapper.setURISetMember(declaringObject.getClass().getAnnotation(winter.class), object, declaringObject, fieldann.var());
		}
		if(typeann != null){
			if (!typeann.query().equals(""))
			writemapper.setSetTypeInfoForURIInSet(typeann, uri, parentBindingSet, fieldann.var());
		}
		return flag;
	}
	
	public boolean addLITERAL(E object){
		boolean flag = false;
		//TODO fix it, very dirty hack, think about the cache
		try {
			cache.put(new java.net.URI("http://".concat(object.toString())), object);
		} catch (URISyntaxException e) {
			logger.error("{}", e);
		}

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
		
	public boolean addAllMAPPINGs(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!addMAPPING(object)) flag = false;
		}
		return flag;
	}
	
	public boolean addAllOBJECTs(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!addOBJECT(object)) flag = false;
		}
		return flag;
	}
	
	public boolean addAllURIs(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!addURI(object)) flag = false;
		}
		return flag;
	}
	
	public boolean addAllLITERALs(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!addLITERAL(object)) flag = false;
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
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cache.containsValue(o)) return true;
		return false;
	}
	
	public boolean containsByURI(java.net.URI uri){
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cache.containsKey(uri)) return true;
		return false;
	}
	
	public boolean containsAll(Collection<?> c) {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean flag = true;
		for (Object o : c){
			flag = cache.containsValue(o);
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
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean flag = true;
		for (java.net.URI u : c){
			flag = cache.containsKey(u);
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
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.isEmpty();
	}

	public int size() {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.size();
	}
	
	public Object[] toArray() {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.values().toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.values().toArray(a);
	}

	public boolean updateOBJECT() throws FieldNotMappedException, FieldMemberTypeException{
		Set<java.net.URI> uris = null;
		Boolean flag = true;
		if (fieldann == null) throw new FieldNotMappedException("WinterSet is not mapped");
		if ((((winter)ClassAnalysis.getAnnotation(this.getClass().getComponentType(), winter.class)).type() != winter.Type.EXTERNALOBJECT)) 
			throw new FieldMemberTypeException("Wrong fieldMemberType, winter.Type.OBJECT annotated collection fields should hace componenttype winter.Type.OBJECT");
		uris = readmapper.getOBJECTs(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), parentBindingSet);
		removeDeprecatedfromCache(uris);
		for (java.net.URI uri : uris){
			if(!cache.containsKey(uri)){
				try {
					Object newObj = this.getClass().getComponentType().newInstance();
					((IdentifiedByURI)newObj).setURI(uri);
					readmapper.updateObject(newObj);
					if(!addOBJECT((E)newObj)) flag = false;
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
	
	public boolean updateURI() throws FieldNotMappedException, FieldMemberTypeException{
		Set<java.net.URI> uris = null;
		Boolean flag = true;
		if (!(((Collection)this).getClass().getComponentType().isInstance(WrapsURI.class))) throw new FieldMemberTypeException(
			"Wrong fieldMemberType, winter.Type.URI annotated collection fields should declare componentType of type URI");
		uris = null;
		if(fieldann.query() != null) {
			uris = readmapper.getURIs(fieldann, fieldann, ((RdfSerialisable)declaringObject).getBindingSet());
			removeDeprecatedfromCache(uris);
		// URI field without query this URI should be present in object annotation
		}else{
			uris = readmapper.getURIs(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), parentBindingSet);
		}
		for (java.net.URI uri : uris){
			if(!cache.containsKey(uri)){
				try {
					Object newObj = this.getClass().getComponentType().newInstance();
					((WrapsURI)newObj).setURI(uri);
					if(!addURI((E)newObj)) flag = false;;
				} catch (InstantiationException e) {
					logger.error("{}", e);
				} catch (IllegalAccessException e) {
					logger.error("{}", e);
				}
			}
		}
		return flag;
	}
	
	public boolean updateLITERAL() throws FieldNotMappedException, FieldMemberTypeException{
		Boolean flag = true;
		if (!((Collection)this).getClass().getComponentType().isPrimitive() &&
				!((Collection)this).getClass().getComponentType().equals(String.class)) throw new FieldMemberTypeException(
				"Wrong fieldMemberType, winter.Type.Literal annotated fields should declare members of primitive type or String");
		Set<String> literals = readmapper.getLITERALs(fieldann, (winter)ClassAnalysis.getAnnotation(declaringObject.getClass(), winter.class), parentBindingSet);
		HashSet<java.net.URI> uris = (HashSet)createKeysforLiterals(literals);
		removeDeprecatedfromCache(uris);
		for (int i = 0; i < literals.size(); i++){
			Iterator<String> lit = literals.iterator();
			Iterator<java.net.URI> uit = uris.iterator();
			if(!cache.containsKey(uit.next())){
				try {
					Object newObj = this.getClass().getComponentType().newInstance();
					newObj = lit.next();
					if(!addLITERAL((E)newObj)) flag = false;
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
	
	public boolean updateMAPPING() throws EntityIncompleteException, MalformedAnnotationException{
		Boolean flag = true;
//		Set<Object> patterns = readmapper.getPATTERNs(fieldann, ((RdfSerialisable)declaringObject).getBindingSet(), this);
//		Object pattern = null;
//		cache.clear();
//		for (Iterator<Object> pit = patterns.iterator(); pit.hasNext(); pattern = pit.next()){
//			//TODO add internal caching mechanism
//			readmapper.updateObject(pattern);
//			if(!addMAPPING((E)pattern)) flag = false;
//		}
		return flag;
	}
	
	/**
	 * Updates a whole WinterSet. Queries for the sets members adds new ones, //TODO delete deprecated ones
	 * 
	 * @return True if everything works well, else false;
	 * @throws FieldNotMappedException
	 * @throws FieldMemberTypeException
	 * @throws MalformedAnnotationException 
	 * @throws EntityIncompleteException 
	 */
	public boolean update() throws FieldNotMappedException, FieldMemberTypeException, EntityIncompleteException, MalformedAnnotationException{
		switch (fieldann.type()) {
		case MAPPING:
			return updateMAPPING();

		case EXTERNALOBJECT:
			return updateOBJECT();
		
		case INTERNALOBJECT:
			return updateURI();
			
		case LITERAL:
			return updateLITERAL();
			
		default:
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(E object) {
		switch (fieldann.type()) {
		case MAPPING:
			return addMAPPING(object);
	
		case EXTERNALOBJECT:	
			return addOBJECT(object);
			
		case INTERNALOBJECT:
			return addURI(object);
			
		case LITERAL:
			return addLITERAL(object);
			
		default:
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Set#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> collection) {
		boolean flag = true;
		for (E object : collection){
			logger.debug("WinterSet:addAll Object {}", object.toString());
			if (!add(object)) flag = false;
		}
		return flag;
	}
	
	public boolean removeDeprecatedfromCache(Set<java.net.URI> uris){
		Set<java.net.URI> keySet = new HashSet<java.net.URI>(uris);
		keySet.retainAll(cache.keySet());
		for (java.net.URI uri : uris){
			cache.remove(uri);
		}
		return true;
	}
	
	public Set<java.net.URI> createKeysforLiterals(Set<String> literals){
		Set<java.net.URI> keys = new HashSet<java.net.URI>();
		for (String literal : literals){
			try {
				keys.add(new java.net.URI("http://".concat(literal.toString())));
			} catch (URISyntaxException e) {
				logger.error("{}", e);
			}
		}
		return keys;
	}

	//TODO ----------------------------------------------------
	
	
	public Iterator<E> iterator() {
		try {
			this.update();
		} catch (FieldNotMappedException e) {
			logger.error("{}", e);
		} catch (FieldMemberTypeException e) {
			logger.error("{}", e);
		} catch (EntityIncompleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedAnnotationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean retainAll(Collection<?> c) {
		return false;
	}
	
	public void clear() {
		// TODO Auto-generated method stub	
	}
}
