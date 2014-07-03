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
package de.uniko.west.m3o.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_predicate;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

public class StructureObject implements RDFSerializable {

	Winter winter = null;
	
	//RDFSerializable Members
	@winter_id("")
	URI objectID = null;
	
	@winter_predicate("rdf:type")
	URI objectType = null;
	
	public StructureObject(){
	}
	
	public StructureObject(String type){
		try {
			objectID = new URI("http://" + type + "#" + UUID.randomUUID());
			objectType = new URI(type);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void setType(String type) {
		try {
			objectID = new URI("http://" + type + "#" + UUID.randomUUID());
			objectType = new URI(type);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public Winter winter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWinter(Winter winter) {
		// TODO Auto-generated method stub

	}

}
