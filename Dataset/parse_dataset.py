import pandas as pd
import os

fall_path = "Data/Fall"
adl_path = "Data/ADL"
filelist = []
count = 0
parsed_dataset = pd.DataFrame(columns=['acc', 'gyr', 'fall'])

def parse_files(fall):
    global count
    global filelist
    global parsed_dataset

    for name in filelist:
        if(name.endswith(".txt")):
            
            dataset = pd.read_csv(name, sep=';', header=None, skiprows=1, encoding='ISO-8859-1')

            dataset[14] = pow((pow(dataset[4], 2) + pow(dataset[5], 2) + pow(dataset[6], 2)), 0.5) * 0.017453

            max_acceleration = dataset[10].max()

            max_gyroscope = dataset[14].max()

            parsed_dataset.loc[count] = [max_acceleration, max_gyroscope, fall]
            count += 1


for root, dirs, files in os.walk(fall_path):
	for file in files:
        #append the file name to the list
		filelist.append(os.path.join(root,file))

parse_files(True)

filelist = []
for root, dirs, files in os.walk(adl_path):
	for file in files:
        #append the file name to the list
		filelist.append(os.path.join(root,file))

parse_files(False)

parsed_dataset.to_csv('dataset.csv', index=False)

