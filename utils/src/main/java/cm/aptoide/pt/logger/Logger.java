/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 20/06/2016.
 */

package cm.aptoide.pt.logger;

import cm.aptoide.pt.logger.Logger;
import cm.aptoide.pt.utils.BuildConfig;
import java.io.EOFException;

/**
 * Aptoide default logger.
 */
public class Logger {

  public static final boolean DBG = BuildConfig.DEBUG;

  /**
   * Prints the stacktrace
   *
   * @param e exception to log.
   */
  public static void printException(Throwable e) {
    if (DBG && e != null) {
      e.printStackTrace();
    }
  }

  public static void i(Object object, String msg) {
    i(object.getClass().getSimpleName(), msg);
  }

  public static void i(Class clz, String msg) {
    i(clz.getSimpleName(), msg);
  }

  public static void i(String tag, String msg) {
    if (DBG) {
      Logger.i(tag, msg);
    }
  }

  public static void w(String tag, String msg) {
    if (DBG) {
      Logger.w(tag, msg);
    }
  }

  public static void w(String tag, String msg, Throwable tr) {
    if (DBG) {
      Logger.w(tag, msg, tr);
    }
  }

  public static void d(String tag, String msg) {
    if (DBG) {
      Logger.d(tag, msg);
    }
  }

  public static void e(String tag, String msg) {
    if (DBG) {
      Logger.e(tag, msg);
    }
  }

  public static void e(String tag, Throwable tr) {
    if (DBG) {
      Logger.e(tag, "", tr);
    }
  }

  public static void e(String tag, String msg, Throwable tr) {
    if (DBG) {
      Logger.e(tag, msg, tr);
    }
  }

  public static void v(String tag, String msg) {
    if (DBG) {
      Logger.v(tag, msg);
    }
  }

  public static void v(String tag, String msg, Throwable tr) {
    if (DBG) {
      Logger.v(tag, msg, tr);
    }
  }

  public static void d(String tag, String msg, Throwable tr) {
    if (DBG) {
      Logger.d(tag, msg, tr);
    }
  }
}
