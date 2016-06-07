package com.globallogic.futbol.core.strategies;

import android.annotation.TargetApi;
import android.os.Build;

import com.globallogic.futbol.core.exceptions.KeyNotFound;
import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.globallogic.futbol.core.interfaces.analyzers.IStrategySharedPreferenceAnalyzer;
import com.globallogic.futbol.core.interfaces.parsers.IOperationParser;
import com.globallogic.futbol.core.interfaces.repository.FloatRepository;
import com.globallogic.futbol.core.interfaces.repository.IntegerRepository;
import com.globallogic.futbol.core.interfaces.repository.LongRepository;
import com.globallogic.futbol.core.interfaces.repository.StringRepository;
import com.globallogic.futbol.core.interfaces.repository.StringSetRepository;
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
 * @see FloatRepository
 * @see IntegerRepository
 * @see LongRepository
 * @see StringRepository
 * @see StringSetRepository
 * @since 0.3.4
 */
public abstract class SharedPreferenceStrategy<T> extends OperationStrategy<StrategySharedPreferenceResponse> implements IOperationParser<StrategySharedPreferenceResponse> {
    private final FloatRepository mFloatRepository;
    private final IntegerRepository mIntegerRepository;
    private final LongRepository mLongRepository;
    private final StringRepository mStringRepository;
    private final StringSetRepository mStringSetRepository;

    //region Constructors implementation
    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, FloatRepository repository) {
        super(anOperation, anAnalyzer);
        this.mFloatRepository = repository;
        this.mIntegerRepository = null;
        this.mLongRepository = null;
        this.mStringRepository = null;
        this.mStringSetRepository = null;
    }

    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, IntegerRepository repository) {
        super(anOperation, anAnalyzer);
        this.mFloatRepository = null;
        this.mIntegerRepository = repository;
        this.mLongRepository = null;
        this.mStringRepository = null;
        this.mStringSetRepository = null;
    }

    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, LongRepository repository) {
        super(anOperation, anAnalyzer);
        this.mFloatRepository = null;
        this.mIntegerRepository = null;
        this.mLongRepository = repository;
        this.mStringRepository = null;
        this.mStringSetRepository = null;
    }

    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, StringRepository repository) {
        super(anOperation, anAnalyzer);
        this.mFloatRepository = null;
        this.mIntegerRepository = null;
        this.mLongRepository = null;
        this.mStringRepository = repository;
        this.mStringSetRepository = null;
    }

    public SharedPreferenceStrategy(Operation anOperation, IStrategySharedPreferenceAnalyzer anAnalyzer, StringSetRepository repository) {
        super(anOperation, anAnalyzer);
        this.mFloatRepository = null;
        this.mIntegerRepository = null;
        this.mLongRepository = null;
        this.mStringRepository = null;
        this.mStringSetRepository = repository;
    }
    //endregion

    //region OperationStrategy implementation
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void doRequestImpl() {
        KeyNotFound keyNotFound = new KeyNotFound("The key doesn't exists");
        if (mFloatRepository != null) {
            parseResponse(!mFloatRepository.hasKey() ? keyNotFound : null, new StrategySharedPreferenceResponse(mFloatRepository.getFloat()));
        } else if (mIntegerRepository != null) {
            parseResponse(!mIntegerRepository.hasKey() ? keyNotFound : null, new StrategySharedPreferenceResponse(mIntegerRepository.getInteger()));
        } else if (mLongRepository != null) {
            parseResponse(!mLongRepository.hasKey() ? keyNotFound : null, new StrategySharedPreferenceResponse(mLongRepository.getLong()));
        } else if (mStringRepository != null) {
            parseResponse(!mStringRepository.hasKey() ? keyNotFound : null, new StrategySharedPreferenceResponse(mStringRepository.getString()));
        } else if (mStringSetRepository != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                parseResponse(!mStringSetRepository.hasKey() ? keyNotFound : null, new StrategySharedPreferenceResponse(mStringSetRepository.getStringSet()));
            } else {
                parseResponse(new Exception("SDK not supported"), null);
            }
        } else {
            parseResponse(new Exception("An error occurs with the repository of the shared preference"), null);
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
}