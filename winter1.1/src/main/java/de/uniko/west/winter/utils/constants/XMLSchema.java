/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.uniko.west.winter.utils.constants;

/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2007.
 *
 * Licensed under the Aduna BSD-style license.
 */

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Defines constants for the standard XML Schema datatypes.
 */
public class XMLSchema {

    /*
     * The XML Schema namespace
     */

    /** The XML Schema namespace (<tt>http://www.w3.org/2001/XMLSchema#</tt>). */
    public static final String NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    /*
     * Primitive datatypes
     */

    /** <tt>http://www.w3.org/2001/XMLSchema#duration</tt> */
    public final static URI DURATION;

    /** <tt>http://www.w3.org/2001/XMLSchema#dateTime</tt> */
    public final static URI DATETIME;

    /** <tt>http://www.w3.org/2001/XMLSchema#time</tt> */
    public final static URI TIME;

    /** <tt>http://www.w3.org/2001/XMLSchema#date</tt> */
    public final static URI DATE;

    /** <tt>http://www.w3.org/2001/XMLSchema#gYearMonth</tt> */
    public final static URI GYEARMONTH;

    /** <tt>http://www.w3.org/2001/XMLSchema#gYear</tt> */
    public final static URI GYEAR;

    /** <tt>http://www.w3.org/2001/XMLSchema#gMonthDay</tt> */
    public final static URI GMONTHDAY;

    /** <tt>http://www.w3.org/2001/XMLSchema#gDay</tt> */
    public final static URI GDAY;

    /** <tt>http://www.w3.org/2001/XMLSchema#gMonth</tt> */
    public final static URI GMONTH;

    /** <tt>http://www.w3.org/2001/XMLSchema#string</tt> */
    public final static URI STRING;

    /** <tt>http://www.w3.org/2001/XMLSchema#boolean</tt> */
    public final static URI BOOLEAN;

    /** <tt>http://www.w3.org/2001/XMLSchema#base64Binary</tt> */
    public final static URI BASE64BINARY;

    /** <tt>http://www.w3.org/2001/XMLSchema#hexBinary</tt> */
    public final static URI HEXBINARY;

    /** <tt>http://www.w3.org/2001/XMLSchema#float</tt> */
    public final static URI FLOAT;

    /** <tt>http://www.w3.org/2001/XMLSchema#decimal</tt> */
    public final static URI DECIMAL;

    /** <tt>http://www.w3.org/2001/XMLSchema#double</tt> */
    public final static URI DOUBLE;

    /** <tt>http://www.w3.org/2001/XMLSchema#anyURI</tt> */
    public final static URI ANYURI;

    /** <tt>http://www.w3.org/2001/XMLSchema#QName</tt> */
    public final static URI QNAME;

    /** <tt>http://www.w3.org/2001/XMLSchema#NOTATION</tt> */
    public final static URI NOTATION;

    /*
     * Derived datatypes
     */

    /** <tt>http://www.w3.org/2001/XMLSchema#normalizedString</tt> */
    public final static URI NORMALIZEDSTRING;

    /** <tt>http://www.w3.org/2001/XMLSchema#token</tt> */
    public final static URI TOKEN;

    /** <tt>http://www.w3.org/2001/XMLSchema#language</tt> */
    public final static URI LANGUAGE;

    /** <tt>http://www.w3.org/2001/XMLSchema#NMTOKEN</tt> */
    public final static URI NMTOKEN;

    /** <tt>http://www.w3.org/2001/XMLSchema#NMTOKENS</tt> */
    public final static URI NMTOKENS;

    /** <tt>http://www.w3.org/2001/XMLSchema#Name</tt> */
    public final static URI NAME;

    /** <tt>http://www.w3.org/2001/XMLSchema#NCName</tt> */
    public final static URI NCNAME;

    /** <tt>http://www.w3.org/2001/XMLSchema#ID</tt> */
    public final static URI ID;

    /** <tt>http://www.w3.org/2001/XMLSchema#IDREF</tt> */
    public final static URI IDREF;

    /** <tt>http://www.w3.org/2001/XMLSchema#IDREFS</tt> */
    public final static URI IDREFS;

    /** <tt>http://www.w3.org/2001/XMLSchema#ENTITY</tt> */
    public final static URI ENTITY;

    /** <tt>http://www.w3.org/2001/XMLSchema#ENTITIES</tt> */
    public final static URI ENTITIES;

    /** <tt>http://www.w3.org/2001/XMLSchema#integer</tt> */
    public final static URI INTEGER;

    /** <tt>http://www.w3.org/2001/XMLSchema#long</tt> */
    public final static URI LONG;

    /** <tt>http://www.w3.org/2001/XMLSchema#int</tt> */
    public final static URI INT;

    /** <tt>http://www.w3.org/2001/XMLSchema#short</tt> */
    public final static URI SHORT;

    /** <tt>http://www.w3.org/2001/XMLSchema#byte</tt> */
    public final static URI BYTE;

    /** <tt>http://www.w3.org/2001/XMLSchema#nonPositiveInteger</tt> */
    public final static URI NON_POSITIVE_INTEGER;

    /** <tt>http://www.w3.org/2001/XMLSchema#negativeInteger</tt> */
    public final static URI NEGATIVE_INTEGER;

    /** <tt>http://www.w3.org/2001/XMLSchema#nonNegativeInteger</tt> */
    public final static URI NON_NEGATIVE_INTEGER;

    /** <tt>http://www.w3.org/2001/XMLSchema#positiveInteger</tt> */
    public final static URI POSITIVE_INTEGER;

    /** <tt>http://www.w3.org/2001/XMLSchema#unsignedLong</tt> */
    public final static URI UNSIGNED_LONG;

    /** <tt>http://www.w3.org/2001/XMLSchema#unsignedInt</tt> */
    public final static URI UNSIGNED_INT;

    /** <tt>http://www.w3.org/2001/XMLSchema#unsignedShort</tt> */
    public final static URI UNSIGNED_SHORT;

    /** <tt>http://www.w3.org/2001/XMLSchema#unsignedByte</tt> */
    public final static URI UNSIGNED_BYTE;

    static {
        
        DURATION = assignURI(XMLSchema.NAMESPACE, "duration");
        DATETIME = assignURI(XMLSchema.NAMESPACE, "dateTime");
        TIME = assignURI(XMLSchema.NAMESPACE, "time");
        DATE = assignURI(XMLSchema.NAMESPACE, "date");
        GYEARMONTH = assignURI(XMLSchema.NAMESPACE, "gYearMonth");
        GYEAR = assignURI(XMLSchema.NAMESPACE, "gYear");
        GMONTHDAY = assignURI(XMLSchema.NAMESPACE, "gMonthDay");
        GDAY = assignURI(XMLSchema.NAMESPACE, "gDay");
        GMONTH = assignURI(XMLSchema.NAMESPACE, "gMonth");
        STRING = assignURI(XMLSchema.NAMESPACE, "string");
        BOOLEAN = assignURI(XMLSchema.NAMESPACE, "boolean");
        BASE64BINARY = assignURI(XMLSchema.NAMESPACE, "base64Binary");
        HEXBINARY = assignURI(XMLSchema.NAMESPACE, "hexBinary");
        FLOAT = assignURI(XMLSchema.NAMESPACE, "float");
        DECIMAL = assignURI(XMLSchema.NAMESPACE, "decimal");
        DOUBLE = assignURI(XMLSchema.NAMESPACE, "double");
        ANYURI = assignURI(XMLSchema.NAMESPACE, "anyURI");
        QNAME = assignURI(XMLSchema.NAMESPACE, "QName");
        NOTATION = assignURI(XMLSchema.NAMESPACE, "NOTATION");
        NORMALIZEDSTRING = assignURI(XMLSchema.NAMESPACE, "normalizedString");
        TOKEN = assignURI(XMLSchema.NAMESPACE, "token");
        LANGUAGE = assignURI(XMLSchema.NAMESPACE, "language");
        NMTOKEN = assignURI(XMLSchema.NAMESPACE, "NMTOKEN");
        NMTOKENS = assignURI(XMLSchema.NAMESPACE, "NMTOKENS");
        NAME = assignURI(XMLSchema.NAMESPACE, "Name");
        NCNAME = assignURI(XMLSchema.NAMESPACE, "NCName");
        ID = assignURI(XMLSchema.NAMESPACE, "ID");
        IDREF = assignURI(XMLSchema.NAMESPACE, "IDREF");
        IDREFS = assignURI(XMLSchema.NAMESPACE, "IDREFS");
        ENTITY = assignURI(XMLSchema.NAMESPACE, "ENTITY");
        ENTITIES = assignURI(XMLSchema.NAMESPACE, "ENTITIES");
        INTEGER = assignURI(XMLSchema.NAMESPACE, "integer");
        LONG = assignURI(XMLSchema.NAMESPACE, "long");
        INT = assignURI(XMLSchema.NAMESPACE, "int");
        SHORT = assignURI(XMLSchema.NAMESPACE, "short");
        BYTE = assignURI(XMLSchema.NAMESPACE, "byte");
        NON_POSITIVE_INTEGER = assignURI(XMLSchema.NAMESPACE, "nonPositiveInteger");
        NEGATIVE_INTEGER = assignURI(XMLSchema.NAMESPACE, "negativeInteger");
        NON_NEGATIVE_INTEGER = assignURI(XMLSchema.NAMESPACE, "nonNegativeInteger");
        POSITIVE_INTEGER = assignURI(XMLSchema.NAMESPACE, "positiveInteger");
        UNSIGNED_LONG = assignURI(XMLSchema.NAMESPACE, "unsignedLong");
        UNSIGNED_INT = assignURI(XMLSchema.NAMESPACE, "unsignedInt");
        UNSIGNED_SHORT = assignURI(XMLSchema.NAMESPACE, "unsignedShort");
        UNSIGNED_BYTE = assignURI(XMLSchema.NAMESPACE, "unsignedByte");

    }
    
    public static URI assignURI (String namespace, String uriString){
    	URI uri = null;
    	try {
    		 uri = new URI(namespace+uriString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (uri != null) return uri;
		return null;
    }
}

