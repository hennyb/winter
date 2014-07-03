/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.uniko.west.winter.test.infostructure;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.annotation.winter_predicate;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class LargeObject implements RDFSerializable {

	Winter winter = null;
	
	@winter_id("test:largeImage")
	URI id = URI.create("http://www.test.de/testLargeObject#" + UUID.randomUUID().toString());
	
	@winter_predicate("test:hasName")
	String name = "";
	
	@winter_predicate("test:fieldCol")
	Vector<Integer> fieldCol = new Vector<Integer>(Arrays.asList(0,1,2,3,4)); 
	
	@winter_predicate("test:obj1")
	SmallObject obj1 = new SmallObject();
	
	@winter_predicate("test:objCol")
	Vector<RDFSerializable> objCol = new Vector<RDFSerializable>(Arrays.asList(new SmallObject(), new SmallObject(), new SmallObject()));

	public LargeObject(String name) {
		this.name = name;
//		try {
//			this.id = new URI("http://test:LargeObject#" + UUID.randomUUID().toString());
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@winter_predicate("test:hasName")
	public void setName(String name){
		this.name = name;
	}

	@winter_predicate("test:hasName")
	public String getName(){
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see de.west.winter.core.interfaces.RdfSerialisable#winter()
	 */
	@Override
	public Winter winter() {
		return winter;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.core.interfaces.RdfSerialisable#setWinter(de.west.winter.core.Winter)
	 */
	@Override
	public void setWinter(Winter winter) {
		this.winter = winter;
	}
}
