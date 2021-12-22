# Golang Morpheus Task Plugin

Write Morpheus Automation tasks in Go

- Results of previous tasks are in a map (var results) and accessible by key
- Output is not available to subsequent tasks ATM
- Go must be installed on the appliance
- Scripts are excuted in using `go run`
- Only Standard Libary packages work in the current implementation

## TODO
- Make the task output available to subsequent tasks in yhe workflow
- Make the Morpheus vars available to the task
- A Github Workflow to build the plugin
