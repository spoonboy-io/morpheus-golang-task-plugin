package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.*

/**
 * Example AbstractTaskService. Each method demonstrates building an example TaskConfig for the relevant task type
 */
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
		
		/*String fName = "/tmp/morpheus-log.txt"
		File cont = new File (fName)

		cont << opts.toString()

		cont << opts.morpheusResults.getResultMap()*/

		task.setResultType("value")


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


		// make morpheus vars available
		def taskJson = opts.taskConfig.encodeAsJson().toString().getBytes().encodeBase64()

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

		// run it
		def sout = new StringBuilder()
		def serr = new StringBuilder()
		def proc = "go run ${fName}".execute()
		proc.consumeProcessOutput(sout, serr)
		proc.waitForOrKill(5 * 1000)

		// clean up
		boolean deleted =  container.delete()  

		// put the result on result map <- this doesn't achieve the chaining hoped for
		if (opts.morpheusResults != null) {
			def add = opts.morpheusResults.setResult(task.code, sout.toString())
		
			// some temp logging
			String logName = "/tmp/morpheus-log-${ts.getTime()}.txt"
			File lg = new File (logName)

			def resOut = opts.morpheusResults.getResultMap()
			lg << resOut
		} 

		new TaskResult(
			success: true,
			data   : sout,
			output : sout,
			error : serr
		)
	
	}
	
}
