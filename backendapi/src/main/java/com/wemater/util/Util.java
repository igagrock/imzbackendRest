package com.wemater.util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import javax.ws.rs.core.UriInfo;

import com.wemater.exception.EvaluateException;

public final class Util {
	private static final int START_TIME = 1;
	private static final int DELAY = 3600;

	public static boolean IsEmptyOrNull(String string) {
		if (string == null || string.isEmpty())
			return true;
		return false;

	}

	public static <E> boolean IsEmptyOrNull(Collection<E> T) {
		if (T == null || T.isEmpty())
			return true;
		return false;

	}

	public static String convertClobToString(Clob clob) {
		Reader reader;
		StringBuilder sb;
		try {
			reader = clob.getCharacterStream();
			int c = -1;
			sb = new StringBuilder();
			while ((c = reader.read()) != -1) {
				sb.append(((char) c));
			}

		} catch (SQLException e) {
			throw new EvaluateException(e);
		} catch (IOException e) {

			throw new EvaluateException(e);
		}

		return sb.toString();
	}

	public static Clob convertStringToClob(String string) {

		Clob clob = null;
		try {
			clob = new SerialClob(string.toCharArray());

		} catch (SerialException e) {
			throw new EvaluateException(e);
		} catch (SQLException e) {
			throw new EvaluateException(e);
		}

		return clob;

	}

	public static String getUsernameFromURLforComments(int index,
			UriInfo uriInfo) {

		String url = uriInfo.getAbsolutePath().toString();
		String[] tokens = url.split("/");

		return tokens[tokens.length - index];

	}

	public static Long getArticleIdFromURLforComments(int index, UriInfo uriInfo) {

		String url = uriInfo.getAbsolutePath().toString();
		String[] tokens = url.split("/");

		return Long.valueOf(tokens[tokens.length - index]);

	}

	public static void StartExecutorService(Runnable task) {

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleWithFixedDelay(task, START_TIME, DELAY,
				TimeUnit.SECONDS);

	}

	public static String removeSpaces(String string) {
		return string.trim().replaceAll("\\s+", ""); // replace extra inside
														// white spaces for one
														// word

	}

}