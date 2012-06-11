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

public class RefInfo implements Constant {

	private short classIndex;
	
	private short nameAndTypeIndex;
	
	private ConstantPool cp;
	
	public RefInfo(short classIndex, short nameAndTypeIndex, ConstantPool cp) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
		this.cp = cp;
	}

	public String getText() {
		return "MethodRef.getText for " + toString();
	}
	
	public short getClassIndex() {
		return classIndex;
	}
	
	public short getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	public String getClassInfoName() {
		return cp.getClassInfoName(classIndex);
	}
	
	public String getName() {
		short nameIndex = cp.getNameAndTypeInfo(nameAndTypeIndex).getNameIndex();
		return cp.get(nameIndex).getText();
	}
	
	public String getType() {
		short nameIndex = cp.getNameAndTypeInfo(nameAndTypeIndex).getDescriptorIndex();
		return cp.get(nameIndex).getText();
		
	}
	
}
