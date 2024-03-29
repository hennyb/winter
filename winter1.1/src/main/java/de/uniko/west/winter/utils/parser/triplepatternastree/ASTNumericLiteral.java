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

/* Generated By:JJTree: Do not edit this line. ASTNumericLiteral.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniko.west.winter.utils.parser.triplepatternastree;

import java.net.URI;

import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParser;
import de.uniko.west.winter.utils.parser.triplepatternparser.SPARQLPatternParserVisitor;
import de.uniko.west.winter.utils.parser.triplepatternparser.VisitorException;

public
class ASTNumericLiteral extends SimpleNode {
	
    private String value;

    private URI datatype;
	
  public ASTNumericLiteral(int id) {
    super(id);
  }

  public ASTNumericLiteral(SPARQLPatternParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLPatternParserVisitor visitor, Object data) throws VisitorException {
    return visitor.visit(this, data);
  }
  

  public String getValue() {
      return value;
  }

  public void setValue(String value) {
      this.value = value;
  }

  public URI getDatatype() {
      return datatype;
  }

  public void setDatatype(URI datatype) {
      this.datatype = datatype;
  }

  @Override
  public String toString() {
      return super.toString() + " (value=" + value + ", datatype=" + datatype + ")";
  }
  @Override
  public boolean jjtCompareTo(Node node) {
  	ASTNumericLiteral numericNode = null;
  	if(this.getClass()==node.getClass()){
  		numericNode = (ASTNumericLiteral)node;
  		if(this.jjtGetNumChildren()==numericNode.jjtGetNumChildren() &&
  				this.getValue().equals(numericNode.getValue()) &&
  				this.getDatatype().toString().equals(numericNode.getDatatype().toString())){
  			return true;
  		} else 
  			return false;
  	} else
  		return false;
  }
}
/* JavaCC - OriginalChecksum=bc36dc125934d9b0429dabd020e5427c (do not edit this line) */
