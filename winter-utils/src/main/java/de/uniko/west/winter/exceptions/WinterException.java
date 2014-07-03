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
package de.uniko.west.winter.exceptions;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class WinterException extends RuntimeException{

	public WinterException() {
	}
	
	public WinterException(Throwable cause) {
		super(cause);
	}

	public WinterException(String message, Throwable cause) {
		super(message, cause);
	}

	public WinterException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -9203045424896837186L;

}
