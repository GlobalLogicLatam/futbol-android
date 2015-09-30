package com.globallogic.futbol.core.operation;

import android.content.Context;

import com.globallogic.futbol.core.exceptions.UnexpectedResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Facundo Mengoni on 5/8/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class OperationHelper {

    /**
     * Read a text file of the "assets" folder to return its contents
     *
     * @param aContext    The context of where you get the resource
     * @param anAssetPath The path where to get the resource
     * @throws IOException
     */
    public static String assetsReader(Context aContext, String anAssetPath) throws IOException {
        StringBuilder buf = new StringBuilder();
        InputStream stream = aContext.getAssets().open(anAssetPath);
        BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String str;
        while ((str = in.readLine()) != null) {
            buf.append(str);
        }
        in.close();
        return buf.toString();
    }

    /**
     * Basic analysis of the different types of most common exceptions.
     *
     * @param exception The exception to analyze
     * @param callback  The callback for the analysis
     */
    public static void analyzeException(Exception exception, ExceptionCallback callback) {
        try {
            throw exception;
        } catch (UnexpectedResponseException e) {
            callback.unexpectedResponseException();
        } catch (JsonSyntaxException e) {
            callback.jsonSyntaxException();
        } catch (SocketException e) {
            callback.socketException();
        } catch (MalformedURLException e) {
            callback.malformedURLException();
        } catch (TimeoutException e) {
            callback.timeOutException();
        } catch (IOException e) {
            callback.ioException();
        } catch (Exception e) {
            callback.otherException();
        }
    }

    /**
     * String converter to {@link <a href="http://google-gson.googlecode.com/svn/tags/1.1.1/docs/javadocs/com/google/gson/JsonObject.html">JsonObject</a>}
     *
     * @param string the string to convert
     * @return the json object
     * @throws JsonSyntaxException - if the specified text is not valid JSON
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static JsonObject getAsJsonObject(String string) throws JsonSyntaxException {
        return new JsonParser().parse(string).getAsJsonObject();
    }

    /**
     * String converter to {@link <a href="http://google-gson.googlecode.com/svn/tags/1.1.1/docs/javadocs/com/google/gson/JsonArray.html">JsonArray</a>}
     *
     * @param string the string to convert
     * @return the json array
     * @throws JsonSyntaxException - if the specified text is not valid JSON
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static JsonArray getAsJsonArray(String string) throws JsonSyntaxException {
        return new JsonParser().parse(string).getAsJsonArray();
    }

    /**
     * String converter to {@link <a href="http://google-gson.googlecode.com/svn/tags/1.1.1/docs/javadocs/com/google/gson/JsonPrimitive.html">JsonPrimitive</a>}
     *
     * @param string the string to convert
     * @return the json primitive
     * @throws JsonSyntaxException - if the specified text is not valid JSON
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static JsonPrimitive getAsJsonPrimitive(String string) throws JsonSyntaxException {
        return new JsonParser().parse(string).getAsJsonPrimitive();
    }

    /**
     * String converter to a object specified
     *
     * @param string the string to convert
     * @param aClass the class of the model
     * @return the model parsed
     * @throws JsonSyntaxException -  if json is not a valid representation for an object of type classOfT
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static <T> T getModelObject(String string, Class<T> aClass) throws JsonSyntaxException {
        return new Gson().fromJson(string, aClass);
    }

    /**
     * String converter to a object specified
     *
     * @param string     the string to convert
     * @param dateFormat the format of the date of the object
     * @param aClass     the class of the model
     * @return the model parsed
     * @throws JsonSyntaxException -  if json is not a valid representation for an object of type classOfT
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static <T> T getModelObject(String string, String dateFormat, Class<T> aClass) throws JsonSyntaxException {
        return new GsonBuilder().setDateFormat(dateFormat).create().fromJson(string, aClass);
    }

    static class ListOfModel<X> implements ParameterizedType {
        private Class<?> wrapped;

        public ListOfModel(Class<X> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }


    /**
     * String converter to a object specified
     *
     * @param string the string to convert
     * @param aClass the class of the model
     * @return the list of model parsed
     * @throws JsonSyntaxException -  if json is not a valid representation for an object of type classOfT
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static <T> ArrayList<T> getModelArray(String string, Class<T> aClass) throws JsonSyntaxException {
        return new Gson().fromJson(string, new ListOfModel<T>(aClass));
    }

    /**
     * String converter to a object specified
     *
     * @param string     the string to convert
     * @param dateFormat the format of the date of the object
     * @param aClass     the class of the model
     * @return the list of model parsed
     * @throws JsonSyntaxException -  if json is not a valid representation for an object of type classOfT
     * @see <a href="https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/JsonSyntaxException.html">JsonSyntaxException</a>
     */
    public static <T> ArrayList<T> getModelArray(String string, String dateFormat, Class<T> aClass) throws JsonSyntaxException {
        return new GsonBuilder().setDateFormat(dateFormat).create().fromJson(string, new ListOfModel<T>(aClass));
    }

    public interface ExceptionCallback {
        void jsonSyntaxException();

        void timeOutException();

        void socketException();

        void malformedURLException();

        void ioException();

        void otherException();

        void unexpectedResponseException();
    }
}