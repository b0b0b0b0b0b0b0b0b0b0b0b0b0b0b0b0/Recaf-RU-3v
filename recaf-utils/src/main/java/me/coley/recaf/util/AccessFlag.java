package me.coley.recaf.util;

import org.objectweb.asm.Opcodes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Utility for handling access flags/modifiers.
 *
 * @author Matt Coley
 * @author Andy Li
 */
public enum AccessFlag {
	ACC_PUBLIC(Opcodes.ACC_PUBLIC, "public", true, Type.CLASS, Type.INNER_CLASS, Type.METHOD, Type.FIELD),
	ACC_PRIVATE(Opcodes.ACC_PRIVATE, "private", true, Type.INNER_CLASS, Type.METHOD, Type.FIELD),
	ACC_PROTECTED(Opcodes.ACC_PROTECTED, "protected", true, Type.INNER_CLASS, Type.METHOD, Type.FIELD),
	ACC_STATIC(Opcodes.ACC_STATIC, "static", true, Type.INNER_CLASS, Type.METHOD, Type.FIELD),
	ACC_FINAL(Opcodes.ACC_FINAL, "final", true, Type.CLASS, Type.INNER_CLASS, Type.METHOD, Type.FIELD, Type.PARAM),
	ACC_SYNCHRONIZED(Opcodes.ACC_SYNCHRONIZED, "synchronized", true, Type.METHOD),
	ACC_SUPER(Opcodes.ACC_SUPER, "super", false, Type.CLASS),
	ACC_BRIDGE(Opcodes.ACC_BRIDGE, "bridge", false, Type.METHOD),
	ACC_VOLATILE(Opcodes.ACC_VOLATILE, "volatile", true, Type.FIELD),
	ACC_VARARGS(Opcodes.ACC_VARARGS, "varargs", false, Type.METHOD),
	ACC_TRANSIENT(Opcodes.ACC_TRANSIENT, "transient", true, Type.FIELD),
	ACC_NATIVE(Opcodes.ACC_NATIVE, "native", true, Type.METHOD),
	ACC_INTERFACE(Opcodes.ACC_INTERFACE, "interface", true, Type.CLASS, Type.INNER_CLASS),
	ACC_ABSTRACT(Opcodes.ACC_ABSTRACT, "abstract", true, Type.CLASS, Type.INNER_CLASS, Type.METHOD),
	ACC_STRICT(Opcodes.ACC_STRICT, "strictfp", true, Type.METHOD),
	ACC_SYNTHETIC(Opcodes.ACC_SYNTHETIC, "synthetic", false,
			Type.CLASS, Type.INNER_CLASS, Type.METHOD, Type.FIELD, Type.PARAM),
	ACC_ANNOTATION(Opcodes.ACC_ANNOTATION, "annotation-interface", false, Type.CLASS, Type.INNER_CLASS),
	ACC_ENUM(Opcodes.ACC_ENUM, "enum", true, Type.CLASS, Type.INNER_CLASS, Type.FIELD),
	ACC_OPEN(Opcodes.ACC_OPEN, "open", false, Type.MODULE),
	ACC_STATIC_PHASE(Opcodes.ACC_STATIC_PHASE, "static-phase", false, Type.MODULE),
	ACC_TRANSITIVE(Opcodes.ACC_TRANSITIVE, "transitive", false, Type.MODULE),
	ACC_MODULE(Opcodes.ACC_MODULE, "module", false, Type.CLASS),
	ACC_MANDATED(Opcodes.ACC_MANDATED, "mandated", false, Type.PARAM);

	private static final Multimap<Integer, AccessFlag, Set<AccessFlag>> maskToFlagsMap;
	private static final Map<String, AccessFlag> nameToFlagMap;
	private static final Multimap<Type, AccessFlag, Set<AccessFlag>> typeToFlagsMap;

	static {
		AccessFlag[] flags = values();
		var maskMap = MultimapBuilder
				.<Integer, AccessFlag>hashKeys()
				.enumValues()
				.build();
		Map<String, AccessFlag> nameMap = new LinkedHashMap<>(flags.length);
		var typeMap = MultimapBuilder
				.<Type, AccessFlag>enumKeys()
				.enumValues()
				.build();
		for (AccessFlag flag : flags) {
			maskMap.put(flag.mask, flag);
			nameMap.put(flag.name, flag);
			flag.types.forEach(type -> typeMap.put(type, flag));
		}
		maskToFlagsMap = maskMap;
		nameToFlagMap = nameMap;
		typeToFlagsMap = typeMap;
		Type.populateOrder();  // lazy load
	}

	/**
	 * @param mask
	 * 		Access flags mask.
	 *
	 * @return Set of applicable flags.
	 */
	public static Set<AccessFlag> getFlags(int mask) {
		return maskToFlagsMap.get(mask);
	}

	/**
	 * @param type
	 * 		Flag type.
	 *
	 * @return Set of flags that belong to the type group.
	 */
	public static Set<AccessFlag> getApplicableFlags(Type type) {
		return typeToFlagsMap.get(type);
	}

	/**
	 * @param type
	 * 		Flag type.
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return Set of flags that belong to the type group in the access flag mask.
	 */
	public static Set<AccessFlag> getApplicableFlags(Type type, int acc) {
		Set<AccessFlag> flags = EnumSet.noneOf(AccessFlag.class);
		for (AccessFlag applicableFlag : getApplicableFlags(type))
			if (applicableFlag.has(acc))
				flags.add(applicableFlag);
		return flags;
	}

	/**
	 * @param type
	 * 		Flag type.
	 * @param flags
	 * 		Collection of flags.
	 *
	 * @return Sorted order of flags.
	 */
	public static List<AccessFlag> sort(Type type, Collection<AccessFlag> flags) {
		List<AccessFlag> list;
		if (flags instanceof List) {
			list = (List<AccessFlag>) flags;
		} else {
			list = new ArrayList<>(flags);
		}
		list.sort(type.recommendOrderComparator);
		return list;
	}

	/**
	 * @param name
	 * 		Name of flag.
	 *
	 * @return Flag from name.
	 */
	public static AccessFlag getFlag(String name) {
		return nameToFlagMap.get(name);
	}

	/**
	 * @param text
	 * 		Text containing one or more flags, separated by spaces.
	 *
	 * @return Flags from text.
	 */
	public static List<AccessFlag> getFlags(String text) {
		String[] parts = text.split("\\s+");
		List<AccessFlag> flags = new ArrayList<>();
		for (String part : parts) {
			AccessFlag flag = getFlag(part);
			if (flag != null)
				flags.add(flag);
		}
		return flags;
	}

	/**
	 * @param flags
	 * 		Array of access flags.
	 *
	 * @return Access flag mask.
	 */
	public static int createAccess(AccessFlag... flags) {
		int acc = 0;
		for (AccessFlag flag : flags) acc = flag.set(acc);
		return acc;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Array of flags to check exist in the mask.
	 *
	 * @return {@code true} if all specified flags exist in the mask.
	 */
	public static boolean hasAll(int acc, AccessFlag... flags) {
		for (AccessFlag flag : flags) {
			if (!flag.has(acc)) return false;
		}
		return true;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Collection of flags to check exist in the mask.
	 *
	 * @return {@code true} if all specified flags exist in the mask.
	 */
	public static boolean hasAll(int acc, Collection<AccessFlag> flags) {
		for (AccessFlag flag : flags) {
			if (!flag.has(acc)) return false;
		}
		return true;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Array of flags to check exist in the mask.
	 *
	 * @return {@code true} if none of the specified flags exist in the mask.
	 */
	public static boolean hasNone(int acc, AccessFlag... flags) {
		for (AccessFlag flag : flags) {
			if (flag.has(acc)) return false;
		}
		return true;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Collection of flags to check exist in the mask.
	 *
	 * @return {@code true} if none of the specified flags exist in the mask.
	 */
	public static boolean hasNone(int acc, Collection<AccessFlag> flags) {
		for (AccessFlag flag : flags) {
			if (flag.has(acc)) return false;
		}
		return true;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Array of flags to check exist in the mask.
	 *
	 * @return {@code true} if any of the specified flags exist in the mask.
	 */
	public static boolean hasAny(int acc, AccessFlag... flags) {
		for (AccessFlag flag : flags) {
			if (flag.has(acc)) return true;
		}
		return false;
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 * @param flags
	 * 		Collection of flags to check exist in the mask.
	 *
	 * @return {@code true} if any of the specified flags exist in the mask.
	 */
	public static boolean hasAny(int acc, Collection<AccessFlag> flags) {
		for (AccessFlag flag : flags) {
			if (flag.has(acc)) return true;
		}
		return false;
	}

	/**
	 * Flag mask value.
	 */
	private final int mask;
	/**
	 * Flag identifier.
	 */
	private final String name;
	/**
	 * If the flag is treated as a keyword by the Java compiler.
	 */
	private final boolean isKeyword;
	/**
	 * Applicable flag type groups.
	 */
	private final Set<Type> types;

	AccessFlag(int mask, String name, boolean isKeyword, Set<Type> types) {
		this.mask = mask;
		this.name = name;
		this.isKeyword = isKeyword;
		this.types = Collections.unmodifiableSet(types);
	}

	AccessFlag(int mask, String name, boolean isKeyword, Type type) {
		this(mask, name, isKeyword, Collections.singleton(type));
	}

	AccessFlag(int mask, String name, boolean isKeyword, Type firstType, Type... restTypes) {
		this(mask, name, isKeyword, EnumSet.of(firstType, restTypes));
	}

	/**
	 * @return Access flag mask.
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * @param access
	 * 		Access flag mask.
	 *
	 * @return {@code true} if the flag contains the mask.
	 */
	public boolean has(int access) {
		return (access & mask) != 0;
	}

	/**
	 * @param access
	 * 		Access flag mask.
	 *
	 * @return Mask combined with the given access flag mask.
	 */
	public int set(int access) {
		return access | mask;
	}

	/**
	 * @param access
	 * 		Access flag mask.
	 *
	 * @return Mask without the current flag.
	 */
	public int clear(int access) {
		return access & (~mask);
	}

	/**
	 * @return Applicable targets for the current flag.
	 */
	public Set<Type> getTypes() {
		return types;
	}

	/**
	 * @return Flag identifier.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getCodeFriendlyName();
	}

	/**
	 * @param flags
	 * 		Collection of flags.
	 *
	 * @return String representation of flags.
	 */
	public static String toString(Iterable<AccessFlag> flags) {
		// Don't include ACC_SUPER, is meaningless
		return StreamSupport.stream(flags.spliterator(), false)
				.filter(Objects::nonNull)
				.filter(v -> v != ACC_SUPER)
				.map(AccessFlag::toString)
				.collect(Collectors.joining(" "));
	}

	/**
	 * @param type
	 * 		Flag type.
	 * @param flags
	 * 		Collection of flags.
	 *
	 * @return String representation of flags in sorted order.
	 */
	public static String sortAndToString(Type type, Collection<AccessFlag> flags) {
		List<AccessFlag> list;
		try {
			list = sort(type, flags);
		} catch (UnsupportedOperationException ex) { // Collection is unmodifiable
			list = sort(type, new ArrayList<>(flags));
		}
		return toString(list);
	}

	/**
	 * @param type
	 * 		Flag type.
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return String representation of flags in sorted order.
	 */
	public static String sortAndToString(Type type, int acc) {
		return sortAndToString(type, getApplicableFlags(type, acc));
	}

	/**
	 * @return Flag identifier with surrounding comments if the identifier is not a Java keyword.
	 */
	public String getCodeFriendlyName() {
		return isKeyword ? this.name : "/* " + name + " */"; // comment out non-keyword
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the public flag.
	 */
	public static boolean isPublic(int acc) {
		return ACC_PUBLIC.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the private flag.
	 */
	public static boolean isPrivate(int acc) {
		return ACC_PRIVATE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the protected flag.
	 */
	public static boolean isProtected(int acc) {
		return ACC_PROTECTED.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains neither a private, protected nor public flag.
	 */
	public static boolean isPackage(int acc) {
		return !isPrivate(acc) && !isProtected(acc) && !isPublic(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the static flag.
	 */
	public static boolean isStatic(int acc) {
		return ACC_STATIC.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the final flag.
	 */
	public static boolean isFinal(int acc) {
		return ACC_FINAL.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the synchronized flag.
	 */
	public static boolean isSynchronized(int acc) {
		return ACC_SYNCHRONIZED.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the super flag.
	 */
	public static boolean isSuper(int acc) {
		return ACC_SUPER.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the bridge flag.
	 */
	public static boolean isBridge(int acc) {
		return ACC_BRIDGE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the volatile flag.
	 */
	public static boolean isVolatile(int acc) {
		return ACC_VOLATILE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the varargs flag.
	 */
	public static boolean isVarargs(int acc) {
		return ACC_VARARGS.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the transient flag.
	 */
	public static boolean isTransient(int acc) {
		return ACC_TRANSIENT.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the native flag.
	 */
	public static boolean isNative(int acc) {
		return ACC_NATIVE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the interface flag.
	 */
	public static boolean isInterface(int acc) {
		return ACC_INTERFACE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the abstract flag.
	 */
	public static boolean isAbstract(int acc) {
		return ACC_ABSTRACT.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the strict <i>(Floating point math)</i> flag.
	 */
	public static boolean isStrict(int acc) {
		return ACC_STRICT.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the synthetic flag.
	 */
	public static boolean isSynthetic(int acc) {
		return ACC_SYNTHETIC.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the annotation flag.
	 */
	public static boolean isAnnotation(int acc) {
		return ACC_ANNOTATION.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the enum flag.
	 */
	public static boolean isEnum(int acc) {
		return ACC_ENUM.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the module flag.
	 */
	public static boolean isModule(int acc) {
		return ACC_MODULE.has(acc);
	}

	/**
	 * @param acc
	 * 		Access flag mask.
	 *
	 * @return {@code true} when the mask contains the mandated flag.
	 */
	public static boolean isMandated(int acc) {
		return ACC_MANDATED.has(acc);
	}

	/**
	 * Flag group.
	 */
	public enum Type {
		CLASS("public abstract final strictfp interface annotation-interface enum module"),
		INNER_CLASS("public protected private abstract static final strictfp interface annotation-interface enum module"),
		METHOD("public protected private abstract static final synchronized native strictfp"),
		FIELD("public protected private static final transient volatile"),
		MODULE("open mandated synthetic mandated"),
		PARAM("final");

		private final String order;
		private final List<AccessFlag> orderList = new ArrayList<>();
		/**
		 * Unmodifiable view of `orderList`
		 */
		public final List<AccessFlag> recommendOrder = Collections.unmodifiableList(orderList);
		/**
		 * Comparator to sort flags by their recommended ordering.
		 */
		public final Comparator<AccessFlag> recommendOrderComparator;

		Type(String recommendOrder) {
			this.order = recommendOrder;
			this.recommendOrderComparator = Comparator.comparingInt(this::index);
		}

		private int index(AccessFlag flag) {
			if (recommendOrder.isEmpty()) return 0; // not initialized yet
			int idx = recommendOrder.indexOf(flag);
			return idx == -1 ? Integer.MAX_VALUE : idx;
		}

		private static void populateOrder() {  // lazy load
			for (Type type : values()) {
				List<AccessFlag> orderList = type.orderList;
				orderList.clear();
				orderList.addAll(parseModifierOrder(type.order));
			}
		}

		private static List<AccessFlag> parseModifierOrder(String string) {
			if (string == null) return Collections.emptyList();
			return Arrays.stream(string.split(" "))
					.map(nameToFlagMap::get)
					.map(Objects::requireNonNull)
					.collect(Collectors.toList());
		}
	}
}
