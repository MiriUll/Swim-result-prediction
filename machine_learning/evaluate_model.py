import numpy as np
import tensorflow as tf
import pandas as pd
from sklearn.metrics import mean_absolute_error
from visualisation.visualise_utils import scatter_predictions


def reuse_model(X , model_path):
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


test_data = pd.read_csv("data_test.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                                   '50m': np.float64, '100m': np.float64,
                                                                   '200m': np.float64})
X = test_data.iloc[:, 0:4].values
y_100m = test_data.iloc[:, 4:5].values
y_200m = test_data.iloc[:, 5:6].values

y_pred_100m = reuse_model(X, '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model100m.tflite')
y_pred_200m = reuse_model(X, '/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/models/model200m.tflite')
print("Mean Absolute Error for 100m: " + str(mean_absolute_error(y_100m, y_pred_100m)))
print("Mean Absolute Error for 200m: " + str(mean_absolute_error(y_200m, y_pred_200m)))
scatter_predictions(y_100m, y_pred_100m)
scatter_predictions(y_200m, y_pred_200m)
