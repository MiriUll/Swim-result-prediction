import hypertools as hyp
import numpy as np
import scipy
import pandas as pd
from scipy.linalg import toeplitz
from copy import copy
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

data = pd.read_csv("data.csv", sep=';', header=0, dtype={'Gender': int, 'Age': int, 'Competitionage': int,
                                                         '50m': np.float64, '100m': np.float64, '200m': np.float64})

X = data.iloc[:, 0:4].values
y = data.iloc[:, 5:6].values.reshape((-1,))

# data['col'] = data['col'].astype(float)
vis_data = np.zeros((3, X.shape[0]))
# Age + competition age
vis_data[0] = X[:, 2]
# 50 m
vis_data[1] = X[:, 3]
vis_data[2] = y
#vis_data = vis_data.reshape((X.shape[0],3))
# geo = hyp.plot(vis_data, '.', ndims=3)
#plt.plot(vis_data, 'ro')
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
ax.scatter(vis_data[0], vis_data[1], vis_data[2])

ax.set_xlabel('Age')
ax.set_ylabel('50m')
ax.set_zlabel('200m')

plt.show()