# TestShell
A simple implement of a light-weight simulation for process and resource management.
## Request enviroment
- JavaSE 12
- Nothing any more actually, but you can use the **Intellij Idea**.
## Setup
The program can only run at the following two modes:
 - **Standard Mode** which means the input is from **System.in**. When no command argument is specified, this mode is picked.
 - **Task Mode** which means the input is from files. when the command argument is not null, this mode is activated and the first argument is treated as the name of the target script file while others is ignored.  
## Debug 
  * If **Main.inTestMode** is set to true, command arguments will be ignored which means that no script file will be accepted and the program always stays at **Standard Mode**.
## Supported commands
  * ### init 
    This command **MUST** be invoked at the very beginning, if not, Who cares? I don't want to handle the shit situation.
    **Note**: when this is invoked, additional arguments are required to specified the total count of resources, by default, four positive integers should be given.
  * ### quit
    This comand **MUST** be invoked **ONLY** at the end of the program when it's at **Task Mode**, if not, well, Exception may arise.
  * ### cr `processName` `priority`
    Create a new process with a given name `processName` and a given priority `priority`, then insert it into the `ReadyList` and execute the `scheduler` task.
    if `processName` is already in use, well, I can only say `Observe the code yourself.`
  * ### de `processName`
    Delete the process named `processName`, release all given resources, trying wake up the blocked processes then execute the `scheduler` task. if not exists, `Observe the code yourself.`
  * ### req R`i` `count`
    Request the R`i` resource with a number of `count` for the current running process, if fails, the process will be blocked.
    **Note**: if `count` is larger than the total count, the program will exit with a code `1`.
  * ### list res
    List the resources' details.
  * ### list ready
    List all of the processes in `readyList`.
  * ### list block
    List all of the processes that are blocked.
  * ### pr `processName`
    Print the PCB of the process with the name `processName`, if not exists, `Observe the code yourself.`
  
