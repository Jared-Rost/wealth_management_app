# Iteration 2

* For grader: our app takes quite a bit of time to start up the main activity on emulator, but the app run smoothly on physical device. We are not sure it is because of android emulator or inefficient way to set up SQL database (if this is the case, we really happy to get feedback and advice from you).

* For your convience, we have 2 options to run the app: ones with stub database and ones with SQL database, which can be set by switching boolean value for [DEVELOPING_STATUS](https://code.cs.umanitoba.ca/comp3350-winter2024/threequarterscs-a02-3/-/blob/fixingDB/app/src/main/java/com/spenditure/application/Services.java?ref_type=heads#L24). `false` means the app will use SQL database and use stub database otherwise.

* We did test our app on physical device, it runs smoothly with SQL database. It also runs android studio emulator, however, it is a bit lag. You can switch to stub database by setting boolean variable mentioned above for testing logic and UI to save your time.

* Note: to login to the app. Use username:Me and password:123

## Features:

* User Manager

* Adding images to Transaction

* Timebase report

* SQL database

* Goal tracker

## User Stories:

### Completed:


* Adding User Manager for user to log in,log-out
* Logic for log out, log out and update user information

* UI/logic to let users choose time period to see report
* Detail report for smaller interval of time

* SQL database and integration testing

* Allow user to open camera and take picture of recipe

### Not complete:

* Due to time constrain, we push goal tracker feature to next iteration

## *Architecture*
The architecture for iteration 1 can be found [here](./docs/Architecture_Diagram_Iteration1.pdf).
The updated architecture for iteration 2 can be found [here](./docs/Architecture_Diagram_Iteration2.pdf).

## Next Iteration

### For the Third Iteration we plan to incorporate the following features:

* Illustrative report manager
    - A feature that provides users with visual reporting graphics on their spending habits.

* User authentication
    - User authentication

* Financial accountability reminders
    - A feature that encourages users to stay on track with their financial goals

* Financial goal tracker
    - A feature that allows users to set and manage their financial goals.



