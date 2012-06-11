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
package net.bakadesho.baddecaf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.bakadesho.baddecaf.bytecode.Opcode;
import net.bakadesho.baddecaf.bytecode.OpcodeFactory;
import net.bakadesho.baddecaf.classfile.AccessFlags;
import net.bakadesho.baddecaf.classfile.ClassMethod;
import net.bakadesho.baddecaf.classfile.DejaClass;
import net.bakadesho.baddecaf.classfile.attributes.CodeAttribute;
import net.bakadesho.baddecaf.classfile.constantpool.RefInfo;

/**
 * Transform a class into a bytecode representation suitable
 * to the decompilation pass.
 * 
 * @author "gidouin"
 *
 */
public class Debytecoder {

	/**
	 * Debytecode a class
	 * 
	 * @param clazz DejaClass to debytecode
	 */
	public void debytecode(final DejaClass clazz) {
		Set<ClassMethod> methods = clazz.getMethods();

		for (ClassMethod method : methods) {
			System.out.println("* Debytecoding method " + method);
			debytecode(clazz, method);
			System.out.println();
		}
	}
	
	/**
	 * Debytecode a method
	 * 
	 * @param clazz DejaClass 
	 * @param method ClassMethod to debytecode
	 */
	public void debytecode(final DejaClass clazz, final ClassMethod method) {
		final CodeAttribute methodCode = method.getCodeAttribute();
		System.out.println(methodCode);
		
		//
		// prepare locals table from parameters
		//

		final boolean isStatic = AccessFlags.Flags.ACC_STATIC.isSet(method.getAccessFlags()); 
		final String locals[] = new String[methodCode.getMaxLocals()];
		int nextLocals = 0;

		//
		// if not static, 1st local is a reference to the instance
		//

		if (!isStatic) {
			locals[nextLocals++] = "this";
		}

		//
		// subsequent locals are parameters
		//

		final String params[] = method.getParametersTypes();
		for (final String param: params) {
			locals[nextLocals++] = "parameter of type " + param; // TODO long, double
		}
		
		System.out.println(method.toString());
		
		//
		// iterate all bytecode to create Instructions to walk
		//

		Map<Integer, Opcode> instructions = new HashMap<Integer, Opcode>();		
		OpcodeFactory of = new OpcodeFactory();
		
		Stack<String> stack = new Stack<String>();
		
		byte[] bytecode = methodCode.getBytecode();
		for (int i = 0; i < bytecode.length; i++) {
			int offset = i;
			int opcode = bytecode[offset] & 0xff;
			Opcode instr = Opcode.UNKNOWN;
			
			switch (opcode) {
			
				//
				// iconst_<i> (-1..5)
				// push int constant
				//
			
				case 0x02:
				case 0x03:
				case 0x04:
				case 0x05:
				case 0x06:
				case 0x07:
				case 0x08: {
					int n = ((int)(short)opcode) - 3;
					instr = of.create("iconst_" + (n < 0 ? "m1" : n), null, (Object[]) null);
					stack.push("" + n);
					break;
				}
				
				//
				// bipush u1	(byte push)
				//
				
				case 0x10: {
					int n = bytecode[++i];
					
					instr = of.create("bipush", null, n);
					break;
				}
			
				//
				// ldc
				// push constant pool entry (8-bit) 
				//

				case 0x12: {
					int index = bytecode[++i] & 0xff;
					String constant = clazz.cp().get(index).getText();
					instr = of.create("ldc ", constant, "#" + index);
					stack.push(constant);
					break;
				}
			
				//
				// iload_0 .. iload_3
				// push int from local variable
				//
				
				case 0x1a:
				case 0x1b:
				case 0x1c:
				case 0x1d: {
					int n = opcode - 0x1a;
					
					instr = of.create("iload_" + n, locals[n], (Object[]) null);
					stack.push("" + n);
					break;
				}

				//
				// aload_0 .. aload_3
				// Load reference from local variable and push on stack
				//
			
				case 0x2a:
				case 0x2b:
				case 0x2c:
				case 0x2d: {
					int n = opcode - 0x2a;
					instr = of.create("aload_" + n, locals[n], (Object[]) null);
					stack.push(locals[n]);
					break;
				}
				
				//
				// istore u2
				//
				
				case 0x36: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8 ) | b2;
					
					instr = of.create("istore", "", "#" + n);
					break;
				}

				//
				// istore_n (0 .. 3)
				//
				
				case 0x3b:
				case 0x3c:
				case 0x3d:
				case 0x3e: {
					int n = opcode - 0x3b;
					
					instr = of.create("istore_" + n, null, (Object[]) null);
					break;
				}

				//
				// astore_n (0..3)
				// pop stack and store in local variable
				//
				
				case 0x4b:
				case 0x4c:
				case 0x4d:
				case 0x4e: {
					int n = opcode - 0x4b;
					
					instr = of.create("astore_" + n, "", (Object[]) null);
					break;
				}
				
				//
				// pop
				//
				
				case 0x57: {
					instr = of.create("pop", null, (Object[]) null);
					stack.pop();
					break;
				}
				
				//
				// dup
				// dupplicate top operand on stack 
				//
				
				case 0x59: {
					String str = stack.pop();
					stack.push(str);
					stack.push(str);
					instr = of.create("dup", str, (Object[]) null);
					break;
				}
				
				//
				// iinc	local (byte) index, (byte) const
				//
				
				case 0x84: {
					int variable = bytecode[++i] & 0xff;
					int increment = bytecode[++i];
					
					instr = of.create("iinc", null, variable, increment);
					break;
				}
				
				
				//
				// if_icmp<cond> u2
				//
				
				case 0x9f:
				case 0xa0:
				case 0xa1:
				case 0xa2:
				case 0xa3:
				case 0xa4: {
					int type = opcode - 0x9f;
					String cond = new String[] {"eq", "ne", "lt", "ge", "gt", "le"} [type];

					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (short)(offset + ((b1 << 8 ) | b2));

					instr = of.create("if_icmp" + cond, null, n);
					break;
				}
				
				//
				// goto u2
				//
				
				case 0xa7: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = offset + ((b1 << 8 ) | b2);
					
					instr = of.create("goto", null, n);
					break;
				}
				
				//
				// return
				//
				
				case 0xb1: {
					instr = of.create("return", null);
					break;
				}
				
				//
				// getstatic
				//
				
				case 0xb2: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8 ) | b2;
					
					RefInfo ref = (RefInfo) clazz.cp().get(n);
					String className = ref.getClassInfoName();
					String fieldName = ref.getName();
					String typeName  = ref.getType();
					
					instr = of.create("getstatic", "type:" + typeName + " class:" + className + " field:" + fieldName, "#" + n);
					stack.push(typeName);
					break;
				}
				
				//
				// putfield u2
				// pop objectref, value
				//
				
				case 0xb5: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8) | b2;
					
					RefInfo fieldRef = (RefInfo) clazz.cp().get(n);
					
					String className = fieldRef.getClassInfoName();
					String fieldName = fieldRef.getName();
					String typeName = fieldRef.getType();
					
					String value = stack.pop();
					String ref = stack.pop();
					
					instr = of.create("putfield", "field=" + fieldName + " of " + className + " (type=" + typeName + ") value=" + value, "#" + n);
					break;
				}
				
				//
				// invokevirtual  u2
				//

				case 0xb6: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8 ) | b2;

					RefInfo ref = (RefInfo) clazz.cp().get(n);
					String className = ref.getClassInfoName();
					String methodName = ref.getName();
					String typeName  = ref.getType();
					
					instr = of.create("invokevirtual", "method:" + methodName + " type:" + typeName + " class:" + className, "#" + n);
					//TODO: pop stack
					break;
				}

				//
				// invokespecial	u2
				//
					
				case 0xb7: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8 ) | b2;
					
					String on = "?stack?";
					if (!stack.isEmpty()) { 
						on = stack.pop();
					}
					
					instr = of.create("invokespecial", "(" +  on + " on stack) " +clazz.cp().getRef(n), "#" + n);
					break;
				}
					
				
				//
				// new u2
				//
				
				case 0xbb: {
					int b1 = bytecode[++i] & 0xff;
					int b2 = bytecode[++i] & 0xff;
					int n = (b1 << 8 ) | b2;
					
					String type = clazz.cp().get(n).getText();

					instr = of.create("new", type, "#" + n);
					stack.push(type);
					break;
				}
					
			}
			
			instructions.put(offset, instr);
			System.out.println(offset + "\t" + instr);
			
		}
		
		
	}

}
