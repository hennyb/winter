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


package de.uniko.west.winter.core.interfaces;

/**
 * 
 * Classes implementing RDFSerializable gain access to the core persistance features of winter. <br>
 * Implementing RDFSerializable ensures that they are found by preprocessing.<br><br>
 * 
 * In workflow their annotated fields are mapped to database.<br><br>
 * 
 * Each implementing class should store their own instance of a winter object (without manipulating it).<br>  
 * This object provides access to core features.<br><br>
 *
 * Implementations of RDFSerializable must contain the default constructor.<br> 
 *
 * @author Stefan Scheglmann, Frederik Jochum
 */
public interface RDFSerializable {
   public Winter winter();
   void setWinter(Winter winter);
}
