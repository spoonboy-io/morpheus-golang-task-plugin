# Golang Morpheus Task Plugin

### Write Morpheus automation tasks in Go.

Go must be installed on the appliance, and a `/tmp/gocache` folder created which is owned by `morpheus-app` user.

As of the plugin 0.12.5 framework and 5.5.0 this plugin can now pass output down in a workflow like native Morpheus tasks.

Only Standard Library modules are supported at this time.

Previous task output can be accessed in the plugin via a results map, the task code used as the key for example:

### Todo

- Figure out how to make the morpheus variable dictionary available to the Go code as well.
- Test, digesting key/val and JSON output from previous task.
- Helpers to use in teh Go script to present key/val and JSON output in the format required by Morpheus.

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

<img width="1135" alt="image" src="https://user-images.githubusercontent.com/7113347/168629276-f42ef563-fde2-47df-a4a7-ea7826efe12c.png">
