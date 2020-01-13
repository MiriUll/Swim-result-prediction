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
y_train100m = y.values[:, 0]
y_train200m = y.values[:, 1]

plt.xlim((25, 55))
plt.ylim((50, 250))
plt.xlabel('50m times [s]')
plt.ylabel('predicted times [s]')

fig, ax = plt.subplots()

def plot_long_distances():
    X_train_50m = X_train[:, 3]
    print('Pearson correlation for 200m: ', np.corrcoef(X_train_50m, y_train200m))
    print('Pearson correlation for 100m: ', np.corrcoef(X_train_50m, y_train100m))

    ax.scatter(X_train_50m, y_train100m, label='100m times')
    ax.scatter(X_train_50m, y_train200m, color='r', label='200m times')
    #for i, _ in enumerate(y_train100m):
        #ax.annotate(str(X_train[i][2]), xy=(X_train_50m[i], y_train100m[i]))
        #ax.annotate(str(X_train[i][2])+'', xy=(X_train_50m[i], y_train200m[i]))


def plot_ages():
    age9, age10, age11, age12, age13, age14 = [], [], [], [], [], []
    concat = np.concatenate((X_train, y_train100m[:, None], y_train200m[:, None]), axis=1)
    for i, sample in enumerate(concat):
        if 9.0 == sample[1]:
            age9.append(sample)
        elif sample[1]==10.0:
            age10.append(sample)
        elif sample[1] == 11.0:
            age11.append(sample)
        elif sample[1] == 12.0:
            age12.append(sample)
        elif sample[1] == 13.0:
            age13.append(sample)
        else:
            age14.append(sample)
    ax.scatter(np.array(age9)[:, 3], np.array(age9)[:, 4], color='r', label='Age 9')
    ax.scatter(np.array(age9)[:, 3], np.array(age9)[:, 5], color='r')
    ax.scatter(np.array(age10)[:, 3], np.array(age10)[:, 4], color='g', label='Age 10')
    ax.scatter(np.array(age10)[:, 3], np.array(age10)[:, 5], color='g')
    ax.scatter(np.array(age11)[:, 3], np.array(age11)[:, 4], color='b', label='Age 11')
    ax.scatter(np.array(age11)[:, 3], np.array(age11)[:, 5], color='b')
    ax.scatter(np.array(age12)[:, 3], np.array(age12)[:, 4], color='y', label='Age 12')
    ax.scatter(np.array(age12)[:, 3], np.array(age12)[:, 5], color='y')
    ax.scatter(np.array(age13)[:, 3], np.array(age13)[:, 4], color='c', label='Age 13')
    ax.scatter(np.array(age13)[:, 3], np.array(age13)[:, 5], color='c')
    ax.scatter(np.array(age14)[:, 3], np.array(age14)[:, 4], color='m', label='Age 14')
    ax.scatter(np.array(age14)[:, 3], np.array(age14)[:, 5], color='m')


def plot_training_ages():
    trage1, trage2, trage3, trage4, trage5, trage6, trage7 = [], [], [], [], [], [], []
    concat = np.concatenate((X_train, y_train100m[:, None], y_train200m[:, None]), axis=1)
    for i, sample in enumerate(concat):
        if sample[2] == 1.0:
            trage1.append(sample)
        elif sample[2] == 2.0:
            trage2.append(sample)
        elif sample[2] == 3.0:
            trage3.append(sample)
        elif sample[2] == 4.0:
            trage4.append(sample)
        elif sample[2] == 5.0:
            trage5.append(sample)
        elif sample[2] == 6.0:
            trage6.append(sample)
        else:
            trage7.append(sample)
    ax.scatter(np.array(trage2)[:, 3], np.array(trage2)[:, 4], color='g', label='Training age 2')
    ax.scatter(np.array(trage2)[:, 3], np.array(trage2)[:, 5], color='g')
    ax.scatter(np.array(trage3)[:, 3], np.array(trage3)[:, 4], color='b', label='Training age 3')
    ax.scatter(np.array(trage3)[:, 3], np.array(trage3)[:, 5], color='b')
    ax.scatter(np.array(trage1)[:, 3], np.array(trage1)[:, 4], color='r', label='Training age 1')
    ax.scatter(np.array(trage1)[:, 3], np.array(trage1)[:, 5], color='r')
    ax.scatter(np.array(trage4)[:, 3], np.array(trage4)[:, 4], color='y', label='Training age 4')
    ax.scatter(np.array(trage4)[:, 3], np.array(trage4)[:, 5], color='y')
    ax.scatter(np.array(trage5)[:, 3], np.array(trage5)[:, 4], color='c', label='Training age 5')
    ax.scatter(np.array(trage5)[:, 3], np.array(trage5)[:, 5], color='c')
    ax.scatter(np.array(trage6)[:, 3], np.array(trage6)[:, 4], color='m', label='Training age 6')
    ax.scatter(np.array(trage6)[:, 3], np.array(trage6)[:, 5], color='m')
    ax.scatter(np.array(trage7)[:, 3], np.array(trage7)[:, 4], color='k', label='Training age 7')
    ax.scatter(np.array(trage7)[:, 3], np.array(trage7)[:, 5], color='k')


plot_training_ages()
ax.legend(loc=2)
plt.savefig('/home/anschutzm/Dokumente/TUM/WS1920/Seminar/anschuetz-miriam/machine_learning/plots/training_data_train_ages.pdf',format='pdf')
plt.show()
