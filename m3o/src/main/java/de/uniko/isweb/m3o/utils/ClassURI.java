package de.uniko.isweb.m3o.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import net.java.rdf.util.WrapsURI;

public class ClassURI implements WrapsURI{
	
	URI conceptURI = null;
	
	public ClassURI(){
		try {
			conceptURI = new URI("http://".concat(UUID.randomUUID().toString()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClassURI(URI uri){
		this.conceptURI = uri;
	}
	
	public ClassURI(String uri){
		try {
			conceptURI = new URI(uri);	
		} catch (URISyntaxException e) {
			// TODO: handle exception
		}
	}
	
	public URI getURI() {
		return conceptURI;
	}

	public void setURI(URI conceptURI) {
		this.conceptURI = conceptURI;	
	}

}
