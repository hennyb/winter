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


import net.java.rdf.annotations.*;
import net.java.rdf.winter.RdfSerialisable;
import net.java.rdf.winter.SesameWriteMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.algebra.evaluation.QueryBindingSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Methods for analysing class elements
 *
 * @author Henry Story, Stefan Scheglmann
 */
public class ClassAnalysis {

	protected static transient Logger logger = LoggerFactory.getLogger(ClassAnalysis.class.getName());
	    
    /**
     * Builds a select-*-query-String from an given annotation and graph 
     * 
     * @param objann 	The annotation to get the query pattern from
     * @param graph	 	The graph 
     * @return			A Stringbuilder object with the complete set-query-String in it
     */
    public static StringBuilder buildSelectAllQuery(winter objann, URI graph) throws MalformedAnnotationException{
    	//TODO check this .equals("")
    	if (objann.query().equals("")) throw new MalformedAnnotationException("ClassAnalysis:buildSetQuery Could not build query annotation is empty");
    	logger.debug("ClassAnalysis:buildSelectAllQuery with annotation {} and graph {}", objann, graph.toString());
		StringBuilder query = new StringBuilder();
		
		
		query.append("SELECT * ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append(objann.query().toString());
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildSelectConceptQuery(URI graph){
    	logger.debug("ClassAnalysis:buildSelectConceptQuery for graph {}",graph.toString());
		StringBuilder query = new StringBuilder();
		query.append("SELECT ?concept ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append("?individual <rdf:Type> ?concept");
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildSelectIndividualURIQuery(URI graph){
    	logger.debug("ClassAnalysis:buildSelectConceptQuery for graph {}",graph.toString());
		StringBuilder query = new StringBuilder();
		query.append("SELECT ?individual ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append("?individual <rdf:Type> ?concept");
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildSelectAllQuery(typeinfo objann, URI graph) throws MalformedAnnotationException{
    	//TODO check this .equals("")
    	if (objann.query().equals("")) throw new MalformedAnnotationException("ClassAnalysis:buildSetQuery Could not build query annotation is empty");
    	logger.debug("ClassAnalysis:buildSetQuery with annotation {} and graph {}", objann, graph.toString());
		StringBuilder query = new StringBuilder();
		query.append("SELECT * ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append(objann.query().toString());
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildSelectAllQuery(StringBuilder query, URI graph){
    	logger.debug("ClassAnalysis:buildSetQuery with annotation {} and graph {}", query.toString(), graph.toString());
    	query.insert(0, "WHERE { ");
    	if (graph != null){
    		query.insert(0, "FROM " + "<" + graph.toString() + "> "); 
    	}
    	query.insert(0, "SELECT * ");
    	if (query.toString().endsWith(".")){
    		query = new StringBuilder(query.substring(0, query.length()-1));
    	}
    	query.append(" }");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildSelectVarQuery(winter objann, URI graph, String[] vars) throws MalformedAnnotationException{
    	//TODO Check this  .equals("")
    	if (objann.query().equals("")) throw new MalformedAnnotationException("ClassAnalysis:buildSetQuery Could not build query annotation is empty");
    	logger.debug("ClassAnalysis:buildSetQuery with annotation {} and graph {}", objann, graph.toString());
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		for (String var : vars) query.append("?").append(var).append(" ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append(objann.query().toString());
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    public static StringBuilder buildAskQuery(winter objann, URI graph) throws MalformedAnnotationException{
    	//TODO Check this  == null
    	if (objann.query().equals("")) throw new MalformedAnnotationException("ClassAnalysis:buildSetQuery Could not build query annotation is empty");
    	logger.debug("ClassAnalysis:buildSetQuery with annotation {} and graph {}", objann, graph.toString());
		StringBuilder query = new StringBuilder();
		query.append("Ask ");
		if (graph != null){
			query.append("FROM ").append("<").append(graph.toString()).append("> ");
		}
		query.append("WHERE ");
		query.append(" { ");
		query.append(objann.query().toString());
		query.append(" } ");
		logger.debug("ClassAnalysis:buildSetQuery query {}", query.toString());
		return query;
    }
    
    /**
     * Get the with annotationClass specified annotation for a Object. Also searches in Superclasses and implemented Interfaces
     * 
     * @param clazz				The Class to get the annotation for
     * @param annotationClass	The Type of the annotation
     * @return
     */
    public static Annotation getAnnotation(Class clazz, Class annotationClass) {
    	logger.debug("ClassAnalysis:getAnnotation Try to get annotation of type {} for class {}", annotationClass.toString(), clazz.toString());
    	Annotation annotation = clazz.getAnnotation(annotationClass);
    	breakpoint:
    	while(annotation == null && !clazz.equals(Object.class)){
    		Class[] interfaces = clazz.getInterfaces();
    		logger.debug("ClassAnalysis:getAnnotation no annotation of type {} found for class {}, try it with interfaces {}", new Object[]{annotationClass.toString(), clazz.toString(), interfaces.toString()});
    		for (Class interf : interfaces){
    			annotation = interf.getAnnotation(annotationClass);
    			logger.debug("ClassAnalysis:getAnnotation try interface {} with annotations {}", interf.toString(), annotation);
    			if (annotation != null) break breakpoint;
    		}
    		logger.debug("Classanalyser:getAnnotation no annotation of type {} found for class {}, try it with superclass {}", new Object[]{annotationClass.toString(), clazz.toString(), clazz.getSuperclass().toString()});
    		clazz = clazz.getSuperclass();
    		annotation = clazz.getAnnotation(winter.class);
    	}
    	if (annotation == null){
    		logger.error("ClassAnalysis:getAnnotation No annotation of type {} found for class {} this should not happen here", annotationClass.toString(), clazz.toString());
    	}else{
    		logger.debug("ClassAnalysis:getAnnotation Annotation of type {} found for class {}", annotationClass.toString(), clazz.toString());
    	}
    	return annotation;
    }
    
    public static QueryBindingSet buildBindingSetForMapping(QueryBindingSet declaringObjectBindingSet, String[] src, String[] dst) throws MalformedAnnotationException {
		int lenght = src.length;
		if(lenght != 0 && dst.length == lenght){
	    	QueryBindingSet bindingSet = new QueryBindingSet();
			for (int i = 0 ; i < lenght; i++){
				bindingSet.addBinding(dst[i], declaringObjectBindingSet.getValue(src[i]));
			}
	    	return bindingSet;
		}else throw new MalformedAnnotationException();
		
    }
    
    @Deprecated
    public static QueryBindingSet buildBindingSetForPattern(QueryBindingSet bindingSet, Object object, ValueFactory vf){
		//TODO change to generic annotation
		logger.debug("ClassAnalysis:buildBindingSet with Object {}, and ValueFactory {}", object.toString(), vf.toString() );
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields){
			logger.debug("ClassAnalysis:buildBindingSet Interating over fields : field {}", field.toString());
			winter fieldann = field.getAnnotation(winter.class);
			if (fieldann != null){
				try{
					field.setAccessible(true);
					Object memberObject = field.get(object);
					logger.debug("ClassAnalysis:buildBindingSet fieldann {}\n for field {}",new Object[]{fieldann.toString(), field.getType().toString()});
					//TODO Check this 2 times????
					if(fieldann != null){
						switch (fieldann.type()) {
						case INTERNALOBJECT:
							if(!(memberObject instanceof Collection)){
								if (bindingSet.hasBinding(fieldann.var())){
									WrapsURI uri = (WrapsURI)memberObject.getClass().newInstance();
									uri.setURI(new URI(bindingSet.getValue(fieldann.var()).toString()));
									((RdfSerialisable)memberObject).setValidility(false);
									field.set(object, uri);
									((RdfSerialisable)memberObject).setValidility(true);
								}
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Object {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
								Value uri = vf.createURI(((WrapsURI)memberObject).getURI().toString());
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Binding for Object {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), uri});
								bindingSet.addBinding(fieldann.var().toString(), uri);
							
							}else{
								for (Object collobj : (Collection)memberObject){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Collection {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
									Value uri = vf.createURI(((WrapsURI)collobj).getURI().toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Bindings for Collection {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), uri});
									
									// that does not work only one binding per name !!!!!!!!! how to make it work ???
									bindingSet.addBinding(fieldann.var(), uri);
								}
								//TODO deal with collections here
							}
	
							break;
							
						case EXTERNALOBJECT:
						//if ((winter)ClassAnalysis.getAnnotation(field.getType() , winter.class) != null){
							if(!(memberObject instanceof Collection)){
								if (bindingSet.hasBinding(fieldann.var())){
									URI uri = new URI(bindingSet.getValue(fieldann.var()).toString());
									((RdfSerialisable)memberObject).setValidility(false);
									((IdentifiedByURI)memberObject).setURI(uri);
									((RdfSerialisable)memberObject).setValidility(true);
								}
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Object {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
								Value uri = vf.createURI(((IdentifiedByURI)memberObject).getURI().getURI().toString());
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Binding for Object {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), uri});
								bindingSet.addBinding(fieldann.var().toString(), uri);
							}else{
								for (Object collobj : (Collection)memberObject){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Collection {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
									Value uri = vf.createURI(((IdentifiedByURI)collobj).getURI().getURI().toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Bindings for Collection {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), uri});
									
									// that does not work only one binding per name !!!!!!!!! how to make it work ???
									bindingSet.addBinding(fieldann.var(), uri);
								}
							}
						//}else{
						//	logger.error("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Field member is not annotated this should not happen here");
						//}
							break;
							
						case LITERAL:
							if(!(memberObject instanceof Collection)){
								if (bindingSet.hasBinding(fieldann.var())){
									String uri = bindingSet.getValue(fieldann.var()).toString();
									((RdfSerialisable)memberObject).setValidility(false);
									field.set(object, (Object)uri);
									((RdfSerialisable)memberObject).setValidility(true);
								}
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Object {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
								Value literal =  vf.createLiteral(memberObject.toString());
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Binding for Object {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), literal});
								bindingSet.addBinding(fieldann.var(), literal);
							}else{
								for(Object collobj : (Collection)memberObject){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Collection {} implements {}", memberObject.toString(), memberObject.getClass().getInterfaces());
									Value literal = vf.createLiteral(collobj.toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Bindings for Collection {},\n Variable {} with Value {}", new Object[]{memberObject.toString(), fieldann.var().toString(), literal});
									
									// that does not work only one binding per name !!!!!!!!! how to make it work ???
									bindingSet.addBinding(fieldann.var(), literal);
								}
								//TODO deal with collections here
							}
							break;
		
						default:
							break;
						}
					}else{
						logger.error("");
					}
				}catch(IllegalAccessException e){
					logger.error(" ClassAnalysis:buildBindingSet Could not access Field {} Exception  : {}", field.toString(), e);
				} catch (InstantiationException e) {
					logger.error("{}",e);
				} catch (URISyntaxException e) {
					logger.error("{}",e);
				}
				field.setAccessible(false);
			}
			
		}
		return bindingSet;
    }
	
    /**
	 * Creates a BindingSet for Pattern-Objects who are MAPPING annotated Members of Pattern-Objects. The parental Object must be already proccessed
	 * 
	 * @param object 				The object the BindingSet should be build for
	 * @param vf					The ValueFactory needed to create Values for the BindingSet
	 * @param objann				The annotation of the member-field of the object in the parental Object. Usually this should be a MAPPING annotation
	 * @param parentBindingSet		The BindingSet of the parental Object
	 * @return						The created BindingSet
	 */
	public static QueryBindingSet buildBindingSet(Object object, ValueFactory vf, winter parentalFieldAnn, QueryBindingSet parentBindingSet) throws MalformedAnnotationException, FieldNotSetException{
		logger.info("ClassAnalysis:buildBindingSet with Object {}\n ValueFactory {}\n, Parental FieldAnnotation{}\n",new Object[]{ object.toString(), vf.toString(), parentalFieldAnn});
		QueryBindingSet bindingSet = new QueryBindingSet();
		if(parentalFieldAnn.src().length != parentalFieldAnn.dst().length) throw new MalformedAnnotationException("Mapping src and dst are not of the same Lenght");
		if(parentalFieldAnn.type() == winter.Type.MAPPING){
			logger.info("ClassAnalysis:buildBindingSet Mapping {} to {}", parentalFieldAnn.src().length, parentalFieldAnn.dst().length);
			for (int i = 0; i <  parentalFieldAnn.src().length; i++){
				bindingSet.addBinding(parentalFieldAnn.dst()[i], parentBindingSet.getBinding(parentalFieldAnn.src()[i]).getValue());
				logger.info("ClassAnalysis:buildBindingSet Binding {} added {} = {}", new Object[]{i , parentalFieldAnn.dst()[i], parentBindingSet.getBinding(parentalFieldAnn.src()[i]).getValue()});
			}
		}else{
			// TODO maybe introduce new Exception
			logger.error("ClassAnalysis:buildBindingSet Wrong method called, this one is form MAPPING bindingSets");
		}
		logger.info("ClassAnalysis:buildBindingSet BindingSet : {}", bindingSet.toString());
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields){
			logger.debug("ClassAnalysis:buildBindingSet Interating over fields : field {}", field.toString());
			winter fieldann = field.getAnnotation(winter.class);
			field.setAccessible(true);
			if(fieldann != null){
				switch (fieldann.type()) {
				case INTERNALOBJECT:
					try{
						logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH INTERNALOBJECT some field information Accessibility : {}, Synthetic : {}, Type : {} ",new Object[]{field.isAccessible(), field.isSynthetic(), field.getGenericType()});
						Object obj = field.get(object);
						if(obj == null) throw new FieldNotSetException(" Field " + field.toString() + " is not set, this could lead to problems all fields should be set");
						logger.debug("	 Field-Object : {}", obj);
						if(!(obj instanceof Collection)){
							if(!bindingSet.hasBinding(fieldann.var().toString())){
								// if Binding is not already set from parental-mapping
								logger.debug("	 Binding for {} is not yet set, set it", fieldann.var());
								Value uri = vf.createURI(((WrapsURI)obj).getURI().toString());
								logger.debug("	 Binding for Object {}, Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
								bindingSet.addBinding(fieldann.var().toString(), uri);
							}else{
								logger.debug("	 Binding for {} is already set from ParentBindingSet", fieldann.var());
								// Set field to the value of binding from parental-mapping	
								//TODO set field fix it
									try {
										logger.debug("	!!!!!!!!!!!!!!!!!! ",obj.toString());
										Object newVal = obj.getClass().newInstance();
										((WrapsURI)newVal).setURI(new java.net.URI(bindingSet.getBinding(fieldann.var().toString()).getValue().toString()));
										field.set(object, newVal);
									} catch (InstantiationException e) {
										logger.error("	 Could not instanciate newVal", e);
									} catch (URISyntaxException e) {
										logger.error("	 Malformed URI", e);
									}
							}
						}else{
							//TODO deal with collections here
						}
					}catch(IllegalAccessException e){
						logger.error(" 	 Could not access Field {} Exception  : {}", field.toString(), e);
					}
					logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH INTERNALOBJECT finished");
					break;
					
				case EXTERNALOBJECT:
					try{
						Object obj = field.get(object);
						logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Checking for {} with type {}", object.toString(), field.getType().toString());
						if ((winter)ClassAnalysis.getAnnotation(field.getType() , winter.class) != null){
							if(!(obj instanceof Collection)){
								if(!bindingSet.hasBinding(fieldann.var().toString())){
									// if Binding is not already set from parental-mapping
									logger.info("	 Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
									Value uri = vf.createURI(((IdentifiedByURI)obj).getURI().getURI().toString());
									logger.info("	 Binding for Object {}, Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
									bindingSet.addBinding(fieldann.var().toString(), uri);
								}else{
									logger.info("	 Binding is already set from ParentBindingSet");
									// Set field to the value of binding from parental-mapping	
										try {
											Object newVal = obj.getClass().newInstance();
											logger.info("	ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI New Object {} created", newVal);
											if (obj instanceof IdentifiedByURI){
												//TODO Fix interfaces an set up new object
												((WrapsURI)newVal).setURI(new java.net.URI(bindingSet.getBinding(fieldann.var().toString()).getValue().toString()));
												field.set(object, newVal);
												if (obj instanceof HasConcept){
													
												}else{
													logger.warn("	Object {} has URI but does not implement Interface {},\n so URI has no type. Maybe check your classes!", obj.toString(), IdentifiedByURI.class.toString());
												}
											}else{
												logger.warn("	Object {} does not implement Interface {}.\n This sould not happend, check your classes!!!",obj.toString(), HasConcept.class.toString());
											}
										} catch (InstantiationException e) {
											logger.error("	 Could not instanciate newVal", e);
										} catch (URISyntaxException e) {
											logger.error("	 Malformed URI", e);
										}
								}
							}else{
								//TODO deal with collections here
							}
						}else{
							logger.error("	ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Field member is not annotated this should not happen here");
						}
					}catch(IllegalAccessException e){
						logger.error("	Writemapper:getBindings Could not access Field {} Exception  : {}", field.toString(), e);
					}
					logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT finished");
					break;
					
				case LITERAL:
					try{
						//TODO fix it. not only String literals should be possible
						Object obj = field.get(object);
						if(!(obj instanceof Collection)){
							if(!bindingSet.hasBinding(fieldann.var().toString())){
								logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
								Literal literal =  vf.createLiteral(field.get(object).toString());
								logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Binding for Object {}, Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), literal});
								bindingSet.addBinding(fieldann.var().toString(), literal);
							}else{
								logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Binding is already set from ParentBindingSet");
								//TODO Set Field fix it
							}
						}else{
							//TODO deal with collections here
						}
					}catch(IllegalAccessException e){
						logger.error(" ClassAnalysis:buildBindingSet Could not access Field {} Exception  : {}", field.toString(), e);
					}
					break;

				default:
					break;
				}

				field.setAccessible(false);
			}else logger.info("Field is not annotated --> skipped");
		}
		return bindingSet;
	}
    
	/**
	 * Creates BindingSet for all other kinds of Objects.
	 * 
	 * @param object 	The object the BindingSet should be build for
	 * @param vf		The ValueFactory needed to create Values for the BindingSet
	 * @return			The created BindingSet
	 */
	public static QueryBindingSet buildBindingSet(Object object, ValueFactory vf){
		//TODO change to generic annotation
		logger.debug("ClassAnalysis:buildBindingSet with Object {}, and ValueFactory {}", object.toString(), vf.toString() );
		QueryBindingSet bindingSet = new QueryBindingSet();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields){
			logger.debug("ClassAnalysis:buildBindingSet Interating over fields : field {}", field.toString());
			winter fieldann = field.getAnnotation(winter.class);
			if (fieldann != null){
				field.setAccessible(true);
				logger.debug("ClassAnalysis:buildBindingSet fieldann {}\n for field {}",new Object[]{fieldann.toString(), field.getType().toString()});
				//TODO Check this 2 times????
				if(fieldann != null){
					switch (fieldann.type()) {
					case INTERNALOBJECT:
						try{
							Object obj = field.get(object);
							if(!(obj instanceof Collection)){
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
								Value uri = vf.createURI(((WrapsURI)obj).getURI().toString());
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Binding for Object {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
								bindingSet.addBinding(fieldann.var().toString(), uri);
							}else{
								if(obj instanceof WinterURISet) obj = ((WinterURISet)obj).map.values();
								for (Object collobj : (Collection)obj){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Collection {} implements {}", obj.toString(), obj.getClass().getInterfaces());
									Value uri = vf.createURI(((WrapsURI)collobj).getURI().toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH URI Bindings for Collection {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
									
									// that does not work only one binding per name !!!!!!!!! how to make it work ???
									bindingSet.addBinding(fieldann.var(), uri);
								}
								//TODO deal with collections here
							}
						}catch(IllegalAccessException e){
							logger.error(" ClassAnalysis:buildBindingSet Could not access Field {} Exception  : {}", field.toString(), e);
						}
						break;
						
					case EXTERNALOBJECT:
						try{
							Object obj = field.get(object);
							//if ((winter)ClassAnalysis.getAnnotation(field.getType() , winter.class) != null){
								if(!(obj instanceof Collection)){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
									Value uri = vf.createURI(((IdentifiedByURI)obj).getURI().getURI().toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Binding for Object {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
									bindingSet.addBinding(fieldann.var().toString(), uri);
								}else{
									if(obj instanceof WinterObjectSet) obj = ((WinterObjectSet)obj).map.values();
									for (Object collobj : (Collection)obj){
										logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Collection {} implements {}", obj.toString(), obj.getClass().getInterfaces());
										Value uri = vf.createURI(((IdentifiedByURI)collobj).getURI().getURI().toString());
										logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Bindings for Collection {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), uri});
										
										// that does not work only one binding per name !!!!!!!!! how to make it work ???
										bindingSet.addBinding(fieldann.var(), uri);
									}
								}
							//}else{
							//	logger.error("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH OBJECT Field member is not annotated this should not happen here");
							//}
						}catch(IllegalAccessException e){
							logger.error(" Writemapper:getBindings Could not access Field {} Exception  : {}", field.toString(), e);
						}
						break;
						
					case LITERAL:
						try{
							//TODO fix it. not only String literals should be possible
							Object obj = field.get(object);
							logger.info("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
							if(!(obj instanceof Collection)){
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Object {} implements {}", obj.toString(), obj.getClass().getInterfaces());
								Value literal =  vf.createLiteral(obj.toString());
								logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Binding for Object {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), literal});
								bindingSet.addBinding(fieldann.var(), literal);
							}else{
								if(obj instanceof WinterLiteralSet) obj = ((WinterLiteralSet)obj).set;
								for(Object collobj : (Collection)obj){
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Collection {} implements {}", obj.toString(), obj.getClass().getInterfaces());
									Value literal = vf.createLiteral(collobj.toString());
									logger.debug("ClassAnalysis:buildBindingSet -- OBJECTTYPESWITCH LITERAL Bindings for Collection {},\n Variable {} with Value {}", new Object[]{obj.toString(), fieldann.var().toString(), literal});
									
									// that does not work only one binding per name !!!!!!!!! how to make it work ???
									bindingSet.addBinding(fieldann.var(), literal);
								}
								//TODO deal with collections here
							}
						}catch(IllegalAccessException e){
							logger.error(" ClassAnalysis:buildBindingSet Could not access Field {} Exception  : {}", field.toString(), e);
						}
						break;
	
					default:
						break;
					}
				}else{
					logger.error("");
				}
				field.setAccessible(false);
			}
		}
		logger.debug("BindingSet {}", bindingSet.toString());
		return bindingSet;
	}

}
