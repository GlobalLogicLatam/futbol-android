package com.globallogic.futbol.core.strategies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.globallogic.futbol.core.LocalBroadcastManager;
import com.globallogic.futbol.core.OperationApp;
import com.globallogic.futbol.core.OperationResult;
import com.globallogic.futbol.core.broadcasts.OperationBroadcastReceiverHelper;
import com.globallogic.futbol.core.interfaces.IStrategy;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategyAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategyResponse;

import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A concrete implementation of {@link IStrategy}
 *
 * @author facundo.mengoni
 * @see IStrategy
 * @see StrategyResponse
 * @since 0.1.0
 */
public abstract class OperationStrategy<T extends StrategyResponse> implements IStrategy, IOperationParser<T>, Serializable {
    //region Variables
    final protected Operation mOperation;
    //region Logger
    public Logger mLogger;
    //endregion
    protected Long timeInit;
    protected Long mConnectionDelay = 0l;
    private IStrategyAnalyzer mAnalyzer;

    {
        mLogger = Logger.getLogger(getClass().getSimpleName());
        mLogger.setLevel(Level.OFF);
    }
    //endregion

    //region Constructors implementation
    public OperationStrategy(Operation anOperation, IStrategyAnalyzer anAnalyzer) {
        mOperation = anOperation;
        mAnalyzer = anAnalyzer;
    }
    //endregion

    //region Getters & Setters implementation

    /**
     * Defines a time delay for the operation
     *
     * @see #setConnectionDelay(long)
     * @see OperationStrategy#simulateWaiting()
     */
    public void setConnectionDelay(int duration) {
        this.mConnectionDelay = (long) duration;
    }

    /**
     * Defines a time delay for the operation
     *
     * @see #setConnectionDelay(int)
     * @see OperationStrategy#simulateWaiting()
     */
    public void setConnectionDelay(long duration) {
        this.mConnectionDelay = duration;
    }

    /**
     * @return The analyzer for this strategy.
     * @see IStrategyAnalyzer
     * @see #OperationStrategy(Operation, IStrategyAnalyzer)
     */
    public IStrategyAnalyzer getAnalyzer() {
        return mAnalyzer;
    }
    //endregion

    //region IStrategy implementation

    /**
     * {@inheritDoc}
     */
    public void execute() {
        timeInit = Calendar.getInstance().getTimeInMillis();
        if (mConnectionDelay > 0) {
            simulateWaiting();
        } else {
            doRequestImplInBackground();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sendBroadcastForOk() {
        mLogger.info("Sending broadcast for success");
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.OK.name);
        getAnalyzer().addExtrasForResultOk(intent);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForOk(mOperation);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForOk(mOperation.getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sendBroadcastForError() {
        mLogger.info("Sending broadcast for error");
        Intent intent = new Intent();
        intent.putExtra(OperationResult.EXTRA_OPERATION_RESULT, OperationResult.ERROR.name);
        getAnalyzer().addExtrasForResultError(intent);

        String actionWithId = OperationBroadcastReceiverHelper.getActionForError(mOperation);
        intent.setAction(actionWithId);
        LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);

        String actionWithOutID = OperationBroadcastReceiverHelper.getActionForError(mOperation.getClass());
        if (!actionWithId.equals(actionWithOutID)) {
            intent.setAction(actionWithOutID);
            LocalBroadcastManager.getInstance(OperationApp.getInstance()).sendBroadcast(intent);
        }
    }
    //endregion

    //region Strategy implementation

    /**
     * Simulate a delay in the connection and then execute the request
     *
     * @see #setConnectionDelay(int)
     * @see #setConnectionDelay(long)
     * @see #doRequestImpl()
     */
    private void simulateWaiting() {
        mLogger.info("Simulating waiting");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(mConnectionDelay.intValue());
                } catch (InterruptedException e) {
                    //Nothing to do
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mLogger.info("Finished simulating waiting");
                doRequestImplInBackground();
            }
        }.execute((Void) null);
    }

    protected void doRequestImplInBackground() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                doRequestImpl();
                return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((Void) null));
        } else {
            task.execute();
        }
    }

    /**
     * You must obtain the response an call the parse response
     *
     * @see #parseResponse(Exception, StrategyResponse)
     */
    protected abstract void doRequestImpl();

    /**
     * Send the broadcast to notify that the operation finished
     *
     * @param aResult Boolean that notify the result of the operation
     */
    protected void afterWorkInBackgroundBroadcasts(Boolean aResult) {
        if (aResult) {
            sendBroadcastForOk();
        } else {
            sendBroadcastForError();
        }
    }

    public void afterWorkInBackground(Boolean aResult) {
        mLogger.info("After work in background");
        afterWorkInBackgroundBroadcasts(aResult);
        if (timeInit != null) {
            long timeFinish = Calendar.getInstance().getTimeInMillis();
            long difference = timeFinish - timeInit;
            onStrategyFinish(difference);
        }
    }

    /**
     * Called when the strategy is finished but before the receiver is notified.
     *
     * @param duration The duration of the request from which start until finish
     */
    protected void onStrategyFinish(Long duration) {
        mLogger.info("On operation finish");
        mOperation.onStrategyFinish(this);
    }
    //endregion
}