package coursera;

import java.util.HashMap;
import java.util.Map;

public class Timer {

	private static Map<String, Long> timeMap = new HashMap<>();
	private static long time = 0;
	
	
	public static final void start(String name) {
		timeMap.put(name, System.currentTimeMillis());
	}
	
	public static final void print(String name) {
		long current = System.currentTimeMillis();
		System.out.println(name + ": " + (current - timeMap.get(name)));
		timeMap.put(name, 0l);
	}
	
	public static final void start() {
		time = System.currentTimeMillis();
	}
	
	public static final void print() {
		long current = System.currentTimeMillis();
		System.out.println("Timer: " + (current - time));
		time = 0;
	}
}
