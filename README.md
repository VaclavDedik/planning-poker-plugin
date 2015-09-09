# Planning Poker Plugin for JIRA

**Planning poker**, also called Scrum poker, is a consensus-based technique for estimating, mostly used to estimate effort or relative size of development goals in software development. In planning poker, members of the group make estimates by playing numbered cards face-down to the table, instead of speaking them aloud. The cards are revealed, and the estimates are then discussed. By hiding the figures in this way, the group can avoid the cognitive bias of anchoring, where the first number spoken aloud sets a precedent for subsequent estimates. See [wikipedia](https://en.wikipedia.org/wiki/Planning_poker) for more information.

This plugin is a simple open-source implementation of planning poker for [Atlassian Jira](https://www.atlassian.com/software/jira).

## How to Create a New Planning Poker Session

You can create a new session for every created issue in JIRA. To create a new session you have to view the issue you want to create a poker session for, click on "More" and then on "Create Poker Session". See the following image.

![Create Session](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/create_session_button.png)

After you click on the button, you will be prompted for the start date of the session, the end date of the session and the list of users you want to notify about the session:

![Create Session Dialog](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/create_session_dialog.png)

To enter the date, you can use the date picker. Note that Start and End date of the session are required. To select users you want to notify, you can click on "Select User(s)" to select one or more users one by one or click on "Select Group" to select a group of users. When you are finished click on "Create Session".

![Create Session Created](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/create_session_created.png)

You can see in the right bottom part of the image information about the newly created session.

## How to Vote

You can vote by first clicking on the "Select Action" button in the Planning Poker Session panel:

![Select Session Action](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/session_select_action.png)

then click on "Vote" in the dropdown menu to vote. When you click on the button, you will be prompted for your vote:

![Vote Dialog](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/vote.png)

You vote by clicking on one of the cards and clicking on the "Vote" button. When you do that, you will see the following changes:

![Vote Created](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/vote_created.png)

You can edit your vote any time when the session is active by clicking on the "Edit Vote" button in the drop down menu "Select Action".

## How to View Current Voters

You can view current voters (users that already voted) by clicking on the number on the "Votes" row:

![View Voters](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/view_voters.png)

## How to View the Results

View the results by clicking on the "View Votes" button in the drop down menu "Select Action". You can only do that after the session ended.

![View Votes](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/view_votes.png)

## How to List All Sessions

You can list all sessions by clicking on the button "Poker Sessions" in the top panel (next to button "Create Issue"). When you do that, you get redirected to this page:

![View Sessions](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/view_sessions.png)

## How to Create New Groups

You can create new groups when you are creating a new session by clicking on "Select Group" button. This window will pop up:

![View Groups](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/view_groups.png)

When you click on "Create new group", this dialog will pop up:

![Create New Group](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/create_group_dialog.png)

You fill in the name of the group and the users and click on "Create Group" to confirm creation of a new group.

## How to Configure Planning Poker Plugin

To configure this plugin, you have to be a jira administrator. If you are an administrator, Click on the cogwheel in the top right corner, then select "System" from the drop down menu. After that, find "Issue Features" category in the left panel and select "Planning Poker". you will be redirected to this page:

![Configuration](https://raw.githubusercontent.com/VaclavDedik/planning-poker-plugin/master/docs/images/configure_poker.png)