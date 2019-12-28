import tensorflow as tf
import tensorflow_core
from tensorflow_core.python.keras.layers import Dense, Dropout
from tensorflow_core.python.keras.optimizers import RMSprop
from tensorflow_core.python.keras.callbacks import EarlyStopping

storepath = 'models/'

def build_model(X_train):
    model = tensorflow_core.python.keras.Sequential([
        Dense(64, activation='relu', input_shape=[len(X_train[0])]),
        Dense(64, activation='relu'),
        Dense(1)
    ])

    optimizer = tensorflow_core.python.keras.optimizers.RMSprop(0.001)

    model.compile(loss='mse',
                  optimizer='rmsprop',
                  metrics=['mae', 'mse'])
    return model


def train_eval_model(X_train, y_train, X_test, y_test, epochs=500):
    model = build_model(X_train)

    early_stop = EarlyStopping(monitor='val_loss', patience=10)
    history = model.fit(X_train, y_train,
                        epochs=epochs, validation_split=0.2, verbose=0,
                        callbacks=[early_stop])

    eval = model.evaluate(X_test, y_test, verbose=2)
    _, mae, _ = eval

    print("Testing set Mean Abs Error: {:5.2f} MPG".format(mae))

    return model, history, eval


def store_model(model, filename):
    model.save(storepath + filename)
    # convert_to_tflite(storepath + filename)
    #converter = tf.lite.TFLiteConverter.from_keras_model(model)
    #tflite_model = converter.convert()
    #open(storepath + filename + '_lite', 'wb').write(tflite_model)

