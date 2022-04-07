import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression

import matplotlib.pyplot as plt

def visualize(X, y, model):
    # Create a mesh to plot in
    h = 0.01
    x_min, x_max = X[0].min() - 1, X[0].max() + 1
    y_min, y_max = X[1].min() - 1, X[1].max() + 1
    xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                         np.arange(y_min, y_max, h))

    # Make predictions on this mesh
    Z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)

    # Plot the contour and training examples
    plt.contourf(xx, yy, Z, cmap=plt.cm.Spectral)
    plt.scatter(X[0], X[1], c=y, cmap=plt.cm.Spectral)
    plt.show()

dataset = pd.read_csv('dataset.csv', sep=',', header=None, skiprows=1, encoding='ISO-8859-1')

x = dataset.loc[:, [0, 1]]
y = dataset[2]


# Fit the data to a logistic regression model.
clf = LogisticRegression()
clf.fit(x, y)

# Retrieve the model parameters.
b = clf.intercept_[0]
w1, w2 = clf.coef_.T
# Calculate the intercept and gradient of the decision boundary.

print("w1 ", w1[0])
print("w2 ", w2[0])
print("b ", b)

visualize(x, y, clf)

