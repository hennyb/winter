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

import de.uniko.west.m3o.interfaces.InformationObject;
import de.uniko.west.m3o.interfaces.InformationRealization;
import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;


@winter_query("?InformationObject <dul:realizes> ?InformationRealization")
public class InformationRealizationUnit implements RDFSerializable{
	
	@winter_id("m3o:InformationRealizationPattern")
	URI objectID = null;
	
	Winter winter = null;
	
	//RDFSerializable Members
	@winter_var("InformationObject")
	InformationObject informationObject = null;
	
	@winter_var("InformationRealization")
	Vector<InformationRealization> informationRealizations = new Vector<InformationRealization>();
	
	//Constructors
	//XXX Winter requires default constructor
	public InformationRealizationUnit() {
	}
	
	public InformationRealizationUnit(InformationObject informationObject, InformationRealization... informationRealizations){
		this.informationObject = informationObject;
		this.informationRealizations.addAll(Arrays.asList(informationRealizations));
	}
	
	//Getter & Setter Methods 
	
	public void setInformationObject(InformationObject informationObject) {
		this.informationObject = informationObject;
	}
	
	public void addInformationRealizations(InformationRealization... informationRealizations) {
		this.informationRealizations.addAll(Arrays.asList(informationRealizations));
	}
	
	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
