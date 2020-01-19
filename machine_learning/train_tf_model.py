import pandas as pd
import numpy as np
from training_utils import train_eval_model, store_model

### 1. Load data from data.csv file
data_train = pd.read_csv("data_train.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                                     '50m': np.float64, '100m': np.float64,
                                                                     '200m': np.float64})
X_train = data_train.iloc[:, 0:4]
y_train = data_train.iloc[:, 4:6]

data_val = pd.read_csv("data_val.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                                 '50m': np.float64, '100m': np.float64,
                                                                 '200m': np.float64})
X_test = data_val.iloc[:, 0:4]
y_test = data_val.iloc[:, 4:6]
# Convert pandas dataframes into numpy arrays (it is needed for the fitting)
X_train = X_train.values
X_test = X_test.values
y_train100m = y_train.values[:, 0]
y_train200m = y_train.values[:, 1]
y_test100m = y_test.values[:, 0]
y_test200m = y_test.values[:, 1]

### 2. Define model, train and evaluate
print("Train 100m times")
model100m, _, _ = train_eval_model(X_train, y_train100m, X_test, y_test100m, units=32)

print("Train 200m times")
model200m, _, _ = train_eval_model(X_train, y_train200m, X_test, y_test200m, units=16)

### 3. Store model
store_model(model100m, 'model100m')
store_model(model200m, 'model200m')

### Include this to train 200m model that uses 100m times
print("Train 200m times using 100m training data")
X_train_int = np.c_[X_train, y_train100m]
X_test_int = np.c_[X_test, y_test100m]
model200m_train_100, _, _ = train_eval_model(X_train_int, y_train200m, X_test_int, y_test200m)

print("Train 200m times using 100m prediction data")
pred_100_train = model100m.predict(X_train)
pred_100_test = model100m.predict(X_test)
X_train_int = np.concatenate((X_train, pred_100_train), axis=1)
X_test_int = np.concatenate((X_test, pred_100_test), axis=1)
model200m_itermediate, _, _ = train_eval_model(X_train_int, y_train200m, X_test_int, y_test200m)

store_model(model200m_train_100, 'model200m_using_100m_orig')
store_model(model200m_itermediate, 'model200m_using_100m')
