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

### The Fourth Phase 4️⃣

The fourth phase refines the requirements, architecture, design, code (and internal documentation), and test suites. The goal is to make it clear that they align and flow gracefully from one to another.  Feedback from the previous phases must be addressed.  Additional functions and user interface elements must be provided, as needed, to convince potential users about the value and ease of use that has been designed into the system, carefully implemented, and thoroughly tested.

Any issues identified in your previous submissions must be addressed and resolved.  There are no new functional requirements for this phase.

Two screencasts must be provided.  The first is a technical screencast.  It explains the flow from the requirements through to the working application.  The second is a how-to-use screencast.  It is aimed at three groups: students, admins, and instructional team members.

## Phase One Requirements ✅
## Phase Two Requirements  ✅
## Phase Three Requirements  ✅
## Phase Four Requirements (In Progress)

- [ ] JUnit Testing
- [ ] User Interface
- [ ] Previous Feedbacks:
  - [ ] Automated testing as done in HW#5 of all non-user-interface classes. (E.g., Do not try to test GUI or command line classes). 

## Phase 4 Deliverables 🎁

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
