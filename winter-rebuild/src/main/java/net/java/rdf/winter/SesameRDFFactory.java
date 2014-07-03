package net.java.rdf.winter;

import org.openrdf.model.ValueFactory;

public class SesameRDFFactory implements RDFFactory {

	ValueFactory vf;
	
	public SesameRDFFactory(ValueFactory vf){
		vf = vf;
	}
	
	public Object createLiteral(String s) {
		return vf.createLiteral(s);
	}

	public Object createLiteral(String s, String lang) {
		return vf.createLiteral(s, lang);
	}

	public Object createLiteralType(String s, String uriType) {
		return vf.createLiteral(s, vf.createURI(uriType));
	}

	public Object createResource(String uri) {
		return vf.createURI(uri);
	}
}
