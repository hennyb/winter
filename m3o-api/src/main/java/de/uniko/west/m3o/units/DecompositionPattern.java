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

@winter_query("?DecompositionDescription <dul:defines> ?CompositeConcept, ?ComponentConcept, ?AppliedMethodRole, ?MethodConcept." +
			  "?DecompositionSituation <dul:satisfies> ?DecompositionDescription." +
			  "?CompositeConcept <dul:classifies> ?Composite." +
			  "?Composite <dul:hasSetting> ?DecompositionSituation." +
			  "?Component <dul:hasSetting> ?DecompositionSituation." +
			  "?ProvenanceEntity <dul:hasSetting> ?AnnotationSituation" +
			  "?MethodEntity <dul:hasSetting> ?AnnotationSituation")
public class DecompositionPattern implements RDFSerializable{
	
	@winter_id("m3o:DecompositionPattern")
	URI objectID = null;
	
	Winter winter = null;
	
	//RDFSerializable Members
	@winter_var("Composite")
	InformationEntity composite = null;
	@winter_var("Component")
	Vector<Component> components = new Vector<Component>();
	@winter_ref({"Method","ProvenanceEntity", "AppliedMethodRole", "MethodConcept"})
	Vector<ProvenanceInformationUnit> provenanceInformations = new Vector<ProvenanceInformationUnit>();	
	@winter_var("DecompositionSituation")
	StructureObject decompositionSituation = null;
	@winter_var("DecompositionDescription")
	StructureObject decompositionDescription = null;
	@winter_var("CompositeConcept")
	StructureObject compositeConcept = null;
	
	public DecompositionPattern() {
		decompositionDescription = new StructureObject("m3o:decompositionDescription");
		decompositionSituation = new StructureObject("m3o.decompositionSituation");
		compositeConcept = new StructureObject("m3o:compositeConcept");
	}
	
	public DecompositionPattern(InformationEntity composite, Component... components){
		this();
		this.composite = composite;
		this.components.addAll(Arrays.asList(components));
	}
	//Getter & Setter Methods

	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
