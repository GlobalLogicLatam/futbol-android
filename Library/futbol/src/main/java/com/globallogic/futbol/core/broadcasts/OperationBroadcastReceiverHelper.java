package com.globallogic.futbol.core.broadcasts;

import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.operations.Operation;

/**
 * A simple class that helps you with the format of the action to register yours filters.
 *
 * @author facundo.mengoni
 * @since 0.1.0
 */
public class OperationBroadcastReceiverHelper {
    public static final String ACTION_FORMAT = "operation_action:%s_%s_%s";

    private OperationBroadcastReceiverHelper() {
    }

    //region Generic action

    /**
     * Helps you to generate a String to filter the broadcasts
     *
     * @param aClazz   The simple name of the class that you want filter. We usually use {@link Class#getSimpleName()}.
     * @param anAction The name of the action that you want listen. Eg: START, OK, ERROR, FINISH, NO_INTERNET.
     * @param anId     An identifier for the operation if you only want listen an specific operation. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see OperationResult
     */
    public static String getAction(String aClazz, String anAction, String anId) {
        return String.format(ACTION_FORMAT, aClazz, anAction, anId);
    }
    //endregion

    //region Actions for start

    /**
     * Helps you to generate a String to filter the broadcasts using an Operation and its id.
     * It set the action {@link OperationResult#START}.
     *
     * @param anOperation The operation that you want listen
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getAction(String, String, String)
     */
    public static String getActionForStart(Operation anOperation) {
        return getAction(anOperation.getClass().getSimpleName(),
                OperationResult.START.name,
                anOperation.getId());
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class without an id.
     * It set the action {@link OperationResult#START}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForStart(Operation)
     * @see #getActionForStart(Class, String)
     * @see #getAction(String, String, String)
     */
    public static String getActionForStart(Class aClass) {
        return getActionForStart(aClass, "");
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class with an id.
     * It set the action {@link OperationResult#START}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForStart(Operation)
     * @see #getActionForStart(Class)
     * @see #getAction(String, String, String)
     */
    public static String getActionForStart(Class aClass, String anId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.START.name,
                anId);
    }
    //endregion

    //region Actions for ok

    /**
     * Helps you to generate a String to filter the broadcasts using an Operation and its id.
     * It set the action {@link OperationResult#OK}.
     *
     * @param anOperation The operation that you want listen
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getAction(String, String, String)
     */
    public static String getActionForOk(Operation anOperation) {
        return getAction(anOperation.getClass().getSimpleName(),
                OperationResult.OK.name,
                anOperation.getId());
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class without an id.
     * It set the action {@link OperationResult#OK}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForOk(Operation)
     * @see #getActionForOk(Class, String)
     * @see #getAction(String, String, String)
     */
    public static String getActionForOk(Class aClass) {
        return getActionForOk(aClass, "");
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class with an id.
     * It set the action {@link OperationResult#OK}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForOk(Operation)
     * @see #getActionForOk(Class)
     * @see #getAction(String, String, String)
     */
    public static String getActionForOk(Class aClass, String anId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.OK.name,
                anId);
    }
    //endregion

    //region Actions for error

    /**
     * Helps you to generate a String to filter the broadcasts using an Operation and its id.
     * It set the action {@link OperationResult#ERROR}.
     *
     * @param anOperation The operation that you want listen
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getAction(String, String, String)
     */
    public static String getActionForError(Operation anOperation) {
        return getAction(anOperation.getClass().getSimpleName(),
                OperationResult.ERROR.name,
                anOperation.getId());
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class without an id.
     * It set the action {@link OperationResult#ERROR}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForError(Operation)
     * @see #getActionForError(Class, String)
     * @see #getAction(String, String, String)
     */
    public static String getActionForError(Class aClass) {
        return getActionForError(aClass, "");
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class with an id.
     * It set the action {@link OperationResult#ERROR}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForError(Operation)
     * @see #getActionForError(Class)
     * @see #getAction(String, String, String)
     */
    public static String getActionForError(Class aClass, String anId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.ERROR.name,
                anId);
    }
    //endregion

    //region Actions for finish

    /**
     * Helps you to generate a String to filter the broadcasts using an Operation and its id.
     * It set the action {@link OperationResult#FINISH}.
     *
     * @param anOperation The operation that you want listen
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getAction(String, String, String)
     */
    public static String getActionForFinish(Operation anOperation) {
        return getAction(anOperation.getClass().getSimpleName(),
                OperationResult.FINISH.name,
                anOperation.getId());
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class without an id.
     * It set the action {@link OperationResult#FINISH}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForFinish(Operation)
     * @see #getActionForFinish(Class, String)
     * @see #getAction(String, String, String)
     */
    public static String getActionForFinish(Class aClass) {
        return getActionForFinish(aClass, "");
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class with an id.
     * It set the action {@link OperationResult#FINISH}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForFinish(Operation)
     * @see #getActionForFinish(Class)
     * @see #getAction(String, String, String)
     */
    public static String getActionForFinish(Class aClass, String anId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.FINISH.name,
                anId);
    }
    //endregion

    //region Actions for no internet

    /**
     * Helps you to generate a String to filter the broadcasts using an Operation and its id.
     * It set the action {@link OperationResult#NO_INTERNET}.
     *
     * @param anOperation The operation that you want listen
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getAction(String, String, String)
     */
    public static String getActionForNoInternet(Operation anOperation) {
        return getAction(anOperation.getClass().getSimpleName(),
                OperationResult.NO_INTERNET.name,
                anOperation.getId());
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class without an id.
     * It set the action {@link OperationResult#NO_INTERNET}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForNoInternet(Operation)
     * @see #getActionForNoInternet(Class, String)
     * @see #getAction(String, String, String)
     */
    public static String getActionForNoInternet(Class aClass) {
        return getActionForNoInternet(aClass, "");
    }

    /**
     * Helps you to generate a String to filter the broadcasts using a Class with an id.
     * It set the action {@link OperationResult#NO_INTERNET}.
     *
     * @param aClass The class that you want filter. It call {@link Class#getSimpleName()}.
     * @param anId   An identifier that you want listen. It can be empty but not null.
     * @return The string formatted with the pattern in {@link #ACTION_FORMAT}
     * @see #getActionForNoInternet(Operation)
     * @see #getActionForNoInternet(Class)
     * @see #getAction(String, String, String)
     */
    public static String getActionForNoInternet(Class aClass, String anId) {
        return getAction(aClass.getSimpleName(),
                OperationResult.NO_INTERNET.name,
                anId);
    }
    //endregion
}