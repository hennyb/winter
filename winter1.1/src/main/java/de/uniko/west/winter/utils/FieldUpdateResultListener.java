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

/**
 * 
 */
package de.uniko.west.winter.utils;


import de.uniko.west.winter.core.QueryResult;
import de.uniko.west.winter.core.serializing.Binding;
import de.uniko.west.winter.infostructure.FieldNode;
import de.uniko.west.winter.utils.interfaces.QueryResultListener;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class FieldUpdateResultListener implements QueryResultListener {

	private FieldNode fieldNode;
	private String varName;
	
	public FieldUpdateResultListener(FieldNode fieldNodeByFieldName) {
		fieldNode = fieldNodeByFieldName;
		this.varName = fieldNode.getVar(); 
	}

	@Override
	public void finished(QueryResult result) {
		if (!result.getVars().contains(varName)){
			System.out.println("var not in result");
			return;
		}
		Binding binding = new Binding(fieldNode);
		binding.fillFromQueryResult(result, varName);
		binding.unbind();
	}


}
