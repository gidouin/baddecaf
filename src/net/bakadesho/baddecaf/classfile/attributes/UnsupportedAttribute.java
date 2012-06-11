/*******************************************************************************
 * Copyright (c) 2012 Frédéric Gidouin.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * Contributors:
 *     Frédéric Gidouin - initial API and implementation
 ******************************************************************************/
package net.bakadesho.baddecaf.classfile.attributes;

/**
 * (yet) unsupported Attribute
 * 
 * @author "gidouin"
 *
 */
public class UnsupportedAttribute extends AbstractAttribute {

	public UnsupportedAttribute(String name) {
		super(name);
	}

	public String toString() {
		return "*** Unsupported Attribute " + getName();
	}
}
