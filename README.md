# CSE360 Project: Help System for ASU Students 🧑‍🏫

## Product Vision 🚀

The ASU `CSE 360 Help System` aims to provide personalized, relevant support for students with varying levels of programming experience. By allowing students to indicate their proficiency in areas like Java and GitHub, the system can deliver tailored information without compromising privacy. It features a straightforward feedback mechanism for students to communicate their needs to the instructional team, ensuring that resources remain current and effective. Integration with Ed Discussion will help identify key topics and enhance searchability through effective tagging.

For the instructional team, the system facilitates quick updates to course materials and allows for efficient management of help articles. Administrators will have tools for backing up data, managing user accounts, and securely resetting passwords without accessing sensitive information. Designed to support multiple user roles, the system is scalable for future enhancements. Overall, the help system aims to streamline information delivery while respecting student privacy and continuously adapting to user feedback.

## Development Overview 🧑‍💻

During the last three development phases, the team must produce (and update if needed) the requirements, architecture, design, code, test cases, and demonstration to make it clear how the requirements flow gracefully through all the software development stages.  Unless special arrangements are made, <strong>all four phases of the application development must be written with a Graphical User Interface using JavaFX. </strong>

### The First Phase 🥇

The first submission must focus on establishing the foundation for a secure and private identity mechanism for the application.  Your team must demonstrate that the requirements for users and roles stated above can be implemented.  (Be aware that an admin might also be a student.  How can that be supported?)

Each user has account information that includes:

- an email address
- a user name
- a password (a non-string data type)
- a flag indicating that the password is a one-time password that requires the generation of a new password
- a date and time after which a one-time password is no longer valid
- the individual's name (first, middle, last, preferred)
- a list of system-recognized topics; for each one of the following: beginner, intermediate, advanced, expert (with intermediate being the default)

The requirements, architecture, and design at a high level need to be sketched out for the whole help system, even though many of the details are not yet known in detail.  The Professor will hold evening events (that will be recorded and made available to all) where students can elicit information for the application.

Two screencasts must be provided.  The first is a technical screencast that explains the flow from the requirements through to the working application.  The second is a how-to-use screencast aimed at three separate groups: students, admins, and instructional team members.

### The Second Phase (TBD)
### The Third Phase  (TBD)
### The Fourth Phase (TBD)

## Phase 1 Architecture ⚙️

![arhitecture](/public/phase1_architecture.png)

## Phase One Requirements ✅

1. The very first person to log into the application (e.g., the list of users is empty) is assumed to be an Admin.  The system requires the first user to specify a username and password.  An account is created for that username and password, and that user is assigned the role of Admin.  At that point, the user is directed back to the original login.
2. When creating a password, the password must be entered two times, and they must match.
3. Before a user can use the system, when logging in again they are first taken to a "Finish setting up your account" page.  The "Finish setting up your account" page requires the user to specify an email address and their name.  There are four fields associated with a name: first, middle, last, and optionally preferred first name.  (If the user specifies an optional preferred first name, this name will be used when displaying messages to that user from the application.)
4. The system must support multiple roles.  The following are required minimum roles: Admin, Student, Instructor.
5. A user may have more than one role.  If a user has more than one role, after signing in, the user must specify which role is appropriate for this session.  If the user has just one role, the user is taken to a page to the home page for that role.  For Phase 1, the Student and Instructor role home pages have only one option, and that is to log out.
6. An Admin can perform the following:
     1. Invite an individual to join the application.  A one-time code is provided that allows a new user to create an account.  The standard login page allows the user to provide a username to start the login process or a different input field in which they can enter the invitation code.  The Admin must specify which role(s) this invited user is being given when producing the invitation.
     2. Reset a user account.  A one-time password and an expiration date and time is set.  The next time the user tries to log in, they must use the one-time password, and the only action possible is to set up a new password.  Before being given access to set up a new password, the system checks to see if the date and time are proper given the deadline.  Once the new password has been set, the user is directed back to the login page. Logging in with the one-time password resets the flag so it can't be used again.
     3. Delete a user account.  An "Are you sure?" message must be answered with "Yes" to do the delete.
     4. List the user accounts. A list of all the user accounts with the user name, the individual's name, and a set of codes for the roles is displayed.
     5. Add or remove a role from a user.
     6. Log out.
7. All other users can perform the following:
     1. At the login page, fill in the one-time invitation code to be allowed to establish an account.  The only action allowed when establishing an account is to specify a username and password.  An account is created for that username and password with the role(s) associated with the one-time invitation.  At that point, the user is directed back to the original login.
     2. As described above, they must finish setting up their account.  Once the account is set up, they then have access to the home page to which they have been assigned or to the page where they can select which role is appropriate for this session and then the home page for that role.
     3. For Phase 1, after logging in, finishing the account setup, and selecting a role for this session (if they have multiple roles) they are taken to a home page for that role where the only option is to log out.


## Phase One Deliverables 🎁

- A GitHub repository must be established, and a private project in that repository must include all the code required to build and test the application.  All the members of the team, a specified member of the grading team, must be given access to the repository.  If a Grader must ask for access after the deadline, `5%` will be deducted from the project grade.__
- A PDF document must be produced that covers the following items.
- Cover page (with an appropriate title and list of the members of the team): `5%`
- Project overview: `20%`
- Requirements (User stories): `10%`
- Architecture (Context diagram and major architectural components using UML with explanatory text): `10%`
- Design (Initial UML Use Case, Class Responsibility Collaborator, and Class Diagrams with explanatory text): `10%`
- Code (nicely formatted with internal documentation (e.g., HW #4): `25%`
- Basic Testing: `5%`
- Two Screencasts (The technical screencast must show the code and it must be readable): `10%`
- URL to the project in GitHub: `5%`