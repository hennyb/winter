package de.uniko.isweb.m3o.interfaces;

import net.java.rdf.annotations.winter;


/**
 * @author schegi
 *
 */
@winter(type = winter.Type.EXTERNALOBJECT, query = "?InformationRealization <rdf:type> ?InformationRealizationType")
public interface InformationRealization extends InformationEntity{

}
