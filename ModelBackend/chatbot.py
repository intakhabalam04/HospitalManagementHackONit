# IMPORT LIBRARIES
import json
import os
import signal
import sys

import pandas as pd
from flask import Flask, jsonify, request
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import make_pipeline

# LOAD DATASETS
data = pd.read_csv("Symptom2Disease.csv")
disease_info = pd.read_csv("disease_info.csv")

doctor_data = pd.read_csv("Disease_Doctor_Data.csv")

recovery_steps = pd.read_csv("health_conditions.csv")

# TRAIN MODEL USING NAIVE BAYES ALGORITHM

X = data['text']
Y = data['label']

model = make_pipeline(CountVectorizer(), MultinomialNB())
model.fit(X, Y)


# Function to detect diseases
def get_disease_detection(symptoms):
    predicted_disease = model.predict([symptoms])[0]
    return predicted_disease


# FUNCTION TO GET DOCTOR SUGGESTION
def get_doctor_suggestion(disease):
    matching_doctors = doctor_data.loc[doctor_data['Disease'] == disease]
    if not matching_doctors.empty:
        doctor_info = matching_doctors.sample(1)  # Randomly select one doctor from matching list
        specialist = doctor_info['Specialist'].values[0]
        doctor_name = doctor_info['Doctor_Name'].values[0]
    else:
        specialist = "General Physician"
        doctor_name = "Dr. Ashutosh Shukla"

    disease_info_row = disease_info[disease_info['Disease'] == disease]
    information = disease_info_row['Information'].values[0]

    recovery_steps_row = recovery_steps[recovery_steps['Condition'] == disease]
    recovery_steps_info = recovery_steps_row['Recovery Steps'].values[0]

    return [specialist, doctor_name, information, recovery_steps_info]


def health_insurance_info():
    response = [
        "A health insurance plan is a contract between the insurance provider and policyholder.",
        "It provides financial coverage against hospitalisation expenses, prescription fees, medical bills, etc.",
        "You can get either reimbursement for medical expenses or cashless treatment from a network of hospitals associated with the insurance provider.",
        "Here are some of the main benefits of a health insurance policy:",
        "- Easy cashless claims",
        "- Helps you manage rising medical costs",
        "- Provides critical illness cover",
        "- Tax benefits under section 80D up to Rs. 1 lakh",
        "Health insurance policy covers the following hospitalisation expenses:",
        "- Pre hospitalisation expenses",
        "- Post hospitalisation expenses",
        "- Hospital room rent",
        "- Ambulance cost",
        "- Surgery cost",
        "- Critical illness cover",
        "- Doctor’s consultancy fees",
        "- Medicines",
        "- ICU rent",
        "- Daycare cost"
    ]
    return response


def insurance_plan_types():
    response = [
        "There are 5 major types of health insurance plans available in India:",
        "1. Individual Health Insurance",
        "2. Family Floater Health Insurance",
        "3. Senior Citizens Health Insurance",
        "4. Critical Illness Insurance",
        "5. Group Health Insurance Plan",
        "6. Maternity Health Insurance"
    ]
    return response


def individual_health_insurance_info():
    response = """
    Individual health insurance is a type of insurance plan that provides medical coverage to individuals based on their specific sum insured.
    Unlike group or family health insurance plans, individual health insurance requires separate purchases for each family member.
    This allows for personalized coverage based on individual health needs, rather than covering the entire family.
    Individual health insurance plans typically offer extensive coverage for emergency medical expenses, including hospitalization costs, day care procedures, road ambulance services, alternative treatments, organ donor expenses, and more.
    However, it's important to note that the premium and coverage of an individual health policy cannot be shared among family members.
    """
    return response


def family_floater_health_insurance_info():
    response = """
    If you have a dependent family with parents, spouse, children, and siblings, it is important to buy a health insurance policy for all your family members.
    A family floater health insurance plan is an affordable option to obtain health insurance coverage for the complete family in a single policy.
    In this type of plan, the sum insured will be shared by all the family members.
    In fact, it is an economical insurance plan which means a single floater policy in which the policyholder pays a single premium amount to get coverage for all family members.
    """
    return response


app = Flask(__name__)
questions = [
    "Hi there! What's your name?",
    "Hello, {}! How can I assist you today?\nDo you have any of the following symptoms?\n1. Fever\n2. Common Cold\n3. "
    "Diabetes\n4. Hypertension (High Blood Pressure)\n5. Influenza (Flu)\n6. Depression\nPlease enter the number(s) "
    "of the symptom(s) you are experiencing, separated by commas (e.g., '1, 3')",
    "\nHow many days have you been experiencing these symptoms?",
    "Do you have any other symptoms or concerns you'd like to share? (yes/no)"
]

answers = {'symptoms': [], 'days': ''}
current_question = 0
symptom_map = {
    '1': 'Fever',
    '2': 'Common Cold',
    '3': 'Diabetes',
    '4': 'Hypertension (High Blood Pressure)',
    '5': 'Influenza (Flu)',
    '6': 'Depression'
}

user_symptoms = ""
insurance = ""


@app.route('/chat', methods=['POST'])
def chat():
    global current_question
    global user_symptoms
    global insurance
    print(current_question)
    data = request.get_data()
    user_input = json.loads(data)['input']

    if current_question == 0:
        name = user_input
        current_question += 1
        response = questions[current_question].format(name)
    elif current_question == 1:
        symptom_numbers = user_input
        symptoms = [symptom_map[num] for num in symptom_numbers.split(',') if num in symptom_map]
        symptoms_text = ", ".join(symptoms)
        user_symptoms = symptoms_text
        response = f'You mentioned experiencing the following symptoms: {symptoms_text}'
        current_question += 1
        response += questions[current_question]
    elif current_question == 2:
        days = user_input
        answers['days'] = days
        current_question += 1
        response = questions[current_question]
    elif current_question == 3:
        if user_input.lower() == 'yes':
            response = "Enter another symptom:"
            current_question = 1
        else:
            # print(answers)
            response = "Goodbye!"
            current_question = 0  # Reset the conversation for the next interaction
            # Process the collected answers
            all_symptoms = ", ".join(answers['symptoms'])
            predicted_output = model.predict([all_symptoms])[0]

            if predicted_output in disease_info['Disease'].values:
                specialist, doctor_name, information, recovery_steps_info = get_doctor_suggestion(predicted_output)
                response += f" It seems like you might have {predicted_output}. I recommend you to consult a {specialist}. You can consult {doctor_name}.\n\nHere is some information about your disease:\n {information}. \n\nRecovery Steps:\n {recovery_steps_info}"
            else:
                response += f" It seems like you might have {predicted_output}. For more information about this condition, please consult a healthcare professional."
            response += "<br>\nWould you like to explore health insurance options? (yes/no)"
            current_question = 4

            answers.clear()  # Clear the answers for the next conversation\
            answers['symptoms'] = []
    elif current_question == 4:
        if user_input.lower() == 'yes':
            response = "Do you have any questions or concerns related to health insurance? (yes/no)"
            current_question = 5
        elif user_input.lower() == 'no':
            response = "Okay, let me know if you need further assistance!"
            current_question = 0
        else:
            response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."
    elif current_question == 5:
        if user_input.lower() == 'yes':
            response = "Here are some options:<br>"
            response += "<br>".join(health_insurance_info())
            response += "<br>\nWould you like to know about different types of health insurance plans? (yes/no)"
            current_question = 6
        elif user_input.lower() == 'no':
            response = "Okay, let me know if you need further assistance!"
            current_question = 0
        else:
            response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."
    elif current_question == 6:
        if user_input.lower() == 'yes':
            response = "Here are some options:<br>"
            response += "<br>".join(insurance_plan_types())
            response += "<br>\nWould you like to apply for a health insurance plan now? (yes/no)"
            current_question = 7
        elif user_input.lower() == 'no':
            response = "Okay, let me know if you need further assistance!"
            current_question = 0
        else:
            response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."
    elif current_question == 7:
        if user_input.lower() == 'yes':
            response = "Select the type of health insurance plan you are interested in:"
            response += "<br>".join(insurance_plan_types())
            response += "<br>Enter your choice (1-6):"
            current_question = 8
        elif user_input.lower() == 'no':
            response = "Okay, let me know if you need further assistance!"
            current_question = 0
        else:
            response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."
    elif current_question == 8:
        plan_choice = user_input
        if plan_choice == "1":
            insurance = "Individual Health Insurance"
            link = 'http://localhost:8080/patient/insurance?plan=1'
            response = f'<a href="{link}" target="_blank">Click here for Individual Health Insurance</a>'
        elif plan_choice == "2":
            insurance = "Family Floater Health Insurance"
            link = 'http://localhost:8080/patient/insurance?plan=2'
            response = f'<a href="{link}" target="_blank">Click here for Family Floater Health Insurance</a>'
        elif plan_choice == "3":
            insurance = "Senior Citizens Health Insurance"
            link = 'http://localhost:8080/patient/insurance?plan=3'
            response = f'<a href="{link}" target="_blank">Click here for Senior Citizens Health Insurance</a>'
        elif plan_choice == "4":
            insurance = "Critical Illness Insurance"
            link = 'http://localhost:8080/patient/insurance?plan=4'
            response = f'<a href="{link}" target="_blank">Click here for Critical Illness Insurance</a>'
        elif plan_choice == "5":
            insurance = "Group Health Insurance Plan"
            link = 'http://localhost:8080/patient/insurance?plan=5'
            response = f'<a href="{link}" target="_blank">Click here for Group Health Insurance Plan</a>'
        elif plan_choice == "6":
            insurance = "Maternity Health Insurance"
            link = 'http://localhost:8080/patient/insurance?plan=6'
            response = f'<a href="{link}" target="_blank">Click here for Maternity Health Insurance</a>'
        else:
            response = "Invalid choice. Please enter a number between 1 and 6."

        current_question = 9
        response += "<br>\nDo you want to book an appointment with a doctor? (yes/no)"
    elif current_question == 9:
        if user_input.lower() == 'yes':
            response = f'<a href="http://localhost:8080/patient/book_appointment" >Click here for Book appointment with the doctor</a>'
        elif user_input.lower() == 'no':
            response = "Okay, let me know if you need further assistance!"
            current_question = 0
        else:
            response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."

    else:
        response = "I'm sorry, I didn't understand that. Please respond with 'yes' or 'no'."

    response = response.replace('\n', '<br>')
    return jsonify({
        "response": response,
        "questionNo": current_question,
        "user_symptom": user_symptoms,
        "insurance": insurance
    })


@app.route('/restart', methods=['POST'])
def restart_server():

    os.execv(sys.executable, ['python'] + sys.argv)

    # Send response first before restarting
    response = jsonify({"message": "Server is restarting..."})
    response.status_code = 200

    # Use signal to safely restart the server after the response is sent
    os.kill(os.getpid(), signal.SIGINT)

    return response


if __name__ == '__main__':
    app.run(debug=True)
