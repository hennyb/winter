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

import de.uniko.west.winter.annotation.winter_query;
import de.uniko.west.winter.annotation.winter_var;
import de.uniko.west.winter.core.interfaces.RDFSerializable;
import de.uniko.west.winter.core.interfaces.Winter;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
@winter_query("?refObj1 test:subrelpred ?refObj2.")
public class SubrelationObject implements RDFSerializable{
	
	Winter winter = null;
	
	@winter_var("refObj1")
	SmallObject obj4 = null;
	
	@winter_var("refObj2")
	LargeObject obj5 = null;
	
	public SubrelationObject(LargeObject object) {
		obj4 = new SmallObject();
		obj5 = object;
		if(object == null) obj5 = new LargeObject("SubRelObjObj5");
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
