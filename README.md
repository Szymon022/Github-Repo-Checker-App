## What is this app?
This app serves as Github repository checker. User can search for github account and view all public repositories of this account. User can then select repository and check all languages used and how many bytes of code was written in each language.

## What was used to build this app
I used Jetpack Compose for UI, Retrofit and Coroutines for handling github api requests.  

## Ideas for future iterations of this project
The goal of this app would be to create github repo checker with complete information about the repos like list of collaborators, repo description etc. 
Improvement of UI - avatar of the repo owner, better search bar, screen transitions etc.
I would like to learn and then use mvvm in this project.
Implementation of reliable search functionality.

## Simplifications
For sure one of the simplifications was the UI - I have focused on backend side of this app.
Repo lists are not stored anywhere so after going back to repo list screen the data is fetched from api.
