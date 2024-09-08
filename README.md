# Android Chatbot App for Memory Loss Patients

This Android chatbot application is designed to assist memory loss patients by providing them with answers to questions they may have forgotten. When a patient asks for information like their name, house location, or details about friends and family, the app sends the query to the Python backend using AIML (Artificial Intelligence Markup Language), which processes the request and returns relevant information.

## Tech Stack
- **Frontend:** Android App (Java)
- **Backend:** Python with Flask
- **AI/ML:** AIML (Artificial Intelligence Markup Language)

## Key Features
- **Personalized Memory Assistance:** Patients can ask the chatbot questions about their personal details such as their name, house location, family, friends, etc. The app fetches responses based on pre-filled data.
- **Patient Information Management:** Family members or caregivers can pre-fill the patient's profile with essential information like names, addresses, relationships, and other relevant details during account creation.
- **Real-time Interaction:** The chatbot provides real-time responses to patient queries by sending them to the Flask backend, which uses AIML to generate the correct answers.
- **Customizable Profiles:** Each patient’s profile is customizable, allowing caregivers to update the information as needed to keep the chatbot’s responses accurate.

## Data and Application Flow
- **Question Processing:** When a patient asks a question, it is sent to the Python Flask backend, where AIML is used to interpret and generate the appropriate response based on the patient’s stored data.
- **Profile Setup:** During account creation, the patient’s family or caregiver can input various details about the patient’s life, ensuring that the chatbot has all the necessary information to assist the patient effectively.
- **Real-Time Responses:** The app facilitates smooth communication between the patient and the chatbot, ensuring that responses are quick and relevant.

## Functionality Supported
- **Interactive Chatbot:** Patients can ask questions using a simple chat interface. The chatbot retrieves relevant information from the backend and responds accordingly.
- **Profile Management:** Caregivers can input and update key information about the patient to keep the system up to date.
- **Seamless Backend Integration:** The Android frontend interacts with a Python backend via Flask, ensuring efficient query processing using AIML.

