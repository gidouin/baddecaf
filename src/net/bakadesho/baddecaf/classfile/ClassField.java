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
package net.bakadesho.baddecaf.classfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Field
 * 
 * @author "gidouin"
 *
 */
public class ClassField {

	private String name;
	
	private short accessFlags;
	
	private String descriptor;
		
	private Map<String, Attribute> attributes  = new HashMap<String, Attribute>();
	
	public ClassField(String name, short accessFlags, String descriptor) {
		this.name = name;
		this.accessFlags = accessFlags;
		this.descriptor = descriptor;
	}
	
	public void addAttribute(final Map<String, Attribute> fieldAttributes) {
		this.attributes.putAll(attributes);
	}
	
	public String toString() {
		return name + " type=" + descriptor + " " + AccessFlags.getModifiers(accessFlags);
	}
	
}
