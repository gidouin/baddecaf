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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.bakadesho.baddecaf.classfile.attributes.CodeAttribute;
import net.bakadesho.baddecaf.classfile.attributes.ConstantValueAttribute;
import net.bakadesho.baddecaf.classfile.attributes.SignatureAttribute;
import net.bakadesho.baddecaf.classfile.attributes.UnsupportedAttribute;
import net.bakadesho.baddecaf.classfile.constantpool.ConstantPool;

/**
 * 
 * Factory for Class Attributes
 * 
 * @author "gidouin"
 *
 */
final public class AttributeFactory {

	/** Constant Pool associated */
	private ConstantPool cp;
	
	/** 
	 * Create an AttributeFactory associated to a ConstantPool
	 * @param cp
	 */
	public AttributeFactory(ConstantPool cp) {
		this.cp = cp;
	}
	
	final public ConstantPool cp() {
		return cp;
	}
	
	/**
	 * Create a class Attribute from the given InputStream 
	 * 
	 * @param dis
	 * @return a class Attribtue
	 * @throws IOException
	 */
	public Attribute createAttribute(final DataInputStream dis) throws IOException {
		final short nameIndex = dis.readShort();
		final int length = dis.readInt();
		
		String name = cp.getText(nameIndex);

		//
		// ConstantValue Attribute
		//

		if ("ConstantValue".equals(name)) {
			return new ConstantValueAttribute(dis, this);
		}
		
		//
		// Code Attribute
		//

		if ("Code".equals(name)) {
			return new CodeAttribute(dis, this);
		}
		
		//
		// Signature
		//
		
		if ("Signature".equals(name)) {
			return new SignatureAttribute(dis, this);
		}

		//
		// unsupported Attribute
		// TODO: implement those
		//

		dis.skip(length);
		return new UnsupportedAttribute(name);
	}
	
	/**
	 * Create class Attributes reading the given InputStream 
	 * @param dis
	 * @return a Map associating attribute name to Attribute
	 * @throws IOException
	 */
	public Map<String, Attribute> getAttributes(final DataInputStream dis) throws IOException {
		final Map<String, Attribute> attributes = new LinkedHashMap<String, Attribute>();
		
		final short count = dis.readShort();
		for (int i = 0; i < count; i++) {
			final Attribute attribute = createAttribute(dis);
			if (null != attribute) {
				attributes.put(attribute.getName(), attribute);
				System.out.println("\t\t" + attribute);
			}
		}
		
		return attributes;
	}
	
}
