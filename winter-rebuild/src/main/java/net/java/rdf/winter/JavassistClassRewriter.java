/*
New BSD license: http://opensource.org/licenses/bsd-license.php
Copyright (c) 2003, 2004, 2005 Sun Microsystems, Inc.
901 San Antonio Road, Palo Alto, CA 94303 USA.
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
- Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
- Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
- Neither the name of Sun Microsystems, Inc. nor the names of its contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 */
package net.java.rdf.winter;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import net.java.rdf.annotations.complex;
import net.java.rdf.annotations.reuse;

import java.net.URI;
import java.io.*;
import static java.text.MessageFormat.format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Henry Story
 */
public class JavassistClassRewriter {

	protected static transient Logger logger = LoggerFactory
			.getLogger(JavassistClassRewriter.class.getName());

	public static void main(String[] args) throws NotFoundException,
			CannotCompileException, ClassNotFoundException {
		logger.info("In JavassistClassRewriter");
		if (args.length == 0) {
			logger.error("No parameters set");
			message();
		} else if ("--run".equals(args[0])) {
			logger.info("Main class for on the fly rewriting called");
			try {
				getRewriteLoader().run(args);
			} catch (Throwable ex) {
				logger.error("Could not start RewriteLoader : Exception {}", ex.toString());
				ex.printStackTrace();
			}
		} else if ("--path".equals(args[0]) && (args.length > 1)) {
			logger.info("Got a classpath to search");
			ClassPool pool = ClassPool.getDefault();
			try {
				pool.insertClassPath(args[1]);
			} catch (NotFoundException e) {
				message();
				logger
						.error("could not find classpath {} : Exception",
								args[1]);
				e.printStackTrace();
				System.exit(1);
			}
			File dir = new File(args[1]);
			ArrayList<File> classfiles = new ArrayList<File>();
			descendThrough(dir, classfiles);
			WinterEditor edt = new WinterEditor();
			for (File cf : classfiles) {
				try {
					logger.info("rewriting class file {}", cf);
					CtClass clzz = pool.makeClass(new FileInputStream(cf));
					clzz.instrument(edt);
					clzz.toBytecode(new DataOutputStream(new FileOutputStream(
							cf)));
				} catch (IOException e) {
					logger.warn("In main : could not read file {}", cf);
					e.printStackTrace();
				} catch (CannotCompileException e) {
					logger
							.error("In main : could not javassist compile {}",
									cf);
					e.printStackTrace();
					System.exit(-1);
				}
			}
		} else if ("--cp".equals(args[0]) && (args.length > 2)) {
			logger.info("Got a list of files");
			// we are given a list of files to rewrite and then save
			ClassPool pool = ClassPool.getDefault();
			try {
				pool.insertClassPath(args[1]);
				pool
						.insertClassPath("/Users/hjs/Programming/sommer/sommer.jar");
			} catch (NotFoundException e) {
				message();
				logger.error("could not find classpath {}", args[1]);
				e.printStackTrace();
				System.exit(1);
			}
			WinterEditor edt = new WinterEditor();
			for (int i = 2; i < args.length; i++) {
				File cf = new File(args[i]);
				try {
					logger.info("Rewrite class files {}", cf);
					CtClass clzz = pool.makeClass(new FileInputStream(cf));
					clzz.instrument(edt);
					clzz.toBytecode(new DataOutputStream(new FileOutputStream(
							cf))); // todo: should not necessarily output to the
									// same file
				} catch (IOException e) {
					logger.warn("In main : Could not read file {}", cf);
					e.printStackTrace(); // todo: decide what exception to throw
				} catch (CannotCompileException e) {
					logger
							.error("In main : Could not javassist compile {}",
									cf);
					e.printStackTrace(); // todo: decide what exception to throw
					System.exit(-1);
				}
			}
		}
	}

	static HashSet<CtClass> changedClasses = new HashSet<CtClass>();

	/**
	 * for some reason clazz.ismodified too often returns true, so we'll be more
	 * explicit here.
	 * 
	 * @param clazz
	 */
	static void markChanged(CtClass clazz) {
		if (!changedClasses.contains(clazz))
			logger.info("Changed class: {}", clazz.getName());
		changedClasses.add(clazz);
	}

	/**
	 * Transform a set of classes.
	 * 
	 * @param classpath
	 *            a classpath for finding relevant classes
	 * @param classfiles
	 *            the classes to transform
	 * @param baseOutputDir
	 *            the base directory in which the classes get saved (after
	 *            creating the appropriate package dirs)
	 */
	public static void transformFiles(List<String> classpath,
			List<String> classfiles, String baseOutputDir, String markerName,
			String markerValue) throws Exception {
		logger.info("In Transform files");

		// Insert all classpaths into classpool
		ClassPool pool = ClassPool.getDefault();
		try {
			for (String path : classpath) {
				logger.debug("Adding path {} to classpool", path);
				pool.insertClassPath(path);
			}
		} catch (NotFoundException e) {
			throw e;
		}
		logger.info("All classpaths added to classpool");

		WinterEditor edt = new WinterEditor();
		FileInputStream fin = null;
		FileOutputStream fout = null;
		CtClass stringCl = pool.get("java.lang.String");

		// we need to do this in stages
		HashMap<CtClass, String> clzzes = new HashMap<CtClass, String>(); 

		// Check all files if they have already been processed
		for (String file : classfiles) {
			fin = new FileInputStream(file);
			CtClass clzz = pool.makeClass(fin);
			if (markerName != null && markerValue != null) {
				try {
					clzz.getDeclaredField(markerName);
					logger.info("Skipping. Allready processed file {} ", file);
					continue;
				} catch (NotFoundException e) {
					logger.warn("Could not find marker {}, in classfile {}",
							markerName, file);
				}
			}
			logger.info("Put class {} from file {} to clzzes", clzz.getName(),
					file.toString());
			clzzes.put(clzz, file);
		}
		logger.info("Loading needed interfaces and initialising hashmaps");
		CtClass serializableInt = ClassPool.getDefault().get("net.java.rdf.winter.RdfSerialisable");
		logger.info("Interfaces loaded");
		
		HashSet<CtClass> annotatedClasses = new HashSet<CtClass>();
		HashMap<CtConstructor, CtClass> getClassConstructor = new HashMap<CtConstructor, CtClass>();
		HashMap<CtField, CtMethod> getFieldMethod = new HashMap<CtField, CtMethod>();
		HashMap<CtField, CtMethod> setFieldMethod = new HashMap<CtField, CtMethod>();
		logger.info("Hashmaps initialised");

		logger.info("Going over all classes");
		// int i = 1;
		for (CtClass clazz : clzzes.keySet()) {
			logger.info("Working on class {}", clazz.getName());

			// Filling fields array for class
			CtField[] flds = clazz.getDeclaredFields();
			logger.debug("Getting fields of class {}. it has {} fields", clazz.getName(), flds.length);

			// Filling constructor array for class
			CtConstructor[] cnstrs = clazz.getDeclaredConstructors();
			logger.debug("Getting constructors of class {}. it has {} constructors",clazz.getName(), cnstrs.length);

			// Filling annotation hashset
			if (WinterEditor.getComplex(clazz.getAnnotations()) != null) {
				annotatedClasses.add(clazz);
				logger.info("Complex annoation on {} added to Hashset", clazz.getName());
			}

			// TODO Howto add an constructor
			// Going over all constructors and adding additional constructors
			int k = 1;
			logger.info("Going over all constructors and adding additional constructors");
			for (CtConstructor cns : cnstrs) {
				logger.info("constructor {} : {} : {}", new Object[] { k++,
						cns.getName(), cns.toString() });
				// String constructorSig =
				// EditorTranslator.constructorSignaturForComplexClass(clazz,
				// cns) + EditorTranslator.constructorForComplexClass(clazz,
				// cns);
				// CtConstructor constructor =
				// CtNewConstructor.make(constructorSig, clazz);
				getClassConstructor.put(cns, clazz);
			}

//			boolean rdffields = false;
			boolean complexfields = false;
			int j = 1;
			logger.info("going over all fields of {}", clazz.getName());
			// Go over all fields in Fields
			for (CtField fld : flds) {
				logger.info("Field {} : {}", j++, fld.getName());
				try {
					Object[] annotations = fld.getAnnotations();
					// Check if field is annotated
					if (WinterEditor.getComplex(annotations) == null) {
						logger.info("Field {} not annotated", fld.getName());
						// Add getter/setters for all nonannotated private fields
						if (Modifier.isPrivate(fld.getModifiers())) {
							try {
								String getterString = EditorTranslator.createNonAnnotatedGetter(fld, clazz);
								String setterString = EditorTranslator.createNonAnnotatedSetter(fld, clazz);
								CtMethod getter = CtMethod.make(getterString,clazz);
								CtMethod setter = CtMethod.make(setterString,clazz);
								clazz.addMethod(getter);
								clazz.addMethod(setter);
							} catch (ClassNotFoundException ex) {
								ex.printStackTrace();
								// TODO: handle exception
							} catch (NotFoundException ex) {
								ex.printStackTrace();
								// TODO: handle exception
							}
						}
						continue;
					}

					if (Modifier.isStatic(fld.getModifiers())) {
						markChanged(clazz);
						continue;
					}

//					if (!rdffields) {
//						rdffields = true;
//						annotatedClasses.add(clazz);
//					}

					if (WinterEditor.isComplex(annotations)) {
						logger.info("Complex annotation found for field {} = \n {}",fld.getName(), WinterEditor.getComplex(annotations).toString());
						
						// Create getter signature for annotated field
						String getterSig = EditorTranslator.getterSignatureForComplexField(clazz, fld) + ";";
						logger.debug("Getter signature for complex field = {}",getterSig);

						// Create setter signature for annotated field
						String setterSig = EditorTranslator.setterSignatureForComplexField(clazz, fld) + ";";
						logger.debug("Setter signature for complex field = {}",setterSig);

						// Make methods out of strings
						CtMethod getter = CtMethod.make(getterSig, clazz);
						CtMethod setter = CtMethod.make(setterSig, clazz);
						logger.info("Getter/Setter methods created from signature \n {} \n {}",new Object[] { getterSig, setterSig });

						// Add methods to class
						clazz.addMethod(getter);
						clazz.addMethod(setter);
						logger.debug("{}, {} added to {}", new Object[] {getter.toString(), setter.toString(), clazz.getName() });
						
						// Mark class as changed
						markChanged(clazz);
						
						// Put fields to fieldmap for later body adding
						getFieldMethod.put(fld, getter);
						setFieldMethod.put(fld, setter);
						logger.info("Getter/Setter put an hashmaps");

					} else {
						logger.info("Unknown annotation found");
					}
				} catch (CannotCompileException e) {
					logger.error("In main : could not javassist compile {}",clazz.getName());
					e.printStackTrace();
					throw e;
				}
			}
		}

		// 1.0 add RDFSerialisable interface
		// only add the interfaces to the topmost class that needs it
		logger.info("addinf interface");
		for (CtClass clazz : annotatedClasses) {
			logger.info("Working on class {}", clazz.getName().toString());
			// should never happen, but anyway
			if (clazz.isInterface()) {
				continue;
			}
			CtClass lastClazz = clazz;
			// check for superclass that is in the list;
			for (CtClass spr = clazz.getSuperclass(); spr != null; spr = spr
					.getSuperclass()) {
				logger.info("check if superclass {} is in the list", spr.getName());
				if (annotatedClasses.contains(spr)) {
					lastClazz = spr;
				}
			}
			// Add the interface and the sesameMapperAdaptor setter
			if (!lastClazz.subtypeOf(serializableInt)) {
				logger.info("adding RDFSerialisable interface, hashmap and implementation to {}",lastClazz.getName());
				lastClazz.addInterface(serializableInt);
				EditorTranslator.addSetSesameMapperAdaptorMethod(lastClazz);
				markChanged(lastClazz);
			} else {
				logger.info("could not add RDFSerialisable interface, hashmap and implementation to {}",lastClazz.getName());

			}
		}

		// 1.1 replace calls to fields with calls to methods in all classes
		// (that have not been markes as transformed!)
		// and then add the winter marker to the class
		// note: This cannot be done after implementations of these methods has
		// been added, or else the implementations will themselves be rewritten!
		int i = 1;
		logger.info("Modify methods and constructors for all classes");
		for (CtClass clzz : clzzes.keySet()) {
			logger.info("Class {}", i++, clzz.getName());
			try {
				// Modifies the bodies of all methods and constructors declared in the class
				clzz.instrument(edt);
				logger.debug("Modify all methods and constructors of {}", clzz.getName());
			} catch (CannotCompileException e) {
				logger.error("In main 1.1 : could not javassist compile {} ",clzz.getName());
				e.printStackTrace(); // todo: decide what exception to throw
				throw e;
			}
		}

		// 2.1 ADD CONSTRUCTOR bodies
		logger.info("Try to add constructor bodies");
		for (Iterator<Entry<CtConstructor, CtClass>> it = getClassConstructor
				.entrySet().iterator(); it.hasNext();) {
			Entry<CtConstructor, CtClass> entry = it.next();
			try {
				EditorTranslator.constructorSignaturForComplexClass(entry.getValue(), entry.getKey());
				// EditorTranslator.addComplexConstructor(entry.getKey());
				logger.debug("Adding complex constructor {}", entry.getKey().getName());
			} catch (NotFoundException ex) {
				logger.error("In main 2.1 : could not javassist compile constructor {} with exception:",entry.getKey().getName());
				ex.printStackTrace();
				// e.printStackTrace(); //todo: decide what exception to throw
				throw ex;
			}
		}

		// 3.1 ADD METHOD bodies: getters
		logger.info("Try to add getter method bodies");
		for (Iterator<Entry<CtField, CtMethod>> it = getFieldMethod.entrySet()
				.iterator(); it.hasNext();) {
			Entry<CtField, CtMethod> entry = it.next();
			try {
				if (WinterEditor.isComplex(entry.getKey().getAnnotations())) {
					EditorTranslator.addComplexGetterMethod(entry.getKey(),entry.getValue());
					logger.debug("Adding complex getter method {}", entry.getKey().getName());
				}
			} catch (NotFoundException ex) {
				logger
						.error(
								"In main 3.1 : could not javassist compile {} with exception:",
								entry.getKey().getName());
				ex.printStackTrace(); // todo: decide what exception to throw
				throw ex;
			}
		}

		// 3.2 ADD METHOD bodies: setter
		logger.info("Try to add setter method bodies");
		for (Iterator<Entry<CtField, CtMethod>> it = setFieldMethod.entrySet()
				.iterator(); it.hasNext();) {
			Entry<CtField, CtMethod> entry = it.next();
			try {
				if (WinterEditor.isComplex(entry.getKey().getAnnotations())) {
					EditorTranslator.addComplexSetterMethod(entry.getKey(),
							entry.getValue());
					logger.debug("Adding complex setter method {}", entry
							.getKey().getName());
				}
			} catch (NotFoundException ex) {
				logger
						.error(
								"In main 3.2 : could not javassist compile {} with exception",
								entry.getKey().getName());
				ex.printStackTrace(); // todo: decide what exception to throw
				throw ex;
			}
		}

		// 4. Make those classes that have changed methods and that were not
		// abstract non abstract
		logger.info("Make changed classes not abstract");
		for (CtMethod method : getFieldMethod.values()) {
			CtClass clazz = method.getDeclaringClass();
			clazz.setModifiers(clazz.getClassFile2().getAccessFlags()
					& ~Modifier.ABSTRACT);
		}

		// 6. Write the output
		logger.info("Writing classes to output");
		for (CtClass clzz : changedClasses) {
			File outfile = null;
			try {
				if (baseOutputDir != null) {
					outfile = new File(baseOutputDir + File.separator
							+ clzz.getName().replace('.', File.separatorChar)
							+ ".class");
				} else {
					outfile = new File(clzzes.get(clzz)); // note:  clzz.getClassFile() may remove the need for this  hash map
				}
				fout = new FileOutputStream(outfile);

				CtField marker = new CtField(stringCl, markerName, clzz);
				marker.setModifiers(Modifier.STATIC); // todo: should also be
														// private?
				clzz
						.addField(marker, CtField.Initializer
								.constant(markerValue));

				clzz.toBytecode(new DataOutputStream(fout)); // todo: should not
																// necessarily
																// output to the
																// same file

				logger.info("Javassist rewrote file {}", clzz.getClassFile2());
			} catch (IOException e) {
				logger.warn("could not write file {}", clzz.getClassFile2());
				e.printStackTrace(); // todo: decide what exception to throw
				throw e;
			} catch (CannotCompileException e) {
				logger.error("In Main 6.0 : could not javassist compile {}",
						clzz.getClassFile2());
				e.printStackTrace(); // todo: decide what exception to throw
				throw e;
			} finally {
				if (fin != null) {
					fin.close();
				}
				if (fout != null) {
					fout.close();
				} // todo: leakage possible due to exception thrown before
			}
		}
	}

	private static void descendThrough(File dir, List<File> files) {
		if (dir.isDirectory()) {
			File[] kids = dir.listFiles();
			for (int i = 0; i < kids.length; i++) {
				descendThrough(kids[i], files);
			}
		} else if (dir.isFile()) {
			if (dir.getName().endsWith(".class")) {
				files.add(dir);
			}
		}
	}

	private static void message() {
		System.err
				.println("Note: This useage has not been thought through carefully. Please improove.");
		System.err
				.println("Usage: JavassistClassRewriter [ --run main-class [args...]]");
		System.err.println("                              [ --path dir ]");
		System.err
				.println("                              [ --cp dir files...]");
		System.err
				.println("[ --run ... ]  option: To run a main class and transform classes on the fly.");
		System.err
				.println("[ --path dir ] the dir is the root directory to search for classes requiring transformation");
		System.err
				.println("[ --cp dir --files files...] a classpath and a list of files. The transform files method is more thought out. This needs improoving. Don't rely on it.");
		System.err
				.println("Warning: the second method will rewrite all the classes beneath directory dir. ");
		System.err
				.println("If classes outside that path call annotated fields directly during some runtime those calls will not be rewritten.");
		System.err
				.println("All fields therefore should be part of a package and be package protected.");
		System.err
				.println("todo: this program could partly verify that, and send a warning message if not.");
	}

	public static Loader getRewriteLoader() throws NotFoundException,
			CannotCompileException, ClassNotFoundException {
		// set up class loader with translator
		EditorTranslator xtor = new EditorTranslator(new WinterEditor());
		ClassPool pool = ClassPool.getDefault();
		Loader loader = new Loader(pool);
		loader.delegateLoadingOf("net.java.rdf.annotations."); // we use this
																// here, and
																// also a lot in
																// the code,...
		loader.delegateLoadingOf("net.java.rdf.winter.");
		loader.addTranslator(pool, xtor);
		return loader;
	}
}

class EditorTranslator implements Translator {

	protected static transient Logger logger = LoggerFactory
			.getLogger(EditorTranslator.class.getName());

	/**
	 * create the name of the getter method for a field on a given class
	 */
	static String getterMethodForField(CtField fld, CtClass clas) {
		return "javassistGet" + clas.getName().replace('.', '_') + "_"
				+ fld.getName() + "()";
	}

	/**
	 * create a setter method on a class for a given field
	 */
	static String setterMethodForField(CtClass clazz, CtField fld) {
		return "javassistSet" + clazz.getName().replace('.', '_') + "_"
				+ fld.getName();
	}

	static String constructorForComplexClass(CtClass clazz,
			CtConstructor constructor) {
		return clazz.getName();
	}

	private ExprEditor m_editor;

	EditorTranslator(ExprEditor editor) {
		m_editor = editor;
	}

	public void start(ClassPool pool) {
		logger.info("start pool");
	}

	public void onLoad(ClassPool pool, String cname) throws NotFoundException,
			CannotCompileException {
		logger.info("cname(in Onload) = {}", cname);
		CtClass clas = pool.get(cname);
		clas.instrument(m_editor);
	}

	/**
	 * @param clazz
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	static void addSetSesameMapperAdaptorMethod(CtClass clazz)
			throws CannotCompileException, NotFoundException {
		// ADD Nullfieldhash
		CtField nullhash = CtField.make(
				"protected java.util.HashSet sommerNullFieldHash;", clazz);
		clazz.addField(nullhash);
		logger.info("NullFieldHash added");
		// ADD winterVarHash
		CtField varhash = CtField.make(
				"private java.util.HashMap winterVarHash;", clazz);
		clazz.addField(varhash);
		logger.info("WinterVarHash added");
		// ADD class uuid-uri id
		if (clazz.getField("id") != null) {
			logger.warn("Class {} already has field id of type URI", clazz
					.getName());
		} else {
			CtField id = CtField.make("private java.net.URI id = null;", clazz);
			clazz.addField(id);
			logger.info("Id added");
		}
		// ADD graph object sould be mapped to
		CtField graph = CtField.make("private java.net.URI graph = null;",
				clazz);
		clazz.addField(graph);
		logger.info("Graph added");
		// ADD isvalid field
		CtField isvalid = CtField.make("private boolean isvalid = false;", clazz);
		clazz.addField(isvalid);
		/*
		 * CtMethod graphSet = CtMethod.make(
		 * "public void setGraph(java.net.URI graph) { " +
		 * "this.graph  = graph;\n " + " } \n", clazz);
		 * clazz.addMethod(graphSet); CtMethod graphGet = CtMethod.make(
		 * "public java.net.URI getGraph() { " + "return this.graph;\n " +
		 * " } \n", clazz); clazz.addMethod(graphGet);
		 * logger.info("Graph added");
		 */
		// ADD SesameMapperAdaptor and setter method
		CtField fld = CtField
				.make(
						"private net.java.rdf.winter.SesameMapperAdaptor sesameMapperAdaptor = null;",
						clazz);
		clazz.addField(fld);
		CtMethod mapperAdaptorSet = CtNewMethod
				.make(
						"public void setSesameMapperAdaptor(net.java.rdf.winter.SesameMapperAdaptor adaptor) { "
								+ "sesameMapperAdaptor = adaptor;\n "
								+ "if (adaptor==null) { \n"
								+ "  sommerNullFieldHash = null; \n"
								+ "} else { \n"
								+ "  sommerNullFieldHash = new java.util.HashSet(); \n"
								+ "}\n" + "}\n", clazz);
		clazz.addMethod(mapperAdaptorSet);
		logger.info("SesameMapperAdaptor added");
		// WARNING WARNING: could it still be abstract for other reasons???
		clazz.setModifiers(clazz.getModifiers() & ~Modifier.ABSTRACT);
	}

	/**
	 * Method to add SetterMethod-Body for @complex annotated fields
	 * 
	 * @param fld
	 * @param method
	 * @throws NotFoundException
	 */
	static void addComplexSetterMethod(CtField fld, CtMethod method)
			throws NotFoundException {
		CtClass clazz = fld.getDeclaringClass();

		logger.info("in addSetterMethod for {}", method.getName());
		try {
			Object[] annotations = fld.getAnnotations();
			if (WinterEditor.getComplex(annotations) == null) {
				logger.info("field=" + fld.getName() + ") has no annotation!");
				throw new NotFoundException(
						"this should have a @complex annotation or else we should never have gotten here!");
			}
			StringBuffer stMtd = new StringBuffer();
			stMtd.append("{\n").append("boolean check = false;\n").append(
					"if (sesameMapperAdaptor==null) { \n " + "this."
							+ fld.getName() + " = $1;\n").append(
					"return check; \n").append("} else {\n");
			String line1 = "java.lang.reflect.Field field = " + clazz.getName()
					+ ".class.getDeclaredField( \"" + fld.getName() + "\" );";
			String line2 = null;
			if (WinterEditor.isCollection(fld.getType())) {
				// TODO add methods for collection type
				/*
				 * line2 = format("this." + fld.getName() +
				 * "=  ( {0} ) sesameMapperAdaptor.setCollectionField( " +
				 * "   this,  " +
				 * "  (net.java.rdf.annotations.complex)field.getAnnotation(net.java.rdf.annotations.complex.class),"
				 * + "   {1}, " + "   field.getGenericType() ); ",
				 * fld.getType().getName(), fld.getName());
				 * stMtd.append("try { \n" + line1 + "\n" + line2 +
				 * "\n    } catch ( java.lang.NoSuchFieldException e) { e.printStackTrace(); }"
				 * );
				 */
			} else {
				// Signature
				// "boolean setField (Complex annotation, URI id, Object Value, URI graph, varHash winterVarHash)"
				line2 = "check = "
						+ "(boolean)sesameMapperAdaptor.setField(\n"
						+ "(net.java.rdf.annotations.complex)field.getAnnotation(net.java.rdf.annotations.complex.class) ,\n"
						+ "(java.net.URI) id ,\n" + "(Object)this."
						+ fld.getName() + " ,\n" + "(java.net.URI) graph, \n"
						+ "(java.util.HashMap)winterVarHash\n" + ");\n";
				line2 += "if ( this. " + fld.getName()
						+ "== null ) { sommerNullFieldHash.add(field); } "
						+ " else { sommerNullFieldHash.remove(field); }";
				stMtd.append("try { \n"
								+ line1
								+ "\n"
								+ line2
								+ "\n    } catch ( java.lang.NoSuchFieldException e) { e.printStackTrace(); } \n");
			}
			stMtd.append("   }\n");// end of else;
			stMtd.append("return check;\n");
			stMtd.append("}\n"); // end of method body;
			logger.debug("setter method for field {} : \n {}", fld.getName(),
					stMtd.toString());
			method.setBody(stMtd.toString());

			// Add external Setter for field
			logger.info(
					"External setter method added for field {} in class {}",
					fld.getName(), clazz.getName());
			String externalSetterString = EditorTranslator
					.createExternalSetter(fld, clazz, method);
			CtMethod externalSetter = CtMethod
					.make(externalSetterString, clazz);
			clazz.addMethod(externalSetter);

			JavassistClassRewriter.markChanged(clazz);
		} catch (CannotCompileException ex) {
			logger.error("In addComplexSetterMethod : could not javassist compile {} with exception:",clazz.getName());
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			logger.error("In addComplexSetterMethod : class {} not found with exception:",clazz.getName());
			ex.printStackTrace();
		}
	}

	/**
	 * Method to add GetterMethod-Body for @complex annotated fields
	 * 
	 * @param fld
	 * @param method
	 * @throws NotFoundException
	 */
	static void addComplexGetterMethod(CtField fld, CtMethod method)
			throws NotFoundException {
		CtClass clazz = fld.getDeclaringClass();

		logger.info("in addgetterMethod for {}", method.getName());
		try {
			Object[] annotations = fld.getAnnotations();
			if (WinterEditor.getComplex(annotations) == null) {
				logger.info("field {} has no annotation!", fld.getName());
				throw new NotFoundException(
						"this should have a @complex annotation or else we should never have gotten here!");
			}
			StringBuffer gtMtd = new StringBuffer();
			gtMtd.append("{\n");

			String line0 = "if (sesameMapperAdaptor==null) { \n"
					+ "    return " + fld.getName() + ";\n" + "} else if ("
					+ fld.getName() + " == null) {\n";

			String line1 = "java.lang.reflect.Field field = " + clazz.getName()
					+ ".class.getDeclaredField( \"" + fld.getName() + "\" );";
			if (WinterEditor.isCollection(fld.getType())) {
				// TODO add methods for collection type
				/*
				 * String line2; line2 = fld.getName() +
				 * "= sesameMapperAdaptor.getCollectionField(" + " this, " +
				 * "(net.java.rdf.annotations.complex)field.getAnnotation(net.java.rdf.annotations.complex.class),"
				 * + fld.getName() + ", " + "field.getGenericType() );";
				 * gtMtd.append(line0 + " try { \n" + line1 + "\n" + line2 +
				 * "\n" + " } catch ( java.lang.NoSuchFieldException e ) { \n" +
				 * "    e.printStackTrace(); \n" + "    return null;\n" + "}");
				 */
			} else {
				String line2 = "if (sommerNullFieldHash.contains(field)) { return null; }";
				// Signature for get field (URI id, Complex annotation, Object
				// Value, URI graph)
				line2 += fld.getName()
						+ "= ("
						+ fld.getType().getName()
						+ ") sesameMapperAdaptor.getField("
						+ "(java.net.URI) id,"
						+ "(net.java.rdf.annotations.complex)field.getAnnotation(net.java.rdf.annotations.complex.class),"
						+ "(Object)this." + fld.getName() + ","
						+ "(java.net.URI) graph" + ");\n";
				line2 += "if ( this. " + fld.getName()
						+ "== null ) { sommerNullFieldHash.add(field); } "
						+ " else { sommerNullFieldHash.remove(field); }";

				gtMtd.append(line0 + " try { \n" + line1 + "\n" + line2 + "\n"
						+ " } catch ( java.lang.NoSuchFieldException e ) { \n"
						+ "    e.printStackTrace(); \n" + "    return null;\n"
						+ " } ");
			}
			gtMtd.append("   }\n"); // end of else;
			gtMtd.append("return " + fld.getName() + ";\n" + "}"); // end of method body;
			logger.debug("getter method for field {} : \n {}", fld.getName(),gtMtd.toString());
			method.setBody(gtMtd.toString());

			// Add external Getter for field
			logger.info("External getter method added for field {} in class {}",fld.getName(), clazz.getName());
			String externalGetterString = EditorTranslator.createExternalGetter(fld, clazz, method);
			CtMethod externalGetter = CtMethod.make(externalGetterString, clazz);
			clazz.addMethod(externalGetter);

			JavassistClassRewriter.markChanged(clazz);
		} catch (CannotCompileException ex) {
			logger
					.error(
							"In addComplexGetterMethod : Could not javassist compile {} with exception:",
							clazz.getName());
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			logger
					.error(
							"In addComplexGetterMethod : Could not find class {} with exception:",
							clazz.getName());
			ex.printStackTrace();
		}

	}

	/**
	 * @param constructor
	 * @throws NotFoundException
	 */
	static void addComplexConstructor(CtConstructor constructor)
			throws NotFoundException {
		CtClass clazz = constructor.getDeclaringClass();
		logger.info("in addComplexConstructor for {}", clazz.getName());

		reuse rann = null;
		List<String> vars = null;

		try {
			rann = WinterEditor.getReuse(clazz.getAnnotations());
			vars = processVars(rann);
		} catch (ClassNotFoundException e) {
			logger.warn("Class {} is not reuse annotated", clazz);
		}

		try {
			Object[] annotations = clazz.getAnnotations();
			if (WinterEditor.getComplex(annotations) == null) {
				logger.info("clazz {} has no complex annotation!", clazz.getName());
				throw new NotFoundException("this should have a @complex annotation or else we should never have gotten here!");
			}
			StringBuffer cnMtd = new StringBuffer();
			cnMtd.append("{\n");
			cnMtd.append("if (id == null) {\n");
			cnMtd.append("	try{\n");
			cnMtd.append("		id = new java.net.URI(\"http://\" + new (UUID.randomUUID).toString());\n");
			cnMtd.append("	}catch(java.net.URISyntaxException e){}\n");
			cnMtd.append("}\n ");
			cnMtd.append("winterVarHash = new HashMap<String, java.net.URI>;\n");
			cnMtd.append("if (($class).getAnnotation(net.java.rdf.annotations.reuse.class) != null) {\n ");
			for (String var : vars) {
				cnMtd.append("	winterVarHash.put(\"").append(var).append("\"").append(", $0);\n");
			}
			cnMtd.append(" }\n");
			cnMtd.append("}\n");
			constructor.setBody(cnMtd.toString());
			JavassistClassRewriter.markChanged(clazz);
		} catch (CannotCompileException ex) {
			logger.error("In addComplexConstructor : Could not javassist compile {} with exception {}",clazz.getName());
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			logger.error("In addComplexConstructor : Could not find class {} with exception {}",clazz.getName());
			ex.printStackTrace();
		}

	}

	/**
	 * @param clazz
	 * @param constructor
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NotFoundException
	 */
	static String constructorSignaturForComplexClass(CtClass clazz,
			CtConstructor constructor) throws ClassNotFoundException,
			NotFoundException {
		// reuse rann = WinterEditor.getReuse(clazz.getAnnotations());
		complex cann = WinterEditor.getComplex(clazz.getAnnotations());
		logger.info("Creating constructor signature for {}", clazz.getName().toString());
		if (cann == null) {
			logger.warn("No complex annotation found, no class to rebuild");
			return null;
		}
		StringBuffer cnstr = new StringBuffer();
		int mods = constructor.getModifiers();
		if (Modifier.isPublic(mods)) {
			cnstr.append("private ");
		}
		cnstr.append(" ").append(clazz.getName());
		cnstr.append("(");
		CtClass[] args = constructorArgs(constructor);
		int l = args.length;
		for (int i = 0; i < l; i++) {
			CtClass arg = args[i];
			if (i == l - 1) {
				cnstr.append(arg.getName()).append(" ").append(i);
			} else {
				cnstr.append(arg.getName()).append(" ").append(i).append(", ");
			}
		}
		cnstr.append(")");
		// String signature =
		// (cnstr.append(" ").append(clazz.getName()).append("(").append(constructorArgs(clazz,
		// constructor) + " setToVal").append(")")).toString();
		String signature = cnstr.toString();
		logger.info("Signature created {}", signature);
		return signature;
	}

	/**
	 * Creates the String setter method signature of an internal getter method
	 * 
	 * @param clazz
	 *            The class containing the field the signature is created for
	 * @param fld
	 *            The field to create an setter method signature for
	 * @return The string containing the setter method signature
	 * @throws NotFoundException
	 * @throws ClassNotFoundException
	 */
	static String setterSignatureForComplexField(CtClass clazz, CtField fld)
			throws ClassNotFoundException, NotFoundException {
		complex cann = WinterEditor.getComplex(fld.getAnnotations());
		if (cann == null) {
			return null;
		}
		StringBuffer mthdSetter = new StringBuffer();
		// set the same access restrictions as on the field
		int mods = fld.getModifiers();
		if (Modifier.isPrivate(mods)) {
			mthdSetter.append("private ");
		}
		if (Modifier.isProtected(mods)) {
			mthdSetter.append("protected ");
		}
		if (Modifier.isPublic(mods)) {
			mthdSetter.append("public ");
		}
		mthdSetter.append(" boolean ").append(setterMethodForField(clazz, fld))
				.append("(").append(fld.getType().getName() + " setToVal")
				.append(")");
		logger.info(mthdSetter.toString());
		return mthdSetter.toString(); // could return StringBuffer or take
										// StringBuffer as input for efficiency
	}

	/**
	 * Creates the String getter method signature of an internal getter method
	 * 
	 * @param clazz
	 *            The class containing the field the signature is created for
	 * @param fld
	 *            The field to create an getter method signature for
	 * @return The string containing the getter method signature
	 * @throws NotFoundException
	 * @throws ClassNotFoundException
	 */
	static String getterSignatureForComplexField(CtClass clazz, CtField fld)
			throws NotFoundException, ClassNotFoundException {
		complex cann = WinterEditor.getComplex(fld.getAnnotations());
		if (cann == null) {
			return null;
		}
		StringBuffer mthdGetter = new StringBuffer();
		// set the same access restrictions as on the field
		int mods = fld.getModifiers();
		if (Modifier.isPrivate(mods)) {
			mthdGetter.append("private ");
		}
		if (Modifier.isProtected(mods)) {
			mthdGetter.append("protected ");
		}
		if (Modifier.isPublic(mods)) {
			mthdGetter.append("public ");
		}
		mthdGetter.append(fld.getType().getName() + " ").append(
				getterMethodForField(fld, clazz));

		return mthdGetter.toString(); // could return StringBuffer or take
										// StringBuffer as input for efficiency
	}

	/**
	 * @param constructor
	 * @return
	 * @throws NotFoundException
	 */
	static CtClass[] constructorArgs(CtConstructor constructor)
			throws NotFoundException {
		try {
			return constructor.getParameterTypes();
		} catch (NotFoundException ex) {
			logger.warn("In constructorArgs : ");
			throw ex;
		}
	}

	/**
	 * @param rann
	 * @return
	 */
	static List<String> processVars(reuse rann) {
		String varString = rann.value().toString();
		List<String> vars = Arrays.asList(varString.split(","));
		for (String var : vars) {
			var.replace(" ", "");
		}
		return vars;
	}
	
	/**
	 * Creates an isValid method for the class checks if Min/Max/MinMax annotated fields have the right cardinallity
	 * 
	 * @return the method as String
	 */
	static String createIsValidMethod(CtClass clazz){
		StringBuffer svldMethod = new StringBuffer();
		svldMethod.append("public boolean isValid(){ \n");
		svldMethod.append("if (isvalid) {\n").append(" return true; \n");
		svldMethod.append("}else{\n").append(" isvalid = true; \n"); 
		svldMethod.append("	for (Field field : this.getClass().getDeclaredFields()){");
		svldMethod.append("		if(field.getAnnotations != null){");
		svldMethod.append("			if(field.getAnnotation()){");
		// TODO Min annotation hinzufügen und verarbeiten
		svldMethod.append("			}else if(field.getAnnoatation()){");
		// TODO Max annotation hinzufügen und verarbeiten
		svldMethod.append("			}else if(field.getAnnoatation()){");
		// TODO MinMax annotation hinzufügen und verarbeiten
		svldMethod.append("			}");
		svldMethod.append("		}else { isvalid = false}");
		svldMethod.append("	}");
		svldMethod.append("	return isvald;");
		svldMethod.append("}");
		return svldMethod.toString();
	}
	/**
	 * Creates an factory method for this class
	 * 
	 * @param clazz		the class to create method for	
	 * @param constr	the speciefied constructor called by this create method
	 * @return the method as String
	 */
	static String createCreateMethod(CtClass clazz, CtConstructor constr){
		StringBuffer crtMethod = new StringBuffer();
		crtMethod.append("public static ").append(clazz.getSimpleName()).append(" create(){");
		crtMethod.append("");
		crtMethod.append("return Object;");
		crtMethod.append("}");
		return crtMethod.toString();
	}

	/**
	 * Creates the string of an getter method for the specified unannotated
	 * member.
	 * 
	 * @param fld
	 *            The field to create an getter for
	 * @param clazz
	 *            The class containing the field
	 * @return the method as String
	 * @throws ClassNotFoundException
	 * @throws NotFoundException
	 */
	static String createNonAnnotatedGetter(CtField fld, CtClass clazz)
			throws ClassNotFoundException, NotFoundException {
		logger.info("In createNonAnnotatedGetter : Field {} of class {} not annotated try to add getter method",fld.getName(), clazz.getName());
		try {
			if (WinterEditor.getComplex(fld.getAnnotations()) != null) {
				logger.warn("In createNonAnnotatedGetter : Field {} annotated we should not be here",fld.getName());
				return null;
			}

			StringBuffer mthdGetter = new StringBuffer();
			mthdGetter.append("public ").append(fld.getType().getName()).append(" ");
			String fldName = (fld.getName()).substring(0, 1).toUpperCase() + (fld.getName()).substring(1);
			mthdGetter.append("get").append(fldName);
			mthdGetter.append("(){ \n");
			mthdGetter.append("return ").append(fld.getName()).append("; \n");
			mthdGetter.append("} \n");
			logger.debug("In createNonAnnotatedGetter : adding getter method \n {}",mthdGetter.toString());
			return mthdGetter.toString();
		} catch (ClassNotFoundException ex) {
			throw ex;
		} catch (NotFoundException ex) {
			throw ex;
		}
	}

	/**
	 * Creates the string of an setter method for the specified unannotated
	 * member.
	 * 
	 * @param fld
	 *            The field to create an setter for
	 * @param clazz
	 *            The class containing the field
	 * @return the method as String
	 * @throws ClassNotFoundException
	 * @throws NotFoundException
	 */
	static String createNonAnnotatedSetter(CtField fld, CtClass clazz)
			throws ClassNotFoundException, NotFoundException {
		logger.info("In createNonAnnotatedSetter : Field {} of class {} not annotated try to add setter method",fld.getName(), clazz.getName());
		try {
			if (WinterEditor.getComplex(fld.getAnnotations()) != null) {
				logger.warn("In createNonAnnotatedSetter : Field {} annotated we should not be here",fld.getName());
				return null;
			}
			StringBuffer mthdSetter = new StringBuffer();
			mthdSetter.append("public ").append("void ");
			String fldName = (fld.getName()).substring(0, 1).toUpperCase()+ (fld.getName()).substring(1);
			mthdSetter.append("set").append(fldName);
			mthdSetter.append("(").append(fld.getType().getName()).append(" value){ \n");
			mthdSetter.append("this.").append(fld.getName()).append(" = $1; \n");
			mthdSetter.append("} \n");
			logger.debug("In createNonAnnotatedSetter : adding setter method \n {}",mthdSetter.toString());
			return mthdSetter.toString();
		} catch (ClassNotFoundException ex) {
			throw ex;
		} catch (NotFoundException ex) {
			throw ex;
		}
	}

	/**
	 * Creates the String of an external getter method for the specified field
	 * and its internal getter method
	 * 
	 * @param fld
	 *            The field to create setter for
	 * @param clazz
	 *            The class containing the field
	 * @param method
	 *            The internal getter method
	 * @return the method as String
	 * @throws ClassNotFoundException
	 * @throws NotFoundException
	 */
	static String createExternalGetter(CtField fld, CtClass clazz,
			CtMethod method) throws ClassNotFoundException, NotFoundException {
		logger.info("In createExternalGetter : Field {} of class {} not annotated try to add external getter method",fld.getName(), clazz.getName());
		try {
			if (WinterEditor.getComplex(fld.getAnnotations()) == null) {
				logger.warn("In createExternalGetter : Field {} not annotated we should not be here",fld.getName());
				return null;
			}
			StringBuffer mthdGetter = new StringBuffer();
			mthdGetter.append("public ").append(fld.getType().getName()).append(" ");
			String fldName = (fld.getName()).substring(0, 1).toUpperCase()+ (fld.getName()).substring(1);
			mthdGetter.append("get").append(fldName);
			mthdGetter.append("(){ \n");
			mthdGetter.append(clazz.getName()).append(" ").append(" c ").append(" = $0; \n");
			mthdGetter.append(fld.getType().getName()).append(" value").append(" = null; \n");
			mthdGetter.append("value = ").append(method.getName()).append("(); \n");
			mthdGetter.append("return value; \n");
			mthdGetter.append("} \n");
			logger.info("In createExternalGetter : adding setter method \n{}",mthdGetter.toString());
			return mthdGetter.toString();
		} catch (ClassNotFoundException ex) {
			throw ex;
		} catch (NotFoundException ex) {
			throw ex;
		}
	}

	/**
	 * Creates the String of an external setter method for the specified field
	 * and its internal setter method
	 * 
	 * @param fld
	 *            The field to create setter for
	 * @param clazz
	 *            The class containing the field
	 * @param method
	 *            The internal getter method
	 * @return the method as String
	 * @throws ClassNotFoundException
	 * @throws NotFoundException
	 */
	static String createExternalSetter(CtField fld, CtClass clazz,
			CtMethod method) throws ClassNotFoundException, NotFoundException {
		logger.info("In createExternalSetter : Field {} of class {} not annotated try to add external setter method",fld.getName(), clazz.getName());
		try {
			if (WinterEditor.getComplex(fld.getAnnotations()) == null) {
				logger.warn("In createExternalSetter : Field {} not annotated we should not be here",fld.getName());
				return null;
			}
			StringBuffer mthdSetter = new StringBuffer();
			mthdSetter.append("public ").append("boolean ");
			String fldName = (fld.getName()).substring(0, 1).toUpperCase()+ (fld.getName()).substring(1);
			mthdSetter.append("set").append(fldName);
			mthdSetter.append("(").append(fld.getType().getName()).append(" val){ \n");
			mthdSetter.append(clazz.getName()).append(" ").append(" c ").append(" = $0; \n");
			mthdSetter.append(fld.getType().getName()).append(" value").append(" = val; \n");
			mthdSetter.append("boolean check = ").append(method.getName()).append("($1)").append("; \n");
			mthdSetter.append("return check; \n");
			mthdSetter.append("} \n");
			logger.info("In createExternalSetter : adding setter method \n{}",mthdSetter.toString());
			return mthdSetter.toString();
		} catch (ClassNotFoundException ex) {
			throw ex;
		} catch (NotFoundException ex) {
			throw ex;
		}
	}
	
	/**
	 * Adds a setter method for the graph field 
	 * 
	 * @param clazz
	 * @return
	 */
	static String createSetGraph(CtClass clazz){
		logger.info("In createSetGraph : Adding setGraph method to class {}", clazz.getName());
		StringBuffer grphSetter = new StringBuffer();
		grphSetter.append("public void setGraph(java.net.URI graph){\n");
		grphSetter.append("this.graph = graph;\n");
		grphSetter.append("}\n");
		logger.debug("In createSetGraph : adding setter method \n{}", grphSetter.toString());
		return (grphSetter.toString());
	}
	
	/**
	 * Adds a getter method for the graph field
	 * 
	 * @param clazz
	 * @return
	 */
	static String createGetGraph(CtClass clazz){
		logger.info("In createGetGraph : Adding getGraph method to class {}", clazz.getName());
		StringBuffer grphSetter = new StringBuffer();
		grphSetter.append("return graph;\n");
		grphSetter.append("}\n");
		logger.debug("In creategetGraph : adding getter method \n{}", grphSetter.toString());
		return (grphSetter.toString());
	}
}

class WinterEditor extends ExprEditor {

	Logger logger = LoggerFactory.getLogger(WinterEditor.class.getClass());

	WinterEditor() {
	}

	@Override
	public void edit(FieldAccess arg) throws CannotCompileException {

		try {
			Object[] annotations = arg.getField().getAnnotations();
			if ((getComplex(annotations) == null)) {
				return;
			}
			// TODO:change.Add setter and getter methods  here too.
			if (Modifier.isStatic(arg.getField().getModifiers())) {
				return;
			}

			StringBuilder code = new StringBuilder();

			if (arg.isWriter()) {
				code.append(arg.getField().getName().replace(".", " ")).append(" = ").append("$1 ;");
				code.append(" $0.").append(EditorTranslator.setterMethodForField(arg.getField().getDeclaringClass(), arg.getField())).append("($1) ;");
			} else {
				code.append(" $_ = ($r) $0.").append(EditorTranslator.getterMethodForField(arg.getField(),arg.getField().getDeclaringClass())).append("; ");
			}

			logger.info("to {} will add {}", arg.getEnclosingClass().getName(),code);
			arg.replace(code.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); // todo: decide what exception to throw
			throw new CannotCompileException(e);
		} catch (NotFoundException e) {
			// e.printStackTrace(); //todo: decide what exception to throw
			logger.info("could not access a class reltated to field {} in class {}. Skipping this rewrite.",arg.getFieldName(), arg.getClassName());
			return;
		} catch (CannotCompileException e) {
			logger.error("In edit : could Not rewrite byte code!!! with exception {}",e);
			throw e;
		}
	}

	/**
	 * @param clazz
	 *            of the field to be checked
	 * @return true is field is of type collection
	 */
	static boolean isCollection(CtClass clazz) {
		return "java.util.Collection".equals(clazz.getName());
	}

	/**
	 * @param annotations
	 * @return return the complex annotation (should never be more than one)
	 */
	static complex getComplex(Object[] annotations) {
		for (Object o : annotations) {
			if (o instanceof complex) {
				return (complex) o;
			}
		}
		return null;
	}

	/**
	 * @param annotations
	 * @return return true if the annotation is a complex annotation should
	 *         never be more than one annotation
	 */
	static boolean isComplex(Object[] annotations) {
		for (Object o : annotations) {
			if (o instanceof complex) {
				return true;
			}
		}
		return false;
	}

	static reuse getReuse(Object[] annotations) {
		for (Object o : annotations) {
			if (o instanceof reuse) {
				return (reuse) o;
			}
		}
		return null;
	}
}