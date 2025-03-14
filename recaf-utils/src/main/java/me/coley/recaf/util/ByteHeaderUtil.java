package me.coley.recaf.util;

import java.util.List;

/**
 * Simple utility with a bunch of pre-defined patterns for file headers.
 *
 * @author Matt Coley
 */
public class ByteHeaderUtil {
	private static final int WILD = Integer.MIN_VALUE;
	// Archives
	public static final int[] TAR_LZW = {0x1F, 0x9D};
	public static final int[] TAR_LZH = {0x1F, 0xA0};
	public static final int[] BZ2 = {0x42, 0x5A, 0x68};
	public static final int[] LZ = {0x4C, 0x5A, 0x49, 0x50};
	public static final int[] TAR = {0x75, 0x73, 0x74, 0x61, 0x72};
	public static final int[] GZIP = {0x1F, 0x8B};
	public static final int[] ZIP = {0x50, 0x4B, 0x03, 0x04};
	public static final int[] ZIP_EMPTY = {0x50, 0x4B, 0x05, 0x06};
	public static final int[] ZIP_SPANNED = {0x50, 0x4B, 0x07, 0x08};
	public static final int[] RAR = {0x52, 0x61, 0x72, 0x21, 0x1A, 0x07};
	public static final int[] SEVEN_Z = {0x37, 0x7A, 0xBC, 0xAF, 0x27, 0x1C};
	public static final int[] JMOD = {0x4A, 0x4D};
	public static final int[] MODULES = {0xDA, 0xDA, 0xFE, 0xCA};
	public static final List<int[]> ARCHIVE_HEADERS = List.of(
			TAR_LZW,
			TAR_LZH,
			BZ2,
			LZ,
			TAR,
			GZIP,
			ZIP,
			ZIP_EMPTY,
			ZIP_SPANNED,
			RAR,
			SEVEN_Z,
			JMOD);
	// Programs
	public static final int[] CLASS = {0xCA, 0xFE, 0xBA, 0xBE};
	public static final int[] DEX = {0x64, 0x65, 0x78, 0x0A, 0x30, 0x33, 0x35, 0x00};
	public static final int[] PE = {0x4D, 0x5A};
	public static final int[] ELF = {0x7F, 0x45, 0x4C, 0x46};
	public static final int[] DYLIB_32 = {0xCE, 0xFA, 0xED, 0xFE};
	public static final int[] DYLIB_64 = {0xCF, 0xFA, 0xED, 0xFE};
	public static final List<int[]> PROGRAM_HEADERS = List.of(
			CLASS,
			DEX,
			PE,
			ELF,
			DYLIB_32,
			DYLIB_64);
	// Images
	public static final int[] PNG = {0x89, 0x50, 0x4E, 0x47};
	public static final int[] JPG = {0xFF, 0xD8, 0xFF};
	public static final int[] GIF = {0x47, 0x49, 0x46, 0x38};
	public static final int[] BMP = {0x42, 0x4D};
	public static final List<int[]> IMAGE_HEADERS = List.of(
			PNG,
			JPG,
			GIF,
			BMP);
	// Audio
	public static final int[] OGG = {0x4F, 0x67, 0x67, 0x53};
	public static final int[] WAV = {0x52, 0x49, 0x46, 0x46, WILD, WILD, WILD, WILD, 0x57, 0x41, 0x56, 0x45};
	public static final int[] MP3_ID3 = {0x49, 0x44, 0x33};
	public static final int[] MP3_NO_ID1 = {0xFF, 0xFB};
	public static final int[] MP3_NO_ID2 = {0xFF, 0xF2};
	public static final int[] MP3_NO_ID3 = {0xFF, 0xF3};
	public static final int[] M4A = {0x00, 0x00, 0x00, 0x20, 0x66, 0x74, 0x79, 0x70, 0x4D, 0x34, 0x41};
	public static final int[] M4ADash = {0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70, 0x64, 0x61, 0x73};
	public static final List<int[]> AUDIO_HEADERS = List.of(
			OGG,
			WAV,
			MP3_ID3, MP3_NO_ID1, MP3_NO_ID2, MP3_NO_ID3,
			M4A,
			M4ADash);
	// Video
	// For MP4/QuickTime see: https://www.ftyps.com/
	public static final int[] MP4_FYTP_MMP4 = {WILD, WILD, WILD, WILD, 0x66, 0x74, 0x79, 0x70, 0x6D, 0x6D, 0x70, 0x34};
	public static final int[] MP4_FYTP_MP42 = {WILD, WILD, WILD, WILD, 0x66, 0x74, 0x79, 0x70, 0x6D, 0x70, 0x34, 0x32};
	public static final List<int[]> VIDEO_HEADERS = List.of(
			MP4_FYTP_MMP4,
			MP4_FYTP_MP42
	);
	// Misc
	public static final int[] BINARY_XML = {0x03, 0x00, 0x08, 0x00};
	public static final int[] PDF = {0x25, 0x50, 0x44, 0x46, 0x2D};

	/**
	 * The reason why {@code int[]} is used is for simple unsigned byte values.
	 * This will convert the arrays into the signed {@code byte[]}.
	 *
	 * @param array
	 * 		Input array.
	 *
	 * @return Byte array.
	 */
	public static byte[] convert(int[] array) {
		byte[] copy = new byte[array.length];
		for (int i = 0; i < array.length; i++)
			copy[i] = (byte) array[i];
		return copy;
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param patterns
	 * 		The header patterns to check against.
	 *
	 * @return {@code true} when array prefix matches one of the patterns.
	 */
	public static boolean matchAny(byte[] array, List<int[]> patterns) {
		return matchAny(array, 0, patterns);
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param offset
	 * 		Offset into the data to check at.
	 * @param patterns
	 * 		The header patterns to check against.
	 *
	 * @return {@code true} when array prefix matches one of the patterns.
	 */
	public static boolean matchAny(byte[] array, int offset, List<int[]> patterns) {
		return getMatch(array, offset, patterns) != null;
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param patterns
	 * 		The header patterns to check against.
	 *
	 * @return The pattern in the list matched against the array.
	 * {@code null} if no match was found.
	 */
	public static int[] getMatch(byte[] array, List<int[]> patterns) {
		return getMatch(array, 0, patterns);
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param offset
	 * 		Offset into the data to check at.
	 * @param patterns
	 * 		The header patterns to check against.
	 *
	 * @return The pattern in the list matched against the array.
	 * {@code null} if no match was found.
	 */
	public static int[] getMatch(byte[] array, int offset, List<int[]> patterns) {
		for (int i = 0, j = patterns.size(); i < j; i++) {
			int[] pattern;
			if (match(array, offset, pattern = patterns.get(i))) {
				return pattern;
			}
		}
		return null;
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param pattern
	 * 		The header pattern.
	 *
	 * @return {@code true} when array prefix matches pattern.
	 */
	public static boolean match(byte[] array, int... pattern) {
		return match(array, 0, pattern);
	}

	/**
	 * @param array
	 * 		File data to compare against.
	 * @param offset
	 * 		Offset into the data to check at.
	 * @param pattern
	 * 		The header pattern.
	 *
	 * @return {@code true} when array prefix matches pattern.
	 */
	public static boolean match(byte[] array, int offset, int... pattern) {
		int patternLen;
		if (array == null || array.length < offset + (patternLen = pattern.length))
			return false;
		for (int i = offset; i < patternLen + offset; i++) {
			int patternVal = pattern[i];
			if (patternVal == WILD)
				continue;
			if (array[i] != (byte) patternVal)
				return false;
		}
		return true;
	}
}
