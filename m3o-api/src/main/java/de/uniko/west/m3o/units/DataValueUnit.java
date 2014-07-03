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

import de.uniko.west.m3o.interfaces.InformationEntity;
import de.uniko.west.m3o.interfaces.Quality;
import de.uniko.west.m3o.interfaces.Region;
import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

@winter_query("?InformationEntity <dul:hasQuality> ?Quality. " +
			  "?Quality <dul:hasRegion> ?Region. " +
			  "?Region <dul:hasRegionDataValue> ?Value. ")
public class DataValueUnit implements RDFSerializable{
	
	@winter_id("m3o:DataValuePattern")
	URI objectID = null;
	
	Winter winter= null;
	
	//RDFSerializable Members
	@winter_var("InformationEntity")
	InformationEntity informationEntity = null;
	@winter_var("Quality")
	Quality quality = null;
	@winter_var("Region")
	Region region = null;
	@winter_var("Value")
	Object data = null;

	public DataValueUnit(InformationEntity informationEntity, Quality quality, Region region, Object data){
		this.informationEntity = informationEntity;
		this.quality = quality;
		this.region = region;
		this.data = data;
	}
	
	public DataValueUnit() {
	}

	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
