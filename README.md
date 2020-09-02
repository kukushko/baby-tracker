# baby-tracker

Baby maintenance tool (website).

Prerequisites - MySQL database. Provide connection details in application.properties. Schema and defaults will be created automatically.

Tool features:
1. Tracking of "baby inputs" - feeding. Mom presses "Start" and "Stop" to track this. If feeding time exceeds 60 minutes, system will automatically 
   assume feeding has been completed and will stop it for you.
2. Tracking of "vigantol" supply on daily basis. If baby did not get vigantol warning message is displayed on the main page.
3. Tracking of "baby outputs" - pees+poops. Once baby outputs, weigh the goods and provide results to the system. Note that weight includes 
   both pampers weight and goods weight. Provide total weight, system will subtract pampers weight automatically (see setting "Pampers Weight").
4. Tracking of temperature on daily basis. If temperature measure was not done, warning message is displayed on the main page.
5. Output daily report. Useful to asses how much food baby eats.
6. Pampers counter. Each time baby's output registered, pampers counter is decreased. Once it becomes less than 10, warning on main page is displayed.
