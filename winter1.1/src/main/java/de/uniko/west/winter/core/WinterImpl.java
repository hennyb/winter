/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.

*/

/**
 * 
 */
package de.uniko.west.winter.core;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;
import de.uniko.west.winter.core.interfaces.WinterSerializer;
import de.uniko.west.winter.core.serializing.SerializingManager;
import de.uniko.west.winter.exceptions.WinterException;
import de.uniko.west.winter.infostructure.FieldNode;
import de.uniko.west.winter.infostructure.ObjectNode;
import de.uniko.west.winter.utils.ClassReflection;
import de.uniko.west.winter.utils.FieldUpdateResultListener;
import de.uniko.west.winter.utils.QueryTask;
import de.uniko.west.winter.utils.QueryTaskExecutor;
import de.uniko.west.winter.utils.QueryTaskFactory;
import de.uniko.west.winter.utils.interfaces.QueryResultListener;

/**
 * New main access class, facading winter functionality
 * 
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public final class WinterImpl implements Winter{
	
	
	public static void main(String[] args) {
		Class<?> clazz = Winter.class;
		System.out.println(WinterImpl.class == clazz);
		System.out.println(Winter.class == clazz);
		System.out.println(Winter.class.equals(clazz));
		System.out.println(WinterImpl.class.isAssignableFrom(clazz));
		System.out.println(clazz.isAssignableFrom(WinterImpl.class));
	}
	
	protected static transient Logger logger = LoggerFactory.getLogger(WinterImpl.class.getSimpleName());

	private static Init init = null;

	private RDFSerializable object = null;
	private ObjectNode objectNode;
	
	private static URI defaultGraph = null;
	
	private QueryResultListener defaultResultListener = new DefaultQueryResultListenerImpl();
	
	private URI objectGraph = null;

	private static WinterImpl sharedInstance = null;
	
//	initial
	public static void init(Init init, URI defaultGraph){
		WinterImpl.init = init;
		Mapper.init(true);
		WinterImpl.defaultGraph = defaultGraph;
		
		Hashtable<URI, Class<?>> preprocessedClasses = ClassReflection.initTypeMap();
		SerializingManager.initDefaultSerializers(preprocessedClasses);
	}
	
	public static Winter getSharedInstance(){
		if (init == null){
			throw new WinterException("Winter not initialized!");
		}
		if (sharedInstance == null){
			sharedInstance = new WinterImpl(null);
		}
		return sharedInstance;
	}
	
	public void add(RDFSerializable object){
		add(new Vector<RDFSerializable>( Arrays.asList( object ) ));
	}
	
	public void add(Collection<RDFSerializable> objects){
		Vector<ObjectNode> nodes= Mapper.generateNodeStructure(objects);
		logger.info("generated InfoNodeStructure");
		for (ObjectNode node : nodes) {
			node.winter().write();
		}
	}
	
	public void deleteRek(RDFSerializable object){
		Set<RDFSerializable> sers = ClassReflection.createObjectSet(
				new HashSet<RDFSerializable>(Arrays.asList( object )));
		
		for (RDFSerializable rdfSerializable : sers) {
			rdfSerializable.winter().delete();
		}
		
	}

	public WinterImpl(RDFSerializable object) {
		this.object = object;
		objectGraph = defaultGraph;
	}

//	public void read(){
//		checkObjectNode();
//		QueryTask task = QueryTaskFactory.createQuery(
//				getObjectNode(),
//				getObjectGraph(),
//				getInit().getPrefixMap(),
//				QueryTaskFactory.TASK.SELECT,
//				new String[]{}); 
//		logger.info("generated task");
//		QueryTaskExecutor.getSharedInstance().queueTask(task);
//		QueryTaskExecutor.getSharedInstance().runTasks(
//				init.getRepositoryConnection(), dumpListener);
//	}
	
	public void readField(String fieldName){
		checkObjectNode();
		FieldNode fieldNode = getObjectNode().getFieldNodeByFieldName(fieldName);
		QueryTask task = QueryTaskFactory.createQuery(
				fieldNode,
				getObjectGraph(),
				getInit().getPrefixMap(),
				QueryTaskFactory.TASK.SELECT,
				new String[]{fieldName}); 
		QueryTaskExecutor.getSharedInstance().queueTask(task);
		QueryTaskExecutor.getSharedInstance().runTasks(
				init.repCon,
				new FieldUpdateResultListener(fieldNode) );
	}
	
	public void delete(){
		checkObjectNode();
		QueryTask task = QueryTaskFactory.createQuery(
				getObjectNode(),
				getObjectGraph(),
				getInit().getPrefixMap(),
				QueryTaskFactory.TASK.DELETE_DATA,
				new String[]{}); 
		logger.info("generated task");
		QueryTaskExecutor.getSharedInstance().queueTask(task);
		QueryTaskExecutor.getSharedInstance().runTasks(
				init.getRepositoryConnection(), defaultResultListener);
	}
	
	public void write(){
		checkObjectNode();
		QueryTask task = QueryTaskFactory.createQuery(
				getObjectNode(),
				getObjectGraph(),
				getInit().getPrefixMap(),
				QueryTaskFactory.TASK.INSERT_DATA,
				new String[]{}); 
		logger.info("generated task");
		QueryTaskExecutor.getSharedInstance().queueTask(task);
		QueryTaskExecutor.getSharedInstance().runTasks(
				init.getRepositoryConnection(), defaultResultListener);
	}
	
//	for a specific field
	public void writeField(String fieldName){
		checkObjectNode();
		QueryTask task = QueryTaskFactory.createQuery(
					getObjectNode().getFieldNodeByFieldName(fieldName),
					getObjectGraph(),
					getInit().getPrefixMap(),
					QueryTaskFactory.TASK.INSERT_DATA,
					new String[]{}); 
		QueryTaskExecutor.getSharedInstance().queueTask(task);
		QueryTaskExecutor.getSharedInstance().runTasks(init.repCon, defaultResultListener);
	}
	
	public void deleteField(String fieldName){
		checkObjectNode();
		QueryTask task = QueryTaskFactory.createQuery(
				getObjectNode().getFieldNodeByFieldName(fieldName),
				getObjectGraph(),
				getInit().getPrefixMap(),
				QueryTaskFactory.TASK.DELETE_DATA,
				new String[]{}); 
		QueryTaskExecutor.getSharedInstance().queueTask(task);
		QueryTaskExecutor.getSharedInstance().runTasks(init.repCon, defaultResultListener);
	}

	/**
	 * @param objectNode the objectInfo to set
	 */
	public void setObjectNode(ObjectNode objectNode) {
		this.objectNode = objectNode;
	}

	/**
	 * @return the objectInfo
	 */
	public ObjectNode getObjectNode() {
		return objectNode;
	}
	
	/**
	 * @param objectGraph the objectGraph to set
	 */
	public void setObjectGraph(URI objectGraph) {
		this.objectGraph = objectGraph;
	}

	/**
	 * @return
	 */
	public URI getObjectGraph() {
		return objectGraph;
	}

	/**
	 * @return
	 */
	static Init getInit() {
		return init;
	}
	
	private void checkObjectNode(){
		if (objectNode == null){
			throw new WinterException("No objectNode present. Node operations can not be performed on sharedInstance.");
		}
	}

	@Override
	public void registerSerializer(WinterSerializer serializer, String fieldName) {
		FieldNode node = getObjectNode().getFieldNodeByFieldName(fieldName);
		SerializingManager.registerCustomSerializer(serializer, node);
	}
	
	
//	public void replace(ObjectInfo oldObjectInfo, ObjectInfo newObjectInfo){
//	}
	
}
