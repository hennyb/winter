package net.java.rdf.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE,CONSTRUCTOR, METHOD, FIELD, PARAMETER})
public @interface winter {
	/**
	 * Enumeration over possible occuring types (not meant in java speech) of annotated entities
	 * @author schegi
	 *
	 */
	public enum Type {
		/**
		 * CLASS ANNOTATION
		 * An annotated pattern. Patterns have no unique URI, they fully qualified by their members. 
		 * Patterns have usually a longer query, describing all members the pattern consist of.  
		 */
		PATTERN, 
		/**
		 * CLASS ANNOTATION
		 * An annotated class. Could be both a single stand alone element with an unique URI and a 
		 * member of an pattern for instance. Usually this annotation uses only the var part to 
		 * describe what to bind with the URI of the underlying object. URI, type description e.t.c
		 * should not be defined in this annotation, instead they should be defined in the underlying
		 * object.  
		 */
		EXTERNALOBJECT, 
		/**
		 * MEMBER ANNOTATION
		 * An annotated member of an CLASS or PATTERN. The annotated variable only represents an URI.
		 * The member should implement the IdentifiedBYURI interface to enable winter to get the URI back.
		 * This kind of annotation appears usually in two different ways. With an query for additional
		 * describtion of what the URI represents (e.g you want to describe the type of the THING 
		 * represented by this URI) or without an query part then only the var part is set to describe
		 * what to bind with this URI. 
		 */
		INTERNALOBJECT, 
		/**
		 * MEMBER ANNOTATION
		 * An annotated member of an CLASS or PATTERN. The annotated variable represents an Literal.
		 * It could consist of the var statement for binding information and can hold an additional 
		 * type declaration. 
		 */
		LITERAL, 
		/**
		 * MEMBER ANNOTATION
		 * The MAPPING is needed for optional PATTERNS incrementing other PATTERNS and describes the
		 * mapping between query variables in the two PATTERNS.
		 */
		MAPPING}
	// the query, mainly for class and type declarations
	String query() default "";
	// the var string to bind to
	String var() default "";
	// the namespace of the variable
	String namepsace() default "";
	// mapping arrays, should have the same size. mappings should have the same possition in src and dst 
	String[] src() default "";
	String[] dst() default "";
	// the variable reuse array
	String[] reuse() default "";
	// the type of the annotated entity
	Type type() default Type.INTERNALOBJECT;
}
