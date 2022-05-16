package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.*

class GolangTaskService extends AbstractTaskService {
	MorpheusContext context

	GolangTaskService(MorpheusContext context) {
		this.context = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return context
	}

	@Override
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildLocalTaskConfig([:], task, [], opts).blockingGet()
		executeTask(task, config, opts)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		return new TaskResult()
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		return new TaskResult()
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		return new TaskResult()
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		return new TaskResult()
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		return new TaskResult()
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance) {
		return new TaskResult()
	}

	TaskResult executeTask(Task task, TaskConfig config, Map opts) {
		
		// get the script
		def taskOption = task.taskOptions.find { it.optionType.code == 'golangTaskScript' }
		String data = taskOption.value

		// make result map available
		String codeHeader = """
			var results = map[string]string{}
			var morpheus = map[string]string{}
			
			// we use init to populate the maps
			func init(){

		"""

		// check we have any 
		if (opts.morpheusResults != null) {
			Map inputResults = opts.morpheusResults.getResultMap()

			inputResults.each { key, val ->
				String trimmed = val
				trimmed = trimmed.replace("\r","").replace("\n","")
				codeHeader += "results[\"$key\"]= \"$trimmed\"" + "\r\n"
			}		
		}

		codeHeader += """
			
			}

			func main(){
		"""
	
		// replace
		def repData = data.replace("func main(){", codeHeader)
	
		// create the file
		Date ts = new Date();
		String fName = "/tmp/morpheus-task-${ts.getTime()}.go"
		File container = new File (fName)

		container << repData
		
		def sout = new StringBuilder()
		def serr = new StringBuilder()
		ProcessBuilder pb = new ProcessBuilder("/usr/local/go/bin/go", "run", "${fName}");
		// set env var for morpheus-app user
		Map<String, String> env = pb.environment();
 		env.put("GOCACHE", "/tmp/gocache");
		
		Process p = pb.start();
		p.consumeProcessOutput(sout, serr)
		p.waitForOrKill(5 * 1000)

		// clean up
		boolean deleted =  container.delete()  

		new TaskResult(
			success: true,
			data   : sout,
			output : sout,
			error : serr
		)
	
	}
	
}
