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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.bakadesho.baddecaf.classfile.attributes.CodeAttribute;

/**
 * ClassMethod
 * 
 * @author "gidouin"
 *
 */
public class ClassMethod {

	private short accessFlags;
	
	private String name;
	
	private String descriptor;
	
	private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	public ClassMethod(String name, short accessFlags, String descriptor) {
		this.name = name;
		this.accessFlags = accessFlags;
		this.descriptor = descriptor;
	}
	
	public void addAttribute(final Map<String, Attribute> attributes) {
		this.attributes.putAll(attributes);
	}
	
	public String toString() {
		return name + " type=" + descriptor + " " + AccessFlags.getModifiers(accessFlags);
	}
	
	public CodeAttribute getCodeAttribute() {
		return (CodeAttribute) attributes.get(CodeAttribute.CODE_ATTRIBUTE);
	}
	
	public String[] getParametersTypes() {
		final List<String> list = new ArrayList<String>();
		final int posEnd = descriptor.lastIndexOf(")");
		final String parameters = descriptor.substring(1, posEnd);
		StringTokenizer stoken = new StringTokenizer(parameters, ",");
		while (stoken.hasMoreElements()) {
			list.add((String) stoken.nextElement());
		}
		return list.toArray(new String[0]);
	}
	
	public String getReturnType() {
		final int posStart = descriptor.indexOf(")");
		final int posEnd = descriptor.lastIndexOf(";");
		return descriptor.substring(1 + posStart, posEnd);
	}
	
	
	public short getAccessFlags() {
		return accessFlags;
	}
	
	

}
