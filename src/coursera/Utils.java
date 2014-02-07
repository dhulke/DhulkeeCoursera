package coursera;

import java.text.DecimalFormat;

public class Utils {

	public static String normalizeFileName(String fileName) {
		return fileName.replaceAll("[\\\\/:;*?\"<>|%,#$!+{}&\\[\\]\']", "");
	}
	
	private static final long K = 1024;
	private static final long M = K * K;
	private static final long G = M * K;
	private static final long T = G * K;
	
	public static String convertByteToStringRepresentation(final long value){
	    final long[] dividers = new long[] { T, G, M, K, 1 };
	    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
	    if(value < 1)
	        throw new IllegalArgumentException("Invalid file size: " + value);
	    String result = null;
	    for(int i = 0; i < dividers.length; i++){
	        final long divider = dividers[i];
	        if(value >= divider){
	        	double convertedValue = divider > 1 ? (double) value / (double) divider : (double) value;
	        	result = new DecimalFormat("#,##0.#").format(convertedValue) + " " + units[i];
	            break;
	        }
	    }
	    return result;
	}

}
