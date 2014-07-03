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
package net.java.rdf.winter;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.ClassAnalysis;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;

import java.lang.reflect.*;
import java.util.*;

import org.openrdf.model.ValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The core mapper implementation 
 * 
 * @author Stefan Scheglmann
 */
public class Mapper implements Mapping{
	
	protected static transient Logger logger = LoggerFactory.getLogger(Mapper.class.toString());

	public HashMap<Object, HashMap<Field, Object>> referenceMap = new HashMap<Object, HashMap<Field,Object>>();
	Init init;
	java.net.URI defaultgraph;
	Queue<Object> list = null;
	Thread objectParser = new Thread();

	SesameWriteMapper writemapper;
	SesameReadMapper readmapper;
	ValueFactory vf;
	

	
	public Mapper(Init init, java.net.URI graph) {
		logger.info("Instanciating Mapper {}",  UUID.randomUUID().toString());
		this.init = init;
		this.defaultgraph = graph;
		
		list  = new LinkedList<Object>(); 

		readmapper = new SesameReadMapper(init, defaultgraph, this);
		writemapper = new SesameWriteMapper(init, defaultgraph, readmapper);
		
		this.vf = init.getValueFactory();
	    logger.debug("Initialising Mapper with default graph : {} ", graph.toString());
	}
	
	public java.net.URI getGraph(){
		return defaultgraph;
	}
	
	public void setGraph(java.net.URI graph){
		this.defaultgraph = graph;
	}
	
	public <T> Boolean addObject(T object){
		logger.debug("addObject try to add Object {}", object.toString());
		if (object instanceof RdfSerialisable){
			((RdfSerialisable)object).setSesameReadMapper(readmapper);
			((RdfSerialisable)object).setSesameWriteMapper(writemapper);
			ObjectParser objectParser = new ObjectParser(list, object);
			ObjectSerialiser objectSerialiser = new ObjectSerialiser(list);
			logger.debug("addObject Object added!");
			return true;
		}else{
			logger.error("addObject failed to add Object not RdfSerializable!");
			return false;
		}

	}
	
	public Object getObjectByUri(java.net.URI uri, Class clazz){
		//TODO check what happens if another object is referenced, should work but check it
		logger.debug("reading Object of type {} with IndividualURI {}", clazz.getName().toString(), uri.toString());
		Object object = null;
		java.net.URI uriType = readmapper.getConceptURIForIndividualURI(uri);
		try {
			//TODO throw exception here
			object = clazz.newInstance();
			if (object == null) {
				logger.error("Object could not be read");
				return null;
			}
			if (!(object instanceof IdentifiedByURI)) {
				logger.error("Object could not be read, ObjectClass and thus does not realizes");
				return null;
			}
			if (!(object instanceof RdfSerialisable)) {
				logger.error("Object could not be read, ObjectClass and thus does not realizes RDFSerializable");
				return null;
			}
			if (!((IdentifiedByURI)object).getURI().equals(uriType)) {
				logger.error("Object could not be read, Objecttype {} does not correspondant to URIType {}",((IdentifiedByURI)object).getURI().toString(), uriType);
				return null;
			}
		} catch (InstantiationException e) {
			logger.error("Could not instanciate Object: ", e);
		} catch (IllegalAccessException e) {
			logger.error("Could not instanciate Object: ", e);
		}
		if((object != null) && (object instanceof IdentifiedByURI)){
			((IdentifiedByURI)object).setURI(uri);
			((RdfSerialisable)object).setSesameReadMapper(readmapper);
			((RdfSerialisable)object).setSesameWriteMapper(writemapper);
			logger.debug("Object successfully instantiated");
		}
		return object;
	}
	
	public Set<Object> getAllObjectsOfType(Class clazz){
		java.net.URI concept = null;
		HashSet<Object> objects = new HashSet<Object>();
		try {
			Object object = clazz.newInstance();
			if (!(object instanceof HasConcept)) {
				logger.error("Objects could not be read, ObjectClass does not implements HasConcept");
				return null;
			}
			concept = ((HasConcept)object).getConcept().getURI();
			if(concept == null){
				logger.error("Objects could not be read, concept URI is empty");
				return null;
			}
		} catch (InstantiationException e) {
			logger.error("Could not instanciate Object: ", e);
		} catch (IllegalAccessException e) {
			logger.error("Could not instanciate Object: ", e);
		}
		Set<java.net.URI> uris = readmapper.getAllIndividualURIsForConcept(concept);
		//TODO get all IndividualURIs of specified type, add method to readmapper
		for (java.net.URI uri : uris){
			objects.add(getObjectByUri(uri, clazz));
		}
		return objects;
	}
	
	public Set<Object> getAllPatternsOfType(Class clazz){
		HashSet<Object> patterns = new HashSet<Object>();
		if (!((winter)clazz.getAnnotation(winter.class)).type().equals(winter.Type.PATTERN)){
			logger.error("Pattern {} is not of type {} could not retrieve any patterns", clazz.getName(), winter.Type.PATTERN);
		}
		
		return patterns;
	}
	
	public Set<Object> getAllPatternsOfTypeForObjects(Class clazz, Set<Object> objects){
		Set<Object> patterns = new HashSet<Object>();
		if (objects.isEmpty()){
			logger.warn("Object Set is empty will not return anything");
			return patterns;
		}
		for (Object object : objects){
			patterns.add(getPatternOfTypeForObject(clazz, object));
		}
		return patterns;
	}
	
	public Object getPatternOfTypeForObject(Class clazz, Object object) {
		winter annotation = object.getClass().getAnnotation(winter.class);
		if (annotation == null && !annotation.type().equals(winter.Type.EXTERNALOBJECT)){
			logger.error("Could not read patterns object {} is not of type {}", object.getClass().getName(), winter.Type.EXTERNALOBJECT.toString());
			return null;
		}
		if (!((winter)clazz.getAnnotation(winter.class)).type().equals(winter.Type.PATTERN)){
			logger.error("Pattern {} is not of type {} could not retrieve any patterns", clazz.getName(), winter.Type.PATTERN);
			return null;
		}
		Object pattern = readmapper.getPATTERN(clazz, object);
		return pattern;
	}
	
	public Object removeObjectByUri(java.net.URI uri){	
		return null;
	}
	
	public <T> T removeAllObjectsOfType(Class<T> clazz){	
		return null;
	}
	
	public  <T> T removeObject(Object object){
		if(((RdfSerialisable)object).getBindingSet() == null){
			logger.info("Object {} object not mapped", object.toString());		
		}else{
			if(((RdfSerialisable)object).getBindingSet().size() == 0){
				logger.info("Object {} object not mapped", object.toString());
			}
		
		}
		return null;
	}
	
	//TODO Stop the threads when rdy 
	/**
	 * The object Parser, runs in its own thread. Parses an object and adds all annotated subobject to objectQueue
	 * 
	 * @author schegi
	 *
	 */
	class ObjectParser{
		
		Object object = null;
		private Set<Object> set;
		private LinkedList<Object> processedList;
				
		/**
		 * @param <T> Object Type
		 * @param object Object to be parsed
		 * @param objectQueue The ObjectQueue the <code>ObjectSerialiser<\code> reads from
		 */
		public <T>ObjectParser(Queue<Object> list, Object object){;
			processedList = new LinkedList<Object>();
			processObject(object);
//			logger.debug("Mapper:ObjectParser New ObjectParser Thread created for {} with queue {} \n", new Object[]{object.getClass().getName(), objectQueue});
		}
		
//        public void run() {
//        	while(true)
////        	logger.debug("Mapper:ObjectParser:run Running Thread for object : {}", object.getClass().getName());
//        	processObject(parseQueue.poll());
//        }

        /**
         * Process incoming object and add all its annotated subobjects to objectQueue 
         * 
         * @param object The object to process
         * @return True if success
         */
        private boolean processObject(Object object) {
//        	logger.debug("Mapper:ObjectParser:processObject for {}", object.getClass().getName());
        	
        	//add object to objectQueue and processedList
        	list.add(object);
        	processedList.add(object);
        	
        	// get all fields of object
        	Field[] fields = object.getClass().getDeclaredFields();
        	logger.debug("Mapper:ObjectParser:processObject Object {} has {} fields", object.getClass().getName(), fields.length);
        	int i = 1;
        	
        	// For all fields of class do
        	for (Field field : fields){
        		logger.debug("Mapper:ObjectParser:processObject Process field {} : {}",i++ ,field.toGenericString());
        		Object memberObject = null;
        		field.setAccessible(true);
        		try {
					memberObject = field.get(object);
				} catch (IllegalAccessException e) {
					logger.error("Mapper:ObjectParser:processObject Could not get Object from Field : Exception : {}", e);
				}
        		field.setAccessible(false);
        		// If field is annotated
				if(memberObject != null){
	        		if (field.getAnnotation(winter.class) != null){
	        			// If field is Object
	        			if(memberObject instanceof AbstractCollection){
	        				if(!((Collection)memberObject).isEmpty()){
		        				if (field.getAnnotation(winter.class).type() == winter.Type.MAPPING || field.getAnnotation(winter.class).type() == winter.Type.EXTERNALOBJECT){
				        			for (Object obj : (Collection)memberObject){
				        				// TODO test it, does it really work??
				        				logger.debug("Mapper:ObjectParser:processObject Field {} is Collection {}", field.getName(), memberObject.toString());
			        					if (!processedList.contains(obj)){
			        						logger.debug("calling processObject");
			        						processObject(obj);
		        						}
				        			}
		        				}
	        				}
	        			}else if(field.getAnnotation(winter.class).type() == winter.Type.MAPPING || field.getAnnotation(winter.class).type() == winter.Type.EXTERNALOBJECT){
	        				logger.debug("Mapper:ObjectParser:processObject Field {} is not Primitive or String or Collection its {}", field.getName(), memberObject.toString());
	    					if (!processedList.contains(memberObject)){
	    						logger.debug("calling processObject");
	    						processObject(memberObject);
	    					}
		        		}
	        		}
	        	}
        	}
        return true;
        }
    };
    
    /**
     * The ObjectSerialiser polls the <code>objectQueue</code> the <code>ObjectParser</code> writes to
     * 
     * @author schegi
     *
     */
    // implements Runnable 
    class ObjectSerialiser{
    	
    	Object object = null;
    	/**
    	 * @param objectQueue The <code>objectQueue</code>
    	 */
    	public ObjectSerialiser(Queue<Object> list) {
			logger.debug("Mapper:ObjectSerialiser:ObjectSerialiser New ObjectSerialiser Thread created for queue {}", list.toString());
			// TODO check if parser is still running
			while (!list.isEmpty()) {
    			object = list.poll();
    			if (object != null){
        			logger.info("Mapper:ObjectSerialiser:run Object {} POLLED", object.toString());
        			processObject(object);
        			logger.debug("Object processed");
    			}
			}
    	}
    	
//    	public void run() {
//    		while (true) {
//    			object = objectQueue.poll();
//    			if (object != null){
//        			logger.debug("Mapper:ObjectSerialiser:run Object {} polled", object.toString());
//        			processObject(object);
//    			}
//			}
//    	}
    	
    	/**
    	 * serializes object polled from the <code>objectQueue</code>
    	 * 
    	 * @param object The current object
    	 * @return True if success
    	 */
    	private boolean processObject(Object object) {
    		logger.debug("Mapper:ObjectSerialiser:processObject processing object {}", object.toString());
    		boolean flag = false;
    		
    		// get the object annotation even from a superclass
			logger.debug("Field {} of type {}",object.toString(), object.getClass().toString());
    		winter objann = (winter)ClassAnalysis.getAnnotation(object.getClass(), winter.class);
    		logger.debug("Mapper:ObjectSerialiser:processObject Annotation {}", objann);
    		if (objann != null){
        		//Serialize object
        		//only switch over CLASS, PATTERN. all other types should only be used as member annotation.
				logger.debug("Mapper:ObjectSerialiser:processObject-- SWITCHING {}\n {}\n",new Object[]{objann.type(), objann});
	    		switch (objann.type()) {
	    		case PATTERN:
					logger.debug("Mapper:ObjectSerialiser:processObject -- OBJECTTYPESWITCH PATTERN object {} is of type {}", object.toString(), winter.Type.PATTERN);
					logger.debug("Mapper:ObjectSerialiser:processObject calling setObject with parameters {}, \n {}",object.toString() , objann);
					if (writemapper.setPattern(objann, object)){
						flag = writemapper.mapTheFields(object, readmapper, writemapper);
					}
					break;
				
	    		case EXTERNALOBJECT:
					logger.debug("Mapper:ObjectSerialiser:processObject -- OBJECTTYPESWITCH OBJECT object {} is of type {}", object.toString(), winter.Type.EXTERNALOBJECT);
		    		logger.debug("Mapper:ObjectSerialiser:processObject calling setObject with parameters {}, \n {}",object.toString() , objann);	
		    		if (writemapper.setObject(objann, object, null, objann.var())){
						flag = writemapper.mapTheFields(object, readmapper, writemapper);
					}
					break;
	
				default:
					logger.debug("Mapper:ObjectSerialiser:processObject -- OBJECTTYPESWITCH object {} is of type {} you should never end up here", object.toString(), objann.type().toString());
					break;
				}
    		} 
    		logger.debug("Mapper:ObjectSerialiser:processObject processing object {} finished \n \n \n \n", object.toString());
       		return flag;
    	}
    	
    	/**
    	 * Serialize fields of the object
    	 * 
    	 * @param object
    	 * @return success
    	 */
    };
}
