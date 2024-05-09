import json

import pandas as pd
from flask import Flask, request, jsonify
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from nltk.stem.porter import PorterStemmer
from fuzzywuzzy import process
import pickle
from sklearn.metrics.pairwise import cosine_similarity

# Load data
medicines = pd.read_csv('medicine (1).csv')

# Drop NaN values
medicines.dropna(inplace=True)

# Preprocessing
medicines['Description'] = medicines['Description'].apply(lambda x: x.split())
medicines['Reason'] = medicines['Reason'].apply(lambda x: x.split())
medicines['tags'] = medicines['Description'] + medicines['Reason']
medicines['tags'] = medicines['tags'].apply(lambda x: [i.replace(" ", "") for i in x])
medicines['tags'] = medicines['tags'].apply(lambda x: " ".join(x))

# Stemming
ps = PorterStemmer()
medicines['tags'] = medicines['tags'].apply(lambda x: " ".join([ps.stem(word) for word in x.split()]))

# Vectorization
cv = CountVectorizer(stop_words='english', max_features=5000)
vectors = cv.fit_transform(medicines['tags']).toarray()

# Similarity matrix
similarity = cosine_similarity(vectors)


# Function to recommend similar medicines


def recommend(medicine_name, top_n=5):
    medicine_name = medicine_name.lower()

    # Check if the medicine name is in the list
    if medicine_name not in medicines['Drug_Name'].str.lower().tolist():
        # If not found, suggest similar medicines
        similar_medicines = process.extract(medicine_name, medicines['Drug_Name'], limit=top_n)
        similar_results = []
        for match in similar_medicines:
            similar_results.append({'medicine_name': match[0], 'score': match[1]})
        similar_medicines_str = ", ".join([f"{med['medicine_name']} (score: {med['score']})" for med in similar_results])
        # return f"Medicine not found. Similar medicines to '{medicine_name}': {similar_medicines_str}"
        return f"Medicine not found. Please enter a valid medicine name."

    medicine_index = medicines[medicines['Drug_Name'].str.lower() == medicine_name].index[0]
    distances = similarity[medicine_index]
    similar_medicines = sorted(list(enumerate(distances)), reverse=True, key=lambda x: x[1])[1:top_n + 1]

    similar_results = []
    for index, distance in similar_medicines:
        similar_med_name = medicines.iloc[index]['Drug_Name']
        similar_med_description = medicines.iloc[index]['Description']
        similar_results.append({
            'medicine_name': similar_med_name,
            'description': ' '.join(similar_med_description)
        })
    similar_medicines_str = "\n".join([f"{i+1}. {med['medicine_name']} ({med['description']})" for i, med in enumerate(similar_results)])
    return f"Top {top_n} similar medicines to '{medicine_name}':\n\n{similar_medicines_str}"


app = Flask(__name__)


@app.route('/recommend', methods=['POST'])
def chat():
    data = request.get_data()
    medicine_input = json.loads(data)['medicine']
    print(medicine_input)
    response = recommend(medicine_input)

    print(type(response))
    response = response.replace('\n', '<br>')
    return jsonify({"response": response})


if __name__ == '__main__':
    app.run(debug=True, port=5001)
