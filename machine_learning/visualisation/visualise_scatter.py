import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
import numpy as np

# change path to "../data_test.csv" for test data
data = pd.read_csv("../data.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                            '50m': np.float64, '100m': np.float64, '200m': np.float64})
X = data.iloc[:, 0:4]
y = data.iloc[:, 4:6]

# Convert pandas dataframes into numpy arrays
X_train = X.values
genders = X_train[:, 0]
y_train100m = y.values[:, 0]
y_train200m = y.values[:, 1]
X_train_50m = X_train[:, 3]
print('Pearson correlation for 200m: ', np.corrcoef(X_train_50m, y_train200m))
print('Pearson correlation for 100m: ', np.corrcoef(X_train_50m, y_train100m))

fig, ax = plt.subplots()
plt.xlim((25, 55))
plt.ylim((50, 250))
plt.xlabel('50m times [s]')
plt.ylabel('predicted times [s]')
ax.scatter(X_train_50m, y_train100m, label='100m times')
ax.scatter(X_train_50m, y_train200m, color='r', label='200m times')
ax.legend(loc=2)

plt.savefig('/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/plots/training_data.pdf',
            format='pdf')
plt.show()
