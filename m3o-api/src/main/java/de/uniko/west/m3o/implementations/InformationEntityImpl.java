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
package de.uniko.west.m3o.implementations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import de.uniko.west.m3o.interfaces.InformationEntity;
import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.core.interfaces.Winter;

public class InformationEntityImpl implements InformationEntity{

	Winter winter = null;
	
	@winter_id("m3o:InformationEntity")
	URI objectID = null;
	
	public InformationEntityImpl(){
		try {
			objectID = new URI("http://m3o:InformationEntity#" + UUID.randomUUID().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public Winter winter() {
		return winter;
	}

	public void setWinter(Winter winter) {
		this.winter = winter;
	}

}
