/*
The M3O-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The M3O-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the M3O-API.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.uniko.west.m3o.units;

import java.net.URI;
import java.util.Arrays;
import java.util.Vector;

import de.uniko.west.m3o.interfaces.InformationEntity;
import de.uniko.west.m3o.utils.StructureObject;
import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_ref;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

@winter_query ("?AnnotationDescription <dul:defines> ?AnnotatedIEConcept, AnnotationConcept, ?AppliedMethodRole, ?MethodConcept."  +
				"?AnnotationSituation <dul:satisfies>  ?AnnotationDescription." +
				"?AnnotatedIEConcept <dul:classifies> ?InformationEntity." +
				"?InformationEntity <dul:hasSetting> ?AnnotationSituation;" +
				"?AnnotationEntity <dul:hasSetting> ?AnnotationSituation" +
				"?ProvenanceEntity <dul:hasSetting> ?AnnotationSituation" +
				"?MethodEntity <dul:hasSetting> ?AnnotationSituation")
public class AnnotationUnit implements RDFSerializable{
	
	@winter_id("m3o:AnnotationPattern")
	URI objectID = null;
	
	Winter winter = null;
	
	//RDFSerializable Members
	@winter_var("InformationEntity")
	InformationEntity informationEntity = null;
	@winter_ref({"AnnotationConcept","AnnotationEntity"})
	Vector<Annotation> annotations = new Vector<Annotation>();
	@winter_ref({"Method","ProvenanceEntity", "AppliedMethodRole", "MethodConcept"})
	Vector<ProvenanceInformationUnit> provenanceInformations = new Vector<ProvenanceInformationUnit>();
	@winter_var("AnnotationSituation")
	StructureObject annotationSituation = null;
	@winter_var("AnnotationDescription")
	StructureObject annotationDescription = null;
	@winter_var("AnnotatedIEConcept")
	StructureObject annotatedInformationEntityConcept = null;
	
	//Constructors
	
	public AnnotationUnit(){
		annotationSituation = new StructureObject("m3o:annotationSituation");
		annotationDescription = new StructureObject("m3o:annotationDescription");
		annotatedInformationEntityConcept = new StructureObject("m3o:annotatedInformationEntityConcept");
	}
	
	public AnnotationUnit(InformationEntity infomationEntity, Annotation... annotations){
		this();
		this.informationEntity = infomationEntity;
		this.annotations.addAll(Arrays.asList(annotations));
	}
	
	//Getter & Setter

	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
