package coursera;

public enum VideoVisitResult {
	/**
	 * Continue.
	 */
	CONTINUE,
	/**
	 * Continue without visiting the siblings of this file or directory.
	 */
	SKIP_SIBLINGS,
	/**
	 * Continue without visiting the entries in this week item.
	 */
	SKIP_WEEK,
	/**
	 * Terminate.
	 */
	TERMINATE
}
