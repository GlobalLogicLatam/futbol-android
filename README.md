# Que es?
Futbol es una arquitectura diseñada específicamente para Android basada en BroadcastReceiver que busca reemplazar a MVVM facilitando la comunicación entre la capa de servicio y la vista proveyendo las herramientas necesarias para comunicarse con el servidor REST.

# Como la instalo?
Por el momento no se encuentra publicada en maven, por lo que es necesario agregar el repositorio a gradle para poder utilizarla.
Para esto, agregar el siguiente código en el archivo build.gradle en la raiz del proyecto dentro de la seccion de repositories para "allprojects":

``` groovy
repositories {
	jcenter()
	maven {
		url 'http://172.17.201.125:8081/nexus/content/repositories/thirdparty/'
	}
}
```

Luego podrá compilarse la dependencia agregandola al build.gradle de tu módulo:

``` groovy
dependencies {
    compile 'com.globallogic:futbol:0.1.+'
}
```

Esta librería sólo posee la funcionalidad de mockear request. Cada uno puede implementar su propia estrategia de comunicación con la API, pero nosotros proveemos una basada en [Ion](https://github.com/koush/ion). Para utilizarla debes importar la siguiente librería en lugar de la anterior:

``` groovy
dependencies {
    compile 'com.globallogic.futbol.strategies:ion:0.1.+'
}
```

# Algunos conceptos
* Los modelos de dominio que se usen deben implementar serializable. Al ser enviados en un intent es necesario que sea así para la comunicación.
* Cada operación hace un request específico. Puede ser configurado por parámetros pero representa una url base y su método HTTP.

# Como empiezo

#### 1- Especificar el contexto
En el AndroidManifest.xml es necesario especificar el contexto de la applicación de la siguiente manera:

```xml
<application android:name="com.globallogic.futbol.core.OperationApp" ...
```

> *Nota: En caso de tener tu propio contexto, en el `onCreate` deberás llamar a `com.globallogic.futbol.core.OperationApp.setInstance(this)` para el correcto funcionamiento de la librería.*

#### 2- Definir la operación
Luego debes generar una clase que extienda de Operation. Al hacerlo, te pedirá que implementes algunos métodos. Mirá la documentación de cada uno para entender que debe hacerse en cada uno.

#### 3- Definir interfaz de comportamiento
También debes definir una interfaz que extienda de IOperationReceiver. Aquí podrías poner un onSuccess(...), un onError(...) o lo que necesites
> *Nota: Es recomendable crearla como inner class.*

#### 4- Definir que se va a recibir
Por último debes crear una clase que extienda de OperationBroadcastReceiver cuyo constructor sea la interfaz creada en el paso anterior.
> *Nota: También es recomendable crearla como inner class para tener una mejor visibilidad de los extras a pasar, junto con la interfaz.*

#### 5- La API (Opcional)
Desarrollar una API REST para ser consultada.
> *Nota: Gracias a las MockStrategy este paso es opcional, pudiendo definir las respuestas del servidor sin tenerlo implementado. Ver el ejemplo*

### Documentación

Si al descargar la librería no se te descarga automáticamente los source, debes entrar a alguna clase de la librería (por ejemplo a Operation.class) y seleccionar el source de tu máquina local.

![Alt text](http://s15.postimg.org/xpaj7vde3/Attach_Source_1_edit.jpg)

![Alt text](http://s22.postimg.org/y411nvirl/Attach_Source_2_edit.jpg)


### Ejemplo

```java
import android.content.Context;
import android.content.Intent;

import com.globallogic.futbol.core.interfaces.IOperationReceiver;
import com.globallogic.futbol.core.interfaces.IOperationStrategy;
import com.globallogic.futbol.core.operation.Operation;
import com.globallogic.futbol.core.operation.OperationBroadcastReceiver;
import com.globallogic.futbol.core.operation.OperationHelper;
import com.globallogic.futbol.core.operation.strategies.StrategyMock;
import com.globallogic.futbol.core.operation.strategies.StrategyMockResponse;

import java.net.HttpURLConnection;

/**
 * Created by Facundo Mengoni.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class DemoOperation extends Operation {
    private int mError = R.string.empty_error;
    private DemoModel mModel;

    /**
     * Returns to the original state of the operation
     */
    @Override
    public void reset() {
        super.reset();
        mError = R.string.empty_error;
        mModel = null;
    }

    /**
     * Returns a strategy for server connection
     *
     * @param arg The arguments specified in performOperation()
     */
    @Override
    protected IOperationStrategy getStrategy(Object... args) {
        String id = (String) args[0];

        StrategyMock strategy = new StrategyMock(0f);
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Facundo\"}"));
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Ezequiel\"}"));
        strategy.add(new StrategyMockResponse(200, "{\"id\":" + id + ",\"name\":\"Fernando\"}"));
        return strategy;
    }

    /**
     * This allows you to analyze the http code and save the objects according to code.
     * Is triggered only if you can connect to the server.
     */
    @Override
    public void analyzeResult(int httpCode, String result) {
        switch (httpCode) {
            case HttpURLConnection.HTTP_OK:
                this.mModel = OperationHelper.getModelObject(result, DemoModel.class);
                break;
        }
    }

    /**
     * This allows you to analyze the exception occurred.
     * Is triggered only if an error has occurred.
     */
    @Override
    public void analyzeException(Exception e) {
        OperationHelper.analyzeException(e, new OperationHelper.ExceptionCallback() {
            @Override
            public void jsonSyntaxException() {
                mError = R.string.json_syntax_exception;
            }

            @Override
            public void timeOutException() {
                mError = R.string.time_out_exception;
            }

            @Override
            public void socketException() {
                mError = R.string.socket_exception;
            }

            @Override
            public void malformedURLException() {
                mError = R.string.malformed_url_exception;
            }

            @Override
            public void ioException() {
                mError = R.string.io_exception;
            }

            @Override
            public void otherException() {
                mError = R.string.other_exception;
            }
        });
    }

    /**
     * It allows you to add extras to the intent that will be received by the receiver.
     * Is triggered only if you can connect to the server.
     */
    @Override
    protected void addExtrasForResultOk(Intent intent) {
        if (mModel != null)
            intent.putExtra(DemoReceiver.DEMO_MODEL, mModel);
    }

    /**
     * It allows you to add extras to the intent that will be received by the receiver.
     * Is triggered only if an error has occurred.
     */
    @Override
    protected void addExtrasForResultError(Intent intent) {
        // Nothing in this case
    }

    /**
     * You must return a string for display in the UI considering the i18n of texts.
     */
    @Override
    public String getError(Context context) {
        return context.getString(mError);
    }

    public interface IDemoOperation extends IOperationReceiver {
        void onSuccess(DemoModel model);

        void onError();

        void onNotFound();
    }

    public static class DemoReceiver extends OperationBroadcastReceiver {
        public static final String DEMO_MODEL = "DEMO_MODEL";

        private final IDemoOperation mCallback;

        public DemoReceiver(IDemoOperation mCallback) {
            super(mCallback);
            this.mCallback = mCallback;
        }

        @Override
        protected void onResultOK(Intent intent) {
            DemoModel demoModel = (DemoModel) intent.getSerializableExtra(DEMO_MODEL);
            if (demoModel != null)
                mCallback.onSuccess(demoModel);
            else
                mCallback.onNotFound();
        }

        @Override
        protected void onResultError(Intent intent) {
            // ...
            mCallback.onError();
        }
    }
}
```

En el activity/fragment debes tener una instancia del operation y otra del receiver (recomendamos que sean final) para luego llamar a los métodos debidos.

```java
public class MainActivityFragment extends Fragment implements DemoOperation.IDemoOperation {

    private final DemoOperation mOperation;
    private final DemoOperation.DemoReceiver mOperationReceiver;

    public MainActivityFragment() {
        mOperation = new DemoOperation();
        mOperationReceiver = new DemoOperation.DemoReceiver(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOperation.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mOperationReceiver.register(mOperation); // <-- Don't forgot register the receiver
        doOperation();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void doOperation() {
        String id = "1";
        mOperation.performOperation(id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mOperation.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mOperationReceiver.unRegister(); // <-- Don't forgot unregister the receiver
    }

    @Override
    public void onStartOperation() {
        // ToDo Do something in the UI
    }

    @Override
    public void onSuccess(DemoModel model) {
        // ToDo Do something in the UI
    }

    @Override
    public void onError() {
        // ToDo Do something in the UI
    }

    @Override
    public void onNotFound() {
        // ToDo Do something in the UI
    }
}
```

# Diagramas


# Feedback
Para mejorar la librería necesitaríamos saber que piensan, corregir bugs y recibir propuestas de mejoras. Por eso les pedimos que abran los [issues](https://gitlab-art.globallogic.com.ar/androidarea_arg/futbol/issues) que sean necesarios **definiendo** el label correspondiente.