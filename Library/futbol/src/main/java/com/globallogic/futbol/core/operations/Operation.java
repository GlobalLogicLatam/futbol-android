package com.globallogic.futbol.core.operations;

import android.content.Intent;
import android.os.Bundle;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiverHelper;
import com.globallogic.futbol.core.interfaces.IOperation;
import com.globallogic.futbol.core.strategies.OperationStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A concrete implementation of {@link IOperation}
 *
 * @author facundo.mengoni
 * @see IOperation
 * @since 0.1.0
 */
public abstract class Operation implements IOperation, Serializable {
    //region Constants
    private static final String SAVE_INSTANCE_STRATEGIES_IN_EXECUTION = "SAVE_INSTANCE_STRATEGIES_IN_EXECUTION";
    //endregion

    //region Variables
    public static boolean sAllMultiProcess = false;
    public Long mConnectionDelay = 0L;
    //region Logger
    public transient Logger mLogger;
    protected boolean mMultiProcess = false;
    private String id;
    //endregion
    private Long mStrategiesInExecution = 0L;

    {
        mLogger = Logger.getLogger(getClass().getSimpleName());
        mLogger.setLevel(Level.OFF);
    }
    //endregion

    //region Constructors implementation

    /**
     * Create a new instance with an empty id.
     * <p>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     *
     * @see #Operation(String)
     * @see #getId()
     * @see #setId(String)
     */
    protected Operation() {
        this("");
    }

    /**
     * Create a new instance with the specified id.
     * <p>
     * The id is used to register the receiver for a specific operation.
     * If you register two operation with different ids then the receiver
     * of one operation never listen the other operation.
     *
     * @see #Operation()
     * @see #getId()
     * @see #setId(String)
     */
    protected Operation(String id) {
        this.id = id;
    }
    //endregion

    //region Getters & Setters implementation

    /**
     * @return The id of the operation
     * @see Operation(String)
     */
    public String getId() {
        return id;
    }

    /**
     * Defines an id for the operation
     *
     * @see Operation(String)
     */
    public void setId(String id) {
        if (id == null)
            id = "";
        this.id = id;
    }

    /**
     * Defines a time delay for the operation.
     *
     * @see #setConnectionDelay(long)
     * @see OperationStrategy#simulateWaiting()
     */
    protected void setConnectionDelay(int duration) {
        this.mConnectionDelay = (long) duration;
    }

    /**
     * Defines a time delay for the operation
     *
     * @see #setConnectionDelay(int)
     * @see OperationStrategy#simulateWaiting()
     */
    protected void setConnectionDelay(long duration) {
        this.mConnectionDelay = duration;
    }
    //endregion

    //region IOperation implementation

    /**
     * {@inheritDoc}<br>
     * The {@code arg} received will be passed to {@code getStrategies} to use there.
     *
     * @see #doRequest(Object...)
     * @see #getStrategies(Object...)
     */
    @Override
    public boolean performOperation(Object... arg) {
        mLogger.info("Performing operation");
        return doRequest(arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBroadcastForStart() {
        mLogger.info("Sending broadcast for start");
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.START.name);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForStart(this);
        intent.setAction(actionWithId);
        sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForStart(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            sendBroadcast(intent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBroadcastForFinish() {
        mLogger.info("Sending broadcast for finished");
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.FINISH.name);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForFinish(this);
        intent.setAction(actionWithId);
        sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForFinish(getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            sendBroadcast(intent);
        }
    }
    //endregion

    //region Operation implementation

    /**
     * Notify that the operation starts and execute all the strategies defined.
     *
     * @see #getStrategies(Object...)
     * @see #sendBroadcastForStart()
     * @see OperationStrategy
     */
    private boolean doRequest(Object... arg) {
        mLogger.info("Doing request");
        ArrayList<OperationStrategy> strategies = getStrategies(arg);
        mStrategiesInExecution += strategies.size();
        Boolean someRequestExecuted = strategies.size() > 0;
        if (mStrategiesInExecution == strategies.size()) {
            sendBroadcastForStart();
        }
        if (someRequestExecuted) {
            for (OperationStrategy operationStrategy : strategies) {
                operationStrategy.setConnectionDelay(mConnectionDelay + operationStrategy.getConnectionDelay());
                operationStrategy.execute();
            }
        } else {
            sendBroadcastForFinish();
        }
        return someRequestExecuted;
    }

    /**
     * Returns the list of strategies to do in this operation.
     *
     * @param arg The arguments specified in performOperation()
     * @see #performOperation(Object...)
     */
    protected abstract ArrayList<OperationStrategy> getStrategies(Object... arg);

    public void onStrategyFinish(OperationStrategy anOperationStrategy) {
        mStrategiesInExecution -= 1;
        if (mStrategiesInExecution == 0)
            sendBroadcastForFinish();
    }

    public Boolean isWorking() {
        return mStrategiesInExecution > 0;
    }

    public void sendBroadcast(Intent intent) {
        if (sAllMultiProcess || mMultiProcess) {
            OperationApp.getInstance().sendBroadcast(intent);
        } else {
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }
    //endregion

    //region Object implementation
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operation operation = (Operation) o;

        return !(id != null ? !id.equals(operation.id) : operation.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    //endregion

    //region Lifecycle implementation
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            onRestoreSavedInstance(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(SAVE_INSTANCE_STRATEGIES_IN_EXECUTION, mStrategiesInExecution);
    }

    public void onRestoreSavedInstance(Bundle savedInstanceState) {
        mStrategiesInExecution = savedInstanceState.getLong(SAVE_INSTANCE_STRATEGIES_IN_EXECUTION);
    }
    //endregion
}