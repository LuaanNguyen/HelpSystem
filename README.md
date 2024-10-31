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

### The Second Phase  🥈
The project's second phase will focus on the data at the heart of the help system.  The user roles of focus for this phase are admins and instructors.  Be aware that there may be changes in the details of this phase based on new information and requests for clarification.

Each help item consists, at a minimum, of:

- a unique header including information such as the level of the article (e.g., beginner, intermediate, advanced, expert), grouping identifiers (so it is easy for the instructional team to update or delete a related set of articles), and other system information that might limit who can read the article for sensitive/restricted information
- a title
- a short description (This is like an abstract for a paper, but shorter.  It is displayed when to enable the user to select which of several help items returned to read first.  This is like the short text provided by a web search engine after a query.)
- a set of keywords or phrases to facilitate the search process for students
- the body of the help article
- a set of links to reference materials and related articles
- other fields may be required to make it possible to find sensitive information (e.g., a separate title and description that is free of all sensitive information) and allow the easy grouping of articles (see below).
We already know that methods for backing up and restoring these articles are needed.  Methods for adding, removing, and updating them must be provided.  Methods for searching and displaying those that fit the user's request must be provided.  The system must enable the grouping of data related to specific topics.  This makes it easy to find, update, or remove articles when preparing for the next semester.  This second phase needs to be integrated with an updated first phase.

Two screencasts must be provided.  The first screencast is technical.  It explains the flow from the requirements through to the working application.  The second is a how-to-use screencast aimed at two separate groups: admins and instructional team members.


### The Third Phase  (TBD)
### The Fourth Phase (TBD)

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

## Phase Two Requirements  ⚙️

- This phase builds on, extends, and may require refinements to your Phase One implementation.  Unless explicitly stated otherwise, all the requirements for Phase One continue into this phase.
- Each article is given a unique long integer identifier when created, so duplicates can be easily detected.
- The admin and the instruction team roles are enhanced by providing commands to back up and restore the help system data to an admin/instructor-named external data file.  When a restore command is issued, an option is provided to remove all the existing help articles or merge the backed-up copies with the current help articles.  When the unique long integer identifiers match, the backed-up copy will not be added.
- The system shall support a mechanism to support multiple groups of help articles.  Grouped articles can be backed up, so only the articles in the group are in the backup.  For example, articles for a CSE360 implementation might have one group for Eclipse articles and another for IntelliJ articles.  This allows instructors or admins to set up the help system for two different help systems based on which IDE is used in the course.  Similarly, there may be a group for H2 database articles and another for SQL Fiddle articles.  It is also possible for a single article to belong to more than one group (e.g., Eclipse and H2).
- Both admins and instructors may create, update, view, and delete help articles.
- Both admins and instructors may list all the help articles and subsets of the help articles in a group or multiple groups.

## Phase Two Deliverables 🎁

A GitHub repository must be established, and a private project in that repository must include all the code required to build and test the application.  All the members of the team, a specified member of the grading team, must be given access to the repository.  If a Grader must ask for access after the deadline, 5% will be deducted from the project grade.
A PDF document must be produced that covers the following items:
- Cover page (with an appropriate title and list of the members of the team): 5%
- Project overview (updated with change bars showing the updates): 15%
- Requirements (User stories with change bars showing any updates): 10%
- Architecture and major architectural components using UML (e.g., Use Case diagram) with explanatory text): 10%
- Design (Class Responsibility Collaborator and Class Diagrams with explanatory text): 10%
- Code (nicely formatted with internal documentation (e.g., HW #4): 25%
- Basic Testing: 5%
- Two Screencasts (The first is a technical screencast that must show the code, and the code must be readable.  The second is aimed at all potential users and shows how the execution of the application and how each requirement is satisfied.): 10%
- URL to the project in GitHub: 5%
- A Credit Page is provided at the end of the PDF.  This sheet lists every member of the team and what they accomplished.  All team members must participate in, support, or contribute to the production of all of the above items, especially the code and test cases.  No entry is longer than six lines.  The entire team must agree to the contents of this credit page.