package net.java.rdf.winter;


import java.net.URI;
import java.net.URISyntaxException;

import org.openrdf.sail.*;
import org.openrdf.sail.memory.MemoryStoreConnection;

import org.openrdf.model.Statement;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.trig.TriGWriter;

import de.uniko.isweb.m3o.implementations.EntityImpl;
import de.uniko.isweb.m3o.implementations.InformationEntityImpl;
import de.uniko.isweb.m3o.implementations.InformationObjectImpl;
import de.uniko.isweb.m3o.implementations.InformationRealizationImpl;

import de.uniko.isweb.m3o.patterns.AnnotationPattern;
import de.uniko.isweb.m3o.patterns.DecompositionPattern;
import de.uniko.isweb.m3o.patterns.InformationRealizationPattern;
import de.uniko.isweb.m3o.utils.IndividualURI;

import net.java.rdf.winter.SesameMemorySailInit;

public class sommertest {

	public static final String ns = "http://www.test.de/";	
	
	/**
	 * @param args	 */
	public static void main(String[] args) {
		SesameMemorySailInit init = null;
		Mapper mppr = null;
		try {
			init = new SesameMemorySailInit();
		} catch (Error e) {
			e.printStackTrace();
		}
		RepositoryConnection con = init.getConnection();
        SailConnectionListener listener = new SailConnectionListener() {
        	public void statementAdded(Statement st) {
//        		System.out.println("add: "+st);
        	}
        	public void statementRemoved(Statement st) {
//        		System.out.println("remove: "+st);
        	}
		};
		// Cool casting action
		((MemoryStoreConnection)((SailRepositoryConnection)con).getSailConnection()).addConnectionListener(listener);
		
		try {
			mppr = new Mapper(init, new URI("http://www.defaultgraph.de"));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Information Realization
		InformationRealizationPattern irp = new InformationRealizationPattern();
		//Information Object
		InformationObjectImpl imageIO = new InformationObjectImpl( new IndividualURI( "http://example.com/informationObject" ) );
		irp.setInformationObject(imageIO);
		//First Information Realization 
		InformationRealizationImpl imageRealization1 = new InformationRealizationImpl(new IndividualURI("http://example.com/first_realization"));
		irp.addInformationRealization(imageRealization1);
		//Second Information Realization
		InformationRealizationImpl imageRealization2 = new InformationRealizationImpl( new IndividualURI("http://example.com/second_realization"));
		irp.addInformationRealization(imageRealization2);
		// add information realization pattern to the triple store
		mppr.addObject( irp );
		
		// Annotation Pattern
		AnnotationPattern ap = new AnnotationPattern();
		// Information Entity to be annotated
		ap.setInformationEntity( imageIO );
		// First Annotation
		ap.addAnnotation(new EntityImpl( new IndividualURI( "http://example.com/first_Annotation" )), null, null);
		// Second Annotation
		ap.addAnnotation(new EntityImpl( new IndividualURI( "http://example.com/second_Annotation")), null, null);
	
//		mppr.addObject(ap);
		
		// Decomposition Pattern
		DecompositionPattern dcp = new DecompositionPattern();
		// Composite
		dcp.setComposite(new InformationEntityImpl(new IndividualURI( "http://example.com/composite")));
		// First Component
		dcp.addComponent(new InformationEntityImpl(new IndividualURI( "http://example.com/first_Component" )), null, null);
		// Second Component
		dcp.addComponent(new InformationEntityImpl(new IndividualURI( "http://example.com/second_Component" )), null, null);
		
//		mppr.addObject(dcp);
		
		try {
			URI uri = new URI("exp:bla");
			System.out.println(uri.toString());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
//			con.export(new org.openrdf.rio.rdfxml.RDFXMLWriter(System.out));
			con.export(new TriGWriter(System.out));
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RDFHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
