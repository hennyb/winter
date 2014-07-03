package net.java.rdf.winter;

/**
 * @author schegi
 *
 */
public interface Mapping {
	
	/**	
	 * Adds one object
	 * 
	 * @param <T>
	 * @param object	The object to add
	 * @return
	 */
	public <T> Boolean addObject(T object);
	
	/**
	 * Get an object by its unique id 
	 * 
	 * @param <T>
	 * @param uri		The uri of the object you want to get from the repository
	 * @return
	 */
	public <T> T getObjectByUri(java.net.URI uri, Class<T> clazz);
	
	/**
	 * Get all objects of given type <code>Class<T> clazz</code>
	 * 
	 * @param <T>		A collection of objects you want to have
	 * @param clazz		The type of the objects you what to have
	 * @param uri
	 * @return
	 */
	public <T> T getAllObjectsOfType(Class<T> clazz);
	
	/**
	 * Remove an object by its unique id
	 * 
	 * @param uri		The uri of the object you want to remove
	 * @return
	 */
	public <T> T removeObjectByUri(java.net.URI uri);
	
	/**
	 * Remove all objects of a given type code>Class<T> clazz</code>
	 * 
	 * @param clazz		The class of the objects you want to remove
	 * @return
	 */
	public <T> T removeAllObjectsOfType(Class<T> clazz);

}
