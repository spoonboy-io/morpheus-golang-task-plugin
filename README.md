# Golang Morpheus Task Plugin

Write Morpheus automation tasks in Go.

Go must be installed on the appliance, and a `/tmp/gocache` folder created which is owned by `morpheus-app` user

As of the plugin 0.12.5 framework and 5.5.0 this plugin can now pass output down in a workflow like native Morpheus tasks.

Only Standard Library modules are supported at this time.

Previous task output can be accessed in the plugin via a results map, the task code used as the key for example:

### Example to print version of Go installed on appliance
```Go
package main

import (
  "fmt"
  "runtime"
)

func main(){
  fmt.Printf("Go version: %s\n", runtime.Version())
}

// outputs
// Go version: go1.18.2

```
