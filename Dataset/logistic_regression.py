import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression

import matplotlib.pyplot as plt

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