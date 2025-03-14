package me.coley.recaf.util.visitor;

import me.coley.recaf.RecafConstants;
import me.coley.recaf.util.AccessFlag;
import org.objectweb.asm.*;

/**
 * Visitor that removes most information from a class not needed for compiling against.
 *
 * @author Matt Coley
 */
public class ClassHollowingVisitor extends ClassVisitor {
	/**
	 * @param cv
	 * 		Parent visitor.
	 */
	public ClassHollowingVisitor(ClassVisitor cv) {
		super(RecafConstants.getAsmVersion(), cv);
	}

	@Override
	public void visitSource(String source, String debug) {
		// Skip
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		// Skip
		return null;
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		// Skip
		return null;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		// Skip private fields
		if (AccessFlag.isPrivate(access))
			return null;
		FieldVisitor fv = super.visitField(access, name, descriptor, signature, value);
		return new FieldHollower(fv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		// Skip private methods
		if (AccessFlag.isPrivate(access))
			return null;
		boolean isAbstract = AccessFlag.isAbstract(access);
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		return new MethodHollower(mv, isAbstract, Type.getReturnType(desc));
	}

	/**
	 * Visitor that removes most information from a field not needed for compiling against.
	 */
	public static class FieldHollower extends FieldVisitor {
		/**
		 * @param fv
		 * 		Parent field visitor.
		 */
		public FieldHollower(FieldVisitor fv) {
			super(RecafConstants.getAsmVersion(), fv);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
			// Skip
			return null;
		}

		@Override
		public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
			// Skip
			return null;
		}
	}

	/**
	 * Visitor that removes most information from a method not needed for compiling against.
	 */
	public static class MethodHollower extends MethodVisitor {
		private final Type retType;
		private final boolean isAbstract;

		/**
		 * @param mv
		 * 		Parent method visitor.
		 * @param isAbstract
		 * 		Is the method being visited abstract?
		 * @param retType
		 * 		Return type of the method being visited.
		 */
		public MethodHollower(MethodVisitor mv, boolean isAbstract, Type retType) {
			super(RecafConstants.getAsmVersion(), mv);
			this.isAbstract = isAbstract;
			this.retType = retType;
		}

		@Override
		public void visitEnd() {
			// Skip making a dummy body if the method is abstract
			if (isAbstract)
				return;
			// Hollowed out method will only be a basic return
			switch (retType.getSort()) {
				case Type.VOID:
					mv.visitInsn(Opcodes.RETURN);
					break;
				case Type.BOOLEAN:
				case Type.CHAR:
				case Type.BYTE:
				case Type.SHORT:
				case Type.INT:
					mv.visitInsn(Opcodes.ICONST_0);
					mv.visitInsn(Opcodes.IRETURN);
					break;
				case Type.FLOAT:
					mv.visitInsn(Opcodes.FCONST_0);
					mv.visitInsn(Opcodes.FRETURN);
					break;
				case Type.DOUBLE:
					mv.visitInsn(Opcodes.DCONST_0);
					mv.visitInsn(Opcodes.DRETURN);
					break;
				case Type.LONG:
					mv.visitInsn(Opcodes.LCONST_0);
					mv.visitInsn(Opcodes.LRETURN);
					break;
				default:
					mv.visitInsn(Opcodes.ACONST_NULL);
					mv.visitInsn(Opcodes.ARETURN);
					break;
			}
			super.visitEnd();
		}

		@Override
		public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
			// Skip
			return null;
		}

		@Override
		public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
			// Skip
			return null;
		}

		@Override
		public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
			// Skip
		}

		@Override
		public void visitInsn(int opcode) {
			// Skip
		}

		@Override
		public void visitIntInsn(int opcode, int operand) {
			// Skip
		}

		@Override
		public void visitVarInsn(int opcode, int var) {
			// Skip
		}

		@Override
		public void visitTypeInsn(int opcode, String type) {
			// Skip
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
			// Skip
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean itf) {
			// Skip
		}

		@Override
		public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
										   Object... bootstrapMethodArguments) {
			// Skip
		}

		@Override
		public void visitJumpInsn(int opcode, Label label) {
			// Skip
		}

		@Override
		public void visitLabel(Label label) {
			// Skip
		}

		@Override
		public void visitLdcInsn(Object value) {
			// Skip
		}

		@Override
		public void visitIincInsn(int var, int increment) {
			// Skip
		}

		@Override
		public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
			// Skip
		}

		@Override
		public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
			// Skip
		}

		@Override
		public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
			// Skip
		}

		@Override
		public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath,
													 String desc, boolean visible) {
			// Skip
			return null;
		}

		@Override
		public void visitLocalVariable(String name, String descriptor, String signature,
									   Label start, Label end, int index) {
			// Skip
		}

		@Override
		public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start,
															  Label[] end, int[] index, String desc,
															  boolean visible) {
			// Skip
			return null;
		}

		@Override
		public void visitLineNumber(int line, Label start) {
			// Skip
		}

		@Override
		public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
			// Skip - Only the exceptions matter. Internal blocks can be tossed.
		}

		@Override
		public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc,
														 boolean visible) {
			// Skip
			return null;
		}
	}
}
