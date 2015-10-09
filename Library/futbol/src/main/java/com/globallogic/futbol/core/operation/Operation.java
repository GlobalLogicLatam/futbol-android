package com.globallogic.futbol.core.operation;

import com.globallogic.futbol.core.interfaces.IOperation;
import com.globallogic.futbol.core.interfaces.IStrategyCallback;

import java.io.Serializable;

/**
 * Created by Agustin Larghi on 07/10/2015.
 * Globallogic
 * agustin.larghi@globallogic.com
 */
public abstract class Operation<T> implements Serializable, IOperation<T>, IStrategyCallback<T> {
}
