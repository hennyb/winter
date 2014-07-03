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

package de.uniko.west.winter.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * This annotation is used to identify Fields.
 * The annotation value is supposed to correlate with variables from a
 * @winter_query annotation.
 * 
 * Example:
 * 
 * @winter_query("?myEntity <myrelation> ?myValue.")
 * class ClassA implements RDFSerializable{
 * 	@winter_var("myValue")
 * 	String someValue;
 * 	(...)
 * }
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface winter_var {
	String value();
}
