package de.uniko.isweb.m3o.interfaces;

import net.java.rdf.annotations.winter;
import net.java.rdf.util.HasConcept;
import net.java.rdf.util.IdentifiedByURI;


/**
 * @author schegi
 *
 */
@winter(type = winter.Type.EXTERNALOBJECT, query = "?Region <rdf:type> ?RegionType")
public interface Region extends IdentifiedByURI, HasConcept {

}
