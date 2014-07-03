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

import de.uniko.west.m3o.interfaces.Entity;
import de.uniko.west.m3o.interfaces.Method;
import de.uniko.west.m3o.utils.StructureObject;
import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

@winter_query("?AppliedMethodRole <dul:classifies> ?Method." +
			  "?MethodConcept <dul:classifies> ?ProvenanceEntity.")
public class ProvenanceInformationUnit implements RDFSerializable{
	
	@winter_id("m3o:ProvenanceInformationPattern")
	URI objectID = null;
	
	Winter winter = null;
	
	//Objects 
	@winter_var("Method")
	Method method = null;
	@winter_var("ProvenanceEntity")
	Entity provenanceEntity = null;
	
	//Members
	@winter_var("AppliedMethodRole")
	StructureObject appliedMethodRole = null;
	@winter_var("MethodConcept")
	StructureObject methodConcept = null;
	
	public ProvenanceInformationUnit(){
		appliedMethodRole = new StructureObject("m3o:appliedMethodRole");
		methodConcept =  new StructureObject("m3o:methodConcept");
	}
	
	public ProvenanceInformationUnit(Method method, Entity provenEntity){
		this();
		this.method = method;
		this.provenanceEntity = provenEntity;
	}

	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
