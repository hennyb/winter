package net.java.rdf.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	protected static transient Logger logger = LoggerFactory.getLogger(Utils.class.getName());
	
	public static URI createURI(String prefix, String var){
		URI uri = null;
		try {
			uri = new URI("http://".concat(prefix).concat(var).concat("#").concat(UUID.randomUUID().toString()));
			logger.debug("URI created with {} for {} : {}",new Object[]{prefix, var, uri.toString()});
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
	}
}
