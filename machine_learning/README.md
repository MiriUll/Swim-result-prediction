# Machine learning
We were using PyCharm to run the Python code. As Tensorflow may cause problems, we recommend to use PyCharm as well.
## Dependencies
All dependencies are listed in the _requirements.txt_ file. You can run it with  
pip install -r requirements.txt

## Structure
This repo is structured as follows:  
  * In the '_/models_' folder the final models are stored.  
  * In the '_/visualisation_' folder you can find scripts to plot the data structure as well as to scatter the results. These scripts uses absolute paths that have to be specified at some points.
  * The _data.csv_ file contains all the data samples used for training. This is splitted into train and validation in the respective files. The _data_test.csv_ file contains data only used to evaluate the models  

## Running the code
### Training a model 
To train a model, run the train_tf_model.py file. This load the trainig data, trains a model as defined in training_utils.py and stores the model in the models directory.
### Evaluating a model
To evaluate a trained model, use the evaluate_model.py file. This will run a stored model and test its performance against the test data. The user has to specify the absolute paths to the models in this file.
