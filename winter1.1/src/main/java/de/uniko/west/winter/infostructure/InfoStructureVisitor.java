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
package de.uniko.west.winter.infostructure;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public abstract class InfoStructureVisitor {

	public abstract Object visit(InfoNode node, Object data);
	public abstract Object visit(InnerNode node, Object data);
	public abstract Object visit(FieldNode node, Object data);
	public abstract Object visit(LeafNode node, Object data);
	public abstract Object visit(ObjectNode node, Object data);
	
}
