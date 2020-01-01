import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from training_utils import train_eval_model, store_model

### 1. Load data from data.csv file and split
data = pd.read_csv("data.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                         '50m': np.float64, '100m': np.float64, '200m': np.float64})
X = data.iloc[:, 0:4]
y = data.iloc[:, 4:6]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)

# Convert pandas dataframes into numpy arrays (it is needed for the fitting)
X_train = X_train.values
X_test = X_test.values
y_train100m = y_train.values[:, 0]
y_train200m = y_train.values[:, 1]
y_test100m = y_test.values[:, 0]
y_test200m = y_test.values[:, 1]


### 2. Define model, train and evaluate
print("Train 100m times")
model100m,_,_ = train_eval_model(X_train, y_train100m, X_test, y_test100m)

print("Train 200m times")
model200m,_,_ = train_eval_model(X_train, y_train200m, X_test, y_test200m)

print("Train 200m times using 100m training data")
X_train_int = np.c_[X_train, y_train100m]
X_test_int = np.c_[X_test, y_test100m]
model200m_train_itermediate,_,_ = train_eval_model(X_train_int, y_train200m, X_test_int, y_test200m)

print("Train 200m times using 100m prediction data")
X_train_int = np.c_[X_train, model100m.predict(X_train)]
X_test_int = np.c_[X_test, model100m.predict(X_test)]
model200m_itermediate,_,_ = train_eval_model(X_train_int, y_train200m, X_test_int, y_test200m)

# from visualise_utils import scatter_predictions
# test_predictions = model100m.predict(X_test).flatten()
# scatter_prediction(y_test100m, test_predictions)

### 3. Store model
store_model(model100m, 'model100m')
store_model(model200m, 'model200m')
store_model(model200m_itermediate, 'model200m_using_100m')