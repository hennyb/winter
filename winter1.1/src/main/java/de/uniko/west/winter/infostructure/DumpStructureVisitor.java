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
public class DumpStructureVisitor extends InfoStructureVisitor {
 
	private static final String TAB = "   ";
	private static final String NODE = ">";
	
	
	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoStructureVisitor#visit(de.west.winter.infostructure.InfoNode, java.lang.Object)
	 */
	@Override
	public Object visit(InfoNode node, Object data) {
		String result = data + NODE + "*DUMMYINFONODE*" + "\n";
		result += (String) visitChildren(node, data + TAB);
		return result;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoStructureVisitor#visit(de.west.winter.infostructure.InnerNode, java.lang.Object)
	 */
	@Override
	public Object visit(InnerNode node, Object data) {
		String result = data + NODE +"*DUMMYINNERNODE*" + "\n";
		result += (String) visitChildren(node, data + TAB);
		return result;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoStructureVisitor#visit(de.west.winter.infostructure.FieldNode, java.lang.Object)
	 */
	@Override
	public Object visit(FieldNode node, Object data) {
		System.out.println("VISITING FieldNode: " + node.toString());
		String result = data + NODE +node.toString() + "\n";
		result += (String) visitChildren(node, data + TAB);
		return result;
	}

	/* (non-Javadoc)
	 * @see de.west.winter.infostructure.InfoStructureVisitor#visit(de.west.winter.infostructure.ObjectNode, java.lang.Object)
	 */
	@Override
	public Object visit(ObjectNode node, Object data) {
		System.out.println("VISITING ObjectNode: " + node.toString());
		String result = data + NODE +node.toString() + "\n";
		result += (String) visitChildren(node, data + TAB);
		return result;

	}
	
	/**
	 * 
	 */
	public Object visitChildren(InfoNode node, Object data) {
		String newData = "";
		for (InfoNode child : node.getChildren()) {
			newData += (String) child.acceptVisitor(this, data);
		}
		return newData;
	}

	/* (non-Javadoc)
	 * @see de.uniko.west.winter.infostructure.InfoStructureVisitor#visit(de.uniko.west.winter.infostructure.LeafNode, java.lang.Object)
	 */
	@Override
	public Object visit(LeafNode node, Object data) {
		String result = data + NODE +"*DUMMYLEAFNODE*" + "\n";
		result += (String) visitChildren(node, data + TAB);
		return result;
	}
	

}
