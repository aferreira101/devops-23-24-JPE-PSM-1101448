# Technical Report: Version Control with Git

###### Made by: [André Ferreira - 1101448]

## Table of Contents

- [Tutorial](#tutorial)
	- [Part 1 - Step 1: Repository Initialization and Initial Commit](#part-1---step-1-repository-initialization-and-initial-commit)
	- [Part 1 - Step 2: New Feature Development in the Main Branch](#part-1---step-2-new-feature-development-in-the-main-branch)
	- [Part 1 - Step 3: End assignment part 1 with a final tag](#part-1---step-3-end-assignment-part-1-with-a-final-tag)
	- [Part 2 - Step 4: New Feature Development in a New Branch (email-field)](#part-2---step-4-new-feature-development-in-a-new-branch-email-field)
	- [Part 2 - Step 5: New Branch for bug fixes (fix-invalid-email)](#part-2---step-5-new-branch-for-bug-fixes-fix-invalid-email)
	- [Part 2 - Step 6: End assignment part 2 with a final tag](#part-2---step-6-end-assignment-part-2-with-a-final-tag)
	- [Alternative Technical Solution for Version Control (Mercurial)](#alternative-technical-solution-for-version-control-mercurial)
	- [Advantages of Mercurial over Git:](#advantages-of-mercurial-over-git)
	- [Disadvantages of Mercurial over Git:](#disadvantages-of-mercurial-over-git)
	- [Implementation of the Alternative](#implementation-of-the-alternative)
	- [Conclusion](#conclusion)

### Tutorial

This section guides you through the steps taken to meet the assignment's requirements,
including the use of Git for version control, the development of new features, and the implementation of unit tests.
For every step of the tutorial, you will find the terminal commands used to perform the required actions.
For every step an issue was created and assigned to the developer.

#### Part 1 - Step 1: Repository Initialization and Initial Commit

Open your terminal and navigate to the directory where you want to create the new folder. Then, run the following
command to create the folder and clone the repository. In the CA1 folder, run the following command to initialize a new
Git repository, add all the files to the staging area, push the initial commit to the remote repository with the message
to close the #1 issue, and tag the commit as v1.1.0.:

- Terminal Command:
	- `mkdir CA1 && cd CA1`
	- `git clone <repository_url>`
	- `git init`
	- `git add .`
	- `git commit -m "Initial commit, closes #1"`
	- `git push -u origin main`
	- `git tag v1.0.0`
	- `git push origin --tags`

#### Part 1 - Step 2: New Feature Development in the Main Branch

In your Spring Boot application, locate the Employee entity class. Add a new field for jobYears and jobTitle with their
respective getters and setters. Then, create a new unit test to validate the new fields. Also update all the required
DataLoader classes to populate the new fields with random data and the controllers to expose the new fields
in the REST API. Add them to the staging area, commit them with a message to close the issue and finally, push the
changes to the remote repository and tag the commit as v1.2.0.:

- Terminal Command:
	- `git add .`
	- `git commit -m "Added new field to the application (jobYears and jobTitle) with tests, closes #2"`
	- `git push origin main`
	- `git tag v1.2.0`
	- `git push origin v1.2.0`

#### Part 1 - Step 3: End assignment part 1 with a final tag

Create the tag ca1-part1 with a message to close issue #3 and push it to the remote repository:

- Terminal Command:
	- `git tag -a ca1-part1 -m "Closes #3"`
	- `git push origin ca1-part1`

#### Part 2 - Step 4: New Feature Development in a New Branch (email-field)

Create a new branch called email-field and add a new field for email to the Employee entity class. Then, create a new
unit test to validate the new field. Update and fix all related problems in the required classes. Next, add them to the
staging area, commit them with a message to close the issue #4, and finally, push the changes to the remote repository
and
tag the commit as v1.3.0.:

- Terminal Command:
	- `git branch email-field`
	- `git checkout email-field`
	- `git push -u origin email-field`
	- `git add .`
	- `git commit -m "Added email field and related unit tests. Closes #4"`
	- `git checkout main`
	- `git merge --no-ff email-field`
	- `git push origin main`
	- `git tag v1.3.0`
	- `git push origin v1.3.0`

#### Part 2 - Step 5: New Branch for bug fixes (fix-invalid-email)

Create a new branch called fix-invalid-email and fix the problem with the email field validation. Add the changes to the
staging area, commit them with a message to close the issue #5, and finally, push the changes to the remote repository
and tag the commit as v1.3.1.:

- Terminal Command:
	- `git branch fix-invalid-email`
	- `git checkout fix-invalid-email`
	- `git push -u origin fix-invalid-email`
	- `git add .`
	- `git commit -m "Fixed email field validation. Closes #5"`
	- `git checkout main`
	- `git merge --no-ff fix-invalid-email`
	- `git push origin main`
	- `git tag v1.3.1`
	- `git push origin v1.3.1`

#### Part 2 - Step 6: End assignment part 2 with a final tag

Create the tag ca1-part2 with a message to close issue #6 and push it to the remote repository:

- Terminal Command:
	- `git tag -a ca1-part2 -m "Closes #6"`
	- `git push origin ca1-part2`

### Alternative Technical Solution for Version Control (Mercurial)

#### Advantages of Mercurial over Git:

1. **Simplicity and Ease of Use**: Mercurial is designed to be simpler and more intuitive than Git, making it easier for
   beginners to learn and use. Its command-line interface is more straightforward, which can lead to a smoother
   onboarding process for new users.

2. **Performance**: Mercurial is generally faster than Git for many operations, especially on Windows. This can lead to
   a more responsive user experience, especially in environments where performance is critical.

3. **Distributed Version Control**: Like Git, Mercurial supports distributed version control, allowing multiple
   developers to work on the same project simultaneously without needing a central server. This feature is crucial for
   modern development practices, enabling efficient collaboration and workflows.

#### Disadvantages of Mercurial over Git:

1. **Feature Set**: Git offers a more extensive feature set compared to Mercurial, including advanced features like
   submodules, rebase, and more complex branching strategies. For projects requiring these advanced features, Git might
   be a better choice.

2. **Community and Ecosystem**: Git has a larger community and a more extensive ecosystem of tools and integrations.
   This means that for many tasks, there are more resources, tutorials, and third-party tools available for Git than for
   Mercurial.

3. **Scalability**: Git is designed with scalability in mind, making it a better choice for very large projects or
   repositories. While Mercurial is efficient for many tasks, Git's architecture is optimized for handling large-scale
   projects more effectively.

#### Implementation of the Alternative

Mercurial has a similar set of commands to Git for version control operations.
Here are some examples:

- **Creating a New Repository**:
  use the  `hg init`  command, similar to  `git init`  in Git.

- **Adding Files to the Repository**:
  use the  `hg add`  command, similar to  `git add`.

- **Committing Changes**:
  use the  `hg commit`  command, similar to  `git commit`.

- **Creating Branches**:
  use the  `hg branch`  command, similar to  `git branch`.

- **Merging Branches**:
  use the  `hg merge`  command, similar to  `git merge`.

- **Pushing and Pulling Changes**:
  use the  `hg push`  command, similar to  `git push`. And use the  `hg pull`  command, similar to  `git pull`.

- **Tagging Releases**:
  use the  `hg tag`  command, similar to  `git tag`.

### Conclusion

Mercurial offers a simpler and more intuitive alternative to Git for version control. It's designed to be easier to use
and understand, especially for beginners, while still providing the essential features needed for version control.
However, for more complex projects or those requiring advanced features, Git's extensive feature set and flexibility
might be more suitable.

# END OF README

```