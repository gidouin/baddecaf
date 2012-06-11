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

import java.io.DataInputStream;
import java.io.IOException;

import net.bakadesho.baddecaf.classfile.AttributeFactory;

public class ConstantValueAttribute extends AbstractAttribute {

	public static final String CONSTANT_VALUE_ATTRIBUTE = "ConstantValue";

	private short constantIndex;
	
	private String text;
	
	public ConstantValueAttribute(final DataInputStream dis, AttributeFactory af) throws IOException {
		super(CONSTANT_VALUE_ATTRIBUTE);
		constantIndex = dis.readShort();
		this.text = af.cp().getText(constantIndex);
	}
	
	public String toString() {
		return getName() + " constantIndex=" + constantIndex + " => " + text;
	}
}
