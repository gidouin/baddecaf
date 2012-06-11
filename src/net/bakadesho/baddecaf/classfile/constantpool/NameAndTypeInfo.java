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
package net.bakadesho.baddecaf.classfile.constantpool;

public class NameAndTypeInfo implements Constant {

	private short nameIndex;
	private short descriptorIndex;
	
	public NameAndTypeInfo(short nameIndex, short descriptorIndex) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	public String getText() {
		return "name and type info.getText()";
	}

	public short getNameIndex() {
		return nameIndex;
	}
	public short getDescriptorIndex() {
		return descriptorIndex;
	}

}
