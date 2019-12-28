import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
import numpy as np

data = pd.read_csv("../data.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
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

X_train_50m = X_train[:, 3]

fig,ax = plt.subplots()
ax.scatter(X_train_50m, y_train100m)
#ax.scatter(X_train_50m, y_train200m, color='r')
# for i, lab in enumerate(X_train[:, 1]):
    #ax.annotate(X_train_50m[i], (X_train_50m[i], y_train100m[i]))
plt.show()
