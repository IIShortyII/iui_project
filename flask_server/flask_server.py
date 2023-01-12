# py -m pip install Flask

# It’s very important to make sure that both the Android phone and the development PC 
# are connected to the same network because we’re using local IPv4 addresses. 

# when running on your laptop for the first time, get the "running on http://192" ... address (without the :5000) port,
# and in the android app code, change IPV4ADDRESS in Home.Activity to this IP address!

import flask
import werkzeug

app = flask.Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def handle_request():
    imagefile = flask.request.files['image']
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image File name : " + imagefile.filename)
    imagefile.save(filename)
    return "Image Uploaded Successfully"

app.run(host="0.0.0.0", port=5000, debug=True)