import tensorflow_core
import numpy as np

# set random seed for reproducible results
np.random.seed(1)
from tensorflow_core.python.keras.layers import Dense, Dropout
from tensorflow_core.python.keras.callbacks import EarlyStopping

storepath = 'models/'


def build_model(X_train, units):
    """
    builds a tensorflow model as defined
    :param X_train: Training data (to define the correct size)
    :param units: number of neuron in layers
    :return: Tensorflow model
    """
    model = tensorflow_core.python.keras.Sequential([
        Dense(units, activation='relu', input_shape=[len(X_train[0])]),
        Dense(1)
    ])

    model.compile(loss='mse',
                  optimizer='rmsprop',
                  metrics=['mae', 'mse'])
    return model


def train_eval_model(X_train, y_train, X_test, y_test, epochs=500, units=32):
    """
    Train and evaluate a predefined model
    :param X_train: trainng data
    :param y_train: training labels
    :param X_test: validation data
    :param y_test: validation labels
    :param epochs: numer of epochs fpr training
    :param units: number of hidden units in the layers of the network
    :return: trained model, training history and evluation of model against validation data
    """
    model = build_model(X_train, units)

    history = model.fit(X_train, y_train,
                        epochs=epochs, validation_split=0.2, verbose=0)
    evaluation = model.evaluate(X_test, y_test, verbose=2)
    # we are only interested in mean absulte error
    _, mae, _ = evaluation

    print("Validation set Mean Abs Error: {:5.2f}".format(mae))

    return model, history, evaluation


def store_model(model, filename):
    """
    Stored a trained model
    :param model: trained model to store
    :param filename: filename to store model as
    """
    model.save(storepath + filename)
    # convert and store tensorflow lite model
    converter = tensorflow_core.lite.TFLiteConverter.from_saved_model(storepath + filename)
    tflite_model = converter.convert()
    open(storepath + filename + '.tflite', 'wb').write(tflite_model)
