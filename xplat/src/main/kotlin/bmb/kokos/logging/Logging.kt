package bmb.kokos.logging

import org.apache.logging.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val stackWalker: StackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)

/**
 * Gets a logger for the calling class.
 *
 * If called from a companion object, returns a logger
 * for the enclosing class.
 *
 * If called from a top-level property initializer,
 * returns a logger for the enclosing file.
 */
fun logger(): Logger {
    var caller = stackWalker.callerClass
    val enclosing = caller.enclosingClass
    if (enclosing != null && caller.kotlin.isCompanion) caller = enclosing
    return LoggerFactory.getLogger(caller)
}

/**
 * Gets a logger for the calling class.
 *
 * If called from a companion object, returns a logger
 * for the enclosing class.
 *
 * If called from a top-level property initializer,
 * returns a logger for the enclosing file.
 */
fun logging(): org.apache.logging.log4j.Logger {
    var caller = stackWalker.callerClass
    val enclosing = caller.enclosingClass
    if (enclosing != null && caller.kotlin.isCompanion) caller = enclosing
    return LogManager.getLogger(caller)
}
