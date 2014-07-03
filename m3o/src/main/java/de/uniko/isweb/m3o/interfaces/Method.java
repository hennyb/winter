package de.uniko.isweb.m3o.interfaces;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;


/**
 * @author schegi
 *
 */
@winter(type = winter.Type.EXTERNALOBJECT, query = "?Method <rdf:type> ?MethodType")
public interface Method extends IdentifiedByURI, HasConcept {

}
