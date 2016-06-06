package com.globallogic.futbol.core.strategies;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import com.globallogic.futbol.core.exceptions.KeyNotFound;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationParser;
import com.globallogic.futbol.core.operations.Operation;
import com.globallogic.futbol.core.responses.StrategySharedPreferenceResponse;
import com.globallogic.futbol.core.utils.Utils;

import java.util.Calendar;
import java.util.logging.Level;

/**
 * A specific implementation to helps to obtains data from shared preferences.
 *
 * @author facundo.mengoni
 * @see OperationStrategy
 * @see IOperationParser
 * @see StrategySharedPreferenceResponse
 * @see IStrategySharedPreferenceAnalyzer
 * @since 0.3.3
 */
public abstract class SharePreferenceStrategy<T> extends OperationStrategy<StrategySharedPreferenceResponse> implements IOperationParser<StrategySharedPreferenceResponse> {
    private final SharedPreferences mSharedPreference;
    private final Type mType;
    private final String mKey;

    //region Constructors implementation
    public SharePreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, SharedPreferences aSharedPreference, Type aType, String aKey) {
        super(anOperation, anAnalyzer);
        this.mSharedPreference = aSharedPreference;
        this.mType = aType;
        this.mKey = aKey;
    }
    //endregion

    //region OperationStrategy implementation
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void doRequestImpl() {
        if (mType == null) {
            parseResponse(new Exception("An error occurs with the type of the shared preference"), null);
        } else {
            if (mSharedPreference.contains(mKey)) {
                switch (mType) {
                    case BOOLEAN:
                        parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getBoolean(mKey, false)));
                        break;
                    case FLOAT:
                        parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getFloat(mKey, 0)));
                        break;
                    case INT:
                        parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getInt(mKey, 0)));
                        break;
                    case LONG:
                        parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getLong(mKey, 0)));
                        break;
                    case STRING:
                        parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getString(mKey, null)));
                        break;
                    case STRING_SET:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            parseResponse(null, new StrategySharedPreferenceResponse(mSharedPreference.getStringSet(mKey, null)));
                        } else {
                            parseResponse(new Exception("SDK not supported"), null);
                        }
                        break;
                    default:
                        parseResponse(new Exception("An error occurs with the type of the shared preference"), null);
                        break;
                }
            } else {
                parseResponse(new KeyNotFound("The key doesn't exists"), null);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IStrategySharedPreferenceAnalyzer getAnalyzer() {
        return (IStrategySharedPreferenceAnalyzer) super.getAnalyzer();
    }
    //endregion

    //region HttpOperationStrategy implementation

    /**
     * Send the broadcast to notify that the operation finished
     *
     * @param aResult Boolean that notify the result of the operation
     */
    private void afterWorkInBackgroundBroadcasts(Boolean aResult) {
        if (aResult) {
            sendBroadcastForOk();
        } else {
            sendBroadcastForError();
        }
    }
    //endregion

    //region IOperationHttpParser implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseResponse(Exception anException, StrategySharedPreferenceResponse aStrategyResponse) {
        Utils.thisMethodShouldNotExecuteInTheThreadUI();
        Object object = null;
        if (aStrategyResponse != null) {
            if (aStrategyResponse.isBoolean() != null) {
                object = aStrategyResponse.isBoolean();
            }
            if (aStrategyResponse.getFloat() != null) {
                object = aStrategyResponse.getFloat();
            }
            if (aStrategyResponse.getInt() != null) {
                object = aStrategyResponse.getInt();
            }
            if (aStrategyResponse.getLong() != null) {
                object = aStrategyResponse.getLong();
            }
            if (aStrategyResponse.getString() != null) {
                object = aStrategyResponse.getString();
            }
            if (aStrategyResponse.getStringSet() != null) {
                object = aStrategyResponse.getStringSet();
            }
        }
        if (anException != null)
            mLogger.log(Level.SEVERE, String.format("Parsing response: %s", anException.getMessage()), anException);
        mLogger.info(String.format("Parsing response: %s", object));
        boolean result = workInBackground(anException, object);
        afterWorkInBackground(result);
    }

    public Boolean workInBackground(Exception anException, Object object) {
        mLogger.info("Work in background");
        if (anException != null) {
            getAnalyzer().analyzeException(anException);
            return false;
        } else {
            try {
                if (!getAnalyzer().analyzeResult((T) object))
                    throw new UnexpectedResponseException();
            } catch (Exception e2) {
                mLogger.log(Level.INFO, "Error in analyzeResult: " + e2.getMessage(), e2);
                getAnalyzer().analyzeException(e2);
                return false;
            }
            return true;
        }
    }


    /**
     * {@inheritDoc}
     */
    public void afterWorkInBackground(Boolean aResult) {
        mLogger.info("After work in background");
        afterWorkInBackgroundBroadcasts(aResult);
        if (timeInit != null) {
            long timeFinish = Calendar.getInstance().getTimeInMillis();
            long difference = timeFinish - timeInit;
            onStrategyFinish(difference);
        }
    }
    //endregion

    public static enum Type {
        BOOLEAN, FLOAT, INT, LONG, STRING, STRING_SET
    }
}