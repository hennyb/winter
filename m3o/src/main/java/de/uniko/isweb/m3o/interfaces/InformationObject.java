package de.uniko.isweb.m3o.interfaces;

import net.java.rdf.annotations.winter;

/**
 * @author schegi
 *
 */

@winter(type = winter.Type.EXTERNALOBJECT, query = "?InformationObject <rdf:type> ?InformationObjectType")
public interface InformationObject extends InformationEntity {
	
}
