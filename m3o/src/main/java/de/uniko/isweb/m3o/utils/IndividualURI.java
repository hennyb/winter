package de.uniko.isweb.m3o.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import net.java.rdf.util.WrapsURI;

public class IndividualURI implements WrapsURI{
	
	URI individualURI = null;
	
	public IndividualURI(){
		try {
			individualURI = new URI("http://".concat(UUID.randomUUID().toString()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IndividualURI(URI uri){
		this.individualURI = uri;
	}
	
	public IndividualURI(String uri){
		try {
			individualURI = new URI(uri);	
		} catch (URISyntaxException e) {
			// TODO: handle exception
		}
	}

	public URI getURI() {
		return individualURI;
	}

	public void setURI(URI individualURI) {
		this.individualURI = individualURI;
	}
	
}
