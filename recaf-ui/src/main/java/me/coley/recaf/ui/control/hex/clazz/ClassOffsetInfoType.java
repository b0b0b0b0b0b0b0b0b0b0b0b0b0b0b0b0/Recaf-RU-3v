package me.coley.recaf.ui.control.hex.clazz;

/**
 * Offset type to differentiate handling of {@link ClassOffsetInfo} values.
 *
 * @author Matt Coley
 */
public enum ClassOffsetInfoType {
	//// Top level
	MAGIC,
	MINOR_VERSION,
	MAJOR_VERSION,
	CONSTANT_POOL_COUNT,
	CONSTANT_POOL,
	ACCESS_FLAGS,
	THIS_CLASS,
	SUPER_CLASS,
	INTERFACES_COUNT,
	INTERFACES,
	INTERFACE_INDEX,
	FIELDS_COUNT,
	FIELDS,
	FIELD_INFO,
	METHODS_COUNT,
	METHODS,
	METHOD_INFO,
	ATTRIBUTES_COUNT,
	CLASS_ATTRIBUTES,
	//// Constant pool
	CP_UTF8,
	CP_INTEGER,
	CP_FLOAT,
	CP_LONG,
	CP_DOUBLE,
	CP_CLASS,
	CP_STRING,
	CP_FIELD_REF,
	CP_METHOD_REF,
	CP_INTERFACE_METHOD_REF,
	CP_NAME_TYPE,
	CP_METHOD_HANDLE,
	CP_METHOD_TYPE,
	CP_DYNAMIC,
	CP_INVOKE_DYNAMIC,
	CP_MODULE,
	CP_PACKAGE,
	//// Field
	FIELD_ACC_FLAGS,
	FIELD_NAME_INDEX,
	FIELD_DESC_INDEX,
	FIELD_ATTRIBUTES_COUNT,
	FIELD_ATTRIBUTES,
	//// Method
	METHOD_ACC_FLAGS,
	METHOD_NAME_INDEX,
	METHOD_DESC_INDEX,
	METHOD_ATTRIBUTES_COUNT,
	METHOD_ATTRIBUTES,
	//// Attribute
	ATTRIBUTE_INFO,
	ATTRIBUTE_NAME_INDEX,
	ATTRIBUTE_LENGTH,
	ATTRIBUTE_DATA,
	ATTRIBUTE_DATA_UNSUPPORTED,
	// Bootstrap method
	BOOTSTRAP_METHODS_COUNT,
	BOOTSTRAP_METHODS,
	BOOTSTRAP_METHOD,
	BOOTSTRAP_METHOD_REF_INDEX,
	BOOTSTRAP_METHOD_ARGS_COUNT,
	BOOTSTRAP_METHOD_ARGS,
	BOOTSTRAP_METHOD_ARG,
	// Code
	CODE_MAX_STACK,
	CODE_MAX_LOCALS,
	CODE_LENGTH,
	CODE_BYTECODE,
	CODE_EXCEPTIONS_COUNT,
	CODE_EXCEPTIONS,
	CODE_EXCEPTION,
	CODE_ATTRIBUTE_COUNT,
	CODE_ATTRIBUTES,
	EXCEPTION_START_PC,
	EXCEPTION_END_PC,
	EXCEPTION_HANDLER_PC,
	EXCEPTION_TYPE_INDEX,
	// Constant value
	CONSTANT_VALUE_INDEX,
	// Element values
	ELEMENT_VALUE_INFO,
	ELEMENT_VALUE_TAG,
	ELEMENT_VALUE_ARRAY_COUNT,
	ELEMENT_VALUE_ARRAY,
	ELEMENT_VALUE_CLASS_INDEX,
	ELEMENT_VALUE_ENUM_TYPE_INDEX,
	ELEMENT_VALUE_ENUM_NAME_INDEX,
	ELEMENT_VALUE_PRIMITIVE_INDEX,
	ELEMENT_VALUE_UTF_INDEX,
	// Annotations
	ANNOTATIONS_COUNT,
	ANNOTATIONS,
	ANNOTATION,
	ANNOTATION_TARGET_TYPE,
	ANNOTATION_TARGET_INFO,
	ANNOTATION_TYPE_PATH,
	ANNOTATION_TYPE_INDEX,
	ANNOTATION_VALUES_COUNT,
	ANNOTATION_VALUES,
	ANNOTATION_VALUE_KEY_NAME_INDEX,
	PARAMETER_ANNOTATIONS_COUNT,
	PARAMETER_ANNOTATIONS_COUNT_FOR_PARAM,
	PARAMETER_ANNOTATIONS,
	PARAMETER_ANNOTATIONS_FOR_ARG,
	// Inner / outer class relations
	ENCLOSING_METHOD_CLASS,
	ENCLOSING_METHOD_METHOD,
	NEST_HOST_CLASS,
	INNER_CLASSES_COUNT,
	INNER_CLASSES,
	INNER_CLASS,
	INNER_CLASS_INNER_INFO,
	INNER_CLASS_OUTER_INFO,
	INNER_CLASS_INNER_NAME,
	INNER_CLASS_INNER_ACCESS,
}
