package net.java.rdf.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Additional TypeInfo annotation for URI/LITERAL Collections 
 * 
 * @author schegi
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE,CONSTRUCTOR, METHOD, FIELD, PARAMETER})
public @interface typeinfo {
	// the query, mainly for class and type declarations
	String query() default "";
}
