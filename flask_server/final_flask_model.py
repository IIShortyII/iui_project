# -*- coding: utf-8 -*-
"""final_flask_model.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1RnEjHCobO2QUdgiYxq71XrVpDGL6C6vC

###**Summary**

* Import important libraries 
* Connect to Google Drive
* Load model and save model as pickle file in temporary folder
* Start flask server 
* Retrieve image from flask server and save it in temporary folder
* Retrieve image from Google Drive and 
  * Preprocess image
  * Predict image 
* Send back prediction to flask server

###**Import libraries**
"""

!pip install flask_ngrok

# Commented out IPython magic to ensure Python compatibility.
# for building and training the model 
import numpy as np
import tensorflow as tf 
from tensorflow import keras
import keras
from tensorflow.python.keras import layers
from tensorflow.keras import models
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Activation, Dense, Flatten, BatchNormalization, Conv2D, MaxPool2D
from tensorflow.keras.optimizers import Adam, SGD, RMSprop
from tensorflow.python.keras.metrics import categorical_crossentropy
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from keras.utils.vis_utils import plot_model
from keras.callbacks import ModelCheckpoint, EarlyStopping, ReduceLROnPlateau
from keras.utils.generic_utils import NoopLoadingScope
from keras.models import load_model
from keras.preprocessing import image
from tensorflow.keras.preprocessing import image
from tensorflow.keras.utils import img_to_array
from tensorflow.keras.models import Model
from tensorflow.keras.applications import imagenet_utils
import tensorflow_datasets as tfds
import cv2
from sklearn.metrics import confusion_matrix
import itertools
import os
#os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import shutil
import random
import glob
import matplotlib.pyplot as plt
import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)
# %matplotlib inline

# for importing saved model 
from tensorflow.keras import models

# for displaying pictures from storage/Google Drive
from IPython.display import Image, display

# for flask server
import flask
from flask import Flask, render_template, request, Response, jsonify
import pickle
import werkzeug
from flask_ngrok import run_with_ngrok 
import warnings
warnings.filterwarnings('ignore')

# for Google Drive connection
from google.colab import drive

# to find out IP address 
import socket

# to transform predictions from nested numpy array to float with two decimals 
import math

# to jsonify for sending the transformed numpy.float32 
# that we converted to a regular Python float before 
# in order to send the drunkenness score to the server 
# and have the Android app receive the drunkenness score as json in order to be interpretable 
import json

"""###**Connect to Google Drive**"""

# Connect to Google Drive
# More information see https://towardsdatascience.com/different-ways-to-connect-google-drive-to-a-google-colab-notebook-pt-1-de03433d2f7a
# /content is the root folder of Google Colab and has to be appended to all paths used in the notebook
drive.mount('/content/drive')

"""###**Load model and save model as pickle file in temporary folder**"""

# Load the model
# model = models.load_model('/content/drive/My Drive/IUI/Drunkometer/Drunkmodel.h5')
drunkmodel = models.load_model('/content/drive/My Drive/IUI/Drunkometer/PretrainedDrunkmodel_tf')

# save model as pickle file IN THE GOOGLE DRIVE TEMPORARY FOLDER!
pickle.dump(drunkmodel, open('drunkmodel.pkl', 'wb'))

"""###**Start flask server**"""

# https://www.geeksforgeeks.org/how-to-run-flask-app-on-google-colab/amp/ 
# Google Colab provides a VM(virtual machine) so we cannot access the localhost
# (all it does it route it to our local machine’s localhost) 
# as we do on our local machine when running a local web server. 
# What we can do is expose it to a public URL using the Python library flask-ngrok.

# https://medium.datadriveninvestor.com/machine-learning-model-deployment-using-flask-in-google-colab-1f718693a3c0
# We first run the flask app and start ngrok when the app is running then load the model using pickle. 
# After that, we will create a GET method and return the root page using the render_template. 
# We will define the home function for getting the values from the page so that we can then predict.

app = Flask(__name__) 
run_with_ngrok(app)
model = pickle.load(open('drunkmodel.pkl', 'rb'))

#Run and then insert http://0ae5-34-145-159-223.ngrok.io in Android app     
# RETRIEVE IMAGE FROM GOOGLE DRIVE, PREPROCESS AND PREDICT 
def prepare_image(file):
  img_path = '/content/'
  # OR IF PIC STORED IN GOOGLE DRIVE img_path = '/content/drive/My Drive/IUI/Drunkometer/'
  img = image.load_img(img_path + file, target_size=(224, 224))
  img_array = image.img_to_array(img)
  img_array_expanded_dims = np.expand_dims(img_array, axis=0)
  return tf.keras.applications.vgg16.preprocess_input(img_array_expanded_dims)

def predict():
  preprocessed_image = prepare_image('drunkometer_selfie.jpeg')
  predictions = drunkmodel.predict(preprocessed_image)
  #extract drunk score from nested numpy array 
  drunkprediction = predictions[0,0]
  floatdrunkprediction = float(drunkprediction)
  ##roundedfloatdrunkprediction = round(floatdrunkprediction, 4)
  ##print(roundedfloatdrunkprediction)
  jsonfloatdrunkprediction = json.dumps(floatdrunkprediction)
  print(jsonfloatdrunkprediction)
  return jsonfloatdrunkprediction
  ##numpy.float32 is returned
  ##return jsonify(drunkprediction)

# RETRIEVE IMAGE FROM FLASK SERVER 
@app.route('/', methods=['POST'])
def handle_request():
  imagefile = flask.request.files['image']
  filename = werkzeug.utils.secure_filename(imagefile.filename)
  print("\nReceived image File name: " + imagefile.filename)
  imagefile.save(filename)
  prediction = predict()
  headers = {'Content-Type': 'application/json'}
  return Response(prediction, headers=headers)
  #return jsonify(prediction)
  ##headers = {'Content-Type': 'text/plain'}
  ##return Response(prediction, headers=headers)
  ##return Response(prediction, mimetype='text/plain')

if __name__ == '__main__':
    app.run()