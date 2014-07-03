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
import java.net.URISyntaxException;
import java.util.UUID;

import de.uniko.west.winter.annotation.winter_id;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class SmallObject implements RDFSerializable {

	Winter winter = null;
	
	@winter_id("http://smallInfo/type")
	URI id = null;
	
	public SmallObject() {
		try {
			this.id = new URI("http://www.test.de/SmallObject#" + UUID.randomUUID().toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.west.winter.core.interfaces.RdfSerialisable#winter()
	 */
	@Override
	public Winter winter() {
		// TODO Auto-generated method stub
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
