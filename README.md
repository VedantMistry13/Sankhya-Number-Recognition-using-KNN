# Sankhya-Number-Recognition-using-KNN
A simple Android Application to recognize numbers. This uses kNN classification for recognizing the digits.

## Introduction
This is just a proof of concept to check if kNN can be used on an Android Device.

### k-NN
k-NN stands for k - Nearest Neighbors. It is an algorithm that is used for classification task. The algorithm tries to classify an input by comparing it with each example in the dataset and then finding the label with maximum count in the `k` closest points.

More on the algorithm can be found [here](https://towardsdatascience.com/machine-learning-basics-with-the-k-nearest-neighbors-algorithm-6a6e71d01761)

## How to use
1) Build an install the application using Android Studio.
2) Run the application.
3) Draw a number on the scratch pad provided.
4) Voila! It can predict numbers.

## The parameter k
kNN algorithm with a k=10 usually gives a good result. Setting k=3 gave better results on the device.
