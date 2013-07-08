
package edu.common.dynamicextensions.nutility;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.common.dynamicextensions.util.global.Variables;

/**
 * This class used to delete a week old(configurable) uploaded files which are not associated with any form data.
 * @author kunal
 *
 */
public final class FileCleaner {

	private static final int DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private static FileCleaner cleaner;
	/**
	 * Used identify older file. Unit used is days.
	 */
	private static int keepFileThreshold;

	/**
	 * the period (in minutes) between successive executions
	 */
	private static int period = 60 * 24;

	private FileCleaner() {
		keepFileThreshold = Variables.fileUploadExpiryLimit;
	}

	public static FileCleaner getInstance() {
		if (cleaner == null) {
			cleaner = new FileCleaner();
		}
		return cleaner;
	}

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	public void start() {
		final Runnable cleaner = new Runnable() {

			@Override
			public void run() {
				File dir = new File(Variables.fileUploadDir);

				for (String fileName : dir.list()) {
					File file = new File(Variables.fileUploadDir + File.separator + fileName);
					long diff = (System.currentTimeMillis() - file.lastModified()) / DAY_IN_MILLIS;

					if (diff > keepFileThreshold) {
						file.delete();
					}
				}
			}
		};

		executorService.scheduleAtFixedRate(cleaner, 1, period, TimeUnit.MINUTES);
	}

	public void stop() {
		executorService.shutdown();
	}


}
