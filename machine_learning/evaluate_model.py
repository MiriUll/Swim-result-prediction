import numpy as np
import tensorflow as tf
import pandas as pd
from sklearn.metrics import mean_absolute_error
from visualisation.visualise_utils import scatter_predictions, scatter_multiple_predictions


def reuse_model(X, model_path):
    """
    Load a model from file and use it for new prediction
    :param X: data to predict labeld for
    :param model_path: path to model
    :return: predictions
    """
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    y_pred = np.zeros(len(X))
    for i, sample in enumerate(X):
        inp = np.array([sample], dtype=np.float32)
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        y_pred[i] = interpreter.get_tensor(output_details[0]['index'])[0][0]
    return y_pred


def eval_standard_models():
    """
    evaluate 100 mand 200m models using the test data
    """
    y_pred_100m = reuse_model(X,
                              '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model100m.tflite')
    print("Mean Absolute Error for 100m: " + str(mean_absolute_error(y_100m, y_pred_100m)))
    scatter_predictions(y_100m, y_pred_100m,
                        '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/plots/eval_100m.pdf')

    y_pred_200m = reuse_model(X,
                              '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m.tflite')
    print("Mean Absolute Error for 200m: " + str(mean_absolute_error(y_200m, y_pred_200m)))
    scatter_predictions(y_200m, y_pred_200m,
                        '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/plots/eval_200m.pdf')


def eval_200_alternative():
    """
    compare 200m prediction alternatives and scatter their predictions
    """
    y_pred_100m = reuse_model(X,
                              '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model100m.tflite')
    y_pred_200m = reuse_model(X,
                              '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m.tflite')
    X_100_train = np.concatenate((X, y_100m), axis=1)
    X_100_pred = np.concatenate((X, y_pred_100m[:, None]), axis=1)
    y_pred_200m_training = reuse_model(X_100_train,
                                       '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m_using_100m_orig.tflite')
    print("Mean Absolute Error for 200m using true 100m: " + str(mean_absolute_error(y_200m, y_pred_200m_training)))
    y_pred_200m_training_pred100 = reuse_model(X_100_pred,
                                               '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m_using_100m_orig.tflite')
    print("Mean Absolute Error for 200m using true 100m and test predicted: " + str(
        mean_absolute_error(y_200m, y_pred_200m_training_pred100)))

    y_pred_200m_predict = reuse_model(X_100_pred,
                                      '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m_using_100m.tflite')
    print("Mean Absolute Error for 200m using predicted 100m: " + str(mean_absolute_error(y_200m, y_pred_200m_predict)))
    scatter_multiple_predictions(y_200m,
                                 [y_pred_200m, y_pred_200m_training_pred100, y_pred_200m_predict, y_pred_200m_training],
                                 '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/plots/eval_200m_variations.pdf',
                                 ['b', 'r', 'g', 'y'], [',', 'o', '^', '.'],
                                 ['original model', 'train: true, test: predicted', 'train, test: predicted',
                                  'train, test: true'])


# Load the test data from file
test_data = pd.read_csv("data_test.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                                   '50m': np.float64, '100m': np.float64,
                                                                   '200m': np.float64})
X = test_data.iloc[:, 0:4].values
y_100m = test_data.iloc[:, 4:5].values
y_200m = test_data.iloc[:, 5:6].values

eval_200_alternative()
