import tensorflow as tf
import tensorflow_core
import numpy as np
# set random seed for reproducible results
np.random.seed(1)
from tensorflow_core.python.keras.layers import Dense, Dropout
from tensorflow_core.python.keras.optimizers import RMSprop
from tensorflow_core.python.keras.callbacks import EarlyStopping

storepath = 'models2/'


def build_model(X_train, units):
    model = tensorflow_core.python.keras.Sequential([
        Dense(units, activation='relu', input_shape=[len(X_train[0])]),
        Dense(1)
    ])

    model.compile(loss='mse',
                  optimizer='rmsprop',
                  metrics=['mae', 'mse'])
    return model


def train_eval_model(X_train, y_train, X_test, y_test, epochs=500, units=32):
    model = build_model(X_train, units)

    early_stop = EarlyStopping(monitor='val_loss', patience=10)
    history = model.fit(X_train, y_train,
                        epochs=epochs, validation_split=0.2, verbose=0)
                        #callbacks=[early_stop])

    eval = model.evaluate(X_test, y_test, verbose=2)
    _, mae, _ = eval

    print("Validation set Mean Abs Error: {:5.2f}".format(mae))

    return model, history, eval


def store_model(model, filename):
    model.save(storepath + filename)
    # convert and store tensorflow lite model
    converter = tensorflow_core.lite.TFLiteConverter.from_saved_model(storepath + filename)
    tflite_model = converter.convert()
    open(storepath + filename + '.tflite', 'wb').write(tflite_model)

