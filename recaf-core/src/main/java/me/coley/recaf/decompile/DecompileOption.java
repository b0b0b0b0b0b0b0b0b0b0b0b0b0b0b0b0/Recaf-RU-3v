package me.coley.recaf.decompile;

import me.coley.recaf.plugin.tools.ToolOption;

/**
 * Wrapper for decompiler options so each decompiler option set is not handled specifically based on implementation.
 *
 * @param <T>
 * 		Option type. Exposed as {@link #getValueType()}.
 *
 * @author Matt Coley
 */
public class DecompileOption<T> extends ToolOption<T> {
	/**
	 * @param valueType
	 * 		Type of supported values.
	 * @param optionName
	 * 		Option name as defined by the decompiler.
	 * @param description
	 * 		Description of what the option controls in the decompiler output.
	 * @param defaultValue
	 * 		Default value for the option.
	 */
	public DecompileOption(Class<T> valueType, String optionName, String description, T defaultValue) {
		this(valueType, optionName, optionName, description, defaultValue);
	}

	/**
	 * @param valueType
	 * 		Type of supported values.
	 * @param optionName
	 * 		Option name as defined by the decompiler.
	 * @param recafNameAlias
	 * 		Alias for the option name, used by Recaf to consolidate the
	 * 		<i>"same"</i> feature across different decompiler implementations.
	 * @param description
	 * 		Description of what the option controls in the decompiler output.
	 * @param defaultValue
	 * 		Default value for the option.
	 */
	public DecompileOption(Class<T> valueType, String optionName, String recafNameAlias, String description,
						   T defaultValue) {
		super(valueType, optionName, recafNameAlias, description, defaultValue);
	}
}
