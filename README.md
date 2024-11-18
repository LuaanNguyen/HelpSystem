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


### The Third Phase  🥉

The main enhancement for this phase is the interface for student users and the methods to facilitate searching, displaying, and asking the system operations team for assistance when the student is not able to find what is needed.

The third phase also adds encrypting data that must be kept private.  When determining whether or not an article should appear in the returned list, an access protocol must be used to determine if this user belongs to a group of users who have been granted access or if this user has been given individual access.

The user interface was responsible for security and privacy in the previous two deliverables.  It was written so that a user role can only see data that is appropriate for that user.  The implementation in this phase is enhanced to ensure that even if memory is dumped and analyzed, private and sensitive data will not be visible.  From the beginning, the architecture and the design must be performed knowing that encryption will be added in this third phase. Significant rewriting of code to implement encryption in this phase must be avoided.  The third phase also requires an initial set of JUnit tests. The team must demonstrate their understanding of automated test suites and their ability to create and use them.

Two screencasts must be provided.  The first is an internal technical screencast.  It explains the flow from the requirements through design to the working code.  The second is a how-to-use screencast targeting students, admins, and instructional team members.

### The Fourth Phase (TBD)

## Phase One Requirements ✅
## Phase Two Requirements  ✅
## Phase Three Requirements  ( In Progress )

- [ ] This phase builds on, extends, and may require minor implementation refinements to the first two phases.  All the requirements for Phases One and Two continue into this phase. Any changes in requirements will be explicitly described in this assignment.
- [ x ] No role has the right to remove admin rights if that means no one will have admin rights for the whole help system, a general group, or a special access group.
- The system supports special access groups consisting of: (Needs Front-End Work)
  - [ x ] A list of articles in the group where the body of the article is encrypted and decrypted access is limited.  (E.g., A collection of help articles that contains proprietary, private, or other kinds of sensitive information.
  - [ x ] A list of admins given admin rights to create, read, update, and delete access rights to this group.  (Admins do not automatically have admin rights to special access groups and do not have the right to view the bodies of articles in this group.)
  - [ x ] The first instructor added to a special access group is given the right to view the bodies of articles in the group and admin rights for this group.
  - [ x ] The default rights for new instructors added to this group do not include admin rights for this group.
  - [ x ] A list of instructors who have been given rights to view the decrypted bodies of articles in this group.
  - [ x ] A list of instructors given admin rights for this group.
  - [  x ] A list of students given viewing rights to the decrypted bodies of articles in the group.
- The student role is enhanced with additional commands:
  - [ ] Students may always perform a set of actions, including quitting the application, sending a generic message to the help system, and sending a specific message to the help system.  Generic messages are used to express confusion about how to use the tool.  Specific messages are those where the student cannot find the help information they need.  In this last case, the student specifies what they need and cannot find.  The system adds a list of the search requests the student has made.  This information can be used to identify new help articles that need to be created and added to the system.
  Set the content level of the articles to be returned (e.g., beginner, intermediate, advanced, expert).  The student may also specify an "all" level to see all the articles independent of the level.  The default is "all".
  Specify a group of articles to limit returned articles to just those items in a group (e.g., a group may be help articles for a particular assignment). The student may request a list of the currently active group, specify which group to use, or "all".
  Searching uses words, names, or phrases in the title, author, or abstract.  Searching for words or phrases in the body of the help articles is currently not supported.  In addition, a search can be performed using the unique long identifier.  Search can be limited to a specific group of papers.
  When a search is performed, the system will display the following.
  - [ ] The first line displayed specifies the group that is currently active,
  - [ ] The number of articles that match each level,
  - [ ] A list of articles matching the search criteria in a short form.  The short form includes a sequence number (1 - n), the title, the author(s), and the abstract.
  - After a search, the student can perform the following:
    - [ ] a different search
    - [ ] a request to view an entire article in detail using the sequence number,
    - [ ] one of the actions that are always available.
  - After viewing a specific article, the student can perform the following:
    - [ ] a different search,
    - [ ] a request to view an entire article in detail using the sequence number,
    - [ ] one of the actions that are always available.
  - [ ] Students may not create, edit, or delete help articles.
- The instructor role is enhanced with additional commands.
  - The instructor is provided the same set of search and view commands as a student.
    - [ ] Instructors may specify a group or a special access group to reduce the length of returned items
    - [ ] Instructors may specify content levels to reduce the length of returned items.
    - [ ] Instructors may view the details of a help article from the returned list of items.
  - [ ] Instructors may create, view, edit, and delete help articles.
  - [ ]  Instructors may create, view, edit, and delete general article groups.  (See above about instructors' rights concerning general groups.)
  - [ ] Instructors may create, view, edit, and delete special access article groups.  (See above about instructor's rights concerning special access groups.)
  - [ ] Instructors may back up and restore articles and groups of articles. 
  - [ ] Instructors may add, view, and delete students from the help system and general groups. 
  - [ ] Instructors with special group rights for a group may add, view, and delete students from that group. 
- The admin role is enhanced with additional commands. 
  - [ ] Admins may create, delete, back up, and restore help articles. (They do not have the right to view or edit the body of any help system article.)
  - [ ] Admins may be given admin rights over general article groups.  (See above about admin rights concerning general article groups.)
  - [ ] Admins may be given admin rights over special access article groups.  (See above about admin rights concerning special access article groups.)
  - [ ] Admins may back up and restore articles, groups of articles, and special access groups of articles.
  -  [ ] Admin may add, view, and remove students, instructors, and admins from general groups. (See the limitation above that requires at least one admin to have admin rights to every group or special access group.)

## Phase Three Deliverables 🎁

A GitHub repository must be established, and a private project in that repository must include all the code required to build and test the application.  All the members of the team, a specified member of the grading team, must be given access to the repository.  If a Grader must ask for access after the deadline, 5% will be deducted from the project grade.

A PDF document must be produced that covers the following items:
- Cover page (with an appropriate title and list of the members of the team): 5%
- Project overview (updated with change bars showing the updates): 10%
- Requirements (User stories with change bars showing any updates): 10%
- Architecture and major architectural components using UML (e.g., Use Case diagram) with explanatory text): 10%
- Design UML Diagrams (e.g., Class Responsibility Collaborator Diagram, Class Diagrams, Sequence Diagrams, State Diagrams, and State diagrams with explanatory text for those aspects): 10%
- Code (nicely formatted with internal documentation and reference to the UML models that explain how the design flowed to this code (e.g., HW #4): 25%
- Basic Testing, including automated testing, as shown in HW #5: 10%
- Two Screencasts (The first is a technical screencast that must show the code, and the code must be readable.  The second is aimed at all potential users and shows how the execution of the application and how each requirement is satisfied.): 10%
- URL to the project in GitHub: 5%
- A Credit Page is provided at the end of the PDF.  This sheet lists every member of the team and what they accomplished.  All team members must participate in support and contribute to the creation of all the above items, especially the code and test cases.  No entry is longer than six lines.  The entire team must agree to the contents of this credit page.
