package com.globallogic.futbol.core.interfaces;

public interface IBackPressable {
    void setAction(IBackPressAction aAction);

    void removeAction();
}