package de.uniko.isweb.m3o.interfaces;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;

@winter(type = winter.Type.EXTERNALOBJECT, query = "?InformationEntity <rdf:type> ?InformationEntityType")
public interface InformationEntity extends IdentifiedByURI, HasConcept {

}
