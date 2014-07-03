package net.java.rdf.util;

import java.net.URI;


/**
 * Basic Indetifier Interface
 * 
 * @author schegi
 *
 */
public interface IdentifiedByURI {
	
	public WrapsURI getURI();
	
	public void setURI(URI uri);
	
}
