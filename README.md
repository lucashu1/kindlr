# kindlr
CS 310 Project 2: Kindlr

Sprint 1:
Edit profile in UI: The user is now able to go to his/her profile and edit his/her email, password, etc. The changes appear in Firebase as well.
Partial Firebase refactoring: 
Book filtering in UI: There is now a text box in the main swiping screen that allows the user to provide a search query, which the backend then uses to filter books to display to the user based on author, genre, etc.
Access notifications screen: Users are now able to view their notifications menu to see matches that they have made with other users.
Email notifications: Upon “matching” with another user, users now receive email notifications describing the match, and next steps.
Book pictures in UI: When posting books, users are now prompted for a book image URL. The book image is then displayed on the main swiping screen for other users to see.

Sprint 2:
Added form checking: Users will be promped to put in valid input if they enter nothing or enter input that is deemed invalid.
Moved from Firebase database to Firestore: no longer need to click refresh in order to see updated information. This allowed us to have more control over our data and not have information constantly be overriden without our control.
Password hashing: Passwords are not visibly stored, which helps make our app more secure and keeps our users' information safe.
For sale transaction: Two users can have a transaction for books that are for sale, and the logic of the transaction can go through compleely.
Better UI: Added a more consistent color for UI, with buttons and search fields for a better and more modern look for users.